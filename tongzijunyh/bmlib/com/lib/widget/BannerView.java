package com.lib.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bm.app.App;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BannerView extends FrameLayout{

	private DotView mBannerDotView;
	private ViewPager mBannerViewPager;
	private BannerAdapter mBannerAdapter;
	/**当前的position*/
	private int mBannerPosition = 0;
	/**Banner点击后的回调*/
	private OnBannerClickListener mBannerClickListener;
	/**自动播放相关*/
    private Handler mHandler = new Handler();
	private Runnable task = new Runnable(){
		@Override
		public void run() {
              mBannerPosition = (mBannerPosition + 1) % mBannerAdapter.getCount();
              mBannerViewPager.setCurrentItem(mBannerPosition);
              Log.d(TAG, "tname:" + Thread.currentThread().getName());
              mHandler.postDelayed(task, 3000);
		}
	};
	
	private static final String TAG = BannerView.class.getSimpleName();
	
	public BannerView(Context context) {
		this(context, null);
	}

	public BannerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//注意这里的潜规则
		mBannerViewPager = (ViewPager)getChildAt(0);
        mBannerDotView = (DotView)getChildAt(1);
        mBannerDotView.setDotNumber(0);
        mBannerAdapter = new BannerAdapter(getContext(), new int[0]);
        mBannerViewPager.setAdapter(mBannerAdapter);
        mBannerViewPager.setOnPageChangeListener(mBannerAdapter);
	}

	public void update(int[] imagesSrc){
		if(imagesSrc == null || imagesSrc.length <= 0){
			return;
		}
		mBannerDotView.setDotNumber(imagesSrc.length);
		mBannerDotView.setSelected(0);
		mBannerAdapter.update(imagesSrc);
        mHandler.postDelayed(task, 3000);
	}
	public void update(String[] imagesSrc){
		if(imagesSrc == null || imagesSrc.length <= 0){
			return;
		}
		mBannerDotView.setDotNumber(imagesSrc.length);
		mBannerDotView.setSelected(0);
		mBannerAdapter.update(imagesSrc);
        mHandler.postDelayed(task, 3000);
	}
	
	private void setIndicator(int position) {
        position %= mBannerAdapter.getSize();
        mBannerDotView.setSelected(position);
    }
	
	private int mDownX;
    private int mDownY;
    private long mDownTime;
	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mHandler.removeCallbacks(task);
            mDownX = (int)event.getX();
            mDownY = (int)event.getY();
            mDownTime = System.currentTimeMillis();
        } else if (action == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - mDownTime < 500 && Math.abs(mDownX - event.getX()) < 5 && Math.abs(mDownY - event.getY()) < 5) {
                // 接口回调
                if (mBannerClickListener != null) {
                	mBannerClickListener.OnBannerClicked(mBannerPosition%mBannerAdapter.getSize());
                }
            }
            mHandler.postDelayed(task, 3000);
        } else if(action == MotionEvent.ACTION_CANCEL){
        	 mHandler.postDelayed(task, 3000);
        } else if(action == MotionEvent.ACTION_MOVE){
        	// do nothing
        }
        return super.dispatchTouchEvent(event);
    }

	private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

		private Context mContext;
        private int[] mImagesSrc;
        private String[] mImagesStr;
        private int mSize;
        private int mFakeSize;

        public BannerAdapter(Context context, int[] imagesSrc) {
            mContext = context;
            update(mImagesSrc);
        }

        public void update(int[] imagesSrc) {
        	if(imagesSrc == null || imagesSrc.length <= 0){
        		return;
        	}
        	this.mImagesSrc = imagesSrc;
        	this.mSize = imagesSrc.length;
            this.mFakeSize = mSize * 10;
        	notifyDataSetChanged();
		}
        public void update(String[] imagesSrc) {
        	if(imagesSrc == null || imagesSrc.length <= 0){
        		return;
        	}
        	this.mImagesStr = imagesSrc;
        	this.mSize = imagesSrc.length;
            this.mFakeSize = mSize * 10;
        	notifyDataSetChanged();
		}

		@Override
        public int getCount() {
            return mFakeSize;
        }

        public int getSize() {
            return mSize;
        }
        
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= mSize;
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ScaleType.CENTER_CROP);
//            imageView.setImageResource(mImagesSrc[position]);
            ImageLoader.getInstance().displayImage(mImagesStr[position], imageView, App.getInstance().getListViewDisplayImageOptions());
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mBannerViewPager.getCurrentItem();
            Log.d(TAG, "finish update before, position=" + position);
            if (position == 0) {
                position = mSize;
                mBannerViewPager.setCurrentItem(position, false);
            } else if (position == getCount() - 1) {
                position = mSize - 1;
                mBannerViewPager.setCurrentItem(position, false);
            }
            Log.d(TAG, "finish update after, position=" + position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;
            setIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
	
	public interface OnBannerClickListener{
		public void OnBannerClicked(int pos);
	}

	public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
		this.mBannerClickListener = bannerClickListener;
	}

	@Override
	public void onDetachedFromWindow(){
		mHandler.removeCallbacksAndMessages(null);
		this.removeAllViews();
		this.mBannerClickListener = null;
		super.onDetachedFromWindow();
	}
}