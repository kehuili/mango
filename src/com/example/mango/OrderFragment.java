package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.mango.ListFragment.ListThread;
import com.example.widget.BookListCard;
import com.example.widget.OrderCard;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

public class OrderFragment extends Fragment {

	private AsyncBitmapLoader abl = new AsyncBitmapLoader();
	ExceptionHandler bookHandler = new ExceptionHandler();
	JSONObject params = new JSONObject();
	private Bitmap orderBm[];
	private int orderNum = 0;
	private List<List<Map<String, Object>>> orderitems = new ArrayList<List<Map<String, Object>>>();
	private List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
	private OrderThread orderThread;
	
	private View page,page0,page1,page2,page3,page4= null;
	private CardListView list0 ,list1,list2,list3,list4= null;
	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private TextView t0,t1, t2,t3,t4;// 页卡头标
	
	public OrderFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		orderThread = new OrderThread();
		orderThread.start();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		page = inflater.inflate(R.layout.order, container, false);
		page0 = inflater.inflate(R.layout.order_page0, container, false);
		page1 = inflater.inflate(R.layout.order_page1, container, false);
		page2 = inflater.inflate(R.layout.order_page2, container, false);
		page3 = inflater.inflate(R.layout.order_page3, container, false);
		page4 = inflater.inflate(R.layout.order_page4, container, false);
		mPager = (ViewPager) page.findViewById(R.id.order_vp);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		/*InitImageView();*/
		InitTextView();
		InitViewPager();
		return page;
	}
	
	private void InitTextView() {
		t0 = (TextView) page.findViewById(R.id.tab_0);
		t1 = (TextView) page.findViewById(R.id.tab_1);
		t2 = (TextView) page.findViewById(R.id.tab_2);
		t3 = (TextView) page.findViewById(R.id.tab_3);
		t4 = (TextView) page.findViewById(R.id.tab_4);
		
		t0.setOnClickListener(new MyOnClickListener(0));
		t1.setOnClickListener(new MyOnClickListener(1));
		t2.setOnClickListener(new MyOnClickListener(2));
		t3.setOnClickListener(new MyOnClickListener(3));
		t4.setOnClickListener(new MyOnClickListener(4));
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		
		

		/*ArrayList<Card> cards = new ArrayList<Card>();
        for (int i=0;i<50;i++){
            BookListCard card = new BookListCard(this.getActivity());
            card.setTitle("Application example "+i);
            card.setWriter("A company inc..."+i);
            card.count=i;

            //Only for test, change some icon

            card.init();
            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					search.setVisibility(View.INVISIBLE);
					BookFragment bookFragment = new BookFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.mainfragment, bookFragment);
					//transaction.addToBackStack(null);
					transaction.commit();
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        list_lin = (CardListView) listlin.findViewById(R.id.list_list);
        if (list_lin!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(list_lin);
            list_lin.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
        }
		listViews.add(list_lin);

		list_lin2 = (CardListView) listlin2.findViewById(R.id.list_list2);
        if (list_lin2!=null){
//            list_lin2.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
            animCardArrayAdapter.setAbsListView(list_lin2);
            list_lin2.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
        }
		
		listViews.add(list_lin2);*/
		
		
		ArrayList<Card> cards0 = new ArrayList<Card>();
        for (int i=0;i<orderNum;i++){
            OrderCard card = new OrderCard(this.getActivity());
            card.setId(orders.get(i).get("ordid").toString());
            card.setSum(orders.get(i).get("price").toString());
            card.setType((Integer)orders.get(i).get("ordertype"));
            card.count=i;

            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards0.add(card);
        }
        CardArrayAdapter mCardArrayAdapter0 = new CardArrayAdapter(getActivity(),cards0);

        list0 = (CardListView) page0.findViewById(R.id.page_list_0);
        if (list0!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter0);
            animCardArrayAdapter.setAbsListView(list0);
            list0.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter0);
        }
        
        //未付款
        ArrayList<Card> cards1 = new ArrayList<Card>();
        for (int i=0;i<5;i++){
            OrderCard card = new OrderCard(this.getActivity());
            card.setId("订单编号：232323123232313123"+i*i);
            card.setSum("共计：   "+i*800);
            card.setType(1);
            card.count=i;

            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards1.add(card);
        }
        CardArrayAdapter mCardArrayAdapter1 = new CardArrayAdapter(getActivity(),cards1);

        list1 = (CardListView) page1.findViewById(R.id.page_list_1);
        if (list1!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter1);
            animCardArrayAdapter.setAbsListView(list1);
            list1.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter1);
        }
        
        //未发货
        ArrayList<Card> cards2 = new ArrayList<Card>();
        for (int i=0;i<5;i++){
            OrderCard card = new OrderCard(this.getActivity());
            card.setId(""+i*i);
            card.setSum(""+i*800);
            card.setType(2);
            card.count=i;

            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards2.add(card);
        }
        CardArrayAdapter mCardArrayAdapter2 = new CardArrayAdapter(getActivity(),cards2);

        list2 = (CardListView) page2.findViewById(R.id.page_list_2);
        if (list2!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter2);
            animCardArrayAdapter.setAbsListView(list2);
            list2.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter2);
        }
        
        //未收货
        ArrayList<Card> cards3 = new ArrayList<Card>();
        for (int i=0;i<5;i++){
            OrderCard card = new OrderCard(this.getActivity());
            card.setId(""+i*i);
            card.setSum(""+i*800);
            card.setType(3);
            card.count=i;

            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards3.add(card);
        }
        CardArrayAdapter mCardArrayAdapter3 = new CardArrayAdapter(getActivity(),cards3);

        list3 = (CardListView) page3.findViewById(R.id.page_list_3);
        if (list3!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter3);
            animCardArrayAdapter.setAbsListView(list3);
            list3.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter3);
        }
        
        
        //未评价
        ArrayList<Card> cards4 = new ArrayList<Card>();
        for (int i=0;i<5;i++){
            OrderCard card = new OrderCard(this.getActivity());
            card.setId(""+i*i);
            card.setSum(""+i*800);
            card.setType(4);
            card.count=i;

            card.setOnClickListener(new OnCardClickListener() {
				
//				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					
				}
			});
            card.setBackgroundResourceId(R.drawable.card_alfa);
            cards4.add(card);
        }
        CardArrayAdapter mCardArrayAdapter4 = new CardArrayAdapter(getActivity(),cards4);

        list4 = (CardListView) page4.findViewById(R.id.page_list_4);
        if (list4!=null){
//            list_lin.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter4);
            animCardArrayAdapter.setAbsListView(list4);
            list4.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter4);
        }
        
		
		
		
		listViews.add(page0);
		listViews.add(page1);
		listViews.add(page2);
		listViews.add(page3);
		listViews.add(page4);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}
	
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}
	
	
	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
//			Animation animation = null;
			switch (arg0) {
			case 0:
				t0.setBackgroundResource(R.drawable.tab_left_focus);
				t1.setBackgroundResource(R.drawable.alfa_nofocus);
				t2.setBackgroundResource(R.drawable.alfa_nofocus);
				t3.setBackgroundResource(R.drawable.alfa_nofocus);
				t4.setBackgroundResource(R.drawable.tab_right_nofocus);
				break;
			case 1:
				
				t0.setBackgroundResource(R.drawable.tab_left_nofocus);
				t1.setBackgroundResource(R.drawable.alfa_focus);
				t2.setBackgroundResource(R.drawable.alfa_nofocus);
				t3.setBackgroundResource(R.drawable.alfa_nofocus);
				t4.setBackgroundResource(R.drawable.tab_right_nofocus);
				break;
			case 2:
				
				t0.setBackgroundResource(R.drawable.tab_left_nofocus);
				t1.setBackgroundResource(R.drawable.alfa_nofocus);
				t2.setBackgroundResource(R.drawable.alfa_focus);
				t3.setBackgroundResource(R.drawable.alfa_nofocus);
				t4.setBackgroundResource(R.drawable.tab_right_nofocus);
				break;
			case 3:
				
				t0.setBackgroundResource(R.drawable.tab_left_nofocus);
				t1.setBackgroundResource(R.drawable.alfa_nofocus);
				t2.setBackgroundResource(R.drawable.alfa_nofocus);
				t3.setBackgroundResource(R.drawable.alfa_focus);
				t4.setBackgroundResource(R.drawable.tab_right_nofocus);
				break;
			case 4:
				t1.setBackgroundResource(R.drawable.alfa_nofocus);
				t2.setBackgroundResource(R.drawable.alfa_nofocus);
				t3.setBackgroundResource(R.drawable.alfa_nofocus);
				t0.setBackgroundResource(R.drawable.tab_left_nofocus);
				t4.setBackgroundResource(R.drawable.tab_right_focus);
				break;
			}
//			currIndex = arg0;
//			animation.setFillAfter(true);// True:图片停在动画结束位置
//			animation.setDuration(300);
//			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		

	}
	class OrderThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("cusid", getActivity().getIntent().getIntExtra("cusid",0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			String temp = HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_ORDER_URL, bookHandler, params);
			String imgURL;
			String imgName;
			orders = JsonParse.getListMap("orders", temp);
//			Log.d("order", orders.get(0).toString());
//			for(int i = 0; i < orders.size(); i++) {
//				orderitems.set(i, JsonParse.getListMap("orderitems", orders.get(i).toString()));
//			}
			
			orderNum = orders.size();
//			orderBm = new Bitmap[orderNum];
//			for(int i = 0; i<orderNum; i++) {
//				imgURL = salesbooks.get(i).get("imagePath").toString();
//				imgName = salesbooks.get(i).get("imageName").toString();
//				salesBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
//						+ imgName);
//			}
			myHandler.sendEmptyMessage(1);
		}
	}
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			InitViewPager();

		};
	};
}
