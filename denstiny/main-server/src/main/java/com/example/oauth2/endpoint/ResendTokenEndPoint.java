package com.example.oauth2.endpoint;

import static com.example.constant.TokenHeaderConstant.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.user.service.UserService;
import com.example.error.UserErrorCode;
import com.example.jwt.JWTUtil;
import com.example.oauth2.dto.Oauth2AddressPlusDto;
import com.example.user.UserEntity;

import api.Api;
import api.Result;
import error.ErrorCode;
import exception.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class ResendTokenEndPoint {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    private UserEntity getUserFromCookie(String accessCookieValue) {
        String resourceId = jwtUtil.getResourceId(accessCookieValue);
        log.info("resourceId : {}", resourceId);
        UserEntity user = userService.getUserByResourceId(resourceId);

        if (user == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다");
        }
        return user;
    }

    private void deleteAccessCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("access", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/endpoint")
    public ResponseEntity<Api<Map<String, Long>>> updateUserAddress(
            @CookieValue(value = "access", required = false) String accessCookieValue,
            @RequestBody Oauth2AddressPlusDto oauth2AddressPlusDto,
            HttpServletResponse response
    ) {
        if (accessCookieValue == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "쿠키가 존재하지 않습니다");
        }

        log.info("들어왔어요");

        UserEntity user = getUserFromCookie(accessCookieValue);
        user.setAddress(oauth2AddressPlusDto.getAddress());
        user.setLatitude(oauth2AddressPlusDto.getLatitude());
        user.setLongitude(oauth2AddressPlusDto.getLongitude());

        // db에 업데이트 내용 저장
        userService.saveUser(user);

        Map<String, Long> responseBody = new HashMap<>();
        responseBody.put("id", user.getUserId());

        deleteAccessCookie(response);

        Api<Map<String, Long>> apiResponse = new Api<>(new Result(HttpStatus.CREATED.value(), "주소 저장 성공", "성공"), responseBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HEADER_AUTHORIZATION, accessCookieValue)
                .body(apiResponse);
    }

    @GetMapping("/endpoint")
    public ResponseEntity<Api<Map<String, Long>>> getUserData(
            @CookieValue(value = "access", required = false) String accessCookieValue,
            HttpServletResponse response
    ) {
        if (accessCookieValue == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "쿠키가 존재하지 않습니다");
        }

        UserEntity user = getUserFromCookie(accessCookieValue);

        Map<String, Long> responseBody = new HashMap<>();
        responseBody.put("id", user.getUserId());

        deleteAccessCookie(response);

        Api<Map<String, Long>> apiResponse = new Api<>(new Result(HttpStatus.OK.value(), "로그인 성공", "성공"), responseBody);
        return ResponseEntity.ok()
                .header(HEADER_AUTHORIZATION, accessCookieValue)
                .body(apiResponse);
    }
}



