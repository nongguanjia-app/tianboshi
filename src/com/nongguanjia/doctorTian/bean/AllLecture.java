package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class AllLecture implements Serializable {

	private String Name;
	private String Address;
	private String LectureId;
	private String Photo;
	
	
	public AllLecture() {}
	
	public AllLecture(String name, String address, String lectureId,
			String photo) {
		super();
		Name = name;
		Address = address;
		LectureId = lectureId;
		Photo = photo;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getLectureId() {
		return LectureId;
	}
	public void setLectureId(String lectureId) {
		LectureId = lectureId;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}

	@Override
	public String toString() {
		return "AllLecture [Name=" + Name + ", Address=" + Address
				+ ", LectureId=" + LectureId + ", Photo=" + Photo + "]";
	}
	
}
