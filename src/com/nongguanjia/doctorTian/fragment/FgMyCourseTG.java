package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.MyCourseAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.CourseMy;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.ToastUtil;
import com.nongguanjia.doctorTian.view.RTPullListView;
import com.nongguanjia.doctorTian.view.RTPullListView.OnRefreshListener;

public class FgMyCourseTG extends Fragment {
	private RTPullListView listView;
	private MyCourseAdapter courseAdapter;
	private Activity activity;
	private List<CourseMy> myCourse;
	
	private LayoutInflater inflater = null;
	private LinearLayout footerView;
	private int pageIndex = 1;
	private boolean isSuccess = false;
	
	
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fg_course_tg, null);
		init(v);
		return v;
	}

	private void init(View view) {
		listView = (RTPullListView) view.findViewById(R.id.myCouserTG_list);
		View v= inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)v.findViewById(R.id.foot_layout);
		listView.addFooterView(v);
		
		// 下拉刷新监听器
		listView.setonRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						if (myCourse != null) {
							getStartDetail();
							courseAdapter.refreshData(myCourse);
						} else {
							ToastUtil.show(getActivity(), "没有更多历史消息");
						}
						listView.onRefreshComplete();
					}
				});
				
		listView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						//当不滚动时
						if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
							//判断是否滚动到底部
							if(view.getLastVisiblePosition() == view.getCount() - 1){
								
								if(courseAdapter.getCount() % 8 == 0){
									if(isSuccess){
										pageIndex = pageIndex + 1;
										//加载更多
										footerView.setVisibility(View.VISIBLE);
										getStartDetail();
									}
								}
							}
						}
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						
					}
				});
		
		getStartDetail();
	}
	
	private void getStartDetail() {
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.alltelephoneexperiences + "/" + "1" + ","+ phoneNum;
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
					if (response.getJSONObject("AllTelephoneExperience").getString("returnCode").equals("1")) {
						JSONArray ja = response.getJSONObject("AllTelephoneExperience").getJSONArray("allTelephoneExperiences");
						// 解析数据
						Gson gson = new Gson();
						myCourse = new ArrayList<CourseMy>();
						myCourse = gson.fromJson(ja.toString(),
								new TypeToken<List<CourseMy>>() {}.getType());
						// Log.e(TAG, "执行"+ allStartCourse);
						courseAdapter = new MyCourseAdapter(getActivity(), myCourse);
						listView.setAdapter(courseAdapter);
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

}
