package com.bm.entity;

import com.bm.base.BasePostEntity;

public class Order extends BasePostEntity {
	 private static final long serialVersionUID = -7819498102668783872L;

//	public String goodsMoney;
//	public String 	goodsId;
//	public String babyId;
//	public String 	orderNumber;
	public String type;

	public String realName;
	public String storeName;
	public String goodsTime;

	//外加自己赋值
	public String goodsType;
	public String babyName;


//	public String goodsName;
//	public String 		isTraffic;
//	public String storeId;

	
	/**
	 *
	 *订单ID
	 */
	public String orderId;
	/**
	 *
	 *
	 */
	public String orderNumber;
	/**
	 *用户ID
	 *
	 */
	public String userid;
	/**
	 *
	 *课程ID
	 */
	public String goodsId;
	/**
	 *payStatus
	 *
	 */
	public String bookStatus;
	/**
	 *
	 *支付金额
	 */
	public String payMoney;
	/**
	 *
	 *支付时间
	 */
	public String payTime;
	/**
	 *
	 *
	 */
	public String mtime;
	/**
	 *
	 *创建时间
	 */
	public String ctime;

	public String goodsMoney; //商品金额
	public String totalMoney;  //总金额

	public String isTraffic; //是否需要交通费 1是 0否

	/**
	 *
	 *
	 */
	public String cuser;
	/**
	 *
	 *
	 */
	public String muser;
	/**
	 *
	 *
	 */
	public String goodsName;
	/**
	 *
	 *
	 */
	public String goodsPrice;
	/**
	 *
	 *
	 */
	public String address;
	/**
	 *
	 *
	 */
	public String beginDate;
	
	public String endDate;
	
	public String payBeginDate;
	/**
	 *
	 *
	 */
	public String payEndDate;
	/**
	 *
	 *
	 */
	public String userName;
	/**
	 *
	 *
	 */
	public String statusTime;
	
	/**
	 * 状态  0 减少钱   1 增加钱
	 */
	public String changeStatus;
	
	/**
	 * 消费金额
	 */
	public String paymentAccount;
	/**
	 * 课程名称
	 */
	public String integralName;
	/**
	 * 消费时间
	 */
	public String cdate;
	/**
	 * 积分
	 */
	public String integral;
	/**
	 * 积分类别 1：购买赠送  2：评价赠送
	 */
	public String integralType;
	
	public String cDate;

	public String babyId;
	
	/**
	 * 优惠券
	 */
	public String couponMoney;
	
	public String storeId; //门店id
	
	
	//微信
	public String appid;
	public String prepay_id;
	public String nonce_str;
	public String timestamp;
	public String mch_id;
	public String sign;
	public String return_msg;
	public String return_code;
	public String trade_type;





}
