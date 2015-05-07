package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllChaptersNong implements Parcelable {
	private String LectureOrder;
	private String Name;
	private String Id;
	private String MediaId;
	private String MediaName;
	private String StartTime;
	private String isbo;
	
	public String getLectureOrder() {
		return LectureOrder;
	}
	public void setLectureOrder(String lectureOrder) {
		LectureOrder = lectureOrder;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getMediaName() {
		return MediaName;
	}
	public void setMediaName(String mediaName) {
		MediaName = mediaName;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getIsbo() {
		return isbo;
	}
	public void setIsbo(String isbo) {
		this.isbo = isbo;
	}
	public static Parcelable.Creator<AllChaptersNong> getCreator() {
		return CREATOR;
	}

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeString(LectureOrder);
		out.writeString(Name);
		out.writeString(Id);
		out.writeString(MediaId);
		out.writeString(MediaName);
		out.writeString(StartTime);
		out.writeString(isbo);
	}
	
	public static final Parcelable.Creator<AllChaptersNong> CREATOR = new Creator<AllChaptersNong>(){

		@Override
		public AllChaptersNong createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllChaptersNong allChaptersNong = new AllChaptersNong();

			allChaptersNong.LectureOrder = in.readString();
			allChaptersNong.Name = in.readString();
			allChaptersNong.Id = in.readString();
			allChaptersNong.MediaId = in.readString();
			allChaptersNong.MediaName = in.readString();
			allChaptersNong.StartTime = in.readString();
			allChaptersNong.isbo = in.readString();
			return allChaptersNong;
		}

		@Override
		public AllChaptersNong[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllChaptersNong[size];
		}
		
	};

	
}
