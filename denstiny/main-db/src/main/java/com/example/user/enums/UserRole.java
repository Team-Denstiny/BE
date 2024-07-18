package com.example.user.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    ADMIN("관리자"),
    MEMBER("회원")
    ;
    private final String roleName;
}
