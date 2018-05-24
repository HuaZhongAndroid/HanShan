package com.bm.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * 
 * 
 * base adapter
 * @author Administrator
 *
 * @param <T>
 */
public abstract class BaseAd<T> extends BaseAdapter {
	protected Context context;
	protected LayoutInflater mInflater;
	protected ArrayList<T> mList;

	/**
	 * 设置上下文
	 * 
	 */
	public void setActivity(Context context,List<T> prolist) {
		this.context = context;
		this.mList = (ArrayList<T>) prolist;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return setConvertView(convertView, position);
	}

	/**
	 * 设置item布局
	 * 
	 * @return
	 */
	protected abstract View setConvertView(View convertView, int position);
	
	
	/**
	 *   * 判断值是否为空   * @param arg   * @return  
	 */
	public static String getNullData(String arg) {
		return arg == null ? "" : arg;
	}
	public static String getNullDataForO(String arg) {
		return arg == null ? "0" : arg;
	}
	 /**
	   * 保留小数
	   * @return
	   */
	  public static String getFormat(String strArg,int digit){
		    BigDecimal bd = new BigDecimal(strArg);
			bd = bd.setScale(digit,BigDecimal.ROUND_HALF_UP); 
			return bd.toString();
	  }
}
