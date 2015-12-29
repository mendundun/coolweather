package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();//HTTP请求方式之一，必须先new一个URL对象，并传入目标的网络地址，然后调用一下openConnection()方法
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));//使用InputStreamReader将in转换为字符
					StringBuilder response = new StringBuilder();//StringBuilder是一个可变的字符序列。此类提供一个与 StringBuffer 兼容的 API，但不保证同步。该类被设计用作 StringBuffer 的一个简易替换，用在字符串缓冲区被单个线程使用的时候
					String line;
					while ((line = reader.readLine())!=null) {
						response.append(line);
					}
					if (listener!=null) {
						//回调onFinish方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener!=null) {
						//回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection!=null){
						connection.disconnect();
					}
				}
			}
			
		}).start();
	}

}
