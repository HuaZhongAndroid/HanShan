package com.bm.tzj.kc;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.UtilDialog;
import com.bm.entity.AliOrder;
import com.bm.entity.HotGoods;
import com.bm.entity.Order;
import com.bm.entity.WeixinOrder;
import com.bm.entity.Youhuiquan;
import com.bm.pay.alipay.AlipayUtil;
import com.bm.pay.weixin.PayActivity;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.PwdSetAc;
import com.bm.util.Util;
import com.google.gson.Gson;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 支付方式
 * 
 * @author shiyt
 * 
 */
public class PayInfoAc extends BaseActivity implements OnClickListener {
	private Context mContext;
	private RoundImageViewFive img_pic;
	private TextView tv_next, tv_course_name, tv_course_money, tv_balance,tv_time,tv_address,tv_mycourse_data,tv_youhuijin,tv_youhuiquanname,tv_shifukuan;
	private LinearLayout ll_alipay, ll_wexin, ll_unio, ll_balance, ll_paytype;
	private ImageView img_balance,img_unio,img_alipay,img_wexin;
	private ImageView[] tab_tvs = new ImageView[4]; // 第部三个按钮切换颜色
	HotGoods hotGoods;//课程信息
	Order orders;//订单信息
	private String payPwd="",strPageTag="";//支付密码
//	Order order; //微信支付用的
	public static PayInfoAc intance;
	
	private AliOrder aliOrder;
	private WeixinOrder wxOrder;
	
	private Youhuiquan quan;
	
	private double shifukuan, yue; //实付款，和 账户余额
	
	/**
	 * 支付方式 1 支付宝 2 微信 3 网银 4 账户余额
	 */
	int payType=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_payinfo);
		mContext = this;
		setTitleName("支付");
		intance = this;
		quan = null;
		init();
		defYouhui();
	}
	
	/**
	 * 自动选择一个优惠券
	 */
	private void defYouhui()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		map.put("goodsType", hotGoods.goodsType);
		map.put("goodsPrice", hotGoods.goodsPrice);
		AsyncHttpHelp.httpGet(mContext,BaseApi.API_pickCoupon, map, new ServiceCallback<CommonResult<Youhuiquan>>(){
			@Override
			public void done(int what, CommonResult<Youhuiquan> obj) {
				quan = obj.data;
				tv_youhuijin.setText("-￥"+quan.money);
				tv_youhuijin.setTextColor(0xffa59945);
				shifukuan = Double.parseDouble(orders.payMoney)-Double.parseDouble(quan.money);
				tv_shifukuan.setText("￥"+shifukuan);
			}
			@Override
			public void error(String msg) {
				tv_youhuijin.setText("无可用优惠券");
				tv_youhuijin.setTextColor(0xffA4A4A4);
			}} );
	}

	public void init() {
		hotGoods = (HotGoods) getIntent().getSerializableExtra("hotGoods");
		orders = (Order) getIntent().getSerializableExtra("order");
		strPageTag = getIntent().getStringExtra("pageTag");
		tv_time = findTextViewById(R.id.tv_time);
		tv_mycourse_data = findTextViewById(R.id.tv_mycourse_data);
		tv_address = findTextViewById(R.id.tv_address);
		tv_youhuijin = findTextViewById(R.id.tv_youhuijin);
		tv_youhuiquanname = findTextViewById(R.id.tv_youhuiquanname);
		tv_shifukuan = findTextViewById(R.id.tv_shifukuan);
		tv_shifukuan.setText("￥"+orders.payMoney);
		img_pic = (RoundImageViewFive) findViewById(R.id.img_pic);
		tv_next = findTextViewById(R.id.tv_next);
		tv_course_name = findTextViewById(R.id.tv_course_name);
		tv_course_money = findTextViewById(R.id.tv_course_money);
		tv_balance = findTextViewById(R.id.tv_balance);

		ll_alipay = findLinearLayoutById(R.id.ll_alipay);
		ll_wexin = findLinearLayoutById(R.id.ll_wexin);
		ll_unio = findLinearLayoutById(R.id.ll_unio);
		ll_balance = findLinearLayoutById(R.id.ll_balance);
		ll_paytype = findLinearLayoutById(R.id.ll_paytype);
		
		img_balance=findImageViewById(R.id.img_balance);
		img_unio=findImageViewById(R.id.img_unio);
		img_alipay=findImageViewById(R.id.img_alipay);
		img_wexin=findImageViewById(R.id.img_wexin);
		
		tab_tvs[0]=img_alipay;
		tab_tvs[1]=img_wexin;
		tab_tvs[2]=img_unio;
		tab_tvs[3]=img_balance;
		img_alipay.setSelected(true);
		
		ll_balance.setOnClickListener(this);
		ll_wexin.setOnClickListener(this);
		ll_alipay.setOnClickListener(this);
		ll_unio.setOnClickListener(this);
		tv_next.setOnClickListener(this);
		
		setDate();
		
		//0元商品的处理
		if(hotGoods.goodsPrice == null || Double.valueOf(hotGoods.goodsPrice) <= 0)
		{
			ll_paytype.setVisibility(View.GONE);
			tv_next.setText("免费报名");
			payType = 4;
		}
		
		if("1".equals(hotGoods.goodsType))
		{
			this.findViewById(R.id.tv_tip).setVisibility(View.GONE);
		}
		else
		{
			ll_alipay.setVisibility(View.GONE);
			ll_wexin.setVisibility(View.GONE);
			switchTvsTo(3);
			payType = 4;
			
			this.findTextViewById(R.id.tv_yuename).setText("本金金额");
			yue = getNullData(App.getInstance().getUser().rechargeBalance) =="" ?0:Double.parseDouble(App.getInstance().getUser().rechargeBalance);
			tv_balance.setText("余额  ￥"+yue);//余额
		}
	}
	
	public void setDate(){
		ImageLoader.getInstance().displayImage(hotGoods.titleMultiUrl, img_pic,App.getInstance().getListViewDisplayImageOptions());
		tv_course_name.setText(getNullData(hotGoods.goodsName));//课程名称
		tv_time.setText(Util.toString(getNullData(hotGoods.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		tv_mycourse_data.setText(Util.toString(getNullData(hotGoods.endTime),"yyyy-MM-dd HH:mm:ss","-HH:mm"));
		tv_address.setText(getNullData(hotGoods.address));
		tv_course_money.setText(getNullData(hotGoods.goodsPrice)==""?"￥0":"￥"+hotGoods.goodsPrice);
		
		if(null == App.getInstance().getUser()){
			return;
		}
		yue = getNullData(App.getInstance().getUser().accountMoney) =="" ?0:Double.parseDouble(App.getInstance().getUser().accountMoney);
		tv_balance.setText("余额  ￥"+yue);//余额
		
	}

	private void switchTvsTo(int pos) {
		for (int i = 0; i < tab_tvs.length; i++) {
			tab_tvs[i].setSelected(pos == i);
		}
	}
	
	/**
	 * 下一步
	 */
	public void getNext(){
		if(payType == 4){
			if (null == App.getInstance().getUser()) {
				dialogToast("用户信息获取失败");
				return;
			}
			
			float payMoney = Float.parseFloat(getNullDataFor0(orders.payMoney));//支付金额
			
			if(payMoney <= 0) //0元支付 不需要密码
			{
				payPwd = "000000";
				getPay();
				return;
			}
			
			if (yue < shifukuan) {
				dialogToast("您的账号余额不足，请选择其他支付方式");
				return;
			}
			
			if(TextUtils.isEmpty(App.getInstance().getUser().paymentPassword) || "0".equals(App.getInstance().getUser().paymentPassword)){
//				dialogToast("余额支付密码未设置，请到设置界面先设置支付密码");
				UtilDialog.dialogTwoBtnContentTtile(this, "余额支付密码未设置，请先设置支付密码", "取消","前往","提示",new Handler(){
					public void handleMessage(android.os.Message msg) {
						switch(msg.what){
						case 1:
							Intent intent = new Intent(PayInfoAc.this,PwdSetAc.class);
							startActivity(intent);
							break;
						}
					}
				},1);
				return;
			}
			
			
			
			UtilDialog.dialogPay(mContext, handler);
		}else{
//			if(payType==2){
//				wxPayinfo();
//			}else{
				getSubmitPay();//提交支付
//			}
		}
	}
	
	/**
	 * 余额支付
	 */
	public void getPay(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goodsId", hotGoods.goodsId);//课程ID
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//用户ID
		}
//		map.put("orderId",orders.orderId);//订单ID
		map.put("orderNumber",orders.orderNumber);//订单ID
		map.put("paymentType",payType+"");//支付方式
		map.put("paymentPassword", payPwd);//支付密码
		if(quan != null)
			map.put("couponid", quan.pkid); //优惠券id
		else
			map.put("couponid", "-1"); //优惠券id
		map.put("finalPrice", tv_shifukuan.getText().subSequence(1, tv_shifukuan.getText().length()).toString()); //实际支付
		
		if(null != App.getInstance().getChild())
			map.put("babyId", App.getInstance().getChild().babyId);
		
		showProgressDialog();
		UserManager.getInstance().getTzjorderPaymentaccount(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				finish();
				if(null!=getIntent().getStringExtra("pageTag")&&"CourseDetailAc".equals(getIntent().getStringExtra("pageTag"))){//课程详情
//					CourseDetailAc.intances.finish();
					MainAc.intance.isDefult =0;
					 App.toast("支付成功!您可以前往我的-我的课程查看已购买的课程!");
				}else{
					App.toast("支付成功!");
				}
				
				
				
//				getSubmitPay();//提交支付
				
			}
		});
	}
//	//微信信息
//	private void wxPayinfo() {
//		showProgressDialog();
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("orderId", orders.orderId);//
//		map.put("userId", App.getInstance().getUser().userid);//
//		UserManager.getInstance().wxPayinfo(mContext, map,new ServiceCallback<CommonResult<Order>>() {
//
//					@Override
//					public void error(String msg) {
//						hideProgressDialog();
//						dialogToast(msg);
//
//					}
//
//					@Override
//					public void done(int what, CommonResult<Order> obj) {
//						hideProgressDialog();
//						if (null != obj.data) {
//							order = obj.data;
//							getSubmitPay();
//						}
//					}
//				});
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_alipay:  //支付宝
			switchTvsTo(0);
			payType = 1;
//			alipay();
			break;
		case R.id.ll_wexin: //微信
			switchTvsTo(1);
			payType = 2;
			break;
		case R.id.ll_unio:  //银联
			switchTvsTo(2);
			payType = 3;
			break;
		case R.id.ll_balance:  //余额
			switchTvsTo(3);
			payType = 4;
			break;
		case R.id.tv_next:
			getNext();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 支付
	 */
	private void getSubmitPay() {
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("orderNumber", orders.orderNumber);//金额 测试用
		map.put("userId", App.getInstance().getUser().userid);//
		map.put("payType", payType+"");//1  支付宝  2 微信  3银联 
		if(quan != null)
			map.put("couponid", quan.pkid); //优惠券id
		else
			map.put("couponid", "-1"); //优惠券id
		map.put("finalPrice", tv_shifukuan.getText().toString().replaceAll("￥", "")); //实际支付
		if (payType == 1) {
			UserManager.getInstance().payAli(this, map, new ServiceCallback<CommonResult<AliOrder>>() {
							@Override
							public void error(String msg) {
								hideProgressDialog();
								dialogToast(msg);
							}
							@Override
							public void done(int what, CommonResult<AliOrder> obj) {
								hideProgressDialog();
								if(null!=obj.data){
									aliOrder=obj.data;
									AlipayUtil.pay(aliOrder.payinfo, PayInfoAc.this,strPageTag);
								}
							}
						});
			
		} else if (payType == 2) {
			UserManager.getInstance().payWx(this, map, new ServiceCallback<CommonResult<WeixinOrder>>() {
				@Override
				public void error(String msg) {
					hideProgressDialog();
					dialogToast(msg);
				}
				@Override
				public void done(int what, CommonResult<WeixinOrder> obj) {
					hideProgressDialog();
					if(null!=obj.data){
						wxOrder=obj.data;
						weichatPay(wxOrder);
					}
				}
			});
		} else if (payType == 3) {
			dialogToast("银联支付");
		} 
	}
	
//	private void alipay(){
//		Order order = new Order();
//		order.orderId = orders.orderId;
//		order.goodsName = hotGoods.goodsName;
//		order.payMoney =orders.payMoney;
//		AlipayUtil.pay(order, this,BaseApi.API_URL_PRE+"tzjorder/alipayNotify.do?",strPageTag);
//	}
	
//	/**
//	 * 微信支付
//	 */
//	private void weichatPay(Order info,String orderSn) {
//		// 通过WXAPIFactory工厂，获取IWXAPI的实例
//		String json = new Gson().toJson(info);
//		System.out.println("json:"+json);
//		Intent intent = new Intent(this,PayActivity.class);
//		intent.putExtra("info", info);
//		intent.putExtra("pageType", strPageTag);
//		intent.putExtra("orderSn", order.sign);
//		startActivity(intent);
//		
//		//PayActivity.pay(this, json);
//	}
	/**
	 * 微信支付
	 */
	private void weichatPay(WeixinOrder order) {
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		String json = new Gson().toJson(order);
		System.out.println("json:"+json);
		Intent intent = new Intent(this,PayActivity.class);
		intent.putExtra("info", json);
		intent.putExtra("pageType", strPageTag);
		startActivity(intent);
		//PayActivity.pay(this, json);
	}
	
	Handler handler=new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				payPwd = ((EditText)msg.obj).getText().toString().trim();
				if(TextUtils.isEmpty(payPwd)){
					dialogToast("密码不能为空");
					return;
				}
				getPay();
				
				
				break;

			default:
				break;
			}
		}
	};
	
	
}
