package com.bm.entity;

import java.io.Serializable;

public class StoreInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3688717860362441267L;

	/**
	 * 门店id
	 */
	public String storeId;
	/**
	 * 门店名
	 */
	public String storeName;
	/**
	 * 门店url
	 */
	public String titleMultiUrl;
	/**
	 * 电话
	 */
	public String tel;
	/**
	 * 门店地址
	 */
	public String address;
	/**
	 * 经度
	 */
	public String lon;
	/**
	 *纬度
	 */
	public String lat;
}
