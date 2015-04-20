package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllReviews implements Parcelable{
	private String TalkId;
	private String Phone;
	private String Photo;
	private String Message;
	private String Name;
	private String Date;
	
	public String getTalkId() {
		return TalkId;
	}
	public void setTalkId(String talkId) {
		TalkId = talkId;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
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
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(TalkId);
		out.writeString(Phone);
		out.writeString(Photo);
		out.writeString(Message);
		out.writeString(Name);
		out.writeString(Date);
	}
	
	
	public static final Parcelable.Creator<AllReviews> CREATOR = new Creator<AllReviews>(){

		@Override
		public AllReviews createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllReviews reviews = new AllReviews();

			reviews.TalkId = in.readString();
			reviews.Phone = in.readString();
			reviews.Photo = in.readString();
			reviews.Message = in.readString();
			reviews.Name = in.readString();
			reviews.Date = in.readString();
			
			return reviews;
		}

		@Override
		public AllReviews[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllReviews[size];
		}
		
	};
	
}
