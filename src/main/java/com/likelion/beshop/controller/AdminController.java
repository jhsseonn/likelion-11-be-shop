package com.likelion.beshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPage() {
        // 관리자 페이지로 이동하는 뷰 이름을 반환합니다.
        return "admin";
    }
}
