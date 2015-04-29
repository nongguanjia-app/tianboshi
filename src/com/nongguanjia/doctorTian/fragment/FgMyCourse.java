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

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.view.PagerSlidingTabStrip;

/**
 * @author tx
 * 我的课程
 */
@SuppressLint("NewApi")
public class FgMyCourse extends Fragment {
	Resources resources;
    private ViewPager mPager;
    private String role;
    private String[] titles;
    private PagerSlidingTabStrip tabs;
    Fragment hasStart;
    Fragment willStart;
    Fragment myCourse;
	
	@Override
	public void onAttach(Activity activity) {
		role = ((AppApplication)activity.getApplication()).ROLE;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_course, container,false);
		resources = getResources();
        
        if(role.equals("推广人")){
			titles = new String[]{ "已经开始", "即将开始", "我的课程" };
		}else{
			titles = new String[]{"已经开始", "即将开始"};
		}
        
        initTextView(view);
		
		return view;
	}
	
	
	private void initTextView(View parentView) {
		tabs = (PagerSlidingTabStrip) parentView.findViewById(R.id.tabs_myCourse);
		mPager = (ViewPager) parentView.findViewById(R.id.vPager);
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
				if (hasStart == null) {
					hasStart = new FgMyCourseHasStart();
				}
				return hasStart;
			case 1:
				if (willStart == null) {
					willStart = new FgMyCourseWillStart();
				}
				return willStart;
			case 2:
				if (myCourse == null) {
					myCourse = new FgMyCourseTG();
				}
				return myCourse;
			default:
				return null;
			}
		}
	}
	
}
