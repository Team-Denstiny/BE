package com.example.jwt;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.user.UserEntity;
import com.example.user.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // Authrization 헤더 검증
        if (authorization  == null || !authorization.startsWith("Bearer ")){
            log.info("token null");
            filterChain.doFilter(request,response);

            // 조건이 해당되면 메서드 종료
            return;
        }
        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)){
            log.info("token expired");
            filterChain.doFilter(request,response);

            return;
        }

        //토큰에서 username과 role 획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);


        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password("temppassword")
                .role(UserRole.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
