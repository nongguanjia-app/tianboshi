package com.nongguanjia.doctorTian.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.view.PagerSlidingTabStrip;

/**
 * @author tx
 * 客户服务
 */
@SuppressLint("NewApi")
public class FgChat extends Fragment {
	Resources resources;
    private ViewPager mPager;
    private PagerSlidingTabStrip tabs;
    Fragment fgDiscuss;
    Fragment fgLetter;
    Fragment fgCustomer;
    private String role;
    private String[] titles;
    private TextView tv_title;
    private ImageView img_back;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		role = ((AppApplication)activity.getApplication()).ROLE;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fg_chat, container,false);
		if(role.equals("推广人")){
			titles = new String[]{ "讨论", "私信", "我的客户" };
		}else{
			titles = new String[]{"讨论", "私信"};
		}
		
		initView(view);
		
		return view;
	}
	
	
	
	private void initView(View view) {
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		img_back = (ImageView)view.findViewById(R.id.img_back);
		if(role.equals("推广人")){
			tv_title.setText("客户服务");
		}else{
			tv_title.setText("交流");
		}
		
		img_back.setVisibility(View.GONE);
		
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mPager = (ViewPager) view.findViewById(R.id.vPager);
		
        mPager.setAdapter(new MyAdapter(getChildFragmentManager(),titles));
        
        tabs.setViewPager(mPager);
    }
	
	public class MyAdapter extends FragmentPagerAdapter{
		String[] _titles;
		public MyAdapter(FragmentManager fm,String[] titles) {
			super(fm);
			_titles=titles;
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
			case 0:
				if (fgDiscuss == null) {
					fgDiscuss = new FgChatDiscuss();
				}
				return fgDiscuss;
			case 1:
				if (fgLetter == null) {
					fgLetter = new FgChatLetter();
				}
				return fgLetter;
			case 2:
				if (fgCustomer == null) {
					fgCustomer = new FgChatCustomer();
				}
				return fgCustomer;
			default:
				return null;
			}
		}
	}
	
}
