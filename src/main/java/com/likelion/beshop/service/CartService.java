package com.likelion.beshop.service;

import com.likelion.beshop.dto_.CartDetailDto;
import com.likelion.beshop.dto_.CartItemDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.CartItem;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.repository.CartItemRepository;
import com.likelion.beshop.repository.CartRepository;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
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

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) { //장바구니에 상품 담는 뢰직


        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); //이메일로 회원 조회
        Cart cart = cartRepository.findByMemberId(member.getId()); //회원 엔티티로부터도 아이디 받아 장바구니 조회


        if (cart == null) { //장바구니가 없을 경우 장바구니 생성 및 db에 저장
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        //장바구니와 상품 엔티티로부터 아이디 받아 장바구니 아이디와 상품 아이디로 장바구니 상품 조회

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());//저장된 장바구니 상품이 있으면 cartIemDto로부터 수량 받아서 저장된 장바구니 상품 수량 더해줌

        } else {
            savedCartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(savedCartItem); //저장된 장바구니 상품 없으면 장바구니 상품 생성 후 db에 저장
        }
        return savedCartItem.getId(); //저장된 장바구니 상품 아이디 리턴
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return  cartDetailDtoList;
    }
}
