package com.nongguanjia.doctorTian;

import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.AllLecture;
import com.nongguanjia.doctorTian.bean.DeleteSubscribe;
import com.nongguanjia.doctorTian.bean.Lectures;
import com.nongguanjia.doctorTian.bean.Subscribe;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author itachi 专家详情
 */
public class ExpertDetailActivity extends Activity implements OnClickListener {
	private String TAG = ExpertDetailActivity.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflater = null;
	private LinearLayout layout;
	private Context mContext;
	private Lectures mLectures;
	private Subscribe mSubscribes;
	private List<Subscribe> mSubscribeDetail;
	private List<DeleteSubscribe> mDeleteSubscribeDetail;
	private DeleteSubscribe mDeleteSubscribe;
	private Button mSubscribe_btn;
	private Button mSubscribe_btn2;
	private ImageView mExpertPhoto,mExpert_Back;
	private TextView mName, mPT, mAddress, mField, mYear, mInfo,mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expert_item);
		AllLecture allLecture = (AllLecture) getIntent().getExtras().get("AllLecture");
		Log.d("s", "^^^" + allLecture.toString());
		String name = allLecture.getName();
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("专家详情页面");
		initViews();
		getExpert();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		mExpertPhoto = (ImageView) findViewById(R.id.expert_photo);
		mName = (TextView) findViewById(R.id.expert_name);
		mPT = (TextView) findViewById(R.id.expert_PT);
		mAddress = (TextView) findViewById(R.id.expert_Address);
		mField = (TextView) findViewById(R.id.expert_Field);
		mYear = (TextView) findViewById(R.id.expert_Year);
		mInfo = (TextView) findViewById(R.id.expert_Info);
		mSubscribe_btn = (Button) findViewById(R.id.subscribe_btn);
		mSubscribe_btn2 = (Button) findViewById(R.id.subscribe_btn2);
		mExpert_Back = (ImageView) findViewById(R.id.img_back);
		mSubscribe_btn.setOnClickListener(this);
		mSubscribe_btn2.setOnClickListener(this);
		mExpert_Back.setOnClickListener(this);
	}

	private void getExpert() {
		String url = CommonConstant.lecture + "/" + "13804563652,13349954813";
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(activity, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.getJSONObject("Lectures").getString("returnCode").equals("1")) {
						JSONObject ja = response.getJSONObject("Lectures");
						// 解析应答数据
						Gson gson = new Gson();
						mLectures = gson.fromJson(ja.toString(), Lectures.class);
						// 就在这儿mLectures 这个对象里的数据 设置你布局的属性
						mName.setText("姓名：" + mLectures.getLectureName());
						mPT.setText("职称：" + mLectures.getLecturePT());
						mAddress.setText("所在单位："+ mLectures.getLectureAddress());
						mField.setText("研究方向：" + mLectures.getLectureField());
						mYear.setText("研究时间：" + mLectures.getLectureYear());
						mInfo.setText(mLectures.getLectureInfo());

					} else {
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String url = CommonConstant.addsubscribe + "/" + "15618509847,13349954813";
		String url2 = CommonConstant.deletesubscribe + "/" + "15618509847,13349954813";
		
		//String url = "addsubscribe/15618509847,13349954813";
		//String url2 = "deletesubscribe/15618509847,13349954813";
		switch (v.getId()) {
		case R.id.subscribe_btn:
			DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					// TODO Auto-generated method stub
					try {
//						JSONObject ja = response.getJSONObject("Subscribe");
						JSONObject ja = response.getJSONObject("AddSubscribe");
						// 解析应答数据
						Gson gson = new Gson();
						mSubscribes = gson.fromJson(ja.toString(), Subscribe.class);
						if (mSubscribes.getReturnCode().equals("1")) {
							Log.e(TAG, "it"+mSubscribes);
							Toast.makeText(getApplicationContext(), "订阅成功", Toast.LENGTH_SHORT).show();
							mSubscribe_btn2.setVisibility(View.VISIBLE);
							mSubscribe_btn.setVisibility(View.GONE);
						}else{
							Toast.makeText(getApplicationContext(), "订阅失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.onSuccess(statusCode, headers, response);
				}
			});
			break;
			
		case R.id.subscribe_btn2:
			DoctorTianRestClient.get(url2, null, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					Toast.makeText(activity, "请求接口异常", Toast.LENGTH_SHORT).show();
					super.onFailure(statusCode, headers, responseString, throwable);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						JSONObject ja = response.getJSONObject("DeleteSubscribe");
						// 解析应答数据
						Gson gson = new Gson();
						mSubscribes = gson.fromJson(ja.toString(), Subscribe.class);
						if (mSubscribes.getReturnCode().equals("1")) {
							Toast.makeText(getApplicationContext(), "取消订阅成功", Toast.LENGTH_SHORT).show();
							mSubscribe_btn2.setVisibility(View.GONE);
							mSubscribe_btn.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(activity, "取消订阅失败", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.onSuccess(statusCode, headers, response);
				}
			});
			break;
		case R.id.img_back:
			finish();
			break;	
		default:
			break;
		}
	}
}