package com.example.jwt;

import api.Result;
import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.error.TokenErrorCode;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.user.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.constant.TokenHeaderConstant.HEADER_AUTHORIZATION;
import static com.example.constant.TokenHeaderConstant.TOKEN_PREFIX;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HEADER_AUTHORIZATION);

        // access키에 담긴 토큰 꺼내기
        if (authorization  == null || !authorization.startsWith(TOKEN_PREFIX)){
//            log.info("토큰이 없거나 Bearer로 시작하지 않습니다.");
            filterChain.doFilter(request,response);

            // 조건이 해당되면 메서드 종료
            return;
        }


        String accessToken = getAccessToken(authorization);

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            // 만료된 토큰에 대한 응답 생성
            Result result = Result.ERROR(TokenErrorCode.EXPIRED_AT, "액세스 토큰이 만료되었습니다.");
            ResponseEntity<Result> responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");

            // 이 부분에서 Front end 개발자와 [협의된 응답]을 주어야 한다
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return;

        }

        // 토큰이 access 인지 확인(발급 페이로드 확인)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")){
            // 잘못된 토큰에 대한 응답 생성
            Result result = Result.ERROR(TokenErrorCode.INVALID_TOKEN, "유효하지 않은 액세스 토큰입니다.");
            response.setContentType("application/json; charset=UTF-8");

            // 이 부분에서 Front end 개발자와 [협의된 응답]을 주어야 한다
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return;
        }

        String resourceId = jwtUtil.getResourceId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //userEntity를 생성하여 값 set
        UserEntity userEntity = UserEntity.builder()
                .resourceId(resourceId)
                .password("temppassword")
                .role(UserRole.valueOf(role))
                .build();

        log.info("UserEntity: {}",userEntity);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로
        filterChain.doFilter(request, response);

    }

    private String getAccessToken(String authorization){
        if (authorization != null && authorization.startsWith(TOKEN_PREFIX)){
            return authorization.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
