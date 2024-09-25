package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Property;

public interface PropertyService {
	List<Property> getAllPropertylist();
	void addProperty(Property property);
	List<Property> getAllAddresses();
	List<Property> searchAddresses(String keyword);
	void deleteProperty(Integer info_no);
	List<Property> getPropertiesByUserId(String id);
}