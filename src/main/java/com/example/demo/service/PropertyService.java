package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Image;
import com.example.demo.entity.Property;
import com.study.basicboard.domain.entity.User;

public interface PropertyService {
	void addProperty(Property property, MultipartFile[] photos);
	List<Property> getAllAddresses();
	List<Property> searchAddresses(String keyword);
	void deleteProperty(Integer info_no);
	List<Property> getPropertiesByUserId(Long userId);
    User findByUsername(String username);
	List<Image> getImagesByPropertyId(Integer info_no);
}