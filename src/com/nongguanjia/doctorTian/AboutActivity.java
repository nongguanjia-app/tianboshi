package com.nongguanjia.doctorTian;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.Abouts;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
/**
 * @author 关于我们
 */
public class AboutActivity extends Activity implements OnClickListener {
	private TextView tv_title;
	private ImageView img_back;
	private TextView tv_version, about_intro, tv_tel, tv_address, tv_copyright, tv_name;
	private ProgressDialog mDialog;
	private Abouts about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_item);
		
		init();
	}
	
	private void init() {
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		tv_version = (TextView)findViewById(R.id.tv_version);
		about_intro = (TextView)findViewById(R.id.about_intro);
		tv_tel = (TextView)findViewById(R.id.tv_tel);
		tv_address = (TextView)findViewById(R.id.tv_address);
		tv_copyright = (TextView)findViewById(R.id.tv_copyright);
		tv_name = (TextView)findViewById(R.id.tv_name);
		
		tv_title.setText("关于");
		img_back.setOnClickListener(this);
		
		mDialog.show();
		getAbouts();
	}
	
	public void getAbouts() {
		String url = CommonConstant.abouts;
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
					if(new JSONArray(response.getString("Abouts")).getJSONObject(0).getString("returnCode").equals("1")){
						Gson gson = new Gson();
						about = new Abouts();
						JSONObject jo = new JSONArray(response.getString("Abouts")).getJSONObject(0);
						about = gson.fromJson(jo.toString(), new TypeToken<Abouts>(){}.getType());
						showView();
					}else{
						Toast.makeText(AboutActivity.this, "获取关于数据失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	private void showView(){
		mDialog.dismiss();
		tv_version.setText(about.getVersion());
		about_intro.setText(about.getSummary());
		tv_tel.setText(about.getPhone());
		tv_address.setText(about.getAddress());
		tv_copyright.setText(about.getCopyright());
		tv_name.setText(about.getName());
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			break;
		default:
			break;
		}
	}
}
