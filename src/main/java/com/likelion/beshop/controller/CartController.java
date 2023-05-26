package com.likelion.beshop.controller;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.entity.CartItem;
import com.likelion.beshop.repository.CartItemRepository;
import com.likelion.beshop.repository.CartRepository;
import com.likelion.beshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @PostMapping(value="/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult,
                                              Principal principal) throws Exception{

        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value="/cart")
    public String orderHist(Principal principal, Model model){
        String email = principal.getName();
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(email);
        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }

    @PatchMapping(value="/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId")Long cartItemId, int count, Principal principal){
        if (count <= 0){
            return new ResponseEntity<String>("수량이 부족합니다.", HttpStatus.BAD_REQUEST);
        }
        else if (!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity<String>("권한 없음", HttpStatus.BAD_REQUEST);
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId")Long cartItemId, Principal principal){
        if (!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("권한 없음", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @PostMapping(value="/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }
        for (CartOrderDto cartOrder : cartOrderDtoList){
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName()))
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
