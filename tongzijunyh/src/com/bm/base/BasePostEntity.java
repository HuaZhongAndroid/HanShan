package com.bm.base;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 基类 封装http请求时提交的参数<br />
 * Service中请求网络时，可以将参数封装到这里，也可以单独写
 * 
 *
 *
 *
 *
 */
public class BasePostEntity implements Serializable {
	private static final long serialVersionUID = 2895146521702870678L;

	/**
	 * 转换成map供Http使用
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> toMap() {
		Gson g = new GsonBuilder().create();
		return g.fromJson(g.toJson(this), HashMap.class);
	}
}
