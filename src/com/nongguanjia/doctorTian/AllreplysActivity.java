package com.nongguanjia.doctorTian;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	
	private String id;//经验谈id|课程id
	private String talkId;
	private String isExp; //是否是经验谈
	private int pageIndex = 1;
	
	private ArrayList<AllReply> replys;
	private AllReplysAdapter adapter;
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				Toast.makeText(AllreplysActivity.this, "获取回复内容失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				try {
					JSONObject data = new JSONObject(msg.obj.toString());
					if(data.getInt("pageCount") > 0){
						Gson gson = new Gson();
						replys = new ArrayList<AllReply>();
						JSONArray ja = data.getJSONArray("allReplys");
						replys = gson.fromJson(ja.toString(), new TypeToken<List<AllReply>>(){}.getType());
						adapter.setReplys(replys);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
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
		Bundle bd = getIntent().getExtras();
		id = bd.getString("id");
		talkId = bd.getString("talkId");
		isExp = bd.getString("isExp");
		
		tv_title = (TextView)findViewById(R.id.tv_title);
		listView = (ListView)findViewById(R.id.allreply_list);
		adapter = new AllReplysAdapter(AllreplysActivity.this);
		
		tv_title.setText("评论回复");
		
		getAllReplys();
	}
	
	
	private void getAllReplys(){
		String url = CommonConstant.allreplys + "/" + id + ","
				+ talkId + ","
				+ isExp + ","
				+ pageIndex;
		AllReplyTask task = new AllReplyTask(url, mHandler);
		task.getAllReplys();
	}

}
