package com.nongguanjia.doctorTian;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AreaActivity extends Activity implements OnClickListener {
	private ImageView mArea_back;
	private TextView mAreaTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plant_area);
		mAreaTitle = (TextView) findViewById(R.id.tv_title);
		mAreaTitle.setText("设置种植作物面积");
		mArea_back = (ImageView) findViewById(R.id.img_back);
		mArea_back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		default:
			break;	
		}
	}
}
