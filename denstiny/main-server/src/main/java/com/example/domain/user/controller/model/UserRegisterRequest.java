package com.example.domain.user.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRegisterRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String nickName;

    @Email
    @NotBlank
    private String email;
    @NotNull
    private Integer birthAt;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String password;
    @NotBlank
    private String address;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

}
