package com.likelion.beshop.service;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.CartItem;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
