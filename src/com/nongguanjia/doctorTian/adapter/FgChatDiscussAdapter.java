package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllTalks;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FgChatDiscussAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private List<AllTalks> talks = new ArrayList<AllTalks>(0);
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	private ViewHolder mHolder = null;
	
	public List<AllTalks> getTalks() {
		return talks;
	}

	public void setTalks(List<AllTalks> talks) {
		this.talks = talks;
	}

	public FgChatDiscussAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(true);
		this.context = context;
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
			mHolder.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.tv_title.setText(talk.getCourseTitle());
		if(!TextUtils.isEmpty(talk.getName())){
			mHolder.tv_reply.setText("最新评论: 回复" + talk.getName() + ": " + talk.getContent());
		}else{
			mHolder.tv_reply.setText("最新评论: " + talk.getContent());
		}
		

		imageLoader.displayImage(CommonConstant.img_course_primary + talk.getCourseImage(),mHolder.img, options);

		return convertView;
	}
	
	private class ViewHolder {
		ImageView img;
		TextView tv_title, tv_reply;
	}

}
