package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class Helps implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Id;
	private String Telephone;
	private String Content;
	private String Url;
	private String CreatedTime;
	private String returnCode;
	private String authTxt;
	
	public Helps(){
		
	}

	public Helps(String id, String telephone, String content, String url,
			String createdTime, String returnCode, String authTxt) {
		super();
		Id = id;
		Telephone = telephone;
		Content = content;
		Url = url;
		CreatedTime = createdTime;
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

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
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
		return "Helps [Id=" + Id + ", Telephone=" + Telephone + ", Content="
				+ Content + ", Url=" + Url + ", CreatedTime=" + CreatedTime
				+ ", returnCode=" + returnCode + ", authTxt=" + authTxt + "]";
	}
}
