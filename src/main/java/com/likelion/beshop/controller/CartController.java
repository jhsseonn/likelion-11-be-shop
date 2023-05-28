package com.likelion.beshop.controller;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.dto.CartItemDto;
import com.likelion.beshop.dto.CartOrderDto;
import com.likelion.beshop.service.CartService;
import com.likelion.beshop.service.ItemService;
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

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        // if-elseif문 사용
        if(count <= 0){ // count가 0보다 작거나 같은 경우
            // 새로운 ResponseEntity 호출(수량 부족 메시지, HttpStatus BAD_REQUEST 반환)
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST); // BAD_REQUEST는 서버가 클라이언트측 오류 또는 잘못된 구문으로 인해 클라이언트의 요청을 처리할 수 없음
        } // cartService의 validateCartItem으로 로그인한 회원과 cartItem의 소유 회원이 같지 않은 경우
        else if(!cartService.validateCartItem(cartItemId, principal.getName())){ // 새로운 ResponseEntity 호출(권한 없음 메시지, HttpStatus FORBIDDEN 반환)
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN); // FORBIDDEN는 클라이언트가 요청된 리소스에 액세스할 수 있는 권한이 없음
        }
        cartService.updateCartItemCount(cartItemId, count); // 장바구니 상품 수량 수정하기(cartService 메서드 쓰기)
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 최종적으로 새로운 ResponseEntity 호출(cartItemId, HttpStatus OK 반환)
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if (!cartService.validateCartItem(cartItemId, principal.getName())){ // cartItemId와 현재 로그인한 유저의 아이디가 같지 않으면
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN); // 새로운 ResponseEntity 만들고 수정 권한 없음 메시지와 HttpStatus FORBIDDEN으로 반환
        }
        cartService.deleteCartItem(cartItemId); // cartService 메서드 이용해 장바구니 상품 삭제
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK); // 최종적으로 새로운 ResponseEntity 호출(cartItemId, HttpStatus OK 반환)
    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        // cartOrderDto로부터 cartOrderDtoList 넘겨받아 새로운 cartOrderDtoList에 추가
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){ //cartOrderDtoList가 null이거나 cartOrderDtoList 크기가 0일 경우
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN); // 새로운ResponseEntity 생성 후 주문 상품 선택하라는 메시지와 HttpStatus FORBIDDEN 반환
        }

        // cartOrderDtoList 돌면서 cartOrder로부터 cartItemId 받아 현재 로그인한 유저 아이디와 같은지 유효성 검사(cartService 메서드 이용)
        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN); // 유효하지 않을 경우 주문 권한이 없다는 메시지와 HttpStatus FORBIDDEN 반환
            }
        }

        // cartService 메서드 이용해 orderCartItem의 orderId 받아와 새로운 ResponseEntity객체 반환해주기(orderId, HttpStatus OK 반환)
        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
