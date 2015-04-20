package com.nongguanjia.doctorTian.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;

/**
 * @author tx
 * 我
 */
public class FgMy extends Fragment {
	private TextView tv_title;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my, container,false);
		
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		tv_title.setText("我");
		
		return view;
	}

}
