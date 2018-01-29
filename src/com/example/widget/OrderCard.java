package com.example.widget;

import it.gmariotti.cardslib.library.internal.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mango.R;

public class OrderCard extends Card{
	
	protected TextView mOrderId;
	protected TextView mOrderSum;
    protected Button mBtn1,mBtn2;
    protected ListView mOrderBook;
    public int count;

    protected String id;
    protected String sum;
    protected int type;
    protected Context context;

    public OrderCard(Context context) {
        this(context, R.layout.order_list);
        this.context = context;
    }

    public OrderCard(Context context, int innerLayout) {
    	
        super(context, innerLayout);
 
    }
 
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
    	
        mOrderId = (TextView) parent.findViewById(R.id.order_id);
        mOrderSum = (TextView) parent.findViewById(R.id.order_sum);
        mOrderBook = (ListView) parent.findViewById(R.id.order_book_list);
        mBtn1 = (Button) parent.findViewById(R.id.btn1);
        mBtn2 = (Button) parent.findViewById(R.id.btn2);
         
        
        if (mOrderId != null)
            mOrderId.setText(id);

        if (mOrderSum != null)
            mOrderSum.setText(sum);
        
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 3; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("headers", R.drawable.book);
			listItem.put("bookName", "这是人写的书");
			listItem.put("bookWriter", "人");
			listItem.put("bookPrice", "40x3"+"34"+i);
			listItems.add(listItem);
		}
		SimpleAdapter adapter = new SimpleAdapter( context, listItems,
				R.layout.list_list1, new String[] { "headers", "bookName",
						"bookWriter", "bookPrice" }, new int[] { R.id.bookImg,
						R.id.name, R.id.writer, R.id.price });
        
		mOrderBook.setAdapter(adapter);
        
        switch( type ){
        case 0:
        	mBtn2.setText("删除订单");
        	mBtn1.setVisibility(View.INVISIBLE);
        	break;
        case 1:
        	mBtn1.setText("取消订单");
        	mBtn2.setText("去付款");
        	mBtn1.setVisibility(View.VISIBLE);
        	mBtn2.setVisibility(View.VISIBLE);
        	break;
        case 2:
        	mBtn1.setVisibility(View.INVISIBLE);
        	mBtn2.setText("取消订单");
        	break;
        case 3:
        	mBtn1.setText("查看物流");
        	mBtn2.setText("确认收货");
        	mBtn1.setVisibility(View.VISIBLE);
        	mBtn2.setVisibility(View.VISIBLE);
        	break;
        case 4:
        	mBtn1.setText("删除订单");
        	mBtn2.setText("评价订单");
        	mBtn1.setVisibility(View.VISIBLE);
        	mBtn2.setVisibility(View.VISIBLE);
        	break;
        }

    }
    
    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
    
    public void setType(int type){
    	this.type = type;
    }
    public int getType(){
    	return type;
    }
    
}

