package com.example.error;

import error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * USER의 경우 1000번대 에러코드 사용
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCodeIfs {
    USER_NOT_FOUND(400,1401,"사용자를 찾을 수 없음"),
    DUPLICATE_EMAIL(400,1402,"Email이 중복됨"),
    DUPLICATE_NICKNAME(400,1403,"nickname이 중복됨"),

    ;

    private final Integer httpStatusCode; // 상응 하는 http error code
    private final Integer errorCode; // 인터널 error code
    private final String description;
}
