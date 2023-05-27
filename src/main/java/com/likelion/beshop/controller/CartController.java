package com.likelion.beshop.controller;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    // 장바구니 담기
    @ResponseBody
    @PostMapping(value="/cart")
    public ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                                BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 조회
    @GetMapping(value="/cart")
    public String orderHist(Principal principal, Model model) {
        String email = principal.getName();

        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(email);

        model.addAttribute("cartItems", cartDetailDtoList);

        return "cart/cartList";
    }

    // 장바구니 상품 수량 수정
    @ResponseBody
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {

        System.out.println("controller");
        // 수량이 0개 이하라면
        if (count <= 0) {
            // 수량 부족 메시지
            System.out.println("111");
            return new ResponseEntity<String>("장바구니 수량은 1개 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
        // 로그인한 유저와 장바구니 담은 유저가 다르다면
        else if (cartService.validateCartItem(cartItemId, principal.getName()) == false) {
            // 권한 없음 메시지
            System.out.println("222");
            return new ResponseEntity<String>("장바구니 수정 권한이 없습니다.", HttpStatus.FORBIDDEN);

        }

        System.out.println(cartItemId.toString());
        // 장바구니 수량 수정
        cartService.updateCartItemCount(cartItemId, count);

        // 장바구니 상품 아이템 아이디 리턴
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

    }

    // 장바구니 상품 삭제
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        if (cartService.validateCartItem(cartItemId, principal.getName()) == false) {
            return new ResponseEntity<String>("장바구니 상품 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 상품들 주문
    @ResponseBody
    @PostMapping(value = "/cart/orders")
    public ResponseEntity orderCartItem (@RequestBody CartOrderDto cartOrderDto, Principal principal) {
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!(cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName()))) {
                return new ResponseEntity<String>("장바구니 주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
