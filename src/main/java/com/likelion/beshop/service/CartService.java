package com.likelion.beshop.service;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.CartItem;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.repository.CartItemRepository;
import com.likelion.beshop.repository.CartRepository;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    //
    public Long addCart(CartItemDto cartItemDto, String email) {
        // dto로 입력받은 상품 아이디를 통해 아이템 조회
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        // 이메일을 통해 멤버 조회
        Member member = memberRepository.findByEmail(email);

        // 멤버 아이디를 통해 장바구니 조회
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니가 존재하지 않는다면
        if (cart == null) {
            cart = Cart.createCart(member); // 해당 멤버의 장바구니 생성
            cartRepository.save(cart); // db에 저장
        }

        // 상품 아이디와 카트 아이디를 통해 해당 카트에 파라미터의 상품이 존재하는지 조회
        CartItem savedCartitem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 카트에 상품이 있다면
        if (savedCartitem != null) {
            // 장바구니 수량 증가
            savedCartitem.addCount(cartItemDto.getCount());
        }
        else { // 상품이 없다면
            // 해당 상품을 장바구니 상품으로 등록
            savedCartitem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            // db에 저장
            cartItemRepository.save(savedCartitem);
        }
        // 장바구니 상품 아이디 리턴
        return savedCartitem.getId();
    }

    // 장바구니 조회
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    // 장바구니 상품 담은 유저와 현재 로그인한 유저 일치 검증 메소드
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        // 현재 로그인한 멤버
        Member curMember = memberRepository.findByEmail(email);
        // 장바구니 상품 아이디 받아와 객체 생성
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        // 해당 장바구니 상품을 소유한 멤버
        Member savedMember = cartItem.getCart().getMember();

        // 로그인한 멤버와 장바구니 상품을 담은 멤버가 일치하지 않는다면
        if (!(curMember.getEmail() == savedMember.getEmail())) {
            return false;
        }

        return true;
    }

    // CartItem 엔티티의 수량 수정 메소드 호출
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    // 장바구니 상품 삭제
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    // 장바구니에 담긴 상품 주문
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        // 주문 dto 리스트 생성
        List<OrderDto> orderDtoList = new ArrayList();

        // 장바구니 주문 dto 돌며
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 장바구니 주문 dto의 장바구니 상품 id를 통해 장바구니 상품 조회
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            // 주문 dto 생성 후 장바구니 상품과 수량으로 주문 필드 세팅
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());

            // 주문 dto 리스트에 주문 추가
            orderDtoList.add(orderDto);
        }

        // 주문 dto 리스트와 사용자 이메일로 해당 사용자의 주문 생성
        Long orderId = orderService.orders(orderDtoList, email);

        // 장바구니 주문 dto 리스트 돌며
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // 주문한 장바구니의 장바구니 상품 id를 통해 장바구니 상품 생성
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            // 주문이 끝났으니 장바구니에서 해당 장바구니 상품 삭제
            cartItemRepository.delete(cartItem);
        }

        // 주문 아이디 리턴
        return orderId;
    }
}
