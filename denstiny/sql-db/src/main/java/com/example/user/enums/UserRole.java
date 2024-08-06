package com.example.user.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("관리자"),
    ROLE_MEMBER("회원")
    ;
    private final String roleName;
}
