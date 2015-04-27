package com.nongguanjia.doctorTian.service;

import com.nongguanjia.doctorTian.task.InitCityTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class InitService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		InitCityTask task = new InitCityTask(InitService.this);
		task.execute();
		
		super.onCreate();
	}
	
	

}
