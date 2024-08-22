package com.example.oauth2.handler;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.jwt.JWTUtil;
import com.example.oauth2.dto.CustomOAuth2User;
import com.example.refresh.RefreshEntity;
import com.example.refresh.RefreshRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;

import com.example.user.UserRepository;
import error.ErrorCode;
import exception.ApiException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

        CustomOAuth2User userDetails = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String resourceId = userDetails.getResourceId();

        // 하이퍼 링크 방식 -> redirect , header방식으로 보내주면 front측에서 받을 수 없음 (일단 쿠키로 보내기)
        String accessToken = jwtUtil.createJwt("access", resourceId, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", resourceId,role,86400000L );

        // refresh entity에 저장
        addRefreshEntity(resourceId, refreshToken,86400000L);

        response.addCookie(createCookie("access", accessToken));
        response.addCookie(createCookie("refresh", refreshToken));

        /// ==> ++ 여기에 id 추가해서 보내달라.
        response.setStatus(HttpServletResponse.SC_OK);
        // redirect
        response.sendRedirect("http://localhost:5173/signin/endpoint");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String resourceId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .resourceId(resourceId)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
