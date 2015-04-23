package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class Abouts implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	public Abouts(){
		
	}

	public Abouts(String id, String telephone, String name, String copyright,
			String address, String phone, String summary, String createdTime,
			String version, String returnCode, String authTxt) {
		super();
		Id = id;
		Telephone = telephone;
		Name = name;
		Copyright = copyright;
		Address = address;
		Phone = phone;
		Summary = summary;
		CreatedTime = createdTime;
		Version = version;
		this.returnCode = returnCode;
		this.authTxt = authTxt;
	}

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
	public String toString() {
		return "Abouts [Id=" + Id + ", Telephone=" + Telephone + ", Name="
				+ Name + ", Copyright=" + Copyright + ", Address=" + Address
				+ ", Phone=" + Phone + ", Summary=" + Summary
				+ ", CreatedTime=" + CreatedTime + ", Version=" + Version
				+ ", returnCode=" + returnCode + ", authTxt=" + authTxt + "]";
	}
}
