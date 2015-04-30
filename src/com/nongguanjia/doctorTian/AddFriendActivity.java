package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendActivity extends Activity implements OnClickListener{
	private TextView tv_title;
	private ImageView img_back;
	private EditText ed_tel;
	private Button btn_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);
		init();
	}
	
	private void init(){
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		tv_title.setText("添加好友");
		ed_tel = (EditText)findViewById(R.id.ed_tel);
		btn_confirm = (Button)findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);
		img_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			if(TextUtils.isEmpty(ed_tel.getText())){
				Toast.makeText(AddFriendActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
			}else{
				if(ed_tel.getText().toString().length() != 11){
					Toast.makeText(AddFriendActivity.this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
				}else{
					addFriend();
				}
			}
			break;
		case R.id.img_back:
			AddFriendActivity.this.finish();
			break;
		default:
			break;
		}
	}

	
	private void addFriend(){
		String phoneNum = ((AppApplication)getApplication()).PHONENUM;
		String url = CommonConstant.addattention + "/" 
				+ ed_tel.getText().toString() + ","
				+ phoneNum + ","
				+ "2";
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
				try {
					if(response.getJSONObject("AddAttention").getString("returnCode").equals("1")){
						Toast.makeText(AddFriendActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
						//发送广播通知更新我的客户列表
						Intent intent = new Intent();
						intent.setAction(CommonConstant.BROADCAST_ACTION);
						sendBroadcast(intent);
						
						AddFriendActivity.this.finish();
					}else{
						Toast.makeText(AddFriendActivity.this, "添加好友失败", Toast.LENGTH_SHORT).show();
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
