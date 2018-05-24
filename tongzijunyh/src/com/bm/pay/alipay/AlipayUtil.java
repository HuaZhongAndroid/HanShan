package com.bm.pay.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.bm.app.App;
import com.bm.entity.Order;
import com.bm.tzj.activity.AbsCoursePayBaseAc;
import com.bm.tzj.activity.LeyuanStoreAc;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.PayInfoAc;
import com.bm.tzj.kc.PayInfoAc2;
import com.bm.tzj.mine.RechargeAc;
import com.bm.tzj.mine.RechargeAc2;

public class AlipayUtil {
	static String strPageType="";

	private static Activity context;
	
	/**
	 * 支付宝支付
	 * 
	 */
	public static void pay(final String payinfo,final Activity c,String pageType)
	{
		context = c;
		strPageType = pageType;
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payinfo);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	
	/**
	 * 支付宝支付
	 * 
	 */
	public static void pay(Order order,final Activity c,String url,String pageType) {
		context = c;
		strPageType = pageType;
		// 订单
		String orderInfo = getOrderInfo(order, "该商品的详细描述",url);
//		System.out.println("江仕华"+orderInfo);
		// 对订单做RSA 签名
		String sign =sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					
					  if(strPageType.equals("CourseDetailAc")){//课程详情
						    App.toast("支付成功!您可以前往个人中心-我的课程查看已购买的课程!");
							if(null != PayInfoAc.intance ){
								PayInfoAc.intance.finish();
							}
//							if(null != CourseDetailAc.intances){
//								CourseDetailAc.intances.finish();
//							}
					  }else if(strPageType.equals("AbsCoursePayBaseAc")) {//课程详情
//						App.toast("支付成功!您可以前往个人中心-我的课程查看已购买的课程!");
						  new AlertDialog.Builder(context).setTitle("支付成功").setMessage("您可以前往我的-我的课程查看已购买的课程!")
								  .setNegativeButton("确定", new DialogInterface.OnClickListener() {
									  @Override
									  public void onClick(DialogInterface dialog, int which) {
										  if (null != PayInfoAc2.intance) {
											  PayInfoAc2.intance.finish();
										  }
										  if (null != AbsCoursePayBaseAc.intances) {
											  AbsCoursePayBaseAc.intances.finish();
										  }
									  }
								  }).setCancelable(false)
								  .show();
					  }else if("CoursebaoAc".equals(strPageType)){//课程包购买
						  MainAc.intance.isDefult =0;
						  if (null != PayInfoAc2.intance) {
							  PayInfoAc2.intance.finish();
						  }
						  App.toast("支付成功!");
					  }
					  else if (strPageType.equals("MyCourse")){//我的课程
		                	App.toast("支付成功!");
		                	PayInfoAc.intance.finish();
					  }else if(strPageType.equals("RechargeAc")){//充值
		                	App.toast("充值成功!");
		                	if(null != RechargeAc2.intance){
		                		RechargeAc2.intance.finish();
		                	}
					  }
					
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						App.toast("支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					      App.toast("支付失败");

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private static String getOrderInfo(Order order, String body,String url) {//BaseApi.API_HOST+"/tongzijun/api/tzjorder/alipayNotify.do?"
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + order.orderId + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + order.goodsName + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + order.payMoney+ "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" +url+ "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		orderInfo += "&it_b_pay=\"30m\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		
		return orderInfo;
	}
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private static String sign(String content) {
		return SignUtils.sign(content, Keys.PRIVATE);
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
}
