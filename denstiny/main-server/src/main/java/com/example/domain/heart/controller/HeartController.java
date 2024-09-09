package com.example.domain.heart.controller;

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
    public ResponseEntity<String> addHeart(
            @PathVariable("userId") Long userId,
            @RequestBody HeartRequest heartReq) {
        HeartResponse heartRes = heartBusiness.addHeart(heartReq, userId);
        return ResponseEntity.ok(heartRes.toString());
    }

    // 게시글 좋아요 삭제
    @DeleteMapping("/{userId}/heart")
    public ResponseEntity<String> deleteHeart(
            @PathVariable("userId") Long userId,
            @RequestBody HeartRequest heartReq) {
        return ResponseEntity.ok(heartBusiness.deleteHeart(heartReq, userId));
    }
}
