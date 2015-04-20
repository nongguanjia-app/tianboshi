package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.CategoryActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.GridViewAdapter;
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
	private LinearLayout layout;
	private List<AllCategorys> categorys;
	private LayoutInflater inflater = null;
	private GridViewAdapter gridAdapter;
	private final int WRAP = LayoutParams.WRAP_CONTENT;
	private final int MATCH = LayoutParams.MATCH_PARENT;
	
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
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.course_res, container,false);
		init(view);
		
		return view;
	}
	
	private void init(View view){
		layout = (LinearLayout)view.findViewById(R.id.category_layout);
		getCategory();
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
		for(int i = 0; i < names.size(); i++){
			//分类
			View view = inflater.inflate(R.layout.course_res_type, null);
			TextView tv = (TextView)view.findViewById(R.id.tv_type);
			TextView tv_count = (TextView)view.findViewById(R.id.tv_count);
			GridView gridView = (GridView)view.findViewById(R.id.gridview);
			
			//添加分类名称
			tv.setText(names.get(i));
			tv_count.setText(list.get(i).get(names.get(i)).size()+"");
			layout.addView(view);
			
			//添加内部子类
			final ArrayList<AllCategorys> cates = list.get(i).get(names.get(i));
			
			gridAdapter = new GridViewAdapter(activity, cates);
			gridView.setAdapter(gridAdapter);
			
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(activity, CategoryActivity.class);
					Bundle bd = new Bundle();
					bd.putString("Id", cates.get(position).getId());
					bd.putString("name", cates.get(position).getName());
					intent.putExtras(bd);
					activity.startActivity(intent);
				}
				
			});
		}
	}
	

}
