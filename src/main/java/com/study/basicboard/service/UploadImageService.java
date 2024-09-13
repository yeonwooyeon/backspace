package com.study.basicboard.service;

import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.entity.UploadImage;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.repository.BoardRepository;
import com.study.basicboard.repository.UploadImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadImageService {

    private final UploadImageRepository uploadImageRepository;
    private final BoardRepository boardRepository;
    
    // 서비스 내에서 사용하는 로컬 파일 경로 설정
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
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        String savedFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        // 파일 저장
        try {
            Path path = Paths.get(fileDir, savedFilename);
            Files.createDirectories(path.getParent()); // 부모 디렉토리를 먼저 생성
            multipartFile.transferTo(path); // 파일 저장
        } catch (IOException e) {
            System.out.println("create file failed: " + e.getMessage());
            throw e; // 예외를 던져 처리하도록 변경
        }

        return uploadImageRepository.save(UploadImage.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .build());
    }

    @Transactional
    public void deleteImage(UploadImage uploadImage) throws IOException {
        uploadImageRepository.delete(uploadImage);
        Files.deleteIfExists(Paths.get(getFullPath(uploadImage.getSavedFilename())));
    }
    @Transactional
    public Long deleteBoard(Long boardId, String category) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();
        User boardUser = board.getUser();
        boardUser.likeChange(boardUser.getReceivedLikeCnt() - board.getLikeCnt());
        if (board.getUploadImage() != null) {
            // 로컬 파일 시스템에서 이미지 삭제
            deleteImage(board.getUploadImage());  // deleteImage() 호출
            board.setUploadImage(null);
        }

        boardRepository.deleteById(boardId);
        return boardId;
    }
    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public ResponseEntity<UrlResource> downloadImage(Long boardId) throws MalformedURLException {
        // boardId에 해당하는 게시글이 없으면 null return
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null || board.getUploadImage() == null) {
            return null; // 에러 처리를 예외를 던지는 방식이 더 나을 수 있음
        }

        UrlResource urlResource = new UrlResource("file:" + getFullPath(board.getUploadImage().getSavedFilename()));

        // 업로드 한 파일명이 한글인 경우 아래 작업을 안해주면 한글이 깨질 수 있음
        String encodedUploadFileName = UriUtils.encode(board.getUploadImage().getOriginalFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
