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

@Controller
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

    @GetMapping
    public String requestPropertyList(Model model) {
        List<Property> propertyList = propertyService.getAllPropertylist(); // 모든 Property 객체 가져오기
        propertyList.forEach(p -> System.out.println(p.getInfo_name())); // 데이터 확인용 로그 출력
        model.addAttribute("propertyList", propertyList); // 모델에 추가
        return "property"; // property.html로 이동
    }

    @GetMapping("/manage")
    public String showProperties(Model model, Principal principal) {
        // 로그인 상태 확인
        if (principal == null) {
            // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
            return "redirect:/users/login";
        }
        // 관리물건 페이지 로드
        model.addAttribute("loggedIn", true);
        // 여기에 다른 로직 추가
        return "property"; // property.html
    }
    
    @GetMapping("/propregister")
    public String propregister() {
        return "propregister";
    }
    
    @PostMapping("/addProperty")
    public RedirectView addProperty(@ModelAttribute Property property) {
        propertyService.addProperty(property);
        return new RedirectView("/property");
    }
    
    @PostMapping("/deleteProperty")
    public RedirectView deleteProperty(@RequestParam Integer info_no) {
        propertyService.deleteProperty(info_no);
        return new RedirectView("/property");
    }
    
}