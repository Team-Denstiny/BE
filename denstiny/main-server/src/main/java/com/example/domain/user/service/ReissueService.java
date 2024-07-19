package com.example.domain.user.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {

    public String getRefreshToken(HttpServletRequest request){

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("refresh")){

                refresh = cookie.getValue();
            }
        }
        return refresh;
    }
}
