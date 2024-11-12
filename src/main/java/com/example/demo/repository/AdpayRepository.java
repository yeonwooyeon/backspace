package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.AdPay;

@Mapper
public interface AdpayRepository {
	 void insertAdPay(AdPay adPay);
	    AdPay selectAdPayById(@Param("info_no") Integer info_no);
	    List<AdPay> selectAllAdPays();
	    void updateAdPay(AdPay adPay);
	    void deleteAdPayById(@Param("info_no") Integer info_no);
}
