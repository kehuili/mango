package com.example.tools;

import android.graphics.Bitmap;

public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private String price;
	private String auther;
	private String salesVolume;
	private Bitmap cover;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}
	public Bitmap getCover(){
		return cover;	
	}
	public void setCover(Bitmap cover){
		this.cover = cover;
	}
	public String getAuther(){
		return auther;
	}
	public void setAuther(String auther){
		this.auther = auther;
	}
/*	public String getSalesVolume(){
		return salesVolume;
	}
	public void setSalesVolume(String salesVolume){
		this.salesVolume = salesVolume;
	}*/
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
