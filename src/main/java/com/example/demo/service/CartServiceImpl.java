package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartProperty;
import com.example.demo.entity.Property;
import com.example.demo.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
    private CartRepository cartRepository;
	
	public void addToCart(Cart cart) {
	   
		// 장바구니 정보 저장
	    cartRepository.addCart(cart);

	    // 각 Property를 CartProperty로 추가
	    for (CartProperty cartProperty : cart.getCartProperties()) {
	        Property property = cartProperty.getProperty(); // Property 객체 가져오기
	        cartRepository.addCartProperty(cart.getId(), property.getInfo_no());
	    }
	}

	    public List<Cart> getCart(Long userId) {
	        return cartRepository.getCartByUserId(userId);
	    }

		@Override
		public List<Cart> getCart() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void clearCart(Long userId) {
			// TODO Auto-generated method stub
			
		}

	}