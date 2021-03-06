package com.nongguanjia.doctorTian.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DoctorTianRestClient {
//	public static final String BASE_URL = "http://182.92.170.172/tianboshi/"; 
	
//	private static final String BASE_URL = "http://123.56.158.177/tianboshi/";//测试服务器
	
	public static final String BASE_URL = "http://192.168.1.116:8080/demo/tbs/"; 
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl){
		return BASE_URL + relativeUrl;
	}
	
}
