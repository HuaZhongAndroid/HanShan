package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Storelist;
import com.bm.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 课程首页 gallery图片
 * @author wanghy
 *
 */
public class CourseGalleryAdapter  extends BaseAd<Storelist>{
	public CourseGalleryAdapter(Context context,List<Storelist> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_gallery_course, null);
			itemView.iv_image = (ImageView)convertView.findViewById(R.id.iv_image);
			itemView.fm_layout = (FrameLayout)convertView.findViewById(R.id.fm_layout);
			itemView.room_ratingbars=(RatingBar) convertView.findViewById(R.id.room_ratingbars);
			itemView.ll_=(LinearLayout) convertView.findViewById(R.id.ll_);
			itemView.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			itemView.custom_ratingbar=(com.lib.widget.RatingBarView) convertView.findViewById(R.id.custom_ratingbar);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
//		FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams();
//		itemView.iv_image.setLayoutParams(params)
//		itemView.room_ratingbars.setRating(4);
		
		
		
		int w = App.getInstance().getScreenWidth();
		if(getCount()>2){
			itemView.iv_image.setLayoutParams(new FrameLayout.LayoutParams(w/13*6-Util.dpToPx(10, context.getResources()),LayoutParams.FILL_PARENT));
			itemView.ll_.setLayoutParams(new FrameLayout.LayoutParams(w/13*6-Util.dpToPx(10, context.getResources()),Util.dpToPx(40, context.getResources())));
			//HorizontalListView.LayoutParams laym = new HorizontalListView.LayoutParams(w/2,w+50);
			FrameLayout.LayoutParams lp = (LayoutParams)itemView.iv_image.getLayoutParams();
			if(position!=0){
				lp.setMargins(10, 0, 0, 0);
			}
			FrameLayout.LayoutParams lp2 = (LayoutParams)itemView.ll_.getLayoutParams();
			if(position!=0){
				lp2.setMargins(10, 0, 0, 0);
			}
			lp2.gravity=Gravity.BOTTOM;
		}else if(getCount()==2){
			itemView.iv_image.setLayoutParams(new FrameLayout.LayoutParams(w/2*1-Util.dpToPx(10, context.getResources()),LayoutParams.FILL_PARENT));
			itemView.ll_.setLayoutParams(new FrameLayout.LayoutParams(w/2*1-Util.dpToPx(10, context.getResources()),Util.dpToPx(40, context.getResources())));
			//HorizontalListView.LayoutParams laym = new HorizontalListView.LayoutParams(w/2,w+50);
			FrameLayout.LayoutParams lp = (LayoutParams)itemView.iv_image.getLayoutParams();
			if(position!=0){
				lp.setMargins(10, 0, 20, 0);
			}
			FrameLayout.LayoutParams lp2 = (LayoutParams)itemView.ll_.getLayoutParams();
			if(position!=0){
				lp2.setMargins(10, 0, 20, 0);
			}
			lp2.gravity=Gravity.BOTTOM;
		}else if(getCount()==1){
			itemView.iv_image.setLayoutParams(new FrameLayout.LayoutParams(w/1-Util.dpToPx(10, context.getResources()),LayoutParams.FILL_PARENT));
			itemView.ll_.setLayoutParams(new FrameLayout.LayoutParams(w/1-Util.dpToPx(10, context.getResources()),Util.dpToPx(40, context.getResources())));
			//HorizontalListView.LayoutParams laym = new HorizontalListView.LayoutParams(w/2,w+50);
			FrameLayout.LayoutParams lp = (LayoutParams)itemView.iv_image.getLayoutParams();
			if(position!=0){
				lp.setMargins(10, 0, 20, 0);
			}
			FrameLayout.LayoutParams lp2 = (LayoutParams)itemView.ll_.getLayoutParams();
			if(position!=0){
				lp2.setMargins(10, 0, 20, 0);
			}
			lp2.gravity=Gravity.BOTTOM;
		}
		
		//itemView.fm_layout.setLayoutParams(new HorizontalListView.LayoutParams(30,w+50));
		Storelist entity= mList.get(position);
		
		
		
//		ImageLoader.getInstance().displayImage("http://xbyx.cersp.com/xxzy/UploadFiles_7930/200808/20080810110053944.jpg", itemView.iv_image,App.getInstance().getListViewDisplayImageOptions());
		ImageLoader.getInstance().displayImage(entity.titleMultiUrl, itemView.iv_image,App.getInstance().getListViewDisplayImageOptions());
//		itemView.room_ratingbar.setRating(3);
//		itemView.room_ratingbars.setRating(Integer.valueOf(entity.rankLogistics==null?"0":entity.rankLogistics));
		itemView.custom_ratingbar.setStar(Integer.valueOf(entity.rankLogistics==null?"0":entity.rankLogistics), true);;
		itemView.tv_name.setText(getNullData(entity.storeName));//门店名
		
	
		return convertView;
	}
	class ItemView{
		private TextView tv_name;
		private ImageView iv_image;
		private FrameLayout fm_layout;
		private RatingBar room_ratingbars;
		private com.lib.widget.RatingBarView custom_ratingbar;
		private LinearLayout ll_;
	
	}
}
