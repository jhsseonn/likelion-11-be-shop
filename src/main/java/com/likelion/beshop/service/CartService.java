package com.likelion.beshop.service;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
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

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;



    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(item, cart, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
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
        return cartDetailDtoList;
    }


}
