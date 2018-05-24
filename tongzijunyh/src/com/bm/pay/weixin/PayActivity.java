package com.bm.pay.weixin;

import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.bm.base.BaseActivity;
import com.bm.entity.WeixinOrder;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 
 * 微信支付
 * @author wangqiang
 *
 */
public class PayActivity extends BaseActivity{
	
	
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static String APP_ID = "";
//    //商户号
//     public static final String MCH_ID = "1231715402";//1321379201
  //  API密钥，在商户平台设置
      public static final  String API_KEY="xajahw2008319sxsjwbq20160330wxzf";
    
    public static PayActivity intance;
	final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
	PayReq req;
	Map<String,String> resultunifiedorder;
	StringBuffer sb;
//	String nonce_str;
//	String orderSn;
//	String prepayId;
	
	private WeixinOrder order;
	
	
	/**CourseDetailAc课程详情   MyCourse我的课程   RechargeAc充值*/
	public static String pageType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rl_top.setVisibility(View.GONE);
		sb=new StringBuffer();
		intance=this;
		
		String info = getIntent().getStringExtra("info");
		order = new Gson().fromJson(info, WeixinOrder.class);
		APP_ID = order.payinfo.appid;
		
		api.registerApp(APP_ID);
		req = new PayReq();
//		nonce_str = info.payinfo;
//		prepayId = info.prepay_id;
//		orderSn = getIntent().getExtras().getString("orderSn");
		pageType=getIntent().getStringExtra("pageType");
		System.out.println("pageType:"+pageType);
		genPayReq();
	}
	
	
	private void genPayReq() {

		
		req.appId = order.payinfo.appid;
		req.partnerId = order.payinfo.partnerid;
		req.prepayId = order.payinfo.prepayid;
		req.packageValue = order.payinfo.packageValue;
		req.nonceStr = order.payinfo.noncestr;
		req.timeStamp = order.payinfo.timestamp;
		req.sign = order.payinfo.sign;


//		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		signParams.add(new BasicNameValuePair("appid", req.appId));
//		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//		signParams.add(new BasicNameValuePair("package", req.packageValue));
//		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//		req.sign = genAppSign(signParams);
//
//		sb.append("sign\n"+req.sign+"\n\n");
//		System.out.println("order"+signParams.toString());
//		Log.e("orion", signParams.toString());
		
		sendPayReq();

	}
	
	
//	private String genAppSign(List<NameValuePair> params) {
//		StringBuilder sb = new StringBuilder();
//
//		for (int i = 0; i < params.size(); i++) {
//			sb.append(params.get(i).getName());
//			sb.append('=');
//			sb.append(params.get(i).getValue());
//			sb.append('&');
//		}
//		sb.append("key=");
//		sb.append(API_KEY);
//
//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
//		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//		Log.e("orion",appSign);
//		return appSign;
//	}
	
	/**
	 * 支付
	 */
	private void sendPayReq() {
		api.registerApp(APP_ID);
		api.sendReq(req);
		finish();
	}
	
//	/**
//	 * 时间戳
//	 * @return
//	 */
//	private long genTimeStamp() {
//		return System.currentTimeMillis() / 1000;
//	}
	
	
}
