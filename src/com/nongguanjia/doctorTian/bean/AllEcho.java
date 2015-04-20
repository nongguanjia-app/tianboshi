package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllEcho implements Parcelable{
	private String TalkId;
	private String Photo;
	private String Name;
	private String Phone;
	private String Massage;
	private String Date;
	
	public String getTalkId() {
		return TalkId;
	}
	public void setTalkId(String talkId) {
		TalkId = talkId;
	}
	public String getPhoto() {
		return Photo;
	}
	public void setPhoto(String photo) {
		Photo = photo;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getMassage() {
		return Massage;
	}
	public void setMassage(String massage) {
		Massage = massage;
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
		out.writeString(Photo);
		out.writeString(Name);
		out.writeString(Phone);
		out.writeString(Massage);
		out.writeString(Date);
	}
	
	public static final Parcelable.Creator<AllEcho> CREATOR = new Creator<AllEcho>(){

		@Override
		public AllEcho createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllEcho echo = new AllEcho();

			echo.TalkId = in.readString();
			echo.Photo = in.readString();
			echo.Name = in.readString();
			echo.Phone = in.readString();
			echo.Massage = in.readString();
			echo.Date = in.readString();
			
			return echo;
		}

		@Override
		public AllEcho[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllEcho[size];
		}
		
	};
}
