package com.nongguanjia.doctorTian;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.fragment.FgChat;
import com.nongguanjia.doctorTian.fragment.FgCourseCenter;
import com.nongguanjia.doctorTian.fragment.FgMy;
import com.nongguanjia.doctorTian.fragment.FgMyCourse;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	// 定义四个Fragment
	private FgMyCourse fgMyCourse; //我的课程
	private FgCourseCenter fgCourseCenter; //课程表
	private FgChat fgChat; //客户服务
	private FgMy fgMy; //我的
	private Dialog noticeDialog;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private RadioGroup radioGroup;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	
	private void init(){
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
		((RadioButton)radioGroup.findViewById(R.id.radio_mycourse)).setChecked(true);
		
		if(((AppApplication)getApplication()).ROLE.equals("农户")){
			((RadioButton)radioGroup.findViewById(R.id.radio_chat)).setText("交流");
			Drawable drawable = this.getResources().getDrawable(R.drawable.selected_consumer_talk);
			((RadioButton)radioGroup.findViewById(R.id.radio_chat)).setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
		}else{
			((RadioButton)radioGroup.findViewById(R.id.radio_chat)).setText("客户服务");

			Drawable drawable = this.getResources().getDrawable(R.drawable.selected_consumer_service);
			((RadioButton)radioGroup.findViewById(R.id.radio_chat)).setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
		}
		
		transaction = fragmentManager.beginTransaction();
        Fragment fragment = new FgMyCourse();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	switch (checkedId) {
				case R.id.radio_mycourse: //我的课程
					transaction = fragmentManager.beginTransaction();
					fgMyCourse = new FgMyCourse();
	                transaction.replace(R.id.content, fgMyCourse);
	                transaction.commit();
					break;
				case R.id.radio_center://课程库
					transaction = fragmentManager.beginTransaction();
					fgCourseCenter = new FgCourseCenter();
	                transaction.replace(R.id.content, fgCourseCenter);
	                transaction.commit();
					break;
				case R.id.radio_chat://交流
					transaction = fragmentManager.beginTransaction();
					fgChat = new FgChat();
	                transaction.replace(R.id.content, fgChat);
	                transaction.commit();
					break;
				case R.id.radio_my://我的
					transaction = fragmentManager.beginTransaction();
					fgMy = new FgMy();
	                transaction.replace(R.id.content, fgMy);
	                transaction.commit();
					break;
            	}
            }
        });
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showNoticeDialog();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	

	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(this);
//		builder.setTitle("退出");
		builder.setMessage("是否退出田博士");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
		noticeDialog = builder.create();
		noticeDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		noticeDialog.show();
	}
	
	
}
