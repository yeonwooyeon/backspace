package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.Property;
import com.example.demo.repository.PropertyRepository;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;

	public List<Property> getAllPropertylist() {
		// TODO Auto-generated method stub
		return propertyRepository.getAllPropertyList();
	}

}