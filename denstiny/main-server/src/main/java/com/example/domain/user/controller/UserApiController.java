package com.example.domain.user.controller;

import api.Api;
import api.Result;
import com.example.domain.user.business.UserBusiness;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import com.example.domain.user.controller.model.UserResponse;
import com.example.domain.user.controller.model.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApiController {

    private final UserBusiness userBusiness;

    @RequestMapping("/register")
    public Api<UserRegisterResponse> register(
            @Valid
            @RequestBody UserRegisterRequest request
    ){
        UserRegisterResponse response = userBusiness.registerMember(request);
        return new Api<>(new Result(201, "회원가입 성공", "성공"), response);
    }

    @GetMapping("{userId}")
    public Api<UserResponse> getUserInfo(
            @Valid
            @PathVariable("userId") Long userId
    ){
        UserResponse userById = userBusiness.getUserById(userId);
        return new Api<>(new Result(200, "정보 조회 성공", "성공"), userById);
    }

    @PatchMapping("{userId}")
    public Api<UserResponse> updateUser(
            @Valid
            @PathVariable("userId") Long userId,
            @RequestBody UserUpdateRequest request
    ){
        UserResponse updatedUser = userBusiness.updateUser(userId, request);
        return new Api<>(new Result(200, "정보 수정 성공", "성공"), updatedUser);
    }


}
