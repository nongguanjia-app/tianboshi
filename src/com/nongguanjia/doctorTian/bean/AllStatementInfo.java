package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class AllStatementInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AllStatements AllStatements;
	
	public AllStatementInfo(){
		
	}

	public AllStatementInfo(AllStatements allStatements) {
		super();
		AllStatements = allStatements;
	}

	public AllStatements getAllStatements() {
		return AllStatements;
	}

	public void setAllStatements(AllStatements allStatements) {
		AllStatements = allStatements;
	}

	@Override
	public String toString() {
		return "AllStatementInfo [AllStatements=" + AllStatements + "]";
	}
	
}
