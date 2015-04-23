package com.nongguanjia.doctorTian;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SystemActivity extends Activity implements OnClickListener{
	private ImageView mSystem_Back;
	private TextView mSystem_title;
	private Button mUpgrade_system;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_upgrade);
		mSystem_Back = (ImageView) findViewById(R.id.img_back);
		mSystem_title = (TextView) findViewById(R.id.tv_title);
		mUpgrade_system = (Button) findViewById(R.id.btn_system);
		mSystem_Back.setOnClickListener(this);
		mSystem_title.setText("系统升级");
		mUpgrade_system.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.btn_system:
			Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}
	}
}
