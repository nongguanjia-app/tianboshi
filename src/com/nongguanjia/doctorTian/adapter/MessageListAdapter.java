package com.nongguanjia.doctorTian.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeUser;
import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.utils.BitmapUtil;
import com.nongguanjia.doctorTian.utils.ImageCache;
import com.nongguanjia.doctorTian.utils.TimeUtil;

public class MessageListAdapter extends BaseAdapter {
	private Context context;
	private List<GotyeChatTarget> sessions;
	private GotyeAPI api;

	public MessageListAdapter(Context context,
			List<GotyeChatTarget> sessions) {
		this.context = context;
		this.sessions = sessions;
		api = GotyeAPI.getInstance();
	}

	static class ViewHolder {
		ImageView icon;
		TextView title, content, time, count;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sessions.size();
	}

	@Override
	public GotyeChatTarget getItem(int arg0) {
		// TODO Auto-generated method stub
		return sessions.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 1;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "InflateParams" })
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_delete, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
			viewHolder.title = (TextView) view.findViewById(R.id.title_tx);
			viewHolder.content = (TextView) view.findViewById(R.id.content_tx);
			viewHolder.time = (TextView) view.findViewById(R.id.time_tx);
			viewHolder.count = (TextView) view.findViewById(R.id.count);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final GotyeChatTarget session =  getItem(arg0);
		Log.d("offLine", "session" + session);
		

		String title = "", content = "";
		viewHolder.content.setVisibility(View.VISIBLE);
		//获取该session最后一条消息记录
		GotyeMessage lastMsg = api.getLastMessage(session);
		//time请*1000还原成正常时间
		String lastMsgTime = TimeUtil
				.dateToMessageTime(lastMsg.getDate() * 1000);
		viewHolder.time.setText(lastMsgTime);
		
		if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeText) {
			content = "文本消息：" + lastMsg.getText();
		} else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeImage) {
			content = "图片消息";
		} else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
			content = "语音消息";
		} else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeUserData) {
			content = "自定义消息";
		} else if (lastMsg.getType() == GotyeMessageType.GotyeMessageTypeInviteGroup) {
			content = "邀请消息";
		}

		if (session.getType() == GotyeChatTargetType.GotyeChatTargetTypeUser) {
			setIcon(viewHolder.icon, session.getName());
			GotyeUser user = api.requestUserInfo(session.getName(), false);
			if (user != null) {
				if (TextUtils.isEmpty(user.getNickname())) {
					title = "好友：" + user.getName();
				} else {
					title = "好友：" + user.getNickname();
				}
			} else {
				title = "好友：" + session.getName();
			}
		} else if (session.getType() == GotyeChatTargetType.GotyeChatTargetTypeRoom) {
			setIcon(viewHolder.icon, String.valueOf(session.getId()));
			GotyeRoom room = api.requestRoomInfo(session.getId(), false);
			if (room != null) {
				if (TextUtils.isEmpty(room.getRoomName())) {
					title = "聊天室：" + room.getId();
				} else {
					title = "聊天室：" + room.getRoomName();
				}
			} else {
				title = "聊天室：" + session.getId();
			}

		} else if (session.getType() == GotyeChatTargetType.GotyeChatTargetTypeGroup) {
			GotyeGroup group = api.requestGroupInfo(session.getId(), false);
			setIcon(viewHolder.icon, String.valueOf(session.getId()));
			if (group != null) {
				if (TextUtils.isEmpty(group.getGroupName())) {
					title = "群：" + group.getId();
				} else {
					title = "群：" + group.getGroupName();
				}
			} else {
				title = "群：" + session.getId();
			}

		}
		viewHolder.title.setText(title);
		viewHolder.content.setText(content);
		int count = api.getUnreadMsgcounts(session);
		if (count > 0) {
			viewHolder.count.setVisibility(View.VISIBLE);
			viewHolder.count.setText(String.valueOf(count));
		} else {
			viewHolder.count.setVisibility(View.GONE);
		}
	
		
		return view;
	}

	private void setIcon(ImageView iconView, String name) {
			Bitmap bmp = ImageCache.getInstance().get(name);
			if (bmp != null) {
				iconView.setImageBitmap(bmp);
			} else {
				GotyeUser user = api.requestUserInfo(name, false);
				if (user != null && user.getIcon() != null) {
					bmp = ImageCache.getInstance().get(user.getIcon().path);
					if (bmp != null) {
						iconView.setImageBitmap(bmp);
						ImageCache.getInstance().put(name, bmp);
					} else {
						bmp = BitmapUtil.getBitmap(user.getIcon().getPath());
						if (bmp != null) {
							iconView.setImageBitmap(bmp);
							ImageCache.getInstance().put(name, bmp);
						} else {
							iconView.setImageResource(R.drawable.default_person_img);
						}
					}
				} else {
					iconView.setImageResource(R.drawable.default_person_img);
				}
			}

	}

	public void setData(List<GotyeChatTarget> sessions) {
		// TODO Auto-generated method stub
		this.sessions = sessions;
		notifyDataSetChanged();
	}
}
