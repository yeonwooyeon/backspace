package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Image;

@Mapper
public interface ImageRepository {

	void save(Image image);

}
