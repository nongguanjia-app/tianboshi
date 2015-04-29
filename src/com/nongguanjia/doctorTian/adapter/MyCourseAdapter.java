package com.nongguanjia.doctorTian.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.CourseMy;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyCourseAdapter extends BaseAdapter {
	private List<CourseMy> mList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ViewHolder mHolder = null;
	private LayoutInflater mInflater;
	
	public MyCourseAdapter(Context mContext, List<CourseMy> mList) {
		super();
		mInflater = LayoutInflater.from(mContext);
		options = Options.getOptions();
		this.mList = mList;
	}
	
	public void refreshData(List<CourseMy> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.my_course_item, null);
			mHolder.imageView = (ImageView) convertView.findViewById(R.id.myCourseItem_img);
			mHolder.title = (TextView) convertView.findViewById(R.id.myCourseItem_title);
			mHolder.content = (TextView) convertView.findViewById(R.id.myCourseItem_content);
			mHolder.date = (TextView) convertView.findViewById(R.id.myCourseItem_time);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.title.setText(mList.get(position).getCourseTitle());
		mHolder.content.setText(mList.get(position).getTitle());
		mHolder.date.setText("已更新  "+mList.get(position).getCreatedTime());
		imageLoader.displayImage(CommonConstant.img_course_primary + mList.get(position).getCourseAvatar(), mHolder.imageView, options);
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageView;
		TextView title, content, date;
	}

}
