package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllAttention;
import com.nongguanjia.doctorTian.utils.PingYinUtil;

/**
 * @author tx
 * 我的客户列表
 */
public class CustomerAdapter extends BaseAdapter implements SectionIndexer{

	private Context mContext;
	private SectionIndexer mIndexer;
	private List<AllAttention> list = new ArrayList<AllAttention>(0);
	
	
	public void updateData(List<AllAttention> data){
		this.list = data;
		this.notifyDataSetChanged();
	}
	
	
	public CustomerAdapter(Context mContext) {
		this.mContext = mContext;

	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.customer_item, null);
			viewHolder.letter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.from = (TextView) view.findViewById(R.id.from);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final AllAttention att = list.get(position);
		
		//隐藏首字母分组
		viewHolder.letter.setVisibility(View.GONE);
		
//		String catalog;
//		
//		if(TextUtils.isEmpty(att.getNoteName())){
//			catalog = PingYinUtil.converterToFirstSpell(att.getTelephone()).substring(0, 1);
//		}else{
//			catalog = PingYinUtil.converterToFirstSpell(att.getNoteName()).substring(0, 1);
//		}
//		
//		if (position == 0) {
//			viewHolder.letter.setVisibility(View.VISIBLE);
//			viewHolder.letter.setText(catalog);
//		} else {
//			String lastCatalog = "";
//			if(TextUtils.isEmpty(list.get(position - 1).getNoteName())){
//				lastCatalog = PingYinUtil.converterToFirstSpell(list.get(position - 1).getTelephone()).substring(0, 1);
//			}else{
//				lastCatalog = PingYinUtil.converterToFirstSpell(list.get(position - 1).getNoteName()).substring(0, 1);
//			}
//			
//			if (catalog.equals(lastCatalog)) {
//				viewHolder.letter.setVisibility(View.GONE);
//			} else {
//				viewHolder.letter.setVisibility(View.VISIBLE);
//				viewHolder.letter.setText(catalog);
//			}
//		}
		
		if(!TextUtils.isEmpty(att.getNoteName())){
			viewHolder.name.setText(att.getNoteName());
		}else{
			viewHolder.name.setText(att.getTelephone());
		}
		
		//来源 (0二维码，1关注，2好友推荐，3通讯录)
		if(att.getSource().equals("0")){
			viewHolder.from.setText("二维码");
		}else if(att.getSource().equals("1")){
			viewHolder.from.setText("关注");
		}else if(att.getSource().equals("2")){
			viewHolder.from.setText("好友推荐");
		}else if(att.getSource().equals("3")){
			viewHolder.from.setText("通讯录");
		}
		
		return view;

	}
	


	final static class ViewHolder {
		TextView letter;
		TextView name;
		TextView from;
	}


	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {
		
		return 0;
	}

	public int getPositionForSection(int section) {
		AllAttention mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = list.get(i);
				l = PingYinUtil.converterToFirstSpell(mContent.getNoteName()).substring(0, 1);
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}

			}
			
		}
		mContent = null;
		l = null;
		return -1;
		
		
	}
}