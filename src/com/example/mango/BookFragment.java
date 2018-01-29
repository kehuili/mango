package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.widget.RemarkCard;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

@SuppressLint("ValidFragment")
public class BookFragment extends Fragment {

	Bitmap bm;
	ExceptionHandler bookHandler = new ExceptionHandler();
	JSONObject params = new JSONObject();
	ImageView bookImage = null;
	TextView bookName = null;
	TextView bookAuther = null;
	TextView bookPrice = null;
	RatingBar bookStar = null;
	TextView bookIntro = null;
	TextView bookCatalog = null;
	private Map<String, Object> book = new HashMap<String, Object>();
	private List<Map<String, Object>> comments = new ArrayList<Map<String, Object>>();
	private AsyncBitmapLoader abl = new AsyncBitmapLoader();

	private int bookId = 1;
	private static final int TAB_COUNT = 2;
	private static final int TAB_ONE = 0;
	private static final int TAB_TWO = 1;
	private int currentItem = 1;
	private List<View> listViews;
	CardListView remark_list = null;
	View bookContent, bookRemark = null;
	View Book = null;
	ChangeThread changethread = null;

	TextView t1, t2 = null;
	ImageView img01, img02 = null;
	Button add, buy = null;
	LinearLayout waitBook = null;
	LinearLayout noneRemark  = null;

	private ViewPager viewPager; // android-support-v4中的滑动组件

	// private BookAdapter bookAdapter;
	public BookFragment(int bookId) {
		this.bookId = bookId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		changethread = new ChangeThread();
		changethread.start();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Book = inflater.inflate(R.layout.book, container, false);
		bookContent = inflater.inflate(R.layout.book_content, container, false);
		bookRemark = inflater.inflate(R.layout.book_remark, container, false);
		add = (Button) Book.findViewById(R.id.add_to_cart);
		buy = (Button) Book.findViewById(R.id.buy_now);
		waitBook = (LinearLayout) Book.findViewById(R.id.wait_book);
		remark_list = (CardListView) bookRemark.findViewById(R.id.remark_list);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// changethread = new ChangeThread();
				// changethread.start();
				AddThread addThread = new AddThread();
				addThread.start();

			}
		});

		buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// changethread = new ChangeThread();
				// changethread.start();

			}
		});
		// bookAdapter = new BookAdapter(getActivity().getFragmentManager());

		t1 = (TextView) Book.findViewById(R.id.tab01);
		t1.setOnClickListener(new MyOnClickListener(0));
		t2 = (TextView) Book.findViewById(R.id.tab02);
		t2.setOnClickListener(new MyOnClickListener(1));

		return Book;
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

	class AddThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("op", "add");
				params.put("bookid", bookId);
				params.put("cusid",
						getActivity().getIntent().getIntExtra("cusid", 0));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			String temp = HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_SHOPPING_URL, bookHandler, params);

			JSONObject ok;
			try {
				ok = new JSONObject(temp);

				if (ok.getBoolean("isOk") == true) {
					bookHandler.showMessage("添加成功");
				} else {
					bookHandler.showMessage("添加失败，再来一次");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class ChangeThread extends Thread {
		@Override
		public void run() {
			try {
				params.put("name", bookId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			book = JsonParse.getMap(HttpUtils.getJsonContent(
					NetworkUtils.DANGDANG_BOOKS_URL, bookHandler, params));
			String imgURL = book.get("imagePath").toString();
			String imgName = book.get("imageName").toString();
			bm = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
					+ imgName);
			try {
				params.remove("name");
				params.put("bookId", bookId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				bookHandler.showMessage("返回参数错误");
			}
			comments = JsonParse.getListMap("comments", HttpUtils
					.getJsonContent(NetworkUtils.DANGDANG_BOOKCOMMENTS_URL,
							bookHandler, params));
			myHandler.sendEmptyMessage(1);

		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			waitBook.setVisibility(View.GONE);
			viewPager = (ViewPager) Book.findViewById(R.id.pager);

			bookImage = (ImageView) bookContent.findViewById(R.id.imgbook);

			bookImage.setImageBitmap(bm);

			bookName = (TextView) bookContent.findViewById(R.id.name);
			bookName.setText(book.get("bookName").toString());

			bookAuther = (TextView) bookContent.findViewById(R.id.writer);
			bookAuther.setText(book.get("author").toString());

			bookPrice = (TextView) bookContent.findViewById(R.id.price);
			bookPrice.setText(book.get("price").toString());

			bookStar = (RatingBar) bookContent.findViewById(R.id.ratingBar1);
			bookStar.setRating(Float.parseFloat(book.get("starnum").toString()));

			bookIntro = (TextView) bookContent.findViewById(R.id.content1);
			bookIntro.setText(book.get("introduction").toString());

			bookCatalog = (TextView) bookContent.findViewById(R.id.content2);
			bookCatalog.setText(book.get("catalog").toString());

			listViews = new ArrayList<View>();
			listViews.add(bookContent);

			// remark

			if ((Integer) book.get("commentnum") != 0) {
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				Log.d("com", book.get("commentnum").toString());
				ArrayList<Card> cards = new ArrayList<Card>();
				for (int i = 0; i < (Integer) book.get("commentnum"); i++) {
					RemarkCard card = new RemarkCard(getActivity());
					card.setCustName(comments.get(i).get("customerName")
							.toString());
					card.setContent(comments.get(i).get("content").toString());
					card.setDate(comments.get(i).get("contentDate").toString());
					card.setRating(Float.parseFloat(comments.get(i).get("star")
							.toString()));

					card.count = i;

					// Only for test, change some icon

					card.init();
					card.setBackgroundResourceId(R.drawable.card_alfa);
					cards.add(card);
				}
				CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(
						getActivity(), cards);

				
				if (remark_list != null) {
					AnimationAdapter animCardArrayAdapter = new ScaleInAnimationAdapter(
							mCardArrayAdapter);
					animCardArrayAdapter.setAbsListView(remark_list);
					remark_list.setExternalAdapter(animCardArrayAdapter,
							mCardArrayAdapter);
				}
				
			}else{
				noneRemark = (LinearLayout) bookRemark.findViewById(R.id.none_remark);  
				noneRemark.setVisibility(View.VISIBLE);
				remark_list.setVisibility(View.GONE);
			}
			listViews.add(bookRemark);

			// viewPager.setOnPageChangeListener(listener);
			viewPager.setAdapter(new MyPagerAdapter(listViews));
			viewPager.setCurrentItem(0);
			viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

			viewPager.setVisibility(View.VISIBLE);

		};
	};

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);

		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// Animation animation = null;
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
			// currIndex = arg0;
			// animation.setFillAfter(true);// True:图片停在动画结束位置
			// animation.setDuration(300);
			// cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
