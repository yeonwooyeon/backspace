package com.vacancy.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.study.basicboard.domain.entity.User;
import com.vacancy.entity.Image;
import com.vacancy.entity.Property;

public interface PropertyService {
	void addProperty(Property property, MultipartFile[] photos, MultipartFile operatorFile);
	List<Property> getAllAddresses();
	List<Property> searchAddresses(String keyword);
	void deleteProperty(Integer info_no);
	List<Property> getPropertiesByUserId(Long userId, Pageable pageable);
    User findByUsername(String username);
	List<Image> getImagesByPropertyId(Integer info_no);
	Property getPropertyById(Integer info_no);
	void updateProperty(Property property, MultipartFile[] photos, MultipartFile operatorFile);
	Property getPropertyDetails(Integer info_no);
	Object getTotalPages(Long userId, int size);
	void deleteImage(String imageUrl);
}