package com.example.mango;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SetFragment extends Fragment implements OnClickListener{
	
	Intent intent = null;
	TextView set_change,exit = null;
	Builder builder = null;
	
   /* @Override
	public void onCreate(Bundle savedInstanceState) {y
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        
        ret  =(Button) findViewById(R.id.returnhome);
        set_change = (TextView)findViewById(R.id.set_change);
        exit = (TextView)findViewById(R.id.set_exit);
        set_change.setOnClickListener(this);
        exit.setOnClickListener(this);
		ret.setOnClickListener(this);
        
    }*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.set, container, false);
	        set_change = (TextView)rootView.findViewById(R.id.set_change);
	        exit = (TextView)rootView.findViewById(R.id.set_exit);
	        set_change.setOnClickListener(this);
	        exit.setOnClickListener(this);
		return rootView;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.set_change:
			/*intent = new Intent(getActivity(),LoginActivity.class);
    		startActivity(intent);*/
			builder =new AlertDialog.Builder(getActivity());  
			builder.setTitle("更换账户");
			builder.setMessage("请再次确认");
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					intent = new Intent(getActivity(),LoginActivity.class);
		    		startActivity(intent);
				}
			});
			builder.create().show();
    		break;
		case R.id.set_exit:
			/*intent = new Intent(getActivity(),MainActivity.class);
    		startActivity(intent);
			Intent home = new Intent(Intent.ACTION_MAIN);   
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
			home.addCategory(Intent.CATEGORY_HOME);   
			startActivity(home);*/
			builder =new AlertDialog.Builder(getActivity());  
			builder.setTitle("退出");
			builder.setMessage("请再次确认");
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					// TODO Auto-generated method stub
					intent = new Intent(getActivity(),MainActivity.class);
		    		startActivity(intent);
					Intent home = new Intent(Intent.ACTION_MAIN);   
					home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
					home.addCategory(Intent.CATEGORY_HOME);   
					startActivity(home);
				}
			});
			builder.create().show();
			break;
		default:
			break;
		
		}
	}




    /**
     * A placeholder fragment containing a simple view.
     */
  

}
