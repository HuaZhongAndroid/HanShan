package com.lib.tool;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.bm.app.App;
import com.google.gson.Gson;

/**
 * SharedPreferencesHelper.java 提供存储本地数据的方法
 *
 */
public class SharedPreferencesHelper {
	public static final String SPM_FILE_NAME = "app_data";
	/**
	 * 保存布尔值到SharedPreferences
	 * @param key
	 * @param b
	 */
	public static void saveBoolean(String key,boolean b) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putBoolean(key, b);
		editer.commit();
	}

	/**
	 * 保存对象为JSON
	 * @param key
	 * @param b
	 */
	public static void saveJSON(String key, Object o) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		String json = new Gson().toJson(o);
		Log.i("spm", json);
		editer.putString(key, json);
		editer.commit();
	}

	/**
	 * 获取JSON对象
	 */
	public static <T> T getJSON(String key, Class<T> clazz) {
		if(!contain(key)) return null;
		String json = getString(key);
		return new Gson().fromJson(json, clazz);
	}

	/**
	 * 保存浮点数到SharedPreferences
	 * @param key
	 * @param f
	 */
	public static void saveFloat(String key,float f) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putFloat(key, f);
		editer.commit();
	}

	/**
	 * 保存整数到SharedPreferences
	 */
	public static void saveInt(String key, int i) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putInt(key, i);
		editer.commit();
	}

	/**
	 * 保存长整数到SharedPreferences
	 * */
	public static void saveLong(String key, long l) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putLong(key, l);
		editer.commit();
	}

	/**
	 * 保存字符串到SharedPreferences
	 * */
	public static void saveString(String key, String s) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putString(key, s);
		editer.commit();
	}

	/**
	 * 删除
	 * */
	public static void remove(String[] key) {
		if(key == null) return;
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		for(String k : key){
			editer.remove(k);
		}
		editer.commit();
	}

	/**
	 * 删除
	 * */
	public static void remove(String key) {
		if(key == null) return;
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.remove(key);
		editer.commit();
	}

	/**
	 * 从SharedPreferences读取布尔值
	 * */
	public static boolean getBoolean(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getBoolean(key, false);
	}

	/**
	 * 从SharedPreferences读取浮点数
	 * */
	public static float getFloat(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getFloat(key, 0);
	}

	/**
	 * 从SharedPreferences获取整数
	 * */
	public static int getInt(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getInt(key, -1);
	}

	/**
	 * 从SharedPreferences读取长整数
	 * */
	public static long getLong(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getLong(key, 0);
	}

	/**
	 * 从SharedPreferences读取字符串
	 * */
	public static String getString(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getString(key, null);
	}
	
	/**
	 * 从SharedPreferences读取字符串
	 * */
	public static String getString(String key, String defautValue) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getString(key, defautValue);
	}

	/**
	 * 从SharedPreferences读取配置文件的所有数据
	 * */
	public static Map<String, ?> getMap() {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getAll();
	}

	/**
	 * 检查对应的值是否存在
	 * @param key
	 * @return
	 */
	public static boolean contain(String key) {
		SharedPreferences sp = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}
	
	/**
	 *保存首页进入时的parentID
	 * @param key
	 * @param praId
	 */
	public static void saveParentId(String key,int  praId) {
		Editor editer = App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).edit();
		editer.putInt(key, praId);
		editer.commit();
	}
	
	/**
	 *得到首页进入时的parentID
	 * @param key
	 * @param praId
	 */
	public static int getParentId(String key) {
		return App.getInstance().getApplicationContext().getSharedPreferences(SPM_FILE_NAME, 0).getInt(key, 0);
	}
}
