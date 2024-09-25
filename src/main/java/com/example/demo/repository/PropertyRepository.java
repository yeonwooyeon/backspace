package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.example.demo.entity.Property;
import com.study.basicboard.domain.entity.User;

@Mapper
public interface PropertyRepository {
    void insertProperty(Property property);
    List<Property> getAllAddresses();
    List<Property> searchAddresses(String keyword);
    void deleteProperty(Integer info_no);
	List<Property> findByUserId(Long id);
	// 사용자 이름으로 사용자 정보를 조회하는 메서드
    User findByUsername(@Param("login_id") String login_id);
	
}