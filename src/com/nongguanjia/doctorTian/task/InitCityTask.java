package com.nongguanjia.doctorTian.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.nongguanjia.doctorTian.db.CacheCityHelper;

public class InitCityTask extends AsyncTask<String, Integer, String> {
	private CacheCityHelper helper;
	private Context context;
	
	public InitCityTask(Context context){
		this.context = context;
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
			Toast.makeText(context, "初始化数据成功", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

}
