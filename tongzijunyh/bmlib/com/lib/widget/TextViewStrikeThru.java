package com.lib.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;



/**
 * 删除线的TextView
 * 
 *
 */
public class TextViewStrikeThru extends TextView{
	public TextViewStrikeThru(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}
	public TextViewStrikeThru(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}
	public TextViewStrikeThru(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	}

}
