package com.likelion.beshop.controller;

import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.dto.OrderHistDto;
import com.likelion.beshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping(value = "/order")
    //스프링의 비동기 처리 어노테이션
    //@RequestBody : HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달
    //@ResponseBody : 자바 객체를 HTTP 요청의 body로 전달
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, //비동기 처리 // @Valid, 주문 정보를 받는 orderDto 검증 에러 확인
                                               BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){ //만약 에러가 있다면
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            // 에러 정보를 ResponseEntity 객체에 담아서 반환
            return  new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // 현재 회원의 정보의 이메일(=이름) 받아오기

        Long orderId;

        //try-catch문으로 화면에서 받아온 주문 정보와 회원 이메일 정보로 주문
        try {
            orderId = orderService.order(orderDto, email);
        }catch (Exception e){
            return  new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // 결과값으로 생성된 주문 번호와 요청이 성공했다는 Http 응답 상태 코드 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


    @GetMapping(value = {"/orders", "/orders/{page}"}) // 경로 = "/orders" or "/orders/{page}"
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){ // 매개변수 -> 페이징 처리 고려, 회원 정보 불러오기 고려, Model 객체

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4); // Pageable 객체를 생성할 때에 한 번에 가져올 주문 개수는 4로 설정
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable); // 현재 회원의 이메일과 페이징 객체를 파라미터로 주문 목록 조회

        model.addAttribute("orders", ordersHistDtoList); // Model에 주문 목록 리스트를 orders 라는 이름으로 담아서 전달
        model.addAttribute("page", pageable.getPageNumber()); // Model에 현재 페이지 번호를 page 라는 이름으로 담아서 전달
        model.addAttribute("maxPage", 5); // Model에 최대 페이지 수인 5를 maxPage라는 이름으로 담아서 전달

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    // 비동기 처리
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId , Principal principal){ // 매개변수 -> 경로 orderId 설정, 회원 정보 불러오기 고려

        // 자바스크립트에서 취소하고자 하는 주문 번호를 조작할 수 있으므로 권한 검사 메소드 호출
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN); // 만약, 권한이 없다면 ResponseEntity 객체에 "주문 취소 권한이 없습니다."와 Forbidden 상태코드 반환
        }

        orderService.cancelOrder(orderId); // 주문 취소 로직 호출
        return new ResponseEntity<Long>(orderId, HttpStatus.OK); // 취소한 주문 번호와 요청이 성공했다는 Http 응답 상태 코드 반환
    }
}
