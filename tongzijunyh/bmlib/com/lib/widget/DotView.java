package com.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.richer.tzj.R;

public class DotView extends LinearLayout {
	
    private int mLittleDotWidth;
    private int mDotSpan = 36;
    private float mDotRadius = 6f;
    private int mDotNumber = 5;
    
    private int mCurrent = 0;

    private int mSelectedColor = 0xFF377BEE;
    private int mUnSelectedColor = 0xFFC5CEDB;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义的属性
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);
        if (arr != null) {
            if (arr.hasValue(R.styleable.DotView_dot_radius)) {
                mDotRadius = arr.getDimension(R.styleable.DotView_dot_radius, mDotRadius);
            }

            if (arr.hasValue(R.styleable.DotView_dot_span)) {
                mDotSpan = (int) arr.getDimension(R.styleable.DotView_dot_span, mDotSpan);
            }
            
            if(arr.hasValue(R.styleable.DotView_dot_number)){
            	mDotNumber = (int) arr.getInt(R.styleable.DotView_dot_number, mDotNumber);
            }

            mSelectedColor = arr.getColor(R.styleable.DotView_dot_selected_color, mSelectedColor);
            mUnSelectedColor = arr.getColor(R.styleable.DotView_dot_unselected_color, mUnSelectedColor);
            arr.recycle();
        }
        mLittleDotWidth = (int) (mDotSpan / 2 + mDotRadius * 2);
        
        //把小点画出来
        addDotViews();
    }

    public void setDotNumber(int dotNumber){
    	this.mDotNumber = dotNumber;
    	addDotViews();
    }
    
    private void addDotViews(){
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(HORIZONTAL);
        removeAllViews();
        for (int i = 0; i < mDotNumber; i++) {
            LittleDot dot = new LittleDot(getContext(), i);
            if (i == 0) {
                dot.setColor(mSelectedColor);
            } else {
                dot.setColor(mUnSelectedColor);
            }
            dot.setLayoutParams(new LayoutParams((int) mLittleDotWidth, (int) mDotRadius * 2, 1));
            addView(dot);
        }
    }
    
    public final void setSelected(int index) {
        if (index >= getChildCount() || index < 0 || mCurrent == index) {
            return;
        }
        if (mCurrent < getChildCount() && mCurrent >= 0) {
            ((LittleDot) getChildAt(mCurrent)).setColor(mUnSelectedColor);
        }
        ((LittleDot) getChildAt(index)).setColor(mSelectedColor);
        mCurrent = index;
    }

    private class LittleDot extends View {

        private int mColor;
        private Paint mPaint;

        public LittleDot(Context context, int index) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        public void setColor(int color) {
            if (color == mColor){
            	return;
            }
            mColor = color;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mPaint.setColor(mColor);
            canvas.drawCircle(mLittleDotWidth / 2, mDotRadius, mDotRadius, mPaint);
        }
    }

    public void setSelectedColor(int color) {
        if (mSelectedColor != color) {
            mSelectedColor = color;
            invalidate();
        }
    }

    public void setUnSelectedColor(int color) {
        if (mUnSelectedColor != color) {
            mSelectedColor = color;
            invalidate();
        }
    }
    
    public void setColor(int selectedColor, int unSelectedColor) {
        if (mSelectedColor != selectedColor || mUnSelectedColor != unSelectedColor) {
            mSelectedColor = selectedColor;
            mUnSelectedColor = unSelectedColor;
            invalidate();
        }
    }
}
