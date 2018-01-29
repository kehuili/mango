package com.example.client.utils;
/**
 * 描述：网络帮助类，主要存放各类URL和服务端IP地址设置
 * 作者：Mike.Fox
 * 时间：2014-2-18
 */
public class NetworkUtils {
	// 计算机映射的本机IP地址为127.0.0.1,Android映射的本机IP为10.0.2.2 	
	public static String DANGDANG_BASE_URL = "http://223.3.7.47/DDWebTest/";
	public static String DANGDANG_MOD_URL =DANGDANG_BASE_URL + "mod.json";
	public static String DANGDANG_CHECK_URL = DANGDANG_BASE_URL +"check.json";
	public static String DANGDANG_SEARCH_URL = DANGDANG_BASE_URL+"search.json";

	// 获取验证码的URL
	public static  String DANGDANG_CODE_URL = DANGDANG_BASE_URL
			// 真实环境URL
//						+ "code.jhtml";
			
			// 测试用的URL
			+"image.json";
	
	// 实施登陆验证的URL
	public static  String DANGDANG_LOGIN_URL = DANGDANG_BASE_URL
			+ "login.json";

	// 获取图书信息的URL
	public static  String DANGDANG_BOOKS_URL = DANGDANG_BASE_URL
			+ "books.json";

	// 获取评论信息的URL
	public static  String DANGDANG_BOOKCOMMENTS_URL = DANGDANG_BASE_URL
			+ "comments.json";

	// 购物车操作的URL
	public static  String DANGDANG_SHOPPING_URL = DANGDANG_BASE_URL
			+ "cart.json";

	// 获取订单所有信息的URL
	public static  String DANGDANG_ORDER_URL = DANGDANG_BASE_URL
			+ "orders.json";

	// 获取地址信息的URL
	public static  String DANGDANG_ADDRESS_URL = DANGDANG_BASE_URL
			+ "address.json";
	
	// 提交创建订单的URL
	public static  String DANGDANG_OREN_URL = DANGDANG_BASE_URL
			+ "orderconfirm.json";
	
	public static String  DANGDANG_REG_URL = DANGDANG_BASE_URL 
			+"register.json";
	/*
	 * 如果用户登录的时候进行ip和端口号设置，则会调用本方法
	 */
	public static void setBaseUrl(String ip,String port){
		// 如果ip和端口号为空
		if(ip.equals("")&&port.equals("")){
			return;
		}
		// 如果端口号为空
		if(port.equals("")){
			DANGDANG_BASE_URL = "http://"+ip+"/";
			// 如果端口号不为空
		}else{
			DANGDANG_BASE_URL = "http://"+ip+":"+port+"/";
		}
		// 更新全部的URL
		DANGDANG_CODE_URL = DANGDANG_BASE_URL
				+ "code.jhtml";
		DANGDANG_LOGIN_URL = DANGDANG_BASE_URL
				+ "login.json";
		DANGDANG_BOOKS_URL = DANGDANG_BASE_URL
				+ "books.json";
		DANGDANG_BOOKCOMMENTS_URL = DANGDANG_BASE_URL
				+ "comments.json";
		DANGDANG_SHOPPING_URL = DANGDANG_BASE_URL
				+ "cart.json";
		DANGDANG_ORDER_URL = DANGDANG_BASE_URL
				+ "orders.json";
		DANGDANG_ADDRESS_URL = DANGDANG_BASE_URL
				+ "address.json";
		DANGDANG_OREN_URL = DANGDANG_BASE_URL
				+ "orderconfirm.json";
		DANGDANG_REG_URL = DANGDANG_BASE_URL 
				+"register.json";
		DANGDANG_MOD_URL =DANGDANG_BASE_URL + "mod.json";
		DANGDANG_CHECK_URL = DANGDANG_BASE_URL +"check.json";
		DANGDANG_SEARCH_URL = DANGDANG_BASE_URL+"search.json";
	}
	
}
