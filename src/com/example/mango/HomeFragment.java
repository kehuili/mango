package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.mango.AddressFragment.AddressAdapter;
import com.example.tools.BitmapUtil;
import com.example.tools.ContextUtil;
import com.example.widget.GalleryFlow;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class HomeFragment extends Fragment implements OnClickListener {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	private JSONObject params = new JSONObject();
	private ExceptionHandler bookHandler = new ExceptionHandler();
	private AsyncBitmapLoader abl = new AsyncBitmapLoader();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	GalleryFlow mGallery = null;
	ArrayList<BitmapDrawable> mBitmaps = new ArrayList<BitmapDrawable>();
	View rootView = null;
	Button search = null;
	LinearLayout waitHome  = null;

	// 图片左右滑动
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private Bitmap[] bm;
	private List<View> dots; // 图片标题正文的那些点

	private TextView tv_title;
	private int currentItem = 300; // 当前图片的索引号

	private static final String ARG_SECTION_NUMBER = "section_number";

	private ScheduledExecutorService scheduledExecutorService;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);
			// 切换当前显示的图片
		};
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(getActivity(), "Correct!",
							Toast.LENGTH_SHORT).show();
					search.setVisibility(View.INVISIBLE);
					BookFragment bookFragment = new BookFragment((Integer)list.get(position % 5).get("bookId"));
					FragmentTransaction transaction = getFragmentManager()
							.beginTransaction();
					transaction.replace(R.id.mainfragment, bookFragment);
					transaction.addToBackStack(null);
					transaction.commit();
				}
			};
			generateBitmaps();
			mGallery = (GalleryFlow) rootView.findViewById(R.id.gallery1);
			mGallery.setSpacing(-20);
			mGallery.setFadingEdgeLength(0);
			mGallery.setGravity(Gravity.CENTER);
			mGallery.setAdapter(new GalleryAdapter());
			mGallery.setMaxZoom(0);
			mGallery.setSelection(mBitmaps.size() * 100);
			mGallery.setUnselectedAlpha(0.3f);
			mGallery.setOnItemClickListener(listener);
			waitHome.setVisibility(View.GONE);
			mGallery.setVisibility(View.VISIBLE);
		};
	};

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static HomeFragment newInstance(int sectionNumber) {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public HomeFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		bm = new Bitmap[5];
		NewBookThread newBookThread = new NewBookThread();
		newBookThread.start();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		search = (Button) getActivity().findViewById(R.id.search);
		waitHome = (LinearLayout) rootView.findViewById(R.id.wait_home);
		imageResId = new int[] { R.drawable.ac1, R.drawable.ac2, R.drawable.ac3 };
		titles = new String[imageResId.length];
		titles[0] = "精美童书  5折封顶";
		titles[1] = "开学装备一站购齐";
		titles[2] = "9月新书速递";

		imageViews = new ArrayList<ImageView>();
		// 初始化viewpager图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(rootView.findViewById(R.id.v_dot0));
		dots.add(rootView.findViewById(R.id.v_dot1));
		dots.add(rootView.findViewById(R.id.v_dot2));

		tv_title = (TextView) rootView.findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);

		viewPager = (ViewPager) rootView.findViewById(R.id.image_slide_page);
		// 设置填充ViewPager页面的适配器
		viewPager.setAdapter(new MyAdapter());
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setCurrentItem(imageResId.length * 100);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		return rootView;
	}

	public void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 2,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	public void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	// 换行切换任务
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem);
				currentItem++;
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	// 当ViewPager中页面的状态发生改变时调用
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 300;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position % imageResId.length]);
			dots.get(oldPosition % imageResId.length).setBackgroundResource(
					R.drawable.dot_normal);
			dots.get(position % imageResId.length).setBackgroundResource(
					R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}


	// 填充ViewPager页面的适配器
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
			// return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int pos) {

			View view = imageViews.get(pos % imageResId.length);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "activity!",
							Toast.LENGTH_SHORT).show();
				}
			});
			try {
				((ViewPager) arg0).addView(view);
			} catch (Exception e) {

			}

			return view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
//			 ((ViewPager) arg0).removeView(imageViews.get(arg1 %
//			 imageResId.length));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
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

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	// 初始化gallery图片资源
	private void generateBitmaps() {
		for (int i = 0; i < 5; i++) {
			
			String bookName = list.get(i).get("bookName").toString();

			Bitmap bitmap = createReflectedBitmap(bm[i], bookName);
			if (null != bitmap) {
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				drawable.setAntiAlias(true);
				mBitmaps.add(drawable);
			}
		}
	}

	// gallery图片效果制作
	private Bitmap createReflectedBitmap(Bitmap bm, String s) {
		// Drawable drawable = getResources().getDrawable(resId);
		// if (drawable instanceof BitmapDrawable) {
		Bitmap reflectedBitmap = BitmapUtil.createReflectedBitmap(bm, s);

		return reflectedBitmap;

	}

	// GalleryAdapter
	private class GalleryAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
			// return mBitmaps.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = new MyImageView(getActivity());
				convertView.setLayoutParams(new Gallery.LayoutParams(280,
						184 * 3));
			}

			ImageView imageView = (ImageView) convertView;
			imageView.setImageDrawable(mBitmaps.get((position)
					% mBitmaps.size()));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackgroundColor(Color.alpha(1));

			return imageView;
		}
	}

	// gallerry attr
	private class MyImageView extends ImageView {
		public MyImageView(Context context) {
			this(context, null);
		}

		public MyImageView(Context context, AttributeSet attrs) {
			super(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
		}
	}

	private class NewBookThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String path = new String();
			String imageName = new String();
				try {
					params.put("name", -3);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					bookHandler.showMessage("返回参数错误");
				}
				list = JsonParse.getListMap("books", HttpUtils.getJsonContent(
						NetworkUtils.DANGDANG_BOOKS_URL, bookHandler, params));
				for (int i = 0; i < 5; i++) {
					path = list.get(i).get("imagePath").toString();
					imageName = list.get(i).get("imageName").toString();
					String imgURL =NetworkUtils.DANGDANG_BASE_URL + path + imageName;

					bm[i] = abl.loadBitmap(imgURL);
			}
			myHandler.sendEmptyMessage(1);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}