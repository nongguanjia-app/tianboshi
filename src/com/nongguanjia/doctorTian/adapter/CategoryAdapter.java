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
import com.nongguanjia.doctorTian.bean.AllCategoryCourses;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private List<AllCategoryCourses> courses = new ArrayList<AllCategoryCourses>(0);
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private ViewHolder mHolder = null;
	
	public List<AllCategoryCourses> getCourses() {
		return courses;
	}

	public void setCourses(List<AllCategoryCourses> courses) {
		this.courses = courses;
	}
	
	public CategoryAdapter(Context context, ArrayList<AllCategoryCourses> courseList){
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(true);
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return courses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return courses.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllCategoryCourses course = courses.get(position);
		
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.category_item, null);
			mHolder.img = (ImageView)convertView.findViewById(R.id.img);
			mHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			mHolder.tv_teacher = (TextView)convertView.findViewById(R.id.tv_teacher);
			mHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.tv_title.setText(course.getTitle());
		mHolder.tv_teacher.setText(course.getTeacher());
		mHolder.tv_status.setText(course.getState());
		
		imageLoader.displayImage(CommonConstant.img_course_primary + course.getPicture(), mHolder.img, options);
		
		return convertView;
	}
	
	private class ViewHolder{
		ImageView img;
		TextView tv_title, tv_teacher, tv_status;
	}

}
