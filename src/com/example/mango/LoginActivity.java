package com.example.mango;

import java.io.File;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends Activity {
	
	final String FILE = "share.xml";
	private LoginDangDangHandler handler = new LoginDangDangHandler();
	private Button login,register = null;
	
	private String sessionid = null;
	private String codeString = null;
	private ImageView ivCode = null;
	
//	SharedPreferences custId;
//	SharedPreferences.Editor editor;
	private Button btnLogin = null;
	private EditText txtUser = null;
	private EditText txtPwd = null;
	private EditText txtCode = null;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d("mainactivity","onCreate");
        ivCode = (ImageView) findViewById(R.id.login_code);
        ivCode.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				ImageThread thread = new ImageThread();
				thread.start();
			}
		});
        
        txtUser = (EditText) findViewById(R.id.editId);
		txtPwd = (EditText) findViewById(R.id.editPwd);
		txtUser.setText("zhaoyun");
		txtPwd.setText("zhaoyun");
		txtCode = (EditText) findViewById(R.id.logincode);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View source)
        	{
        		LoginThread thread = new LoginThread();
				thread.start();
//        		Toast.makeText(LoginActivity.this,"Correct!",Toast.LENGTH_SHORT).show();
//				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//        		startActivity(intent);
//        		finish();
        		
        	}
        });
        register = (Button) findViewById(R.id.reg);
        register.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View source)
        	{
        		Intent intent = new Intent(LoginActivity.this,RegActivity.class);
        		startActivity(intent);
        		
        	}
        });
//        File file = new File(FILE);
//        if (!file.exists()) {  
//            try {  
//                //����ָ����·�������ļ���  
//                file.mkdirs();  
//            } catch (Exception e) {  
//                // TODO: handle exception  
//            }  
//        }  
//        custId = getSharedPreferences("custId",MODE_PRIVATE);
//        editor = custId.edit();
        
        ImageThread thread = new ImageThread();
		thread.start();
    }
    /**
     * A placeholder fragment containing a simple view.
     */
  
    /*
	 * ��½�̣߳������˷��͵�½��֤������߳�
	 */
	class LoginThread extends Thread {

		@Override
		public void run() {

			// ����һ��Post�����������������Ҫͨ��Post�������͵�����˵�����ͷ��������Ĳ��������øö�����
			HttpPost httpPost = new HttpPost(NetworkUtils.DANGDANG_LOGIN_URL);

			/* ���������������ʱʱ�� */
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5 * 1000);
			// ����sessionid����������session�ռ���Ч
			if (sessionid != null) {
				httpPost.setHeader("cookie", "JSESSIONID=" + sessionid);
			}
			httpPost.setParams(httpParameters);

			// ����֤�룬�û�����������JSON��ʽ���͸������
			try {
				JSONObject params = new JSONObject();
				params.put("code", txtCode.getText().toString());
				params.put("uid", txtUser.getText().toString());
				params.put("pwd", txtPwd.getText().toString());
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				BasicNameValuePair se = new BasicNameValuePair("params",
						params.toString());
				nvps.add(se);
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (UnsupportedEncodingException e1) {
				showMessage(e1.toString());
			} catch (JSONException e1) {
				showMessage("��Ϣ��ȡ����");
			}

			// ����һ���ͻ�����������
			HttpClient client = new DefaultHttpClient();

			// ����Get���󣬲��ȴ�����˵���Ӧ
			try {
				HttpResponse response = client.execute(httpPost);

				// ����������Ӧͷ��Ϣ
				// response.getAllHeaders();
				// ������Ӧ�壬����ʾ��Ӧͷ
				String responseBody = EntityUtils
						.toString(response.getEntity());

				JSONObject jsons = new JSONObject(responseBody);

				// ��֤������Ƿ����쳣
				if (responseBody.contains(",msg=")) {
					showMessage(jsons.getString("msg"));
				} else {
					// ��֤����˷������ݣ��Ƿ��½�ɹ�
					boolean isOk = jsons.getBoolean("isOk");
					if (isOk) {
						showMessage("��½�ɹ�");
//						editor.putInt("cusid",jsons.getInt("cusid"));
//						editor.commit();
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						intent.putExtra("cusid", jsons.getInt("cusid"));
		        		startActivity(intent);
		        		finish();
					} else {
						showMessage(jsons.getString("errorinfo"));
					}
				}

			} catch (ClientProtocolException e) {
				// ����������֧�ֱ�׼HttpЭ�飬���������쳣
				showMessage(e.getMessage());
			} catch (IOException e) {
				// ����ͨѶ�������ϣ����������쳣
				showMessage("�޷�����Զ�̷�����");
			} catch (JSONException e) {
				// ������ص����ݸ�ʽ������JSON�ĸ�ʽ�����������쳣
				showMessage("���ز������󣬵�¼ʧ�ܣ�");
			}

		}
	}

	/*
	 * �����½��������ñ����� ͨ��handler���󣬽�������ʾ��Ϣ���͵����̣߳�����ʾ֪ͨ�û� ���ǵ�ˢ����֤��
	 */
	private void showMessage(String message) {
		Message msg = Message
				.obtain(handler, LoginDangDangHandler.SHOW_MESSAGE);
		msg.obj = message;
		msg.sendToTarget();
		if (!message.contains("�޷�����Զ�̷�����")) {
			ImageThread thread = new ImageThread();
			thread.start();
		}
	}

	@SuppressLint("HandlerLeak")
	class LoginDangDangHandler extends Handler {

		// ������֤��
		public static final int SHOW_NETWORK_IMAGE = 0x0001;
		// ��ʾ������Ϣ
		public static final int SHOW_MESSAGE = 0x0002;

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SHOW_NETWORK_IMAGE) {
				Bitmap bitmap = (Bitmap) msg.obj;
				ivCode.setImageBitmap(bitmap);
				txtCode.setText(codeString);
			}
			if (msg.what == SHOW_MESSAGE) {
				Toast.makeText(LoginActivity.this, msg.obj.toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/*
	 * �����˷��ͻ�ȡ��֤��������߳� ����ʹ����SESSIONID�Ż��������
	 */
	class ImageThread extends Thread {

		@Override
		public void run() {

			// ����һ��Post�����������������Ҫͨ��Post�������͵�����˵�����ͷ��������Ĳ��������øö�����
			HttpPost httpPost = new HttpPost(NetworkUtils.DANGDANG_CODE_URL);

			/* ���������������ʱʱ�� */
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5 * 1000);
			// ����sessionid����������session�ռ���Ч,���η���������������
			if (sessionid != null) {
				httpPost.setHeader("cookie", "JSESSIONID=" + sessionid);
			}
			httpPost.setParams(httpParameters);

			// ����һ���ͻ�����������
			HttpClient client = new DefaultHttpClient();
			// ����Get���󣬲��ȴ�����˵���Ӧ
			try {
				HttpResponse response = client.execute(httpPost);

				// ���SessionID�������棬Ϊ�ύ������׼��
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
				// ����������֧�ֱ�׼HttpЭ�飬���������쳣
				showMessage(e.getMessage());
			} catch (IOException e) {
				// ����ͨѶ�������ϣ����������쳣
				showMessage("�޷�����Զ�̷�����");
			}

		}

		/*
		 * ͨ��handler������֤��ͼƬ���͸����̲߳������������������֤��
		 */
		private void showImage(Bitmap bitmap) {

			// ��ʾͼƬ�Ĳ������ܳ��������߳��У���Ҫ��Handler�����
			// ivImage.setIma2geBitmap(bitmap);
			Message msg = Message.obtain(handler,
					LoginDangDangHandler.SHOW_NETWORK_IMAGE);
			msg.obj = bitmap;
			msg.sendToTarget();
		}

	}
}
