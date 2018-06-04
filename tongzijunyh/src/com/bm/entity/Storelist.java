package com.bm.entity;

import java.io.Serializable;

/**
 * 门店
 * @author jiangsh
 *
 */
public class Storelist implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5968564129612812118L;
	/**
	 * 图片路径
	 */
	public String logo;
	/**
	 * 满意度评分分值：0 - 5
	 */
	public String rankLogistics;
	/**
	 * 门店id
	 */
	public String storeId;
	
	/**
	 * 门店名称
	 */
	public String storeName;
	
	public String titleMultiUrl;
	
	public String lon;//经度 
	public String lat;//纬度

	public String tel; //电话
	public String address; //地址
	public String distance; //距离
	public String remark; //详情

	public String acrossImage; //课程内用的图
	public String imgUrl; //详情传用图
}
