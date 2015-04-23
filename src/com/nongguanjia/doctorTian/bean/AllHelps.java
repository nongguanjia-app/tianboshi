package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;
import java.util.List;

public class AllHelps implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Helps> Helps;
	
	public AllHelps(){
		
	}

	public AllHelps(List<Helps> helps) {
		super();
		Helps = helps;
	}

	public List<Helps> getHelps() {
		return Helps;
	}

	public void setHelps(List<Helps> helps) {
		Helps = helps;
	}

	@Override
	public String toString() {
		return "AllHelps [Helps=" + Helps + "]";
	}
}

