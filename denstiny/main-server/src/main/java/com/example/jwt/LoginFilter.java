package com.example.jwt;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.domain.user.controller.model.UserLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import error.ErrorCode;
import exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/open-api/users/login"); // 커스텀 로그인 경로 설정 (spring security 기본 경로는 /login)

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getUsername();

        log.info("{} loign 성공",email);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String accessToken = jwtUtil.createJwt(email, role, 60*60*1000L);

        response.addHeader("Authorization","Bearer "+accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(401);
    }
}
