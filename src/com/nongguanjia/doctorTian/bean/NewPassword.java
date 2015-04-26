package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class NewPassword implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String authTxt;
	
	public NewPassword(){
		
	}

	public NewPassword(String returnCode, String authTxt) {
		super();
		this.returnCode = returnCode;
		this.authTxt = authTxt;
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
		return "NewPassword [returnCode=" + returnCode + ", authTxt=" + authTxt
				+ "]";
	}
}
