package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllCategoryCourses implements Parcelable{
	private String Courseid;
	private String State;
	private String Title;
	private String Progress;
	private String Teacher;
	private String Picture;
	private String StartTime;
	
	public String getCourseid() {
		return Courseid;
	}
	public void setCourseid(String courseid) {
		Courseid = courseid;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getProgress() {
		return Progress;
	}
	public void setProgress(String progress) {
		Progress = progress;
	}
	public String getTeacher() {
		return Teacher;
	}
	public void setTeacher(String teacher) {
		Teacher = teacher;
	}
	public String getPicture() {
		return Picture;
	}
	public void setPicture(String picture) {
		Picture = picture;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Courseid);
		out.writeString(State);
		out.writeString(Title);
		out.writeString(Progress);
		out.writeString(Teacher);
		out.writeString(Picture);
		out.writeString(StartTime);
	}
	
	
	public static final Parcelable.Creator<AllCategoryCourses> CREATOR = new Creator<AllCategoryCourses>(){

		@Override
		public AllCategoryCourses createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllCategoryCourses info = new AllCategoryCourses();
			info.Courseid = in.readString();
			info.State = in.readString();
			info.Title = in.readString();
			info.Progress = in.readString();
			info.Teacher = in.readString();
			info.Picture = in.readString();
			info.StartTime = in.readString();
			
			return info;
		}

		@Override
		public AllCategoryCourses[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllCategoryCourses[size];
		}
		
	};
	
	
}
