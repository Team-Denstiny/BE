package com.example.oauth2.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection=new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    // oauth2 resource 이름 + id
    public String getResourceName(){
        return userDTO.getResourceName();
    }

    public String getNickname(){
        return userDTO.getNickname();
    }
    public String getBirthAt(){
        return userDTO.getBirthAt();
    }
    public String getEmail(){
        return userDTO.getEmail();
    }
    public String getPhoneNumber(){
        return userDTO.getPhoneNumber();
    }
    public String getProfileImg(){
        return userDTO.getProfileImg();
    }
    public String getResourceId(){
        return userDTO.getResourceId();
    }
    public boolean isNewUser() {
        return userDTO.getIsNewUser();
    }
}
