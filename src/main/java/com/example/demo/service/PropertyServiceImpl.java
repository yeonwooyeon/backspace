package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Image;
import com.example.demo.entity.Property;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.PropertyRepository;
import com.study.basicboard.domain.entity.User;

@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;
	
	 @Autowired
	 private ImageRepository imageRepository;
	
    public void addProperty(Property property, MultipartFile[] photos, MultipartFile operatorFile) {
        if (operatorFile != null && !operatorFile.isEmpty()) {
            String operatorFileUrl = savePhoto(operatorFile); // FileService 사용
            property.setInfo_operators(operatorFileUrl); // URL 설정
        }
    	
    	propertyRepository.insertProperty(property);
        Integer info_no = property.getInfo_no();

        // 이미지 저장
        saveImages(info_no, photos);
    }
    

    private void saveImages(Integer info_no, MultipartFile[] photos) {
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String insideUrl = savePhoto(photo); // 파일 저장 로직 호출
                String insideName = photo.getOriginalFilename();
                
                // Image 객체 생성 및 저장
                Image image = new Image();
                image.setInfo_no(info_no.intValue()); // info_no 설정
                image.setSi_insideurl(insideUrl); // 내부 사진 URL
                image.setSi_insidename(insideName); // 내부 사진 이름
                image.setSi_create(new Timestamp(System.currentTimeMillis())); // 생성 날짜
                imageRepository.save(image); // 이미지 저장
            }
        }
    }


    private String savePhoto(MultipartFile photo) {
        try {
            String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
            Path path = Paths.get("src/main/resources/static/images/uploads/" + fileName);
            
            // 디렉토리 체크 및 생성
            File directory = new File("src/main/resources/static/images/uploads");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // 파일 저장
            Files.copy(photo.getInputStream(), path);
            return "/images/uploads/" + fileName; // 실제 URL 경로
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 실패 시 null 반환
        }
    }



	public List<Property> getAllAddresses() {
        return propertyRepository.getAllAddresses();
    }

    public List<Property> searchAddresses(String keyword) {
        return propertyRepository.searchAddresses(keyword);
    }
    
    public void deleteProperty(Integer info_no) {
        propertyRepository.deleteProperty(info_no);
    }
	//사용자별 매물 목록 조회
    public List<Property> getPropertiesByUserId(Long id) {
        return propertyRepository.findByUserId(id);
    }
    public User findByUsername(String username) {
        return propertyRepository.findByUsername(username);
    }
    // Property ID로 이미지 목록을 가져오는 메서드 추가
    public List<Image> getImagesByPropertyId(Integer info_no) {
        return imageRepository.getImagesByPropertyId(info_no);
    }


	@Override
	public Property getPropertyById(Integer info_no) {
		 return propertyRepository.getPropertyById(info_no);
	}


	@Override
	public void updateProperty(Property property) {
		propertyRepository.updateProperty(property);
		
	}


	
	public Property getPropertyDetails(Integer info_no) {
		return propertyRepository.getPropertyById(info_no);
	}
}