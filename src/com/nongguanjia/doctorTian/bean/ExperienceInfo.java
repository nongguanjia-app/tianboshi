package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ExperienceInfo implements Parcelable{
	private String returnCode;
	private String authTxt;
	private String status;
	private String title;
	private String summary;
	private String content;
	private String videoId;
	private String videoName;
	private String viewedNum;
	private String createdTime;
	private String courseId;
	private String courseTitle;
	private String telephone;
	private String loginName;
	private String name;
	private String avatar;
	private String gender;
	private String age;
	private String products;
	private String workPlace;
	private String businessForms;
	private String workYear;
	private String professional;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getViewedNum() {
		return viewedNum;
	}
	public void setViewedNum(String viewedNum) {
		this.viewedNum = viewedNum;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getBusinessForms() {
		return businessForms;
	}
	public void setBusinessForms(String businessForms) {
		this.businessForms = businessForms;
	}
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(returnCode);
		out.writeString(authTxt);
		out.writeString(status);
		out.writeString(title);
		out.writeString(summary);
		out.writeString(content);
		out.writeString(videoId);
		out.writeString(videoName);
		out.writeString(viewedNum);
		out.writeString(createdTime);
		out.writeString(courseId);
		out.writeString(courseTitle);
		out.writeString(telephone);
		out.writeString(loginName);
		out.writeString(name);
		out.writeString(avatar);
		out.writeString(gender);
		out.writeString(age);
		out.writeString(products);
		out.writeString(workPlace);
		out.writeString(businessForms);
		out.writeString(workYear);
		out.writeString(professional);
	}
	
	
	
	public static final Parcelable.Creator<ExperienceInfo> CREATOR = new Creator<ExperienceInfo>(){

		@Override
		public ExperienceInfo createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			ExperienceInfo info = new ExperienceInfo();

			info.returnCode = in.readString();
			info.authTxt = in.readString();
			info.status = in.readString();
			info.title = in.readString();
			info.summary = in.readString();
			info.content = in.readString();
			info.videoId = in.readString();
			info.videoName = in.readString();
			info.viewedNum = in.readString();
			info.createdTime = in.readString();
			info.courseId = in.readString();
			info.courseTitle = in.readString();
			info.telephone = in.readString();
			info.loginName = in.readString();
			info.name = in.readString();
			info.avatar = in.readString();
			info.gender = in.readString();
			info.age = in.readString();
			info.products = in.readString();
			info.workPlace = in.readString();
			info.businessForms = in.readString();
			info.workYear = in.readString();
			info.professional = in.readString();
			
			return info;
		}

		@Override
		public ExperienceInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ExperienceInfo[size];
		}
		
	};
	
}
