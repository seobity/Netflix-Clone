package com.likelion.NetflixClone.global.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadService {

    private final String uploadDir = "uploads/";

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 존재하지 않습니다.");
        }

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리가 없으면 생성
            }

            String originalFilename = file.getOriginalFilename();
            String storeFilename = UUID.randomUUID() + "_" + originalFilename;
            String filePath = dir.getAbsolutePath() + File.separator + storeFilename;

            file.transferTo(new File(filePath));

            return "/images/" + storeFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}