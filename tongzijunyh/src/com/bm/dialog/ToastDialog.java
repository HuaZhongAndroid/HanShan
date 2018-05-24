package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.richer.tzj.R;
/**
 * toast
 *
 */
public class ToastDialog extends Dialog{
	final static String TAG = ToastDialog.class.getSimpleName();
	public ToastDialog(Context context) {
		super(context, R.style.MyDialog);
		setCanceledOnTouchOutside(true);
	}

	private String info = null;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_toast);
		tv = (TextView) findViewById(R.id.tv);
		tv.setText(info);
	}

	/**
	 * 指定毫秒数以后自动销毁
	 * @param msg
	 * @param delayMs
	 * @return
	 */
	public ToastDialog show(String msg, int delayMs){
		info = msg;
		if(tv != null){
			tv.setText(msg);
		}
		super.show();
		handler.removeCallbacks(close);
		handler.postDelayed(close, delayMs);
		return this;
	}
	
	private Handler handler = new Handler();
	private Runnable close = new Runnable() {
		@Override
		public void run() {
			dismiss();
		}
	};
	
	/**
	 * 指定毫秒数以后自动销毁
	 * @param msg
	 * @param delayMs
	 * @return
	 */
	public ToastDialog showHandler(String msg, int delayMs,final Activity ac){
		info = msg;
		if(tv != null){
			tv.setText(msg);
		}
		super.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
				ac.finish();
			}
		}, delayMs);
		return this;
	}

}