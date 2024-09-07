package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.domain.Property;
import com.example.demo.service.PropertyService;

@Controller
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

	@GetMapping("/property")
	public String requestPropertyList(Model model) {
		List<Property> list = propertyService.getAllPropertylist();
		model.addAttribute("propertyList", list);
		return "property";
	}

    @GetMapping("/propertytest")
    public String propertytest() {
        return "propertytest";
    }

}