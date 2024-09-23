package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Property;
import com.example.demo.repository.PropertyRepository;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;
 
	public List<Property> getAllPropertylist() {
		return propertyRepository.getAllPropertyList();
	}
	
    public void addProperty(Property property) {
        propertyRepository.insertProperty(property);
    }

    public List<Property> getAllAddresses() {
        return propertyRepository.getAllAddresses();
    }

    public List<Property> searchAddresses(String keyword) {
        return propertyRepository.searchAddresses(keyword);
    }
    
    public void deleteProperty(Integer info_no) {
        propertyRepository.deleteProperty(info_no);
    }
    
}