package com.vacancy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdPayController {
	@GetMapping("/adpayment")
	public String adpayment(Model model, Authentication authentication) {
		String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
		model.addAttribute("loginId", loginId);
		return "adpayment"; // html 템플릿을 렌더링
	}

}
