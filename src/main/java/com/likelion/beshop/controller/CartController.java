package com.likelion.beshop.controller;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
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

import javax.validation.Valid;
import java.security.Principal;
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

}
