package com.vacancy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Component
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAuthenticationAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 상태 확인
        boolean loggedIn = authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                && "anonymousUser".equals(authentication.getPrincipal()));
        model.addAttribute("loggedIn", loggedIn);

        // 로그인된 경우 로그인 ID 추가
        if (loggedIn && authentication.getPrincipal() instanceof UserDetails) {
            String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
            model.addAttribute("loginId", loginId);
        }
    }
}
