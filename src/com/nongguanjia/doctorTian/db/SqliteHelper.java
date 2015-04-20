package com.nongguanjia.doctorTian.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
	private static String DB_NAME = "doctorTian.db";
	private static int DB_VERSION = 1;

	public SqliteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	private void createTable(SQLiteDatabase db) {
		// 创建user_info表
		String create_user_info = "CREATE TABLE IF NOT EXISTS user_info("
				+ "_id integer primary key," 
				+ "phone char(11),"
				+ "psd char(20))";
		db.execSQL(create_user_info);

		
	}

}
