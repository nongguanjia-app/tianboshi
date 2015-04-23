package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;
import java.util.List;

public class AllAbouts implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Abouts> Abouts;

	public AllAbouts() {

	}

	public AllAbouts(List<Abouts> abouts) {
		super();
		Abouts = abouts;
	}

	public List<Abouts> getAbouts() {
		return Abouts;
	}

	public void setAbouts(List<Abouts> abouts) {
		Abouts = abouts;
	}

	@Override
	public String toString() {
		return "AllAbouts [Abouts=" + Abouts + "]";
	}

	
}
