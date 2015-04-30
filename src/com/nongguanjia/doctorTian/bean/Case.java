package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Case implements Parcelable{
	private String Title;
	private String Id;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Title);
		out.writeString(Id);
	}
	
	public static final Parcelable.Creator<Case> CREATOR = new Creator<Case>(){

		@Override
		public Case createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			Case allCase = new Case();

			allCase.Title = in.readString();
			allCase.Id = in.readString();
			
			return allCase;
		}

		@Override
		public Case[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Case[size];
		}
		
	};
	
}
