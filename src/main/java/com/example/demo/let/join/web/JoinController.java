package com.example.demo.let.join.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.let.join.service.JoinService;
import com.example.demo.let.join.service.JoinVO;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Controller
public class JoinController {
	

	// 1. 약관동의
		@RequestMapping(value = "/join/siteUseAgree.do")
		public String siteUseAgree(@ModelAttribute("searchVO") JoinVO vo, HttpServletRequest request, ModelMap model,
				HttpSession session) throws Exception {

			return "thymeleaf/join/SiteUseAgree";
		}
		
		// 2. 회원구분
		@RequestMapping(value = "/join/memberType.do")
		public String memberType(@ModelAttribute("searchVO") JoinVO vo, HttpServletRequest request, ModelMap model,
				HttpSession session) throws Exception {
			
			// 약관동의
			if(!"Y".equals(vo.getAgree01()) || !"Y".equals(vo.getAgree02())) {
				model.addAttribute("message","잘못된 접근입니다.");
				return "forward:/join/siteUseAgree.do";
			}
			return "join/MemberType";
}
}