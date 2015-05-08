package com.nongguanjia.doctorTian.adapter;

import java.util.List;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.Allunits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaSpinnerAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Allunits> list;
	
	public AreaSpinnerAdapter(Context context,List<Allunits> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1 = inflater.inflate(R.layout.spinner_item, null);
		}
		TextView tv = (TextView) arg1.findViewById(R.id.textView_spinner);
		tv.setText(list.get(arg0).getName());
		return arg1;
	}

}
