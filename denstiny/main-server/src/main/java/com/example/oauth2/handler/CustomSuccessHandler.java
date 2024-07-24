package com.example.oauth2.handler;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.jwt.JWTUtil;
import com.example.oauth2.dto.CustomOAuth2User;
import com.example.refresh.RefreshEntity;
import com.example.refresh.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();


        // TODO login성공시 redirect로 body에 담아서 전달
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 하이퍼 링크 방식 -> redirect , header방식으로 보내주면 front측에서 받을 수 없음 (일단 쿠키로 보내기)
        String accessToken = jwtUtil.createJwt("access", email, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", email,role,86400000L );

        // refresh entity에 저장
        addRefreshEntity(email, refreshToken,86400000L);

        response.addCookie(createCookie("access", accessToken));
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpServletResponse.SC_OK);
        // redirect
        response.sendRedirect("http://localhost:3000/");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .email(email)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
