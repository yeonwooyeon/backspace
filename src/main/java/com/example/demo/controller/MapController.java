package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class MapController {
	
    @GetMapping("/map")
    public String map() {
        return "map";  // map.html 템플릿을 렌더링
    }
    
    @GetMapping("/map2")
    public String map2() {
        return "map2";  // map2.html 템플릿을 렌더링
    }
}