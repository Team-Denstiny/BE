package com.example.jwt;

import static com.example.jwt.JWTFilter.HEADER_AUTHORIZATION;
import static com.example.jwt.JWTFilter.TOKEN_PREFIX;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class ResendTokenEndPoint {

    /*
     * @GetMapping("/endpoint")
     * public Map<String, String> handleRequest(@RequestHeader Map<String, String>
     * headers) {
     * // 로그에 요청 헤더를 출력합니다.
     * System.out.println("Request received at /login/endpoint");
     * for (Map.Entry<String, String> entry : headers.entrySet()) {
     * System.out.println(entry.getKey() + ": " + entry.getValue());
     * }
     * 
     * // 간단한 응답을 반환합니다.
     * Map<String, String> response = new HashMap<>();
     * response.put("message", "Request received and headers logged.");
     * return response;
     * }
     */
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @GetMapping("/endpoint")
    public ResponseEntity<Map<String, String>> getCookieData(HttpServletRequest request) {
        System.out.println("Get into!!!");

        Cookie[] cookies = request.getCookies();
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> response = new HashMap<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    String cookieValue = cookie.getValue();
                    System.out.println(cookieValue);
                    headers.set(HEADER_AUTHORIZATION, TOKEN_PREFIX + cookieValue);
                    response.put("cookieData", cookieValue); // 응답에 cookieData 포함
                }
            }
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(response); // JSON 응답으로 반환
    }
}


