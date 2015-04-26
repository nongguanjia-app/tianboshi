package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;
import java.util.List;

public class AllNewPassword implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NewPassword> NewPassword;
	
	public AllNewPassword(){
		
	}

	public AllNewPassword(
			List<com.nongguanjia.doctorTian.bean.NewPassword> newPassword) {
		super();
		NewPassword = newPassword;
	}

	public List<NewPassword> getNewPassword() {
		return NewPassword;
	}

	public void setNewPassword(List<NewPassword> newPassword) {
		NewPassword = newPassword;
	}

	@Override
	public String toString() {
		return "AllNewPassword [NewPassword=" + NewPassword + "]";
	}
}

