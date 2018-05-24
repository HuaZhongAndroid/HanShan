package com.bm.entity;



public class WeixinOrder {

	public Payinfo payinfo;
	public String recNumber;
	
	public class Payinfo
	{
		public String sign;
		public String timestamp;
		public String noncestr;
		public String partnerid;
		public String prepayid;
		public String packageValue;
		public String appid;
	}
}
