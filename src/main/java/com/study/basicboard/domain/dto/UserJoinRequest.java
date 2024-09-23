package com.study.basicboard.domain.dto;

import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserJoinRequest {
    private String loginId;
    private String password; // 비밀번호
    private String passwordCheck; // 비밀번호 확인용
    private String nickname; // 닉네임
    private String email; // 이메일 필드 추가
    private LocalDateTime createdAt; // 가입 시간

    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .email(email) // 이메일을 설정
                .createdAt(LocalDateTime.now()) // 가입 시간
                .userRole(UserRole.BRONZE) // 기본 권한 설정
                .build();
    }
}
