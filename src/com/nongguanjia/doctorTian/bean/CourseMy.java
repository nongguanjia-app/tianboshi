package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CourseMy implements Parcelable{
	private String Id;
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

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

	public String getCourseAvatar() {
		return courseAvatar;
	}

	public void setCourseAvatar(String courseAvatar) {
		this.courseAvatar = courseAvatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	private String courseId;
	private String courseTitle;
	private String courseAvatar;//图片
	private String status;
	private String title;
	private String createdTime;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(Id);
		out.writeString(courseId);
		out.writeString(courseTitle);
		out.writeString(courseAvatar);
		out.writeString(status);
		out.writeString(title);
		out.writeString(createdTime);
	}
	
	public static final Parcelable.Creator<CourseMy> CREATOR = new Creator<CourseMy>() {
		
		@Override
		public CourseMy[] newArray(int arg0) {
			return new CourseMy[arg0];
		}
		
		@Override
		public CourseMy createFromParcel(Parcel arg0) {
			CourseMy courseMy = new CourseMy();
			courseMy.Id = arg0.readString();
			courseMy.courseId = arg0.readString();
			courseMy.courseTitle = arg0.readString();
			courseMy.courseAvatar = arg0.readString();
			courseMy.status = arg0.readString();
			courseMy.title = arg0.readString();
			courseMy.createdTime = arg0.readString();
			return courseMy;
		}
	};

}
