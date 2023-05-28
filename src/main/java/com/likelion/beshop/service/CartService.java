package com.likelion.beshop.service;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.entity.*;
import com.likelion.beshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private  final ItemRepository itemRepository;
    private  final MemberRepository memberRepository;
    private  final CartRepository cartRepository;
    private  final CartItemRepository cartItemRepository;
    private  final OrderService orderService;

    // cartItemDto, email을 매개변수로 받아 장바구니에 상품 담는 로직
    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId()) //cartItemDto로부터 상품 아이디를 받아 상품 엔티티 조회
                .orElseThrow(EntityNotFoundException::new); //예외처리

        Member member = memberRepository.findByEmail(email); // email로 회원 조회
        Cart cart = cartRepository.findByMemberId(member.getId()); // 회원 엔티티로부터 아이디 받아 장바구니 조회

        // 장바구니가 없을 경우 장바구니 생성 및 db에 저장
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 장바구니와 상품 엔티티로부터 아이디 받아 장바구니 아이디와 상품 아이디로 장바구니 상품 조회
        CartItem saveCartItem = cartItemRepository.findByCartCodeAndItemId(cart.getCode(), item.getId());

        // 저장된 장바구니 상품이 있으면 cartItemDto로부터 수량 받아서
        if(saveCartItem != null){
            saveCartItem.addCount(cartItemDto.getCount()); // 저장된 장바구니 상품 수량 더해주고
            return saveCartItem.getCode(); // 저장된 장바구니 상품 아이디 리턴해주기
        }else{ // 저장된 장바구니 상품이 없을 경우
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount()); // 장바구니 상품 생성 후
            cartItemRepository.save(cartItem); // db에 저장하고
            return cartItem.getCode(); // 장바구니 상품 아이디 리턴
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>(); // cartDetailDtoList 새로 생성

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId()); // 파라미터로 받은 email로 멤버 아이디 검색해 장바구니 조회

        // 장바구니가 없다면 cartDetailDtoList 반환
        if(cart == null){
            return cartDetailDtoList;
        }

        // cartDetailDtoList에 장바구니 아이디 매개변수로 해 cartDetailDtoList 조회해 값 넘겨주기
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getCode());

        return cartDetailDtoList; // 최종적으로 cartDetailDtoList 반환
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = memberRepository.findByEmail(email); // email로 회원 정보 찾아 현재 로그인 한 회원 객체 생성해줌
        // 장바구니 상품 아이디로 장바구니 상품 찾아 새로운 장바구니 상품 객체로 넘겨줌(예외 처리하기)
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember(); //  장바구니 상품 객체에서 해당 상품이 속한 장바구니를 소유한 회원을 찾아 savedMember 회원 객체로 넘겨줌

        // 현재 로그인한 회원의 이메일과 장바구니를 소유한 회원의 이메일이 다른 경우 false 리턴, 같으면 true 리턴해줌
        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){

        // cartItemId로 cartItem 찾아 새로운 객체에 할당(예외처리 확실히 해주기)
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 할당된 객체 updateCount 메소드 호출
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        // cartItemId를 받아 아이디로 cartItem 검색 및 호출해 새로운 cartItem 객체에 넣기(예외처리 확실히)
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem); // 해당 cartItem 객체 삭제(cartItemRepository 메서드 사용)
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>(); // orderDtoList 새로 생성

        // cartOrderDtoList 돌면서 cartOrderDto들 CartItemId 가져와 cartItem 조회(예외처리해주기)
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            // 새로운 orderDto 생성해 orderDto에 ItemId와 count set해주고 orderDtoList에 추가
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getNum());
            orderDtoList.add(orderDto);
        }

        // for문 다 돌았으면 orderService의 orders 메서드로 orderId 반환 받아 새로운 변수 orderId로 넘겨주기
        Long orderId = orderService.orders(orderDtoList, email);

        //  새로운 orderDto에 저장하는 과정 끝났으면 cartOrderDto 장바구니에서 삭제하는 로직 추가
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId; // 최종적으로 orderId 반환
    }
}
