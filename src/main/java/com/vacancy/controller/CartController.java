package com.example.demo.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartProperty;
import com.example.demo.entity.CartRequest;
import com.example.demo.entity.Property;
import com.example.demo.service.CartService;
import com.example.demo.service.PropertyService;
import com.study.basicboard.domain.entity.User;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private PropertyService propertyService;
	
	@PostMapping("/add-to-cart")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody CartRequest cartRequest) {
		
		 Long userId = cartRequest.getUserId();  // DTO에서 userId 가져오기
		 Integer propertyId = cartRequest.getPropertyId();  // DTO에서 propertyId 가져오기
		
		// 장바구니 객체 생성
        Cart cart = new Cart();
        cart.setInfo_no(propertyId);
        cart.setId(userId);
        cart.setWish_status("찜함"); // 찜한 상태
        cart.setWish_date(new Timestamp(System.currentTimeMillis())); // 현재 시간으로 설정

        // Cart 저장 (장바구니가 없으면 새로 생성)
        cartService.addToCart(cart);

        // CartProperty 리스트 생성
        CartProperty cartProperty = new CartProperty();
        Property property = propertyService.getPropertyById(propertyId);
        if (property != null) {  
            cartProperty.setCart(cart);
            cartProperty.setProperty(property);
            cartService.addCartProperties(cartProperty); 
        }
     // 응답으로 성공 메시지를 반환
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response); // 추가 후 맵으로 리디렉션
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        // 로그인 상태 확인
        if (principal == null) {
            return "redirect:/users/login"; // 로그인하지 않았다면 로그인 페이지로 리디렉션
        }

        String username = principal.getName();
        User user = propertyService.findByUsername(username);
        Long userId = user.getId();

        // 장바구니 아이템 조회
        List<Cart> cartItems = cartService.getCart(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("loginId", username); // 로그인 ID 추가

        return "cart"; // cart.html 템플릿을 렌더링
    }

    @PostMapping("/clear-cart")
    public String clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return "redirect:/cart"; // 장바구니를 비운 후 다시 장바구니로 리디렉션
    }
}
