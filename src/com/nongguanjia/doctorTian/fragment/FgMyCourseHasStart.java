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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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

/**
 * @author 我的课程 -- 已开始
 */
public class FgMyCourseHasStart extends Fragment {
	private ListView mListView;
	private String TAG = FgMyCourseHasStart.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflater = null;
	private StartCoursesAdapter mStartCoursesAdapter;
	private List<AllStartCourse> mAllStartCourse;

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
		mListView = (ListView) view.findViewById(R.id.has_start_list);
		init(view);
		return view;
	}

	private void init(View view) {
		// TODO Auto-generated method stub
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
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
