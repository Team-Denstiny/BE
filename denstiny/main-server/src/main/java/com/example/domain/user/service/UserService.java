package com.example.domain.user.service;

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

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 1. 일반 회원 가입시 주소를 입력하는 경우 사용하는 메서드
     * email, nickName의 중복을 검사 후 -> ROLE을 MEMBER로 부여 후 저장
     */
    public UserEntity register(UserEntity memberEntity){
        if (isDuplicateEmail(memberEntity.getEmail())) {
            throw new ApiException(UserErrorCode.DUPLICATE_EMAIL, "회원 가입시 Email 중복");
        }
        if (isDuplicateNickname(memberEntity.getNickName())){
            throw new ApiException(UserErrorCode.DUPLICATE_NICKNAME, "회원 가입시 nickName 중복");
        }
        return Optional.ofNullable(memberEntity)
                .map(it ->{
                    memberEntity.setRole(UserRole.MEMBER);
                    return userRepository.save(memberEntity);
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "User Entity Null"));
    }
    private boolean isDuplicateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    private boolean isDuplicateNickname(String nickName){
        return userRepository.findByNickName(nickName).isPresent();
    }
}
