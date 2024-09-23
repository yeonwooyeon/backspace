package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.example.demo.Property;

@Mapper
public interface PropertyRepository {
    List<Property> getAllPropertyList();
    void insertProperty(Property property);
    List<Property> getAllAddresses();
    List<Property> searchAddresses(String keyword);
    void deleteProperty(Integer info_no);
}