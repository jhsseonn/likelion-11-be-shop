package com.likelion.beshop.controller;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.service.CartService;
import com.likelion.beshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    @PostMapping("/cart")  // 비동기 처리로 order 객체를 http 요청의 body로 전달 / cartItemDto로 넘김(Valid로 유효성 검증)
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){ //  BindingResult에서 오류가 생겼을 경우
            StringBuilder sb = new StringBuilder(); // 새로운 StringBuilder 객체 생성 후
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // bindingResult의 FieldError들을 가져와 리스트 형태로 저장

            // 리스트 돌면서 에러메시지들 StringBuilder 객체에 저장 후
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            // ResponseEntity 형태로 StringBuilder 객체 String으로 반환하고 HttpStatus는 BAD_REQUEST로 반환
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // principal에서 이메일 받아오기
        Long cartItemId; // cartItemId 필드 만들기

        // try-catch문으로 예외처리
        try{ // cartItemDto와 email로 장바구니 상품 장바구니에 담고 cartItemId 필드로 값 넘겨주기
            cartItemId = cartService.addCart(cartItemDto, email);
        }catch (Exception e){ //ResponseEntity 형태로 오류 메시지 및 HttpStatus BAD_REQUEST로 반환
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 위 코드들이 모두 정상적으로 돌아갔을 경우 ResponseEntity 형태로 cartItemId 및 HttpStatus OK로 반환
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model) {
        // principal로 이메일 받아서 장바구니 리스트 조회해 cartDetailList로 값 넘겨주기
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailList); // cartDetailList cartItems로 뷰에 전달
        return "cart/cartList";
    }
}
