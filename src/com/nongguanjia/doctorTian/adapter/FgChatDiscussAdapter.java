package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllTalks;
import com.nongguanjia.doctorTian.utils.CommonConstant;
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

public class FgChatDiscussAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<AllTalks> talks;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private ViewHolder mHolder = null;

	public FgChatDiscussAdapter(Context context, ArrayList<AllTalks> talks) {
		inflater = LayoutInflater.from(context);
		options = Options.getOptions();
		this.context = context;
		this.talks = talks;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return talks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return talks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllTalks talk = talks.get(position);
		
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.chat_discuss_item, null);
			mHolder.img = (ImageView) convertView.findViewById(R.id.img);
			mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
			mHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.tv_title.setText(talk.getCourseTitle());
		mHolder.tv_name.setText(talk.getName());
		mHolder.tv_reply.setText(talk.getContent());
		mHolder.tv_count.setText(talk.getNum());

		imageLoader.displayImage(CommonConstant.img_discuss + talk.getCourseImage(),mHolder.img, options);

		return convertView;
	}
	
	private class ViewHolder {
		ImageView img;
		TextView tv_title, tv_name, tv_reply, tv_count;
	}

}
