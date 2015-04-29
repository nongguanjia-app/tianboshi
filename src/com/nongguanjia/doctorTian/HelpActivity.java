package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.AllHelps;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.view.ProgressWebView;

public class HelpActivity extends Activity {
	private TextView tv_title;
	private ImageView img_back;
	private AllHelps mAllHelps;
	private Activity activity;
	private ProgressWebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		init();
	}
	
	private void init(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		webView = (ProgressWebView) findViewById(R.id.webView);
		
		//设置WebView属性，能够执行Javascript脚本  
		webView.getSettings().setJavaScriptEnabled(true); 
		//设置支持缩放
		webView.setVerticalScrollbarOverlay(true); //指定的垂直滚动条有叠加样式
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);//设定支持viewport
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);//设定支持缩放
		
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(HelpActivity.this, description, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				tv_title.setText("帮助");
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				tv_title.setText("加载中...");
				super.onPageStarted(view, url, favicon);
			}
			
		});
		
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HelpActivity.this.finish();
			}
		});
		
		getHelp();
	}
	

	public void getHelp() {
		String url = CommonConstant.helps;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(response.getJSONArray("Helps").getJSONObject(0).getString("returnCode").equals("1")){
						Gson gson = new Gson();
						mAllHelps = gson.fromJson(response.toString(),AllHelps.class);
						webView.loadUrl(mAllHelps.getHelps().get(0).getUrl());
					}else {
						Toast.makeText(activity, "获取信息失败", Toast.LENGTH_SHORT).show();
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
