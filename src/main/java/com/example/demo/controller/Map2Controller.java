package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Property;
import com.example.demo.service.PropertyService;

@RestController
public class Map2Controller {

    @Autowired
    private PropertyService propertyService;

    public Map2Controller(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/addresses")
    public List<Property> getAllAddresses() {
        return propertyService.getAllAddresses();
    }
 
    @GetMapping("/search")
    public List<Property> searchAddresses(@RequestParam String keyword) {
        // 검색된 주소 또는 이름을 기반으로 매물 검색
        return propertyService.searchAddresses(keyword);
    }
}