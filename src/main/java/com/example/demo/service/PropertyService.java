package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Image;
import com.example.demo.entity.Property;
import com.study.basicboard.domain.entity.User;

public interface PropertyService {
	void addProperty(Property property, MultipartFile[] photos, MultipartFile operatorFile);
	List<Property> getAllAddresses();
	List<Property> searchAddresses(String keyword);
	void deleteProperty(Integer info_no);
	List<Property> getPropertiesByUserId(Long userId, Pageable pageable);
    User findByUsername(String username);
	List<Image> getImagesByPropertyId(Integer info_no);
	Property getPropertyById(Integer info_no);
	void updateProperty(Property property);
	Property getPropertyDetails(Integer info_no);
	Object getTotalPages(Long userId, int size);
}