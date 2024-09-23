package com.study.basicboard.config.auth;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        

	@Override
	public void commence(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
			throws IOException, jakarta.servlet.ServletException {
		// 메세지 출력 후 홈으로 redirect
		response.setContentType("text/html; charset=UTF-8");
        PrintWriter pw = response.getWriter();
        pw.println("<script>alert('로그인한 유저만 가능합니다!'); location.href='/users/login';</script>");
        pw.flush();
    }
}
