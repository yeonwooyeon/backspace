package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	
	@GetMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("name", "타임리프테스트");
		return "hello";
	}
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
}