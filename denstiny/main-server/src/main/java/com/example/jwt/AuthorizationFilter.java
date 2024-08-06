package com.example.jwt;

import api.Api;
import api.Result;
import com.example.domain.user.controller.model.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.startsWith("/api/user/")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Long userId = extractUserIdFromUri(uri);

                if (!userDetails.getUserId().equals(userId)) {
                    sendForbiddenResponse(response, "다른사람의 정보를 볼 수 없습니다");
                    return;
                }
            } else {
                sendForbiddenResponse(response, "인증되지 않은 사용자입니다");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Long extractUserIdFromUri(String uri) {
        String[] parts = uri.split("/");
        return Long.parseLong(parts[3]);
    }

    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        Result result = new Result(HttpStatus.FORBIDDEN.value(), message, "실패");
        Api<Result> apiResponse = new Api<>(result, null);

        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(jsonResponse);
    }
}
