package com.nongguanjia.doctorTian;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nongguanjia.doctorTian.adapter.AllReplysAdapter;
import com.nongguanjia.doctorTian.bean.AllReply;
import com.nongguanjia.doctorTian.task.AllReplyTask;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 回复信息
 */
public class AllreplysActivity extends Activity {
	private ListView listView;
	private TextView tv_title;
	private ImageView img_back;
	
	private String id;//经验谈id|课程id
	private String talkId;
	private String isExp; //是否是经验谈
	private String phoneNum;
	private int pageIndex = 1;
	private LinearLayout footerView;
	private boolean isSuccess = false;
	
	private AllReplysAdapter adapter;
	
	private ProgressDialog mDialog;
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mDialog.dismiss();
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				isSuccess = false;
				Toast.makeText(AllreplysActivity.this, "获取回复内容失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				try {
					JSONObject data = new JSONObject(msg.obj.toString());
					if(data.getInt("pageCount") > 0){
						isSuccess = true;
						
						Gson gson = new Gson();
						List<AllReply> replys = new ArrayList<AllReply>();
						JSONArray ja = data.getJSONArray("allReplys");
						replys = gson.fromJson(ja.toString(), new TypeToken<List<AllReply>>(){}.getType());
						
						adapter.getReplys().addAll(replys);
						adapter.notifyDataSetChanged();
						
						setListViewInfo();
					}else{
						Toast.makeText(AllreplysActivity.this, "暂时没有评论内容", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allreplys);
		
		init();
	}
	
	
	private void init(){
		mDialog = new ProgressDialog(AllreplysActivity.this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		Bundle bd = getIntent().getExtras();
		id = bd.getString("id");
		talkId = bd.getString("talkId");
		isExp = bd.getString("isExp");
		phoneNum = bd.getString("phoneNum");
		
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		listView = (ListView)findViewById(R.id.allreply_list);
		adapter = new AllReplysAdapter(AllreplysActivity.this);
		
		listView.setAdapter(adapter);
		
		LayoutInflater inflater = LayoutInflater.from(AllreplysActivity.this);
		View view = inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)view.findViewById(R.id.foot_layout);
		listView.addFooterView(view);
		
		tv_title.setText("评论回复");
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AllreplysActivity.this.finish();
			}
		});
		
		mDialog.show();
		getAllReplys();
	}
	
	
	private void getAllReplys(){
		String url = CommonConstant.allreplys + "/" + id + ","
				+ talkId + ","
				+ isExp + ","
				+ pageIndex + ","
				+ phoneNum;
		AllReplyTask task = new AllReplyTask(url, mHandler);
		task.getAllReplys();
	}
	
	
	private void setListViewInfo(){
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//当不滚动时
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					//判断是否滚动到底部
					if(view.getLastVisiblePosition() == view.getCount() - 1){
						
						if(adapter.getCount() % 8 == 0){
							if(isSuccess){
								pageIndex = pageIndex + 1;
								
								//加载更多
								footerView.setVisibility(View.VISIBLE);
								
								getAllReplys();
							}
							
						}
						
					}
				}
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
