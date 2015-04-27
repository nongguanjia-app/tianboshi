package com.nongguanjia.doctorTian.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.Version;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {

	private Context mContext;
	private Version version;
	
	private Dialog noticeDialog;
	
	private Dialog downloadDialog;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    
    private static final int DOWN_UPDATE = 1;
    
    private static final int DOWN_OVER = 2;
    
    private int progress;
    
    private Thread downLoadThread;
    
    private boolean interceptFlag = false;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				
				installApk();
				break;
			default:
				break;
			}
    	};
    };
    
	public UpdateManager(Context context, Version version) {
		this.mContext = context;
		this.version = version;
	}
	
	//外部接口让主Activity调用
	public void checkUpdateInfo(double current, double server, boolean isShow){
		if(current != server){
			double min = Math.min(current, server);
			if(min == current){
				showNoticeDialog();
			}
		}else{
			if(isShow){
				Toast.makeText(mContext, "当前为最新版本", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	
	private void showNoticeDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(version.getIntroduction());
		builder.setPositiveButton("下载", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();			
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});
		noticeDialog = builder.create();
		noticeDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		noticeDialog.show();
	}
	
	private void showDownloadDialog(){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		downloadDialog.show();
		
		downloadApk();
	}
	
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			try {
				URL url = new URL(version.getUrl());
			
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				File file = new File(CommonConstant.savePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = CommonConstant.savePath + version.getSourcePath();
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do{   		   		
		    		int numread = is.read(buf);
		    		count += numread;
		    	    progress =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.sendEmptyMessage(DOWN_UPDATE);
		    		if(numread <= 0){	
		    			//下载完成通知安装
		    			mHandler.sendEmptyMessage(DOWN_OVER);
		    			downloadDialog.dismiss();
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(!interceptFlag);//点击取消就停止下载.
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
	
	 /**
     * 下载apk
     * @param url
     */
	
	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	 /**
     * 安装apk
     * @param url
     */
	private void installApk(){
		File apkfile = new File(CommonConstant.savePath + version.getSourcePath());
        if (!apkfile.exists()) {
            return;
        }    
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        mContext.startActivity(i);
	
	}
}
