package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllCategorys implements Parcelable{
	private String Id;
	private String Name;
	private String Icon;
	private String ParentName;
	
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
	public String getIcon() {
		return Icon;
	}
	public void setIcon(String icon) {
		Icon = icon;
	}
	public String getParentName() {
		return ParentName;
	}
	public void setParentName(String parentName) {
		ParentName = parentName;
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Id);
		out.writeString(Name);
		out.writeString(Icon);
		out.writeString(ParentName);
	}
	
	
	public static final Parcelable.Creator<AllCategorys> CREATOR = new Creator<AllCategorys>(){

		@Override
		public AllCategorys createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllCategorys info = new AllCategorys();

			info.Id = in.readString();
			info.Name = in.readString();
			info.Icon = in.readString();
			info.ParentName = in.readString();
			
			return info;
		}

		@Override
		public AllCategorys[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllCategorys[size];
		}
		
	};
	
}
