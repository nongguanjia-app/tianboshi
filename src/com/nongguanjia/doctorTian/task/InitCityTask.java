package com.nongguanjia.doctorTian.task;

import android.content.Context;
import android.os.AsyncTask;

import com.nongguanjia.doctorTian.db.CacheCityHelper;

public class InitCityTask extends AsyncTask<String, Integer, String> {
	private CacheCityHelper helper;
	
	public InitCityTask(Context context){
		helper = CacheCityHelper.getInstance(context);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		if(helper.selectCount() == 0){
			helper.insertTable();
			return "0";
		}
		return "1";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if(result.equals("0")){
			helper.closeDB();
		}
		super.onPostExecute(result);
	}

}
