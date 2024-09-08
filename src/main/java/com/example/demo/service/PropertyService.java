package com.example.demo.service;

import java.util.List;

import com.example.demo.Property;

public interface PropertyService {
	List<Property> getAllPropertylist();
	void addProperty(Property property);
}