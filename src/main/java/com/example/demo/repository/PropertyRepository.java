package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.Property;

@Mapper
public interface PropertyRepository{
	List<Property> getAllPropertyList();
	
	@Insert("INSERT INTO Pp_info (info_name, info_add, info_size, info_allfl, info_fl, info_count, info_upload, info_update, info_delflag, info_option, info_type, info_sell, info_year, info_month, info_deposit, info_comp, info_move) "
			+ "VALUES (#{info_name}, #{info_add}, #{info_size}, #{info_allfl}, #{info_fl}, #{info_count}, #{info_upload}, #{info_update}, #{info_delflag}, #{info_option}, #{info_type}, #{info_sell}, #{info_year}, #{info_month}, #{info_deposit}, #{info_comp}, #{info_move})")
	void insertProperty(Property property);
	
}