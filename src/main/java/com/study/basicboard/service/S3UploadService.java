package com.study.basicboard.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.entity.UploadImage;
import com.study.basicboard.repository.BoardRepository;
import com.study.basicboard.repository.UploadImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final UploadImageRepository uploadImageRepository;
    private final BoardRepository boardRepository;
    
    private final String rootPath = System.getProperty("user.dir");
    private final String fileDir = rootPath + "/src/main/resources/static/upload-images/";

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public UploadImage saveImage(MultipartFile multipartFile, Board board) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        String savedFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        // 파일 저장
        Path path = Paths.get(fileDir, savedFilename);
        Files.createDirectories(path.getParent()); // 부모 디렉토리 생성
        multipartFile.transferTo(path); // 파일 저장

        return uploadImageRepository.save(UploadImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .build());
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public void deleteImage(UploadImage uploadImage) throws IOException {
        uploadImageRepository.delete(uploadImage);
        Files.deleteIfExists(Paths.get(getFullPath(uploadImage.getSavedFilename())));
    }

	public ResponseEntity<UrlResource> downloadImage(Long boardId) {
		// TODO Auto-generated method stub
		return null;
	}
}
