package com.example.oauth2.dto;

import lombok.ToString;

import java.util.Map;
@ToString
public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String,Object> attribute){
        // naver는 이중 json 형식으로, response 에 데이터가 담긴다
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }

    @Override
    public String getNickname() {
        return attribute.get("nickname").toString();
    }

    @Override
    public String getBirthyear() {
        return attribute.get("birthyear").toString();
    }

    @Override
    public String getPhone() {
        return attribute.get("mobile").toString();
    }

    @Override
    public String getProfile() {
        return attribute.get("profile_image").toString();
    }
}
