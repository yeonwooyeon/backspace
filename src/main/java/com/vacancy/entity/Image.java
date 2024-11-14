package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Image {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer img_no; //사진번호
	private Integer info_no; //건물번호
	private Timestamp si_create; //올린날짜
	private Timestamp si_update; //수정한날짜
    private String si_insideurl; //내부사진url
	private String si_outsideurl; //외부사진url
    private String si_insidename; //내부사진이름
	private String si_outsidename; //외부사진이름
	
	 @ManyToOne
	    @JoinColumn(name = "property_id")
	 public Property property; // Property와의 관계
	
	public Integer getImg_no() {
		return img_no;
	}
	public void setImg_no(Integer img_no) {
		this.img_no = img_no;
	}
	public Integer getInfo_no() {
		return info_no;
	}
	public void setInfo_no(Integer info_no) {
		this.info_no = info_no;
	}
	public Timestamp getSi_create() {
		return si_create;
	}
	public void setSi_create(Timestamp si_create) {
		this.si_create = si_create;
	}
	public Timestamp getSi_update() {
		return si_update;
	}
	public void setSi_update(Timestamp si_update) {
		this.si_update = si_update;
	}
	public String getSi_insideurl() {
		return si_insideurl;
	}
	public void setSi_insideurl(String si_insideurl) {
		this.si_insideurl = si_insideurl;
	}
	public String getSi_outsideurl() {
		return si_outsideurl;
	}
	public void setSi_outsideurl(String si_outsideurl) {
		this.si_outsideurl = si_outsideurl;
	}
	public String getSi_insidename() {
		return si_insidename;
	}
	public void setSi_insidename(String si_insidename) {
		this.si_insidename = si_insidename;
	}
	public String getSi_outsidename() {
		return si_outsidename;
	}
	public void setSi_outsidename(String si_outsidename) {
		this.si_outsidename = si_outsidename;
	}

}
