package com.nongguanjia.doctorTian.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.AllEcho;
import com.nongguanjia.doctorTian.task.AddReplyTask;
import com.nongguanjia.doctorTian.utils.CommonConstant;

/**
 * @author tx
 * 回复评论
 */
public class ReplyDialog extends Dialog implements android.view.View.OnClickListener{
	private Context context;
	private String id; //经验谈id
	private String talkId;
	private String isExp;
	
	private TextView tv_title;
	private Button btn_confirm, btn_cancel;
	private EditText ed_reason;

	public ReplyDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	
	public void setIdAndEcho(String id, String talkId, String isExp){
		this.id = id;
		this.talkId = talkId;
		this.isExp = isExp;
	}
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case CommonConstant.RESPONSE_ERROR:
				Toast.makeText(getContext(), "发表回复失败", Toast.LENGTH_SHORT).show();
				break;
			case CommonConstant.RESPONSE_SUCCESS:
				Toast.makeText(getContext(), "发表回复成功", Toast.LENGTH_SHORT).show();
				ed_reason.setText("");
				dismiss();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);
		
		initParams();
		
		init();
	}

	
	private void initParams(){
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = 350;
		
		dialogWindow.setAttributes(lp);
	}
	
	
	
	private void init(){
		tv_title = (TextView)findViewById(R.id.reply_title);
		btn_confirm = (Button)findViewById(R.id.btn_reply);
		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		ed_reason = (EditText)findViewById(R.id.ed_reason);
		
		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
//		tv_title.setText("回复：" + echo.getName());
		tv_title.setText("回复");
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_reply:
			if(TextUtils.isEmpty(ed_reason.getText())){
				Toast.makeText(context, "请输入回复内容", Toast.LENGTH_SHORT).show();
			}else{
				reply(ed_reason.getText().toString());
			}
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	
	

	private void reply(String content){
		String phoneNum = ((AppApplication)context.getApplicationContext()).PHONENUM;
		String url = CommonConstant.addtalkreply 
				+ "/" + id + "," 
				+ phoneNum + ","
				+ content + ","
				+ isExp + "," //经验谈
				+ talkId;
		AddReplyTask task = new AddReplyTask(url, mHandler);
		task.addReply();
	}
	
}
