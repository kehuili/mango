package com.example.widget;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mango.R;

public class CartCard extends Card {
	
	protected ImageView mImage;
	protected TextView mTitle;
	protected TextView mPrice;
	protected TextView mSecondaryTitle;
	protected EditText mNumber;
	protected ImageButton mPlus;
	protected ImageButton mMius;
	protected int resourceIdThumbnail;
	public int count;
	public int bookId;

	protected Bitmap bm;
	protected String title;
	protected int number;
	protected String secondaryTitle;
	protected String price;
	protected float rating;

	PlusThread plusThread;
	MiusThread miusThread;
	boolean isOnLongClick, miusEnable, plusEnable;

	public CartCard(Context context) {
		this(context, R.layout.cart_list);
	}

	public CartCard(Context context, int innerLayout) {
		super(context, innerLayout);
		// init();
	}

	public void init() {


		// Only for test, some cards have different clickListeners
		if (count == 12) {

			setTitle(title + " No Click");
			setClickable(false);

		} else {
			setOnClickListener(new OnCardClickListener() {

				@Override
				public void onClick(Card card, View view) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(),
							"Partial click Listener card=" + title,
							Toast.LENGTH_SHORT).show();
				}
			});
			// Add ClickListener
			// Swipe
		}

	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {

		// Retrieve elements
		mImage = (ImageView) parent.findViewById(R.id.cart_bookImg);
		mTitle = (TextView) parent.findViewById(R.id.cart_name);
		mSecondaryTitle = (TextView) parent.findViewById(R.id.cart_writer);
		mPrice = (TextView) parent.findViewById(R.id.cart_price);
		mNumber = (EditText) parent.findViewById(R.id.cart_number);
		// mRatingBar = (RatingBar)
		// parent.findViewById(R.id.carddemo_myapps_main_inner_ratingBar);

		mPlus = (ImageButton) parent.findViewById(R.id.btn_up);
		mMius = (ImageButton) parent.findViewById(R.id.btn_down);

		if (mImage != null & bm != null) {
			mImage.setImageBitmap(bm);
		}else {
			mImage.setBackgroundResource(R.drawable.book);}

		if (mTitle != null)
			mTitle.setText(title);

		if (mSecondaryTitle != null)
			mSecondaryTitle.setText(secondaryTitle);

		if (mPrice != null)
			mPrice.setText(price);

		if (mNumber != null)
			mNumber.setText(number + "");

		mPlus.setOnTouchListener(new CompentOnTouch());
		mMius.setOnTouchListener(new CompentOnTouch());

	}

	// Touch事件
	class CompentOnTouch implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			// 这是btnMius下的一个层，为了增强易点击
			// 这里也写，是为了增强易点击性
			case R.id.btn_down:
				onTouchChange("mius", event.getAction());
				break;
			case R.id.btn_up:
				onTouchChange("plus", event.getAction());
				break;
			}
			return true;
		}
	}

	private void onTouchChange(String methodName, int eventAction) {

		// 按下松开分别对应启动停止减线程方法
		if ("mius".equals(methodName)) {

			if (eventAction == MotionEvent.ACTION_DOWN) {
				miusThread = new MiusThread();
				isOnLongClick = true;
				miusThread.start();
			} else if (eventAction == MotionEvent.ACTION_UP) {
				if (miusThread != null) {
					isOnLongClick = false;
				}
			} else if (eventAction == MotionEvent.ACTION_MOVE) {
				if (miusThread != null) {
					isOnLongClick = true;
				}
			}
		}
		// 按下松开分别对应启动停止加线程方法
		else if ("plus".equals(methodName)) {

			if (eventAction == MotionEvent.ACTION_DOWN) {
				plusThread = new PlusThread();
				isOnLongClick = true;
				plusThread.start();
			} else if (eventAction == MotionEvent.ACTION_UP) {
				if (plusThread != null) {
					isOnLongClick = false;
				}
			} else if (eventAction == MotionEvent.ACTION_MOVE) {
				if (plusThread != null) {
					isOnLongClick = true;
				}
			}
		}
	}

	// 减操作
	class MiusThread extends Thread {
		@Override
		public void run() {
			while (isOnLongClick) {
				try {
					Thread.sleep(200);
					myHandler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				super.run();
			}
		}
	}

	// 加操作
	class PlusThread extends Thread {
		@Override
		public void run() {
			while (isOnLongClick) {
				try {
					Thread.sleep(200);
					myHandler.sendEmptyMessage(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				super.run();
			}
		}
	}

	// 更新文本框的值
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (miusEnable) {
					mNumber.setText((new BigDecimal(mNumber.getText()
							.toString()).subtract(new BigDecimal("1"))) + "");
				}
				break;
			case 2:
				if (plusEnable) {
					mNumber.setText((new BigDecimal(mNumber.getText()
							.toString()).add(new BigDecimal("1"))) + "");
				}
				break;
			}
			setBtnEnable();
		};
	};

	private void setBtnEnable() {
		if (new BigDecimal(mNumber.getText().toString())
				.compareTo(new BigDecimal(1 + "")) > 0) {
			miusEnable = true;
		} else {
			miusEnable = false;
		}
		if (new BigDecimal(mNumber.getText().toString())
				.compareTo(new BigDecimal(100 + "")) < 0) {
			plusEnable = true;
		} else {
			plusEnable = false;
		}

	}

	public int getBookId(){
		return bookId;
	}
	
	public void setBookId(int bookId ){
		this.bookId = bookId; 
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSecondaryTitle() {
		return secondaryTitle;
	}

	public void setImage(Bitmap bm) {
		this.bm = bm;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	/*
	 * public float getRating() { return rating; }
	 * 
	 * public void setRating(float rating) { this.rating = rating; }
	 */

	/*
	 * public int getResourceIdThumbnail() { return resourceIdThumbnail; }
	 * 
	 * public void setResourceIdThumbnail(int resourceIdThumbnail) {
	 * this.resourceIdThumbnail = resourceIdThumbnail; }
	 */
}
