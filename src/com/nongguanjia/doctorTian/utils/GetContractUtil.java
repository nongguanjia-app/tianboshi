package com.nongguanjia.doctorTian.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

import com.nongguanjia.doctorTian.bean.ContractInfo;

public class GetContractUtil {
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 1;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 2;

	
	/**得到手机SIM卡联系人人信息**/
	public static List<ContractInfo> getSIMContacts(Context mContext) {
		List<ContractInfo> contractList = new ArrayList<ContractInfo>();

		ContentResolver resolver = mContext.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像

				ContractInfo contractInfo = new ContractInfo();
				contractInfo.setPhoneNum(phoneNumber);
				contractInfo.setName(contactName);

				contractList.add(contractInfo);
			}

			phoneCursor.close();
		}
		return contractList;
	}

}
