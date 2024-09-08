package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.Property;
import com.example.demo.service.PropertyService;

@Controller
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

	@GetMapping
	public String requestPropertyList(Model model) {
		List<Property> list = propertyService.getAllPropertylist();
		model.addAttribute("propertyList", list);
		return "property";
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
}