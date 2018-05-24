package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bm.tzj.mine.MyYouhuiquanAc;
import com.richer.tzj.R;

public class YouhuiquanDialog extends Dialog implements android.view.View.OnClickListener {

	private Activity context;
	
	
	public YouhuiquanDialog(Activity context) {
		super(context, R.style.HongbaoDialog);
		this.getWindow().setWindowAnimations(R.style.HongbaoDialog);
		
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_youhuiquan);
		
		
		this.findViewById(R.id.bt_cha).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				YouhuiquanDialog.this.cancel();
				return false;
			}
		});
		
		this.findViewById(R.id.bt_lingqu).setOnClickListener(this);
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
		switch(v.getId())
		{
		case R.id.bt_lingqu:
			Intent i = new Intent(context, MyYouhuiquanAc.class);
			context.startActivity(i);
			this.dismiss();
			break;
		}
	}
}
