package com.example.jwt;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.user.UserEntity;
import com.example.user.enums.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    public final static String HEADER_AUTHORIZATION = "Authorization";
    public final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HEADER_AUTHORIZATION);

        // access키에 담긴 토큰 꺼내기
        if (authorization  == null || !authorization.startsWith(TOKEN_PREFIX)){
            log.info("token null");
            filterChain.doFilter(request,response);

            // 조건이 해당되면 메서드 종료
            return;
        }

        String accessToken = getAccessToken(authorization);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            // 이 부분에서 Front end 개발자와 [협의된 응답]을 주어야 한다
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access 인지 확인(발급 페이로드 확인)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")){
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰에서 username과 role 획득
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

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

    private String getAccessToken(String authorization){
        if (authorization != null && authorization.startsWith(TOKEN_PREFIX)){
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
