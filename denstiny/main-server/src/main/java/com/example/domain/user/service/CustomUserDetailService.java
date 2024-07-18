package com.example.domain.user.service;

import com.example.domain.user.controller.model.CustomUserDetails;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(it -> {
                    return new CustomUserDetails(it);
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "email로 user을 찾을 수 없습니다"));
    }
}
