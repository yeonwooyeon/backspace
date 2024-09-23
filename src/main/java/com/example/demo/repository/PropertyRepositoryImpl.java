package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.Property;

@Repository
public class PropertyRepositoryImpl implements PropertyRepository {
	private List<Property> listOfProperty = new ArrayList<Property>();

	public PropertyRepositoryImpl() {
		Property Property1 = new Property();
		Property1.setInfo_add("대전폴리텍 앞인디");
		Property1.setInfo_sell(500);

		listOfProperty.add(Property1);
	}

	@Override
	public List<Property> getAllPropertyList() {
		return listOfProperty;
	}

    @Override
    public void insertProperty(Property property) {
        listOfProperty.add(property);
    }
    
	@Override
	public List<Property> getAllAddresses() {
		return listOfProperty;
	}
	
	@Override
	public List<Property> searchAddresses(String keyword) {
		return listOfProperty;
	}

	public void deleteProperty(Integer info_no) {
		listOfProperty.removeIf(property -> property.getInfo_no().equals(info_no));
	}
    

	}
