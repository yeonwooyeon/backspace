package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.entity.Image;
import com.example.demo.entity.Property;
import com.example.demo.service.PropertyService;
import com.study.basicboard.domain.entity.User;

@Controller
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

    @GetMapping
    public String showProperties(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, 
    							Model model, Principal principal) {
        // 로그인 상태 확인
        if (principal == null) {
            return "redirect:/users/login";
        }
        
        String username = principal.getName();
        User user = propertyService.findByUsername(username);
        Long userId = user.getId();
        
        // 페이지 요청 생성
        Pageable pageable = PageRequest.of(page, size);

        // 사용자 ID에 따라 매물 목록을 페이지네이션하여 가져오기
        List<Property> userProperties = propertyService.getPropertiesByUserId(userId, pageable);
        
        // 각 매물에 대한 이미지 목록을 추가
        for (Property property : userProperties) {
            List<Image> images = propertyService.getImagesByPropertyId(property.getInfo_no());
            property.setImages(images); // Property 객체에 이미지 목록 설정
        }
        
        model.addAttribute("propertyList", userProperties);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", propertyService.getTotalPages(userId, size));
        model.addAttribute("loggedIn", true);
        
        return "property"; // property.html로 이동
    }
    
    @GetMapping("/propregister")
    public String propregister() {
        return "propregister";
    }
    
    @PostMapping("/addProperty")
    public RedirectView addProperty(@ModelAttribute Property property, 
    								@RequestParam MultipartFile[] photos,
    								@RequestParam MultipartFile operatorFile,
    								Principal principal) {
    	String username = principal.getName();// 현재 로그인한 사용자 ID 가져오기
    	 // 사용자 정보를 조회하여 실제 ID를 가져오기
    	User user = propertyService.findByUsername(username); // 사용자 로그인 ID로 실제 ID 조회
    	Long userId = user.getId();
    	property.setId(userId);// 사용자의 ID를 Property에 설정
    	
    	propertyService.addProperty(property, photos, operatorFile);
        return new RedirectView("/property");
    }
    
    @PostMapping("/deleteProperty")
    public RedirectView deleteProperty(@RequestParam Integer info_no) {
        propertyService.deleteProperty(info_no);
        return new RedirectView("/property");
    }
    
    @GetMapping("/editProperty")
    public String editProperty(@RequestParam Integer info_no, Model model) {
        Property property = propertyService.getPropertyById(info_no);
        model.addAttribute("property", property);
        return "propregister"; // 수정된 propregister.html로 이동
    }
    
    @PostMapping("/updateProperty")
    public RedirectView updateProperty(@ModelAttribute Property property,
							    		@RequestParam MultipartFile[] photos,
										@RequestParam MultipartFile operatorFile,
    									Principal principal) {
        // 현재 로그인한 사용자 ID 설정
        String username = principal.getName();
        User user = propertyService.findByUsername(username);
        property.setId(user.getId());
        
        propertyService.updateProperty(property, photos, operatorFile);
        return new RedirectView("/property");
    }
    
    @GetMapping("/view")
    public ResponseEntity<Property> viewProperty(@RequestParam Integer info_no) {
        // 데이터베이스에서 infoNo에 해당하는 Property 조회
    	Property selectedProperty = propertyService.getPropertyDetails(info_no);

     // JSON 형태로 반환
        return ResponseEntity.ok(selectedProperty);
    }
    
}