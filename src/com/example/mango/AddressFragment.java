package com.example.mango;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.tools.ContextUtil;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

@SuppressLint("ValidFragment") public class AddressFragment extends Fragment implements OnClickListener {

	private static int FLAG = 0;
	private SwipeListView adSLV;
	protected static final String TAG = "Activity";
	View adView, dialog = null;
	Builder builder = null;
	Button del, mod,newCancel,newSub = null;
	//ChangeThread changeThread = null;
	ProgressBar pb = null;
	LinearLayout newAdd = null;
	AlertDialog address = null;
	private List<Map<String,Object>> mAddress;
	private AddressHandler addressHandler = new AddressHandler();
	private ExceptionHandler addrsHandler = new ExceptionHandler();
	private Map<String,Object> newAddress;
	private EditText Address,phone,receiver;
	/*
	 * private String[] phone = new String[]
	 * {"15151815181","15151515151","11111111111","12341223456"}; private
	 * String[] names = new String[] { "做广药", "王思聪", "回克利", "礼金吧" }; private
	 * String address = "这是一个风景优美动物众多占地极广的三本学校";
	 */
	TextView newAd = null;
/*
	public AddressFragment(){
		
	}
	public AddressFragment(List<Map<String,Object>> address){
		this.mAddress = address;
		Log.d("hjhjhgh", address.get(1).get("phone").toString());
	}*/
	
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			List<String> listItems = new ArrayList<String>();
			for (int i = 0; i < mAddress.size(); i++)
				listItems.add(mAddress.get(i).get("receiver")+ "     " + mAddress.get(i).get("phone")+"\n" + mAddress.get(i).get("address"));
			adSLV = (SwipeListView) adView.findViewById(R.id.slv_ad);
			AddressAdapter adapter = new AddressAdapter(getActivity(), listItems,
					adSLV);
			adSLV.setAdapter(adapter);
			adSLV.setSwipeListViewListener(new BaseSwipeListViewListener() {
				@Override
				public void onListChanged() {
					Log.d(TAG, "onListChanged");

					adSLV.closeOpenedItems();

				}

			});
			pb.setVisibility(View.GONE);
			adSLV.setVisibility(View.VISIBLE);
		};
	};
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.new_address:
			newAdd.setVisibility(View.VISIBLE);
			break;
		case R.id.new_add_cancel:
			newAdd.setVisibility(View.GONE);
			break;
		case R.id.add_sub:
			//新建收货地址线程
			NewAddressThread newAddressThread = new NewAddressThread();
			newAddressThread.start();
			break;
		}
		
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AddressThread addressThread = new AddressThread();
		addressThread.start();
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		adView = inflater.inflate(R.layout.address, container, false);
		dialog = inflater.inflate(R.layout.address_dialog, container, false);
		newAd = (TextView) adView.findViewById(R.id.new_address);
		pb = (ProgressBar) adView.findViewById(R.id.pb_address);
		newAdd = (LinearLayout) adView.findViewById(R.id.new_add);
		newCancel = (Button) adView.findViewById(R.id.new_add_cancel);
		newSub = (Button) adView.findViewById(R.id.add_sub);
		Address = (EditText)dialog.findViewById(R.id.editText3);
		receiver = (EditText)dialog.findViewById(R.id.editText1);
		phone = (EditText)dialog.findViewById(R.id.editText2);
		/*
		 * newCancel = (TextView) adView.findViewById(R.id.new_cancel); newSub =
		 * (TextView) adView.findViewById(R.id.new_sub);
		 */
		newAd.setOnClickListener(this);
		newCancel.setOnClickListener(this);
		newSub.setOnClickListener(this);
		/*
		 * newCancel.setOnClickListener(this); newSub.setOnClickListener(this);
		 */

		/*
		 * List<Map<String, String>> listItems = new ArrayList<Map<String,
		 * String>>();
		 * 
		 * for (int i = 0; i < names.length; i++) { Map<String, String> listItem
		 * = new HashMap<String, String>(); listItem.put("addName", names[i]);
		 * listItem.put("addPhone", phone[i]); listItem.put("addAddress",
		 * address); listItems.add(listItem); }
		 */
		/*List<String> listItems = new ArrayList<String>();
		for (int i = 0; i <= mAddress.size(); i++)
			
			listItems.add(mAddress.get(i).get("receiver")+ "     " + mAddress.get(i).get("phone")+"\n" + mAddress.get(i).get("address"));
		adSLV = (SwipeListView) adView.findViewById(R.id.slv_ad);
		AddressAdapter adapter = new AddressAdapter(getActivity(), listItems,
				adSLV);
		adSLV.setAdapter(adapter);
		adSLV.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onListChanged() {
				Log.d(TAG, "onListChanged");

				adSLV.closeOpenedItems();

			}

		});*/

		return adView;
	}

	public class AddressAdapter extends BaseAdapter {

		// private List<Map<String, String>> mDatas = null;
		private List<String> mDatas;
		private LayoutInflater mInflater = null;
		private SwipeListView mSwipeListView = null;

		public AddressAdapter(Context context, List<String> datas,
				SwipeListView swipeListView) {
			this.mDatas = datas;
			mInflater = LayoutInflater.from(context);
			mSwipeListView = swipeListView;
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.address_list, null);
			/*
			 * TextView t_name = (TextView)
			 * convertView.findViewById(R.id.ad_name); TextView t_phone =
			 * (TextView) convertView.findViewById(R.id.ad_phone); TextView
			 * t_address = (TextView) convertView.findViewById(R.id.ad_address);
			 * t_name.setText(mDatas.get(position).get("addName"));
			 * t_phone.setText(mDatas.get(position).get("addPhone"));
			 * t_address.setText(mDatas.get(position).get("addAddress"));
			 */
			del = (Button) convertView.findViewById(R.id.id_remove);
			mod = (Button) convertView.findViewById(R.id.id_mod);
			TextView tv = (TextView) convertView.findViewById(R.id.id_text);
			tv.setText(mDatas.get(position));

			del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDatas.remove(position);
					notifyDataSetChanged();
					/**
					 * 关闭SwipeListView 不关闭的话，刚删除位置的item存在问题
					 * 在监听事件中onListChange中关闭，会出现问题
					 */
					mSwipeListView.closeOpenedItems();
				}
			});
			mod.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (builder != null) {
						address.show();
					} else {
						builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("修改收货地址");
						builder.setView(dialog);
						builder.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						builder.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int arg1) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						address = builder.create();
						address.show();
					}
				}
			});

			return convertView;
		}

	}
	public void showMessage(String message){
		Message msg = Message.obtain(addressHandler,AddressHandler.SHOW_MESSAGE);
		msg.obj = message;
		msg.sendToTarget();
	}
	
	class AddressHandler extends Handler{
		public static final int SHOW_MESSAGE = 0x0001;
		public void handleMessage(Message msg){
			if(msg.what == SHOW_MESSAGE){
				Toast.makeText(ContextUtil.getInstance(), msg.obj.toString(),Toast.LENGTH_LONG ).show();
			}
		}
	}

     public class AddressThread extends Thread{
    	 public void run(){
    		 JSONObject params =new JSONObject();
    		 try{
    			 params .put("op", "all");
    			 params.put("cusid",getActivity().getIntent().getIntExtra("cusid",0));
    		 }catch(JSONException e1){
    			 addrsHandler.showMessage("信息获取错误");
    		 }
    		mAddress = JsonParse.getListMap("addrs",HttpUtils.getJsonContent(NetworkUtils.DANGDANG_ADDRESS_URL, addrsHandler, params));
    		//FLAG = 1;
    		myHandler.sendEmptyMessage(1);
    	 }
    	 
     }
     public class NewAddressThread extends Thread{
    	 public void run(){
    		 JSONObject params =new JSONObject();
    		 try{
    			 if(Address.getText().toString()!=null||phone.getText().toString()!=null||receiver.getText().toString()!=null)
    			 {
    				 Map<String,Object> addressObject = new HashMap<String,Object>();
    				 if(Address.getText().toString()!=null){
    				 addressObject.put("address",Address.getText().toString());}
    				 addressObject.put("idofaddress",0);
    				 if(phone.getText().toString()!=null){
    				 addressObject.put("phone", phone.getText().toString());}
    				 if(receiver.getText().toString()!=null){
    				 addressObject.put("receiver",receiver.getText().toString());}
    				 params.put("cusid",getActivity().getIntent().getIntExtra("cusid",0));
    				 params.put("addressobject",addressObject);
    				 }
    			 
    			 /*params .put("address",Address.getText().toString());
    			 params.put("phone",phone.getText().toString());
    			 params.put("receiver",receiver.getText().toString());*/
    			 
    			 
    		 }catch(JSONException e1){
    			 addrsHandler.showMessage("信息获取错误");
    		 }
    		newAddress = JsonParse.getMap(HttpUtils.getJsonContent(NetworkUtils.DANGDANG_MOD_URL, addrsHandler, params));
    		//FLAG = 1;
    		if((Boolean)newAddress.get("isOk")){
    			addrsHandler.showMessage("添加地址成功");
    		}
    		/*AddressThread addressThread = new AddressThread();
    		addressThread.start();*/
     }
    }

}
