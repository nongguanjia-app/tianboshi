package com.nongguanjia.doctorTian.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Courses implements Parcelable {
	private String returnCode;
	private String authTxt;
	private String Flag;
	private String CourseVideo;
	private String State;
	private String LargePicture;
	private String StartTime;
	private String Progress;
	private String CourseIntro;
	private String Title;
	private List<Lecture> AllLecture;
	private List<Case> AllCase;
	
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

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getCourseVideo() {
		return CourseVideo;
	}

	public void setCourseVideo(String courseVideo) {
		CourseVideo = courseVideo;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getLargePicture() {
		return LargePicture;
	}

	public void setLargePicture(String largePicture) {
		LargePicture = largePicture;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getProgress() {
		return Progress;
	}

	public void setProgress(String progress) {
		Progress = progress;
	}

	public String getCourseIntro() {
		return CourseIntro;
	}

	public void setCourseIntro(String courseIntro) {
		CourseIntro = courseIntro;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public List<Lecture> getAllLecture() {
		return AllLecture;
	}

	public void setAllLecture(List<Lecture> allLecture) {
		AllLecture = allLecture;
	}

	public List<Case> getAllCase() {
		return AllCase;
	}

	public void setAllCase(List<Case> allCase) {
		AllCase = allCase;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(returnCode);
		out.writeString(authTxt);
		out.writeString(Flag);
		out.writeString(CourseVideo);
		out.writeString(State);
		out.writeString(LargePicture);
		out.writeString(StartTime);
		out.writeString(Progress);
		out.writeString(CourseIntro);
		out.writeString(Title);
		out.writeTypedList(AllLecture);
		out.writeTypedList(AllCase);
	}
	
	
	public static final Parcelable.Creator<Courses> CREATOR = new Creator<Courses>(){

		@Override
		public Courses createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			Courses course = new Courses();
			course.returnCode = in.readString();
			course.authTxt = in.readString();
			course.Flag = in.readString();
			course.CourseVideo = in.readString();
			course.State = in.readString();
			course.LargePicture = in.readString();
			course.StartTime = in.readString();
			course.Progress = in.readString();
			course.CourseIntro = in.readString();
			course.Title = in.readString();
			
			course.AllLecture = new ArrayList<Lecture>();
			course.AllCase = new ArrayList<Case>();
			
			in.readTypedList(course.AllLecture, Lecture.CREATOR);
			in.readTypedList(course.AllCase, Case.CREATOR);
			
			return course;
		}

		@Override
		public Courses[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Courses[size];
		}
		
	};
	
}
