package com.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 圆环图片(background为圆环颜色，tag为圆形填充色(例如#ffffff)，padding为圆环线宽)
 *
 */
public class CircleRingLayout extends LinearLayout {
	public CircleRingLayout(Context context) {
		super(context);
		initColor(null);
	}

	public CircleRingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initColor(attrs);
	}

	private Paint paint;
	private boolean init = false;
	private RectF rect = new RectF();
	private RectF fillRect = new RectF();
	private int background = Color.WHITE;
	private int fillColor = Color.TRANSPARENT;

	private void initColor(AttributeSet attrs) {
		int[] styles = new int[] { android.R.attr.background,
				android.R.attr.tag };
		TypedArray ta = null;
		if (attrs != null) {
			ta = getContext().obtainStyledAttributes(attrs, styles);
			background = ta.getColor(0, Color.WHITE);
		}
		int style = attrs.getStyleAttribute();
		if (style != 0) {
			ta = getContext().obtainStyledAttributes(style, styles);
			if (background != Color.WHITE)
				background = ta.getColor(0, Color.WHITE);
		}
		if (getTag() != null) {
			try {
				fillColor = Color.parseColor(getTag().toString());
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		if (ta != null)
			ta.recycle();
		setBackgroundResource(android.R.color.transparent);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!init) {
			initPaint();
		}
	}

	private void initPaint() {
		paint = new Paint();
		// paint.setColor(Color.TRANSPARENT);
		paint.setAntiAlias(true);
		float lineWidth = 2;
		if (getPaddingLeft() != 0) {
			lineWidth = getPaddingLeft();
		} else if (getPaddingBottom() != 0) {
			lineWidth = getPaddingBottom();
		} else if (getPaddingLeft() != 0) {
			lineWidth = getPaddingLeft();
		} else if (getPaddingTop() != 0) {
			lineWidth = getPaddingTop();
		}
		paint.setStrokeWidth(lineWidth);
		init = true;
		rect.left = lineWidth / 2;
		rect.top = lineWidth / 2;
		rect.right = getMeasuredWidth() - lineWidth / 2;
		rect.bottom = getMeasuredHeight() - lineWidth / 2;
		fillRect.left = lineWidth;
		fillRect.top = lineWidth;
		fillRect.right = getMeasuredWidth() - lineWidth;
		fillRect.bottom = getMeasuredHeight() - lineWidth;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setStyle(Style.STROKE);
		paint.setColor(background);
		canvas.drawOval(rect, paint);
		paint.setStyle(Style.FILL);
		paint.setColor(fillColor);
		canvas.drawOval(rect, paint);
	}
}
