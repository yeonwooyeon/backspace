package com.study.basicboard.config.auth;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

	/*
	 * public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse
	 * response, Authentication authentication) throws IOException, ServletException
	 * { response.setContentType("text/html"); PrintWriter out =
	 * response.getWriter();
	 * out.println("<script>alert('로그아웃 되었습니다.'); location.href='/';</script>");
	 * out.flush(); }
	 */

	@Override
	public void onLogoutSuccess(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, Authentication authentication)
			throws IOException, jakarta.servlet.ServletException {
		response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('로그아웃 되었습니다.'); location.href='/';</script>");
	        out.flush();
		
	}
}
