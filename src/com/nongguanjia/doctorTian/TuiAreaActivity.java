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

public class TuiAreaActivity extends Activity{
	private ListView listView;
	private TextView tvTitle;
	private ImageView ivBack;
	private List<Map<String,String>> dataSheng;
	private TuiAreaAdapter adapter;
	private CacheCityHelper helper;
	
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
		tvTitle.setText("选择省市");
		
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TuiAreaActivity.this.finish();
			}
		});
		helper = new CacheCityHelper(TuiAreaActivity.this);
		dataSheng = helper.selectArea(0);
		
		adapter = new TuiAreaAdapter(TuiAreaActivity.this, dataSheng);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int id = Integer.valueOf(dataSheng.get(arg2).get("id").toString());
				Intent intent = new Intent(TuiAreaActivity.this, TuiAreaShiActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", dataSheng.get(arg2).get("name").toString());
				startActivity(intent);
				TuiAreaActivity.this.finish();
			}
		});
	}
	

}
