package com.nongguanjia.doctorTian.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nongguanjia.doctorTian.AboutActivity;
import com.nongguanjia.doctorTian.HelpActivity;
import com.nongguanjia.doctorTian.MyDataActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.UpdatePassActivity;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.UserInfo;
import com.nongguanjia.doctorTian.task.UpdateTask;
import com.nongguanjia.doctorTian.utils.NetworkDetector;

/**
 * @author 我
 */
public class FgMy extends Fragment implements OnClickListener {
	private RelativeLayout mMy_info, mEdit_psd, mHelp, mAbout, mSystemUpgrade; //mRec mMy_Down mUpgrade
	private Button mExit;
	private TextView tv_title;
	private ImageView img_back,mHead_img;
	private TextView tvPhone,tvName;
	private UserInfo info;
	private PhotoBroadcastReceiver receiver;
	private NameBroadcastReceiver receiverName;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my, container, false);
		tvName = (TextView) view.findViewById(R.id.name_my);
		tvPhone = (TextView) view.findViewById(R.id.phone_my);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		mHead_img = (ImageView) view.findViewById(R.id.my_icon);
		tv_title.setText("我");
		img_back = (ImageView)view.findViewById(R.id.img_back);
		img_back.setVisibility(View.GONE);
		info= ((AppApplication)getActivity().getApplication()).info;
		if(info!=null){
			tvName.setText(info.getName());
			tvPhone.setText(info.getTelephone());
		}
		
		mMy_info = (RelativeLayout) view.findViewById(R.id.my_info);
//		mMy_Down = (RelativeLayout) view.findViewById(R.id.my_down);
//		mRec = (RelativeLayout) view.findViewById(R.id.rec);
		mEdit_psd = (RelativeLayout) view.findViewById(R.id.edit_psd);
//		mUpgrade = (RelativeLayout) view.findViewById(R.id.upgrade);
		mHelp = (RelativeLayout) view.findViewById(R.id.help);
		mAbout = (RelativeLayout) view.findViewById(R.id.about);
		mSystemUpgrade = (RelativeLayout) view.findViewById(R.id.upgrade_system);
		mExit = (Button) view.findViewById(R.id.exitlogin);
//		mRec.setOnClickListener(this);
		mMy_info.setOnClickListener(this);
//		mMy_Down.setOnClickListener(this);
		mEdit_psd.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mAbout.setOnClickListener(this);
//		mUpgrade.setOnClickListener(this);
		mSystemUpgrade.setOnClickListener(this);
		mExit.setOnClickListener(this);
		
		SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
		String path = sp.getString("path", "");
		if(!TextUtils.isEmpty(path)){
			mHead_img.setImageBitmap(BitmapFactory.decodeFile(path));
		}
		
		receiver = new PhotoBroadcastReceiver();
		getActivity().registerReceiver(receiver, new IntentFilter("com.nongguanjia.doctorTian.photo"));
		
		receiverName = new NameBroadcastReceiver();
		getActivity().registerReceiver(receiverName, new IntentFilter("com.nongguanjia.doctorTian.name"));

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_info:
			Intent intent_Info = new Intent(getActivity(), MyDataActivity.class);
			startActivity(intent_Info);
			break;

//		case R.id.my_down:
//			Toast.makeText(getActivity(), "该功能正在完善中...", Toast.LENGTH_SHORT).show();
//			break;
//		case R.id.rec:
//			Toast.makeText(getActivity(), "该功能正在完善中...", Toast.LENGTH_SHORT).show();
//			break;
		case R.id.edit_psd:
			Intent intent_Update_Psd = new Intent(getActivity(),UpdatePassActivity.class);
			startActivity(intent_Update_Psd);
			break;
		case R.id.help:
			Intent intent_help = new Intent(getActivity(), HelpActivity.class);
			startActivity(intent_help);
			break;
		case R.id.about:
			Intent intent_about = new Intent(getActivity(), AboutActivity.class);
			startActivity(intent_about);
			break;
//		case R.id.upgrade:
//			Intent intent_upgrade = new Intent(getActivity(),UpgradeActivity.class);
//			startActivity(intent_upgrade);
//			break;
		case R.id.upgrade_system: //系统升级
//			Intent intent_system = new Intent(getActivity(),SystemActivity.class);
//			startActivity(intent_system);
			
			if(NetworkDetector.detect(getActivity())){
				UpdateTask task = new UpdateTask(getActivity());
				task.getVersion();	
			}else{
				NetworkDetector.dialog(getActivity());
			}
			
			break;
		case R.id.exitlogin:
			new AlertDialog.Builder(getActivity())
					.setTitle("提示")
					.setMessage("确定退出吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							}).show();
			break;

		default:
			break;
		}
	}
	
	private class PhotoBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String path = arg1.getStringExtra("path");
			if(!TextUtils.isEmpty(path)){
				mHead_img.setImageBitmap(BitmapFactory.decodeFile(path));
			}
		}
		
	}
	
	private class NameBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			tvName.setText(arg1.getStringExtra("name"));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
		getActivity().unregisterReceiver(receiverName);
	}
}
