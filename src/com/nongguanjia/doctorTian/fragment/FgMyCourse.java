package com.nongguanjia.doctorTian.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nongguanjia.doctorTian.R;
import com.nongguanjia.doctorTian.adapter.MyFragmentPageAadpter;

/**
 * @author tx
 * 我的课程
 */
@SuppressLint("NewApi")
public class FgMyCourse extends Fragment {
	Resources resources;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvHasStart, tvWillStart;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    public final static int num = 2 ; 
    Fragment hasStart;
    Fragment willStart;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_course, container,false);
		resources = getResources();
        initWidth(view);
        initTextView(view);
        initViewPager(view);
        TranslateAnimation animation = new TranslateAnimation(position_one, offset, 0, 0);
        tvHasStart.setTextColor(resources.getColor(R.color.lightgreen));
        animation.setFillAfter(true);
        animation.setDuration(300);
        ivBottomLine.startAnimation(animation);
		
		return view;
	}
	
	
	private void initWidth(View parentView) {
        ivBottomLine = (ImageView) parentView.findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / num - bottomLineWidth) / 2);
        int avg = (int) (screenW / num);
        position_one = avg + offset;
    }
	
	private void initTextView(View parentView) {
		tvHasStart = (TextView) parentView.findViewById(R.id.tv_has_start);
		tvWillStart = (TextView) parentView.findViewById(R.id.tv_will_start);

        tvHasStart.setOnClickListener(new MyOnClickListener(0));
        tvWillStart.setOnClickListener(new MyOnClickListener(1));
    }
	
	
	private void initViewPager(View parentView) {
        mPager = (ViewPager) parentView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        hasStart = new FgMyCourseHasStart();
        willStart = new FgMyCourseWillStart();

        fragmentsList.add(hasStart);
        fragmentsList.add(willStart);
        
        mPager.setAdapter(new MyFragmentPageAadpter(getChildFragmentManager(), fragmentsList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
        
    }
	
	
	public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
	
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, offset, 0, 0);
                    tvHasStart.setTextColor(resources.getColor(R.color.lightgreen));
                } 
                tvWillStart.setTextColor(resources.getColor(R.color.lightgray));
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                    tvWillStart.setTextColor(resources.getColor(R.color.lightgreen));
                } 
                tvHasStart.setTextColor(resources.getColor(R.color.lightgray));
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
	
}
