package com.example.oauth2.endpoint;

import java.util.HashMap;
import java.util.Map;

import api.Api;
import api.Result;
import com.example.error.TokenErrorCode;
import com.example.error.UserErrorCode;
import com.example.jwt.JWTUtil;
import com.example.oauth2.dto.Oauth2AddressPlusDto;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import static com.example.constant.TokenHeaderConstant.HEADER_AUTHORIZATION;
import static com.example.constant.TokenHeaderConstant.TOKEN_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class ResendTokenEndPoint {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    /* 
    @GetMapping("/endpoint")
    public Map<String, String> handleRequest(@RequestHeader Map<String, String> headers) {
        // 로그에 요청 헤더를 출력합니다.
        System.out.println("Request received at /login/endpoint");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // 간단한 응답을 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put("message", "Request received and headers logged.");
        return response;
    }
        */

    @PostMapping("/endpoint")
    public ResponseEntity<Api<Map<String, Long>>> getCookieData(
            HttpServletRequest request,
            @RequestBody Oauth2AddressPlusDto oauth2AddressPlusDto
    ) {
        Cookie[] cookies = request.getCookies();
        HttpHeaders headers = new HttpHeaders();
        Map<String, Long> responseBody = new HashMap<>();

        Result result = new Result(HttpStatus.OK.value(), "로그인 성공", "성공");

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access")) {
                    String cookieValue = cookie.getValue();

                    // 토큰에서 resourceId 획득
                    String resourceId = jwtUtil.getResourceId(cookieValue);
                    log.info("resourceId : {}", resourceId);
                    UserEntity user = userRepository.findByResourceId(resourceId);

                    if (user != null) {
                        // 주소 업데이트
                        user.setAddress(oauth2AddressPlusDto.getAddress());
                        Long userId = user.getUserId();

                        headers.set(HEADER_AUTHORIZATION, TOKEN_PREFIX + cookieValue);
                        responseBody.put("id", userId);
                    } else {
                        throw new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다");
                    }
                    break; // 쿠키가 발견되면 반복을 종료합니다.
                }
            }

            if (responseBody.isEmpty()) {
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, "쿠키안에 담긴 토큰이 유효하지 않습니다");
            }
        } else {
            throw new ApiException(ErrorCode.NULL_POINT, "쿠키가 존재하지 않습니다");
        }

        Api<Map<String, Long>> apiResponse = new Api<>(result, responseBody);

        return ResponseEntity.ok()
                .headers(headers)
                .body(apiResponse);
    }
}


