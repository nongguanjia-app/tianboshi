package com.nongguanjia.doctorTian.fragment;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.CourseTableAdapter;
import com.nongguanjia.doctorTian.bean.AllChaptersNong;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author itachi 课程表
 */
public class FgCourse extends Fragment {
	private Activity activity;
	private ListView mListView;
	private LayoutInflater inflater = null;
	private LinearLayout layout;
	private AllChaptersNong allChapters;
	private CourseTableAdapter mCourseTableAdapter;
	private List<AllChaptersNong> mAllChaptersList;
	private String courseId;
	
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_course, container,false);
		init(view);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mAllChaptersList!=null){
					String flag = mAllChaptersList.get(arg2).getIsbo().toString();
					if("1".equals(flag)){
						String uvid = mAllChaptersList.get(arg2).getMediaId().toString();
						Intent intent = new Intent(CommonConstant.VIDEO_ACTION);
						intent.putExtra("vid", uvid);
//						intent.putExtra("title", mAllChaptersList.get(arg2).getName());
						activity.sendBroadcast(intent);
					}else{
						Toast.makeText(activity, "目前不能播放，敬请期待！", 0).show();
					}
				}
			}
		});
		return view;
	}

	private void init(View view) {
		// TODO Auto-generated method stub
		mListView = (ListView) view.findViewById(R.id.course_table);
		getTableDetail();
	}

	private void getTableDetail() {
		String url = CommonConstant.allchapters + "/" + courseId;
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
					if (response.getJSONObject("AllChapters").getString("returnCode").equals("1")) {
						JSONArray ja = response.getJSONObject("AllChapters").getJSONArray("allChapters");
						// 解析应答数据
						Gson gson = new Gson();
						mAllChaptersList = gson.fromJson(ja.toString(), new TypeToken<List<AllChaptersNong>>(){}.getType());
						mCourseTableAdapter = new CourseTableAdapter(getActivity(), mAllChaptersList);
						mListView.setAdapter(mCourseTableAdapter);
					
					} else {
						Toast.makeText(activity, "获取分类信息失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

}
