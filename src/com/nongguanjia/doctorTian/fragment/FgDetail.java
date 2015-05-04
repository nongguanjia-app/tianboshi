package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.ExpertDetailActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.SuccessCaseActivity;
import com.nongguanjia.doctorTian.adapter.DetailAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.Courses;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author itachi 课程详情
 */
public class FgDetail extends Fragment {
	private Activity activity;
	private ExpandableListView expListView;
	private Courses mCourses;
	
	private String courseId;
	
	private List<String> groupData = new ArrayList<String>();
	
	private DetailAdapter adapter;
	
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		
		groupData.add("课程简介");
		groupData.add("成功案例");
		
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.course_item, container, false);
		expListView = (ExpandableListView)view.findViewById(R.id.exp_list);
		
		getDetail();
		
		return view;
	}


	private void getDetail() {
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.course + "/" + courseId +","+ phoneNum;
		
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
					if (response.getJSONObject("Courses").getString("returnCode").equals("1")) {
						JSONObject ja = response.getJSONObject("Courses");
						// 解析应答数据
						Gson gson = new Gson();
						mCourses = gson.fromJson(ja.toString(), Courses.class);
						
						//发送广播通知更新我的客户列表
						Intent intent = new Intent();
						intent.putExtra("vid", mCourses.getCourseVideo());
						intent.putExtra("flag", mCourses.getFlag());
						intent.putExtra("title", mCourses.getTitle());
						intent.setAction(CommonConstant.VIDEO_ACTION);
						activity.sendBroadcast(intent);
						
						adapter = new DetailAdapter(activity, groupData, mCourses);
						expListView.setAdapter(adapter);
						
						expListView.setGroupIndicator(null);
						
						for(int i = 0; i < groupData.size(); i++){
							expListView.expandGroup(i);
						}
						
						setListViewInfo();
						
					} else {
						Toast.makeText(activity, "获取分类信息失败", Toast.LENGTH_SHORT).show();
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
		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
					int childPos, long id) {
				// TODO Auto-generated method stub
				
				
				Intent intent;
				if(groupPos == 0){
					intent = new Intent(getActivity(), ExpertDetailActivity.class);
					String lectureId = mCourses.getAllLecture().get(childPos).getLectureId();
					intent.putExtra("lectureId", lectureId);
				}else{
					intent = new Intent(getActivity(), SuccessCaseActivity.class);
					String caseId = mCourses.getAllCase().get(childPos).getId();
					intent.putExtra("caseId", caseId);
				}
				startActivity(intent);
				
				return true;
			}
		});
		
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
	}
	
}
