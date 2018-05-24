package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class DisableHorizontalScrollView extends HorizontalScrollView{

	public DisableHorizontalScrollView(Context context) {
		super(context);
	}
	
	public DisableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}
}
