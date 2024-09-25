package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Property;
import com.example.demo.repository.PropertyRepository;
import com.study.basicboard.domain.entity.User;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;
	
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
	//사용자별 매물 목록 조회
    public List<Property> getPropertiesByUserId(Long id) {
        return propertyRepository.findByUserId(id);
    }
    public User findByUsername(String username) {
        return propertyRepository.findByUsername(username); // 수정된 부분
    }
    
    
}