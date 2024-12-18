package com.vacancy.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.study.basicboard.domain.entity.User;
import com.vacancy.entity.Image;
import com.vacancy.entity.Property;
import com.vacancy.repository.ImageRepository;
import com.vacancy.repository.PropertyRepository;

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
    	deleteImagesByPropertyId(info_no);
        propertyRepository.deleteProperty(info_no);
    }
 // 기존 이미지 삭제 메서드
    public void deleteImagesByPropertyId(Integer info_no) {
        List<Image> images = imageRepository.getImagesByPropertyId(info_no);
        for (Image image : images) {
            // 실제 파일 시스템에서 이미지 삭제
            Path path = Paths.get("src/main/resources/static" + image.getSi_insideurl());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageRepository.deleteByNo(image.getImg_no()); // 데이터베이스에서 이미지 삭제
        }
    }
    
	//사용자별 페이지별 매물 목록 조회
    public List<Property> getPropertiesByUserId(Long id, Pageable pageable) {
    	int startIndex = pageable.getPageNumber() * pageable.getPageSize();
        return propertyRepository.findByUserId(id, startIndex, pageable.getPageSize());
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
	public void updateProperty(Property property, MultipartFile[] photos, MultipartFile operatorFile) {
	    if (operatorFile != null && !operatorFile.isEmpty()) {
	        String operatorFileUrl = savePhoto(operatorFile); // 파일 저장
	        property.setInfo_operators(operatorFileUrl); // URL 설정
	    }
		
		propertyRepository.updateProperty(property);
		
		Integer info_no = property.getInfo_no();
		
		saveImages(info_no, photos);
	}

	public Property getPropertyDetails(Integer info_no) {
		return propertyRepository.getPropertyById(info_no);
	}


	@Override
	public Object getTotalPages(Long userId, int size) {
	    // 사용자 ID로 총 매물 수 가져오기
	    int totalProperties = propertyRepository.countPropertiesByUserId(userId);
	    return (int) Math.ceil((double) totalProperties / size);
	}


	@Override
	public void deleteImage(String imageUrl) {
		Path filePath = Paths.get("src/main/resources/static" + imageUrl);
	    try {
	        Files.deleteIfExists(filePath); // 파일 삭제
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    // 데이터베이스에서 이미지 삭제
	    imageRepository.deleteByUrl(imageUrl);
	}
}