package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.ContractInfo;
import com.nongguanjia.doctorTian.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author tx
 * 联系人列表
 */
public class ContactAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private List<ContractInfo> contracts;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private ViewHolder mHolder = null;
	
	private List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	
	
	public List<HashMap<String, String>> getList() {
		return list;
	}

	public ContactAdapter(Context context, List<ContractInfo> contracts){
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(false);
		this.context = context;
		this.contracts = contracts;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contracts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contracts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ContractInfo info = contracts.get(position);
		
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.contract_item, null);
			mHolder.img = (ImageView)convertView.findViewById(R.id.img);
			mHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			mHolder.cb_choose = (CheckBox)convertView.findViewById(R.id.cb_choose);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.tv_name.setText(info.getName());
		
		mHolder.cb_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton btnView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put("name", info.getName());
					map.put("tel", info.getPhoneNum());
					
					list.add(map);
				}
			}
		});
		
//		imageLoader.displayImage(info.getPhotoId(), mHolder.img, options);
		
		return convertView;
	}
	
	
	private class ViewHolder{
		ImageView img;
		TextView tv_name;
		CheckBox cb_choose;
	}
	
}
