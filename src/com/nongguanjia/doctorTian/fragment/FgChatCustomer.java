package com.nongguanjia.doctorTian.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.AddFriendActivity;
import com.nongguanjia.doctorTian.ContractActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.CustomerAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllAttention;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 客户服务 -- 我的客户（推广人）
 */
public class FgChatCustomer extends Fragment {
	private ListView listView;
//	private SideBar sideBar;
	private ArrayList<AllAttention> atts;
	private CustomerAdapter adapter;
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;
	private Dialog noticeDialog;
	
	private RefreshReceiver mReceiver;
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		mReceiver = new RefreshReceiver();
		IntentFilter intentFilter = new IntentFilter(CommonConstant.BROADCAST_ACTION);
		activity.registerReceiver(mReceiver, intentFilter);
		
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_customer, container,false);
		listView = (ListView)view.findViewById(R.id.cons_list);
//		sideBar = (SideBar)view.findViewById(R.id.sideBar);
		
		mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.list_position, null);
        
		head = LayoutInflater.from(getActivity()).inflate(R.layout.top_customer, null);
		listView.addHeaderView(head);
		
		mDialogText.setVisibility(View.INVISIBLE);
		mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
//		sideBar.setTextView(mDialogText);
		
		adapter = new CustomerAdapter(getActivity().getApplicationContext());
		listView.setAdapter(adapter);
		
		getAllAttention();
		
		head.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showNoticeDialog();
			}
		});
		
		return view;
	}

	
	private void getAllAttention(){
		String phoneNum = ((AppApplication)getActivity().getApplication()).PHONENUM;
		String url = CommonConstant.allattentions + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				//解析应答数据
				try {
					if(response.getJSONObject("AllAttentions").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("AllAttentions").getJSONArray("allAttentions");
						Gson gson = new Gson();
						atts = new ArrayList<AllAttention>();
						atts = gson.fromJson(ja.toString(), new TypeToken<List<AllAttention>>(){}.getType());
						//根据a-z进行排序
//						Collections.sort(atts,new PinyinComparator());
						
//						sideBar.setListView(listView);
						
						adapter.updateData(atts);
						
					}else{
						Toast.makeText(getActivity().getApplicationContext(), "获取好友失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	
	private void showNoticeDialog(){
		final String[] types = new String[]{"手动添加 ", "一键上传通讯录"};
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("选择添加好友方式"); 
		builder.setItems(types, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent;
				if(which == 0){
					intent = new Intent(getActivity(), AddFriendActivity.class);
					startActivity(intent);
				}else if(which == 1){
					intent = new Intent(getActivity(), ContractActivity.class);
					intent.putExtra("FriendList", (Serializable)atts);
					startActivity(intent);
				}
			}
		});
		
		noticeDialog = builder.create();
		noticeDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		noticeDialog.show();
	}

	
	//广播接收器
	public class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			getAllAttention();
		}

	}	
	
	
	
}
