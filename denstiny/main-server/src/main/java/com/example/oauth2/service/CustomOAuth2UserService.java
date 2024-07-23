package com.example.oauth2.service;

import com.example.oauth2.dto.CustomOAuth2User;
import com.example.oauth2.dto.NaverResponse;
import com.example.oauth2.dto.OAuth2Response;
import com.example.oauth2.dto.UserDTO;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("로그인한 유저 {}",oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("로그인한 사이트 {}",registrationId);

        OAuth2Response oAuth2Response = null;
        switch (registrationId) {
            case "naver":
                oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
                break;
            case "kakao":
                //TODO kakao는 naver 이후에 추가 예정
                break;
            default:
                oAuth2Response = null;
                break;
        }

        String resourceName = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // db에 oauth2 로그인 정보 저장
        UserEntity existData = userRepository.findByResourceName(resourceName);

        if (existData == null){
            UserEntity userEntity = UserEntity.builder()
                    .name(oAuth2Response.getName())
                    .nickName(oAuth2Response.getNickname())
                    .birthAt(oAuth2Response.getBirthyear())
                    .email(oAuth2Response.getEmail())
                    .role(UserRole.ROLE_MEMBER)
                    .phoneNumber(oAuth2Response.getPhone())
//                    .address()
                    .resourceName(resourceName)
                    .profileImg(oAuth2Response.getProfile())
                    .build();

            // 저장
            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .resourceName(resourceName)
                    .role(UserRole.ROLE_MEMBER.toString())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getNickname())
                    .email(oAuth2Response.getEmail())
                    .birthAt(oAuth2Response.getBirthyear())
                    .phoneNumber(oAuth2Response.getPhone())
                    .profileImg(oAuth2Response.getProfile())
                    .build();
            return new CustomOAuth2User(userDTO);

        }
        else {

            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2User.getName());
            existData.setNickName(oAuth2Response.getNickname());
            existData.setPhoneNumber(oAuth2Response.getPhone());
            existData.setProfileImg(oAuth2Response.getProfile());

            userRepository.save(existData);

            UserDTO userDTO = UserDTO.builder()
                    .resourceName(existData.getResourceName())
                    .role(UserRole.ROLE_MEMBER.toString())
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getNickname())
                    .email(oAuth2Response.getEmail())
                    .birthAt(existData.getBirthAt())
                    .phoneNumber(oAuth2Response.getPhone())
                    .profileImg(oAuth2Response.getProfile())
                    .build();

            return new CustomOAuth2User(userDTO);
        }
    }
}
