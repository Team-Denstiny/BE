package com.example.util;

import error.ErrorCode;
import exception.ApiException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 이미지 파일을 받으면  -> 해당 경로 파일에 저장
 * 나중에 AWS S3에 올려서 받은 resoucre를 UserEntity의 이미지 속성에 저장!
 */
@Component
public class UserImageUtil {

    public String parseUserImage(Long userId, MultipartFile userImage) throws IOException {

        if (userImage.isEmpty()) {
            throw new ApiException(ErrorCode.NULL_POINT, "유저의 이미지를 수정하려고 하였지만, 파일을 받지 못했습니다.");
        }

        // 날짜 형식으로 저장할 디렉토리 생성
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = "images" + File.separator + current_date;

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 파일 확장자 체크
        String contentType = userImage.getContentType();
        String originalFileExtension;

        if (contentType == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "사진 파일의 확장자를 확인해주세요.");
        } else if (contentType.contains("image/jpeg")) {
            originalFileExtension = ".jpg";
        } else if (contentType.contains("image/png")) {
            originalFileExtension = ".png";
        } else if (contentType.contains("image/gif")) {
            originalFileExtension = ".gif";
        } else {
            throw new ApiException(ErrorCode.BAD_REQUEST, "지원되지 않는 파일 형식입니다. JPG, PNG, GIF만 허용됩니다.");
        }

        // 고유 파일 이름 생성
        String newFileName = userId.toString() + "_" + System.nanoTime() + originalFileExtension;

        // 파일 저장
        File file = new File(absolutePath + path + File.separator + newFileName);
        userImage.transferTo(file);

        // 저장된 파일의 경로 또는 URL 반환 (추후 S3 업로드 후 URL 반환 가능)
        return absolutePath + path + File.separator + newFileName;
    }
}
