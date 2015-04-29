package com.nongguanjia.doctorTian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nongguanjia.doctorTian.adapter.ContactAdapter;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllAttention;
import com.nongguanjia.doctorTian.bean.ContractInfo;
import com.nongguanjia.doctorTian.http.CustomerHttpClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.GetContractUtil;

/**
 * @author tx
 * 联系人列表
 */
public class ContractActivity extends Activity {
	private TextView tv_title;
	private ImageView img_back;
	private ListView listView;
	private ContactAdapter adapter;
	private List<ContractInfo> contracts;
	private Button btn_commit;
	
	private String phoneNum;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contract);
		
		init();
	}
	
	private void init(){
		phoneNum = ((AppApplication)getApplication()).PHONENUM;
		
		btn_commit = (Button)findViewById(R.id.btn_commit);
		tv_title = (TextView)findViewById(R.id.tv_title);
		img_back = (ImageView)findViewById(R.id.img_back);
		listView = (ListView)findViewById(R.id.listview);
		tv_title.setText("通讯录");
		btn_commit.setVisibility(View.VISIBLE);
		
		contracts = GetContractUtil.getPhoneContracts(this);
		
		ArrayList<AllAttention> friendList = (ArrayList<AllAttention>)getIntent().getSerializableExtra("FriendList");
		//已经添加的好友不再显示
		for(int i = 0; i < friendList.size(); i++){
			for(int j = 0; j < contracts.size(); j++){
				if(contracts.get(j).getPhoneNum().equals(friendList.get(i).getTelephone())){
					contracts.remove(contracts.get(j));
				}
			}
		}
		
		adapter = new ContactAdapter(this, contracts);
		listView.setAdapter(adapter);
		
		btn_commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list = adapter.getList();
				UploadTask task = new UploadTask();
				task.execute();
			}
		});
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContractActivity.this.finish();
			}
		});
	}
	
	
	
	
	private class UploadTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			String url = CommonConstant.uploadAttention;
			
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < list.size(); i++){
				sb.append(phoneNum)
				.append(",")
				.append(list.get(i).get("tel").trim())
				.append(",")
				.append(list.get(i).get("name"))
				.append(";");
			}
			
			String response = CustomerHttpClient.post(url, sb.toString());
			
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject response = new JSONObject(result);
				if(response.getJSONObject("UpLoadAttention").getString("returnCode").equals("1")){
					Toast.makeText(ContractActivity.this, "上传通讯录成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ContractActivity.this, "上传通讯录失败", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	

}
