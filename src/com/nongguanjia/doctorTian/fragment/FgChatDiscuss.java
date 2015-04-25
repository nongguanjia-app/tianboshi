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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.DiscussAllreviewsActivity;
import com.nongguanjia.doctorTian.ExpInfoActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.FgChatDiscussAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllTalks;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 讨论 -- 交流|客户服务
 */
public class FgChatDiscuss extends Fragment {
	private ListView listView;
	private int pageIndex = 1;
	private boolean isSuccess = false;
	private ArrayList<AllTalks> talkList;
	private FgChatDiscussAdapter adapter;
	private LinearLayout footerView;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_discuss, container,false);
		listView = (ListView)view.findViewById(R.id.chat_discuss_list);
		
		adapter = new FgChatDiscussAdapter(getActivity());
		listView.setAdapter(adapter);
		
		View fview = inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)fview.findViewById(R.id.foot_layout);
		listView.addFooterView(fview);
		
		getAllTalks();
		
		return view;
	}
	
	
	private void getAllTalks(){
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.alltalks + "/" + pageIndex + "," + phoneNum;
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
					if(response.getJSONObject("AllTalks").getString("returnCode").equals("1")){
						isSuccess = true;
						JSONArray ja = response.getJSONObject("AllTalks").getJSONArray("allTalks");
						Gson gson = new Gson();
						talkList = new ArrayList<AllTalks>();
						talkList = gson.fromJson(ja.toString(), new TypeToken<List<AllTalks>>(){}.getType());
						
						adapter.getTalks().addAll(talkList);
						adapter.notifyDataSetChanged();
						
						setListViewInfo();
						
					}else{
						isSuccess = false;
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
	
	
	
	private void setListViewInfo(){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				Bundle bd = new Bundle();
				AllTalks talk = adapter.getTalks().get(position);
				if(talk.getIsExperience().equals("1")){ //经验谈
					intent = new Intent(getActivity(), ExpInfoActivity.class);
					bd.putString("ExperienceId", talk.getExperienceId());
				}else{
					intent = new Intent(getActivity(), DiscussAllreviewsActivity.class);
					bd.putString("courseId", talk.getCourseId());
				}
				 
				intent.putExtras(bd);
				startActivity(intent);
				
			}
			
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//当不滚动时
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					//判断是否滚动到底部
					if(view.getLastVisiblePosition() == view.getCount() - 1){
						
						if(adapter.getCount() % 8 == 0){
							if(isSuccess){
								pageIndex = pageIndex + 1;
								
								//加载更多
								footerView.setVisibility(View.VISIBLE);
								
								getAllTalks();
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
