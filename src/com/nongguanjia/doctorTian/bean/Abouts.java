package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Abouts implements Parcelable{
	private String Id;
	private String Telephone;
	private String Name;
	private String Copyright;
	private String Address;
	private String Phone;
	private String Summary;
	private String CreatedTime;
	private String Version;
	private String returnCode;
	private String authTxt;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCopyright() {
		return Copyright;
	}
	public void setCopyright(String copyright) {
		Copyright = copyright;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getSummary() {
		return Summary;
	}
	public void setSummary(String summary) {
		Summary = summary;
	}
	public String getCreatedTime() {
		return CreatedTime;
	}
	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
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
		out.writeString(Telephone);
		out.writeString(Name);
		out.writeString(Copyright);
		out.writeString(Address);
		out.writeString(Phone);
		out.writeString(Summary);
		out.writeString(CreatedTime);
		out.writeString(Version);
		out.writeString(returnCode);
		out.writeString(authTxt);
	}
	
	public static final Parcelable.Creator<Abouts> CREATOR = new Creator<Abouts>(){

		@Override
		public Abouts createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			Abouts about = new Abouts();

			about.Id = in.readString();
			about.Telephone = in.readString();
			about.Name = in.readString();
			about.Copyright = in.readString();
			about.Address = in.readString();
			about.Phone = in.readString();
			about.Summary = in.readString();
			about.CreatedTime = in.readString();
			about.Version = in.readString();
			about.returnCode = in.readString();
			about.authTxt = in.readString();
			
			return about;
		}

		@Override
		public Abouts[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Abouts[size];
		}
		
	};
	
}
