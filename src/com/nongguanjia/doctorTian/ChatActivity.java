package com.nongguanjia.doctorTian;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeMessageType;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.PathUtil;
import com.gotye.api.WhineMode;
import com.nongguanjia.doctorTian.adapter.ChatMessageAdapter;
import com.nongguanjia.doctorTian.base.BaseActivity;
import com.nongguanjia.doctorTian.utils.CommonUtils;
import com.nongguanjia.doctorTian.utils.Constants;
import com.nongguanjia.doctorTian.utils.GotyeVoicePlayClickPlayListener;
import com.nongguanjia.doctorTian.utils.ProgressDialogUtil;
import com.nongguanjia.doctorTian.utils.SendImageMessageTask;
import com.nongguanjia.doctorTian.utils.ToastUtil;
import com.nongguanjia.doctorTian.utils.URIUtil;
import com.nongguanjia.doctorTian.utils.VoiceToTextUtil;
import com.nongguanjia.doctorTian.view.RTPullListView;
import com.nongguanjia.doctorTian.view.RTPullListView.OnRefreshListener;

public class ChatActivity extends BaseActivity implements OnClickListener{

	public static final int REALTIMEFROM_OTHER = 2;
	public static final int REALTIMEFROM_SELF = 1;
	public static final int REALTIMEFROM_NO = 0;
	private static final int REQUEST_PIC = 1;
	private static final int REQUEST_CAMERA = 2;

	public static final int VOICE_MAX_TIME = 60 * 1000;
	private RTPullListView pullListView;
	public ChatMessageAdapter adapter;
	private GotyeUser o_user,user;
	private GotyeRoom o_room,room;
	private GotyeGroup o_group,group;
	private GotyeUser currentLoginUser;
	private ImageView voice_text_chage;
	private Button pressToVoice;
	private EditText textMessage;
	private ImageView showMoreType;
	private LinearLayout moreTypeLayout;

	//private PopupWindow menuWindow;
	//private AnimationDrawable anim;
	public int chatType = 0;
       
	private View realTalkView;
	private TextView realTalkName, stopRealTalk;
	private AnimationDrawable realTimeAnim;
	private boolean moreTypeForSend = true;

	public int onRealTimeTalkFrom = -1; // -1默认状态 ,0表示我在说话,1表示别人在实时语音

	private File cameraFile;
	public static final int IMAGE_MAX_SIZE_LIMIT = 1024 * 1024;
	public static final int Voice_MAX_TIME_LIMIT = 60 * 1000;
	private long playingId;
	private TextView title;
	private ImageView img_back;
	private VoiceRecognitionClient mASREngine;
	private PopupWindow mPopupWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_activity);
		
		mASREngine = VoiceRecognitionClient.getInstance(ChatActivity.this);
		mASREngine.setTokenApis(Constants.API_KEY, Constants.SECRET_KEY);
		currentLoginUser = api.getCurrentLoginUser();
		api.addListener(this);
		o_user=user = (GotyeUser) getIntent().getSerializableExtra("user");
		o_room=room = (GotyeRoom) getIntent().getSerializableExtra("room");
		o_group=group = (GotyeGroup) getIntent().getSerializableExtra("group");
		initView();
		if (chatType == 0) {
			api.activeSession(user);
			loadData();
		} else if (chatType == 1) {
			int code = api.enterRoom(room);
			if (code == GotyeStatusCode.CODE_OK) {
				api.activeSession(room);
				loadData();
				api.getLocalMessages(room, true);
				GotyeRoom temp = api.requestRoomInfo(room.getRoomID(), true);
				if (temp != null && !TextUtils.isEmpty(temp.getRoomName())) {
					title.setText("聊天室：" + temp.getRoomName());
				}
			} else {
				ProgressDialogUtil.showProgress(this, "正在进入房间...");
			}
		} else if (chatType == 2) {
			api.activeSession(group);
			loadData();
		}
		SharedPreferences spf=getSharedPreferences("fifter_cfg", Context.MODE_PRIVATE);
		boolean fifter=spf.getBoolean("fifter", false);
		api.enableTextFilter(GotyeChatTargetType.values()[chatType], fifter);
		
		int state=api.getOnLineState();
		if(state!=1){
			setErrorTip(0);
		}else{
			setErrorTip(1);
		}
	}

	private void initView() {
		pullListView = (RTPullListView) findViewById(R.id.gotye_msg_listview);
		img_back = (ImageView)findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		title = ((TextView) findViewById(R.id.tv_title));
		realTalkView = findViewById(R.id.real_time_talk_layout);
		realTalkName = (TextView) realTalkView
				.findViewById(R.id.real_talk_name);
		Drawable[] anim = realTalkName.getCompoundDrawables();
		realTimeAnim = (AnimationDrawable) anim[2];
		stopRealTalk = (TextView) realTalkView
				.findViewById(R.id.stop_real_talk);
		stopRealTalk.setOnClickListener(this);

		if (user != null) {
			chatType = 0;
			if(!TextUtils.isEmpty(user.getNickname())){
				title.setText("和 " + user.getNickname() + " 聊天");
			}else{
				title.setText("和 " + user.getName() + " 聊天");
			}
		} else if (room != null) {
			chatType = 1;
			title.setText("聊天室：" + room.getRoomID());
		} else if (group != null) {
			chatType = 2;
			String titleText = null;
			if (!TextUtils.isEmpty(group.getGroupName())) {
				titleText = group.getGroupName();
			} else {
				GotyeGroup temp = api
						.requestGroupInfo(group.getGroupID(), true);
				if (temp != null && !TextUtils.isEmpty(temp.getGroupName())) {
					titleText = temp.getGroupName();
				} else {
					titleText = String.valueOf(group.getGroupID());
				}
			}
			title.setText("群：" + titleText);
		}

		voice_text_chage = (ImageView) findViewById(R.id.send_voice);
		pressToVoice = (Button) findViewById(R.id.press_to_voice_chat);
		textMessage = (EditText) findViewById(R.id.text_msg_input);
		showMoreType = (ImageView) findViewById(R.id.more_type);
		moreTypeLayout = (LinearLayout) findViewById(R.id.more_type_layout);

		moreTypeLayout.findViewById(R.id.to_gallery).setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.to_camera).setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.voice_to_text)
				.setOnClickListener(this);
		moreTypeLayout.findViewById(R.id.real_time_voice_chat)
				.setOnClickListener(this);

		voice_text_chage.setOnClickListener(this);
		showMoreType.setOnClickListener(this);
		textMessage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				String text = arg0.getText().toString();
				// GotyeMessage message =new GotyeMessage();
				// GotyeChatManager.getInstance().sendMessage(message);
				sendTextMessage(text);
				textMessage.setText("");
				return true;
			}
		});
		pressToVoice.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					showPopupWindow2(pressToVoice);
					if (onRealTimeTalkFrom == 0) {
						ToastUtil.show(ChatActivity.this, "正在实时通话中...");
						return false;
					}

					if (GotyeVoicePlayClickPlayListener.isPlaying) {
						GotyeVoicePlayClickPlayListener.currentPlayListener
								.stopPlayVoice(true);
					}
                   int code = 0;
					if (chatType == 0) {
						code=api.startTalk(user, WhineMode.DEFAULT, 60 * 1000);
					} else if (chatType == 1) {
						code=api.startTalk(room, WhineMode.DEFAULT, false, 60 * 1000);
					} else if (chatType == 2) {
						code=api.startTalk(group, WhineMode.DEFAULT,60 * 1000);
					}
					int c=code;
					pressToVoice.setText("松开 发送");
					break;
				case MotionEvent.ACTION_UP:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					Log.d("chat_page",
							"onTouch action=ACTION_UP" + event.getAction());
					// if (onRealTimeTalkFrom > 0) {
					// return false;
					// }
					api.stopTalk();
					Log.d("chat_page",
							"after stopTalk action=" + event.getAction());
					pressToVoice.setText("按住 说话");
					mPopupWindow.dismiss();
					break;
				case MotionEvent.ACTION_CANCEL:
					if (onRealTimeTalkFrom == 0) {
						return false;
					}
					Log.d("chat_page",
							"onTouch action=ACTION_CANCEL" + event.getAction());
					// if (onRealTimeTalkFrom > 0) {
					// return false;
					// }
					api.stopTalk();
					pressToVoice.setText("按住 说话");
					mPopupWindow.dismiss();
					break;
				default:
					Log.d("chat_page",
							"onTouch action=default" + event.getAction());
					break;
				}
				return false;
			}
		});
		adapter = new ChatMessageAdapter(this, new ArrayList<GotyeMessage>());
		pullListView.setAdapter(adapter);
		pullListView.setSelection(adapter.getCount());
		setListViewInfo();
	}

	private void sendTextMessage(String text) {
		if (!TextUtils.isEmpty(text)) {
			GotyeMessage toSend;
			if (chatType == 0) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser, o_user,
						text);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createTextMessage(currentLoginUser, o_room,
						text);
			} else {
				toSend = GotyeMessage.createTextMessage(currentLoginUser,
						o_group, text);
			}
			String extraStr = null;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}
			
			//putExtre(toSend);
			int code = api.sendMessage(toSend);
			Log.d("", String.valueOf(code));
			adapter.addMsgToBottom(toSend);
			refreshToTail();
		}
	}
 //从assets中读取文件写入到额外数据中
	private void putExtre(GotyeMessage msg){
		try {
			InputStream in=getAssets().open("json.txt");
			InputStreamReader ir=new InputStreamReader(in);
			BufferedReader br=new BufferedReader(ir);
			//String s=br.readLine();
			//msg.putExtraData(s.getBytes());
			//msg.setText(s);
			String s="";
			for(int i=0;i<1000;i++){
				s+=i;
			}
			msg.setText(s);
			br.close();
			ir.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendUserDataMessage(byte[] userData, String text) {
		if (userData != null) {
			GotyeMessage toSend;
			if (chatType == 0) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						user, userData, userData.length);
			} else if (chatType == 1) {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						room, userData, userData.length);
			} else {
				toSend = GotyeMessage.createUserDataMessage(currentLoginUser,
						group, userData, userData.length);
			}
			String extraStr = null;
			if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}

			} else if (text.contains("#")) {
				String[] temp = text.split("#");
				if (temp.length > 1) {
					extraStr = temp[1];
				}
			}
			if (extraStr != null) {
				toSend.putExtraData(extraStr.getBytes());
			}

			// int code =
			api.sendMessage(toSend);
			adapter.addMsgToBottom(toSend);
			refreshToTail();
		}
	}

	public void callBackSendImageMessage(GotyeMessage msg) {
		adapter.addMsgToBottom(msg);
		refreshToTail();
	}

	public void info(View v) {
//		if (chatType == 0) {
//			Intent intent = getIntent();
//			intent.setClass(getApplication(), UserInfoPage.class);
//			intent.putExtra("user", user);
//			startActivity(intent);
//		} else if (chatType == 1) {
//			Intent info = new Intent(getApplication(), RoomInfoPage.class);
//			info.putExtra("room", room);
//			startActivity(info);
//		} else {
//			Intent info = new Intent(getApplication(), GroupInfoPage.class);
//			info.putExtra("group", group);
//			startActivity(info);
//		}
	}

	private void loadData() {
		List<GotyeMessage> messages = null;
		if (user != null) {
			messages = api.getLocalMessages(user, true);
		} else if (room != null) {
			messages = api.getLocalMessages(room, true);
		} else if (group != null) {
			messages = api.getLocalMessages(group, true);
		}
		if (messages == null) {
			messages = new ArrayList<GotyeMessage>();
		}
		adapter.refreshData(messages);
	}

	private void setListViewInfo() {
		// 下拉刷新监听器
		pullListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (chatType == 1) {
					api.getLocalMessages(room, true);
				} else {
					List<GotyeMessage> list = null;

					if (chatType == 0) {
						list = api.getLocalMessages(user, true);
					} else if (chatType == 2) {
						list = api.getLocalMessages(group, true);
					}
					if (list != null) {
						adapter.refreshData(list);
					} else {
						ToastUtil.show(ChatActivity.this, "没有更多历史消息");
					}
				}
				adapter.notifyDataSetChanged();
				pullListView.onRefreshComplete();
			}
		});
		pullListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				final GotyeMessage message = adapter.getItem(position - 1);
				pullListView.setTag(message);
				pullListView.showContextMenu();
				return true;
			}
		});
		
		pullListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu conMenu,
							View arg1, ContextMenuInfo arg2) {
						final GotyeMessage message = (GotyeMessage) pullListView
								.getTag();
						if (message == null) {
							return;
						}
						MenuItem m = null;
						if (chatType == 1
								&& !message.getSender().getName()
										.equals(currentLoginUser.getName())) {
							m = conMenu.add(0, 0, 0, "举报");
						}
						// if(message.getType()==GotyeMessageType.GotyeMessageTypeAudio){
						// m= conMenu.add(0, 1, 0, "转为文字(仅限普通话)");
						// }
						if (m == null) {
							return;
						}
						m.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getItemId()) {
								case 0:
									api.report(0, "举报的说明", message);
									break;

								case 1:
									// api.decodeMessage(message);
									break;
								}
								return true;
							}
						});
					}
				});

	}
 

	// private void showTalkView() {
	// dismissTalkView();
	// View view = LayoutInflater.from(this).inflate(
	// R.layout.gotye_audio_recorder_ring, null);
	//
	// anim = initRecordingView(view);
	// anim.start();
	// menuWindow = new PopupWindow(this);
	// menuWindow.setContentView(view);
	// menuWindow.setAnimationStyle(android.R.style.Animation_Dialog);
	// // int width = (int) (view.getMeasuredWidth() * 3 * 1.0 / 2);
	// Drawable dd = getResources().getDrawable(R.drawable.gotye_pls_talk);
	// menuWindow.setWidth(dd.getIntrinsicWidth());
	//
	// menuWindow.setHeight(dd.getIntrinsicHeight());
	// menuWindow.setBackgroundDrawable(null);
	// menuWindow.showAtLocation(findViewById(R.id.gotye_chat_content),
	// Gravity.CENTER, 0, 0);
	// }
	//
	// private void dismissTalkView() {
	// if (menuWindow != null && menuWindow.isShowing()) {
	// menuWindow.dismiss();
	// }
	// if (anim != null && anim.isRunning()) {
	// anim.stop();
	// }
	// }
	//
	// private AnimationDrawable initRecordingView(View layout) {
	// ImageView speakingBg = (ImageView) layout
	// .findViewById(R.id.background_image);
	// speakingBg.setImageDrawable(getResources().getDrawable(
	// R.drawable.gotye_pop_voice));
	// layout.setBackgroundResource(R.drawable.gotye_pls_talk);
	//
	// AnimationDrawable anim = AnimUtil.getSpeakBgAnim(getResources());
	// anim.selectDrawable(0);
	//
	// ImageView dot = (ImageView) layout.findViewById(R.id.speak_tip);
	// dot.setBackgroundDrawable(anim);
	// return anim;
	// }
	
	public void showPopupWindow2(View view) {
		Context context = ChatActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view_popunwindow = mLayoutInflater.inflate(
				R.layout.chat_image_popwindow, null);

		int screenWidth=getResources().getDisplayMetrics().widthPixels;
		  int screenHeight=getResources().getDisplayMetrics().heightPixels;
		mPopupWindow = new PopupWindow(view_popunwindow, 
				screenWidth, screenHeight, false);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		mPopupWindow.showAsDropDown(view, 0, 0);
		mPopupWindow.update();
	}

	@Override
	protected void onDestroy() {
		api.removeListener(this);
		if (chatType == 0) {
			api.deactiveSession(o_user);
		} else if (chatType == 1) {
			api.deactiveSession(o_room);
			api.leaveRoom(o_room);
		} else {
			api.deactiveSession(o_group);
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (GotyeVoicePlayClickPlayListener.isPlaying
				&& GotyeVoicePlayClickPlayListener.currentPlayListener != null) {
			// 停止语音播放
			GotyeVoicePlayClickPlayListener.currentPlayListener.stopPlayVoice(false);
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		if (onRealTimeTalkFrom == REALTIMEFROM_SELF) {
			api.stopTalk();
//			return;
//		} else if (onRealTimeTalkFrom == REALTIMEFROM_OTHER) {
			api.stopPlay();
//		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.img_back:
			if(onRealTimeTalkFrom>=0){
				return;
			}
			onBackPressed();
			break;
		case R.id.send_voice:
			if (pressToVoice.getVisibility() == View.VISIBLE) {
				pressToVoice.setVisibility(View.GONE);
				textMessage.setVisibility(View.VISIBLE);
				voice_text_chage
						.setImageResource(R.drawable.voice_btn_selector);
				showMoreType.setImageResource(R.drawable.send_selector);
				moreTypeForSend = true;
				moreTypeLayout.setVisibility(View.GONE);
			} else {
				pressToVoice.setVisibility(View.VISIBLE);
				textMessage.setVisibility(View.GONE);

				voice_text_chage
						.setImageResource(R.drawable.change_to_text_press);

				showMoreType.setImageResource(R.drawable.more_type_selector);
				moreTypeForSend = false;
				hideKeyboard();
			}

			break;
		case R.id.more_type:
			if (moreTypeForSend) {
				hideKeyboard();
				String str = textMessage.getText().toString();
				sendTextMessage(str);
				textMessage.setText("");
			} else {
				if (moreTypeLayout.getVisibility() == View.VISIBLE) {
					moreTypeLayout.setVisibility(View.GONE);
				} else {
					moreTypeLayout.setVisibility(View.VISIBLE);
					if (chatType == 1 && api.supportRealtime(room) == true) {
						moreTypeLayout.findViewById(R.id.real_time_voice_chat)
								.setVisibility(View.VISIBLE);
					}

				}
			}
			break;
		case R.id.to_gallery:
			takePic();
			break;
		case R.id.to_camera:
			takePhoto();
			break;
		case R.id.voice_to_text:
//			 showTalkView();
			break;
		case R.id.real_time_voice_chat:
			realTimeTalk();
			break;
		case R.id.stop_real_talk:
			// int i =
			api.stopTalk();
			break;
		default:
			break;
		}
	}

	public void showImagePrev(GotyeMessage message) {
		hideKeyboard();
	}

	public void realTimeTalk() {
		if (onRealTimeTalkFrom > 0) {
			Toast.makeText(this, "请稍后...", Toast.LENGTH_SHORT).show();
			return;
		}
		api.startTalk(room, WhineMode.DEFAULT, true, Voice_MAX_TIME_LIMIT);
		moreTypeLayout.setVisibility(View.GONE);
	}

	public void hideKeyboard() {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 显示或者隐藏输入法
		imm.hideSoftInputFromWindow(textMessage.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void takePic() {
		Intent intent;
		intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/jpeg");
		startActivityForResult(intent, REQUEST_PIC);

//		 Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//		 intent.setType("image/*");
//		 startActivityForResult(intent, REQUEST_PIC);
	}

	private void takePhoto() {
		selectPicFromCamera();
	}

	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照",
					Toast.LENGTH_SHORT).show();
			return;
		}

		cameraFile = new File(PathUtil.getAppFIlePath()
				+ +System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选取图片的返回值
		if (requestCode == REQUEST_PIC) {
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					String path = URIUtil.uriToPath(this, selectedImage);
					sendPicture(path);
				}
			}

		} else if (requestCode == REQUEST_CAMERA) {
			if (resultCode == RESULT_OK) {

				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			}
		}
		// TODO 获取图片失败
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void sendPicture(String path) {
		SendImageMessageTask task;
		if (chatType == 0) {
			task = new SendImageMessageTask(this, user);
		} else if (chatType == 1) {
			task = new SendImageMessageTask(this, room);
		} else {
			task = new SendImageMessageTask(this, group);
		}
		task.execute(path);
	}

	public void setPlayingId(long playingId) {
		this.playingId = playingId;
		adapter.notifyDataSetChanged();
	}

	public long getPlayingId() {
		return playingId;
	}

	@Override
	public void onSendMessage(int code, GotyeMessage message) {
		Log.d("OnSend", "code= " + code + "message = " + message);
		// GotyeChatManager.getInstance().insertChatMessage(message);
		adapter.updateMessage(message);
		if (message.getType() == GotyeMessageType.GotyeMessageTypeAudio) {
			// api.decodeMessage(message);
		}
		// message.senderUser =
		// DBManager.getInstance().getUser(currentLoginName);
		pullListView.setSelection(adapter.getCount());
	}

	@Override
	public void onReceiveMessage(int code, GotyeMessage message) {
		// GotyeChatManager.getInstance().insertChatMessage(message);
		if (chatType == 0) {
			if (isMyMessage(message)) {
				adapter.addMsgToBottom(message);
				pullListView.setSelection(adapter.getCount());
				api.downloadMessage(message);
			}
		} else if (chatType == 1) {
			if (message.getReceiver().getId() == room.getRoomID()) {
				adapter.addMsgToBottom(message);
				pullListView.setSelection(adapter.getCount());
				api.downloadMessage(message);

			}
		} else if (chatType == 2) {
			if (message.getReceiver().getId() == group.getGroupID()) {
				adapter.addMsgToBottom(message);
				pullListView.setSelection(adapter.getCount());
				api.downloadMessage(message);
			}
		}

		// scrollToBottom();
	}

	private boolean isMyMessage(GotyeMessage message) {
		if (message.getSender() != null
				&& user.getName().equals(message.getSender().getName())
				&& currentLoginUser.getName().equals(message.getReceiver().getName())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDownloadMessage(int code, GotyeMessage message) {
		 adapter.updateChatMessage(message);
	}

	@Override
	public void onEnterRoom(int code, long lastMsgID, GotyeRoom room) {
		ProgressDialogUtil.dismiss();
		if (code == 0) {
			api.activeSession(room);
			loadData();
			GotyeRoom temp = api.requestRoomInfo(room.getRoomID(), true);
			if (temp != null && !TextUtils.isEmpty(temp.getRoomName())) {
				title.setText("聊天室：" + temp.getRoomName());
			}
		} else {
			ToastUtil.show(this, "房间不存在...");
			finish();
		}
	}

	@Override
	public void onGetMessageList(int code, List<GotyeMessage> list) {
		if (chatType == 0) {
			List<GotyeMessage> listmessages = api.getLocalMessages(o_user, false);
			if (listmessages != null) {
				adapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}else if (chatType == 1){
			List<GotyeMessage> listmessages = api.getLocalMessages(o_room, false);
			if (listmessages != null) {
				adapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}else if (chatType == 2){
			List<GotyeMessage> listmessages = api.getLocalMessages(o_group, false);
			if (listmessages != null) {
				adapter.refreshData(listmessages);
			} else {
				ToastUtil.show(this, "没有历史记录");
			}
		}
		adapter.notifyDataSetInvalidated();
		pullListView.onRefreshComplete();
	}
	
    boolean realTalk,realPlay;
	@Override
	public void onStartTalk(int code, boolean isRealTime, int targetType,
			GotyeChatTarget target) {
		
		Log.e("player", "onStartTalk:" + isRealTime);
		if (isRealTime) {
			this.realTalk=true;
			if (code != 0) {
				ToastUtil.show(this, "抢麦失败，先听听别人说什么。");
				return;
			}
			if (GotyeVoicePlayClickPlayListener.isPlaying) {
				GotyeVoicePlayClickPlayListener.currentPlayListener.stopPlayVoice(true);
			}
			onRealTimeTalkFrom = 0;
			realTimeAnim.start();
			realTalkView.setVisibility(View.VISIBLE);
			realTalkName.setText("您正在说话..");
			stopRealTalk.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onStopTalk(int code, GotyeMessage message, boolean isVoiceReal) {
		Log.e("player", "onStopTalk");
		
		if (isVoiceReal) {
			onRealTimeTalkFrom = -1;
			realTimeAnim.stop();
			realTalkView.setVisibility(View.GONE);
		} else {
			if (code != 0) {
				ToastUtil.show(this, "时间太短...");
				return;
			} else if (message == null) {
				ToastUtil.show(this, "时间太短...");
				return;
			}
//			api.decodeMessage(message);
			api.sendMessage(message);
			adapter.addMsgToBottom(message);
			refreshToTail();
		}

	}
	
	@Override
	public void onPlayStart(int code, GotyeMessage message) {
		Log.e("player", "onPlayStart");
	}

	@Override
	public void onPlaying(int code, int position) {
		Log.e("player", "onPlaying");
	}

	@Override
	public void onPlayStop(int code) {
		Log.e("player", "onPlayStop");
		setPlayingId(-1);
		if(this.realPlay){
			onRealTimeTalkFrom = -1;
			realTimeAnim.stop();
			realTalkView.setVisibility(View.GONE);	
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPlayStartReal(int code, long roomId, String who) {
		if (code == 0 && roomId == this.room.getRoomID()) {
			this.realPlay=true;
			onRealTimeTalkFrom = 1;
			realTalkView.setVisibility(View.VISIBLE);
			realTalkName.setText(who + "正在说话..");
			realTimeAnim.start();
			stopRealTalk.setVisibility(View.GONE);
			if (GotyeVoicePlayClickPlayListener.isPlaying) {
				GotyeVoicePlayClickPlayListener.currentPlayListener.stopPlayVoice(false);
			}
		}
	}

	@Override
	public void onRequestUserInfo(int code, GotyeUser user) {
        if(chatType==0){
        	if(user.getName().equals(this.user.getName())){
    			this.user = user;
    		}
        }
	}

	@Override
	public void onDownloadMedia(int code, String path, String url) {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onUserDismissGroup(GotyeGroup group, GotyeUser user) {
		// TODO Auto-generated method stub
		if (this.group != null && group.getGroupID() == this.group.getGroupID()) {
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Toast.makeText(getBaseContext(), "群主解散了该群,会话结束", Toast.LENGTH_SHORT)
					.show();
			finish();
			startActivity(i);
		}
	}

	@Override
	public void onUserKickdFromGroup(GotyeGroup group, GotyeUser kicked,
			GotyeUser actor) {
		// TODO Auto-generated method stub
		if (this.group != null && group.getGroupID() == this.group.getGroupID()) {
			if (kicked.getName().equals(currentLoginUser.getName())) {
				Intent i = new Intent(this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Toast.makeText(getBaseContext(), "您被踢出了群,会话结束",
						Toast.LENGTH_SHORT).show();
				finish();
				startActivity(i);
			}

		}
	}

	@Override
	public void onReport(int code, GotyeMessage message) {
		// TODO Auto-generated method stub
		if (code == GotyeStatusCode.CODE_OK) {
			ToastUtil.show(this, "举报成功");
		} else {
			ToastUtil.show(this, "举报失败");
		}
		super.onReport(code, message);
	}

	@Override
	public void onRequestRoomInfo(int code, GotyeRoom room) {
		// TODO Auto-generated method stub
		if (this.room != null && this.room.getRoomID() == room.getRoomID()) {
			title.setText("聊天室：" + room.getRoomName());
		}
		super.onRequestRoomInfo(code, room);
	}

	@Override
	public void onRequestGroupInfo(int code, GotyeGroup group) {
		if (this.group != null && this.group.getGroupID() == group.getGroupID()) {
			title.setText("聊天室：" + group.getGroupName());
		}
	}

	@Override
	public void onDecodeMessage(int code, GotyeMessage message) {
		if (code == GotyeStatusCode.CODE_OK) {
			VoiceToTextUtil util = new VoiceToTextUtil(ChatActivity.this,mASREngine);
			util.toPress(message);
		}  
		super.onDecodeMessage(code, message);
	}
	@Override
	public void onLogin(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub
		setErrorTip(1);
	}

	@Override
	public void onLogout(int code) {
		// TODO Auto-generated method stub
		setErrorTip(0);
	}

	@Override
	public void onReconnecting(int code, GotyeUser currentLoginUser) {
		// TODO Auto-generated method stub
		setErrorTip(-1);
	}
	private void setErrorTip(int code) {
		if (code == 1) {
			 findViewById(R.id.error_tip).setVisibility(View.GONE);
		} else {
			 findViewById(R.id.error_tip).setVisibility(View.VISIBLE);
			if (code == -1) {
				 findViewById(R.id.loading)
						.setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.showText))
						.setText("连接中...");
				 findViewById(R.id.error_tip_icon).setVisibility(
						View.GONE);
			} else {
				 findViewById(R.id.loading).setVisibility(View.GONE);
				((TextView)  findViewById(R.id.showText))
						.setText("未连接");
				 findViewById(R.id.error_tip_icon).setVisibility(
						View.VISIBLE);
			}
		}
	}
	public void refreshToTail() {
		if (adapter != null) {
			if (pullListView.getLastVisiblePosition()
					- pullListView.getFirstVisiblePosition() <= pullListView.getCount())
				pullListView.setStackFromBottom(false);
			else
				pullListView.setStackFromBottom(true);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pullListView.setSelection(pullListView.getAdapter().getCount() - 1);

					// This seems to work
					handler.post(new Runnable() {
						@Override
						public void run() {
							pullListView.clearFocus();
							pullListView.setSelection(pullListView.getAdapter()
									.getCount() - 1);
						}
					});
				}
			}, 300);
			pullListView.setSelection(adapter.getCount()
					+ pullListView.getHeaderViewsCount() - 1);
		}
	}
	private Handler handler=new Handler();

}
