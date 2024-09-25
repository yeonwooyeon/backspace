package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.entity.Property;
import com.example.demo.service.PropertyService;
import com.study.basicboard.domain.entity.User;

@Controller
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

    @GetMapping
    public String showProperties(Model model, Principal principal) {
        // 로그인 상태 확인
        if (principal == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/users/login";
        }
        
        String username = principal.getName();
        User user = propertyService.findByUsername(username); // 사용자 정보 가져오기
        Long userId = user.getId(); // 사용자 ID 가져오기

        // 사용자 ID에 따라 매물 목록 가져오기
        List<Property> userProperties = propertyService.getPropertiesByUserId(userId);
        model.addAttribute("propertyList", userProperties); // 모델에 매물 목록 추가
        model.addAttribute("loggedIn", true);
        
        return "property"; // property.html로 이동
    }
    
    @GetMapping("/propregister")
    public String propregister() {
        return "propregister";
    }
    
    @PostMapping("/addProperty")
    public RedirectView addProperty(@ModelAttribute Property property, Principal principal) {
    	String userId = principal.getName();// 현재 로그인한 사용자 ID 가져오기
    	property.setId(Long.parseLong(userId));// 사용자의 ID를 Property에 설정
    	
    	propertyService.addProperty(property);
        return new RedirectView("/property/manage");
    }
    
    @PostMapping("/deleteProperty")
    public RedirectView deleteProperty(@RequestParam Integer info_no) {
        propertyService.deleteProperty(info_no);
        return new RedirectView("/property");
    }
    
}