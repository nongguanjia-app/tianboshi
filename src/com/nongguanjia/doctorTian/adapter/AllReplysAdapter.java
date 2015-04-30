package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllReply;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AllReplysAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private List<AllReply> replys = new ArrayList<AllReply>(0);
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private ViewHolder mHolder = null;
	
	public List<AllReply> getReplys() {
		return replys;
	}

	public void setReplys(List<AllReply> replys) {
		this.replys = replys;
	}
	
	
	public AllReplysAdapter(Context context){
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(false);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return replys.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return replys.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllReply reply = replys.get(position);
		
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.allreply_item, null);
			mHolder.img = (ImageView)convertView.findViewById(R.id.img);
			mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			mHolder.tv_msg = (TextView)convertView.findViewById(R.id.tv_msg);
			mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.tv_name.setText(reply.getTitle());
		mHolder.tv_msg.setText(reply.getMessage());
		mHolder.tv_time.setText(reply.getReplyDate());
		
		return convertView;
	}
	
	private class ViewHolder{
		ImageView img;
		TextView tv_name, tv_msg, tv_time;
	}

}
