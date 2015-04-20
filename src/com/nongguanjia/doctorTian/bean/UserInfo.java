package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable{
	private String Name;
	private String LoginName;
	private String Password;
	private String Telephone;
	private String Avatar;
	private String Gender;
	private String Age;
	private String CreatedTime;
	private String CreatedIP;
	private String IsEnabled;
	private String CropsId;
	private String CropsArea;
	private String CropsAreaUnit;
	private String roleID;
	private String returnCode;
	private String authTxt;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getLoginName() {
		return LoginName;
	}
	public void setLoginName(String loginName) {
		LoginName = loginName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getCreatedTime() {
		return CreatedTime;
	}
	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}
	public String getCreatedIP() {
		return CreatedIP;
	}
	public void setCreatedIP(String createdIP) {
		CreatedIP = createdIP;
	}
	public String getIsEnabled() {
		return IsEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		IsEnabled = isEnabled;
	}
	public String getCropsId() {
		return CropsId;
	}
	public void setCropsId(String cropsId) {
		CropsId = cropsId;
	}
	public String getCropsArea() {
		return CropsArea;
	}
	public void setCropsArea(String cropsArea) {
		CropsArea = cropsArea;
	}
	public String getCropsAreaUnit() {
		return CropsAreaUnit;
	}
	public void setCropsAreaUnit(String cropsAreaUnit) {
		CropsAreaUnit = cropsAreaUnit;
	}
	public String getRoleID() {
		return roleID;
	}
	public void setRoleID(String roleID) {
		this.roleID = roleID;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Name);
		out.writeString(LoginName);
		out.writeString(Password);
		out.writeString(Telephone);
		out.writeString(Avatar);
		out.writeString(Gender);
		out.writeString(Age);
		out.writeString(CreatedTime);
		out.writeString(CreatedIP);
		out.writeString(IsEnabled);
		out.writeString(CropsId);
		out.writeString(CropsArea);
		out.writeString(CropsAreaUnit);
		out.writeString(roleID);
		out.writeString(returnCode);
		out.writeString(authTxt);
	}
	
	
	public static final Parcelable.Creator<UserInfo> CREATOR = new Creator<UserInfo>(){

		@Override
		public UserInfo createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			UserInfo info = new UserInfo();

			info.Name = in.readString();
			info.LoginName = in.readString();
			info.Password = in.readString();
			info.Telephone = in.readString();
			info.Avatar = in.readString();
			info.Gender = in.readString();
			info.Age = in.readString();
			info.CreatedTime = in.readString();
			info.CreatedIP = in.readString();
			info.IsEnabled = in.readString();
			info.CropsId = in.readString();
			info.CropsArea = in.readString();
			info.CropsAreaUnit = in.readString();
			info.roleID = in.readString();
			info.returnCode = in.readString();
			info.authTxt = in.readString();
			
			return info;
		}

		@Override
		public UserInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new UserInfo[size];
		}
		
	};
	
}
