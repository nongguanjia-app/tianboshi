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
import android.widget.AdapterView.OnItemClickListener;
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
	private String courseId;
	private ListView listView;
	private FgCourseExpAdapter adapter;
	private int pageIndex = 1;
	
	private ArrayList<AllExperiences> experienceList;

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
						JSONArray ja = response.getJSONObject("AllExperiences").getJSONArray("allExperiences");
						Gson gson = new Gson();
						experienceList = new ArrayList<AllExperiences>();
						experienceList = gson.fromJson(ja.toString(), new TypeToken<List<AllExperiences>>(){}.getType());
						adapter = new FgCourseExpAdapter(getActivity(), experienceList);
						listView.setAdapter(adapter);
						
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), ExpInfoActivity.class);
								Bundle bd = new Bundle();
								bd.putString("ExperienceId", experienceList.get(position).getExperienceId());
								bd.putString("Flag", experienceList.get(position).getFlage());
								intent.putExtras(bd);
								getActivity().startActivity(intent);
							}
							
						});
						
					}else{
						Toast.makeText(getActivity(), "获取全部课程失败", Toast.LENGTH_SHORT).show();
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
