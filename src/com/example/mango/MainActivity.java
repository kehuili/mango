package com.example.mango;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.tools.ContextUtil;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class MainActivity extends SherlockActivity implements OnClickListener {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	FragmentTransaction transaction = null;
	ResideMenu resideMenu;
	RelativeLayout home, list, sort, cart;
	
	LinearLayout bot_menu = null;
	int show =1;
	int left = 0;
	private ResideMenuItem itemOrder;
	private ResideMenuItem itemProfile;
	private ResideMenuItem itemAddress;
	private ResideMenuItem itemSettings;
	
	List<Map<String,Object>> address;
	

	TextView title = null;
	Button search ,left_menu= null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.mainfragment, new HomeFragment()).commit();
		}
		
		bot_menu = (LinearLayout)findViewById(R.id.botmenu);
		list = (RelativeLayout) findViewById(R.id.list);
		home = (RelativeLayout) findViewById(R.id.home);
		cart = (RelativeLayout) findViewById(R.id.cart);
		sort = (RelativeLayout) findViewById(R.id.sort);
		title = (TextView) findViewById(R.id.titlebar);
		search = (Button)findViewById(R.id.search);
		search.setOnClickListener(this);
		cart.setOnClickListener(this);
		home.setOnClickListener(this);
		list.setOnClickListener(this);
		sort.setOnClickListener(this);

		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.main6);
		resideMenu.attachToActivity(this);
		resideMenu.setScaleValue(0.6f);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
		resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		// create menu items;
		
		itemProfile = new ResideMenuItem(this, R.drawable.icon_profile,
				"个人信息");
		itemOrder = new ResideMenuItem(this, R.drawable.icon_calendar, "所有订单");
		itemAddress = new ResideMenuItem(this, R.drawable.icon_home,
				"收货地址");
		itemSettings = new ResideMenuItem(this, R.drawable.icon_settings,
				"设置");

		itemProfile.setOnClickListener(this);
		itemOrder.setOnClickListener(this);
		itemAddress.setOnClickListener(this);
		itemSettings.setOnClickListener(this);

		resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemOrder, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemAddress, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_LEFT);
		
		left_menu = (Button)findViewById(R.id.title_bar_left_menu);
		left_menu.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(show == 1){
							resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
						}else{
							onBackPressed();
						}
					}
				});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		search.setVisibility(View.VISIBLE);
		title.setText("Mango");
		left_menu.setBackgroundResource(R.drawable.titlebar_menu_selector);
		show = 1;
		HomeFragment homeFragment = new HomeFragment();
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.mainfragment, homeFragment);
		transaction.commit();
		super.onBackPressed();
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU){
			if(left == 0){
			resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			left = 1;
			}else{
			resideMenu.closeMenu();
			left = 0;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.list:
			search.setVisibility(View.VISIBLE);
			title.setText("排行榜");
			left_menu.setBackgroundResource(R.drawable.titlebar_menu_selector);
			show = 1;
			ListFragment listFragment = new ListFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, listFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.cart:
			title.setText("购物车");
			search.setVisibility(View.INVISIBLE);
			left_menu.setBackgroundResource(R.drawable.titlebar_menu_selector);
			show = 1;
			CartFragment cartFragment = new CartFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, cartFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.sort:
			title.setText("分类");
			search.setVisibility(View.VISIBLE);
			left_menu.setBackgroundResource(R.drawable.titlebar_menu_selector);
			show = 1;
			SortFragment sortFragment = new SortFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, sortFragment);
			//transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.home:
			search.setVisibility(View.VISIBLE);
			title.setText("Mango");
			left_menu.setBackgroundResource(R.drawable.titlebar_menu_selector);
			show = 1;
			HomeFragment homeFragment = new HomeFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, homeFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case R.id.search:
			Intent intent = new Intent(MainActivity.this,SearchActivity.class);
    		startActivity(intent);
			break;
		default:
			break;
		}
		if (view == itemOrder){
			search.setVisibility(View.INVISIBLE);
			title.setText("订单管理");
			left_menu.setBackgroundResource(R.drawable.abs__ic_ab_back_holo_dark);
			show = 0;
        	OrderFragment orderFragment = new OrderFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, orderFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			resideMenu.closeMenu();
        }else if (view == itemProfile){
        	search.setVisibility(View.INVISIBLE);
			title.setText("个人信息管理");
			left_menu.setBackgroundResource(R.drawable.abs__ic_ab_back_holo_dark);
			show = 0;
        	InfoFragment infoFragment = new InfoFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, infoFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			resideMenu.closeMenu();
        }else  if (view == itemAddress){
        	search.setVisibility(View.INVISIBLE);
			title.setText("收货地址管理");
			left_menu.setBackgroundResource(R.drawable.abs__ic_ab_back_holo_dark);
			show = 0;
			AddressFragment addressFragment = new AddressFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, addressFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			resideMenu.closeMenu();
        }else if (view == itemSettings){
        	search.setVisibility(View.INVISIBLE);
			title.setText("设置");
			left_menu.setBackgroundResource(R.drawable.abs__ic_ab_back_holo_dark);
			show = 0;
        	SetFragment setFragment = new SetFragment();
			transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.mainfragment, setFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			resideMenu.closeMenu();
        }
        
	}
	
}
