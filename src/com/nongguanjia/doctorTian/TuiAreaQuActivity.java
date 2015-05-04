package com.nongguanjia.doctorTian;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.adapter.TuiAreaAdapter;
import com.nongguanjia.doctorTian.db.CacheCityHelper;

public class TuiAreaQuActivity extends Activity {
	private ListView listView;
	private TextView tvTitle;
	private ImageView ivBack;
	private List<Map<String,String>> data;
	private TuiAreaAdapter adapter;
	private CacheCityHelper helper;
	private int id;
	private String sheng,shi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tui_area);
		init();
	}

	
	private void init() {
		listView = (ListView) findViewById(R.id.listView_tuiArea);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		ivBack = (ImageView) findViewById(R.id.img_back);
		tvTitle.setText("选择城市");
		
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TuiAreaQuActivity.this.finish();
			}
		});
		
		Intent intent = getIntent();
		id = intent.getIntExtra("id", -1);
		sheng = intent.getStringExtra("sheng");
		shi = intent.getStringExtra("shi");
		
		helper = new CacheCityHelper(TuiAreaQuActivity.this);
		if(id!=-1){
			data= helper.selectArea(id);
		}
		
		adapter = new TuiAreaAdapter(TuiAreaQuActivity.this, data);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent("TuiAreaQuActivity");
				intent.putExtra("sheng", sheng);
				intent.putExtra("shi", shi);
				intent.putExtra("qu", data.get(arg2).get("name").toString());
				sendBroadcast(intent);
				TuiAreaQuActivity.this.finish();
			}
		});
		
	}
	
	
}
