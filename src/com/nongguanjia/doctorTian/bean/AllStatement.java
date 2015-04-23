package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class AllStatement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Content;
	
	public AllStatement(){
		
	}

	public AllStatement(String content) {
		super();
		Content = content;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toString() {
		return "AllStatement [Content=" + Content + "]";
	}
}
