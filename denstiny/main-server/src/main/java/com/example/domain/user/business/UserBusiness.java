package com.example.domain.user.business;

import annotation.Business;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import com.example.domain.user.converter.UserConverter;
import com.example.domain.user.service.UserService;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Business
@AllArgsConstructor
public class UserBusiness {

    private final UserConverter memberConverter;
    private final UserService memberService;

    public UserRegisterResponse registerMember(UserRegisterRequest memberRegisterRequest){

        return Optional.ofNullable(memberRegisterRequest)
                .map(it -> memberConverter.toEntity(it))
                .map(it -> memberService.register(it))
                .map(it -> memberConverter.toResponse(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "registerMember null point"));
    }

}
