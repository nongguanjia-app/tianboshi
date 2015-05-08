package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Allcrops implements Parcelable{
	private String Id;
	private String Name;
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(Id);
		out.writeString(Name);
	}
	
	public static final Parcelable.Creator<Allcrops> CREATOR = new Creator<Allcrops>() {
		
		@Override
		public Allcrops[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new Allcrops[arg0];
		}
		
		@Override
		public Allcrops createFromParcel(Parcel in) {
			Allcrops allcrops = new Allcrops();
			allcrops.Id = in.readString();
			allcrops.Name = in.readString();
			return allcrops;
		}
	};
	

}
