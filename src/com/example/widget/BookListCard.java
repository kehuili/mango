package com.example.widget;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mango.R;

public class BookListCard extends Card{
	protected ImageView mImage;
	protected TextView mTitle;
	protected TextView mPrice;
    protected TextView mWriter;
    protected RatingBar mRatingBar;
    protected int resourceIdThumbnail;
    public int count;
    public int bookId;

    protected String title;
    protected String writer;
    protected String price;
    protected float rating;
    private Bitmap bm;


    public BookListCard(Context context) {
        this(context, R.layout.list_list1);
    }

    public BookListCard(Context context, int innerLayout) {
        super(context, innerLayout);
        //init();
    }

    public void init() {

        //Only for test, some cards have different clickListeners
        if (count>5 && count<13){

            setTitle(title + " Swipe enabled");
            setSwipeable(true);
            /*setOnSwipeListener(new OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    Toast.makeText(getContext(), "Removed card=" + title, Toast.LENGTH_SHORT).show();
                }
            });*/
            setOnSwipeListener(new OnSwipeListener() {
				
				@Override
				public void onSwipe(Card card) {
					// TODO Auto-generated method stub
					Toast.makeText(getContext(), "Removed card=" + title, Toast.LENGTH_SHORT).show();
	                
				}
			});
        }
    }
        
        
    
   
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
    	mImage =(ImageView) parent.findViewById(R.id.bookImg);
        mTitle = (TextView) parent.findViewById(R.id.name);
        mWriter = (TextView) parent.findViewById(R.id.writer);
        mPrice = (TextView) parent.findViewById(R.id.price);
        
        if(mImage != null & bm != null){
        	mImage.setImageBitmap(bm);
        	
        }else {
        	mImage.setBackgroundResource(R.drawable.book);
        }
        
        if (mTitle != null)
            mTitle.setText(title);

        if (mWriter != null)
            mWriter.setText(writer);
        
        if(mPrice != null)
        	mPrice.setText(price);

    }

    public void setBookId(int bookId){
    	this.bookId = bookId;
    }
    
    public int getBookId(){
    	return bookId;
    }
    
    public String getTitle(){
        return title;
    }

    public void setBitmap(Bitmap bm) {
    	this.bm = bm;
    }
    
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
    
    public String getPrice(){
    	return price;
    }
    
    public void setPrice(String price){
    	this.price = price;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getResourceIdThumbnail() {
        return resourceIdThumbnail;
    }

    public void setResourceIdThumbnail(int resourceIdThumbnail) {
        this.resourceIdThumbnail = resourceIdThumbnail;
    }
}

