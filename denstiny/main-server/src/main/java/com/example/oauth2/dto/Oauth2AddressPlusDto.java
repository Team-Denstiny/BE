package com.example.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2AddressPlusDto {
    private String address;
    private Double latitude;
    private Double longitude;
}
