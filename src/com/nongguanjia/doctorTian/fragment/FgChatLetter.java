package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeUser;
import com.nongguanjia.doctorTian.ChatActivity;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.MessageListAdapter;
import com.nongguanjia.doctorTian.base.BaseFragment;

/**
 * @author tx
 * 私信
 */
public class FgChatLetter extends BaseFragment {
	private ListView listView;
	private MessageListAdapter adapter;
	
	private GotyeAPI api = GotyeAPI.getInstance();

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg_letter, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		GotyeAPI.getInstance().addListener(this);
		initView();
	}
	
	
	private void initView(){
		listView = (ListView)getView().findViewById(R.id.listview);
		
		int state=api.getOnLineState();
		if(state!=1){
			setErrorTip(0);
		}else{
			setErrorTip(1);
		}
		updateList();
		setListener();
	}
	
	
	@Override
	public void onDestroy() {
		GotyeAPI.getInstance().removeListener(this);
		super.onDestroy();

	}
	@Override
	public void onDownloadMedia(int code, String path, String url) {
		// TODO Auto-generated method stub
		if(getActivity().isTaskRoot()){
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onLogout(int code) {
		setErrorTip(0);
	}
	@Override
	public void onLogin(int code, GotyeUser currentLoginUser) {
		setErrorTip(1);
	}
	@Override
	public void onReconnecting(int code, GotyeUser currentLoginUser) {
		setErrorTip(-1);
	}
	
	
	private void updateList() {
		List<GotyeChatTarget> sessions = api.getSessionList();
		if (sessions == null) {
			sessions = new ArrayList<GotyeChatTarget>();
		}
		
		if (adapter == null) {
			adapter = new MessageListAdapter(getActivity(), sessions);
			listView.setAdapter(adapter);
		} else {
			adapter.setData(sessions);
		}
	}
	
	
	private void setErrorTip(int code){
//		code=api.getOnLineState();
		if(code==1){
			getView().findViewById(R.id.error_tip).setVisibility(View.GONE);
		}else{
			getView().findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if(code==-1){
				getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
				((TextView)getView().findViewById(R.id.showText)).setText("连接中...");
				getView().findViewById(R.id.error_tip_icon).setVisibility(View.GONE);
			}else{
				getView().findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView)getView().findViewById(R.id.showText)).setText("未连接");
				getView().findViewById(R.id.error_tip_icon).setVisibility(View.VISIBLE);
			}
		}
	}
	
	
	
	public void refresh() {
		updateList();
	}
	
	
	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GotyeChatTarget target = (GotyeChatTarget) adapter
						.getItem(arg2);
//				if (target.getName().equals(fixName)) {
//					Intent i = new Intent(getActivity(), NotifyListPage.class);
//					startActivity(i);
//				} else {
					GotyeAPI.getInstance().markMessagesAsRead(target);
					if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeUser) {
						Intent toChat = new Intent(getActivity(),
								ChatActivity.class);
						toChat.putExtra("user", (GotyeUser) target);
						startActivity(toChat);
						// updateList();
					} else if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeRoom) {
						Intent toChat = new Intent(getActivity(),
								ChatActivity.class);
						toChat.putExtra("room", (GotyeRoom) target);
						startActivity(toChat);

					} else if (target.getType() == GotyeChatTargetType.GotyeChatTargetTypeGroup) {
						Intent toChat = new Intent(getActivity(),
								ChatActivity.class);
						toChat.putExtra("group", (GotyeGroup) target);
						startActivity(toChat);
					}
					refresh();
				
//				}
			}
		});
	}
	
}
