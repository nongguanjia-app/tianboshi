package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.CourseTypeAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllCategorys;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 课程库
 */
public class FgCourseCenter extends Fragment {
	private Activity activity;
	private ExpandableListView expListView;
	private List<AllCategorys> categorys;
	private CourseTypeAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.course_res, container,false);
		expListView = (ExpandableListView)view.findViewById(R.id.exp_list);
		getCategory();

		return view;
	}
	
	
	
	private void getCategory(){
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.allcategorys + "/" + phoneNum;
		
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(activity, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				//解析应答数据
				try {
					if(response.getJSONObject("AllCategorys").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("AllCategorys").getJSONArray("allCategorys");
						Gson gson = new Gson();
						categorys = new ArrayList<AllCategorys>();
						categorys = gson.fromJson(ja.toString(), new TypeToken<List<AllCategorys>>(){}.getType());
						
						List<String> secondCategorys = new ArrayList<String>();
						List<HashMap<String, ArrayList<AllCategorys>>> second = null;
						
						for(int i = 0; i < categorys.size(); i++){
							if(TextUtils.isEmpty(categorys.get(i).getParentName())){ // 顶级分类
								String topName = categorys.get(i).getName();
								
								second = new ArrayList<HashMap<String, ArrayList<AllCategorys>>>();
								
								//二级分类
								for(int j = 0; j < categorys.size(); j++){ 
									if(categorys.get(j).getParentName().equals(topName)){ //二级分类
										secondCategorys.add(categorys.get(j).getName());
										
										HashMap<String, ArrayList<AllCategorys>> map = new HashMap<String, ArrayList<AllCategorys>>();
										
										ArrayList<AllCategorys> cell = new ArrayList<AllCategorys>();;
										
										for(int k = 0; k < categorys.size(); k++){
											if(categorys.get(k).getParentName().equals(categorys.get(j).getName())){
												cell.add(categorys.get(k));
												continue;
											}
										}
										map.put(categorys.get(j).getName(), cell); //key--二级分类名  value--二级分类内容
										//添加
										second.add(map);
									}
								}
								break;
							}
						}
						
						showView(secondCategorys, second);
						
					}else{
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
	
	
	private void showView(List<String> names, List<HashMap<String, ArrayList<AllCategorys>>> list){
		adapter = new CourseTypeAdapter(getActivity(), names, list);
		expListView.setAdapter(adapter);
		
		expListView.setGroupIndicator(null);
		
		for(int i = 0; i < names.size(); i++){
			expListView.expandGroup(i);
		}
		
	}
	

}
