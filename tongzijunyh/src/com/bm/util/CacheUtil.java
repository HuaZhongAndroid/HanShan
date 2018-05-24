package com.bm.util;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CacheUtil {

	private static final String SPNAME = "CacheUtil_SPNAME";
	
	public static void save(Context context, String key,Map<String,String> map, Serializable object)
	{
		try{
			Gson gson = new GsonBuilder().create();
			key += gson.toJson(map);
			String serStr = gson.toJson(object);
	//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	//		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
	//				byteArrayOutputStream);
	//		objectOutputStream.writeObject(object);
	//		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
	//		objectOutputStream.close();
	//		byteArrayOutputStream.close();
			final SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
			sp.edit().putString(key, serStr).commit();
			Log.d("", "缓存成功");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static Object read(Context context, String key, Map<String,String> map, Type t)
	{
		try{
			Log.d("", "缓存读取中...");
			Gson gson = new GsonBuilder().create();
			key += gson.toJson(map);
			final SharedPreferences sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
			String str = sp.getString(key, null);
			if(str == null)
				return null;
			
	//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
	//		ObjectInputStream objectInputStream = new ObjectInputStream(
	//				byteArrayInputStream);
	//		Object o = objectInputStream.readObject();
	//		objectInputStream.close();
	//		byteArrayInputStream.close();
			
			Object o = gson.fromJson(str, t);
			
			Log.d("", "缓存读取完成");
			return o;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
