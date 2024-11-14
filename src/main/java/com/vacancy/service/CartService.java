package com.vacancy.service;

import java.util.List;

import com.vacancy.entity.Cart;
import com.vacancy.entity.CartProperty;

public interface CartService {
	void addToCart(Cart cart);
	void clearCart(Long userId);
	List<Cart> getCart(Long userId);
	void addCartProperties(CartProperty cartProperties);
}
