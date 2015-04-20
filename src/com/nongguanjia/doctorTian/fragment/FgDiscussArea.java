package com.nongguanjia.doctorTian.fragment;

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
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.AllreplysActivity;
import com.nongguanjia.doctorTian.ExpInfoActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.FgDiscussAreaAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllReviews;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.task.AddTalkTask;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 讨论区 --课程库
 */
public class FgDiscussArea extends Fragment {
	private String courseId;
	private ListView listView;
	private EditText ed_info;
	private Button btn_send;
	private int pageIndex = 1;
	private FgDiscussAreaAdapter adapter;
	
	private ArrayList<AllReviews> reviewList;

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				Toast.makeText(getActivity(), "评论失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_discuss_area, container,false);
		init(view);
		getAllreviews();
		
		return view;
	}
	
	
	private void init(View view){
		listView = (ListView)view.findViewById(R.id.discuss_list);
		ed_info = (EditText)view.findViewById(R.id.ed_info);
		btn_send = (Button)view.findViewById(R.id.btn_send);
		
		ed_info.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if(ed_info.getText().toString().trim().length() > 0){
					//按钮改变颜色
					btn_send.setTextColor(getActivity().getResources().getColor(R.color.send_btn_color));
				}else{
					btn_send.setTextColor(getActivity().getResources().getColor(R.color.login_txt));
				}
			}
			
		});
		
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addtalk(ed_info.getText().toString());
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
				Toast.makeText(getActivity(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if(response.getJSONObject("AllReviews").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("AllReviews").getJSONArray("allReviews");
						Gson gson = new Gson();
						reviewList = new ArrayList<AllReviews>();
						reviewList = gson.fromJson(ja.toString(), new TypeToken<List<AllReviews>>(){}.getType());
						adapter = new FgDiscussAreaAdapter(getActivity(), courseId);
						adapter.setReviews(reviewList);
						listView.setAdapter(adapter);
						
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), AllreplysActivity.class);
								Bundle bd = new Bundle();
								bd.putString("id", courseId);
								bd.putString("talkId", reviewList.get(position).getTalkId());
								bd.putString("isExp", "0");
								intent.putExtras(bd);
								startActivity(intent);
							}
							
						});
						
					}else{
						Toast.makeText(getActivity(), "获取讨论区内容失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}


	
	private void addtalk(String content){
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.addtalk + "/" + courseId + "," 
					+ phoneNum + ","
					+ content + ","
					+ "0"; //非经验谈
		AddTalkTask task = new AddTalkTask(url, mHandler);
		task.addTalk();
	}
	
	
}
