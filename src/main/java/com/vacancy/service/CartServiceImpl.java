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
	
	 @Override
	    public void addToCart(Cart cart) {
	        cartRepository.addCart(cart);
	    }

	    @Override
	    public List<Cart> getCart(Long userId) {
	        return cartRepository.getCartByUserId(userId);
	    }

	    @Override
	    public void clearCart(Long userId) {
	        cartRepository.clearCart(userId);
	    }

	    @Override
	    public void addCartProperties(CartProperty cartProperties) {
	    	cartRepository.addCartProperty(cartProperties.getCart().getInfo_no(), cartProperties.getProperty().getInfo_no());
	    }
	}