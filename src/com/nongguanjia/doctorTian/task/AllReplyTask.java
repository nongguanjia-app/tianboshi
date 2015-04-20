package com.nongguanjia.doctorTian.task;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

import android.os.Handler;
import android.os.Message;

public class AllReplyTask {

	private String url;
	private Handler handler;
	
	public AllReplyTask(String url, Handler handler){
		this.url = url;
		this.handler = handler;
	}
	
	public void getAllReplys(){
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
				
				Message message = new Message();
				
				try {
					if(response.getJSONObject("AllReplys").getString("returnCode").equals("1")){
						message.what = CommonConstant.RESPONSE_SUCCESS;
						message.obj = response.getJSONObject("AllReplys");
					}else{
						message.what = CommonConstant.RESPONSE_ERROR;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				handler.sendMessage(message);
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
}
