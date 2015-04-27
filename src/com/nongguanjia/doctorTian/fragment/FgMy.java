package com.nongguanjia.doctorTian.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nongguanjia.doctorTian.AboutActivity;
import com.nongguanjia.doctorTian.HelpActivity;
import com.nongguanjia.doctorTian.MyDataActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.SystemActivity;
import com.nongguanjia.doctorTian.UpdatePassActivity;
import com.nongguanjia.doctorTian.UpgradeActivity;
import com.nongguanjia.doctorTian.task.UpdateTask;
import com.nongguanjia.doctorTian.utils.NetworkDetector;

/**
 * @author 我
 */
public class FgMy extends Fragment implements OnClickListener {
	private RelativeLayout mMy_info, mMy_Down, mRec, mEdit_psd, mHelp, mAbout,
			mUpgrade, mSystemUpgrade;
	private Button mExit;
	private TextView tv_title;

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
		mMy_info = (RelativeLayout) view.findViewById(R.id.my_info);
		mMy_Down = (RelativeLayout) view.findViewById(R.id.my_down);
		mRec = (RelativeLayout) view.findViewById(R.id.rec);
		mEdit_psd = (RelativeLayout) view.findViewById(R.id.edit_psd);
		mUpgrade = (RelativeLayout) view.findViewById(R.id.upgrade);
		mHelp = (RelativeLayout) view.findViewById(R.id.help);
		mAbout = (RelativeLayout) view.findViewById(R.id.about);
		mSystemUpgrade = (RelativeLayout) view.findViewById(R.id.upgrade_system);
		mExit = (Button) view.findViewById(R.id.exitlogin);
		mRec.setOnClickListener(this);
		mMy_info.setOnClickListener(this);
		mMy_Down.setOnClickListener(this);
		mEdit_psd.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		mUpgrade.setOnClickListener(this);
		mSystemUpgrade.setOnClickListener(this);
		mExit.setOnClickListener(this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		//tv_title.setText("我");

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_info:
			Intent intent_Info = new Intent(getActivity(), MyDataActivity.class);
			startActivity(intent_Info);
			break;

		case R.id.my_down:
			Toast.makeText(getActivity(), "该功能正在完善中...", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rec:
			Toast.makeText(getActivity(), "该功能正在完善中...", Toast.LENGTH_SHORT).show();
			break;
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
		case R.id.upgrade:
			Intent intent_upgrade = new Intent(getActivity(),UpgradeActivity.class);
			startActivity(intent_upgrade);
			break;
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

}
