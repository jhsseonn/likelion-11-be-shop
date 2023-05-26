package com.likelion.beshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    // /admin/ 접속
    @GetMapping(value="/")
    public String adminMain(Model model){
        return "/admin/adminMain"; // adminMain.html
    }


}

