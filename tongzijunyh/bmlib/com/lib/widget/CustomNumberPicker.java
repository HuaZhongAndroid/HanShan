package com.lib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.richer.tzj.R;

@SuppressLint("ResourceAsColor")
public class CustomNumberPicker extends NumberPicker {

	public CustomNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		updateView(child);
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		updateView(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		updateView(child);
	}

	public void updateView(View view) {
		if (view instanceof EditText) {
			
			// 这里修改字体的属性
			((EditText) view).setTextSize(14);
			((EditText) view).setTextColor(R.color.gray_deep);
		}
	}

}