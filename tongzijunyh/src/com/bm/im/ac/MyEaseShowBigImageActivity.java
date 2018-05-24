package com.bm.im.ac;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bm.util.Util;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.ImageMessageBody;
import com.easemob.easeui.R;
import com.easemob.easeui.model.EaseImageCache;
import com.easemob.easeui.ui.EaseBaseActivity;
import com.easemob.easeui.utils.EaseLoadLocalBigImgTask;
import com.easemob.easeui.widget.photoview.EasePhotoView;
import com.easemob.easeui.widget.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.easemob.easeui.widget.photoview.PhotoViewAttacher.OnViewTapListener;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;

public class MyEaseShowBigImageActivity extends EaseBaseActivity {
	private boolean isDownloaded;
	
	private List<ImageMessageBody> imgBodyList;
	
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(com.richer.tzj.R.layout.my_ease_activity_show_big_image);
		super.onCreate(savedInstanceState);
		
		imgBodyList = this.getIntent().getParcelableArrayListExtra("imgBodyList");
		
		viewPager = (ViewPager)this.findViewById(com.richer.tzj.R.id.viewpager);
		
		
		viewPager.setAdapter(pagerAdapter);
		
		viewPager.setCurrentItem(this.getIntent().getIntExtra("position", 0));
	}
	
	
	private PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if(imgBodyList == null)
				return 0;
			
			return imgBodyList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}  
	
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {  
            container.removeView((View)object);  
        }  
          
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
        	View v = new BigImageView(MyEaseShowBigImageActivity.this, imgBodyList.get(position));
        	container.addView(v);
            return v;  
        }  
	};
	
	
	private class BigImageView extends LinearLayout
	{
		private static final String TAG = "ShowBigImage"; 
		private ProgressDialog pd;
		private EasePhotoView image;
		private int default_res = R.drawable.ease_default_image;
		private String localFilePath;
		private Bitmap bitmap;
		private ProgressBar loadLocalPb;
		
		
		public BigImageView(Context context,ImageMessageBody imgBody) {
			super(context);
			
			View.inflate(MyEaseShowBigImageActivity.this, R.layout.ease_activity_show_big_image, this);
			
			image = (EasePhotoView) findViewById(R.id.image);
			loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
			default_res = getIntent().getIntExtra("default_image", R.drawable.default_avatar);
			
			Uri uri = null;
			String remotepath = null;
			String secret = null;
			File file = new File(imgBody.getLocalUrl());
	        if (file.exists()) {
	            uri = Uri.fromFile(file);
	        } else {
	            // The local full size pic does not exist yet.
	            // ShowBigImage needs to download it from the server
	            // first
	        	secret = imgBody.getSecret();
	            remotepath = imgBody.getRemoteUrl();
	        }
			
			EMLog.d(TAG, "show big image uri:" + uri + " remotepath:" + remotepath);

			//本地存在，直接显示本地的图片
			if (uri != null && new File(uri.getPath()).exists()) {
				EMLog.d(TAG, "showbigimage file exists. directly show it");
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				// int screenWidth = metrics.widthPixels;
				// int screenHeight =metrics.heightPixels;
				bitmap = EaseImageCache.getInstance().get(uri.getPath());
				if (bitmap == null) {
					EaseLoadLocalBigImgTask task = new EaseLoadLocalBigImgTask(MyEaseShowBigImageActivity.this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
							ImageUtils.SCALE_IMAGE_HEIGHT);
					if (android.os.Build.VERSION.SDK_INT > 10) {
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						task.execute();
					}
				} else {
					image.setImageBitmap(bitmap);
				}
			} else if (remotepath != null) { //去服务器下载图片
				EMLog.d(TAG, "download remote image");
				Map<String, String> maps = new HashMap<String, String>();
				if (!TextUtils.isEmpty(secret)) {
					maps.put("share-secret", secret);
				}
				downloadImage(remotepath, maps);
			} else {
				image.setImageResource(default_res);
			}

//			image.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					finish();
//				}
//			});
			
			image.setOnPhotoTapListener(new OnPhotoTapListener(){
				@Override
				public void onPhotoTap(View view, float x, float y) {
					Log.d("fffff","onPhotoTap");
					finish();
				}});
			image.setOnViewTapListener(new OnViewTapListener(){
				@Override
				public void onViewTap(View view, float x, float y) {
					Log.d("fffff","onViewTap");
					finish();
				}});
			image.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {
					Log.d("fffff","onLongClick");
					image.showContextMenu();
					return false;
				}});
			image.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
				@Override
				public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
					MenuItem mi = menu.add(0, 0, 0, "保存到本地");
					mi.setActionView(image);
				}});
			
			
		}
		
		/**
		 * 通过远程URL，确定下本地下载后的localurl
		 * @param remoteUrl
		 * @return
		 */
		public String getLocalFilePath(String remoteUrl){
			String localPath;
			if (remoteUrl.contains("/")){
				localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
						+ remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
			}else{
				localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
			}
			return localPath;
		}
		
		/**
		 * 下载图片
		 * 
		 * @param remoteFilePath
		 */
		private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
			String str1 = getResources().getString(R.string.Download_the_pictures);
			pd = new ProgressDialog(MyEaseShowBigImageActivity.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCanceledOnTouchOutside(false);
			pd.setMessage(str1);
			pd.show();
			localFilePath = getLocalFilePath(remoteFilePath);
			final EMCallBack callback = new EMCallBack() {
				public void onSuccess() {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							DisplayMetrics metrics = new DisplayMetrics();
							getWindowManager().getDefaultDisplay().getMetrics(metrics);
							int screenWidth = metrics.widthPixels;
							int screenHeight = metrics.heightPixels;

							bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
							if (bitmap == null) {
								image.setImageResource(default_res);
							} else {
								image.setImageBitmap(bitmap);
								EaseImageCache.getInstance().put(localFilePath, bitmap);
								isDownloaded = true;
							}
							if (pd != null) {
								pd.dismiss();
							}
						}
					});
				}

				public void onError(int error, String msg) {
					EMLog.e(TAG, "offline file transfer error:" + msg);
					File file = new File(localFilePath);
					if (file.exists()&&file.isFile()) {
						file.delete();
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pd.dismiss();
							image.setImageResource(default_res);
						}
					});
				}

				public void onProgress(final int progress, String status) {
					EMLog.d(TAG, "Progress: " + progress);
					final String str2 = getResources().getString(R.string.Download_the_pictures_new);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							
							pd.setMessage(str2 + progress + "%");
						}
					});
				}
			};

		    EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath, headers, callback);

		}
		
	}

	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case 0:
			EasePhotoView image = (EasePhotoView)item.getActionView();
			Bitmap bm =((BitmapDrawable) image.getDrawable()).getBitmap(); 
			String path = "/tzj/pic/";
			Util.saveBitmap(path, bm);
			Toast.makeText(this, "图片已保存至 "+path+" 目录下", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}



	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}
}
