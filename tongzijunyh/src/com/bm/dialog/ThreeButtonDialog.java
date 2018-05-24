package com.bm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.richer.tzj.R;
/**
 * 三个按钮对话框
 * @author wangqiang 2014-8-15
 *
 */
public class ThreeButtonDialog extends Dialog{
	final static String TAG = ThreeButtonDialog.class.getSimpleName();
	public ThreeButtonDialog(Context context) {
		super(context, R.style.MyDialog);
		getWindow().getAttributes().windowAnimations = R.style.SlideUpDialogAnimation;

		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.BOTTOM| Gravity.CENTER;
		getWindow().setAttributes(wl);

		setCanceledOnTouchOutside(true);
	}

	private String btn1Text = null;
	private String btn2Text = null;

	private Button btn_1;
	private Button btn_2;
	private Button btn_cancel;

	private View.OnClickListener btn1Listener;
	private View.OnClickListener btn2Listener;
	private View.OnClickListener cancelListener;

	private boolean autoHide = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_three_button);
		//设置视图宽度为屏幕宽度
		View root = findViewById(R.id.root);
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) root.getLayoutParams();
		lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
		root.setLayoutParams(lp);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		if(btn1Text != null){
			btn_1.setText(btn1Text);
		}
		if(btn2Text != null){
			btn_2.setText(btn2Text);
		}
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(autoHide)dismiss();
				if(cancelListener != null){
					cancelListener.onClick(arg0);
				}
			}
		});
		btn_1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(autoHide) dismiss();
				if(btn1Listener != null){
					btn1Listener.onClick(arg0);
				}
			}
		});
		btn_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(autoHide) dismiss();
				if(btn2Listener != null){
					btn2Listener.onClick(arg0);
				}
			}
		});
	}
	
	/**
	 * 设置第一个按钮的文字
	 * @param text
	 */
	public ThreeButtonDialog setFirstButtonText(String text){
		btn1Text = text;
		if(btn_1 != null){
			btn_1.setText(text);	
		}
		return this;
	}
	
	public ThreeButtonDialog setThecondButtonText(String text){
		btn2Text = text;
		if(btn_2 != null){
			btn_2.setText(text);	
		}
		return this;
	}

	/**
	 * 任何按钮点击以后自动关闭对话框
	 */
	public ThreeButtonDialog autoHide() {
		autoHide = true;
		return this;
	}
	
	/**
	 * 取消
	 * @param cancelListener
	 */
	public ThreeButtonDialog setCancelListener(View.OnClickListener cancelListener) {
		this.cancelListener = cancelListener;
		return this;
	}
	
	public ThreeButtonDialog setBtn1Listener(View.OnClickListener btn1Listener) {
		this.btn1Listener = btn1Listener;
		return this;
	}
	
	public ThreeButtonDialog setBtn2Listener(View.OnClickListener btn2Listener) {
		this.btn2Listener = btn2Listener;
		return this;
	}
}