package com.likelion.beshop.controller;

import com.likelion.beshop.dto_.CartDetailDto;
import com.likelion.beshop.dto_.CartItemDto;

import com.likelion.beshop.service.CartService;
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

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) { //비동기 처리로 order 객체를 http요청의 body로 전달

        if (bindingResult.hasErrors()) { //bindingResult에서 오류가 생겼을 경우
            StringBuilder sb = new StringBuilder(); //새로운 StringBuilder 객체 생성 후 bindingResult의 fieldError들을 가져와 리스트 형태로 저장
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()); //리스트를 돌면서 에러 메시지들 StringBuilder 객체에 저장후
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); //ResponseEntity 형태로 StringBuilder 객체  String으로 반환하고 HTTPstatuss는 BAD_REQUEST으로 반환
        }
        String email = principal.getName(); //principal에서 이메일 받아오기
        Long cartItemId; //cartitemid 필드 만들기

        try {
            System.out.println(cartItemDto.getItemId());
            System.out.println(email);
            cartItemId = cartService.addCart(cartItemDto, email);
            //cartiemdto와 email로 장바구니 상품 장바구니에 담고 cartiemid 필드로 값 넘겨주기
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);//responseentity 형태로 오류 메시지 및 httpstatus badREquest로 반환
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); //위 코드들이 모두 정상적으로 돌아갔을 경우 responseentity 형태로 cartiemid 및 httpstatus ok로 반환
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model) {
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailDtoList);
        return "cart/cartList";
    }
}
