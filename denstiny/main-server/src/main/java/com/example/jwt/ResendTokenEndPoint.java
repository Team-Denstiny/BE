package com.example.jwt;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class ResendTokenEndPoint {


    @GetMapping("/endpoint")
    public Map<String, String> handleRequest(@RequestHeader Map<String, String> headers) {
        // 로그에 요청 헤더를 출력합니다.
        System.out.println("Request received at /login/endpoint");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 간단한 응답을 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Request received and headers logged.");
        return response;
    }

    /* 
    @GetMapping("/endpoint")
     public Map<String, String> getCookieData(@RequestHeader(value = "Cookie", required = false) String cookiesHeader) {
        Map<String, String> response = new HashMap<>();
        System.out.println("Get into!!!");
        if (cookiesHeader != null && cookiesHeader.contains("access")) {
            String[] cookies = cookiesHeader.split(";");
            for (String cookie : cookies) {
                if (cookie.trim().startsWith("access")) {
                    String cookieValue = cookie.split("=")[1];
                    System.out.println(cookieValue);
                    response.put("cookieData", cookieValue);
                }
            }
        }
        return response;
    }
        */
}
