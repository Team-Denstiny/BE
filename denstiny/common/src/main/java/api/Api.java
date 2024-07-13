package org.example.api.common.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.common.error.ErrorCodeIfs;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> { // Api 스펙의 최상위를 담당할 Api 클래스

    private Result result;

    @Valid
    private T body;


    /**
     * 성공에 대한 static OK 메서드
     */
    public static <T> Api<T> OK(T data){
        Api<T> api = new Api<>();
        api.result = Result.OK();
        api.body = data;
        return api;
    }

    /**
     * 실제 result 값을 이용해 -> Error 이용
     */

    public static Api<Object> Error(Result result){
        Api<Object> api = new Api<Object>();
        api.result = result;
        return api;
    }

    /**
     * ErrorCodeIfs를 이용해 -> Error 이용
     */

    public static Api<Object> Error(ErrorCodeIfs errorCodeIfs){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs);
        return api;
    }
    /**
     * ErrorCodeIfs, Throwable 을 이용해 -> Error 이용
     */

    public static Api<Object> Error(ErrorCodeIfs errorCodeIfs, Throwable tx){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, tx);
        return api;
    }

    /**
     * ErrorCodeIfs, description 을 이용해 -> Error 이용
     */

    public static Api<Object> Error(ErrorCodeIfs errorCodeIfs, String description){
        Api<Object> api = new Api<Object>();
        api.result = Result.ERROR(errorCodeIfs, description);
        return api;
    }

}
