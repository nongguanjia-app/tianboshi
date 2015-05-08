package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Allproduct implements Parcelable{
	private String id;
	private String name;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(id);
		out.writeString(name);
	}
	
public static final Parcelable.Creator<Allproduct> CREATOR = new Creator<Allproduct>() {
		
		@Override
		public Allproduct[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new Allproduct[arg0];
		}
		
		@Override
		public Allproduct createFromParcel(Parcel in) {
			Allproduct allproduct = new Allproduct();
			allproduct.id = in.readString();
			allproduct.name = in.readString();
			return allproduct;
		}
	};
}
