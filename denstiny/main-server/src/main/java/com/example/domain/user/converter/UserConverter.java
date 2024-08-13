package com.example.domain.user.converter;

import annotation.Converter;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import com.example.domain.user.controller.model.UserResponse;
import com.example.domain.user.controller.model.UserUpdateRequest;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Converter
@RequiredArgsConstructor
public class UserConverter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity toEntity(UserRegisterRequest request){
        return Optional.ofNullable(request)
                .map(it -> {
                    return  UserEntity.builder()
                            .name(request.getName())
                            .email(request.getEmail())
                            .nickName(request.getNickName())
                            .password(bCryptPasswordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                            .birthAt(String.valueOf(request.getBirthAt()))
                            .phoneNumber(request.getPhoneNumber())
                            .address(request.getAddress())
                            .latitude(request.getLatitude())
                            .longitude(request.getLongitude())
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT,"UserRegisterRequest is null"));
    }

    public UserRegisterResponse toRegisterResponse(UserEntity user){
        return Optional.ofNullable(user)
                .map(it -> {
                    return UserRegisterResponse.builder()
                            .memberId(user.getUserId())
                            .name(user.getName())
                            .nickName(user.getNickName())
                            .email(user.getEmail())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserEntity is null"));
    }

    public UserResponse toResponse(UserEntity user){
        return Optional.ofNullable(user)
                .map(it -> {
                    return UserResponse.builder()
                            .name(user.getName())
                            .nickName(user.getNickName())
                            .birthAt(user.getBirthAt())
                            .phoneNumber(user.getPhoneNumber())
                            .address(user.getAddress())
                            .email(user.getEmail())
                            .profileImg(user.getProfileImg())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserEntity is null"));
    }

}
