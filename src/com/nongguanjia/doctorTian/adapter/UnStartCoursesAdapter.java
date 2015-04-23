package com.nongguanjia.doctorTian.adapter;

import java.util.List;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllUnStartCourses;
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

public class UnStartCoursesAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<AllUnStartCourses> mList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ViewHolder mHolder = null;
	private LayoutInflater mInflater;
	
	public UnStartCoursesAdapter(Context mContext, List<AllUnStartCourses> mList) {
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
			convertView = mInflater.inflate(R.layout.unstart_item, null);
			mHolder.mUnSmallPicture = (ImageView) convertView.findViewById(R.id.unSmallPicture);
			mHolder.mUnTitle = (TextView) convertView.findViewById(R.id.unCourseTitle);
			mHolder.mUnId = (TextView) convertView.findViewById(R.id.unExpertId);
			mHolder.mUnTime = (TextView) convertView.findViewById(R.id.unStartTime);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mUnTitle.setText(mList.get(position).getCourseTitle());
		mHolder.mUnId.setText(mList.get(position).getExpertId());
		mHolder.mUnTime.setText(mList.get(position).getStartTime());
		//imageLoader.displayImage(CommonConstant.img_course_primary + mList.get(position).getLecturePhoto(), mHolder.mExpertPhoto, options);
		return convertView;
	}
	
	private class ViewHolder {
		ImageView mUnSmallPicture;
		TextView mUnTitle, mUnId, mUnTime;
	}
}
