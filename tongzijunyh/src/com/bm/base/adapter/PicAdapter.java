package com.bm.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bm.app.App;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

public class PicAdapter extends BaseAdapter {
	private String[] imgArray;
	private Context context;

	public PicAdapter(Context context, String[] imgArray) {
		this.imgArray = imgArray;
		this.context = context;
	}

	@Override
	public int getCount() {
		return imgArray.length;
	}

	@Override
	public Object getItem(int position) {
		return imgArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View contextView, ViewGroup arg2) {
		ItemView itemView = null;
		if (itemView == null) {
			itemView = new ItemView();
			contextView = LayoutInflater.from(context).inflate(R.layout.item_reply_gridview_pic, null);

			itemView.img_src = (RoundImageViewFive) contextView.findViewById(R.id.img_src);
			contextView.setTag(itemView);

		} else {
			contextView.getTag();
		}
		// 赋值
		String myPhoto = imgArray[position];
		ImageLoader.getInstance().displayImage(myPhoto, itemView.img_src,App.getInstance().getListViewDisplayImageOptions());

		return contextView;
	}

	class ItemView {
		private RoundImageViewFive img_src;

	}
}
