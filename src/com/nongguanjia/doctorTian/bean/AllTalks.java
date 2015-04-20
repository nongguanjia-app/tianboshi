package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllTalks implements Parcelable{
	private String courseId;
	private String courseTitle;
	private String courseImage;
	private String Num;
	private String Name;
	private String Content;
	private String experienceId;
	private String isExperience;
	private String TalkId;
	private String CreatedTime;
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getCourseImage() {
		return courseImage;
	}
	public void setCourseImage(String courseImage) {
		this.courseImage = courseImage;
	}
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getExperienceId() {
		return experienceId;
	}
	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}
	public String getIsExperience() {
		return isExperience;
	}
	public void setIsExperience(String isExperience) {
		this.isExperience = isExperience;
	}
	public String getTalkId() {
		return TalkId;
	}
	public void setTalkId(String talkId) {
		TalkId = talkId;
	}
	public String getCreatedTime() {
		return CreatedTime;
	}
	public void setCreatedTime(String createdTime) {
		CreatedTime = createdTime;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(courseId);
		out.writeString(courseTitle);
		out.writeString(courseImage);
		out.writeString(Num);
		out.writeString(Name);
		out.writeString(Content);
		out.writeString(experienceId);
		out.writeString(isExperience);
		out.writeString(TalkId);
		out.writeString(CreatedTime);
	}
	
	
	public static final Parcelable.Creator<AllTalks> CREATOR = new Creator<AllTalks>(){

		@Override
		public AllTalks createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllTalks talks = new AllTalks();

			talks.courseId = in.readString();
			talks.courseTitle = in.readString();
			talks.courseImage = in.readString();
			talks.Num = in.readString();
			talks.Name = in.readString();
			talks.Content = in.readString();
			talks.experienceId = in.readString();
			talks.isExperience = in.readString();
			talks.TalkId = in.readString();
			talks.CreatedTime = in.readString();
			
			return talks;
		}

		@Override
		public AllTalks[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllTalks[size];
		}
		
	};
	
	
}
