package com.example.error;

import error.ErrorCodeIfs;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs {
    INVALID_TOKEN(401,2000,"유효하지 않은 토큰"),
    EXPIRED_AT(HttpServletResponse.SC_UNAUTHORIZED,2001,"만료된 토큰"),
    TOKEN_EXCEPTION(401,2002,"토큰의 알 수 없는 에러입니다"),
    AUTHORIZATION_TOKEN_NOT_FOUND(401, 2003, "인증 헤더 토큰이 없습니다"),
    REFRESH_TOKEN_EXCEPTION(401, 2004, "리프레쉬 토큰의 알 수 없는 에러입니다"),
    NULL_REFRESH_TOKEN(401, 2005, "리프레쉬 토큰이 없습니다"),
    EXPIRED_REFRESH_TOKEN(401, 2006, "리프레쉬 토큰이 만료되었습니다"),
    INVALID_REFRESH_TOKEN(401,2000,"유효하지 않은 리프레시 토큰입니다");

    private final Integer httpStatusCode; // 상응하는 http status 코드
    private final Integer errorCode;  // 인터널 에러 코드
    private final String description; //설명
}
