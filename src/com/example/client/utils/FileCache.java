package com.example.client.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import com.example.tools.ContextUtil;

import android.content.Context;

public class FileCache {
	private static Context context = ContextUtil.getInstance();
	public static void putFileCache(List<Map<String, Object>> list, String fileName) {
		
		try {
			FileOutputStream fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
//			byte[] bytes = list.getBytes();
//			fos.write(bytes);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);	
			objectOutputStream.writeObject(list);
			fos.close();
			} catch (Exception e) {
				
			}
	}
	
	public static List<Map<String, Object>> getFileCache(String fileName) {
		FileInputStream fis;
		//String s = "";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			fis = context.openFileInput(fileName);
//			int length = fis.available();
//			byte[] buff = new byte[5000];
//			fis.read(buff);
//			s = EncodingUtils.getString(buff, "UTF-8");
//			fis.close();
//			int hasRead = 0;
//			StringBuilder sb = new StringBuilder("");
//			while ((hasRead = fis.read(buff)) > 0) {
//				sb.append(new String(buff, 0, hasRead));
//			}
//			return sb.toString();
			ObjectInputStream objectInputStream = new ObjectInputStream(fis);
						
			list = (List<Map<String, Object>>)objectInputStream.readObject();
			} catch (Exception e) {
				
			}
		return list;
	}
}
