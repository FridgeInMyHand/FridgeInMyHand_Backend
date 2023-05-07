package com.fridgeInMyHand.fridgeInMyHandBackend.member.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    //기본 요청
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
