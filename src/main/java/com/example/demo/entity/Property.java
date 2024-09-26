package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer info_no; // 건물번호
	private Long id; // 회원넘버
	private String info_name; // 건물명
	private String info_add; // 건물주소
	private String info_option; // 건물유형
	private Integer info_fl; // 건물해당층수
	private Integer info_allfl; // 건물전체층수
	private Integer room_num; // 방호실
	private Integer option_money; // 권리금
	private Integer info_year; // 전세금
	private Integer info_deposit; // 보증금
	private Integer info_month; // 월세금
	private Integer option_cost; // 관리비
	private Integer info_sell; // 매매금
	private Float info_size; // 건물면적
	private Integer info_count; // 방갯수
	private Date info_comp; // 준공일
	private Date info_move; // 입주가능일
	private String option_op; // 상세옵션
	private Timestamp info_upload; // 등록날짜
	private Timestamp info_update; // 업데이트 날짜
	private Boolean info_delflag; // 삭제플래그
	private Timestamp info_piry; // 판매가능기간
	private String info_type; // 매물유형
	private Integer info_hits; // 조회수
	private Timestamp info_ok; // 확인날짜
	private double info_latitude; // 위도
	private double info_longitude; // 경도
	private String option_etc; //기타옵션
	private String option_notes; //비고
	private String info_operators; //등기부등본URL
	
	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Image> images; // Image 엔티티와의 관계

	public Integer getInfo_no() {
		return info_no;
	}

	public void setInfo_no(Integer info_no) {
		this.info_no = info_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInfo_name() {
		return info_name;
	}

	public void setInfo_name(String info_name) {
		this.info_name = info_name;
	}

	public String getInfo_add() {
		return info_add;
	}

	public void setInfo_add(String info_add) {
		this.info_add = info_add;
	}

	public Float getInfo_size() {
		return info_size;
	}

	public void setInfo_size(Float info_size) {
		this.info_size = info_size;
	}

	public Integer getInfo_allfl() {
		return info_allfl;
	}

	public void setInfo_allfl(Integer info_allfl) {
		this.info_allfl = info_allfl;
	}

	public Integer getInfo_fl() {
		return info_fl;
	}

	public void setInfo_fl(Integer info_fl) {
		this.info_fl = info_fl;
	}

	public Integer getInfo_count() {
		return info_count;
	}

	public void setInfo_count(Integer info_count) {
		this.info_count = info_count;
	}

	public Timestamp getInfo_upload() {
		return info_upload;
	}

	public void setInfo_upload(Timestamp info_upload) {
		this.info_upload = info_upload;
	}

	public Timestamp getInfo_update() {
		return info_update;
	}

	public void setInfo_update(Timestamp info_update) {
		this.info_update = info_update;
	}

	public Boolean getInfo_delflag() {
		return info_delflag;
	}

	public void setInfo_delflag(Boolean info_delflag) {
		this.info_delflag = info_delflag;
	}

	public Timestamp getInfo_piry() {
		return info_piry;
	}

	public void setInfo_piry(Timestamp info_piry) {
		this.info_piry = info_piry;
	}

	public String getInfo_option() {
		return info_option;
	}

	public void setInfo_option(String info_option) {
		this.info_option = info_option;
	}

	public String getInfo_type() {
		return info_type;
	}

	public void setInfo_type(String info_type) {
		this.info_type = info_type;
	}

	public Integer getInfo_sell() {
		return info_sell;
	}

	public void setInfo_sell(Integer info_sell) {
		this.info_sell = info_sell;
	}

	public Integer getInfo_year() {
		return info_year;
	}

	public void setInfo_year(Integer info_year) {
		this.info_year = info_year;
	}

	public Integer getInfo_month() {
		return info_month;
	}

	public void setInfo_month(Integer info_month) {
		this.info_month = info_month;
	}

	public Integer getInfo_deposit() {
		return info_deposit;
	}

	public void setInfo_deposit(Integer info_deposit) {
		this.info_deposit = info_deposit;
	}

	public Integer getInfo_hits() {
		return info_hits;
	}

	public void setInfo_hits(Integer info_hits) {
		this.info_hits = info_hits;
	}

	public Date getInfo_comp() {
		return info_comp;
	}

	public void setInfo_comp(Date info_comp) {
		this.info_comp = info_comp;
	}

	public Timestamp getInfo_ok() {
		return info_ok;
	}

	public void setInfo_ok(Timestamp info_ok) {
		this.info_ok = info_ok;
	}

	public Date getInfo_move() {
		return info_move;
	}

	public void setInfo_move(Date info_move) {
		this.info_move = info_move;
	}

	public double getInfo_latitude() {
		return info_latitude;
	}

	public void setInfo_latitude(double info_latitude) {
		this.info_latitude = info_latitude;
	}

	public double getInfo_longitude() {
		return info_longitude;
	}

	public void setInfo_longitude(double info_longitude) {
		this.info_longitude = info_longitude;
	}

	public Integer getRoom_num() {
		return room_num;
	}

	public void setRoom_num(Integer room_num) {
		this.room_num = room_num;
	}

	public Integer getOption_money() {
		return option_money;
	}

	public void setOption_money(Integer option_money) {
		this.option_money = option_money;
	}

	public Integer getOption_cost() {
		return option_cost;
	}

	public void setOption_cost(Integer option_cost) {
		this.option_cost = option_cost;
	}

	public String getOption_op() {
		return option_op;
	}

	public void setOption_op(String option_op) {
		this.option_op = option_op;
	}

	public String getOption_etc() {
		return option_etc;
	}

	public void setOption_etc(String option_etc) {
		this.option_etc = option_etc;
	}

	public String getOption_notes() {
		return option_notes;
	}

	public void setOption_notes(String option_notes) {
		this.option_notes = option_notes;
	}

	public String getInfo_operators() {
		return info_operators;
	}

	public void setInfo_operators(String info_operators) {
		this.info_operators = info_operators;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

}
