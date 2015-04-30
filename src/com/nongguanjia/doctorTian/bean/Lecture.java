package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Lecture implements Parcelable {
	private String Name;
	private String Photo;
	private String LectureId;
	private String Address;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public String getLectureId() {
		return LectureId;
	}
	public void setLectureId(String lectureId) {
		LectureId = lectureId;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Name);
		out.writeString(Photo);
		out.writeString(LectureId);
		out.writeString(Address);
	}
	
	
	public static final Parcelable.Creator<Lecture> CREATOR = new Creator<Lecture>(){

		@Override
		public Lecture createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			Lecture lecture = new Lecture();

			lecture.Name = in.readString();
			lecture.Photo = in.readString();
			lecture.LectureId = in.readString();
			lecture.Address = in.readString();
			
			return lecture;
		}

		@Override
		public Lecture[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Lecture[size];
		}
		
	};
	
	
}
