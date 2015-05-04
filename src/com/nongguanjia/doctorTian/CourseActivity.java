package com.nongguanjia.doctorTian;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lecloud.skin.PlayerStateCallback;
import com.lecloud.skin.vod.VODPlayCenter;
import com.letvcloud.sdk.base.util.Logger;
import com.letvcloud.sdk.play.util.LogUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.Courses;
import com.nongguanjia.doctorTian.bean.Favorite;
import com.nongguanjia.doctorTian.bean.FavoriteColl;
import com.nongguanjia.doctorTian.fragment.FgCourse;
import com.nongguanjia.doctorTian.fragment.FgCourseExp;
import com.nongguanjia.doctorTian.fragment.FgDetail;
import com.nongguanjia.doctorTian.fragment.FgDiscussArea;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;
import com.nongguanjia.doctorTian.view.PagerSlidingTabStrip;

/**
 * @author tx 课程详情
 */
public class CourseActivity extends FragmentActivity implements OnClickListener {
	private ViewPager mPager;
	private PagerSlidingTabStrip tabs;
	private String[] titles = new String[] { "课程详情", "课程表", "经验谈", "讨论区" };

	private FgDetail fgDetail;
	private FgCourse fgCourse;
	private FgCourseExp fgCourseExp;
	private FgDiscussArea fgDiscussArea;
	
	private TextView tv_title;
	private ImageView ivCollection;
	private String flagCollection;
//	private ImageView img_back;

	private FragmentManager fragmentManager;
	private Favorite mFavorite;
	private FavoriteColl mFavoriteColl;
	private boolean isBackgroud = false;
	private Bundle db;
	private Context mContext;
	private String id;
	private String title;
	private Courses courses;//课程信息
	private BroadcastReceiver broadcastReceiver;

	// 乐视视频
	private RelativeLayout layout_player;
	private VODPlayCenter mPlayerView;
	private EditText etUUID;
	private EditText etVUID;
	private EditText etLiveId;
	private RadioButton rb1;
	private RadioButton rb2;

	private String vuid;
	private String courseId;
	private PlayReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course);
		
		mReceiver = new PlayReceiver();
		IntentFilter intentFilter = new IntentFilter(CommonConstant.VIDEO_ACTION);
		registerReceiver(mReceiver, intentFilter);
		
		mContext = this;
		init();
		
		broadcastReceiver = new CollectionBroadcastReceiver();
		registerReceiver(broadcastReceiver, new IntentFilter(CommonConstant.VIDEO_ACTION));
		
		/*
		 * 乐视
		 */
		layout_player = (RelativeLayout) findViewById(R.id.layout_player);
		mPlayerView = new VODPlayCenter(CourseActivity.this, true);
		layout_player.addView(mPlayerView.getPlayerView());

		mPlayerView.setPlayerStateCallback(new PlayerStateCallback() {

			@Override
			public void onStateChange(int state, Object... extra) {
				if (state == PlayerStateCallback.PLAYER_VIDEO_PAUSE) {
					Logger.e("onStateChange", "PLAYER_VIDEO_PAUSE");
				} else if (state == PlayerStateCallback.PLAYER_VIDEO_PLAY) {
					Logger.e("onStateChange", "PLAYER_VIDEO_PLAY");
				} else if (state == PlayerStateCallback.PLAYER_VIDEO_RESUME) {
					Logger.e("onStateChange", "PLAYER_VIDEO_RESUME");
				} else if (state == PlayerStateCallback.PLAYER_STOP) {
					Logger.e("onStateChange", "PLAYER_STOP");
				}
			}
		});
		
	}

	private void init() {
		db = getIntent().getExtras();
		courseId = db.getString("Id");
		title = db.getString("title");
		fragmentManager = getSupportFragmentManager();

		tv_title = (TextView)findViewById(R.id.course_title);
		ivCollection = (ImageView) findViewById(R.id.course_collection);
		
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.vPager);

		mPager.setAdapter(new MyAdapter(fragmentManager, titles));

		tabs.setViewPager(mPager);
		
		ivCollection.setOnClickListener(this);
	}

	public class MyAdapter extends FragmentPagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:// 课程详情
				if (fgDetail == null) {
					fgDetail = new FgDetail();
					fgDetail.setCourseId(courseId);
				}
				return fgDetail;
			case 1:// 课程表
				if (fgCourse == null) {
					fgCourse = new FgCourse();
					fgCourse.setCourseId(courseId);
				}
				return fgCourse;
			case 2:// 经验谈
				if (fgCourseExp == null) {
					fgCourseExp = new FgCourseExp();
					fgCourseExp.setCourseId(courseId);
				}
				return fgCourseExp;
			case 3:// 讨论区
				if (fgDiscussArea == null) {
					fgDiscussArea = new FgDiscussArea();
					fgDiscussArea.setCourseId(courseId);
				}
				return fgDiscussArea;
			default:
				return null;
			}
		}
	}

	@Override
	public void onClick(View v) {
		String phoneNum = ((AppApplication) getBaseContext().getApplicationContext()).PHONENUM;
		
		Bundle bd = getIntent().getExtras();
		id = bd.getString("Id");
		
		String url = CommonConstant.deletefavorite + "/" + phoneNum + "," + id;
		String urlAdd = CommonConstant.addfavorite + "/" + phoneNum + "," + id;

		switch (v.getId()) {
		case R.id.course_collection:
			if("0".equals(flagCollection)){
				collection(urlAdd, flagCollection);
			}else{
				collection(url, flagCollection);
			}
			break;
		}
	}
	/**
	 * 收藏课程
	 * @param url
	 */
	private void collection (String url,final String flag){
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(getApplicationContext(), "请求接口异常",Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString,throwable);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				JSONObject ja;
				try {
					if("0".equals(flag)){
						ja = response.getJSONObject("AddFavorite");
						Gson gson = new Gson();
						mFavorite = gson.fromJson(ja.toString(), Favorite.class);
						if (mFavorite.getReturnCode().equals("1")) {
							Toast.makeText(getApplicationContext(), "收藏成功",Toast.LENGTH_SHORT).show();
							flagCollection = "1";
							ivCollection.setImageResource(R.drawable.collection_not);
						} else {
							Toast.makeText(getApplicationContext(), "收藏失败",Toast.LENGTH_SHORT).show();
						}
					}else{
						ja = response.getJSONObject("DeleteFavorite");
						Gson gson = new Gson();
						mFavorite = gson.fromJson(ja.toString(), Favorite.class);
						if (mFavorite.getReturnCode().equals("1")) {
							Toast.makeText(getApplicationContext(), "取消收藏",Toast.LENGTH_SHORT).show();
							flagCollection = "0";
							ivCollection.setImageResource(R.drawable.collection);
						} else {
							Toast.makeText(getApplicationContext(), "取消收藏失败",Toast.LENGTH_SHORT).show();
						}
					}
					// 解析应答数据
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mPlayerView != null) {
			if (isBackgroud) {
				if (mPlayerView.getCurrentPlayState() == PlayerStateCallback.PLAYER_VIDEO_PAUSE) {
					this.mPlayerView.resumeVideo();
				} else {
					Logger.e("VODActivity", "已回收，重新请求播放");
					if(!TextUtils.isEmpty(vuid)){
						mPlayerView.playVideo(CommonConstant.UUID, vuid,
								"c8b127186556ccfae084bbede663a898", "", "测试节目");
					}
					
				}
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mPlayerView != null) {
			mPlayerView.pauseVideo();
			isBackgroud = true;
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	
	//广播接收器
	public class PlayReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			vuid = intent.getExtras().getString("vid");
			if(!TextUtils.isEmpty(vuid)){
				mPlayerView.playVideo(CommonConstant.UUID, vuid, "c8b127186556ccfae084bbede663a898", "", "");
			}
			
		}

	}	

	@Override
	protected void onDestroy() {
		mPlayerView.destroyVideo();
		layout_player.removeAllViews();
		mPlayerView = null;
		super.onDestroy();
		isBackgroud = false;
		LogUtils.clearLog();
		unregisterReceiver(broadcastReceiver);
	}
	/**
	 * 获取到课程信息时显示是否收藏
	 * @author itachi
	 *
	 */
	private class CollectionBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			flagCollection = arg1.getStringExtra("flag");
			String title = arg1.getStringExtra("title");
			tv_title.setText(title);
			if(!TextUtils.isEmpty(flagCollection)){
				if("1".equals(flagCollection)){
					ivCollection.setImageResource(R.drawable.collection_not);
				}else{
					ivCollection.setImageResource(R.drawable.collection);
				}
			}
		}
	}
	
}