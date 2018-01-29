package com.example.widget;

import it.gmariotti.cardslib.library.internal.Card;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mango.R;

public class RemarkCard extends Card{
	protected TextView mCustName;
	protected TextView mContent;
    protected TextView mDate;
    protected RatingBar mRatingBar;
    public int count;

    protected String custName;
    protected String content;
    protected String date;
    protected float rating;


    public RemarkCard(Context context) {
        this(context, R.layout.remark_list);
    }

    public RemarkCard(Context context, int innerLayout) {
        super(context, innerLayout);
        //init();
    }

    public void init() {

        //Only for test, some cards have different clickListeners
    }
        
        
    
   
    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
       mCustName = (TextView) parent.findViewById(R.id.remark_custname);
        mContent = (TextView) parent.findViewById(R.id.remark_main);
        mDate = (TextView) parent.findViewById(R.id.remark_date);
        mRatingBar = (RatingBar) parent.findViewById(R.id.remark_star);
        
        if (mCustName != null)
            mCustName.setText(custName);

        if (mContent != null)
            mContent.setText(content);
        
        if(mDate != null)
        	mDate.setText(date);
        
        if (mRatingBar != null) {
            mRatingBar.setNumStars(5);
            mRatingBar.setMax(5);
            mRatingBar.setStepSize(0.5f);
            mRatingBar.setRating(rating);
        }
        
    }


    public String getCustName(){
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getDate(){
    	return date;
    }
    
    public void setDate(String date){
    	this.date = date;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}

