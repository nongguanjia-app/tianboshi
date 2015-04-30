package com.nongguanjia.doctorTian;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.adapter.UpgradeAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllStatement;
import com.nongguanjia.doctorTian.bean.AllStatementInfo;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
/**
 * @author itachi-升级推广人
 */
public class UpgradeActivity extends Activity implements OnClickListener {
	private String TAG = UpgradeActivity.class.getSimpleName();
	private UpgradeAdapter mUpgradeAdapter;
	private ListView mListView;
	private AllStatementInfo mAllStatementInfo;
	private List<AllStatement> mAllStatement;
	private ImageView mUpgrade_back;
	private Button mButton;
	private String phoneNum;
	private TextView mTitle;
	private ImageView img_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgrade);
		mListView = (ListView) findViewById(R.id.upgrade_list);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("升级为推广人");
		img_back = (ImageView)findViewById(R.id.img_back);
		mButton = (Button) findViewById(R.id.btn_update);
		mButton.setOnClickListener(this);
		img_back.setOnClickListener(this);
		init();
	}

	public void init() {
		phoneNum = ((AppApplication)getApplication()).PHONENUM;
		String url = CommonConstant.statements + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "请求接口异常",Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				// mDialog.dismiss();
				try {
					if (response.getJSONObject("AllStatements").getString("returnCode").equals("1")) {
						JSONArray ja = response.getJSONObject("AllStatements").getJSONArray("allStatement");
						Gson gson = new Gson();
						mAllStatement = gson.fromJson(ja.toString(),new TypeToken<List<AllStatement>>() {}.getType());
						mUpgradeAdapter = new UpgradeAdapter(getApplicationContext(), mAllStatement);
						mListView.setAdapter(mUpgradeAdapter);
					} else {
						Toast.makeText(getApplicationContext(), "获取全部课程失败",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.onSuccess(statusCode, headers, response);
			}

		});
	}
	
	private void init_update() {
		// TODO Auto-generated method stub
		phoneNum = ((AppApplication)getApplication()).PHONENUM;
		String url = CommonConstant.upstatement + "/" + phoneNum;
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
				//解析应答数据
				try {
					if(response.getJSONObject("UpStatement").getString("returnCode").equals("1")){
						Toast.makeText(getApplicationContext(), "升级成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "升级失败", Toast.LENGTH_SHORT).show();
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
		case R.id.btn_update:
			init_update();
			break;
		case R.id.img_back:
			UpgradeActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	
}
