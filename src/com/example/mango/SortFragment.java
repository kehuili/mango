package com.example.mango;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.tools.ChineseSpelling;
import com.example.tools.PinyinComparator;
import com.example.tools.SortAdapter;
import com.example.tools.SortModel;
import com.example.widget.ClearEditText;
import com.example.widget.SideBar;
import com.example.widget.SideBar.OnTouchingLetterChangedListener;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

public class SortFragment extends Fragment {
	Bitmap[] bookCoverBm;
	private ExceptionHandler classifyHandler = new ExceptionHandler();
	private List<Map<String,Object>> mBook;
	private List<Map<String,Object>> sortBook;
	private String[] category = new String[] { "小说", "文艺", "青春", "少儿", "生活",
			"工具书", "人文社科", "教育", "科技", "管理", "励志/成功" };
	private int[] bookType = new int[] {1,2,3,5,6,11,7,8,4};
	private AsyncBitmapLoader abl = new AsyncBitmapLoader();
	private String imgURL,imgName;
	private int offset = 0;// 动画图片的偏移量
	private int currIndex = 0;// 当前页卡的编号
	private int bmpW;
	
	View myView ,list01,sortRight= null;
	LinearLayout waitSort = null;
	FragmentTransaction transaction = null;
	
	private Context mContext;
	private List<View> listViews;
	private ViewPager mPager;
	private ImageView cursor;
	private TextView t1, t2, dialog;
	private AnimationAdapter mAnimAdapter = null;
	private ChineseSpelling chinesspelling = ChineseSpelling.getInstance();
	private SideBar sideBar;
	private ClearEditText mClearEditText;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator = new PinyinComparator();
	private SortAdapter adapter1;
	private ListView list1;
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			waitSort.setVisibility(View.GONE);
		
			mPager = (ViewPager) myView.findViewById(R.id.vPager);
			mPager.setVisibility(View.VISIBLE);
			listViews = new ArrayList<View>();

			//SourceDateList = filledData(category);
			//Collections.sort(SourceDateList, pinyinComparator);
			SourceDateList = filledData(category);
			Collections.sort(SourceDateList, pinyinComparator);
			
			listViews = new ArrayList<View>();

			List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < category.length; i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem.put("category", category[i]);
				listItems.add(listItem);
			}

			SimpleAdapter adapter2 = new SimpleAdapter(getActivity(), listItems,
					R.layout.sort_right_list, new String[] { "category" },
					new int[] { R.id.category });
			adapter1 = new SortAdapter(getActivity(), SourceDateList);
			
			//list1.setAdapter(adapter1);
			
			mAnimAdapter = new ScaleInAnimationAdapter(adapter1);
	        mAnimAdapter.setAbsListView(list1);
	        list1.setAdapter(mAnimAdapter);
			
			
			// mListView.setFastScrollEnabled(true);
			listViews.add(list01);
			ListView list2 = (ListView) sortRight.findViewById(R.id.classifyList);
			
//			mAnimAdapter = new ScaleInAnimationAdapter(adapter2);
//	        mAnimAdapter.setAbsListView(list2);
//	        list2.setAdapter(mAnimAdapter);
			list2.setAdapter(adapter2);
	        list2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "hsihsihsih", Toast.LENGTH_SHORT);
					SearchListFragment searchListFragment = new SearchListFragment(bookType[position]);
					transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.mainfragment, searchListFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}	
			});
	        list2.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "hsihsihsih", Toast.LENGTH_SHORT);
					SearchListFragment searchListFragment = new SearchListFragment(bookType[position]);
					transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.mainfragment, searchListFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
			});
			
			listViews.add(sortRight);
			/* listViews.add(mInflater.inflate(R.layout.page1, null)); */
			/* listViews.add(mInflater.inflate(R.layout.page2, null)); */
			mPager.setAdapter(new MyPagerAdapter(listViews));
			mPager.setCurrentItem(0);
			mPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
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
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

				}
			});
			
		};
	};

	public interface MyListener {
		public void showMessage(int index);
	}

	private MyListener myListener;

	public void onCreate(Bundle savedInstanceState) {
		bookThread book = new bookThread();
		book.start();
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 myView = inflater.inflate(R.layout.sort, container, false);
		 list01 = inflater.inflate(R.layout.page1, container, false);
		sortRight = inflater.inflate(R.layout.sort_right, container, false);
		LayoutInflater mInflater = getActivity().getLayoutInflater();
		waitSort = (LinearLayout) myView.findViewById(R.id.wait_sort);
		// return inflater.inflate(R.layout.activity_main, container,false);
		list1 = (ListView) list01.findViewById(R.id.listview);
		t1 = (TextView) myView.findViewById(R.id.Letter);
		t2 = (TextView) myView.findViewById(R.id.Classify);
		sideBar = (SideBar) list01.findViewById(R.id.sidrbar);
		dialog = (TextView) list01.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ¸Ã×ÖÄ¸Ê×´Î³öÏÖµÄÎ»ÖÃ
				int position = adapter1.getPositionForSection(s.charAt(0));
				if (position != -1) {
					list1.setSelection(position);
				}

			}
		});

		mClearEditText = (ClearEditText) list01.findViewById(R.id.filter_edit);
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// µ±ÊäÈë¿òÀïÃæµÄÖµÎª¿Õ£¬¸üÐÂÎªÔ­À´µÄÁÐ±í£¬·ñÔòÎª¹ýÂËÊý¾ÝÁÐ±í
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});


		return myView;

	}

	/**
	 * ÎªListViewÌî³äÊý¾Ý
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for (int i = 0; i < mBook.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(mBook.get(i).get("bookName").toString());
			sortModel.setPrice(mBook.get(i).get("price").toString());
			
			sortModel.setCover(bookCoverBm[i]);
			// sortModel.setSalesVolume("35");
			sortModel.setAuther(mBook.get(i).get("author").toString());
			// ºº×Ö×ª»»³ÉÆ´Òô
			String pinyin = chinesspelling.getSelling(mBook.get(i).get("bookName").toString());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// ÕýÔò±í´ïÊ½£¬ÅÐ¶ÏÊ××ÖÄ¸ÊÇ·ñÊÇÓ¢ÎÄ×ÖÄ¸
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * ¸ù¾ÝÊäÈë¿òÖÐµÄÖµÀ´¹ýÂËÊý¾Ý²¢¸üÐÂListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.toUpperCase().indexOf(
						filterStr.toString().toUpperCase()) != -1
						|| chinesspelling.getSelling(name).toUpperCase()
								.startsWith(filterStr.toString().toUpperCase())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// ¸ù¾Ýa-z½øÐÐÅÅÐò
		Collections.sort(filterDateList, pinyinComparator);
		adapter1.updateListView(filterDateList);
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
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

	/*
	 * public class MyOnPageChangeListener implements OnPageChangeListener {
	 * 
	 * int one = offset * 2 + bmpW; int two = one * 2;
	 * 
	 * @Override public void onPageSelected(int arg0) { Animation animation =
	 * null; switch (arg0) { case 0: if(currIndex == 1) animation = new
	 * TranslateAnimation(one, 0, 0, 0);
	 * 
	 * break; case 1: if (currIndex == 0) { animation = new
	 * TranslateAnimation(offset, one, 0, 0); } break;
	 * 
	 * } currIndex = arg0; animation.setFillAfter(true);
	 * animation.setDuration(300); cursor.startAnimation(animation); }
	 * 
	 * @Override public void onPageScrolled(int arg0, float arg1, int arg2) { }
	 * 
	 * @Override public void onPageScrollStateChanged(int arg0) { } }
	 */
	public class bookThread extends Thread{
		public void run(){
			JSONObject params = new JSONObject();
			try{
				params.put("name", -2);
			}catch(JSONException e1){
				classifyHandler.showMessage("信息获取错误");
			}
			
			mBook = JsonParse.getListMap("books", HttpUtils.getJsonContent(NetworkUtils.DANGDANG_BOOKS_URL, classifyHandler, params));
			bookCoverBm = new Bitmap[mBook.size()];
			for(int i=0;i<mBook.size();i++){
			imgURL = mBook.get(i).get("imagePath").toString();
			imgName = mBook.get(i).get("imageName").toString();
			bookCoverBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
					+ imgName);
			}
			myHandler.sendEmptyMessage(1);
		}
	}
	
}
