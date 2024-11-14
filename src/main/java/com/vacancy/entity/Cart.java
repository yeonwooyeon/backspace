package com.vacancy.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer info_no; //건물번호
	private Long id; // 회원넘버
	private Integer agent_no;
	private Timestamp wish_date; // 찜한날짜
	private String wish_status; // 찜한상태
	
	 @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 public List<CartProperty> cartProperties = new ArrayList<>(); // Cart와 Property 관계
	
	public Integer getInfo_no() {
		return info_no;
	}
	public void setInfo_no(Integer info_no) {
		this.info_no = info_no;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getAgnet_no() {
		return agent_no;
	}
	public void setAgnet_no(Integer agnet_no) {
		this.agent_no = agnet_no;
	}
	public Timestamp getWish_date() {
		return wish_date;
	}
	public void setWish_date(Timestamp wish_date) {
		this.wish_date = wish_date;
	}
	public String getWish_status() {
		return wish_status;
	}
	public void setWish_status(String wish_status) {
		this.wish_status = wish_status;
	}
	public List<CartProperty> getCartProperties() {
		return cartProperties;
	}
	public void setCartProperties(List<CartProperty> cartProperties) {
		this.cartProperties = cartProperties;
	}


}
