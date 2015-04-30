package com.nongguanjia.doctorTian;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.listener.LoginListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.db.CacheUserHelper;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.service.GotyeService;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.MD5Util;

/**
 * @author tx
 * 注册
 */
public class RegistActivity extends Activity implements OnClickListener, LoginListener{
	private Button btn_regist;
	private ProgressDialog mDialog;
	private Button btn_get_code;
	private Button btn_timer;
	private EditText ed_phone, ed_psd, ed_psd_again, ed_verify_code;
	private String verifyCode;
	private CacheUserHelper cacheUser;
	
	private Timer timer;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			//handler处理消息
			if(msg.what > 0){
				btn_timer.setText(msg.what + "s");
			}else{
				//隐藏倒计时，显示验证按钮
				btn_timer.setVisibility(View.GONE);
				btn_get_code.setVisibility(View.VISIBLE);
				//结束timer
				timer.cancel();
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);
		
		init();
	}
	
	private void init(){
		//添加LoginListener
		GotyeAPI.getInstance().addListener(this);
		
		cacheUser = CacheUserHelper.getInstance(getApplicationContext());
				
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		btn_regist = (Button)findViewById(R.id.btn_regist);
		btn_get_code = (Button)findViewById(R.id.get_code);
		btn_timer = (Button)findViewById(R.id.btn_timer);
		ed_phone = (EditText)findViewById(R.id.edit_phone); 
		ed_psd = (EditText)findViewById(R.id.edit_psd);
		ed_psd_again = (EditText)findViewById(R.id.edit_psd_again);
		ed_verify_code = (EditText)findViewById(R.id.edit_verify_code);
		
		btn_regist.setOnClickListener(this);
		btn_get_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_regist:
			//添加验证
			if(!TextUtils.isEmpty(ed_phone.getText())){
				if(!TextUtils.isEmpty(ed_psd.getText())){
					if(!TextUtils.isEmpty(ed_psd_again.getText())){
						if(!TextUtils.isEmpty(ed_verify_code.getText())){
							if(ed_verify_code.getText().toString().equals(verifyCode)){
								mDialog.show();
								String psd = MD5Util.GetMD5Code(ed_psd.getText().toString());
								regist(ed_phone.getText().toString(), psd);
							}else{
								Toast.makeText(getApplicationContext(), "验证码失效，请重新获取", Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.get_code:
			if(!TextUtils.isEmpty(ed_phone.getText())){
				mDialog.show();
				
				showTime();
				
				//显示倒计时
				btn_get_code.setVisibility(View.GONE);
				btn_timer.setVisibility(View.VISIBLE);
				
				getCode(ed_phone.getText().toString());
			}else{
				Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
			}
			
			break;

		default:
			break;
		}
	}
	
	
	private void getCode(String phoneNum){
		String url = CommonConstant.verifycodes + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				mDialog.dismiss();
				//解析应答数据
				try {
					if(response.getJSONObject("VerifyCodes").getString("returnCode").equals("1")){
						verifyCode = response.getJSONObject("VerifyCodes").getString("verifyCode");
						Toast.makeText(RegistActivity.this, "手机验证码已发送，请查看手机", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(RegistActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
						//结束timer
						timer.cancel();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	
	private void showTime(){
		timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			int i = 120;
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//定义一个消息传过去
				Message msg = new Message();
				msg.what = i--;
				handler.sendMessage(msg);
			}
			
		};
		//2s后开启倒计时，倒计时间隔为1s
		timer.scheduleAtFixedRate(timerTask, 1000, 1000);
	}
	
	
	private void regist(String name, String psd){
		String url = CommonConstant.regist + "/" + name + "," + psd;
		
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				//解析应答数据
				try {
					if(response.getJSONObject("AddUser").getString("returnCode").equals("1")){
						//注册成功后，初始化权限（农户）
						((AppApplication)getApplication()).ROLE = "农户";
						
						//缓存用户名密码
						cacheUser.insertTable(ed_phone.getText().toString(), ed_psd.getText().toString());
						cacheUser.closeDB();
						
						//登录Gotye
						loginGotye();
						
					}else{
						Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
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
		
		startService(login);
	}

	
	@Override
	public void onLogin(int code, GotyeUser user) {
		// TODO Auto-generated method stub
		//返回400 验证失败
		if (code == GotyeStatusCode.CODE_OK
				|| code == GotyeStatusCode.CODE_OFFLINELOGIN_SUCCESS
				|| code == GotyeStatusCode.CODE_RELOGIN_SUCCESS) {
			
			//登录亲加服务器成功，跳转到主页
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			
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
	

}
