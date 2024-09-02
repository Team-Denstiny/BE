package com.example.util;


import com.example.board.BoardEntity;
import com.example.board_image.BoardImageEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;


public class ImageUtil {
    public static List<BoardImageEntity> parseFileInfo(
            BoardEntity board,
            List<MultipartFile> multipartFiles
    ) throws Exception {

        List<BoardImageEntity> fileList = new ArrayList<>();

        if (multipartFiles.isEmpty()) {
            return fileList;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        // 프로젝트 폴더에 저장하기 위해 절대경로를 설정 (Window 의 Tomcat 은 Temp 파일을 이용한다)
        String absolutePath = new File("").getAbsolutePath() + "\\";

        // 경로를 지정하고 그곳에다가 저장
        String path = "images/" + current_date;
        File file = new File(path);
        // 저장할 위치의 디렉토리가 존지하지 않을 경우
        if (!file.exists()) {
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            file.mkdirs();
        }

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                // jpeg, png, gif 파일들만 받아서 처리할 예정
                String contentType = multipartFile.getContentType();
                String originalFileExtension;
                // 확장자 명이 없으면 이 파일은 잘 못 된 것이다
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        originalFileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        originalFileExtension = ".png";
                    } else if (contentType.contains("image/gif")) {
                        originalFileExtension = ".gif";
                    }
                    // 다른 파일 명이면 아무 일 하지 않는다
                    else {
                        break;
                    }
                }
                // 각 이름은 겹치면 안되므로 나노 초까지 동원하여 지정
                String newFileName = System.nanoTime() + originalFileExtension;
                // 생성 후 리스트에 추가
                BoardImageEntity boardImage = BoardImageEntity.builder()
                        .storedFileName(path + "/" + newFileName)
                        .originalFileName(multipartFile.getOriginalFilename())
                        .fileSize(multipartFile.getSize())
                        .board(board)
                        .build();
                fileList.add(boardImage);

                // 저장된 파일로 변경하여 이를 보여주기 위함
                file = new File(replaceLastCharacterWithSlash(absolutePath) + path + "/" + newFileName);
                multipartFile.transferTo(file);
            }
        }

        return fileList;
    }

    public static String replaceLastCharacterWithSlash(String str) {
        if (str == null || str.isEmpty()) {
            return str; // 빈 문자열이나 null 처리
        }

        return str.substring(0, str.length() - 1) + "/"; // 마지막 문자를 "/"로 변경
    }
}
