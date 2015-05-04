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

public class TuiAreaShiActivity extends Activity {
	private ListView listView;
	private TextView tvTitle;
	private ImageView ivBack;
	private List<Map<String,String>> data;
	private TuiAreaAdapter adapter;
	private CacheCityHelper helper;
	private int id;
	private String ShengName;
	
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
				TuiAreaShiActivity.this.finish();
			}
		});
		
		Intent intent = getIntent();
		id = intent.getIntExtra("id", -1);
		ShengName = intent.getStringExtra("name");
		helper = new CacheCityHelper(TuiAreaShiActivity.this);
		if(id!=-1){
			data= helper.selectArea(id);
		}
		
		adapter = new TuiAreaAdapter(TuiAreaShiActivity.this, data);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int id = Integer.valueOf(data.get(arg2).get("id").toString());
				Intent intent = new Intent(TuiAreaShiActivity.this, TuiAreaQuActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("shi", data.get(arg2).get("name").toString());
				intent.putExtra("sheng", ShengName);
				startActivity(intent);
				TuiAreaShiActivity.this.finish();
			}
		});
	}
}
