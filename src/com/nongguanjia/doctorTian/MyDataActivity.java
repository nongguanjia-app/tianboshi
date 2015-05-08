package com.nongguanjia.doctorTian;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.Allcrops;
import com.nongguanjia.doctorTian.bean.Allproduct;
import com.nongguanjia.doctorTian.bean.TuiUserInfo;
import com.nongguanjia.doctorTian.bean.UserInfo;
import com.nongguanjia.doctorTian.http.CustomerHttpClient;
import com.nongguanjia.doctorTian.http.DoctorTianRestClient;
import com.nongguanjia.doctorTian.utils.CommonConstant;

public class MyDataActivity extends Activity implements OnClickListener {
	private ImageView mImage_back,mHead_img;;
	private RelativeLayout mSex, mAge, mPlant, mArea;
	private LinearLayout llNong,llTui;
	private TextView mDataTitle;
	private TextView tvNikcName,tvPhone,tvSex,tvAge,tvPlant,tvArea;
	private TextView tvRegion,tvOffice,tvOfficeStyle,tvProduct,tvDate,tvNongJi;
	private RelativeLayout rlRegion,rlOffice,rlOfficeStyle,rlProduct,rlDate,rlNongJi;
//	private Uri photoUri;
//	private final int PIC_FROM_CAMERA = 1;
//	private final int PIC_FROM＿LOCALPHOTO = 0;
	//拍照
	private int NONE = 0;
	private int PHOTOHRAPH = 1;// 拍照
	private int PHOTOZOOM = 2; // 缩放
	private int PHOTORESOULT = 3;// 结果
	private Uri uri;
	private Bitmap photo;
	private String path;
	private SharedPreferences sp;
	
	private String[] items;
	private boolean[] selected;// 一个存放Boolean值的数组
	private String[] itemsProduct;
	private boolean[] selectedProduct;
	
	
	private String role;
	private String phone;
	private String nickname;
	int gender =1;
	String telephone;
	private UserInfo info;
	private String one="",two="",three="",four="",five="",six="",seven="",nongNamePar;
	private String oneT="",twoT="",threeT="",fourT="",fiveT="",sixT="",nongNameParT;
	private TuiUserInfo tuiUserInfo;
	private AreaBroadcastReceiver broadcastReceiver;
	private List<Allcrops> listAllcrops;//获取农作物名称
	private ArrayList<String> listArea;//获取面积单位
	private UnitsBroadcastReceiver unitsReceiver;
	
	private  Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				Toast.makeText(getApplicationContext(), "成功", 0).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		llNong = (LinearLayout) findViewById(R.id.myInfo_linearLayout_nong);
		llTui = (LinearLayout) findViewById(R.id.myInfo_linearLayout_tui);
		initView();
		
		info = ((AppApplication)this.getApplication()).info;
		phone = ((AppApplication)this.getApplication()).PHONENUM;
		nickname = ((AppApplication)this.getApplicationContext()).NICKNAME;
		tvPhone.setText("手机号："+phone);
		mySetTextInfo(tvNikcName, info.getName(), 0);
		role = ((AppApplication)this.getApplication()).ROLE;
		
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		path = sp.getString("path", "");
		if(!TextUtils.isEmpty(path)){
			mHead_img.setImageBitmap(BitmapFactory.decodeFile(path));
		}
		
		if(role.equals("推广人")){
			getTuiInfo();//获取推广人信息
			llTui.setVisibility(View.VISIBLE);
			llNong.setVisibility(View.GONE);
		}else{
			llNong.setVisibility(View.VISIBLE);
			llTui.setVisibility(View.GONE);
			if(info!=null){
				mySetTextInfo(tvSex, info.getGender(), 0);
				mySetTextInfo(tvAge, info.getAge(), 2);
				mySetTextInfo(tvPlant, info.getCropsId(), 0);
				mySetTextInfo(tvArea, info.getCropsArea()+info.getCropsAreaUnit(), 0);
			}
		}
		
		broadcastReceiver = new AreaBroadcastReceiver();
		registerReceiver(broadcastReceiver, new IntentFilter("TuiAreaQuActivity"));
		
		unitsReceiver = new UnitsBroadcastReceiver();
		registerReceiver(unitsReceiver, new IntentFilter("com.nongguanjia.doctorTian"));
	}

	private void initView(){
		mDataTitle = (TextView) findViewById(R.id.tv_title);
		mDataTitle.setText("我的资料");
		mImage_back = (ImageView) findViewById(R.id.img_back);
		mSex = (RelativeLayout) findViewById(R.id.sex_myInfo);
		mAge = (RelativeLayout) findViewById(R.id.age_myInfo);
		mPlant = (RelativeLayout) findViewById(R.id.plant_myInfo);
		mArea = (RelativeLayout) findViewById(R.id.area);
		mHead_img = (ImageView) findViewById(R.id.my_icon);
		tvNikcName = (TextView) findViewById(R.id.name_myInfo);
		//农户信息
		tvSex = (TextView) findViewById(R.id.myInfo_sex);
		tvAge = (TextView) findViewById(R.id.t_age_myInfo);
		tvArea = (TextView) findViewById(R.id.info_area);
		tvPhone = (TextView) findViewById(R.id.phone_myInfo);
		tvPlant = (TextView) findViewById(R.id.info_plant);
		//推广人
		tvRegion = (TextView) findViewById(R.id.info_region);
		tvOffice = (TextView) findViewById(R.id.info_office);
		tvOfficeStyle = (TextView) findViewById(R.id.info_officeStyle);
		tvProduct = (TextView) findViewById(R.id.info_product);
		tvDate = (TextView) findViewById(R.id.info_time);
		tvNongJi = (TextView) findViewById(R.id.info_nongJi);
		rlRegion = (RelativeLayout) findViewById(R.id.region);
		rlOffice = (RelativeLayout) findViewById(R.id.office);
		rlOfficeStyle = (RelativeLayout) findViewById(R.id.officeStyle);
		rlProduct = (RelativeLayout) findViewById(R.id.product);
		rlDate = (RelativeLayout) findViewById(R.id.time);
		rlNongJi = (RelativeLayout) findViewById(R.id.nongJi);
		
		tvNikcName.setOnClickListener(this);
		
		mHead_img.setOnClickListener(this);
		mImage_back.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mAge.setOnClickListener(this);
		mPlant.setOnClickListener(this);
		mArea.setOnClickListener(this);
		//推广人
		rlRegion.setOnClickListener(this);
		rlOffice.setOnClickListener(this);
		rlOfficeStyle.setOnClickListener(this);
		rlProduct.setOnClickListener(this);
		rlDate.setOnClickListener(this);
		rlNongJi.setOnClickListener(this);
		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.name_myInfo:
			final EditText etName = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("编辑昵称")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(etName)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String name = etName.getText().toString();
							if(TextUtils.isEmpty(name)){
								Toast.makeText(MyDataActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
							}else{
								if(!(TextUtils.isEmpty(name))){
									ChangeInfo(name, info.getName(), info.getAvatar(), info.getAge(), info.getGender(), 
											info.getCropsId(), info.getCropsArea(), info.getCauid());
									info.setName(name);
									tvNikcName.setText(name);
									
									Intent intent = new Intent("com.nongguanjia.doctorTian.name");
									intent.putExtra("name", name);
									sendBroadcast(intent);
								}
							}
						}
					}).show();
			break;
		case R.id.my_icon:
			AlertDialog.Builder builderImg = new AlertDialog.Builder(MyDataActivity.this);
			builderImg.setTitle("选择头像");
			// 指定下拉列表的显示数据
			final String[] photo = { "使用摄像头拍照", "从相册选择" };
			// 设置一个下拉的列表选择项
			builderImg.setItems(photo, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File root = new File(Environment.getExternalStorageDirectory(), "image.jpg");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(root));
						startActivityForResult(intent, PHOTOHRAPH);
						dialog.dismiss();
						break;
					case 1:
						Intent intent1 = new Intent(Intent.ACTION_PICK,null);
						intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent1, PHOTOZOOM);
						dialog.dismiss();
						break;
					}
				}
			}).show();
			//builderImg.show();
			break;	
		case R.id.sex_myInfo:
			AlertDialog.Builder builder = new AlertDialog.Builder(MyDataActivity.this);
			builder.setTitle("选择性别");
			// 指定下拉列表的显示数据
			final String[] cities = { "男", "女" };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvSex.setText(cities[which]);
					if(cities[which] == "男"){
						ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getAge(), "男", 
								info.getCropsId(), info.getCropsArea(), info.getCauid());
						tvSex.setText("男");
						info.setGender("男");
					}else{
						ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getAge(), "女", 
								info.getCropsId(), info.getCropsArea(), info.getCauid());
						tvSex.setText("女");
						info.setGender("女");
					}
				}
			});
			builder.show();
			break;
		case R.id.age_myInfo:
			final EditText editText = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("编辑年龄")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editText)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String age = editText.getText().toString();
							if(!(TextUtils.isEmpty(age))){
								ChangeInfo(nickname, info.getName(), info.getAvatar(), age, info.getGender(), 
										info.getCropsId(), info.getCropsArea(), info.getCauid());
								tvAge.setText(age+" 岁");
								info.setAge(age);
							}
						}
					}).show();
			break;
		case R.id.plant_myInfo:
			getAllcrops();
			final StringBuffer sb = new StringBuffer();
			new AlertDialog.Builder(MyDataActivity.this)
					.setTitle("选择种植作物")
					// 标题
					.setMultiChoiceItems(items, selected,
							new DialogInterface.OnMultiChoiceClickListener() {// 设置多选条目
								public void onClick(DialogInterface dialog, int which, boolean isChecked) {
									
									String temp = items[which]+".";
									if(isChecked){
										sb.append(temp);
									}else{
										sb.delete(sb.indexOf(temp), temp.length());
									}
									String par = sb.toString();
									if(par.length()>0){
										nongNamePar = par.substring(0, par.length()-1);
									}
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getAge(), info.getGender(), 
											nongNamePar, info.getCropsArea(), info.getCauid());
									tvPlant.setText(nongNamePar);
									info.setCropsId(nongNamePar);
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.area:
			//TODO 面积
			Intent intent = new Intent(MyDataActivity.this, AreaActivity.class);
			startActivity(intent);
			break;
		case R.id.region:
			Intent intentArea = new Intent(MyDataActivity.this, TuiAreaActivity.class);
			startActivity(intentArea);
			break;
		case R.id.office:
			final EditText office = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("就业单位")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(office)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String data = office.getText().toString();
							if(tuiUserInfo==null){
								tvOffice.setText(data);
								Editor editor = sp.edit();
								editor.putString("workPlace", data);
								editor.commit();
							}else{
								if(!(TextUtils.isEmpty(data))){
									ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
											tuiUserInfo.getGender(), tuiUserInfo.getAge(),
											tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
											tuiUserInfo.getProducts(),data,
											tuiUserInfo.getBusinessForms(), tuiUserInfo.getWorkYear(), 
											tuiUserInfo.getProfessional());
									tvOffice.setText(data);
									tuiUserInfo.setWorkPlace(data);
								}
							}
						}
					}).show();
			break;
		case R.id.officeStyle:
			AlertDialog.Builder builderStyle = new AlertDialog.Builder(MyDataActivity.this);
			builderStyle.setTitle("公司类型");
			final String[] citiesStyle = { "个体户", "有限公司" };
			builderStyle.setItems(citiesStyle, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tvSex.setText(citiesStyle[which]);
					if(citiesStyle[which] == "个体户"){
						if(tuiUserInfo==null){
							Editor editor = sp.edit();
							editor.putString("BusinessForms", "个体户");
							editor.commit();
						}else{
							ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
									tuiUserInfo.getGender(), tuiUserInfo.getAge(),
									tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
									tuiUserInfo.getProducts(),tuiUserInfo.getWorkPlace(),
									"个体户", tuiUserInfo.getWorkYear(), 
									tuiUserInfo.getProfessional());
							tuiUserInfo.setBusinessForms("个体户");
						}
						tvOfficeStyle.setText("个体户");
					}else{
						if(tuiUserInfo==null){
							Editor editor = sp.edit();
							editor.putString("BusinessForms", "有限公司");
							editor.commit();
						}else{
							ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
									tuiUserInfo.getGender(), tuiUserInfo.getAge(),
									tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
									tuiUserInfo.getProducts(),tuiUserInfo.getWorkPlace(),
									"有限公司", tuiUserInfo.getWorkYear(), 
									tuiUserInfo.getProfessional());
							tuiUserInfo.setBusinessForms("有限公司");
						}
						tvOfficeStyle.setText("有限公司");
					}
				}
			});
			builderStyle.show();
			break;
		case R.id.product:
			getAllproduct();
			final StringBuffer sb1 = new StringBuffer();
			new AlertDialog.Builder(MyDataActivity.this)
			.setTitle("选择种植作物")
			// 标题
			.setMultiChoiceItems(itemsProduct, selectedProduct,
					new DialogInterface.OnMultiChoiceClickListener() {// 设置多选条目
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							
							String temp = itemsProduct[which]+".";
							if(isChecked){
								sb1.append(temp);
							}else{
								sb1.delete(sb1.indexOf(temp), temp.length());
							}
							String par = sb1.toString();
							if(par.length()>0){
								nongNameParT = par.substring(0, par.length()-1);
							}
						}
					})
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if(tuiUserInfo==null){
								Editor editor = sp.edit();
								editor.putString("Products", nongNameParT);
								editor.commit();
							}else{
								ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
										tuiUserInfo.getGender(), tuiUserInfo.getAge(),
										tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
										nongNameParT,tuiUserInfo.getWorkPlace(),
										tuiUserInfo.getBusinessForms(), tuiUserInfo.getWorkYear(), 
										tuiUserInfo.getProfessional());
								tuiUserInfo.setProducts(nongNameParT);
							}
							tvProduct.setText(nongNameParT);
						}
					}).setNegativeButton("取消", null).show();
			break;
		case R.id.time:
			final EditText time = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("从业时间")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(time)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String data = time.getText().toString();
							if(!(TextUtils.isEmpty(data))){
								if(tuiUserInfo==null){
									Editor editor = sp.edit();
									editor.putString("WorkYear", data);
									editor.commit();
								}else{
									ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
											tuiUserInfo.getGender(), tuiUserInfo.getAge(),
											tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
											tuiUserInfo.getProducts(),tuiUserInfo.getWorkPlace(),
											tuiUserInfo.getBusinessForms(), data, 
											tuiUserInfo.getProfessional());
									tuiUserInfo.setWorkYear(data);
								}
								tvDate.setText(data+" 年");
							}
						}
					}).show();
			break;
		case R.id.nongJi:
			final EditText nongJi = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("擅长技术")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(nongJi)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String data = nongJi.getText().toString();
							if(!(TextUtils.isEmpty(data))){
								if(tuiUserInfo==null){
									Editor editor = sp.edit();
									editor.putString("Professional", data);
									editor.commit();
								}else{
									ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
											tuiUserInfo.getGender(), tuiUserInfo.getAge(),
											tuiUserInfo.getProvince(), tuiUserInfo.getCity(), tuiUserInfo.getArea(), 
											tuiUserInfo.getProducts(),tuiUserInfo.getWorkPlace(),
											tuiUserInfo.getBusinessForms(), tuiUserInfo.getWorkYear(), 
											data);
									tuiUserInfo.setProfessional(data);
								}
								tvNongJi.setText(data);
							}
						}
					}).show();
			break;
		}
	}
	/**
	 * 修改个人信息
	 * @param nickname 昵称
	 * @param name 登录名
	 * @param head 头像
	 * @param age 年龄
	 * @param sex 性别
	 * @param nongName 农作物名称
	 * @param area 农作物面积
	 * @param nongId 农作物面积单位ID
	 */
	private void ChangeInfo(String nickname,String name,String head,String age,String sex,String nongName,
			String area,String nongId) {
		String parameter= nickname+","+name+","+head+","+age+","+sex+","+nongName+","+area+","+nongId;
				
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.edituser + "/" + phoneNum + ","+parameter;
		
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(response.getJSONObject("EditUser").getString("returnCode").equals("1")){
						Toast.makeText(MyDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MyDataActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}
	/**
	 * 修改推广人信息 
	 * @param name 登录名
	 * @param pic 图像
	 * @param sex 性别
	 * @param age 年龄
	 * @param sheng 地区省
	 * @param shi 地区市
	 * @param qu 地区区
	 * @param produce 经营产品(如果多个用圆点分隔，不能用逗号)
	 * @param office 所在单位
	 * @param style 经营方式
	 * @param date 经营时间
	 * @param nongJi 擅长农技
	 */
	private void ChangeInfo(String name,String pic,String sex,String age,
			String sheng,String shi,String qu,String produce,String office,String style,String date,String nongJi) {
		String parameter= name+","+pic+","+sex+","+age+","+sheng+","+shi+","+qu+","+produce+","
			+office+","+style+","+date+","+nongJi;
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.editdealer + "/" + phoneNum + ","+parameter;
		
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(response.getJSONObject("EditDealer").getString("returnCode").equals("1")){
						Toast.makeText(MyDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MyDataActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
	/**
	 * 获取推广人信息
	 */
	private void getTuiInfo(){
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.dealers + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if(response.getJSONObject("Dealers").getString("returnCode").equals("1")){
						Gson gson = new Gson();
						tuiUserInfo = new TuiUserInfo();
						tuiUserInfo = gson.fromJson(response.getJSONObject("Dealers").toString(), 
								new TypeToken<TuiUserInfo>(){}.getType());
						
						mySetText(tvRegion, tuiUserInfo.getProvince()+" "+tuiUserInfo.getCity()+" "+tuiUserInfo.getArea(), 0);
						mySetText(tvArea, tuiUserInfo.getProvince(), 0);
						mySetText(tvOffice, tuiUserInfo.getWorkPlace(), 0);
						mySetText(tvOfficeStyle, tuiUserInfo.getBusinessForms(), 0);
						mySetText(tvProduct, tuiUserInfo.getProducts(), 0);
						mySetText(tvDate, tuiUserInfo.getWorkYear(), 1);
						mySetText(tvNongJi, tuiUserInfo.getProfessional(), 0);
					}else{
						if(tuiUserInfo==null){
							mySetText(tvRegion, sp.getString("area", "未设置"), 0);
							mySetText(tvArea, sp.getString("province", "未设置"), 0);
							mySetText(tvOffice, sp.getString("workPlace", "未设置"), 0);
							mySetText(tvOfficeStyle, sp.getString("BusinessForms", "未设置"), 0);
							mySetText(tvProduct, sp.getString("Products", "未设置"), 0);
							mySetText(tvDate, sp.getString("WorkYear", "未设置"), 1);
							mySetText(tvNongJi, sp.getString("Professional", "未设置"), 0);
						}
//						Toast.makeText(MyDataActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
		});
	}
	/**
	 * 获取农作物名称
	 */
	private void getAllcrops(){
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.allcrops + "/" + phoneNum;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getJSONObject("allCrops").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("allCrops").getJSONArray("allCrops");
						Gson gson = new Gson();
						listAllcrops = new ArrayList<Allcrops>();
						listAllcrops = gson.fromJson(ja.toString(), new TypeToken<List<Allcrops>>(){}.getType());
						
						items = new String[listAllcrops.size()];
						selected = new boolean[listAllcrops.size()];
						if(TextUtils.isEmpty(items[0])){
							for (int i = 0; i < listAllcrops.size(); i++) {
								items[i] = listAllcrops.get(i).getName();
								selected[i] = false;
							}
						}
						
					}else{
						Toast.makeText(MyDataActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 获取经营产品信息
	 */
	private void getAllproduct(){
		String url = CommonConstant.allproduct;
		DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getJSONObject("Allproduct").getString("returnCode").equals("1")){
						JSONArray ja = response.getJSONObject("Allproduct").getJSONArray("allproduct");
						Gson gson = new Gson();
						List<Allproduct> listAllproduct = new ArrayList<Allproduct>();
						listAllproduct = gson.fromJson(ja.toString(),new TypeToken<List<Allproduct>>(){}.getType());
						
						itemsProduct = new String [listAllproduct.size()];
						selectedProduct = new boolean [listAllproduct.size()];
						if(TextUtils.isEmpty(itemsProduct[0])){
							for (int i = 0; i < listAllproduct.size(); i++) {
								itemsProduct[i] = listAllproduct.get(i).getName();
								selectedProduct[i] = false;
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * textview设置初始数据
	 * @param tv textview
	 * @param data 数据
	 * @param age 2表示设置年龄  4表示设置面积  0设置其他
	 * @param area 如上
	 */
	private void mySetTextInfo(TextView tv, String data, int age) {
		if (age == 0) {
			if (TextUtils.isEmpty(data)) {
				tv.setText("未设置");
			} else {
				tv.setText(data);
			}
		}else if(age==2){
			if (TextUtils.isEmpty(data)) {
				tv.setText("未设置");
			} else {
				tv.setText(data+" 岁");
			}
		}
	}
	/**
	 * 设置推广人信息
	 * @param tv
	 * @param data
	 * @param year
	 */
	private void mySetText(TextView tv, String data, int year) {
		if (year == 0) {
			if (TextUtils.isEmpty(data)) {
				tv.setText("未设置");
			} else {
				tv.setText(data);
			}
		}else if(year==1){
			if (TextUtils.isEmpty(data)) {
				tv.setText("未设置");
			} else {
				tv.setText(data+" 年");
			}
		}
	}
	
	public void startPhotoZoom(Uri uri){
	 Intent intent = new Intent("com.android.camera.action.CROP");
	 intent.setDataAndType(uri, "image/*");
	 intent.putExtra("crop", "true");
	  intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪图片的宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
	  startActivityForResult(intent, PHOTORESOULT);
}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==NONE) return;
		if(requestCode==PHOTOHRAPH&&(resultCode==RESULT_OK)){
			File photo = new File(Environment.getExternalStorageDirectory()+"/image.jpg");
			startPhotoZoom(Uri.fromFile(photo));
			return;
		}
		if(data==null)	return;
		if(requestCode==PHOTOZOOM){
			uri = data.getData();
			startPhotoZoom(uri);
			return;
		}
		if(requestCode==PHOTORESOULT){
			Bundle extras = data.getExtras();
			if(extras!=null){
				photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				if(photo!=null){
					Bitmap bitmap = getRoundedCornerBitmap(photo);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
					mHead_img.setImageBitmap(bitmap);
				}
				try{
					File root = new File(Environment.getExternalStorageDirectory(),"image.jpg"); 
					FileOutputStream fileOutputStream = new FileOutputStream(root);
					fileOutputStream.write(stream.toByteArray());
					fileOutputStream.flush();
					fileOutputStream.close();
					path = root.getPath();
					
					//TODO 图片上传
					uploadPhoto(root);
					
					Intent intent = new Intent("com.nongguanjia.doctorTian.photo");
					intent.putExtra("path", path);
					sendBroadcast(intent);
					
					sp = getSharedPreferences("config", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("path", path);
					editor.commit();
					
				}catch(Exception e){
					Log.d("文件路径错误", Log.getStackTraceString(e));
				}
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 图片上传
	 * @param file
	 */
	private void uploadPhoto(File file){
		String phoneNum = ((AppApplication)MyDataActivity.this.getApplication()).PHONENUM;
		String url = CommonConstant.fileuploads + "/" + phoneNum;
		RequestParams params = new RequestParams();
		try {
			params.put("img", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		DoctorTianRestClient.post(url, params,  new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Toast.makeText(MyDataActivity.this, "请求接口异常", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					if(response.getJSONObject("FileUploadRequest").getString("returnCode").equals("1")){
						Toast.makeText(MyDataActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * 把剪裁后的图片设置为圆形
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if(bitmap!=null){
			Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(outBitmap);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPX = bitmap.getWidth() / 2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return outBitmap;
		}else{
			return bitmap;
		}
	}
	
	private class AreaBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String sheng = intent.getStringExtra("sheng");
			String shi = intent.getStringExtra("shi");
			String qu = intent.getStringExtra("qu");
			if(!TextUtils.isEmpty(qu)){
				if(tuiUserInfo==null){
					Editor editor = sp.edit();
					editor.putString("area", sheng+" "+shi+" "+qu);
					editor.commit();
				}else{
					ChangeInfo(tuiUserInfo.getLoginName(), tuiUserInfo.getAvatar(), 
							tuiUserInfo.getGender(), tuiUserInfo.getAge(),
							sheng, shi, qu, tuiUserInfo.getProducts(),tuiUserInfo.getWorkPlace(),
							tuiUserInfo.getBusinessForms(), tuiUserInfo.getWorkYear(), 
							tuiUserInfo.getProfessional());
				}
				tvRegion.setText(sheng+" "+shi+" "+qu);
			}
		}
	}

	private class UnitsBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String mj = arg1.getStringExtra("number");
			String dan = arg1.getStringExtra("dan");
			String danName = arg1.getStringExtra("danName");
			
			ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getGender(), info.getGender(), 
					info.getCropsId(), mj, dan);
			tvArea.setText(mj+danName);
			info.setCropsArea(mj);
			info.setCropsAreaUnit(danName);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
		unregisterReceiver(unitsReceiver);
	}
}