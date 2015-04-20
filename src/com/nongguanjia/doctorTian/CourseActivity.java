package com.nongguanjia.doctorTian;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.fragment.FgCourse;
import com.nongguanjia.doctorTian.fragment.FgCourseExp;
import com.nongguanjia.doctorTian.fragment.FgDetail;
import com.nongguanjia.doctorTian.fragment.FgDiscussArea;
import com.nongguanjia.doctorTian.fragment.FgMyCourse;

/**
 * @author tx
 * 课程详情
 */
public class CourseActivity extends FragmentActivity {
	private FgDetail fgDetail;
	private FgCourse fgCourse;
	private FgCourseExp fgCourseExp;
	private FgDiscussArea fgDiscussArea;
	
	private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    
    private String courseId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
		
		init();
	}
	
	private void init(){
		Bundle bd = getIntent().getExtras();
		courseId = bd.getString("courseId");
		
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		((RadioButton)radioGroup.findViewById(R.id.cou_detail)).setChecked(true);
		
		transaction = fragmentManager.beginTransaction();
        Fragment fragment = new FgDetail();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	switch (checkedId) {
				case R.id.cou_detail:
					transaction = fragmentManager.beginTransaction();
					fgDetail = new FgDetail();
	                transaction.replace(R.id.content, fgDetail);
	                transaction.commit();
					break;
				case R.id.cou_table:
					transaction = fragmentManager.beginTransaction();
					fgCourse = new FgCourse();
	                transaction.replace(R.id.content, fgCourse);
	                transaction.commit();
					break;
				case R.id.cou_exp:	//经验谈
					transaction = fragmentManager.beginTransaction();
					fgCourseExp = new FgCourseExp();
					fgCourseExp.setCourseId(courseId);
	                transaction.replace(R.id.content, fgCourseExp);
	                transaction.commit();
					break;
				case R.id.cou_discus: // 讨论区
					transaction = fragmentManager.beginTransaction();
					fgDiscussArea = new FgDiscussArea();
					fgDiscussArea.setCourseId(courseId);
	                transaction.replace(R.id.content, fgDiscussArea);
	                transaction.commit();
					break;
            	}
                
            }
        });
	
		
	}

	
	

}
