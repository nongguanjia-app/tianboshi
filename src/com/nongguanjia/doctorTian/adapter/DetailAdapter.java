package com.nongguanjia.doctorTian.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.Courses;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//课程详情
public class DetailAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private List<String> group;
	private Courses mCourses;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private ViewHolder holder;
	
	
	public DetailAdapter(Context context, List<String> group, Courses mCourses){
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.group = group;
		this.mCourses = mCourses;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groupPosition==0 ? mCourses.getAllLecture().get(childPosition) : mCourses.getAllCase().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			if(groupPosition == 0){
				convertView = mInflater.inflate(R.layout.detail_child, null);
				holder.img = (ImageView)convertView.findViewById(R.id.lec_icon);
				holder.name = (TextView)convertView.findViewById(R.id.tv_name);
				holder.unit = (TextView)convertView.findViewById(R.id.tv_unit);
			}else{
				convertView = mInflater.inflate(R.layout.detail_child_case, null);
				holder.tvCase = (TextView)convertView.findViewById(R.id.tv_case);
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(groupPosition == 0){
			holder.name.setText(mCourses.getAllLecture().get(childPosition).getName());
			holder.unit.setText("单位：" + mCourses.getAllLecture().get(childPosition).getAddress());
			
			imageLoader.displayImage(CommonConstant.img_detail+mCourses.getAllLecture().get(childPosition).getPhoto(), holder.img);
		}else{
			holder.tvCase.setText(mCourses.getAllCase().get(childPosition).getTitle());
		}
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition==0 ? mCourses.getAllLecture().size() : mCourses.getAllCase().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.detail_group, null);
			holder.groupName = (TextView)convertView.findViewById(R.id.tv_name);
			holder.groupIntro = (TextView)convertView.findViewById(R.id.tv_intro);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.groupName.setText(group.get(groupPosition));
		if(groupPosition == 0){
			holder.groupIntro.setVisibility(View.VISIBLE);
			holder.groupIntro.setText(mCourses.getCourseIntro());
		}else{
			holder.groupIntro.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private class ViewHolder{
		//group
		private TextView groupName, groupIntro; 
		//child
		private ImageView img;
		private TextView name, unit;
		
		private TextView tvCase;
	}

}
