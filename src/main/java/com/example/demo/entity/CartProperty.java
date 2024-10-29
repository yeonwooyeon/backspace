package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartProperty {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer info_no; // 조인 테이블의 고유 ID

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart; // Cart와의 관계

    @ManyToOne
    @JoinColumn(name = "property_id")
     Property property; // Property와의 관계

	public Integer getId() {
		return info_no;
	}

	public void setId(Integer info_no) {
		this.info_no = info_no;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}
    
    
}
