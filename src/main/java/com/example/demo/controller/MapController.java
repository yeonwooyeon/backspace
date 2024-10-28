package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MapController {

	@GetMapping("/")
	public String home(Model model, Authentication authentication) {
		boolean loggedIn = authentication != null && authentication.isAuthenticated();
		model.addAttribute("loggedIn", loggedIn);
		return "main";
	}

	@GetMapping("/map")
	public String map() {
		return "map"; // map.html 템플릿을 렌더링
	}


}