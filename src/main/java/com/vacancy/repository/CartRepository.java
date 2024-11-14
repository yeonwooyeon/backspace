package com.vacancy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.vacancy.entity.Cart;

@Mapper
public interface CartRepository {
	void addCart(Cart cart);
    List<Cart> getCartByUserId(Long userId);
    void clearCart(Long userId);
    void addCartProperty(Integer cartId, Integer propertyId);
}
