package com.nongguanjia.doctorTian;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.bean.AllAbouts;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
/**
 * @author 关于我们
 */
public class AboutActivity extends Activity implements OnClickListener {
	private String TAG = AboutActivity.class.getSimpleName();
	private ImageView mAboub_Back;
	private TextView mTitle,mAbout_version,mAbout_summary,mCompany_tel,mCompany_add,mCopyright;
	private AllAbouts mAbouts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_item);
		initViews();
		init();
	}
	private void initViews() {
		mTitle = (TextView) findViewById(R.id.tv_title);
		mTitle.setText("关于我们");
		mAboub_Back = (ImageView) findViewById(R.id.img_back);
		mAboub_Back.setOnClickListener(this);
		mAbout_version = (TextView) findViewById(R.id.about_version);
		mAbout_summary = (TextView) findViewById(R.id.about_summary);
		mCompany_tel = (TextView) findViewById(R.id.about_tel);
		mCompany_add = (TextView) findViewById(R.id.about_address);
		mCopyright = (TextView) findViewById(R.id.about_copyright);
	}
	
	public void init() {
		String url = "abouts";
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
					if(response.getJSONArray("Abouts").getJSONObject(0).getString("returnCode").equals("1")){
						Gson gson = new Gson();
						mAbouts = gson.fromJson(response.toString(),AllAbouts.class);
						mAbout_version.setText(mAbouts.getAbouts().get(0).getVersion());
						mAbout_summary.setText(mAbouts.getAbouts().get(0).getSummary());
						mCompany_tel.setText("公司电话："+mAbouts.getAbouts().get(0).getTelephone());
						mCompany_add.setText("公司地址："+mAbouts.getAbouts().get(0).getAddress());
						mCopyright.setText(mAbouts.getAbouts().get(0).getCopyright());
						//mAboutAdapter = new AboutAdapter(getApplicationContext(), mAbouts.getAbouts());
						//mListView.setAdapter(mAboutAdapter);
					}else{
						Toast.makeText(getApplicationContext(), "获取全部课程失败", Toast.LENGTH_SHORT).show();
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
		case R.id.img_back:
			finish();
			break;
		default:
			break;
		}
	}
}
