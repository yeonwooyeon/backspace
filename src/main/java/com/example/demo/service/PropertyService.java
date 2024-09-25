package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Property;
import com.study.basicboard.domain.entity.User;

public interface PropertyService {
	void addProperty(Property property);
	List<Property> getAllAddresses();
	List<Property> searchAddresses(String keyword);
	void deleteProperty(Integer info_no);
	List<Property> getPropertiesByUserId(Long userId);
    User findByUsername(String username);
}