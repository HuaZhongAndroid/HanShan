package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.share.ShareModel;
import com.bm.share.ShareUtil;
import com.bm.tzj.activity.MainAc;
import com.bm.util.Util;
import com.richer.tzj.R;

public class HongbaoDialog extends Dialog implements android.view.View.OnClickListener {

	private TextView tv_money, tv_money_des;
	private Activity context;
	
	
	public HongbaoDialog(Activity context) {
		super(context, R.style.HongbaoDialog);
		this.getWindow().setWindowAnimations(R.style.HongbaoDialog);
		
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_hongbao);
		
		tv_money = (TextView)this.findViewById(R.id.tv_money);
		tv_money_des = (TextView)this.findViewById(R.id.tv_money_des);
		
		this.findViewById(R.id.iv_close).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				HongbaoDialog.this.cancel();
				return false;
			}
		});
		
		this.findViewById(R.id.ll_weixin).setOnClickListener(this);
		this.findViewById(R.id.ll_quan).setOnClickListener(this);
		this.findViewById(R.id.ll_xinlang).setOnClickListener(this);
		this.findViewById(R.id.ll_qqzone).setOnClickListener(this);
	}
	
	public void windowDeploy(int x, int y){
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.HongbaoDialog); //设置窗口弹出动画
//        window.setBackgroundDrawableResource(R.color.vifrification); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移  
//        wl.alpha = 0.6f; //设置透明度
//        wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }
	
	public void setMoney(String money)
	{
		tv_money.setText(money);
		tv_money_des.setText(Html.fromHtml("恭喜您获得<font color='red'>"+money+"</font>元赠送金额<br />快告诉小伙伴，一起体验探索的乐趣"));
	}

	@Override
	public void onClick(View v) {
		ShareUtil share = MainAc.intance.share;
		ShareModel model = new ShareModel();
		model.title = "给孩子送福利喽！红包限量抢送";
		model.conent = "我已在君昂喊山领到红包啦，你也赶紧去吧，只要完成新人注册，即得红包哦。";
		model.targetUrl = BaseApi.SHARE_URL + "&userid="+App.getInstance().getUser().userid + "&appVersion="+Util.getAppVersion(context);
		
		switch(v.getId())
		{
		case R.id.ll_weixin:
			share.shareInfo(model,2);
			break;
		case R.id.ll_quan:
			share.shareInfo(model,3);
			break;
		case R.id.ll_xinlang:
			share.shareInfo(model,1);
			break;
		case R.id.ll_qqzone:
			share.shareInfo(model,5);
			break;
		}
	}
}
