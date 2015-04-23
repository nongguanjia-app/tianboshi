package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.AllHelps;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends Activity {
	private String TAG = HelpActivity.class.getSimpleName();
	private TextView mHelp_word,mTitle;
	private AllHelps mAllHelps;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		mHelp_word = (TextView) findViewById(R.id.help_word);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("帮助中心");
		init();
	}

	public void init() {
		String url = "helps";
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
					if(response.getJSONArray("Helps").getJSONObject(0).getString("returnCode").equals("1")){
						Gson gson = new Gson();
						mAllHelps = gson.fromJson(response.toString(),AllHelps.class);
						Log.e(TAG, "解析"+mAllHelps);
						mHelp_word.setText(mAllHelps.getHelps().get(0).getContent());
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
