package com.nongguanjia.doctorTian.adapter;

import java.util.List;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllStartCourse;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StartCoursesAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<AllStartCourse> mList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ViewHolder mHolder = null;
	private LayoutInflater mInflater;
	
	public StartCoursesAdapter(Context mContext, List<AllStartCourse> mList) {
		super();
		mInflater = LayoutInflater.from(mContext);
		options = Options.getOptions();
		this.mContext = mContext;
		this.mList = mList;
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
			convertView = mInflater.inflate(R.layout.start_item, null);
			mHolder.mSmallPicture = (ImageView) convertView.findViewById(R.id.smallPicture);
			mHolder.mTitle = (TextView) convertView.findViewById(R.id.courseTitle);
			mHolder.mId = (TextView) convertView.findViewById(R.id.expertId);
			mHolder.mProgress = (TextView) convertView.findViewById(R.id.progress);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mTitle.setText(mList.get(position).getCourseTitle());
		mHolder.mId.setText(mList.get(position).getCourseId());
		mHolder.mProgress.setText("已更新  "+mList.get(position).getProgress());
		//imageLoader.displayImage(CommonConstant.img_course_primary + mList.get(position).getLecturePhoto(), mHolder.mExpertPhoto, options);
		return convertView;
	}
	
	private class ViewHolder {
		ImageView mSmallPicture;
		TextView mTitle, mId, mProgress;
	}
}
