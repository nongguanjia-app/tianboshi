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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.ExpertDetailActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.CourseAdapter;
import com.nongguanjia.doctorTian.bean.AllLecture;
import com.nongguanjia.doctorTian.bean.Courses;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
/**
 * @author itachi 课程详情
 */
public class FgDetail extends Fragment {
	private Activity activity;
	private ListView alllecture_listview;
	private LayoutInflater inflater = null;
	private LinearLayout layout;
	private Courses mCourses;
	private CourseAdapter mCourseAdapter;
	private List<Courses> mAllCoursesList;
	private TextView mCourseIntro;
	private Bundle db;
	//private ArrayList<AllLecture> AllLecture_list;
	
	
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
		View view = inflater.inflate(R.layout.course_item, container, false);
		mCourseIntro = (TextView) view.findViewById(R.id.course_intro);
		// mLinearLayout = (LinearLayout) view.findViewById(R.id.lin_zhu);
		alllecture_listview = (ListView) view.findViewById(R.id.alllecture_listview);
		// mLinearLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent it = new Intent(getActivity(),ExpertDetailActivity.class);
		// startActivity(it);
		// }
		// });
		init(view);
		return view;
	}

	private void init(View view) {
		// TODO Auto-generated method stub
		getDetail();
	}
	ArrayList<AllLecture> AllLecture_list;
	private void getDetail() {
		// TODO Auto-generated method stub
		//db = this.getArguments();
		//String id = db.getString("Id");
		//String name = db.getString("name");
	//	Log.d("zhao", "-----" + id + "*" + name);
		String url = CommonConstant.course + "/38,13778866445";
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
						// String str = getString(R.string.json);
						mAllCoursesList = new ArrayList<Courses>();
						mCourses = gson.fromJson(ja.toString(), Courses.class);
						Log.d("zhao", "-----" + mCourses);
						mAllCoursesList.add(mCourses);
						AllLecture_list = mCourses.getAllLecture();
						Log.v("zhao", "&&" + AllLecture_list);
						mCourseIntro.setText(mCourses.getCourseIntro()+ "----------");
						mCourseAdapter = new CourseAdapter(getActivity(),AllLecture_list);
						alllecture_listview.setAdapter(mCourseAdapter);
						alllecture_listview.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
										Intent intent = new Intent(getActivity(),ExpertDetailActivity.class);
										AllLecture allLecture = AllLecture_list.get(arg2);
										intent.putExtra("AllLecture",allLecture);
										startActivity(intent);
									}
								});
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
