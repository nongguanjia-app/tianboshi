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
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.adapter.FgDiscussAreaAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllReviews;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.task.AddTalkTask;
import com.nongguanjia.doctorTian.utils.CommonConstant;

public class DiscussAllreviewsActivity extends Activity {
	private TextView tv_name;
	private ImageView img_back;
	private String courseId;
	private ListView listView;
	private EditText ed_info;
	private Button btn_send;
	private int pageIndex = 1;
	private boolean isSuccess = false;
	private LinearLayout footerView;
	private FgDiscussAreaAdapter adapter;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				Toast.makeText(DiscussAllreviewsActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				Toast.makeText(DiscussAllreviewsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
				pageIndex = 1;//相当于刷新
				getAllreviews();//重新获取数据
				ed_info.setText("");
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discuss_layout);
		
		init();
		
		getAllreviews();
	}
	
	
	private void init(){
		Bundle bd = getIntent().getExtras();
		courseId = bd.getString("courseId");
		
		adapter = new FgDiscussAreaAdapter(this, courseId);
		
		tv_name = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		listView = (ListView)findViewById(R.id.discuss_list);
		ed_info = (EditText)findViewById(R.id.ed_info);
		btn_send = (Button)findViewById(R.id.btn_send);
		tv_name.setText("讨论区");
		
		listView.setAdapter(adapter);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)layout.findViewById(R.id.foot_layout);
		listView.addFooterView(layout);
		
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(ed_info.getText())){
					Toast.makeText(DiscussAllreviewsActivity.this, "请输入评论内容", Toast.LENGTH_SHORT).show();
				}else{
					addtalk(ed_info.getText().toString());
				}
				
			}
		});
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiscussAllreviewsActivity.this.finish();
			}
		});
	
	}
	

	private void getAllreviews(){
		String url = CommonConstant.allreviews + "/" + courseId + "," + pageIndex;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Toast.makeText(DiscussAllreviewsActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if(response.getJSONObject("AllReviews").getString("returnCode").equals("1")){
						isSuccess = true;
						
						JSONArray ja = response.getJSONObject("AllReviews").getJSONArray("allReviews");
						Gson gson = new Gson();
						ArrayList<AllReviews> reviewList = new ArrayList<AllReviews>();
						reviewList = gson.fromJson(ja.toString(), new TypeToken<List<AllReviews>>(){}.getType());
						
						adapter.getReviews().addAll(reviewList);
						adapter.notifyDataSetChanged();
						
						setListViewInfo();
					}else{
						isSuccess = false;
						Toast.makeText(DiscussAllreviewsActivity.this, "获取讨论区内容失败", Toast.LENGTH_SHORT).show();
					}
					footerView.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	private void addtalk(String content){
		String phoneNum = ((AppApplication)getApplication()).PHONENUM;
		String url = CommonConstant.addtalk + "/" + courseId + "," 
					+ phoneNum + ","
					+ content + ","
					+ "0"; //非经验谈
		AddTalkTask task = new AddTalkTask(url, mHandler);
		task.addTalk();
	}
	
	
	
	private void setListViewInfo(){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DiscussAllreviewsActivity.this, AllreplysActivity.class);
				Bundle bd = new Bundle();
				bd.putString("id", courseId);
				bd.putString("talkId", adapter.getReviews().get(position).getTalkId());
				bd.putString("isExp", "0");
				bd.putString("phoneNum", adapter.getReviews().get(position).getPhone());
				intent.putExtras(bd);
				startActivity(intent);
			}
			
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//当不滚动时
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					//判断是否滚动到底部
					if(view.getLastVisiblePosition() == view.getCount() - 1){
						
						if(adapter.getCount() % 8 == 0){
							if(isSuccess){
								pageIndex = pageIndex + 1;
								
								//加载更多
								footerView.setVisibility(View.VISIBLE);
								
								getAllreviews();
							}
							
						}
						
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
