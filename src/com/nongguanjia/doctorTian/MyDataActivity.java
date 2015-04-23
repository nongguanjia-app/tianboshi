package com.nongguanjia.doctorTian;

import java.io.File;
import java.io.FileNotFoundException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyDataActivity extends Activity implements OnClickListener {
	private ImageView mImage_back,mHead_img;;
	private RelativeLayout mSex, mAge, mPlant, mArea;
	private TextView mDataTitle;
	private TextView info_sex;
	private Uri photoUri;
	private final int PIC_FROM_CAMERA = 1;
	private final int PIC_FROM＿LOCALPHOTO = 0;
	final String[] items = { "小麦", "水稻", "大豆", "芝麻", "玉米", "食用菌", "蔬菜" };
	final boolean[] selected = new boolean[] { true, false, true,false,true,false,true };// 一个存放Boolean值的数组
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		mDataTitle = (TextView) findViewById(R.id.tv_title);
		mDataTitle.setText("我的资料");
		mImage_back = (ImageView) findViewById(R.id.img_back);
		mSex = (RelativeLayout) findViewById(R.id.sex);
		mAge = (RelativeLayout) findViewById(R.id.age);
		mPlant = (RelativeLayout) findViewById(R.id.plant);
		mArea = (RelativeLayout) findViewById(R.id.area);
		mHead_img = (ImageView) findViewById(R.id.my_icon);
		info_sex = (TextView) findViewById(R.id.info_sex);
		mHead_img.setOnClickListener(this);
		mImage_back.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mAge.setOnClickListener(this);
		mPlant.setOnClickListener(this);
		mArea.setOnClickListener(this);
	}

	int gender =1;
	String telephone;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			finish();
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
					//Toast.makeText(MyDataActivity.this,"您选择的头像为：" + photo[which], Toast.LENGTH_SHORT).show();
					switch (which) {
					case 0:
						doHandlerPhoto(PIC_FROM_CAMERA);// 用户点击了从照相机获取
						break;
					case 1:
						doHandlerPhoto(PIC_FROM＿LOCALPHOTO);// 从相册中去获取
						break;	
					default:
						break;
					}
				}
			}).show();
			//builderImg.show();
			break;	
		case R.id.sex:
			AlertDialog.Builder builder = new AlertDialog.Builder(MyDataActivity.this);
			builder.setTitle("选择性别");
			// 指定下拉列表的显示数据
			final String[] cities = { "男", "女" };
			// 设置一个下拉的列表选择项
			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MyDataActivity.this, "您选择的性别为：" + cities[which], Toast.LENGTH_SHORT).show();
					info_sex.setText(cities[which]);
					if(cities[which] == "男"){
						gender = 1;
					}
//					String url = "edituser/"+telephone+",name,loginname,test,25,"+gender+",水稻.小麦,1000,1";
//					DoctorTianRestClient.get(url, null, new JsonHttpResponseHandler() {
//						
//						@Override
//						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//							// TODO Auto-generated method stub
//							super.onFailure(statusCode, headers, responseString, throwable);
//						}
//						
//						@Override
//						public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//							super.onSuccess(statusCode, headers, response);
//							
//						}
//					});
				}
			});
			builder.show();
			break;
		case R.id.age:
			new AlertDialog.Builder(MyDataActivity.this).setTitle("编辑年龄")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(new EditText(MyDataActivity.this))
					.setNegativeButton("取消", null)
					.setPositiveButton("确定", null).show();

			break;
		case R.id.plant:
			new AlertDialog.Builder(MyDataActivity.this)
					.setTitle("选择种植作物")
					// 标题
					.setMultiChoiceItems(items, selected,
							new DialogInterface.OnMultiChoiceClickListener() {// 设置多选条目
								public void onClick(DialogInterface dialog, int which, boolean isChecked) {
									// do something
								}
							})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// do something
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// do something
								}
							}).show();
			break;
		case R.id.area:
			Intent Intent_Area = new Intent(MyDataActivity.this,AreaActivity.class);
			startActivity(Intent_Area);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 根据不同方式选择图片设置ImageView
	 * @param type 0-本地相册选择，非0为拍照
	 */
	private void doHandlerPhoto(int type)
	{
		try
		{
			//保存裁剪后的图片文件
			File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/upload");
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			File picFile = new File(pictureFileDir, "upload.jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			
			if (type==PIC_FROM＿LOCALPHOTO)
			{
				Intent intent = getCropImageIntent();
				startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
			}else
			{
				Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
			}

		} catch (Exception e)
		{
			Log.i("HandlerPicError", "处理图片出现错误");
		}
	}
	
	/**
	 * 调用图片剪辑程序
	 */
	public Intent getCropImageIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		setIntentParams(intent);
		return intent;
	}
	
	/**
	 * 启动裁剪
	 */
	private void cropImageUriByTakePhoto() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		setIntentParams(intent);
		startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
	}
	
	/**
	 * 设置公用参数
	 */
	private void setIntentParams(Intent intent)
	{
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case PIC_FROM_CAMERA: // 拍照
			try 
			{
				cropImageUriByTakePhoto();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			break;
		case PIC_FROM＿LOCALPHOTO:
			try
			{
				if (photoUri != null) 
				{
					Bitmap bitmap = decodeUriAsBitmap(photoUri);
					mHead_img.setImageBitmap(bitmap);
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			break;
		}
	}
	
	private Bitmap decodeUriAsBitmap(Uri uri)
	{
		Bitmap bitmap = null;
		try 
		{
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
}