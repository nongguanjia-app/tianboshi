package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Courses implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String returnCode;
	private String authTxt;
	private String Title;
	private String CourseIntro;
	private String CourseVideo;
	private String Flag;
	private String LargePicture;
	private String State;
	private String Progress;
	private String StartTime;
	private List AllCase;
	private ArrayList<AllLecture> AllLecture;

	public Courses(String returnCode, String authTxt, String title,
			String courseIntro, String courseVideo, String flag,
			String largePicture, String state, String progress,
			String startTime, List allCase, ArrayList<AllLecture> allLecture) {
		super();
		this.returnCode = returnCode;
		this.authTxt = authTxt;
		Title = title;
		CourseIntro = courseIntro;
		CourseVideo = courseVideo;
		Flag = flag;
		LargePicture = largePicture;
		State = state;
		Progress = progress;
		StartTime = startTime;
		AllCase = allCase;
		AllLecture = allLecture;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getAuthTxt() {
		return authTxt;
	}

	public void setAuthTxt(String authTxt) {
		this.authTxt = authTxt;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getCourseIntro() {
		return CourseIntro;
	}

	public void setCourseIntro(String courseIntro) {
		CourseIntro = courseIntro;
	}

	public String getCourseVideo() {
		return CourseVideo;
	}

	public void setCourseVideo(String courseVideo) {
		CourseVideo = courseVideo;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getLargePicture() {
		return LargePicture;
	}

	public void setLargePicture(String largePicture) {
		LargePicture = largePicture;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getProgress() {
		return Progress;
	}

	public void setProgress(String progress) {
		Progress = progress;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public List getAllCase() {
		return AllCase;
	}

	public void setAllCase(List allCase) {
		AllCase = allCase;
	}

	public ArrayList<AllLecture> getAllLecture() {
		return AllLecture;
	}

	public void setAllLecture(ArrayList<AllLecture> allLecture) {
		AllLecture = allLecture;
	}

	@Override
	public String toString() {
		return "Courses [returnCode=" + returnCode + ", authTxt=" + authTxt
				+ ", Title=" + Title + ", CourseIntro=" + CourseIntro
				+ ", CourseVideo=" + CourseVideo + ", Flag=" + Flag
				+ ", LargePicture=" + LargePicture + ", State=" + State
				+ ", Progress=" + Progress + ", StartTime=" + StartTime
				+ ", AllCase=" + AllCase + ", AllLecture=" + AllLecture + "]";
	}
}
