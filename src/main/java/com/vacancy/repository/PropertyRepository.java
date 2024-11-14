package com.vacancy.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.study.basicboard.domain.entity.User;
import com.vacancy.entity.Property;

@Mapper
public interface PropertyRepository {
    void insertProperty(Property property);
    List<Property> getAllAddresses();
    List<Property> searchAddresses(String keyword);
    void deleteProperty(Integer info_no);
    List<Property> findByUserId(@Param("id") Long userId, @Param("startIndex") int startIndex, @Param("pageSize") int pageSize);
	// 사용자 이름으로 사용자 정보를 조회하는 메서드
    User findByUsername(@Param("login_id") String login_id);
	Property getPropertyById(@Param("info_no")Integer info_no);
	void updateProperty(Property property);
	int countPropertiesByUserId(Long userId);
}