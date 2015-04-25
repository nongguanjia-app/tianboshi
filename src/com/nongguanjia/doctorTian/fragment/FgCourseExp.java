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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.ExpInfoActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.FgCourseExpAdapter;
import com.nongguanjia.doctorTian.bean.AllExperiences;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 经验谈 -- 课程库
 */
public class FgCourseExp extends Fragment {
	private ListView listView;
	private FgCourseExpAdapter adapter;
	private int pageIndex = 1;
	private boolean isSuccess = false;
	private String courseId;
	private LinearLayout footerView;

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_course_exp, container,false);
		listView = (ListView)view.findViewById(R.id.course_list);
		
		View layout = inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)layout.findViewById(R.id.foot_layout);
		listView.addFooterView(layout);
		
		adapter = new FgCourseExpAdapter(getActivity());
		listView.setAdapter(adapter);
		
		getAllexperiences();
		
		return view;
	}
	
	
	private void getAllexperiences(){
		String url = CommonConstant.allexperiences + "/" + courseId + "," + pageIndex;
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
					if(response.getJSONObject("AllExperiences").getString("returnCode").equals("1")){
						isSuccess = true;
						
						JSONArray ja = response.getJSONObject("AllExperiences").getJSONArray("allExperiences");
						Gson gson = new Gson();
						List<AllExperiences> experienceList = new ArrayList<AllExperiences>();
						experienceList = gson.fromJson(ja.toString(), new TypeToken<List<AllExperiences>>(){}.getType());
						
						adapter.getExperiences().addAll(experienceList);
						adapter.notifyDataSetChanged();
						
						setListViewInfo();
					}else{
						isSuccess = false;
						Toast.makeText(getActivity(), "获取全部课程失败", Toast.LENGTH_SHORT).show();
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
	
	
	
	private void setListViewInfo(){
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ExpInfoActivity.class);
				Bundle bd = new Bundle();
				bd.putString("ExperienceId", adapter.getExperiences().get(position).getExperienceId());
				bd.putString("Flag", adapter.getExperiences().get(position).getFlage());
				intent.putExtras(bd);
				getActivity().startActivity(intent);
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
								
								getAllexperiences();
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
