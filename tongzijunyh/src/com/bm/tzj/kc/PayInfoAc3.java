package com.bm.tzj.kc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.JiaotongfeiPop;
import com.bm.dialog.UtilDialog;
import com.bm.entity.AliOrder;
import com.bm.entity.Child;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.bm.entity.Jiaotongfei;
import com.bm.entity.Order;
import com.bm.entity.Storelist;
import com.bm.entity.WeixinOrder;
import com.bm.entity.Youhuiquan;
import com.bm.pay.alipay.AlipayUtil;
import com.bm.pay.weixin.PayActivity;
import com.bm.tzj.activity.AbsCoursePayBaseAc;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.PwdSetAc;
import com.bm.tzj.mine.RechargeAc2;
import com.google.gson.Gson;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

import java.util.HashMap;

/**
 * 支付方式
 */
public class PayInfoAc3 extends BaseActivity implements OnClickListener {
    private Context mContext;
    private TextView tv_next, tv_course_name, tv_balance, tv_time, tv_address, tv_babyname, tv_course_count,
            tv_youhuijin, tv_youhuiquanname, tv_shifukuan;
    private LinearLayout ll_alipay, ll_wexin, ll_unio, ll_balance, ll_paytype;
    private ImageView img_balance, img_unio, img_alipay, img_wexin;

    private ImageView[] tab_tvs = new ImageView[4]; // 支付方式radio按钮

    private ImageView[] tab_tvs_b = new ImageView[2]; // 底部弹窗的支付方式radio按钮

    //private Course course;//课程信息
    private Order orders;//订单信息
    //private Child child; //孩子信息
    private Storelist storelist; //门店信息
    private String  strPageTag = "";//支付密码
    //	Order order; //微信支付用的
    public static PayInfoAc3 intance;

    private AliOrder aliOrder;
    private WeixinOrder wxOrder;

    private Youhuiquan quan;

    private double shifukuan, yue, d_jiaotongfei; //实付款，和 账户余额

    /**
     * 支付方式 1 支付宝 2 微信 3 网银 4 账户余额
     */
    int payType = 1;
    int payType_b = 1; //底部弹窗用的


    private JiaotongfeiPop jiaotongfeiPop;
    private Jiaotongfei jiaotongfei;
    private TextView tv_jiaotongfei;
    private View v_jiaotongfei;

    private View lay_store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_payinfo3);
        mContext = this;
        setTitleName("支付订单");
        intance = this;
        quan = null;
        jiaotongfei = null;
        d_jiaotongfei = 0;
        init();
        defYouhui();
    }

    @Override
    protected void onStart() {
        super.onStart();
        yue = getNullData(App.getInstance().getUser().accountMoney) == "" ? 0 : Double.parseDouble(App.getInstance().getUser().accountMoney);
        tv_balance.setText("￥" + yue);//余额
        if (ll_balance!=null)
        if (yue>shifukuan){
            //如果余额大于当前课程支付金额 则把余额放第一位
            LinearLayout parentViwe = (LinearLayout) ll_balance.getParent();
            parentViwe.removeView(ll_balance);
            parentViwe.addView(ll_balance,0);
            switchTvsTo(3);
            //img_balance.setImageResource(R.drawable.btn_pay_selector);
        }
    }

    /**
     * 自动选择一个优惠券
     */
    private void defYouhui() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", App.getInstance().getUser().userid);
        map.put("goodsType", orders.goodsType);
        map.put("goodsPrice", orders.goodsPrice);
        AsyncHttpHelp.httpGet(mContext, BaseApi.API_pickCoupon, map, new ServiceCallback<CommonResult<Youhuiquan>>() {
            @Override
            public void done(int what, CommonResult<Youhuiquan> obj) {
                quan = obj.data;
                tv_youhuijin.setText("-￥" + quan.money);
                tv_youhuijin.setTextColor(0xffa59945);
                shifukuan = Double.parseDouble(orders.goodsMoney) - Double.parseDouble(quan.money);
                tv_shifukuan.setText("￥" + shifukuan);
            }

            @Override
            public void error(String msg) {
                tv_youhuijin.setText("无可用优惠券");
                tv_youhuijin.setTextColor(0xffA4A4A4);
            }
        });
    }

    public void init() {
        orders = (Order) getIntent().getSerializableExtra("order");
        storelist = (Storelist) getIntent().getSerializableExtra("storelist");
        strPageTag = getIntent().getStringExtra("pageTag");

        tv_babyname = findTextViewById(R.id.tv_babyname);
        tv_address = findTextViewById(R.id.tv_address);
        tv_time = findTextViewById(R.id.tv_time);
        tv_course_name = findTextViewById(R.id.tv_course_name);
        tv_youhuiquanname = findTextViewById(R.id.tv_youhuiquanname);
        tv_youhuijin = findTextViewById(R.id.tv_youhuijin);
        tv_shifukuan = findTextViewById(R.id.tv_shifukuan);
        tv_next = findTextViewById(R.id.tv_next);
        tv_balance = findTextViewById(R.id.tv_balance);

        tv_jiaotongfei = findTextViewById(R.id.tv_jiaotongfei);
        v_jiaotongfei = findViewById(R.id.v_jiaotongfei);
        lay_store = findViewById(R.id.lay_store);

        if (orders != null) {
            tv_babyname.setText(orders.realName);
            tv_address.setText(orders.storeName);
            if (orders.storeId.equals("-1")){
                lay_store.setVisibility(View.GONE);
            }
            tv_time.setText(orders.goodsTime);
            tv_course_name.setText(getNullData(orders.goodsName));//课程名称
        }

        tv_shifukuan.setText("￥" + orders.goodsMoney);
        shifukuan = Double.valueOf(orders.goodsMoney);

        ll_alipay = findLinearLayoutById(R.id.ll_alipay);
        ll_wexin = findLinearLayoutById(R.id.ll_wexin);
        ll_unio = findLinearLayoutById(R.id.ll_unio);
        ll_balance = findLinearLayoutById(R.id.ll_balance);
        ll_paytype = findLinearLayoutById(R.id.ll_paytype);

        img_balance = findImageViewById(R.id.img_balance);
        img_unio = findImageViewById(R.id.img_unio);
        img_alipay = findImageViewById(R.id.img_alipay);
        img_wexin = findImageViewById(R.id.img_wexin);

        tab_tvs[0] = img_alipay;
        tab_tvs[1] = img_wexin;
        tab_tvs[2] = img_unio;
        tab_tvs[3] = img_balance;
        tab_tvs[1].setSelected(true);
        payType = 2;

        ll_balance.setOnClickListener(this);
        ll_wexin.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_unio.setOnClickListener(this);
        tv_next.setOnClickListener(this);


        //0元商品的处理
        if (orders.goodsMoney == null || Double.valueOf(orders.goodsMoney) <= 0) {
            ll_paytype.setVisibility(View.GONE);
            tv_next.setText("免费报名");
            payType = 4;
        }


        if ("1".equals(orders.isTraffic)) {
            v_jiaotongfei.setOnClickListener(this);
            v_jiaotongfei.setVisibility(View.VISIBLE);
            jiaotongfeiPop = new JiaotongfeiPop(context);
            jiaotongfeiPop.setSListener(new JiaotongfeiPop.OnClickListener() {
                @Override
                public void onclick(Jiaotongfei j) {
                    jiaotongfei = j;
                    if (jiaotongfei == null) {
                        tv_jiaotongfei.setText("未选择");
                        tv_jiaotongfei.setTextColor(0xff999999);
                        tv_jiaotongfei.setTextSize(15);
                        d_jiaotongfei = 0;
                    } else {
                        tv_jiaotongfei.setText("￥" + j.price);
                        tv_jiaotongfei.setTextColor(0xffA59945);
                        tv_jiaotongfei.setTextSize(19);
                        d_jiaotongfei = Double.valueOf(j.price);
                    }
                    tv_shifukuan.setText("￥" + (shifukuan + d_jiaotongfei));
                }
            });
        } else {
            v_jiaotongfei.setVisibility(View.GONE);
        }

//        if("1".equals(hotGoods.goodsType))
//        {
//            this.findViewById(R.id.tv_tip).setVisibility(View.GONE);
//        }
//        else
//        {
//            ll_alipay.setVisibility(View.GONE);
//            ll_wexin.setVisibility(View.GONE);
//            switchTvsTo(3);
//            payType = 4;
//
//            this.findTextViewById(R.id.tv_yuename).setText("本金金额");
//            yue = getNullData(App.getInstance().getUser().rechargeBalance) =="" ?0:Double.parseDouble(App.getInstance().getUser().rechargeBalance);
//            tv_balance.setText("余额  ￥"+yue);//余额
//        }

    }


    private void switchTvsTo(int pos) {
        for (int i = 0; i < tab_tvs.length; i++) {
            tab_tvs[i].setSelected(pos == i);
        }
        payType = pos+1;
    }

    /**
     * 下一步
     */
    public void getNext() {
        if (payType == 4) {
            if (null == App.getInstance().getUser()) {
                dialogToast("用户信息获取失败");
                return;
            }

            if (shifukuan <= 0) //0元支付 不需要密码
            {
                getPay();
                return;
            }
            if (yue < shifukuan) {
//                dialogToast("您的账号余额不足，请选择其他支付方式");
                makeBtmPop();
                return;
            }
//            if (TextUtils.isEmpty(App.getInstance().getUser().paymentPassword) || "0".equals(App.getInstance().getUser().paymentPassword)) {
////				dialogToast("余额支付密码未设置，请到设置界面先设置支付密码");
//                UtilDialog.dialogTwoBtnContentTtile(this, "余额支付密码未设置，请先设置支付密码", "取消", "前往", "提示", new Handler() {
//                    public void handleMessage(Message msg) {
//                        switch (msg.what) {
//                            case 1:
//                                Intent intent = new Intent(PayInfoAc3.this, PwdSetAc.class);
//                                startActivity(intent);
//                                break;
//                        }
//                    }
//                }, 1);
//                return;
//            }
            String content = "您即将用充值余额支付%s元,\n确认支付吗？";
            content =  String.format(content,String.valueOf(shifukuan));
            UtilDialog.dialogPrompt(context,
                    content,
                    "取消",
                    "确认支付",
                    "余额支付",
                    handler,1);
            //UtilDialog.dialogPay(mContext, handler);
        } else {
//			if(payType==2){
//				wxPayinfo();
//			}else{
            getSubmitPay(payType);//提交支付
//			}
        }
    }



    /**
     * 余额支付
     */
    public void getPay() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsId", orders.goodsId);//课程包ID
        map.put("babyId", orders.babyId);
        map.put("userId", App.getInstance().getUser().userid);
		//map.put("orderId",orders.orderId);//订单ID
        map.put("orderNumber", orders.orderNumber);//订单ID
        map.put("paymentType", payType + "");//支付方式
       // map.put("paymentPassword", payPwd);//支付密码
        if (quan != null)
            map.put("couponid", quan.pkid); //优惠券id
        else
            map.put("couponid", "-1"); //优惠券id
        map.put("finalPrice", shifukuan + ""); //实际支付
//        map.put("trafficPrice",d_jiaotongfei+""); //交通费
        if (jiaotongfei == null)
            map.put("trafficId", "-1"); //交通费
        else
            map.put("trafficId", jiaotongfei.pkid); //交通费

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
                String content = "您可以前往我的-我的课程查看已购买的课程!";
                UtilDialog.dialogPrompt(context,
                        content,
                        "知道了",
                        "支付成功",
                        handler,404);

//
//                if ("CourseDetailAc".equals(getIntent().getStringExtra("pageTag"))) {//课程详情
////                    CourseDetailAc.intances.finish();
//                    MainAc.intance.isDefult = 0;
//                   // App.toast("支付成功!您可以前往我的-我的课程查看已购买的课程!");
//                    String content = "支付成功!您可以前往我的-我的课程查看已购买的课程!";
//                    UtilDialog.dialogPrompt(context,
//                            content,
//                            "确认支付",
//                            "支付成功",
//                            handler,404);
//                } else if ("AbsCoursePayBaseAc".equals(getIntent().getStringExtra("pageTag"))) {//课程详情
//                    if (AbsCoursePayBaseAc.intances != null)
//                        AbsCoursePayBaseAc.intances.finish();
//                    MainAc.intance.isDefult = 0;
//                   // App.toast("支付成功!您可以前往我的-我的课程查看已购买的课程!");
//                    String content = "支付成功!您可以前往我的-我的课程查看已购买的课程!";
//                    UtilDialog.dialogPrompt(context,
//                            content,
//                            "确认支付",
//                            "支付成功",
//                            handler,404);
//                } else if ("CoursebaoAc".equals(getIntent().getStringExtra("pageTag"))) {//课程包购买
//                    MainAc.intance.isDefult = 0;
//                    String content = "支付成功!您可以前往我的-我的课程查看已购买的课程!";
//                    UtilDialog.dialogPrompt(context,
//                            content,
//                            "确认支付",
//                            "支付成功",
//                            handler,404);
//                } else {
//                    String content = "支付成功!您可以前往我的-我的课程查看已购买的课程!";
//                    UtilDialog.dialogPrompt(context,
//                            content,
//                            "确认支付",
//                            "支付成功",
//                            handler,404);
//                }
            }
        });
    }

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
            case R.id.v_jiaotongfei:
                showJiaotongfeiPop(v);
                break;
            default:
                break;
        }
    }

    private void showJiaotongfeiPop(View v) {
//        jiaotongfeiPop.showAsDropDown(v);
        jiaotongfeiPop.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    /**
     * 支付
     */
    private void getSubmitPay(int payType) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", App.getInstance().getUser().userid);//
        map.put("orderNumber", orders.orderNumber);//金额 测试用
        map.put("babyId", orders.babyId);
        map.put("payType", payType + "");//1  支付宝  2 微信  3银联
        if (quan != null)
            map.put("couponid", quan.pkid); //优惠券id
        else
            map.put("couponid", "-1"); //优惠券id
        map.put("finalPrice", shifukuan + ""); //实际支付
//        map.put("trafficPrice",d_jiaotongfei+""); //交通费
        if (jiaotongfei == null)
            map.put("trafficId", "-1"); //交通费
        else
            map.put("trafficId", jiaotongfei.pkid); //交通费
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
                    if (null != obj.data) {
                        aliOrder = obj.data;
                        AlipayUtil.pay(aliOrder.payinfo, PayInfoAc3.this, strPageTag);
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
                    if (null != obj.data) {
                        wxOrder = obj.data;
                        weichatPay(wxOrder);
                    }
                }
            });
        } else if (payType == 3) {
            dialogToast("银联支付");
        }
    }

    /**
     * 微信支付
     */
    private void weichatPay(WeixinOrder order) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        String json = new Gson().toJson(order);
        System.out.println("json:" + json);
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("info", json);
        intent.putExtra("pageType", strPageTag);
        startActivity(intent);
        //PayActivity.pay(this, json);
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getPay();
                 break;
                 case 404:
                    finish();
                 break;

                default:
                    break;
            }
        }
    };


    //底部弹窗
    private void makeBtmPop() {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.SlideUpDialogAnimation;
        WindowManager.LayoutParams wl = dialog.getWindow().getAttributes();
        wl.gravity = Gravity.BOTTOM | Gravity.CENTER;
        dialog.getWindow().setAttributes(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.payinfo_tanchuang);
        //设置视图宽度为屏幕宽度
        View root = dialog.findViewById(R.id.root);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) root.getLayoutParams();
        lp.width = getResources().getDisplayMetrics().widthPixels;
        root.setLayoutParams(lp);

        ((TextView) dialog.findViewById(R.id.tv_shifukuan_b)).setText("￥" + (shifukuan + d_jiaotongfei));

        tab_tvs_b[0] = (ImageView) dialog.findViewById(R.id.img_alipay);
        tab_tvs_b[1] = (ImageView) dialog.findViewById(R.id.img_wexin);
        tab_tvs_b[1].setSelected(true);
        payType_b = 2;

        tab_tvs_b[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_tvs_b[0].setSelected(true);
                tab_tvs_b[1].setSelected(false);
                payType_b = 1;
            }
        });
        tab_tvs_b[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tab_tvs_b[1].setSelected(true);
                tab_tvs_b[0].setSelected(false);
                payType_b = 2;
            }
        });

        dialog.findViewById(R.id.btn_jixuzhifu).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  //继续支付
                getSubmitPay(payType_b);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_chongzhi).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//充值
                Intent intent = new Intent(context, RechargeAc2.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
