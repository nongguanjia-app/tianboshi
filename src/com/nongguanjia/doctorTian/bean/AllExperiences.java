package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllExperiences implements Parcelable{
	private String ExperienceId;
	private String Photo;
	private String Message;
	private String Name;
	private String Date;
	private String Address;
	private String StartTime;
	private String Flage;
	
	public String getExperienceId() {
		return ExperienceId;
	}
	public void setExperienceId(String experienceId) {
		ExperienceId = experienceId;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getFlage() {
		return Flage;
	}
	public void setFlage(String flage) {
		Flage = flage;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(ExperienceId);
		out.writeString(Photo);
		out.writeString(Message);
		out.writeString(Name);
		out.writeString(Date);
		out.writeString(Address);
		out.writeString(StartTime);
		out.writeString(Flage);
	}
	
	
	public static final Parcelable.Creator<AllExperiences> CREATOR = new Creator<AllExperiences>(){

		@Override
		public AllExperiences createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllExperiences experiences = new AllExperiences();

			experiences.ExperienceId = in.readString();
			experiences.Photo = in.readString();
			experiences.Message = in.readString();
			experiences.Name = in.readString();
			experiences.Date = in.readString();
			experiences.Address = in.readString();
			experiences.StartTime = in.readString();
			experiences.Flage = in.readString();
			
			return experiences;
		}

		@Override
		public AllExperiences[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllExperiences[size];
		}
		
	};

}
