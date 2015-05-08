package com.nongguanjia.doctorTian;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.adapter.AreaSpinnerAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.Allunits;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

public class AreaActivity extends Activity implements OnClickListener{
	private ImageView mArea_back;
	private TextView mAreaTitle;
	private EditText etArea;
	private Spinner spArea;
	private String areaId = "14";
	private int selectId = 0;
	private Button btSubmit;
	private List<Allunits> listAllunits;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_area);
		mAreaTitle = (TextView) findViewById(R.id.tv_title);
		mArea_back = (ImageView) findViewById(R.id.img_back);
		etArea = (EditText) findViewById(R.id.EditText_area);
		spArea = (Spinner) findViewById(R.id.spinner_area);
		btSubmit = (Button) findViewById(R.id.btn_area);
		
		getAllunits();
		
		mAreaTitle.setText("设置种植作物面积");
		mArea_back.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		spArea.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				areaId = listAllunits.get(arg2).getId();
				selectId = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			AreaActivity.this.finish();
			break;
		case R.id.btn_area:
			String data = etArea.getText().toString();
			if(TextUtils.isEmpty(data)){
				Toast.makeText(AreaActivity.this, "面积不能为空", 0).show();
			}else{
				Intent intent = new Intent("com.nongguanjia.doctorTian");
				intent.putExtra("number", data);
				intent.putExtra("dan", areaId);
				intent.putExtra("danName", listAllunits.get(selectId).getName());
				sendBroadcast(intent);
				AreaActivity.this.finish();
			}
			break;
		}
	}

	
	/**
	 * 获取面积单位
	 */
	private void getAllunits(){
		String phoneNum = ((AppApplication)AreaActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.allunits + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(AreaActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getJSONObject("AllUnits").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("AllUnits").getJSONArray("allUnit");
						Gson gson = new Gson();
						listAllunits = new ArrayList<Allunits>();
						listAllunits = gson.fromJson(ja.toString(), new TypeToken<List<Allunits>>(){}.getType());
						
						AreaSpinnerAdapter adapter = new AreaSpinnerAdapter(AreaActivity.this, listAllunits);
						spArea.setAdapter(adapter);
						
					}else{
						Toast.makeText(AreaActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
