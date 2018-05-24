package com.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
/**
 * 可滑动的LinearLayout
 *
 */
public class SlideUpLinearLayout extends LinearLayout {  
	static final String TAG = "SlideUpLinearLayout"; 

	private boolean mFinished = true;
	private boolean isOppend = false;

	public interface OnScrollFinishedListener{
		public void onScrollFinished(ViewGroup scrollView);
	}

	private Scroller mScroller;
	private OnScrollFinishedListener mOnScrollFinishedListener;

	public void setOnScrollFinishedListener(
			OnScrollFinishedListener mOnScrollFinishedListener) {
		this.mOnScrollFinishedListener = mOnScrollFinishedListener;
	}
	public void removeOnScrollFinishedListener(){
		mOnScrollFinishedListener = null;
	}

	public boolean isFinished() {
		return mFinished;
	}

	public SlideUpLinearLayout(Context context, AttributeSet attrs) {  
		super(context, attrs);  
		mScroller = new Scroller(context);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		//初次显示隐藏
		post(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "隐藏菜单>>>>>>>>>>>>>>getMeasuredHeight()="+-getMeasuredHeight());
				scrollTo(0, -getMeasuredHeight());
			}
		});
	}

	/**
	 * 打开
	 */
	public void postSlideUp(){
		post(new Runnable() {
			@Override
			public void run() {
				smoothScrollTo(0, 0, 400);
				isOppend = true;
			}
		});
	}

	/**
	 * 关闭
	 */
	public void postSlideDown(){
		post(new Runnable() {
			@Override
			public void run() {
				smoothScrollTo(0, -getMeasuredHeight(), 400);
				isOppend = false;
			}
		});
	}
	/**
	 * 直接关闭菜单
	 */
	public void postClose(){
		post(new Runnable() {
			@Override
			public void run() {
				scrollTo(0, -getMeasuredHeight());
				isOppend = false;
				setVisibility(View.VISIBLE);
			}
		});
	}
	/**
	 * 直接关闭菜单
	 */
	public void close(){
		scrollTo(0, -getMeasuredHeight());
		isOppend = false;
	}
	public boolean isOppend() {
		return isOppend;
	}
	/**
	 * 开关
	 */
	public void toggle(){
		if(isOppend){
			postSlideDown();
		}else{
			postSlideUp();
		}
	}

	public void setInterpolator(Interpolator interpolator){
		mScroller = new Scroller(getContext(), interpolator);
	}

	/**
	 * 调用此方法滚动到目标位置  
	 * @param fx
	 * @param fy
	 */
	public void smoothScrollTo(int fx, int fy, int duration) {
		int dx = fx - getScrollX();  
		int dy = fy - getScrollY();  
		smoothScrollBy(dx, dy, duration);  
	}

	/**
	 * 调用此方法设置滚动的相对偏移
	 * @param dx
	 * @param dy
	 */
	public void smoothScrollBy(int dx, int dy, int duration) {  
		//设置mScroller的滚动偏移量
		mFinished = false;
		mScroller.startScroll(getScrollX(), getScrollY(), dx, dy, duration);
		invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果  
	}

	@Override  
	public void computeScroll() {
		//先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {  

			//这里调用View的scrollTo()完成实际的滚动  
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
			//必须调用该方法，否则不一定能看到滚动效果  
			postInvalidate();

			if(mScroller.isFinished()){
				mFinished = true;
				if(mOnScrollFinishedListener != null){
					mOnScrollFinishedListener.onScrollFinished(this);
				}
				mOnScrollFinishedListener = null;
			}
		}
		super.computeScroll();  
	}
}  