package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class Lectures implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String authTxt;
	private String LectureName;
	private String LecturePT;
	private String LectureAddress;
	private String LectureField;
	private String LectureYear;
	private String LectureInfo;
	private String LecturePhoto;
	private String IsSubscrib;
	
	public Lectures(){
		
	}
	
	
	public Lectures(String returnCode, String authTxt, String lectureName,
			String lecturePT, String lectureAddress, String lectureField,
			String lectureYear, String lectureInfo, String lecturePhoto,
			String isSubscrib) {
		super();
		this.returnCode = returnCode;
		this.authTxt = authTxt;
		LectureName = lectureName;
		LecturePT = lecturePT;
		LectureAddress = lectureAddress;
		LectureField = lectureField;
		LectureYear = lectureYear;
		LectureInfo = lectureInfo;
		LecturePhoto = lecturePhoto;
		IsSubscrib = isSubscrib;
	}
	
	

	public String getReturnCode() {
		return returnCode;
	}


	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}


	public String getAuthTxt() {
		return authTxt;
	}


	public void setAuthTxt(String authTxt) {
		this.authTxt = authTxt;
	}


	public String getLectureName() {
		return LectureName;
	}


	public void setLectureName(String lectureName) {
		LectureName = lectureName;
	}


	public String getLecturePT() {
		return LecturePT;
	}


	public void setLecturePT(String lecturePT) {
		LecturePT = lecturePT;
	}


	public String getLectureAddress() {
		return LectureAddress;
	}


	public void setLectureAddress(String lectureAddress) {
		LectureAddress = lectureAddress;
	}


	public String getLectureField() {
		return LectureField;
	}


	public void setLectureField(String lectureField) {
		LectureField = lectureField;
	}


	public String getLectureYear() {
		return LectureYear;
	}


	public void setLectureYear(String lectureYear) {
		LectureYear = lectureYear;
	}


	public String getLectureInfo() {
		return LectureInfo;
	}


	public void setLectureInfo(String lectureInfo) {
		LectureInfo = lectureInfo;
	}


	public String getLecturePhoto() {
		return LecturePhoto;
	}


	public void setLecturePhoto(String lecturePhoto) {
		LecturePhoto = lecturePhoto;
	}


	public String getIsSubscrib() {
		return IsSubscrib;
	}


	public void setIsSubscrib(String isSubscrib) {
		IsSubscrib = isSubscrib;
	}


	@Override
	public String toString() {
		return "Lectures [returnCode=" + returnCode + ", authTxt=" + authTxt
				+ ", LectureName=" + LectureName + ", LecturePT=" + LecturePT
				+ ", LectureAddress=" + LectureAddress + ", LectureField="
				+ LectureField + ", LectureYear=" + LectureYear
				+ ", LectureInfo=" + LectureInfo + ", LecturePhoto="
				+ LecturePhoto + ", IsSubscrib=" + IsSubscrib + "]";
	}
}