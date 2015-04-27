package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Version implements Parcelable{
	private String Id;
	private String Name;
	private String fromVersion;
	private String toVersion;
	private String Type;
	private String sourcePath;
	private String Url;
	private String Telephone;
	private String Size;
	private String Introduction;
	private String Improved;
	private String createdTime;
	private String isEnabled;
	private String returnCode;
	private String authTxt;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getFromVersion() {
		return fromVersion;
	}
	public void setFromVersion(String fromVersion) {
		this.fromVersion = fromVersion;
	}
	public String getToVersion() {
		return toVersion;
	}
	public void setToVersion(String toVersion) {
		this.toVersion = toVersion;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getIntroduction() {
		return Introduction;
	}
	public void setIntroduction(String introduction) {
		Introduction = introduction;
	}
	public String getImproved() {
		return Improved;
	}
	public void setImproved(String improved) {
		Improved = improved;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
		out.writeString(Id);
		out.writeString(Name);
		out.writeString(fromVersion);
		out.writeString(toVersion);
		out.writeString(Type);
		out.writeString(sourcePath);
		out.writeString(Url);
		out.writeString(Telephone);
		out.writeString(Size);
		out.writeString(Introduction);
		out.writeString(Improved);
		out.writeString(createdTime);
		out.writeString(isEnabled);
		out.writeString(returnCode);
		out.writeString(authTxt);
	}
	
	
	public static final Parcelable.Creator<Version> CREATOR = new Creator<Version>(){

		@Override
		public Version createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			Version version = new Version();

			version.Id = in.readString();
			version.Name = in.readString();
			version.fromVersion = in.readString();
			version.toVersion = in.readString();
			version.Type = in.readString();
			version.sourcePath = in.readString();
			version.Url = in.readString();
			version.Telephone = in.readString();
			version.Size = in.readString();
			version.Introduction = in.readString();
			version.Improved = in.readString();
			version.createdTime = in.readString();
			version.isEnabled = in.readString();
			version.returnCode = in.readString();
			version.authTxt = in.readString();
			
			return version;
		}

		@Override
		public Version[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Version[size];
		}
		
	};
	
}
