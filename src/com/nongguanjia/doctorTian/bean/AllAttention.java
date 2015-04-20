package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllAttention implements Parcelable{
	private String Telephone;
	private String NoteName;
	private String Source;
	private String Avatar;
	private String Flage;
	
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getNoteName() {
		return NoteName;
	}
	public void setNoteName(String noteName) {
		NoteName = noteName;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
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
		out.writeString(Telephone);
		out.writeString(NoteName);
		out.writeString(Source);
		out.writeString(Avatar);
		out.writeString(Flage);
	}
	
	public static final Parcelable.Creator<AllAttention> CREATOR = new Creator<AllAttention>(){

		@Override
		public AllAttention createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllAttention attention = new AllAttention();

			attention.Telephone = in.readString();
			attention.NoteName = in.readString();
			attention.Source = in.readString();
			attention.Avatar = in.readString();
			attention.Flage = in.readString();
			
			return attention;
		}

		@Override
		public AllAttention[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllAttention[size];
		}
		
	};
	
}
