package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdPayController {
	@GetMapping("/property/adpayment")
	public String adpayment() {
		return "adpayment"; // html 템플릿을 렌더링
	}
}
