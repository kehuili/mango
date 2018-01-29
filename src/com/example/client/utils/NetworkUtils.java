package com.example.client.utils;
/**
 * ��������������࣬��Ҫ��Ÿ���URL�ͷ����IP��ַ����
 * ���ߣ�Mike.Fox
 * ʱ�䣺2014-2-18
 */
public class NetworkUtils {
	// �����ӳ��ı���IP��ַΪ127.0.0.1,Androidӳ��ı���IPΪ10.0.2.2 	
	public static String DANGDANG_BASE_URL = "http://223.3.7.47/DDWebTest/";
	public static String DANGDANG_MOD_URL =DANGDANG_BASE_URL + "mod.json";
	public static String DANGDANG_CHECK_URL = DANGDANG_BASE_URL +"check.json";
	public static String DANGDANG_SEARCH_URL = DANGDANG_BASE_URL+"search.json";

	// ��ȡ��֤���URL
	public static  String DANGDANG_CODE_URL = DANGDANG_BASE_URL
			// ��ʵ����URL
//						+ "code.jhtml";
			
			// �����õ�URL
			+"image.json";
	
	// ʵʩ��½��֤��URL
	public static  String DANGDANG_LOGIN_URL = DANGDANG_BASE_URL
			+ "login.json";

	// ��ȡͼ����Ϣ��URL
	public static  String DANGDANG_BOOKS_URL = DANGDANG_BASE_URL
			+ "books.json";

	// ��ȡ������Ϣ��URL
	public static  String DANGDANG_BOOKCOMMENTS_URL = DANGDANG_BASE_URL
			+ "comments.json";

	// ���ﳵ������URL
	public static  String DANGDANG_SHOPPING_URL = DANGDANG_BASE_URL
			+ "cart.json";

	// ��ȡ����������Ϣ��URL
	public static  String DANGDANG_ORDER_URL = DANGDANG_BASE_URL
			+ "orders.json";

	// ��ȡ��ַ��Ϣ��URL
	public static  String DANGDANG_ADDRESS_URL = DANGDANG_BASE_URL
			+ "address.json";
	
	// �ύ����������URL
	public static  String DANGDANG_OREN_URL = DANGDANG_BASE_URL
			+ "orderconfirm.json";
	
	public static String  DANGDANG_REG_URL = DANGDANG_BASE_URL 
			+"register.json";
	/*
	 * ����û���¼��ʱ�����ip�Ͷ˿ں����ã������ñ�����
	 */
	public static void setBaseUrl(String ip,String port){
		// ���ip�Ͷ˿ں�Ϊ��
		if(ip.equals("")&&port.equals("")){
			return;
		}
		// ����˿ں�Ϊ��
		if(port.equals("")){
			DANGDANG_BASE_URL = "http://"+ip+"/";
			// ����˿ںŲ�Ϊ��
		}else{
			DANGDANG_BASE_URL = "http://"+ip+":"+port+"/";
		}
		// ����ȫ����URL
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
