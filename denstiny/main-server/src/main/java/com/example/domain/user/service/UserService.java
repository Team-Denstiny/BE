package com.example.domain.user.service;

import com.example.domain.user.controller.model.UserResponse;
import com.example.domain.user.controller.model.UserUpdateRequest;
import com.example.error.UserErrorCode;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.user.enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 1. 일반 회원 가입시 주소를 입력하는 경우 사용하는 메서드
     * email, nickName의 중복을 검사 후 -> ROLE을 MEMBER로 부여 후 저장
     */
    public UserEntity register(UserEntity userEntity) {
        if (isDuplicateEmail(userEntity.getEmail())) {
            throw new ApiException(UserErrorCode.DUPLICATE_EMAIL, "회원 가입시 Email 중복");
        }
        if (isDuplicateNickname(userEntity.getNickName())) {
            throw new ApiException(UserErrorCode.DUPLICATE_NICKNAME, "회원 가입시 nickName 중복");
        }
        String uuid = UUID.randomUUID().toString();

        return Optional.ofNullable(userEntity)
                .map(it -> {
                    userEntity.setRole(UserRole.ROLE_MEMBER);
                    userEntity.setResourceId(uuid);
                    userEntity.setResourceName("denstiny");
                    return userRepository.save(userEntity);
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "User Entity Null"));
    }

    public boolean isDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isDuplicateNickname(String nickName) {
        return userRepository.findByNickName(nickName).isPresent();
    }

    public UserEntity getUserById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "pk로 회원을 찾을 수 없음"));
        return userEntity;
    }

    public UserEntity updateUser(Long userId, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "update 회원을 찾을 수 없음"));

        // 업데이트할 필드만 수정합니다.
        if (request.getName() != null) {
            userEntity.setName(request.getName());
        }
        if (request.getNickName() != null) {
            userEntity.setNickName(request.getNickName());
        }
        if (request.getBirthAt() != null) {
            userEntity.setBirthAt(request.getBirthAt());
        }
        if (request.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            userEntity.setAddress(request.getAddress());
        }
        if (request.getEmail() != null) {
            userEntity.setEmail(request.getEmail());
        }
        if (request.getProfileImg() != null) {
            userEntity.setProfileImg(request.getProfileImg());
        }

        // 변경 사항 저장
        return userRepository.save(userEntity);
    }
}
