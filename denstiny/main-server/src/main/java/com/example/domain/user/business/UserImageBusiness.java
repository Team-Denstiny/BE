package com.example.domain.user.business;

import annotation.Business;
import com.example.domain.user.service.UserService;
import com.example.error.UserErrorCode;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import com.example.util.UserImageUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Business
@AllArgsConstructor
@Transactional
public class UserImageBusiness {

    private final UserImageUtil userImageUtil;
    private final JWTUtil jwtUtil;
    private final UserService userService;

    public String saveUserImage(String token, Long userId, MultipartFile userImage){

        // 토큰을 통해 유저의 위도, 경도 획득
        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userService.getUserByResourceId(resourceId);

        if (!user.getUserId().equals(userId)){
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자기자신의 사진에만 접근할 수 있습니다");
        }

        try {
            String url = userImageUtil.parseUserImage(userId, userImage);
            return url;
        } catch (IOException e) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "파일 변환시 실패하였습니다");
        }

        //TODO 나중에 s3에서 url 생성시 해당 url을 userEntity에 넣어줘야 한다
    }
}
