package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AdPay {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer info_no; //건물번호
	private Long id; // 회원넘버
}
