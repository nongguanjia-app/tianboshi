package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class AllChapters implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String LectureOrder;
	private String Name;
	private String Id;
	private String MediaId;
	private String MediaName;
	private String StartTime;
	
	public AllChapters(){
		
	}

	public AllChapters(String lectureOrder, String name, String id,
			String mediaId, String mediaName, String startTime) {
		super();
		LectureOrder = lectureOrder;
		Name = name;
		Id = id;
		MediaId = mediaId;
		MediaName = mediaName;
		StartTime = startTime;
	}

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

	@Override
	public String toString() {
		return "AllChapters [LectureOrder=" + LectureOrder + ", Name=" + Name
				+ ", Id=" + Id + ", MediaId=" + MediaId + ", MediaName="
				+ MediaName + ", StartTime=" + StartTime + "]";
	}
}
