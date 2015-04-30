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
import com.nongguanjia.doctorTian.bean.AllExperiences;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FgCourseExpAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private List<AllExperiences> experiences = new ArrayList<AllExperiences>(0);
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private ViewHolder mHolder = null;
	
	public List<AllExperiences> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<AllExperiences> experiences) {
		this.experiences = experiences;
	}
	
	public FgCourseExpAdapter(Context context){
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(false);
		this.context = context;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return experiences.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return experiences.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllExperiences exp = experiences.get(position);
		
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fg_course_exp_item, null);
			mHolder.img = (ImageView)convertView.findViewById(R.id.img);
			mHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			mHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.tv_title.setText(exp.getMessage());
		mHolder.tv_time.setText(exp.getDate());
		
		imageLoader.displayImage(CommonConstant.img_exp + exp.getPhoto(), mHolder.img, options);
		
		return convertView;
	}
	
	private class ViewHolder{
		ImageView img;
		TextView tv_title, tv_time;
	}

}
