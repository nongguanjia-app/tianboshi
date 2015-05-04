package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CaseInfo implements Parcelable{
	private String CaseId;
	private String CourseId;
	private String CaseTitle;
	private String CaseContent;
	private String CasePicture;
	private String returnCode;
	private String authTxt;
	
	public String getCaseId() {
		return CaseId;
	}
	public void setCaseId(String caseId) {
		CaseId = caseId;
	}
	public String getCourseId() {
		return CourseId;
	}
	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
	public String getCaseTitle() {
		return CaseTitle;
	}
	public void setCaseTitle(String caseTitle) {
		CaseTitle = caseTitle;
	}
	public String getCaseContent() {
		return CaseContent;
	}
	public void setCaseContent(String caseContent) {
		CaseContent = caseContent;
	}
	public String getCasePicture() {
		return CasePicture;
	}
	public void setCasePicture(String casePicture) {
		CasePicture = casePicture;
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
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(CaseId);
		out.writeString(CourseId);
		out.writeString(CaseTitle);
		out.writeString(CaseContent);
		out.writeString(CasePicture);
		out.writeString(returnCode);
		out.writeString(authTxt);
	}
	
	public static final Parcelable.Creator<CaseInfo> CREATOR = new Creator<CaseInfo>(){

		@Override
		public CaseInfo createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			CaseInfo info = new CaseInfo();

			info.CaseId = in.readString();
			info.CourseId = in.readString();
			info.CaseTitle = in.readString();
			info.CaseContent = in.readString();
			info.CasePicture = in.readString();
			info.returnCode = in.readString();
			info.authTxt = in.readString();
			
			return info;
		}

		@Override
		public CaseInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CaseInfo[size];
		}
		
	};
	
}
