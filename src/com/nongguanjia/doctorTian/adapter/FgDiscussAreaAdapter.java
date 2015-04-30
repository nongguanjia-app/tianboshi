package com.nongguanjia.doctorTian.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gotye.api.GotyeUser;
import com.nongguanjia.doctorTian.ChatActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.bean.AllReviews;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.utils.Options;
import com.nongguanjia.doctorTian.view.ReplyDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FgDiscussAreaAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private List<AllReviews> reviews = new ArrayList<AllReviews>(0);
	private String id;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ReplyDialog dialog;

	private ViewHolder mHolder = null;
	
	public List<AllReviews> getReviews() {
		return reviews;
	}

	public void setReviews(List<AllReviews> reviews) {
		this.reviews = reviews;
	}
	

	public FgDiscussAreaAdapter(Context context, String id) {
		inflater = LayoutInflater.from(context);
		options = Options.getOptions(false);
		this.context = context;
		this.id = id;
		dialog = new ReplyDialog(context, R.style.ReplyDialog);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reviews.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return reviews.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AllReviews review = reviews.get(position);

		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.echo_item, null);
			mHolder.img = (ImageView) convertView.findViewById(R.id.img);
			mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_content);
			mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.btn_reply = (Button) convertView.findViewById(R.id.btn_reply);
			mHolder.btn_letter = (Button) convertView.findViewById(R.id.btn_letter);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		mHolder.tv_name.setText(review.getName());
		mHolder.tv_msg.setText(review.getMessage());
		mHolder.tv_time.setText(review.getDate());

		imageLoader.displayImage(CommonConstant.img_discuss + review.getPhoto(),mHolder.img, options);
		
		mHolder.btn_reply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.setIdAndEcho(id, review.getTalkId(), "0");
				dialog.show();
			}
		});
		
		
		mHolder.btn_letter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ChatActivity.class);
				GotyeUser user = new GotyeUser(review.getPhone());
				intent.putExtra("user", user);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView tv_name, tv_msg, tv_time;
		Button btn_reply, btn_letter;
	}

}
