package com.nongguanjia.doctorTian.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class AppApplication extends Application {
	public static final String DEFAULT_APPKEY = "4637ba04-1792-486f-a87e-cfd077707244";
	public static String APPKEY = DEFAULT_APPKEY;
	public static String IP = null;
	public static int PORT = -1;
	private static SharedPreferences spf;
	private static final String SHARE_PREFERENCE_NAME = "gotye_config";
	
	public String ROLE = "农户";
	public String PHONENUM;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		loadSelectedKey(this);
		initImageLoader(getApplicationContext());
		
		spf = getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub

		super.onTerminate();
	}

	
	// 初始化ImageLoader、FIFO算法、LRU算法
	public static void initImageLoader(Context context) {// http://www.cnblogs.com/Jerseyblog/p/3731862.html
		File cacheDir = StorageUtils
				.getOwnCacheDirectory(context, "nongguanji");// 获取到缓存的目录地址
		Log.d("cacheDir", cacheDir.getPath());
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// .memoryCacheExtraOptions(480, 800) // max width, max
				// height，即保存的每个缓存文件的最大长宽
				// .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,
				// null) // Can slow ImageLoader, use it carefully (Better don't
				// use it)设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024)) // You can pass your own memory cache
				// implementation你可以通过自己的内存缓存实现
				// .memoryCacheSize(2 * 1024 * 1024)
				// /.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				// .discCacheFileNameGenerator(new
				// HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100) // 缓存的File数量
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// .imageDownloader(new BaseImageDownloader(context, 5 * 1000,
				// 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

	
	
	
	public static void loadSelectedKey(Context context) {
		SharedPreferences spf = context.getSharedPreferences("gotye_api",
				Context.MODE_PRIVATE);
		APPKEY = spf.getString("selected_key", DEFAULT_APPKEY);
		String ip_port = spf.getString("selected_ip_port", null);
		if (!TextUtils.isEmpty(ip_port)) {
			String[] ipPort = ip_port.split(":");
			if (ipPort != null && ipPort.length >= 2) {
				try {
					int port = Integer.parseInt(ipPort[1]);
					IP = ipPort[0];
					PORT = port;
				} catch (Exception e) {

				}

			}
		}

	}
	

}
