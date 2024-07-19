package com.example.jwt;

import api.Api;
import api.Result;
import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.domain.user.controller.model.UserLoginRequest;
import com.example.refresh.RefreshEntity;
import com.example.refresh.RefreshRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import error.ErrorCode;
import exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static com.example.jwt.JWTFilter.HEADER_AUTHORIZATION;
import static com.example.jwt.JWTFilter.TOKEN_PREFIX;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,RefreshRepository refreshRepository) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/open-api/users/login"); // 커스텀 로그인 경로 설정 (spring security 기본 경로는 /login)

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // 요청 바디에서 JSON 데이터를 읽어와서 UserLoginRequest 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            UserLoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);

            System.out.println(loginRequest);

            // UserLoginRequest 객체에서 이메일과 비밀번호 추출
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            // UsernamePasswordAuthenticationToken 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "login 요청시 parsing 실패");
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        //유저 정보
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String access = jwtUtil.createJwt("access", email, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", email,role,86400000L );

        // refreshToken RDB에 저장
        // TODO RDB <--> REDIS
        addRefreshEntity(email, refresh,86400000L);

        // JSON 응답 설정
        response.setContentType("application/json; charset=UTF-8");

        Result result = new Result(200, "로그인 성공", "성공");
        ResponseEntity<Result> responseEntity = new ResponseEntity<>(result, HttpStatus.OK);

        // JSON 응답을 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());

        // 응답 작성
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();

        // 헤더와 쿠키 설정
        response.setHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + access);
        response.addCookie(createCookie("refresh", refresh));
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        // JSON 응답 설정
        response.setContentType("application/json; charset=UTF-8");

        // 실패 응답 메시지
        Result result = new Result(401, "로그인 실패", "인증에 실패하였습니다.");
        ResponseEntity<Result> responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

        // JSON 응답을 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseEntity.getBody());

        // 응답 작성
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
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
