package com.example.domain.user.business;

import annotation.Business;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import com.example.domain.user.controller.model.UserResponse;
import com.example.domain.user.controller.model.UserUpdateRequest;
import com.example.domain.user.converter.UserConverter;
import com.example.domain.user.service.UserService;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Business
@AllArgsConstructor
public class UserBusiness {

    private final UserConverter userConverter;
    private final UserService userService;

    public UserRegisterResponse registerMember(UserRegisterRequest userRegisterRequest){

        return Optional.ofNullable(userRegisterRequest)
                .map(it -> userConverter.toEntity(it))
                .map(it -> userService.register(it))
                .map(it -> userConverter.toRegisterResponse(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "registerMember null point"));
    }

    public UserResponse getUserById(Long id){
        UserEntity userById = userService.getUserById(id);
        UserResponse response = userConverter.toResponse(userById);
        return response;
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request){
        UserEntity userEntity = userService.updateUser(id, request);
        UserResponse response = userConverter.toResponse(userEntity);
        return response;
    }

}
