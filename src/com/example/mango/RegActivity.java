package com.example.mango;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.client.utils.NetworkUtils;

public class RegActivity extends Activity {
	
	private String sessionid = null;
	private RegHandler handler = new RegHandler();
	private String codeString = null;
	private EditText txtUser = null;
	private EditText txtPwd = null;
	private EditText txtCode = null;
	private EditText txtName = null;
	private ImageView ivCode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        txtUser = (EditText)findViewById(R.id.regId);
        txtPwd = (EditText)findViewById(R.id.regpwd);
        txtCode = (EditText)findViewById(R.id.regcode);
        txtName = (EditText)findViewById(R.id.regname);
        
      //注册按钮的获取与监听事件设置
        Button sub = (Button) findViewById(R.id.reg_sub);
        sub.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View source)
        	{
        		String str_spwd,str_pwd;
        		EditText regPwd = (EditText) findViewById(R.id.regpwd);
        		str_pwd = regPwd.getText().toString();
        		EditText regSpwd = (EditText) findViewById(R.id.regspwd);
        		str_spwd = regSpwd.getText().toString();
        		if(str_pwd.equals(str_spwd)){
        			RegisterThread thread = new RegisterThread();
    				thread.start();
        		}else{
        			Toast.makeText(RegActivity.this,"两次输入的密码不一致!",Toast.LENGTH_SHORT).show();
        		}
        	}
        });
        //取消按钮的获取与监听事件设置
        Button cancel = (Button) findViewById(R.id.reg_cancel);
        cancel.setOnClickListener(new OnClickListener()
        {
        	
        	public void onClick(View source)
        	{
        		Intent intent = new Intent(RegActivity.this,LoginActivity.class);
        		startActivity(intent);
        		
        	}
        });
        
      //验证码图片控制点击事件，刷新验证码
        ivCode = (ImageView) findViewById(R.id.reg_code);
		ivCode.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ImageThread thread = new ImageThread();
				thread.start();
			}
		});
    }
    class RegisterThread extends Thread {

		@Override
		public void run() {

			// 创建一个Post请求参数对象，所有需要通过Post方法发送到服务端的请求头和请求体的参数都设置该对象中
			HttpPost httpPost = new HttpPost(NetworkUtils.DANGDANG_REG_URL);

			/* 设置请求参数：超时时间 */
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 100000);
			// 设置sessionid，保存服务端session空间有效
			if (sessionid != null) {
				httpPost.setHeader("cookie", "JSESSIONID=" + sessionid);
			}
			httpPost.setParams(httpParameters);

			// 讲验证码，用户名和密码以JSON格式发送给服务端
			try {
				JSONObject params = new JSONObject();
				//params.put("name", txtRegname.getText().toString());
				params.put("uid", txtUser.getText().toString());
				params.put("pwd", txtPwd.getText().toString());
				params.put("code", txtCode.getText().toString());
				params.put("regname", txtName.getText().toString());
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				BasicNameValuePair se = new BasicNameValuePair("params",
						params.toString());
				nvps.add(se);
				httpPost.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				showMessage(e1.toString());
			} catch (JSONException e1) {
				showMessage("信息获取错误！");
			}

			// 创建一个客户端请求发送器
			HttpClient client = new DefaultHttpClient();

			// 发送Get请求，并等待服务端的响应
			try {
				HttpResponse response = client.execute(httpPost);

				// 返回所有响应头信息
				// response.getAllHeaders();
				// 返回响应体，不显示响应头
				String responseBody = EntityUtils
						.toString(response.getEntity());

				JSONObject jsons = new JSONObject(responseBody);

				// 验证服务端是否有异常
				if (responseBody.contains(",msg=")) {
					showMessage(jsons.getString("msg"));
				} else {
					boolean isOk = jsons.getBoolean("isOk");
					if (isOk) {
						showMessage("注册成功");
						Intent intent = new Intent(RegActivity.this,LoginActivity.class);
	            		startActivity(intent);
					} else {
						showMessage(jsons.getString("errorinfo"));
					}
				}

			} catch (ClientProtocolException e) {
				// 服务端如果不支持标准Http协议，则出现这个异常
				showMessage(e.getMessage());
			} catch (IOException e) {
				// 网络通讯发生故障，则出现这个异常
				showMessage("无法连接远程服务器");
			} catch (JSONException e) {
				// 如果返回的数据格式不满足JSON的格式，则出现这个异常
				showMessage("返回参数错误，注册失败！");
			}

		}
	}
    private void showMessage(String message) {
		Message msg = Message
				.obtain(handler, RegHandler.SHOW_MESSAGE);
		msg.obj = message;
		msg.sendToTarget();
		if (!message.contains("无法连接远程服务器")) {
			ImageThread thread = new ImageThread();
			thread.start();
		}
	}    
    @SuppressLint("HandlerLeak") 
	class RegHandler extends Handler {

		
		// 更新验证码
				public static final int SHOW_NETWORK_IMAGE = 0x0001;
				// 显示错误信息
				public static final int SHOW_MESSAGE = 0x0002;

				@Override
				public void handleMessage(Message msg) {
					if (msg.what == SHOW_NETWORK_IMAGE) {
						Bitmap bitmap = (Bitmap) msg.obj;
						ivCode.setImageBitmap(bitmap);
						txtCode.setText(codeString);
					}
					if (msg.what == SHOW_MESSAGE) {
						Toast.makeText(RegActivity.this, msg.obj.toString(),
								Toast.LENGTH_LONG).show();
					}
				}
	}

    class ImageThread extends Thread {

		@Override
		public void run() {

			// 创建一个Post请求参数对象，所有需要通过Post方法发送到服务端的请求头和请求体的参数都设置该对象中
			HttpPost httpPost = new HttpPost(NetworkUtils.DANGDANG_CODE_URL);

			/* 设置请求参数：超时时间 */
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5 * 1000);
			// 设置sessionid，保存服务端session空间有效,初次发送请求无需设置
			if (sessionid != null) {
				httpPost.setHeader("cookie", "JSESSIONID=" + sessionid);
			}
			httpPost.setParams(httpParameters);

			// 创建一个客户端请求发送器
			HttpClient client = new DefaultHttpClient();
			// 发送Get请求，并等待服务端的响应
			try {
				HttpResponse response = client.execute(httpPost);

				// 获得SessionID，并保存，为提交数据做准备
				Header[] header = response.getHeaders("Set-Cookie");
				if (header.length > 0) {
					String temp = header[0].getValue().toString();
					sessionid = temp
							.substring(temp.indexOf("JSESSIONID=") + 11,
									temp.indexOf(";"));
					Log.d("sessionid", sessionid);
				}

				InputStream is = response.getEntity().getContent();
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				is.close();
				showImage(bitmap);

			} catch (ClientProtocolException e) {
				// 服务端如果不支持标准Http协议，则出现这个异常
				showMessage(e.getMessage());
			} catch (IOException e) {
				// 网络通讯发生故障，则出现这个异常
				showMessage("无法连接远程服务器");
			}

		}
	

		/*
		 * 通过handler对象将验证码图片发送给主线程并提醒其更新主界面验证码
		 */
		private void showImage(Bitmap bitmap) {

			// 显示图片的操作不能出现在子线程中，需要由Handler来完成
			// ivImage.setImageBitmap(bitmap);
			Message msg = Message.obtain(handler,
					RegHandler.SHOW_NETWORK_IMAGE);
			msg.obj = bitmap;
			msg.sendToTarget();
		}
}
}
