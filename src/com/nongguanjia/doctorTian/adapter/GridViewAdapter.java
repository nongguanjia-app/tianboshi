package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllCategorys;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GridViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private ArrayList<AllCategorys> list;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private ViewHolder holder = null;
	
	
	public GridViewAdapter(Context context, ArrayList<AllCategorys> list){
		this.context = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		options = Options.getOptions(true);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(list != null && list.size() != 0){
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllCategorys category = list.get(position);
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_cell, null);
			holder.img = (ImageView)convertView.findViewById(R.id.item_img);
			holder.text = (TextView)convertView.findViewById(R.id.item_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		imageLoader.displayImage(CommonConstant.img_course_category + category.getIcon(), holder.img, options);
		holder.text.setText(category.getName());
		
		return convertView;
	}

	private class ViewHolder{
		private ImageView img;
		private TextView text;
	}
	
}
