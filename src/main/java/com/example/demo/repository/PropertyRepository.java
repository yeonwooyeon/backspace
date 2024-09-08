package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.Property;

@Mapper
public interface PropertyRepository {
    List<Property> getAllPropertyList();
    
    void insertProperty(Property property);
}