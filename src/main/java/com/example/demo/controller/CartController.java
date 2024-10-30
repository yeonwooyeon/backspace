package com.example.demo.controller;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartProperty;
import com.example.demo.entity.Image;
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
	public String addToCart(@RequestParam List<Integer> propertyId, @RequestParam Long userId) {
	    Cart cart = new Cart();
	    cart.setId(userId);
	    cart.setWish_status("찜함"); // 찜한 상태
	    cart.setWish_date(new Timestamp(System.currentTimeMillis())); // 현재 시간으로 설정
	    
	    // Property 목록 가져오기
	    List<Property> properties = propertyService.getPropertiesByUserId(userId, null);
	    
	    // CartProperty 리스트 생성
	    List<CartProperty> cartProperties = new ArrayList<>();
	    for (Property property : properties) {
	        CartProperty cartProperty = new CartProperty();
	        cartProperty.setCart(cart);
	        cartProperty.setProperty(property);
	        cartProperties.add(cartProperty);
	    }
	    
	    // Cart에 CartProperty 설정
	    cart.setCartProperties(cartProperties);
	    
	    // 장바구니 추가
	    cartService.addToCart(cart);
	    return "redirect:/map"; // 추가 후 맵으로 리디렉션
	}

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal, Authentication authentication) {
    	 // 로그인 상태 확인
        if (principal == null) {
            return "redirect:/users/login";
        }
        String loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
		model.addAttribute("loginId", loginId);
        
        String username = principal.getName();
        User user = propertyService.findByUsername(username);
        Long userId = user.getId();        
        
        List<Cart> cartItems = cartService.getCart();
        model.addAttribute("cartItems", cartItems);
        return "cart"; // cart.html 템플릿을 렌더링
        
    }

    @PostMapping("/clear-cart")
    public String clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return "redirect:/cart"; // 장바구니를 비운 후 다시 장바구니로 리디렉션
    }
	
}
