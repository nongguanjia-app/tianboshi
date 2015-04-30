package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.Lecture;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Lecture> mAllCoursesList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ViewHolder mHolder = null;
	private LayoutInflater mInflater;
	
	public CourseAdapter(Context mContext, ArrayList<Lecture> mAllCoursesList) {
		super();
		mInflater = LayoutInflater.from(mContext);
		options = Options.getOptions(true);
		this.mContext = mContext;
		this.mAllCoursesList = mAllCoursesList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAllCoursesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mAllCoursesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Lecture lecture = mAllCoursesList.get(position);
		if (convertView==null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.alllecture_item, null);
			mHolder.expert_img = (ImageView) convertView.findViewById(R.id.expert_img);
			mHolder.name = (TextView) convertView.findViewById(R.id.name);
			mHolder.address = (TextView) convertView.findViewById(R.id.address);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		Log.v("zhao", "&&&" + mAllCoursesList.toString());
		mHolder.name.setText(mAllCoursesList.get(position).getName());
		mHolder.address.setText(mAllCoursesList.get(position).getAddress());
		imageLoader.displayImage(CommonConstant.img_detail + lecture.getPhoto(), mHolder.expert_img, options);
		return convertView;
	}
	
	private class ViewHolder{
		ImageView expert_img;
		TextView name;
		TextView address;
	}
}
