package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;
import java.util.List;

public class AllStatements implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String authTxt;
	private List<AllStatement> allStatement;

	public AllStatements() {

	}

	public AllStatements(String returnCode, String authTxt,
			List<AllStatement> allStatement) {
		super();
		this.returnCode = returnCode;
		this.authTxt = authTxt;
		this.allStatement = allStatement;
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

	public List<AllStatement> getAllStatement() {
		return allStatement;
	}

	public void setAllStatement(List<AllStatement> allStatement) {
		this.allStatement = allStatement;
	}

	@Override
	public String toString() {
		return "AllStatements [returnCode=" + returnCode + ", authTxt="
				+ authTxt + ", allStatement=" + allStatement + "]";
	}
}