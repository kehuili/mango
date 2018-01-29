package com.example.mango;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.mango.AddressFragment.AddressAdapter;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class InfoFragment extends Fragment {
	
	Button btn11,btn12,btn22,btn21 = null;
	LinearLayout liName,liPwd = null;
	private ExceptionHandler infoHandler = new ExceptionHandler();
	private Map<String,Object> mInfo = new HashMap<String, Object>();
	private Map<String,Object> modInfo = new HashMap<String, Object>();
	private EditText name,pwd;
	private TextView oldName;
	 
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			oldName.setText("当前昵称："+mInfo.get("name").toString());
			liName.setVisibility(View.GONE);
			btn12.setVisibility(View.GONE);
		};
	};
	/*Handler myHandler2 = new Handler(){
		public void handleMessage(Message msg){
			oldName.setText("当前昵称："+modInfo.get("name").toString());
		};
	};*/
	public InfoFragment(){
		
	}
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		InfoThread infoThread = new InfoThread();
		infoThread.start();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View info = inflater.inflate(R.layout.info, container, false);
		btn11 = (Button) info.findViewById(R.id.name_sub);
		btn12 = (Button) info.findViewById(R.id.mod_name_cancel);
		btn22= (Button) info.findViewById(R.id.pwd_cancel);
		btn21 = (Button) info.findViewById(R.id.pwd_sub);
		liName = (LinearLayout) info.findViewById(R.id.modname);
		liPwd = (LinearLayout) info.findViewById(R.id.modpwd);
		name = (EditText)info.findViewById(R.id.regname);
		pwd = (EditText)info.findViewById(R.id.newpwd);
		oldName = (TextView)info.findViewById(R.id.old_regname);
		
		btn11.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(liName.getVisibility()== View.VISIBLE){
					ModInfoThread modInfoThread = new ModInfoThread();
					modInfoThread.start();
					liName.setVisibility(View.GONE);
					btn12.setVisibility(View.GONE);
				}
				liName.setVisibility(View.VISIBLE);
				btn12.setVisibility(View.VISIBLE);
				
			}
		});
		
		btn12.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				liName.setVisibility(View.GONE);
				btn12.setVisibility(View.GONE);
			}
		});
		
		btn21.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(liPwd.getVisibility()== View.VISIBLE){
					ModInfoThread modInfoThread = new ModInfoThread();
					modInfoThread.start();
					liPwd.setVisibility(View.GONE);
					btn22.setVisibility(View.GONE);
				}
				liPwd.setVisibility(View.VISIBLE);
				btn22.setVisibility(View.VISIBLE);
			}
		});
		
		btn22.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				liPwd.setVisibility(View.GONE);
				btn22.setVisibility(View.GONE);
			}
		});
		
		
		return info;
	}
	public class InfoThread extends Thread{
		public void run(){
			JSONObject params = new JSONObject();
			try{
				params.put("cusid",getActivity().getIntent().getIntExtra("cusid",0));
			}catch(JSONException e1){
				infoHandler.showMessage("信息获取错误");
			}
			mInfo = JsonParse.getMap(HttpUtils.getJsonContent(NetworkUtils.DANGDANG_CHECK_URL, infoHandler, params));
			//Log.d("sdada", mInfo.get("name").toString());
			myHandler.sendEmptyMessage(1);
		}
	}
	public class ModInfoThread extends Thread{
		public void run(){
			JSONObject params = new JSONObject();
			try{
				if(liName.getVisibility()== View.VISIBLE||liPwd.getVisibility() == View.VISIBLE){
					if(liName.getVisibility()== View.VISIBLE){
						params.put("name",name.getText().toString());
					}
					if(liPwd.getVisibility() == View.VISIBLE){
						params.put("pwd", pwd.getText().toString());
					}
					params.put("cusid",getActivity().getIntent().getIntExtra("cusid",0));
					
				}
				
			}catch(JSONException e1){
				infoHandler.showMessage("信息获取错误");
			}
			modInfo = JsonParse.getMap(HttpUtils.getJsonContent(NetworkUtils.DANGDANG_MOD_URL, infoHandler, params));
			if((Boolean)modInfo.get("isOk")){
				infoHandler.showMessage("信息修改成功");
				myHandler.sendEmptyMessage(1);
			}
			
		}
	}
	
}
