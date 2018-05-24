package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bm.entity.Adverts;
import com.bm.tzj.activity.AdvWebActivity;
import com.bm.util.GlobalPrams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.richer.tzj.R;

public class GuanggaoDialog extends Dialog {

	private Activity context;

	private ImageView img_gg;


	public GuanggaoDialog(Activity context) {
		super(context, R.style.HongbaoDialog);
		this.getWindow().setWindowAnimations(R.style.HongbaoDialog);

		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_guanggao);

		img_gg = (ImageView)this.findViewById(R.id.img_gg);

		this.findViewById(R.id.iv_close).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				GuanggaoDialog.this.cancel();
				GuanggaoDialog.this.dismiss();
				return false;
			}
		});

	}

	public void setAd(final Adverts ad)
	{
		//显示图片的配置  
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
		.cacheInMemory(true)  
		.cacheOnDisk(true)  
		.build();  
		ImageLoader.getInstance().loadImage(ad.imageUrl, options, new SimpleImageLoadingListener(){  

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				super.onLoadingFailed(imageUri, view, failReason);
			}

			@Override  
			public void onLoadingComplete(String imageUri, View view,  
					Bitmap loadedImage) {  
				super.onLoadingComplete(imageUri, view, loadedImage);
				GuanggaoDialog.this.show();
				img_gg.setImageBitmap(loadedImage);  
				img_gg.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(ad.linkUrl != null && !"".equals(ad.linkUrl))
						{
							Intent intent = new Intent(context,AdvWebActivity.class);        
							intent.putExtra(GlobalPrams.WebUrl, ad.linkUrl);
							context.startActivity(intent);
						}
						GuanggaoDialog.this.cancel();
						GuanggaoDialog.this.dismiss();
					}
				});
			}
		});
	}


}
