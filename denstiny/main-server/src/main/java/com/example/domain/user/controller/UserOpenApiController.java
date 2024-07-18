package com.example.domain.user.controller;

import api.Api;
import api.Result;
import com.example.domain.user.business.UserBusiness;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/users")
public class UserOpenApiController {

    private final UserBusiness memberBusiness;

    @RequestMapping("/register")
    public Api<UserRegisterResponse> register(
            @Valid
            @RequestBody UserRegisterRequest request
    ){
        UserRegisterResponse response = memberBusiness.registerMember(request);
        return new Api<>(new Result(201, "회원가입 성공", "성공"), response);
    }
}
