package com.lib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 圆角遮罩处理(tag为圆角背景色, padding为圆角大小)
 *
 */
public class RoundCornerImageView extends ImageView {
	public RoundCornerImageView(Context context) {
		super(context);
		initColor(null);
	}
	public RoundCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initColor(attrs);
	}
	public RoundCornerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initColor(attrs);
	}

	private Paint paint;
	private Path roundRect;
	private int radius;
	private boolean initPaint = false;
	private int background = 0xffefefef;

	private void initColor(AttributeSet attrs){
		if(attrs != null){
			String v = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "tag");
			if(v != null){
				if(v.startsWith("#")){
					background = Color.parseColor(v);
				}else{
					background = getResources().getColor(Integer.parseInt(v.replaceAll("@", "")));
				}
			}
		}
	}

	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		background = color;
	}

	private void initPaint(){
		if(getPaddingLeft()!=0){
			radius = getPaddingLeft();
		}else if(getPaddingBottom()!=0){
			radius = getPaddingBottom();
		}else if(getPaddingLeft()!=0){
			radius = getPaddingLeft();
		}else if(getPaddingTop()!=0){
			radius = getPaddingTop();
		}
		setPadding(0, 0, 0, 0);
		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(background);
		paint.setAntiAlias(true);
		//paint.setShadowLayer(12, 12, 12, 0xFF555555);
		//paint.setColor(Color.RED);
       //shadowPaint.setColor(0xFF555555);

		roundRect = new Path();
		//正方形
		roundRect.moveTo(0, 0);
		roundRect.lineTo(getMeasuredWidth(), 0);
		roundRect.lineTo(getMeasuredWidth(), getMeasuredHeight());
		roundRect.lineTo(0, getMeasuredHeight());
		roundRect.lineTo(0, 0);
		//圆角矩形
		roundRect.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), radius, radius, Direction.CCW);
		initPaint = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initPaint();
		canvas.drawPath(roundRect, paint);
	}
}
