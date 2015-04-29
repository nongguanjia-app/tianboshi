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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.StartCoursesAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllStartCourse;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.ToastUtil;
import com.nongguanjia.doctorTian.view.RTPullListView;
import com.nongguanjia.doctorTian.view.RTPullListView.OnRefreshListener;

/**
 * @author 我的课程 -- 已开始
 */
public class FgMyCourseHasStart extends Fragment {
	private RTPullListView mListView;
	private String TAG = FgMyCourseHasStart.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflater = null;
	private StartCoursesAdapter mStartCoursesAdapter;
	private List<AllStartCourse> mAllStartCourse;
	private LinearLayout footerView;
	private int pageIndex = 1;
	private boolean isSuccess = false;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		super.onAttach(activity);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.has_start, null);
		mListView = (RTPullListView) view.findViewById(R.id.has_start_list);
		
		
		init(view);
		return view;
	}

	private void init(View view) {
		View v= inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)v.findViewById(R.id.foot_layout);
		mListView.addFooterView(v);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
		// 下拉刷新监听器
		mListView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (mAllStartCourse != null) {
					getStartDetail();
					mStartCoursesAdapter.refreshData(mAllStartCourse);
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
						
						if(mStartCoursesAdapter.getCount() % 8 == 0){
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
		String url = CommonConstant.startcourses + "/" + phoneNum + ","+ "1";
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
					if (response.getJSONObject("StartCourses")
							.getString("returnCode").equals("1")) {
						JSONArray ja = response.getJSONObject("StartCourses")
								.getJSONArray("allStartCourse");
						// 解析应答数据
						Gson gson = new Gson();
						mAllStartCourse = new ArrayList<AllStartCourse>();
						mAllStartCourse = gson.fromJson(ja.toString(),
								new TypeToken<List<AllStartCourse>>() {
								}.getType());
						// Log.e(TAG, "执行"+ allStartCourse);
						mStartCoursesAdapter = new StartCoursesAdapter(getActivity(), mAllStartCourse);
						mListView.setAdapter(mStartCoursesAdapter);
					} else {
						Toast.makeText(activity, "获取分类信息失败", Toast.LENGTH_SHORT).show();
						footerView.setVisibility(View.GONE);
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
