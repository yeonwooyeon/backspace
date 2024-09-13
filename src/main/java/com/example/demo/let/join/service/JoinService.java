package com.example.demo.let.join.service;

import java.util.List;

import org.springframework.stereotype.Service;




public interface JoinService {
	
	// 아이디 중복체크
	public int duplicateCheck(JoinVO vo) throws Exception;
	
	// 회원가입
	public String insertJoin(JoinVO vo) throws Exception;

}