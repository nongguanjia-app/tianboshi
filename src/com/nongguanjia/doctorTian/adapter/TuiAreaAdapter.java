package com.nongguanjia.doctorTian.adapter;

import java.util.List;
import java.util.Map;

import com.nongguanjia.doctorTian.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 推广人选择地区适配器
 * @author itachi
 *
 */
public class TuiAreaAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Map<String,String>> list;
	
	public TuiAreaAdapter(Context context,List<Map<String,String>> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public void dataChange(List<Map<String,String>> list){
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
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
			arg1 = inflater.inflate(R.layout.tui_area_item, null);
		}
		TextView textView = (TextView) arg1.findViewById(R.id.textView_tuiAreaItem);
		textView.setText(list.get(arg0).get("name").toString());
		return arg1;
	}

}
