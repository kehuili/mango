package com.example.widget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mango.R;

public class OrderBookAdapter extends BaseAdapter{
	
	List<Map<String, Object>> datas = null;
	Context context = null;
	
	public OrderBookAdapter(List<Map<String, Object>> datas,Context context){
		this.datas = datas;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater  = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.list_list1, null);
		return v;
	}
	
}
