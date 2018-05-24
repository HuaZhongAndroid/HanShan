package com.bm.entity;

/**
 * 优惠券
 *
 */
public class Youhuiquan {

	public String pkid; //我的优惠券ID
	public String money; //金额
	public String userid; //用户id
	public String couponid; //优惠券ID
	public String couponGroupid; //优惠券组ID
	public String orderid;
	public String useTime;
	public String status; //使用状态（10、未领取20、已领取30、已使用90、已过期）
	public String endDate; //结束时间
	public String beginDate; //开始时间
	public String goodsType; //适用范围（-1通用   1 城市营地   2  户外俱乐部  3暑期大露营）
	
	
	public String unclaimedQuantity; //未领取数量
	public String getQuantity; //领取数量
	public String useQuantity; //使用数量
	public String validFlag; //状态（10、未领取20、已领取30、已使用90、已过期）
}
