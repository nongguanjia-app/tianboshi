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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.UnStartCoursesAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllUnStartCourses;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.ToastUtil;
import com.nongguanjia.doctorTian.view.RTPullListView;
import com.nongguanjia.doctorTian.view.RTPullListView.OnRefreshListener;

/**
 * @author tx 我的课程 -- 即将开始
 */
public class FgMyCourseWillStart extends Fragment {
	private Activity activity;
	private RTPullListView mListView;
	private LayoutInflater inflater = null;
	private LinearLayout layout;
	private UnStartCoursesAdapter mUnStartCoursesAdapter;
	private List<AllUnStartCourses> mAllUnStartCourses;
	
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
		View view = inflater.inflate(R.layout.will_start, null);

		init(view);
		return view;
	}

	private void init(View view) {
		mListView = (RTPullListView) view.findViewById(R.id.un_start_list);
		View v= inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)v.findViewById(R.id.foot_layout);
		mListView.addFooterView(v);
		
		
		// 下拉刷新监听器
				mListView.setonRefreshListener(new OnRefreshListener() {
					@Override
					public void onRefresh() {
						if (mAllUnStartCourses != null) {
							getUnStartDetail();
							mUnStartCoursesAdapter.refreshData(mAllUnStartCourses);
						} else {
							ToastUtil.show(getActivity(), "没有更多历史消息");
						}
						mListView.onRefreshComplete();
					}
				});
				
				mListView.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						//当不滚动时
						if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
							//判断是否滚动到底部
							if(view.getLastVisiblePosition() == view.getCount() - 1){
								
								if(mUnStartCoursesAdapter.getCount() % 8 == 0){
									if(isSuccess){
										pageIndex = pageIndex + 1;
										//加载更多
										footerView.setVisibility(View.VISIBLE);
										getUnStartDetail();
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
		
		getUnStartDetail();
	}

	private void getUnStartDetail() {
		// TODO Auto-generated method stub
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.unstartcourses + "/" + phoneNum + ","+ "1";
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
					if (response.getJSONObject("UnStartCourses")
							.getString("returnCode").equals("1")) {
						JSONArray ja = response.getJSONObject("UnStartCourses")
								.getJSONArray("allUnStartCourses");
						// 解析应答数据
						Gson gson = new Gson();
						mAllUnStartCourses = new ArrayList<AllUnStartCourses>();
						mAllUnStartCourses = gson.fromJson(ja.toString(),
								new TypeToken<List<AllUnStartCourses>>() {
								}.getType());
						// Log.e(TAG, "执行"+ allStartCourse);
						mUnStartCoursesAdapter = new UnStartCoursesAdapter(
								getActivity(), mAllUnStartCourses);
						mListView.setAdapter(mUnStartCoursesAdapter);
					} else {
						Toast.makeText(activity, "获取分类信息失败", Toast.LENGTH_SHORT)
								.show();
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
