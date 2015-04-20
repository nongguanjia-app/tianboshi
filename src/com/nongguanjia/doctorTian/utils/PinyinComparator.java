package com.nongguanjia.doctorTian.utils;

import java.util.Comparator;

import com.nongguanjia.doctorTian.bean.AllAttention;

public class PinyinComparator implements Comparator<AllAttention> {

	public int compare(AllAttention o1, AllAttention o2) {
		String str1 = PingYinUtil.getPingYin(o1.getNoteName());
	    String str2 = PingYinUtil.getPingYin(o2.getNoteName());
	    return str1.compareTo(str2);
	}

}
