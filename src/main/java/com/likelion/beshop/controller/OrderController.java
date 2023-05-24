package com.likelion.beshop.controller;


import com.likelion.beshop.dto_.OrderDto;
import com.likelion.beshop.dto_.OrderHistDto;
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
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); //만약 에러가 있다면, 에러 정보를 ReponseEntity 객체에 담아서 반환
        }

        String email = principal.getName(); //현재 회원의 정보의 이메일 받아오기(이름 받아오는 것)
        Long orderId;

        try { //화면에서 받아온 주문 정보와 회원 이메일 정보로 주문
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK); //결과값으로 생성된 주문 번호와 요청이 성공했다는 http 응답 상태 코드 반환
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){ //매개변수 ->페이징 처리 고려, 회원 정보 불러오기 고려, Model 객체
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,4); //pageable 객체를 생성할 때에 한번에 가져올 줌누 개수는 4로 설정
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(),pageable); //현재 회원의 이메일과 페이징 객체를 파라미터로 주문 목록 조회

        model.addAttribute("orders",orderHistDtoList); //model에 주문 목록 리스트를 orders라는 이름으로 담아서 전달
        model.addAttribute("page",pageable.getPageNumber());//model에 현재 페이지 번호를 page라는 이름으로 담아서 전달
        model.addAttribute("maxPage",5);//model에 최대 페이지 수인 5를 maxPage라는 이름으로 담아서 전달

        return "order/orderHist"; //템플릿 반환
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){
        if(!orderService.validateOrder( orderId,principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
