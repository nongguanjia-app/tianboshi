package com.nongguanjia.doctorTian;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.adapter.CategoryAdapter;
import com.nongguanjia.doctorTian.bean.AllCategoryCourses;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.view.RTPullListView;

/**
 * @author tx
 * 分类下的全部课程
 */
public class CategoryActivity extends Activity {
	private TextView tv_name;
	private RTPullListView  listView;
	private CategoryAdapter adapter;
	private ProgressDialog mDialog;
	private String id;
	private String name;
	private int pageIndex = 1;
	private ArrayList<AllCategoryCourses> courseList;
	
	private ProgressBar moreProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		init();
	}

	
	private void init(){
		mDialog = new ProgressDialog(CategoryActivity.this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		Bundle bd = getIntent().getExtras();
		id = bd.getString("Id");
		name = bd.getString("name");
		
		tv_name= (TextView)findViewById(R.id.tv_title);
		listView = (RTPullListView)findViewById(R.id.category_list);
		
		tv_name.setText(name);
		
		mDialog.show();
		getCategory();
	}
	
	
	private void getCategory(){
		String url = CommonConstant.categorycourses + "/" + id + "," + pageIndex;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				try {
					if(response.getJSONObject("AllCategoryCourses").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("AllCategoryCourses").getJSONArray("allCategoryCourses");
						Gson gson = new Gson();
						courseList = new ArrayList<AllCategoryCourses>();
						courseList = gson.fromJson(ja.toString(), new TypeToken<List<AllCategoryCourses>>(){}.getType());
						adapter = new CategoryAdapter(getApplicationContext(), courseList);
						listView.setAdapter(adapter);
						
						setListViewInfo();
						
					}else{
						Toast.makeText(getApplicationContext(), "获取全部课程失败", Toast.LENGTH_SHORT).show();
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CategoryActivity.this, CourseActivity.class);
				Bundle bd = new Bundle();
				bd.putString("courseId", courseList.get(position).getCourseid());
				intent.putExtras(bd);
				startActivity(intent);
			}
			
		});
		
	}
	
	
}
