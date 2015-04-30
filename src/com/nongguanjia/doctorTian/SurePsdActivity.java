package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.db.CacheUserHelper;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.MD5Util;

/**
 * @author itachi 忘记密码
 */
public class SurePsdActivity extends Activity implements OnClickListener {
	private EditText ed_psd, ed_psd_again;
	private Button last_btn_next;
	private CacheUserHelper cacheUser;
	private ProgressDialog mDialog;
	private String phoneNum;
	private String psd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sure_psd);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		cacheUser = CacheUserHelper.getInstance(getApplicationContext());
		phoneNum = getIntent().getStringExtra("phoneNumber");
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);

		last_btn_next = (Button) findViewById(R.id.last_btn_next);
		ed_psd = (EditText) findViewById(R.id.input_psd);
		ed_psd_again = (EditText) findViewById(R.id.again_input_psd);
		last_btn_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.last_btn_next:
			// 添加验证
			if (!TextUtils.isEmpty(ed_psd.getText())) {
				if (!TextUtils.isEmpty(ed_psd_again.getText())) {
					mDialog.show();
					psd = MD5Util.GetMD5Code(ed_psd.getText().toString());
					getpsd();
				} else {
					Toast.makeText(getApplicationContext(), "请再次输入密码",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请输入密码",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	public void getpsd() {
		// TODO Auto-generated method stub
		String url = CommonConstant.newpassword + "/" + phoneNum + "," + psd;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				Toast.makeText(getApplicationContext(), "请求接口异常",Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				mDialog.dismiss();
				// 解析应答数据
				try {
					if(response.getJSONArray("NewPassword").getJSONObject(0).getString("returnCode").equals("1")){
						Toast.makeText(getApplicationContext(), "修改密码成功",Toast.LENGTH_SHORT).show();
						
						Intent intent = new Intent(SurePsdActivity.this, LoginActivity.class);
						startActivity(intent);
						
						SurePsdActivity.this.finish();
					} else {
						Toast.makeText(getApplicationContext(), "修改密码失败",Toast.LENGTH_SHORT).show();
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
