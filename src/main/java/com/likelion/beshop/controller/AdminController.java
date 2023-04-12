package com.likelion.beshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {
    @GetMapping(value="/")
    public String admin() {
        return "admin/adminPage";
    }

}
