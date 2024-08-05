package com.example.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    // 해당 DTO를 이용해서, 소셜로그인에서 받은 정보를 Entity에 저장하기

    private String role;
    private String name;
    private String nickname;
    private String birthAt;
    private String email;
    private String phoneNumber;
    private String resourceName;
    private String resourceId;
    private String profileImg;

}
