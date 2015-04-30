package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.db.CacheUserHelper;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
/**
 * @author tx
 * 修改密码
 */
public class UpdatePassActivity extends Activity implements OnClickListener{
	private EditText ed_phone, ed_old, ed_new, ed_again;
	private Button btn_save;
	private CacheUserHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_pass);
		
		init();
	}
	
	private void init(){
		helper = CacheUserHelper.getInstance(getApplicationContext());
		
		ed_phone = (EditText)findViewById(R.id.edit_phone);
		ed_old = (EditText)findViewById(R.id.edit_psd);
		ed_new = (EditText)findViewById(R.id.edit_new);
		ed_again = (EditText)findViewById(R.id.edit_psd_again);
		btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
	}
	
	
	private void editPsd(String old, String newPsd){
		String phoneNum = ((AppApplication)getApplication()).PHONENUM;
		String url = CommonConstant.editpassword + "/" + phoneNum + "," + old + "," + newPsd;
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
					if(new JSONArray(response.getString("EditPassword")).getJSONObject(0).getString("returnCode").equals("1")){
						helper.deleteTable();
						Toast.makeText(UpdatePassActivity.this, "修改密码成功，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(UpdatePassActivity.this, LoginActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(UpdatePassActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_save:
			if(TextUtils.isEmpty(ed_phone.getText())){
				Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
			}else{
				if(TextUtils.isEmpty(ed_old.getText())){
					Toast.makeText(getApplicationContext(), "请输入旧密码", Toast.LENGTH_SHORT).show();
				}else{
					if(TextUtils.isEmpty(ed_new.getText())){
						Toast.makeText(getApplicationContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
					}else{
						if(TextUtils.isEmpty(ed_again.getText())){
							Toast.makeText(getApplicationContext(), "请再次输入新密码", Toast.LENGTH_SHORT).show();
						}else{
							//判断两次密码是否相同
							if(!ed_again.getText().toString().equals(ed_new.getText().toString())){
								Toast.makeText(getApplicationContext(), "两次密码不同，请重新输入", Toast.LENGTH_SHORT).show();
							}else{
								editPsd(ed_old.getText().toString(), ed_new.getText().toString());
							}
						}
					}
				}
			}
			break;

		default:
			break;
		}
	}
	
	
	
	
}
