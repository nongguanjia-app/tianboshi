package com.nongguanjia.doctorTian.db;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author tianxin
 * 缓存用户名密码
 */
public class CacheUserHelper {
    private static CacheUserHelper INSTANCE = null;
	private SqliteHelper mHelper = null;
	private SQLiteDatabase db = null;
    private static final String TB_NAME = "user_info";
    
    public CacheUserHelper(Context context){
    	mHelper = new SqliteHelper(context);
    	db = mHelper.getWritableDatabase();
    }
    
    
    public static CacheUserHelper getInstance(Context context) {
		if (INSTANCE == null) {
			return new CacheUserHelper(context);
		}
		return INSTANCE;
	}
    
    
    
    public void insertTable(String phoneNum, String psd){
    	ContentValues values = new ContentValues();

    	values.put("phone", phoneNum);
		values.put("psd", psd);
    	
        db.insert(TB_NAME, "_id", values);

    }
    
    
    public HashMap<String, String> selectTable(){
    	String sql = "SELECT * FROM " + TB_NAME;
    	
    	Cursor cursor = db.rawQuery(sql, null);
    	
    	HashMap<String, String> info = new HashMap<String, String>();
    	
    	if(cursor.getCount() > 0){
    		cursor.moveToFirst();
    		info.put("phone", cursor.getString(1));
    		info.put("psd", cursor.getString(2));
    	}
    	
    	cursor.close();
    	
    	return info;
    }
    
    
    
    public void deleteTable(){
    	db.delete(TB_NAME, null, null);
    }
    
    
    public void closeDB(){
    	db.close();
	}
    
    
}
