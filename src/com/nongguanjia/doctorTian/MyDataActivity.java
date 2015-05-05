package com.nongguanjia.doctorTian;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.Header;
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
import com.nongguanjia.doctorTian.app.AppApplication;
import com.nongguanjia.doctorTian.bean.TuiUserInfo;
import com.nongguanjia.doctorTian.bean.UserInfo;
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
	
	final String[] items = { "小麦", "水稻", "大豆", "芝麻", "玉米", "食用菌", "蔬菜" };
	final boolean[] selected = new boolean[] { false, false, false,false,false,false,false };// 一个存放Boolean值的数组
	final String[] itemsProduct = { "拖拉机", "种子", "化肥", "农具", "收割机", "插秧机" };
	final boolean[] selectedProduct = new boolean[] { false, false, false,false,false,false};
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		llNong = (LinearLayout) findViewById(R.id.myInfo_linearLayout_nong);
		llTui = (LinearLayout) findViewById(R.id.myInfo_linearLayout_tui);
		initView();
		
		info = ((AppApplication)this.getApplication()).info;
		phone = ((AppApplication)this.getApplication()).PHONENUM;
		nickname = ((AppApplication)this.getApplicationContext()).NICKNAME;
		tvPhone.setText("手机号："+phone);
		mySetText(tvNikcName, info.getName(), 0, 0);
		
		role = ((AppApplication)this.getApplication()).ROLE;
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		path = sp.getString("path", "");
		if(!TextUtils.isEmpty(path)){
			mHead_img.setImageBitmap(BitmapFactory.decodeFile(path));
		}
		
		if(role.equals("推广人")){
			getTuiInfo();//获取推广人信息
			if(tuiUserInfo==null){
				mySetText(tvRegion, sp.getString("area", "未设置"), 0);
				mySetText(tvArea, sp.getString("province", "未设置"), 0);
				mySetText(tvOffice, sp.getString("workPlace", "未设置"), 0);
				mySetText(tvOfficeStyle, sp.getString("BusinessForms", "未设置"), 0);
				mySetText(tvProduct, sp.getString("Products", "未设置"), 0);
				mySetText(tvDate, sp.getString("WorkYear", "未设置"), 1);
				mySetText(tvNongJi, sp.getString("Professional", "未设置"), 0);
			}
			llTui.setVisibility(View.VISIBLE);
			llNong.setVisibility(View.GONE);
		}else{
			llNong.setVisibility(View.VISIBLE);
			llTui.setVisibility(View.GONE);
			if(info!=null){
				mySetText(tvSex, info.getGender(), 0, 0);
				mySetText(tvAge, info.getAge(), 2, 0);
				mySetText(tvPlant, info.getCropsId(), 0, 0);
				mySetText(tvArea, info.getCropsArea(), 0, 4);
			}
		}
		
		broadcastReceiver = new AreaBroadcastReceiver();
		registerReceiver(broadcastReceiver, new IntentFilter("TuiAreaQuActivity"));
		
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
											info.getCropsId(), info.getCropsArea(), info.getCropsAreaUnit());
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
								info.getCropsId(), info.getCropsArea(), info.getCropsAreaUnit());
						tvSex.setText("男");
						info.setGender("男");
					}else{
						ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getAge(), "女", 
								info.getCropsId(), info.getCropsArea(), info.getCropsAreaUnit());
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
										info.getCropsId(), info.getCropsArea(), info.getCropsAreaUnit());
								tvAge.setText(age+" 岁");
								info.setAge(age);
							}
						}
					}).show();
			break;
		case R.id.plant_myInfo:
			new AlertDialog.Builder(MyDataActivity.this)
					.setTitle("选择种植作物")
					// 标题
					.setMultiChoiceItems(items, selected,
							new DialogInterface.OnMultiChoiceClickListener() {// 设置多选条目
								public void onClick(DialogInterface dialog, int which, boolean isChecked) {
									switch (which) {
									case 0:
										if (isChecked) {
											one = "小麦.";
										} else {
											one = "";
										}
										break;
									case 1:
										if (isChecked) {
											two = "水稻.";
										} else {
											two = "";
										}
										break;
									case 2:
										if (isChecked) {
											three = "大豆.";
										} else {
											three = "";
										}
										break;
									case 3:
										if (isChecked) {
											four = "芝麻.";
										} else {
											four = "";
										}
										break;
									case 4:
										if (isChecked) {
											five = "玉米.";
										} else {
											five = "";
										}
										break;
									case 5:
										if (isChecked) {
											six = "食用菌.";
										} else {
											six = "";
										}
										break;
									case 6:
										if (isChecked) {
											seven = "蔬菜.";
										} else {
											seven = "";
										}
										break;
									}
									String par = one+two+three+four+five+six+seven;
									if(par.length()>0){
										nongNamePar = par.substring(0, par.length()-1);
									}
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getAge(), info.getGender(), 
											nongNamePar, info.getCropsArea(), info.getCropsAreaUnit());
									tvPlant.setText(nongNamePar);
									info.setCropsId(nongNamePar);
								}
							}).setNegativeButton("取消", null).show();
			break;
		case R.id.area:
			final EditText area = new EditText(MyDataActivity.this);
			new AlertDialog.Builder(MyDataActivity.this).setTitle("请输入种植面积")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(area)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							String mj = area.getText().toString();
							if(!(TextUtils.isEmpty(mj))){
								ChangeInfo(nickname, info.getName(), info.getAvatar(), info.getGender(), info.getGender(), 
										info.getCropsId(), mj, info.getCropsAreaUnit());
								tvArea.setText(mj+" 亩");
								info.setCropsArea(mj);
							}
						}
					}).show();
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
			new AlertDialog.Builder(MyDataActivity.this)
			.setTitle("选择种植作物")
			// 标题
			.setMultiChoiceItems(itemsProduct, selectedProduct,
					new DialogInterface.OnMultiChoiceClickListener() {// 设置多选条目
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							switch (which) {
							case 0:
								if (isChecked) {
									oneT = "拖拉机.";
								} else {
									oneT = "";
								}
								break;
							case 1:
								if (isChecked) {
									twoT = "种子.";
								} else {
									twoT = "";
								}
								break;
							case 2:
								if (isChecked) {
									threeT = "化肥.";
								} else {
									threeT = "";
								}
								break;
							case 3:
								if (isChecked) {
									fourT = "农具.";
								} else {
									fourT = "";
								}
								break;
							case 4:
								if (isChecked) {
									fiveT = "收割机.";
								} else {
									fiveT = "";
								}
								break;
							case 5:
								if (isChecked) {
									sixT = "插秧机.";
								} else {
									sixT = "";
								}
								break;
							}
							String par = oneT+twoT+threeT+fourT+fiveT+sixT;
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
	 * textview设置初始数据
	 * @param tv textview
	 * @param data 数据
	 * @param age 2表示设置年龄  4表示设置面积  0设置其他
	 * @param area 如上
	 */
	private void mySetText(TextView tv, String data, int age, int area) {
		if (age == 0 && area == 0) {
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
		}else if(area==4){
			if (TextUtils.isEmpty(data)) {
				tv.setText("未设置");
			} else {
				tv.setText(data+" 亩");
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
					
					Intent intent = new Intent("com.nongguanjia.doctorTian.photo");
					intent.putExtra("path", path);
					sendBroadcast(intent);
					
					sp = getSharedPreferences("config", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("path", path);
					editor.commit();
//					String phone = ((AppApplication)getApplication()).PHONENUM;
//					String url =DoctorTianRestClient.BASE_URL + phone +"," +"image.jpg";
//					CustomerHttpClient.photoUpload(url, path, "image.jpg");
//					File file = new File(path);
//					CustomerHttpClient.uploadFile(file, url);
//					CustomerHttpClient.uploadpic(path, handler, url, pictoken, 1);
				}catch(Exception e){
					Log.d("文件路径错误", Log.getStackTraceString(e));
				}
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}