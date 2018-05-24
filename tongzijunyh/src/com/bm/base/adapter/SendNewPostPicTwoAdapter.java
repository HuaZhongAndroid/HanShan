package com.bm.base.adapter;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.ImageTag;
import com.lib.widget.RoundImageViewsix;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/***
 * 
 * 
 * 图片adapter
 * 
 */

public class SendNewPostPicTwoAdapter extends BaseAd<ImageTag> {



    private Context context;
    private boolean isShowDelete;//根据这个变量来判断是否显示删除图标，true是显示，false是不显示
    private OnItemClickListener clickListener;
    public SendNewPostPicTwoAdapter(Context context,List<ImageTag> list) {
		setActivity(context, list);
        this.context = context;
        
    }
    public void setIsShowDelete(boolean isShowDelete){
		  this.isShowDelete=isShowDelete;
		  notifyDataSetChanged();
		 }
	
    
    
	public OnItemClickListener getClickListener() {
		return clickListener;
	}
	public void setClickListener(OnItemClickListener clickListener) {
		this.clickListener = clickListener;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
    	ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_grid_send_pic_two, parent, false);
			itemView.iv_pic = (RoundImageViewsix)convertView.findViewById(R.id.iv_pic);
			itemView.iv_delete = (ImageView)convertView.findViewById(R.id.iv_delete);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		final ImageTag model = mList.get(position);
		boolean tag = model.isImgTag();
		
		 if(!tag){
			 ImageLoader.getInstance().displayImage("file://"+model.getImageStr(), itemView.iv_pic, App.getInstance().getListViewDisplayImageOptions());
			 itemView.iv_delete.setVisibility(isShowDelete?View.VISIBLE:View.GONE);//设置删除按钮是否显示
		 }else{
			 itemView.iv_delete.setVisibility(View.GONE);//设置删除按钮是否显示
			 String  imgstr = "drawable://" + R.drawable.pic_add;
			  
			 ImageLoader.getInstance().displayImage(imgstr, itemView.iv_pic, App.getInstance().getListViewDisplayImageOptions());
			 
		 }
		 itemView.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = position;
					if(clickListener != null){
						clickListener.onItemClick((ImageView)v, pos);
					}
					
				}
			});
		return convertView;
	}

	@Override
	protected View setConvertView(View convertView, int position) {
		return null;
	}


	private class ItemView{
    	RoundImageViewsix iv_pic;
    	ImageView iv_delete;
        
    }

    
    public interface OnItemClickListener{
		public void onItemClick(ImageView view,int position);
	} 
    
    

	
}