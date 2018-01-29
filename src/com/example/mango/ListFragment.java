package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.text.SimpleDateFormat;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.mango.BookFragment.ChangeThread;
import com.example.mango.BookFragment.MyOnPageChangeListener;
import com.example.mango.BookFragment.MyPagerAdapter;
import com.example.widget.BookListCard;
import com.example.widget.RemarkCard;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

public class ListFragment extends Fragment {
	int flag = 0;
	int flag2 = 0;
	private AsyncBitmapLoader abl = new AsyncBitmapLoader();
	ExceptionHandler bookHandler = new ExceptionHandler();
	JSONObject params = new JSONObject();
	private Bitmap salesBm[];
	private Bitmap starsBm[];
	private int salesNum = 0;
	private int starsNum = 0;
	private List<Map<String, Object>> salesbooks = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> starsbooks = new ArrayList<Map<String, Object>>();
	private ListThread listThread;

	Button search = null;
	View list,listlin,listlin2= null;
	CardListView list_lin ,list_lin2= null;
	LinearLayout waitList = null;
	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private TextView t1, t2;// 页卡头标
	
	public ListFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		listThread = new ListThread();
		listThread.start();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		list = inflater.inflate(R.layout.list, container, false);
		search = (Button)getActivity().findViewById(R.id.search);
		listlin = inflater.inflate(R.layout.list_lin1, container, false);
		listlin2 = inflater.inflate(R.layout.list_lin2, container, false);
		waitList = (LinearLayout)list.findViewById(R.id.wait_list);
		/*InitImageView();*/
		InitTextView();
		//InitViewPager();
		return list;
	}
	
	private void InitTextView() {
		t1 = (TextView) list.findViewById(R.id.text1);
		t2 = (TextView) list.findViewById(R.id.text2);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) list.findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		

		ArrayList<Card> cards = new ArrayList<Card>();
        for (int i=0;i<salesNum;i++){
            BookListCard card = new BookListCard(this.getActivity());
            card.setTitle(salesbooks.get(i).get("bookName").toString());
            card.setWriter(salesbooks.get(i).get("author").toString());
            card.setPrice("$" + salesbooks.get(i).get("price").toString());
            card.setBookId( (Integer) salesbooks.get(i).get("bookId"));
            card.setBitmap(salesBm[i]);
            card.count=i;

            //Only for test, change some icon
            flag = i;
            card.init();
            card.setOnClickListener(new OnCardClickListener() {
				
				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					Log.d("flag", ""+flag);
					search.setVisibility(View.INVISIBLE);
					BookFragment bookFragment = new BookFragment(((BookListCard)card).getBookId());
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

		ArrayList<Card> cards2 = new ArrayList<Card>();
        for (int i=0;i<starsNum;i++){
            BookListCard card2 = new BookListCard(this.getActivity());
            card2.setTitle(starsbooks.get(i).get("bookName").toString());
            card2.setWriter(starsbooks.get(i).get("author").toString());
            card2.setPrice("$" + starsbooks.get(i).get("price").toString());
            card2.setBitmap(starsBm[i]);
            card2.count=i;

            //Only for test, change some icon
            flag2 = i;
            card2.init();
            card2.setOnClickListener(new OnCardClickListener() {
				
				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					search.setVisibility(View.INVISIBLE);
					BookFragment bookFragment = new BookFragment((Integer) salesbooks.get(flag).get("bookId"));
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.mainfragment, bookFragment);
					//transaction.addToBackStack(null);
					transaction.commit();
				}
			});
            card2.setBackgroundResourceId(R.drawable.card_alfa);
            cards2.add(card2);
        }
        CardArrayAdapter mCardArrayAdapter2 = new CardArrayAdapter(getActivity(),cards2);
		
		
		list_lin2 = (CardListView) listlin2.findViewById(R.id.list_list2);
        if (list_lin2!=null){
//          list_lin2.setAdapter(mCardArrayAdapter);
            AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter2);
            animCardArrayAdapter.setAbsListView(list_lin2);
            list_lin2.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter2);
        }
		
		listViews.add(list_lin2);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setVisibility(View.VISIBLE);
		waitList.setVisibility(View.GONE);
		
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
				t1.setBackgroundResource(R.drawable.tab_left_focus);
				t2.setBackgroundResource(R.drawable.tab_right_nofocus);
				break;
			case 1:
				t2.setBackgroundResource(R.drawable.tab_right_focus);
				t1.setBackgroundResource(R.drawable.tab_left_nofocus);
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
	class ListThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("name", -1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			String temp = HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_BOOKS_URL, bookHandler, params);
			String imgURL;
			String imgName;
			salesbooks = JsonParse.getListMap("salesbooks", temp);
			starsbooks = JsonParse.getListMap("starsbooks", temp);
			salesNum = salesbooks.size();
			starsNum = starsbooks.size();
			Log.d("star",""+starsNum);
			salesBm = new Bitmap[salesNum];
			starsBm = new Bitmap[starsNum];
			for(int i = 0; i<salesNum; i++) {
				imgURL = salesbooks.get(i).get("imagePath").toString();
				imgName = salesbooks.get(i).get("imageName").toString();
				salesBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
						+ imgName);
			}
			for(int i = 0; i<starsNum; i++) {
				imgURL = starsbooks.get(i).get("imagePath").toString();
				imgName = starsbooks.get(i).get("imageName").toString();
				starsBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
						+ imgName);
			}
			
			myHandler.sendEmptyMessage(1);

		}
	}
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			InitViewPager();

		};
	};
}
