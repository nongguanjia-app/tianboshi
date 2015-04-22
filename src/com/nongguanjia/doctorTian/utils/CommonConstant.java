package com.nongguanjia.doctorTian.utils;

public class CommonConstant {
	public static final String login = "users"; //登录
	public static final String regist = "adduser"; //注册
	public static final String verifycodes = "verifycodes"; //获取验证码
	public static final String verifyuser = "verifyuser"; //？判断用户是否存在（注册时 -- 暂未添加）
	public static final String allcategorys = "allcategorys"; // 获取分类信息
	public static final String categorycourses = "categorycourses"; // 获取分类下的所有课程
	public static final String course = "course"; // 课程详情
	public static final String userinfo = "userinfo"; //用户详情
	public static final String authtxts = "authtxts"; //获取authTxt
	public static final String allexperiences = "allexperiences"; //获取经验谈
	public static final String experienceinfo = "experienceinfo"; //获取经验谈详情
	public static final String allechos = "allechos"; //获取经验谈的讨论区
	public static final String allreviews = "allreviews"; //获取讨论区（课程库）
	public static final String alltalks = "alltalks"; //获取讨论区消息（交流）
	public static final String allreplys = "allreplys"; //查看详情回复信息
	public static final String addtalkreply = "addtalkreply"; //增加讨论的回复
	public static final String addtalk = "addtalk"; //增加讨论
	public static final String allattentions = "allattentions"; //获取全部好友
	public static final String addattention = "addattention"; //添加好友
	public static final String delAttention = "deleteattention"; //取消推广人关注
	public static final String uploadAttention = "uploadattention"; //上传通讯录 
	
	public static final int RESPONSE_ERROR = 0;
	public static final int RESPONSE_SUCCESS = 1;
	
	//分类--图片
	public static String img_course_category = "http://182.92.170.172/DoctorTian/img/course_category/";
	//当前分类下的全部课程--图片
	public static String img_course_primary = "http://182.92.170.172/DoctorTian/img/course_primary/";
	//经验谈--图片
	public static String img_exp = "http://182.92.170.172/DoctorTian/img/user/";
	//讨论区--图片
	public static String img_discuss = "http://182.92.170.172/DoctorTian/img/roles/";
	
	//课程经验谈详情图片路径
	public static String img_exp_img = "http://182.92.170.172/DoctorTian";
}
