package com.nongguanjia.doctorTian;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.listener.LoginListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.UserInfo;
import com.nongguanjia.doctorTian.db.CacheUserHelper;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.service.GotyeService;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.MD5Util;

/**
 * @author tx
 * 登录
 */
public class LoginActivity extends Activity implements OnClickListener, LoginListener{
	private Button btn_login, btn_regist;
	private TextView forget_psd;
	private EditText ed_phone, ed_psd;
	private ProgressDialog mDialog;
	
	private CacheUserHelper cacheUser;
	
	public static final String CONFIG = "login_config";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//判断当前登录状态
		int state = GotyeAPI.getInstance().getOnLineState();
		if (state != 0) {
			//已经登陆或离线可以直接跳到主界面
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			//启动service保存service长期活动
			Intent toService = new Intent(this, GotyeService.class);
			startService(toService);
			finish();
			return;
		}
		
		setContentView(R.layout.login);
		//添加LoginListener
		GotyeAPI.getInstance().addListener(this);
		
		init();
	}
	
	private void init(){
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		ed_phone = (EditText)findViewById(R.id.edit_phone);
		ed_psd = (EditText)findViewById(R.id.edit_psd);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_regist = (Button)findViewById(R.id.btn_regist);
		forget_psd = (TextView)findViewById(R.id.forget_psd);
		btn_login.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
		forget_psd.setOnClickListener(this);
		
		cacheUser = CacheUserHelper.getInstance(getApplicationContext());
		HashMap<String, String> user = cacheUser.selectTable();
		//数据库中有缓存
		if(!TextUtils.isEmpty(user.get("phone"))){ 
			ed_phone.setText(user.get("phone"));
			ed_psd.setText(user.get("psd"));
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_login:
			if(!TextUtils.isEmpty(ed_phone.getText())){
				if(!TextUtils.isEmpty(ed_psd.getText())){
					mDialog.show();
					login(ed_phone.getText().toString(), MD5Util.GetMD5Code(ed_psd.getText().toString()));
				}else{
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.btn_regist:
			intent = new Intent(getApplicationContext(), RegistActivity.class);
			startActivity(intent);
			break;
		case R.id.forget_psd:
			intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	
	private void login(final String phoneNum, final String psd){
		String url = CommonConstant.login + "/" + phoneNum + "," + psd;
		
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
//				mDialog.dismiss();
//				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				
				//解析应答数据
				try {
					if(response.getJSONObject("Users").getString("returnCode").equals("1")){
						//缓存用户名密码
						cacheUser.insertTable(phoneNum, ed_psd.getText().toString());
						cacheUser.closeDB();
						
						((AppApplication)getApplication()).PHONENUM = phoneNum;
						
						//登录Gotye
						loginGotye();
					}else{
						mDialog.dismiss();
						Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}

	
	private void getUserInfo(String phoneNum){
		String url = CommonConstant.userinfo + "/" + phoneNum;
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
				mDialog.dismiss();
				//解析应答数据
				try {
					if(response.getJSONObject("UserInfo").getString("returnCode").equals("1")){
						Gson gson = new Gson();
						UserInfo info = new UserInfo();
						info = gson.fromJson(response.getJSONObject("UserInfo").toString(), new TypeToken<UserInfo>(){}.getType());
						
						//区分权限
						if(info.getRoleID().equals("农户")){
							Toast.makeText(getApplicationContext(), "农户", Toast.LENGTH_SHORT).show();
							((AppApplication)getApplication()).ROLE = "农户";
						}else{
							((AppApplication)getApplication()).ROLE = "推广人";
						}
						
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
					}else{
						Toast.makeText(getApplicationContext(), "获取用户详细信息失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	private void loginGotye(){
		GotyeUser u = GotyeAPI.getInstance().getCurrentLoginUser();
		u = GotyeAPI.getInstance().getCurrentLoginUser();
		Log.d("", u.getName());
		
		// 登录的时候要传入登录监听，当重复登录时会直接返回登录状态
		Intent login = new Intent(this, GotyeService.class);
		login.setAction(GotyeService.ACTION_LOGIN);
		login.putExtra("name", ed_phone.getText().toString());
		login.putExtra("pwd", "");
		
		//由于服务端注册亲加有问题，暂时先在亲加后台添加tx账号，完成登录测试
//		login.putExtra("name", "tx");
//		login.putExtra("pwd", "");
		
		startService(login);
	}

	
	@Override
	public void onLogin(int code, GotyeUser user) {
		// TODO Auto-generated method stub
		//返回400 验证失败
		if (code == GotyeStatusCode.CODE_OK
				|| code == GotyeStatusCode.CODE_OFFLINELOGIN_SUCCESS
				|| code == GotyeStatusCode.CODE_RELOGIN_SUCCESS) {
			
			//登录亲加服务器成功后，获取用户详情
			getUserInfo(ed_phone.getText().toString());
			
			GotyeUser u = GotyeAPI.getInstance().getCurrentLoginUser();
			
			if (code == GotyeStatusCode.CODE_OFFLINELOGIN_SUCCESS) {
				Toast.makeText(this, "您当前处于离线状态", Toast.LENGTH_SHORT).show();
			} else if (code == GotyeStatusCode.CODE_OK) {
				Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			}
			
		} else {
			mDialog.dismiss();
			// 失败,可根据code定位失败原因
			Toast.makeText(this, "登录失败 code=" + code, Toast.LENGTH_SHORT)
					.show();
		}
	}

	
	@Override
	public void onLogout(int code) {
		// TODO Auto-generated method stub
		Log.d("gotye", "onLogout");
	}

	
	@Override
	public void onReconnecting(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub
		Log.d("gotye", "onReconnecting");
	}

	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//移除监听器
		GotyeAPI.getInstance().removeListener(this);
		super.onDestroy();
	}
	
	
	
//	private String getIMEI(){
//		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);  
//        String IMEI = tm.getDeviceId();  
//        return IMEI;
//	}
//	
//	
//	private void authtxt(String imei){
//		String url = CommonConstant.authtxts + "/" + imei;
//		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					String responseString, Throwable throwable) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
//				super.onFailure(statusCode, headers, responseString, throwable);
//			}
//
//			@Override
//			public void onSuccess(int statusCode, Header[] headers,
//					JSONObject response) {
//				//解析应答数据
//				try {
//					if(response.getJSONObject("AuthTxts").getString("returnCode").equals("1")){
//						Toast.makeText(getApplicationContext(), "获取auth成功", Toast.LENGTH_SHORT).show();
//						//登录Gotye
//						loginGotye();
//					}else{
//						Toast.makeText(getApplicationContext(), "获取auth失败", Toast.LENGTH_SHORT).show();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				super.onSuccess(statusCode, headers, response);
//			}
//			
//		});
//	}
	
	
	
}
