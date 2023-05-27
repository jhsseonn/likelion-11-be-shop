package com.likelion.beshop.controller;

import com.likelion.beshop.dto_.CartDetailDto;
import com.likelion.beshop.dto_.CartItemDto;

import com.likelion.beshop.dto_.CartOrderDto;
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
import java.nio.channels.Pipe;
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

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("CartItemId")Long cartItemId, int count, Principal principal){
        System.out.println(1);
        if(count<=0) {
            System.out.println(2);
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);

        }
        else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            System.out.println(3);
            return new ResponseEntity<String>("수정 권한이 없습니다",HttpStatus.FORBIDDEN);
        }
        //count가 0보다 작거나 같은 경우 새로운 ResponseEntity 호출(수량 부족 메세지, httpStatus bad_request 반환
        //cartservice의 validateCartIem으로 로그인한 회원과 cartItem의 소윤 회원이 같지 않은 경우 새로운 responseEntity 호출

        System.out.println(4);
        cartService.updateCartItemCount(cartItemId,count); //장바구니 수량 수정하기
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK); //최종적으로 새로운 responseentity 호출
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId")Long cartItemId, Principal principal){
        if(!cartService.validateCartItem(cartItemId,principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다",HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);
    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDto == null || cartOrderDtoList.size()==0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요",HttpStatus.BAD_REQUEST);
        }

        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(),principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다",HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList,principal.getName());
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
