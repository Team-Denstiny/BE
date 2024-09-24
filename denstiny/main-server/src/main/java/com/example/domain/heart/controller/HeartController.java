package com.example.domain.heart.controller;

import api.Api;
import api.Result;
import com.example.domain.heart.business.HeartBusiness;
import com.example.domain.heart.controller.model.HeartRequest;
import com.example.domain.heart.controller.model.HeartResponse;
import com.example.domain.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class HeartController {
    private final HeartBusiness heartBusiness;

    // 게시글 좋아요 생성
    @PostMapping("/{userId}/heart")
    public Api<String> addHeart(
            @PathVariable("userId") Long userId,
            @RequestBody HeartRequest heartReq) {
        HeartResponse heartRes = heartBusiness.addHeart(heartReq, userId);
        return new Api<>(new Result(201, "게시글 좋아요 생성 성공", "성공"), heartRes.toString());
    }

    // 게시글 좋아요 삭제
    @DeleteMapping("/{userId}/heart")
    public Api<String> deleteHeart(
            @PathVariable("userId") Long userId,
            @RequestBody HeartRequest heartReq) {
        return new Api<>(new Result(200, "게시글 좋아요 삭제 성공", "성공"), heartBusiness.deleteHeart(heartReq, userId));
    }
}
