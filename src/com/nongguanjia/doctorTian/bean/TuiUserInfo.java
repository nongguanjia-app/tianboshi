package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TuiUserInfo implements Parcelable{
	private String returnCode;
	private String authTxt;
	private String loginName;
	private String avatar;
	private String gender;
	private String age;
	private String isEnabled;
	private String lastLoginCity;
	private String createdCity;
	private String province;
	private String city;
	private String area;
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
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getLastLoginCity() {
		return lastLoginCity;
	}

	public void setLastLoginCity(String lastLoginCity) {
		this.lastLoginCity = lastLoginCity;
	}

	public String getCreatedCity() {
		return createdCity;
	}

	public void setCreatedCity(String createdCity) {
		this.createdCity = createdCity;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public static Creator<TuiUserInfo> getCreator() {
		return CREATOR;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(returnCode);
		out.writeString(authTxt);
		out.writeString(loginName);
		out.writeString(avatar);
		out.writeString(gender);
		out.writeString(age);
		out.writeString(isEnabled);
		out.writeString(lastLoginCity);
		out.writeString(createdCity);
		out.writeString(province);
		out.writeString(city);
		out.writeString(area);
		out.writeString(products);
		out.writeString(workPlace);
		out.writeString(businessForms);
		out.writeString(workYear);
		out.writeString(professional);
	}
	
	public static final Creator<TuiUserInfo> CREATOR = new Creator<TuiUserInfo>(){
		@Override
		public TuiUserInfo createFromParcel(Parcel in) {
			TuiUserInfo info = new TuiUserInfo();
			info.returnCode = in.readString();
			info.authTxt = in.readString();
			info.loginName = in.readString();
			info.avatar = in.readString();
			info.gender = in.readString();
			info.age = in.readString();
			info.isEnabled = in.readString();
			info.lastLoginCity = in.readString();
			info.createdCity = in.readString();
			info.province = in.readString();
			info.city = in.readString();
			info.area = in.readString();
			info.products = in.readString();
			info.workPlace = in.readString();
			info.businessForms = in.readString();
			info.workYear = in.readString();
			info.professional = in.readString();
			return info;
		}

		@Override
		public TuiUserInfo[] newArray(int size) {
			return new TuiUserInfo[size];
		}
	};

}
