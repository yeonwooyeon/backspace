package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartProperty;

public interface CartService {
	void addToCart(Cart cart);
	void clearCart(Long userId);
	List<Cart> getCart(Long userId);
	void addCartProperties(CartProperty cartProperties);
}
