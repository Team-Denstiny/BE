package com.example.domain.user.controller;

import api.Api;
import api.Result;
import com.example.domain.user.business.UserImageBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserImageController {

    private final UserImageBusiness userImageBusiness;

    @PostMapping("/{userId}/upload-image")
    public Api<String> uploadUserImage(
            @PathVariable("userId") Long userId,
            @RequestPart("userImage")MultipartFile image,
            @RequestHeader("Authorization") String token
    ){
        String url = userImageBusiness.saveUserImage(token, userId, image);
        return new Api<>(new Result(201, "해당경로에 user image가 저장되었습니다", "성공"), url);
    }

}
