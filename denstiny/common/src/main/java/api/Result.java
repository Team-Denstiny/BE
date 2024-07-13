package org.example.api.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.common.error.ErrorCode;
import org.example.api.common.error.ErrorCodeIfs;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {

    private Integer resultCode;
    private String resultMessage;
    private String resultDescription;

    /**
     * 성공에 대한 Result 정보를 반환 하는 static 메서드
     */
    public static Result OK(){
        return Result
                .builder()
                .resultCode(ErrorCode.OK.getErrorCode())
                .resultMessage(ErrorCode.OK.getDescription())
                .resultDescription("성공")
                .build();
    }

    /**
     * @param errorCodeIfs -> Interface 추상화를 통해, 어떠한 ErrorCode 도 받을 수 있다
     */

    public static Result ERROR(ErrorCodeIfs errorCodeIfs){
        return Result
                .builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription("에러가 발생하였습니다")
                .build();
    }

    /**
     * @param errorCodeIfs,tx -> description에 tx의 메시지를 반환
     */

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx){
        return Result
                .builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription(tx.getLocalizedMessage())
                .build();
    }

    /**
     * @param errorCodeIfs,description -> 직접 error 설명 추가
     */

    public static Result ERROR(ErrorCodeIfs errorCodeIfs, String description){
        return Result
                .builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription(description)
                .build();
    }
}
