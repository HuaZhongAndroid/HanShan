package com.bm.tzj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.bm.app.App;
import com.lib.widget.ScaleImageView;
import com.lib.widget.ScaleImageView.OnSingleTabUpListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.richer.tzj.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * 查看大图
 * 
 *  传递图片数组
 * @author Administrator
 *
 */
public class ImageViewActivity extends Activity {
	private String[] images = null;
	private int position;
	private ViewPager mViewPager;
	private View root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("1111111111111111111111111111");
		root = LayoutInflater.from(this).inflate(R.layout.ac_common_image_view, null);
		setContentView(root);
		mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);
		images = getIntent().getStringArrayExtra("images");
		position = getIntent().getIntExtra("position", 0);
		if(images == null){
			App.toast("没有图片");
			finish();
			return;
		}
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(position);
		LayoutParams lp = root.getLayoutParams();
		lp.width = getResources().getDisplayMetrics().widthPixels;
		
	}


	class SamplePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			final ScaleImageView photoView = new ScaleImageView(container.getContext());
			photoView.setImageResource(R.drawable.bg_image_circle);
			photoView.setScaleType(ScaleType.CENTER_INSIDE);
			photoView.setOnSingleTabUpListener(new OnSingleTabUpListener() {
				@Override
				public void onSingleTabUp(MotionEvent e) {
					finish();
				}
			});
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			System.out.println("加载大图 ："+images[position]);
			ImageLoader.getInstance().displayImage(images[position], photoView, App.getInstance().getListViewDisplayImageOptions());
			ImageLoader.getInstance().loadImage(images[position], new SimpleImageLoadingListener(){
				@Override
				public void onLoadingComplete(final String imageUri, View view,
											  final Bitmap loadedImage) {
					if(loadedImage != null) {
						Log.i("TAG", "大图浏览宽度:" + loadedImage.getWidth());
						photoView.setImageBitmap(loadedImage);
					}

				}
			});

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}





}
