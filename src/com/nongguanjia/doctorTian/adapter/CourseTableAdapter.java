package com.nongguanjia.doctorTian.adapter;

import java.util.List;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllChapters;
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

public class CourseTableAdapter extends BaseAdapter {
	private Context mContext;
	private List<AllChapters> mList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ViewHolder mHolder = null;
	private LayoutInflater mInflater;
	
	public CourseTableAdapter(Context mContext, List<AllChapters> mList) {
		super();
		mInflater = LayoutInflater.from(mContext);
//		options = Options.getOptions();
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
			convertView = mInflater.inflate(R.layout.table_item, null);
			mHolder.mName = (TextView) convertView.findViewById(R.id.table_chapter);
			mHolder.mMediaName = (TextView) convertView.findViewById(R.id.table_title);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mName.setText("第" + mList.get(position).getLectureOrder()+"节");
		mHolder.mMediaName.setText(mList.get(position).getName());
	//	imageLoader.displayImage(CommonConstant.img_course_primary +mList.get(position).getLargePicture(), mHolder.img, options);
		return convertView;
	}

	private class ViewHolder{
		ImageView img;
		TextView mName, mMediaName;
	}
}
