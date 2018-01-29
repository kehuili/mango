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
		
		// ����һ��Post�����������������Ҫͨ��Post�������͵�����˵�����ͷ��������Ĳ��������øö�����
		HttpPost httpPost = new HttpPost(urlPath);

		/* ���������������ʱʱ�� */
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
		// ����֤�룬�û�����������JSON��ʽ���͸������
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

		// ����һ���ͻ�����������f
		HttpClient client = new DefaultHttpClient();

		// ����Get���󣬲��ȴ�����˵���Ӧ
		try {
			HttpResponse response = client.execute(httpPost);
			
			String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
			return responseBody;

		} catch (ClientProtocolException e) {
			// ����������֧�ֱ�׼HttpЭ�飬���������쳣
			handler.showMessage(e.getMessage());
		} catch (IOException e) {
			// ����ͨѶ�������ϣ����������쳣
			handler.showMessage("�޷�����Զ�̷�����");
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
//			// ����������֧�ֱ�׼HttpЭ�飬���������쳣
//			handler.showMessage(e.getMessage());
//		} catch (IOException e) {
//			// ����ͨѶ�������ϣ����������쳣
//			handler.showMessage("�޷�����Զ�̷�����");
//		}
		return " ";
	}

}
