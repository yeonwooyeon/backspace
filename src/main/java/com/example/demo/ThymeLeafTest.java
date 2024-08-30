package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ThymeLeafTest {
    @RequestMapping("/")
    public String map() {
        return "extras/map"; // templates/extras/map.html 파일을 Thymeleaf가 렌더링하도록 설정
    }
    
    public String hello() {
        return "extras/hello";
    }
    
    public String index() {
        return "extras/index";
    }
}