package com.example.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

public class HttpUtils {
	private static String sessionid = null;
//	static String bigtype = new String();
	public static String getJsonContent(String urlPath, ExceptionHandler handler, JSONObject params) {
		
		// 创建一个Post请求参数对象，所有需要通过Post方法发送到服务端的请求头和请求体的参数都设置该对象中
		HttpPost httpPost = new HttpPost(urlPath);

		/* 设置请求参数：超时时间 */
		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5 * 1000);

//		if (sessionid != null) {
//			httpPost.setHeader("cookie", "JSESSIONID=" + sessionid);
//		}
		//httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		httpPost.setParams(httpParameters);
//		try {
//			bigtype = new String(params.toString().getBytes("iso-8859-1"),"utf-8");
//		} catch (UnsupportedEncodingException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		// 讲验证码，用户名和密码以JSON格式发送给服务端
		try {
			//JSONObject params = new JSONObject();
			//params.put("name", " ");
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			BasicNameValuePair se = new BasicNameValuePair("params",
					params.toString());
			nvps.add(se);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			handler.showMessage(e1.toString());
		}

		// 创建一个客户端请求发送器f
		HttpClient client = new DefaultHttpClient();

		// 发送Get请求，并等待服务端的响应
		try {
			HttpResponse response = client.execute(httpPost);
			
			String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
			return responseBody;

		} catch (ClientProtocolException e) {
			// 服务端如果不支持标准Http协议，则出现这个异常
			handler.showMessage(e.getMessage());
		} catch (IOException e) {
			// 网络通讯发生故障，则出现这个异常
			handler.showMessage("无法连接远程服务器");
		}
//		try {
//
//			URL url = new URL(urlPath);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setConnectTimeout(5 * 1000);
//			connection.setRequestMethod("GET");
//			connection.setDoInput(true);
//			int code = connection.getResponseCode();
//			if (code == 200) {
//				return changeInputString(connection.getInputStream());
//			}
//		} catch (ClientProtocolException e) {
//			// 服务端如果不支持标准Http协议，则出现这个异常
//			handler.showMessage(e.getMessage());
//		} catch (IOException e) {
//			// 网络通讯发生故障，则出现这个异常
//			handler.showMessage("无法连接远程服务器");
//		}
		return " ";
	}

}
