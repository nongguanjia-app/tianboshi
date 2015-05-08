package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Allunits implements Parcelable{
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
	
	public static final Parcelable.Creator<Allunits> CREATOR = new Creator<Allunits>() {
		
		@Override
		public Allunits[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new Allunits[arg0];
		}
		
		@Override
		public Allunits createFromParcel(Parcel in) {
			Allunits allunits = new Allunits();
			allunits.Id = in.readString();
			allunits.Name = in.readString();
			return allunits;
		}
	};
	

}
