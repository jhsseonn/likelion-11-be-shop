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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
}
