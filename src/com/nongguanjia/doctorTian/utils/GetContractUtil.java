package com.nongguanjia.doctorTian.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.nongguanjia.doctorTian.bean.ContractInfo;


public class GetContractUtil {
	//读取本地通讯录
	public static List<ContractInfo> getPhoneContracts(Context mContext){
		List<ContractInfo> contractList = new ArrayList<ContractInfo>();
		
		ContentResolver resolver = mContext.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,null, null, null, null); //传入正确的uri
		
		if(phoneCursor != null){
			while(phoneCursor.moveToNext()){
				//获取手机号
				String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
				//当手机号码为空的或者为空字段 跳过当前循环
				if(TextUtils.isEmpty(phoneNumber)){
					continue;
				}
				//获取联系人名称
				int nameIndex = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME); 
				String name = phoneCursor.getString(nameIndex);
				
				//获取联系人id
				int contactIndex = phoneCursor.getColumnIndex(Phone.CONTACT_ID);
				Long contactId = phoneCursor.getLong(contactIndex);
				
				//获取联系人头像id
				int photoIndex = phoneCursor.getColumnIndex(Phone.PHOTO_ID);
				Long photoId = phoneCursor.getLong(photoIndex);
				
				//得到联系人头像Bitamp  
//		        Bitmap contactPhoto = null;  
//		 
//		        //photoId 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
//		        if(photoId > 0 ) {  
//		            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);  
//		            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
//		            contactPhoto = BitmapFactory.decodeStream(input);  
//		        }else {  
//		            contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_icon);  
//		        }  
				
				ContractInfo contractInfo = new ContractInfo();
				contractInfo.setId(contactId+"");
				contractInfo.setName(name);
				contractInfo.setPhoneNum(phoneNumber);
				contractInfo.setPhotoId(photoId+"");
				
				contractList.add(contractInfo);
			}
			phoneCursor.close();
		}
		return contractList;
	}
	
	
}
