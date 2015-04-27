package com.nongguanjia.doctorTian.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author tx
 * 判断当前网络是否可用
 */
public class NetworkDetector {

	public static boolean detect(Context context){
		ConnectivityManager manageer = (ConnectivityManager)context.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(manageer == null){
			return false;
		}
		NetworkInfo netWorkInfo = manageer.getActiveNetworkInfo();
		if(netWorkInfo == null || !netWorkInfo.isAvailable()){
			return false;
		}
		return true;
	}
	
	
	//提示网络异常
	public static void dialog(Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("当前网络不可用，请查看网络设置");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}	
	
}
