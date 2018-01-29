package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnSwipeListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.mango.ListFragment.ListThread;
import com.example.widget.CartCard;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

public class CartFragment extends Fragment{
	int deleteId = 0;
	ExceptionHandler bookHandler = new ExceptionHandler();
	JSONObject params = new JSONObject();
	private Bitmap cartBm[];
	private int cartNum = 0;
	private ListThread cartThread;
	private AsyncBitmapLoader abl = new AsyncBitmapLoader();
	private List<Map<String, Object>> cart = new ArrayList<Map<String, Object>>();
	CheckBox one = null;
	CheckBox all = null;
	View rootView = null;
	LinearLayout waitCart = null;
	/*ListView list = null;*/
	
	CardListView list = null;
	
	Button sub = null;
	
	public CartFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		CartThread cartThread= new CartThread();
		cartThread.start();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.cart, container, false);
		waitCart = (LinearLayout) rootView.findViewById(R.id.wait_cart);
		/*List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < names.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("headers", imageIds[i]);
			listItem.put("bookName", names[i]);
			listItem.put("bookWriter", writers[i]);
			listItem.put("bookPrice","$"+ prices[i]);
			listItems.add(listItem);
		}
		CartAdapter adapter = new CartAdapter(listItems,getActivity());
		list = (ListView) rootView.findViewById(R.id.cart_list);
		list.setLayoutAnimation(getAnimationController());
		
		list.setAdapter(adapter);*/

        
		one = (CheckBox)rootView.findViewById(R.id.cart_checkOneBox);
		all = (CheckBox) rootView.findViewById(R.id.cart_checkAllBox);

		all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton box, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					for (int index = 0; index < list.getChildCount(); index++) {
						LinearLayout layout = (LinearLayout) list.getChildAt(index);
						CheckBox checkBox = (CheckBox) layout.findViewById(R.id.cart_checkOneBox);
						checkBox.setChecked(false);
					}
				} else {
					for (int index = 0; index < list.getChildCount(); index++) {
						LinearLayout layout = (LinearLayout) list.getChildAt(index);
						CheckBox checkBox = (CheckBox) layout.findViewById(R.id.cart_checkOneBox);
						checkBox.setChecked(true);
					}
				}
			}
		});
		sub = (Button) rootView.findViewById(R.id.btn_sub);
		sub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),SubActivity.class);
	    		startActivity(intent);
			}
		});
		return rootView;
	}
	
	
	class CartThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("cusid", getActivity().getIntent().getIntExtra("cusid",0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			String temp = HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_SHOPPING_URL, bookHandler, params);
			String imgURL;
			String imgName;
			cart = JsonParse.getListMap("books", temp);
			cartNum = cart.size();
			Log.d("star",""+cartNum);
			cartBm = new Bitmap[cartNum];
			for(int i = 0; i<cartNum; i++) {
				imgURL = cart.get(i).get("imagePath").toString();
				imgName = cart.get(i).get("imageName").toString();
				cartBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
						+ imgName);
			}
			
			myHandler.sendEmptyMessage(1);

		}
	}
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			ArrayList<Card> cards = new ArrayList<Card>();
	        for (int i=0;i<cartNum;i++){
	            CartCard card = new CartCard(getActivity());
	            card.setTitle(cart.get(i).get("bookName").toString());
	            card.setPrice(cart.get(i).get("bookPrice").toString());
	            card.setNumber((Integer)cart.get(i).get("quantity"));
	            card.setImage(cartBm[i]);
	            card.setBookId((Integer)cart.get(i).get("bookId"));
	            card.count=i;

	            //Only for test, change som
	            card.init();
	            
	            card.setSwipeable(true);
	            
	            card.setOnSwipeListener(new OnSwipeListener() {
					
					@Override
					public void onSwipe(Card card) {
						// TODO Auto-generated method stub
						deleteId = ((CartCard) card).getBookId();
						DelThread delThread = new DelThread();
						delThread.start();
					}
				});
	            
	            card.setBackgroundResourceId(R.drawable.card_alfa);
	            cards.add(card);
	        }
	        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

	        list = (CardListView) rootView.findViewById(R.id.cart_list);
	        if (list!=null){
//	            list.setAdapter(mCardArrayAdapter);
	        	AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
	            animCardArrayAdapter.setAbsListView(list);
	            list.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
	        }
	        
	        list.setVisibility(View.VISIBLE);
	        waitCart.setVisibility(View.GONE);

		};
	};
	class DelThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("op", "del");
				params.put("cusid", getActivity().getIntent().getIntExtra("cusid",0));
				params.put("bookid", deleteId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			String temp = HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_SHOPPING_URL, bookHandler, params);
			
			JSONObject ok;
			try {
				ok = new JSONObject(temp);
			
				if(ok.getBoolean("isOk") == true) {
					bookHandler.showMessage("删除成功");
				}else {
					bookHandler.showMessage("删除失败，再来一次");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
