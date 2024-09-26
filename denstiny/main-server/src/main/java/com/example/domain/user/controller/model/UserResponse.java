package com.example.domain.user.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String name;
    private String nickName;
    private String birthAt;
    private String phoneNumber;
    private String address;
    private String email;
    private String profileImg;
    private Double longitude;
    private Double latitude;

}
