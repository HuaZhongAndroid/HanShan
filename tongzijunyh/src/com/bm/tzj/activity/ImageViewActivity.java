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
					if(loadedImage != null){
						Log.i("TAG", "大图浏览宽度:"+loadedImage.getWidth());
						photoView.setImageBitmap(loadedImage);
					}


					photoView.setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							String[] sp = imageUri.split("/");
							saveBmp2Gallery(photoView.getContext(),loadedImage,sp[sp.length-1]);
							return false;
						}
					});
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








	/**
	 * 保存图片到相册
	 * @param bmp 获取的bitmap数据
	 * @param picName 自定义的图片名
	 */
	public static void saveBmp2Gallery(Context ctx, Bitmap bmp, String picName) {

		String fileName = null;
		//系统相册目录
		String galleryPath= Environment.getExternalStorageDirectory()
				+ File.separator + Environment.DIRECTORY_DCIM
				+File.separator+"Camera"+File.separator;


		// 声明文件对象
		File file = null;
		// 声明输出流
		FileOutputStream outStream = null;

		try {
			// 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
			file = new File(galleryPath, picName+ ".jpg");

			// 获得文件相对路径
			fileName = file.toString();
			// 获得输出流，如果文件中有内容，追加内容
			outStream = new FileOutputStream(fileName);
			if (null != outStream) {
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
			}

		} catch (Exception e) {
			e.getStackTrace();
		}finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		//通知相册更新
		MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
				bmp, fileName, null);
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		ctx.sendBroadcast(intent);

		Toast.makeText(ctx,"图片已保存相册",Toast.LENGTH_SHORT).show();
	}

}
