package com.study.basicboard.config.auth;

import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;
import com.study.basicboard.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        // 세션 유지 시간 = 3600초
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);

        User loginUser = userRepository.findByLoginId(authentication.getName());

        // 응답의 ContentType과 Character Encoding 설정
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter pw = response.getWriter();
        
        if (loginUser.getUserRole().equals(UserRole.BLACKLIST)) {
            pw.println("<script>alert('" + loginUser.getNickname() +
                    "님은 블랙리스트 입니다. 글, 댓글 작성이 불가능합니다.'); location.href='/';</script>");
        } else {
            String prevPage = (String) request.getSession().getAttribute("prevPage");
            if (prevPage != null) {
                pw.println("<script>alert('" + loginUser.getNickname() +
                        "님 반갑습니다!'); location.href='" + prevPage + "';</script>");
            } else {
                pw.println("<script>alert('" + loginUser.getNickname() +
                        "님 반갑습니다!'); location.href='/';</script>");
            }
        }
        pw.flush();
    }
}
