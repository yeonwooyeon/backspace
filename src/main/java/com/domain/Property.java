package com.domain;

import java.time.LocalDateTime;

public class Property {
	private int info_no; //건물번호
	private String info_name; //건물명
	private String info_add; //건물주소
	private float info_size; //건물면적
	private int info_allfl; //건물전체층수
	private int info_fl; //건물해당층수
	private int info_count; //방갯수
	private LocalDateTime info_upload; //등록날짜
	private LocalDateTime info_update; //업데이트 날짜
	private boolean info_delflag; //삭제플래그
	private LocalDateTime info_piry; //판매가능기간
	private String info_option; //건물유형
	private String info_type; //매물유형
	private int info_sell; //매매가
	private int info_year; //전세가
	private int info_month; //월세
	private int info_deposit; //보증금
	private int info_hits; //조회수 
	private int info_comp; //사용승인일 //date 방법으로 수정 생각해보기
	private LocalDateTime info_ok; //확인날짜 
	private int info_move; //입주가능일 //date 방법으로 수정 생각해보기
	
	public Property() {
		super();
	}

	public Property(int info_no, String info_name) {
		super();
		this.info_no = info_no;
		this.info_name = info_name;
	}

	public int getInfo_no() {
		return info_no;
	}

	public void setInfo_no(int info_no) {
		this.info_no = info_no;
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

	public float getInfo_size() {
		return info_size;
	}

	public void setInfo_size(float info_size) {
		this.info_size = info_size;
	}

	public int getInfo_allfl() {
		return info_allfl;
	}

	public void setInfo_allfl(int info_allfl) {
		this.info_allfl = info_allfl;
	}

	public int getInfo_fl() {
		return info_fl;
	}

	public void setInfo_fl(int info_fl) {
		this.info_fl = info_fl;
	}

	public int getInfo_count() {
		return info_count;
	}

	public void setInfo_count(int info_count) {
		this.info_count = info_count;
	}

	public LocalDateTime getInfo_upload() {
		return info_upload;
	}

	public void setInfo_upload(LocalDateTime info_upload) {
		this.info_upload = info_upload;
	}

	public LocalDateTime getInfo_update() {
		return info_update;
	}

	public void setInfo_update(LocalDateTime info_update) {
		this.info_update = info_update;
	}

	public boolean isInfo_delflag() {
		return info_delflag;
	}

	public void setInfo_delflag(boolean info_delflag) {
		this.info_delflag = info_delflag;
	}

	public LocalDateTime getInfo_piry() {
		return info_piry;
	}

	public void setInfo_piry(LocalDateTime info_piry) {
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

	public int getInfo_sell() {
		return info_sell;
	}

	public void setInfo_sell(int info_sell) {
		this.info_sell = info_sell;
	}

	public int getInfo_year() {
		return info_year;
	}

	public void setInfo_year(int info_year) {
		this.info_year = info_year;
	}

	public int getInfo_month() {
		return info_month;
	}

	public void setInfo_month(int info_month) {
		this.info_month = info_month;
	}

	public int getInfo_deposit() {
		return info_deposit;
	}

	public void setInfo_deposit(int info_deposit) {
		this.info_deposit = info_deposit;
	}

	public int getInfo_hits() {
		return info_hits;
	}

	public void setInfo_hits(int info_hits) {
		this.info_hits = info_hits;
	}

	public int getInfo_comp() {
		return info_comp;
	}

	public void setInfo_comp(int info_comp) {
		this.info_comp = info_comp;
	}

	public LocalDateTime getInfo_ok() {
		return info_ok;
	}

	public void setInfo_ok(LocalDateTime info_ok) {
		this.info_ok = info_ok;
	}

	public int getInfo_move() {
		return info_move;
	}

	public void setInfo_move(int info_move) {
		this.info_move = info_move;
	}
	
}
