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
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.adapter.AllechosAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllEcho;
import com.nongguanjia.doctorTian.bean.ExperienceInfo;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.task.AddTalkTask;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author tx
 * 经验谈详情
 */
public class ExpInfoActivity extends Activity implements OnClickListener{
	private TextView tv_title, tv_info_title, tv_time, tv_summary; 
	private ImageView img_back;
	private ImageView img;
	private TextView tv_name, tv_product; 
	private TextView tv_content;
	private ListView listView;
	private EditText ed_info;
	private Button btn_send, btn_attention;
	private AllechosAdapter adapter;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private String expId;
//	private String flag;
	private ExperienceInfo info;
	private ArrayList<AllEcho> allEchoList;
	private int pageIndex = 1;
	
	private LinearLayout footerView;
	private boolean isSuccess = false;
	private String phoneNum;
	private boolean isAttention = false;
	private ProgressDialog mDialog;
	
	private Html.ImageGetter imageGetter;
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				Toast.makeText(ExpInfoActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				Toast.makeText(ExpInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
				pageIndex = 1;//相当于刷新
				allEchoList = null;
				getAllechos();//重新获取数据
				ed_info.setText("");
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	
	
//	final Html.ImageGetter imageGetter = new Html.ImageGetter() {
//
//	    public Drawable getDrawable(String source) {
//	        Drawable drawable=null;
//		    URL url;
//		    try {
//		    	source = CommonConstant.img_exp_img + source.substring(2);
//		        url = new URL(source);
//		        drawable = Drawable.createFromStream(url.openStream(), "");
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        return null;
//		    }
//		    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());            
//		    return drawable;
//		}
//	};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exp_info);
		
		init();
	}
	
	
	private void init(){
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在加载请稍后...");
		mDialog.setIndeterminate(true);
		mDialog.setCancelable(true);
		
		Bundle bd = getIntent().getExtras();
		expId = bd.getString("ExperienceId");
//		flag = bd.getString("Flag");
		
		phoneNum = ((AppApplication)getApplication()).PHONENUM;
		
		adapter = new AllechosAdapter(ExpInfoActivity.this, expId);
				
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		listView = (ListView)findViewById(R.id.talk_list);
		
		LayoutInflater inflater = LayoutInflater.from(ExpInfoActivity.this);
		View header = inflater.inflate(R.layout.expinfo_header, null);
		tv_info_title = (TextView)header.findViewById(R.id.info_title);
		tv_time = (TextView)header.findViewById(R.id.tv_time);
		tv_summary = (TextView)header.findViewById(R.id.tv_summary);
		tv_name = (TextView)header.findViewById(R.id.name);
		tv_product = (TextView)header.findViewById(R.id.product);
		tv_content = (TextView)header.findViewById(R.id.tv_content);
		img = (ImageView)header.findViewById(R.id.img);
		btn_attention = (Button)header.findViewById(R.id.btn_attention);
		listView.addHeaderView(header);
		
		ed_info = (EditText)findViewById(R.id.ed_info);
		btn_send = (Button)findViewById(R.id.btn_send);
		
		btn_attention.setOnClickListener(this);
		img_back.setOnClickListener(this);
		
		ed_info.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(ed_info.getText())){
					//按钮改变颜色
					btn_send.setTextColor(ExpInfoActivity.this.getResources().getColor(R.color.send_btn_color));
					btn_send.setOnClickListener(ExpInfoActivity.this);
				}else{
					btn_send.setTextColor(ExpInfoActivity.this.getResources().getColor(R.color.login_txt));
					btn_send.setEnabled(false);
				}
			}
			
		});
		
		View view = inflater.inflate(R.layout.list_footview, null);
		footerView = (LinearLayout)view.findViewById(R.id.foot_layout);
		listView.addFooterView(view);
		
		mDialog.show();
		getExperienceInfo();
		
		getAllechos();
	}
	
	
	private void getExperienceInfo(){
		String url = CommonConstant.experienceinfo + "/" + phoneNum + "," + expId;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
				try {
					if(response.getJSONObject("ExperienceInfos").getString("returnCode").equals("1")){
						Gson gson = new Gson();
						info = new ExperienceInfo();
						info = gson.fromJson(response.getJSONObject("ExperienceInfos").toString(), new TypeToken<ExperienceInfo>(){}.getType());
						
						//判断好友是否存在
						verifyattention();
						
						showView();
					}else{
						Toast.makeText(getApplicationContext(), "获取经验谈详情失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});	
	}
	
	
	//获取讨论区内容
	private void getAllechos(){
		String url = CommonConstant.allechos + "/" + expId + "," + pageIndex;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				if(mDialog.isShowing()){
					mDialog.dismiss();
				}
				try {
					if(response.getJSONObject("AllEchos").getString("returnCode").equals("1")){
						isSuccess = true;
						
						Gson gson = new Gson();
						ArrayList<AllEcho> echoList = new ArrayList<AllEcho>();
						JSONArray ja = response.getJSONObject("AllEchos").getJSONArray("allEchos");
						echoList = gson.fromJson(ja.toString(), new TypeToken<List<AllEcho>>(){}.getType());
						
						if(allEchoList == null){
							allEchoList = new ArrayList<AllEcho>();
						}
						allEchoList.addAll(echoList);
						
						adapter.setEchos(allEchoList);
						
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						
						setListViewInfo();
					}else{
						isSuccess = false;
						Toast.makeText(getApplicationContext(), "获取讨论区内容失败", Toast.LENGTH_SHORT).show();
					}
					
					footerView.setVisibility(View.GONE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	
	
	
 	private void showView(){
		tv_title.setText(info.getCourseTitle());
		
		tv_info_title.setText(info.getTitle());
		tv_time.setText(info.getCreatedTime());
		tv_summary.setText(info.getSummary());
		tv_name.setText(info.getName());
		tv_product.setText(info.getProducts());
		
		//判断html是否含有图片
		if(info.getContent().indexOf("img") > 0){
//			tv_content.setText(Html.fromHtml(info.getContent(), imageGetter, null));
		}else{
//			tv_content.setText(Html.fromHtml(info.getContent()));
		}
		
		tv_content.setText(Html.fromHtml(info.getContent()));
		
	}

 	
 	
 	private void addtalk(String content){
		String url = CommonConstant.addtalk + "/" + expId + "," 
					+ phoneNum + ","
					+ content + ","
					+ "1"; //经验谈
		AddTalkTask task = new AddTalkTask(url, mHandler);
		task.addTalk();
	}

 	
 	
 	//取消关注推广人
 	private void delAttention(){
 		String url = CommonConstant.delAttention + "/"
 				+ info.getTelephone() + ","
 				+ phoneNum;
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
				// TODO Auto-generated method stub
				try {
					if(response.getJSONObject("DeleteAttention").getString("returnCode").equals("1")){
						Toast.makeText(ExpInfoActivity.this, "取消关注成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ExpInfoActivity.this, "取消关注失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
 			
 		});
 	}
 	
 	
 	//关注推广人
 	private void addAttention(){
		String url = CommonConstant.addattention + "/" 
				+ info.getTelephone() + ","
				+ phoneNum + ","
				+ "2";
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
				try {
					if(response.getJSONObject("AddAttention").getString("returnCode").equals("1")){
						Toast.makeText(ExpInfoActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(ExpInfoActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
 	
 	
 	//判断好友是否存在
 	private void verifyattention(){
 		String url = CommonConstant.verifyattention + "/" 
 				+ info.getTelephone() 
 				+ "," + phoneNum;
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
				try {
					if(response.getJSONObject("VerifyAttention").getString("returnCode").equals("1")){
						isAttention = true;
						btn_attention.setBackgroundResource(R.drawable.btn_attention_on);
					}else{
						isAttention = false;
						btn_attention.setBackgroundResource(R.drawable.btn_attention_off);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
 			
 		});
 	}
 	
 	
 	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			addtalk(ed_info.getText().toString());
			break;
		case R.id.btn_attention:
			isAttention = !isAttention;
			if(isAttention){ //已经关注 -- 取消关注
				btn_attention.setBackgroundResource(R.drawable.btn_attention_off);
				delAttention();
			}else{ //未关注 -- 添加关注
				btn_attention.setBackgroundResource(R.drawable.btn_attention_on);
				addAttention();
			}
			break;
		case R.id.img_back:
			this.finish();
			break;
		default:
			break;
		}
	}
 	
	
	
	private void setListViewInfo(){
		//点击item
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ExpInfoActivity.this, AllreplysActivity.class);
				Bundle bd = new Bundle();
				bd.putString("id", expId);
				bd.putString("talkId", allEchoList.get(position-1).getTalkId());
				bd.putString("isExp", "1");
				bd.putString("phoneNum", allEchoList.get(position-1).getPhone());
				intent.putExtras(bd);
				startActivity(intent);
			}
			
		});
		
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
								
								getAllechos();
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
