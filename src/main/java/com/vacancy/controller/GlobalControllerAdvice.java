package com.vacancy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.study.basicboard.service.UserService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
@Component
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute
    public void addAuthenticationAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 상태 확인
        boolean loggedIn = authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                && "anonymousUser".equals(authentication.getPrincipal()));
        model.addAttribute("loggedIn", loggedIn);

        // 로그인된 경우 로그인 ID와 닉네임 추가
        if (loggedIn) {
            String loginId = authentication.getName(); // 로그인된 사용자의 ID 가져오기
            model.addAttribute("loginId", loginId);

            // UserService를 통해 닉네임 가져오기
            String nickname = userService.myInfo(loginId).getNickname();
            model.addAttribute("userNickname", nickname); // 닉네임 추가
        }
    }
}