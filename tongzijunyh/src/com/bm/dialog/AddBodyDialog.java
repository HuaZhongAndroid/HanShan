package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.bm.tzj.mine.AddChildAc;
import com.bm.tzj.mine.MyChildrenAc;
import com.bm.util.Util;
import com.lib.log.Lg;
import com.lib.tool.JsonFormatTool;
import com.richer.tzj.R;

public class AddBodyDialog extends Dialog implements View.OnClickListener {

	private TextView bt_tianjia;
	private Activity context;


	public AddBodyDialog(Activity context) {
		super(context, R.style.HongbaoDialog);
		this.getWindow().setWindowAnimations(R.style.HongbaoDialog);
		this.context = context;
		Lg.d("显示：1");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_addbody);
		Lg.d("显示：2");
		bt_tianjia = (TextView)this.findViewById(R.id.bt_tianjia);

		this.findViewById(R.id.iv_close).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				AddBodyDialog.this.cancel();
				return false;
			}
		});

		bt_tianjia.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.bt_tianjia:
				context.startActivity(new Intent(context, AddChildAc.class));
				this.dismiss();
				break;
		}
	}
}
