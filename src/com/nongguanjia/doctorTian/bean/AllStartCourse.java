package com.nongguanjia.doctorTian.bean;

import java.io.Serializable;

public class AllStartCourse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String courseId;
	private String courseTitle;
	private String progress;
	private String smallPicture;
	private String expertId;
	private String isExperience;
	private String experienceId;
	private String experienceTitle;
	private String StartTime;
	
	public AllStartCourse(){
		
	}

	public AllStartCourse(String courseId, String courseTitle, String progress,
			String smallPicture, String expertId, String isExperience,
			String experienceId, String experienceTitle, String startTime) {
		super();
		this.courseId = courseId;
		this.courseTitle = courseTitle;
		this.progress = progress;
		this.smallPicture = smallPicture;
		this.expertId = expertId;
		this.isExperience = isExperience;
		this.experienceId = experienceId;
		this.experienceTitle = experienceTitle;
		StartTime = startTime;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public String getIsExperience() {
		return isExperience;
	}

	public void setIsExperience(String isExperience) {
		this.isExperience = isExperience;
	}

	public String getExperienceId() {
		return experienceId;
	}

	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}

	public String getExperienceTitle() {
		return experienceTitle;
	}

	public void setExperienceTitle(String experienceTitle) {
		this.experienceTitle = experienceTitle;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	@Override
	public String toString() {
		return "AllStartCourse [courseId=" + courseId + ", courseTitle="
				+ courseTitle + ", progress=" + progress + ", smallPicture="
				+ smallPicture + ", expertId=" + expertId + ", isExperience="
				+ isExperience + ", experienceId=" + experienceId
				+ ", experienceTitle=" + experienceTitle + ", StartTime="
				+ StartTime + "]";
	}
}
