package com.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

	
	@GetMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("name", "타임리프테스트");
		return "hello";
	}
	
	@GetMapping("/")
	public String home(Model model, Authentication authentication) {
	    boolean loggedIn = authentication != null && authentication.isAuthenticated();
	    model.addAttribute("loggedIn", loggedIn);
	    return "index";  // index.html 템플릿을 반환한다고 가정
	}
	
}