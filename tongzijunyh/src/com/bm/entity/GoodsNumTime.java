package com.bm.entity;

import java.io.Serializable;

public class GoodsNumTime implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4713200240017490759L;

	/**
	 * 课程id
	 */
	public String goodsId;
	/**
	 * 课程名称
	 */
	public String goodsName;
	/**
	 * 课程图片
	 */
	public String titleMultiUrl;
	/**
	 * 剩余人数
	 */
	public String personCount;
	/**
	 * 价格
	 */
	public String goodsPrice;
	/**
	 * 类型
	 */
	public String goodsType;
	/**
	 * 上课开始时间
	 */
	public String startTime;
	/**
	 * 上课结束时间
	 */
	public String endTime;
	/**
	 * 是否已经买过   0没买  1 已买
	 */
	public String isBought;
}
