package com.likelion.beshop.service;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.entity.*;
import com.likelion.beshop.repository.CartItemRepository;
import com.likelion.beshop.repository.CartRepository;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
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

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    private final OrderService orderService;

    public Long addCart(CartItemDto cartItemDto, String email){

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        //장바구니가 존재하지 않는다면 생성
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }


        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem savedcartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        //해당 상품이 장바구니에 존재하지 않는다면 생성 후 추가
//        if(savedCartItem != null) {
//            //장바구니 수량 증가
//            savedCartItem.addCount(cartItemDto.getCount());
//            return savedCartItem.getId();
//        }
//        else{   //상품이 없다면
//            //해당 상품을 장바구니 상품으로 등록
//            CartItem cartItem= CartItem.createCartItem(cart, item, cartItemDto.getCount());
//            cartItemRepository.save(cartItem);
//            return cartItem.getId();
//            //해당 상품이 장바구니에 이미 존재한다면 수량을 증가
//
//        }//return savedCartItem.getId();

        if(savedcartItem == null){
            savedcartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(savedcartItem);

            //해당 상품이 장바구니에 이미 존재한다면 수량을 증가

        }else{

            savedcartItem.addCount(cartItemDto.getCount());
        }
        return savedcartItem.getId();

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

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member curMember = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId,int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList,email);

        for(CartOrderDto cartOrderDto : cartOrderDtoList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;

    }

}
