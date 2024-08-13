package com.example.oauth2.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2AddressPlusDto {
    private String address;

    @JsonCreator
    public Oauth2AddressPlusDto(@JsonProperty("address") String address) {
        this.address = address;
    }
}
