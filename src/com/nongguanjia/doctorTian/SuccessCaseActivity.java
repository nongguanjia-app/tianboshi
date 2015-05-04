package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.CaseInfo;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author tx
 * 成功案例
 */
public class SuccessCaseActivity extends Activity {
	private TextView tv_title;
	private ImageView img_back;
	private TextView suc_title;
	private TextView tv_detail;
	private ImageView img_pic;
	private String caseId;
	
	private CaseInfo info;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suc_case);
		
		init();
	}
	
	
	private void init(){
		caseId = getIntent().getExtras().getString("caseId");
		
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		suc_title = (TextView)findViewById(R.id.suc_title);
		tv_detail = (TextView)findViewById(R.id.case_detail);
		img_pic = (ImageView)findViewById(R.id.img);
		
		tv_title.setText("成功案例");
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SuccessCaseActivity.this.finish();
			}
		});
		
		getDetail();
	}
	
	
	private void getDetail(){
		String url = CommonConstant.caseinfo + "/" + caseId;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {

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
					if (response.getJSONObject("CaseInfos").getString("returnCode").equals("1")) {
						Gson gson = new Gson();
						info = new CaseInfo();
						info = gson.fromJson(response.getJSONObject("CaseInfos").toString(), new TypeToken<CaseInfo>(){}.getType());
						showView();
					}else{
						Toast.makeText(SuccessCaseActivity.this, "获取成功案例失败", Toast.LENGTH_SHORT).show();
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
		suc_title.setText(info.getCaseTitle());
		tv_detail.setText(Html.fromHtml(info.getCaseContent()));
		imageLoader.displayImage(CommonConstant.img_course_case + info.getCasePicture(), img_pic, options);
	}
	

}
