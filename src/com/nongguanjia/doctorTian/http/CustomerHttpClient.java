package com.nongguanjia.doctorTian.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.util.Log;

public class CustomerHttpClient {

	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient customerHttpClient;

	private static final Charset strCharSet = Charset
			.forName(org.apache.http.protocol.HTTP.UTF_8);

	private static final String TAG = "CustomerHttpClient";
	
//	private static final String BASE_URL = "http://182.92.170.172/tianboshi/"; 
	private static final String BASE_URL = "http://192.168.1.116:8080/demo/tbs/"; 

	private CustomerHttpClient() {
	}

	//单例模式
	public static synchronized HttpClient getHttpClient() {
		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params,
							"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
							+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 20 * 1000);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 50 * 1000);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	
	
	// POST 请求 发送文本
	public static String post(String url, String params) {
		try {
			// 创建POST请求
			HttpPost request = new HttpPost(BASE_URL + url);
			request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			
			request.setEntity(new StringEntity(params, CHARSET));
			// 发送请求
			HttpClient client = getHttpClient();
			
			HttpResponse response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			
			return (resEntity == null) ? null : EntityUtils.toString(resEntity,
					CHARSET);
		} catch (UnsupportedEncodingException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (ClientProtocolException e) {
			Log.w(TAG, e.getMessage());
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
	
//	/**
//	 * 图片上传方法
//	 * @param picpath 图片路径
//	 * @param handler 
//	 * @param httppath utl路径
//	 * @param pictoken 参数 值
//	 * @param index    handler  code值
//	 */
//	public static void uploadpic(final String picpath, final Handler handler,
//			final String httppath, final String pictoken, final int index) {
//		new Thread() {
//			public void run() {
//				try {
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpPost httppost = new HttpPost(httppath);
//					MultipartEntity reqEntity = new MultipartEntity();
//
//					FileBody file = new FileBody(new File(picpath));
//					reqEntity.addPart("img", file);//参数phone文件路径
//
//					StringBody descript = new StringBody(pictoken);//key对应的 值
//					reqEntity.addPart("img", descript);//参数token
//					
//					httppost.setEntity(reqEntity);
//					httpclient.execute(httppost);
//					handler.sendMessage(handler.obtainMessage(index, "发送成功"));
//					return;
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				} catch (ClientProtocolException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				handler.sendEmptyMessage(-1);
//			};
//		}.start();
//	}
	
	public static int uploadFile(File file, String RequestURL) {
		final String TAG = "uploadFile";
	    final int TIME_OUT = 10 * 1000; // 超时时间
	    final String CHARSET = "utf-8"; // 设置编码
        int res=0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
 
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空时执行上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名
                 */
                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                 res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.e(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    Log.e(TAG, "result :------------------------- " + result);
                } else {
                    Log.e(TAG, "request error");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
	

}
