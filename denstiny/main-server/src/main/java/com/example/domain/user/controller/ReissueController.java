package com.example.domain.user.controller;

import api.Api;
import api.Result;
import com.example.domain.user.service.ReissueService;
import com.example.error.TokenErrorCode;
import com.example.jwt.JWTUtil;
import com.example.refresh.RefreshEntity;
import com.example.refresh.RefreshRepository;
import exception.ApiException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.example.constant.TokenHeaderConstant.HEADER_AUTHORIZATION;
import static com.example.constant.TokenHeaderConstant.TOKEN_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final ReissueService reissueService;
    private final RefreshRepository refreshRepository;

    @PostMapping("/api/users/reissue")
    public Api<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = reissueService.getRefreshToken(request);

        if (refresh == null) {
            throw new ApiException(TokenErrorCode.NULL_REFRESH_TOKEN, "refresh token이 없습니다");
        }
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new ApiException(TokenErrorCode.EXPIRED_REFRESH_TOKEN, "refresh token이 만료되었습니다");
        }

        // 토큰이 refresh인지 확인(발급시 페이로드에 명시되어 있다)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new ApiException(TokenErrorCode.INVALID_REFRESH_TOKEN, "refresh token이 유효하지 않습니다");
        }

        // DB에 refreshToken이 저장되어 있는지 확인
        // TODO Redis로 변경된다면 수정되어야 하는 부분

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        log.info("Exist : {}",isExist);
        if (!isExist){
            throw new ApiException(TokenErrorCode.REFRESH_TOKEN_EXCEPTION, "refresh token이 db에 저장되지 않았습니다");
        }

        String resourceId = jwtUtil.getResourceId(refresh);
        String role = jwtUtil.getRole(refresh);

        // 새로운 JWT 생성
        String newAccess = jwtUtil.createJwt("access", resourceId, role, 600000L);
        // Refresh Token Rotate 기능 추가
        String newRefresh = jwtUtil.createJwt("refresh", resourceId, role, 86400000L);

        // Refresh Rotate 하는 부분
        // TODO : Redis로 변경시  수정해야 하는 부분

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(resourceId, newRefresh, 96400000L);

        // 응답 헤더 설정
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HEADER_AUTHORIZATION,  newAccess); // 이 부분에서 prefix부분 삭제 : bearer bearer
        response.addCookie(createCookie("refresh", newRefresh));

        // Result 및 Api 객체 생성
        Result result = new Result(200, "refresh token으로 access token 발급", "성공");
        return new Api<>(result, null);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
    private void addRefreshEntity(String resourceId, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = RefreshEntity.builder()
                .resourceId(resourceId)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
}
