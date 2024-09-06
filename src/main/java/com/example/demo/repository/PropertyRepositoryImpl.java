package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.domain.Property;

@Repository
public class PropertyRepositoryImpl implements PropertyRepository {
	private List<Property> listOfProperty = new ArrayList<Property>();
	
	public PropertyRepositoryImpl() {
		Property Property1 = new Property(00001, "세아빌딩");
		Property1.setInfo_add("대전폴리텍 앞인디");
		Property1.setInfo_sell(500);
		
		listOfProperty.add(Property1);
	}

	@Override
	public List<Property> getAllPropertyList() {
		// TODO Auto-generated method stub
		return listOfProperty;
	}
	
	}


