package com.bm.base.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.bm.app.App;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 图片滑动ViewPager适配器
 * 
 */
public class ImagePagerAdapters280 extends PagerAdapter {
	static final String TAG = "ImagePagerAdapter";
	private Context context;
	private String[] data;
	private ImageView[] mViews;

	public ImagePagerAdapters280(Context context, String[] data) {
		this.data = data;
		mViews = new ImageView[data.length];
		this.context = context;
	}

	private OnItemClickListener onItemClickListener;

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		ImageView imageView = mViews[position];
		if (imageView == null) {
			String imageUrl = data[position];
			imageView = new ImageView(context);
			int w = App.getInstance().getScreenWidth();
			int h = (300 * w) / 700;
			imageView.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, h));
			imageView.setScaleType(ScaleType.CENTER_CROP);
				ImageLoader.getInstance().displayImage(imageUrl, imageView,App.getInstance().getBigImageOptions());

			mViews[position] = imageView;
			// imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setTag(position);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = (Integer) view.getTag();
					if (onItemClickListener != null) {
						onItemClickListener.onItemClick((ImageView) view, pos);
					}
				}
			});
		}
		((ViewPager) view).addView(imageView, 0);
		return imageView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	public interface OnItemClickListener {
		public void onItemClick(ImageView view, int position);
	}
}