package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Image;

@Mapper
public interface ImageRepository {
	void save(Image image);
    // 이미지 목록을 가져오는 메서드 추가
    List<Image> getImagesByPropertyId(@Param("info_no") Integer info_no);
	void deleteByNo(Integer img_no);
	void deleteByUrl(String imageUrl);
}
