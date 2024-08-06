package com.example.oauth2.dto;

import lombok.ToString;
import java.util.Map;

@ToString
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String,Object> attribute){
        // 카카오는 사용자 정보가 직접 포함되어 있음
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        // 이메일 정보는 "kakao_account" 안에 들어있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        // 이름 정보는 "properties" 안에 들어있음
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("nickname").toString();
    }

    @Override
    public String getNickname() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("nickname").toString();
    }

    @Override
    public String getBirthyear() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return kakaoAccount.get("birthyear").toString();
    }

    @Override
    public String getPhone() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        return kakaoAccount.get("phone_number").toString();
    }

    @Override
    public String getProfile() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        return properties.get("profile_image").toString();
    }
}
