package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.widget.BookListCard;

public class SubActivity extends Activity {
	
	private String[] address = new String[] {"519","520","518"};
	private String[] logistics = new String[] {"奔奔快递","左小海快递","王思聪专运"};
	private String[] pay = new String[] { "货到付款", "支付宝", "微信支付" };
	/*ListView list = null;
	*/
	
	Spinner s_address,s_logistics,s_pay = null;
	CardListView sub_list= null;
	Button reCart = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub);
        
       /* List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("headers", imageIds[i]);
			listItem.put("bookName", names[i]);
			listItem.put("bookDetail", details[i]);
			listItem.put("bookPrice", prices[i]);
			listItems.add(listItem);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.sub_list, new String[] { "headers", "bookName",
						"bookDetail", "bookPrice" }, new int[] { R.id.bookImg,
						R.id.name, R.id.writer, R.id.price });
		list = (ListView) findViewById(R.id.sub_list);
		list.setAdapter(adapter);*/
		
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i=0;i<10;i++){
            BookListCard card = new BookListCard(this);
            card.setTitle("Application example "+i);
            card.setWriter("A company inc..."+i);
            card.setPrice(i+"\n"+i*50);
            card.count=i;

            //Only for test, change some icon

            card.init();
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);
        
        s_address = (Spinner) findViewById(R.id.spin_address);
        s_logistics = (Spinner) findViewById(R.id.spin_logistics);
        s_pay = (Spinner) findViewById(R.id.spin_pay);
        
        ArrayAdapter<String> a_adapter = new ArrayAdapter<String>(SubActivity.this, R.layout.spinner_text,address);
        ArrayAdapter<String> l_adapter = new ArrayAdapter<String>(SubActivity.this, R.layout.spinner_text,logistics);
        ArrayAdapter<String> p_adapter = new ArrayAdapter<String>(SubActivity.this, R.layout.spinner_text,pay);
        
        s_address.setAdapter(a_adapter);
        s_logistics.setAdapter(l_adapter);
        s_pay.setAdapter(p_adapter);
        
        sub_list = (CardListView) findViewById(R.id.sub_list);
        if (sub_list!=null){
            sub_list.setAdapter(mCardArrayAdapter);
        }
        
		reCart  =(Button) findViewById(R.id.returncart);
		reCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				onBackPressed();
			}
		});
		
    }


    /**
     * A placeholder fragment containing a simple view.
     */
  

}
