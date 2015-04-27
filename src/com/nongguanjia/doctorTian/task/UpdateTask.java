package com.nongguanjia.doctorTian.task;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.Version;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.UpdateManager;

/**
 * @author tx
 * 版本检测
 *
 */
public class UpdateTask {

	private UpdateManager updateManager;
	
	private Context context;
	
	private String clientVersion, serverVersion;
	
	private ProgressDialog mDialog;
	
	public UpdateTask(Context context){
		this.context = context;
		
		// 判断是否有更新版本 
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			clientVersion = pinfo.versionName;// 当前客户端的版本
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		mDialog = new ProgressDialog(context);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
	}

	
	
	public void getVersion(){
		String phoneNum = ((AppApplication)context.getApplicationContext()).PHONENUM;
		String url = CommonConstant.version + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if(response.getJSONObject("Versions").getString("returnCode").equals("1")){
						Gson gson = new Gson();
						Version version = new Version();
						version = gson.fromJson(response.getJSONObject("Versions").toString(), new TypeToken<Version>(){}.getType());
						
						serverVersion = version.getToVersion();
						
						updateManager = new UpdateManager(context, version);
						
						updateManager.checkUpdateInfo(Double.parseDouble(clientVersion), Double.parseDouble(serverVersion), false);
					}else{
						Toast.makeText(context, "版本检测失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
}
