package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Cart;

public interface CartService {
	void addToCart(Cart cart);
	List<Cart> getCart();
	void clearCart(Long userId);
}
