package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.OpenLocDialog;
import com.bm.dialog.OpenNotifyDialog;
import com.bm.entity.AliOrder;
import com.bm.entity.RechargeConfig;
import com.bm.entity.WeixinOrder;
import com.bm.pay.alipay.AlipayUtil;
import com.bm.pay.weixin.PayActivity;
import com.bm.util.BDLocationHelper;
import com.bm.util.BDLocationHelper.LocationInfo;
import com.bm.util.BDLocationHelper.MyLocationListener;
import com.google.gson.Gson;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

/**
 * 充值
 * 
 * @author wanghy
 * 
 */
public class RechargeAc extends BaseActivity implements OnClickListener {
	private Context context;
	private TextView tv_nexts;
	private TextView tv_czMoney, tv_zsMoney, tv_money, tv_xieyi, tv_paymoney,tv_overmoney;
	private EditText et_rechargeMoney;
	private LinearLayout ll_alipays, ll_wexins, ll_unios;
	private ImageView img_unios, img_alipays, img_wexins;
	private ImageView[] tab_tvs = new ImageView[3]; // 第部三个按钮切换颜色
	public static RechargeAc intance;
//	User info;
//	Order order;
	
	private AliOrder aliOrder;
	private WeixinOrder wxOrder;
	
	private List<RechargeConfig> cfgList;
	private List<View> cfgViews;
	private int indexS; //当前选择第几个充值额度
	
	
	/**
	 * 支付方式 1 支付宝 2 微信 3 网银 
	 */
	int payType=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_recharge_new);
		context = this;
		setTitleName("充值中心");
		super.setRightName("账单明细");
		intance = this;
		initView();
		loadConfig();
	}
	
	/**
	 * 
	 * 右边按钮点击事件
	 */
	public void clickRight() {
		Intent intent = new Intent(context, AccountBalanceAc.class);
		startActivity(intent);
	}
	
	/**
	 * 加载充值额度列表
	 */
	private void loadConfig()
	{
		showProgressDialog();
		BDLocationHelper.locate(context, new MyLocationListener() {
			@Override
			public void success(BDLocation location) {
				LocationInfo info = BDLocationHelper.getCacheLocation();
				HashMap<String, String> map =new HashMap<String, String>();
				map.put("userId", App.getInstance().getUser().userid);//
				if (info != null) {
					map.put("longitude", info.lng + "");// 坐标
					map.put("latitude", info.lat + "");// 坐标
				} else {
					map.put("longitude", "-1");// 坐标
					map.put("latitude", "-1");// 坐标
				}

				if(info == null || info.lat < 1 || info.lng < 1)
				{
					OpenLocDialog dialog = new OpenLocDialog((Activity)context);
					dialog.show();
				}

 				UserManager.getInstance().rechargeConfigList(context, map, new ServiceCallback<CommonListResult<RechargeConfig>>() {
						@Override
						public void error(String msg) {
							hideProgressDialog();
							dialogToast(msg);
						}
						@Override
						public void done(int what, CommonListResult<RechargeConfig> obj) {
							hideProgressDialog();
							if(null!=obj.data){
								cfgList = obj.data;
								initConfigView();
							}
						}
					});
			}
		});
	}
	
	/**
	 * 构建额度列表视图
	 */
	private void initConfigView()
	{
		indexS = -1;
		LinearLayout ll1 = (LinearLayout)this.findViewById(R.id.ll_l1);
		LinearLayout ll2 = (LinearLayout)this.findViewById(R.id.ll_l2);
		LinearLayout ll3 = (LinearLayout)this.findViewById(R.id.ll_l3);
		ll3.removeView(et_rechargeMoney);
		LinearLayout[] lls = new LinearLayout[]{ll1,ll2,ll3};
		cfgViews = new ArrayList<View>();
		int i=0;
		if(cfgList != null)
		{
			for(i=0; i<cfgList.size(); i++)
			{
				View v = LayoutInflater.from(this).inflate(R.layout.recharge_config, ll1, false);
				v.setTag(i);
				if(i == 0)
				{
					v.setBackgroundResource(R.drawable.yjjuxing_x);
					indexS = 0;
					tv_paymoney.setText(cfgList.get(i).rechageMoney);
					double oo = Double.parseDouble(cfgList.get(i).rechageMoney)+Double.parseDouble(cfgList.get(i).giveMoney);
					tv_overmoney.setText(oo+"");
				}
				TextView tvMoney = (TextView)v.findViewById(R.id.tv_money);
				TextView tvGive = (TextView)v.findViewById(R.id.tv_give_money);
				tvMoney.setText(cfgList.get(i).rechageMoney + "元");
				tvGive.setText("赠送"+cfgList.get(i).giveMoney + "元");
				if(Double.valueOf(cfgList.get(i).giveMoney) > 0)
				{
					tvGive.setVisibility(View.VISIBLE);
				}
				else
				{
					tvGive.setVisibility(View.GONE);
				}
				lls[i%lls.length].addView(v);
				cfgViews.add(v);
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for(View a:cfgViews)
							a.setBackgroundResource(R.drawable.yjjuxing);
						et_rechargeMoney.setBackgroundResource(R.drawable.yjjuxing);
						v.setBackgroundResource(R.drawable.yjjuxing_x);
						indexS = (Integer)v.getTag();
						et_rechargeMoney.clearFocus();
						tv_money.requestFocus();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
						imm.hideSoftInputFromWindow(et_rechargeMoney.getWindowToken(), 0); //强制隐藏键盘  
						tv_paymoney.setText(cfgList.get((Integer)v.getTag()).rechageMoney);
						double oo = Double.parseDouble(cfgList.get((Integer)v.getTag()).rechageMoney)+Double.parseDouble(cfgList.get((Integer)v.getTag()).giveMoney);
						tv_overmoney.setText(oo+"");
					}
				});
			}
		}
		
		lls[i%lls.length].addView(et_rechargeMoney);
		et_rechargeMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
				{
					for(View a:cfgViews)
						a.setBackgroundResource(R.drawable.yjjuxing);
					et_rechargeMoney.setBackgroundResource(R.drawable.yjjuxing_x);
					indexS = -1;
					tv_paymoney.setText(et_rechargeMoney.getText());
					tv_overmoney.setText(et_rechargeMoney.getText());
				}
			}
		});
		et_rechargeMoney.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				int dian = s.toString().indexOf('.');
				if(dian != -1 && s.length()-dian >3)
				{
					s.delete(dian+3, s.length());
				}
				
				tv_paymoney.setText(s);
				tv_overmoney.setText(s);
			}});
	}

	private void initView() {
		
		tv_czMoney = findTextViewById(R.id.tv_czMoney);
		tv_zsMoney = findTextViewById(R.id.tv_zsMoney);
		tv_money = findTextViewById(R.id.tv_money);
		tv_paymoney = findTextViewById(R.id.tv_paymoney);
		tv_overmoney = findTextViewById(R.id.tv_overmoney);
		if(null != App.getInstance().getUser()){
			tv_money.setText(App.getInstance().getUser().accountMoney == null?"0.00":App.getInstance().getUser().accountMoney);//余额
			tv_czMoney.setText(App.getInstance().getUser().rechargeBalance == null?"0.00":App.getInstance().getUser().rechargeBalance);//余额
			tv_zsMoney.setText(App.getInstance().getUser().giveBalance == null?"0.00":App.getInstance().getUser().giveBalance);//余额
		}
		
		tv_nexts = findTextViewById(R.id.tv_nexts);
		et_rechargeMoney = findEditTextById(R.id.et_rechargeMoney);

		ll_alipays = findLinearLayoutById(R.id.ll_alipays);
		ll_wexins = findLinearLayoutById(R.id.ll_wexins);
		ll_unios = findLinearLayoutById(R.id.ll_unios);

		img_wexins = findImageViewById(R.id.img_wexins);
		img_unios = findImageViewById(R.id.img_unios);
		img_alipays = findImageViewById(R.id.img_alipays);

		tab_tvs[0] = img_alipays;
		tab_tvs[1] = img_wexins;
		tab_tvs[2] = img_unios;
		img_alipays.setSelected(true);

		ll_wexins.setOnClickListener(this);
		ll_alipays.setOnClickListener(this);
		ll_unios.setOnClickListener(this);
		tv_nexts.setOnClickListener(this);

		tv_xieyi = findTextViewById(R.id.tv_xieyi);
		tv_xieyi.setText(Html.fromHtml("点击立即充值，即表示您同意<u>《充值协议》</u>"));
		tv_xieyi.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_nexts:
			payrecharge();
			
			break;
		case R.id.ll_alipays: // 支付宝
			switchTvsTo(0);
			payType = 1;
			break;
		case R.id.ll_wexins: // 微信
			switchTvsTo(1);
			payType = 2;
			break;
		case R.id.ll_unios: // 银联
			switchTvsTo(2);
			payType = 3;
			break;
		case R.id.tv_xieyi:
			this.startActivity(new Intent(this, RechargeXieyiAc.class));
			break;

		default:
			break;
		}
	}
	
	private void payrecharge(){
		
//		if(TextUtils.isEmpty(App.getInstance().getUser().paymentPassword)){
//			String  paymentPassword =getNullData(App.getInstance().getUser().paymentPassword)==""?"0":App.getInstance().getUser().paymentPassword ;
//			if(paymentPassword.equals("0")){//0 没有设置支付密码  1 设置支付密码
//				dialogToast("余额支付密码未设置，请到设置界面先设置支付密码");
//				return;
//			}
//		}
		
		HashMap<String, String> map =new HashMap<String, String>();
		if(indexS == -1)
		{
			String strMoney = et_rechargeMoney.getText().toString().trim();
			if(strMoney.length() == 0){
				dialogToast("充值金额不能为空");
				return;
			}
			if(Float.valueOf(strMoney)<=0){
				dialogToast("充值金额不能为0");
				return;
			}
			map.put("recMoney", strMoney);//金额
			map.put("giveMoney", "-1");//赠送金额
			map.put("storeid", cfgList.get(0).storeid);//门店
		}
		else
		{
			map.put("recMoney", cfgList.get(indexS).rechageMoney);//金额 
			map.put("giveMoney", cfgList.get(indexS).giveMoney);//赠送金额
			map.put("storeid", cfgList.get(indexS).storeid);//门店id
		}
		
		map.put("userId", App.getInstance().getUser().userid);//
		map.put("payType", payType+"");//1  支付宝  2 微信  3银联 
		showProgressDialog();
		if(payType == 1)
		{
			UserManager.getInstance().payrechargeAli(context, map, new ServiceCallback<CommonResult<AliOrder>>() {
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
						getSubmit();
	//					if(payType == 2){//微信
	//						wxPayinfo(info);
	//					}else{
	//						getSubmit();
	//					}
					}
				}
			});
		}
		else if(payType == 2)
		{
				UserManager.getInstance().payrechargeWx(context, map, new ServiceCallback<CommonResult<WeixinOrder>>() {
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
						getSubmit();
					}
				}
			});
		}
	}
	
//	//微信信息
//	private void wxPayinfo(User info){
//		showProgressDialog();
//		HashMap<String, String> map =new HashMap<String, String>();
//		map.put("orderId", info.recNumber);//
//		map.put("userId", App.getInstance().getUser().userid);//
//		UserManager.getInstance().wxRechareInfo(context, map, new ServiceCallback<CommonResult<Order>>() {
//			
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//				dialogToast(msg);
//			}
//			
//			@Override
//			public void done(int what, CommonResult<Order> obj) {
//				hideProgressDialog();
//				if(null!=obj.data){
//					order=obj.data;
//					getSubmit();
//				}
//			}
//		});
//	}
	
	private void getSubmit(){
		if(payType == 1){
			AlipayUtil.pay(aliOrder.payinfo, this,"RechargeAc");
		}else if (payType == 2){
			weichatPay(wxOrder);
		}else if (payType == 3){
		}else if (payType == 4){
		}
	}

	private void switchTvsTo(int pos) {
		for (int i = 0; i < tab_tvs.length; i++) {
			tab_tvs[i].setSelected(pos == i);
		}
	}

//	private void alipay() {
//		Order order = new Order();
//		order.orderId = info.recNumber+"";
//		order.goodsName = "充值";
//		order.payMoney = et_rechargeMoney.getText().toString().trim()+"";
//		AlipayUtil.pay(order, this,BaseApi.API_URL_PRE+"tzjaccount/alipayrecharge.do?","RechargeAc");
////		finish();
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
		intent.putExtra("pageType", "RechargeAc");
		startActivity(intent);
		//PayActivity.pay(this, json);
	}
	
}
