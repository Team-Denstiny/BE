package com.example.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/main") // 테스트용
    public String adminP(){
        return "main controller";
    }
}
