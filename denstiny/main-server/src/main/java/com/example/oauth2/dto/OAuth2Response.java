package com.example.oauth2.dto;

public interface OAuth2Response {

    // 제공자(ex naver, kakao)
    String getProvider();
    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();
    // 이메일
    String getEmail();
    // 사용자 실명
    String getName();
    // 사용자 닉네임
    String getNickname();
    // 사용자 생일
    String getBirthyear();
    // 사용자 핸드폰 번호
    String getPhone();
    //사진 img
    String getProfile();
}
