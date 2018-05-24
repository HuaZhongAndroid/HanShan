package com.bm.util;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bm.app.App;
import com.richer.tzj.R;

 

/**
 * spinner工具包
 * 
 * @author jiangshihua
 * 
 */
public class SpinnerUtil {
	ArrayAdapter<String> arrayAdapter;
	Spinner spinnerView;
	private Animation animation;
	int layout;
	int anim;
	String[] sp;

	public SpinnerUtil(Spinner spinnerView, int layout, String[] sp, int anim) {
		this.spinnerView = spinnerView;
		this.layout = layout;
		this.sp = sp;
	}

	public void showSpinner() {
		arrayAdapter = new ArrayAdapter<String>(App.getInstance()
				.getApplicationContext(), R.layout.spinner_checked_text,
				sp);
		arrayAdapter.setDropDownViewResource(layout);
		spinnerView.setAdapter(arrayAdapter);
		// 为下拉列表添加动画
		if (0 != anim) {
			// 获取animation
			animation = AnimationUtils.loadAnimation(App.getInstance()
					.getApplicationContext(), anim);
			// 添加动画
			spinnerView.setAnimation(animation);
		}
	}
}
