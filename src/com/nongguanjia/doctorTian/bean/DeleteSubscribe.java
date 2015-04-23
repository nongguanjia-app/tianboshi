package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class DeleteSubscribe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String authTxt;

	public DeleteSubscribe() {

	}

	public DeleteSubscribe(String returnCode, String authTxt) {
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
		return "DeleteSubscribe [returnCode=" + returnCode + ", authTxt="
				+ authTxt + "]";
	}

}
