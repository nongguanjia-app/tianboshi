package com.nongguanjia.doctorTian.adapter;

import java.util.List;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllStatement;
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

public class UpgradeAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater inflater = null;
	private List<AllStatement> mList;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private LayoutInflater mInflater;
	private ViewHolder mHolder = null;

	public UpgradeAdapter(Context mContext, List<AllStatement> mList) {
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
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.upgrade_item, null);
			mHolder.mUpgradeStroy = (TextView) convertView.findViewById(R.id.upgrade_story);
//			mHolder.mUpgradeHappy = (TextView) convertView.findViewById(R.id.upgrade_happy);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.mUpgradeStroy.setText(mList.get(position).getContent());
		//mHolder.mUpgradeHappy.setText(mList.get(position).getContent());
		// imageLoader.displayImage(CommonConstant.img_course_primary
		// +mList.get(position).getLargePicture(), mHolder.img, options);
		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView mUpgradeStroy,mUpgradeHappy ;
	}
}
