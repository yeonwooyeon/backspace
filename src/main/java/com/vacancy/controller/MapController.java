package com.vacancy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MapController {

	@GetMapping("/")
	public String home(Model model, Authentication authentication) {
		boolean loggedIn = authentication != null && authentication.isAuthenticated();
		model.addAttribute("loggedIn", loggedIn);
		if (loggedIn) {
			String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
			model.addAttribute("loginId", loginId);
		}
		return "main";
	}

	@GetMapping("/map")
	public String map(Model model, Authentication authentication) {
		boolean loggedIn = authentication != null && authentication.isAuthenticated();
		model.addAttribute("loggedIn", loggedIn);
		if (loggedIn) {
			String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
			model.addAttribute("loginId", loginId);
		}
		return "map";
	}

}