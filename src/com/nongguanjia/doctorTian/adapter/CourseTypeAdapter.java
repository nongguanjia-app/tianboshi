package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.CategoryActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllCategorys;
import com.nongguanjia.doctorTian.view.MyGridView;

public class CourseTypeAdapter extends BaseExpandableListAdapter {
	private LayoutInflater mInflater;
	private Context context;
	private List<String> group;
	private List<HashMap<String, ArrayList<AllCategorys>>> child;
	private GridViewAdapter gridAdapter;
	
	private ViewHolder holder;
	
	public CourseTypeAdapter(Context context, List<String> names, List<HashMap<String, ArrayList<AllCategorys>>> list){
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.group = names;
		this.child = list;
	}
	

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).get(childPosition);
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
			convertView = mInflater.inflate(R.layout.course_type_child, null);
			holder.gridView = (MyGridView)convertView.findViewById(R.id.gridview);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		//添加内部子类
		final ArrayList<AllCategorys> cates = child.get(groupPosition).get(group.get(groupPosition));
		
		gridAdapter = new GridViewAdapter(context, cates);
		holder.gridView.setAdapter(gridAdapter);
		
		holder.gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, CategoryActivity.class);
				Bundle bd = new Bundle();
				bd.putString("Id", cates.get(position).getId());
				bd.putString("name", cates.get(position).getName());
				intent.putExtras(bd);
				context.startActivity(intent);
			}
			
		});
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).size();
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
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.course_type_group, null);
			holder.groupName = (TextView)convertView.findViewById(R.id.tv_type);
			holder.groupCount = (TextView)convertView.findViewById(R.id.tv_count);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.groupName.setText(group.get(groupPosition));
//		holder.groupCount.setText(child.get(groupPosition).get(group.get(groupPosition)).size()+"");
		
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
		private TextView groupName;
		private TextView groupCount;
		
		private MyGridView gridView;
	}

}
