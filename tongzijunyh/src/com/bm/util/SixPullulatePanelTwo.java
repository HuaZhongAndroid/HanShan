package com.bm.util;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.app.App;
import com.bm.entity.Badge;
import com.bm.tzj.fm.PullulateFm;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;


/**
 * 
 * 成长中心的  六边形   图片显示
 * @author wanghy
 *
 */
public class SixPullulatePanelTwo {

	
	static Handler handler;
	
	public static  void  setViews(FrameLayout fm_view, List<Badge> list,Context context,Handler strHandler){
		handler = strHandler;
		 List<List<Badge>> mlist = Util.subList(list,4);
		for(int i = 0;i<mlist.size();i++){
			View vm1 = LayoutInflater.from(context).inflate(R.layout.fm_view_six_item, null);
			fm_view.addView(vm1);
			setViewId(vm1,mlist.get(i));
			int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			vm1.measure(width,height);
//			int heights=Util.dpToPx(vm1.getMeasuredHeight()-50, context.getResources()); 
//			int heights=Util.dpToPx(vm1.getMeasuredHeight()-479, context.getResources()); 
			int heights=Util.dpToPx(214, context.getResources()); 
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vm1.getLayoutParams();
			lp.setMargins(0, heights*i, 0, 0);
		}
		
	}
	
	
	private static void setViewId(View v,List<Badge> list){
		 ImageView iv_a,iv_c,iv_d,iv_e, iv_a_bg,iv_c_bg,iv_d_bg,iv_e_bg;
		 
		 LinearLayout ll_left,ll_right;
		 iv_a_bg = (ImageView)v.findViewById(R.id.iv_a_bg);
		 iv_c_bg = (ImageView)v.findViewById(R.id.iv_c_bg);
		 iv_d_bg = (ImageView)v.findViewById(R.id.iv_d_bg);
		 iv_e_bg = (ImageView)v.findViewById(R.id.iv_e_bg);
		 
		 iv_a = (ImageView)v.findViewById(R.id.iv_a);
		 iv_c = (ImageView)v.findViewById(R.id.iv_c);
		 iv_d = (ImageView)v.findViewById(R.id.iv_d);
		 iv_e = (ImageView)v.findViewById(R.id.iv_e);
		 
		 ll_left = (LinearLayout)v.findViewById(R.id.ll_left);
		 ll_right = (LinearLayout)v.findViewById(R.id.ll_right);
		 
		 if(list.size()>0){
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
			 if(list.get(0).isLight.equals("0")){//isLight  0 未点亮  1 点亮
//				 iv_a_bg.setBackgroundResource(R.drawable.sixmsg);
				 iv_a_bg.setBackgroundResource(R.drawable.sixmsg1);
				 ImageLoader.getInstance().displayImage(list.get(0).titleMultiUrl, iv_a,App.getInstance().getListViewDisplayImageOptions());
			 }else  if(list.get(0).isLight.equals("1")){
				 iv_a_bg.setBackgroundResource(R.drawable.sixmsg1);
				 ImageLoader.getInstance().displayImage(list.get(0).titleMultiUrl, iv_a,App.getInstance().getListViewDisplayImageOptions());
//				 iv_a.setOnClickListener(onclickView(list.get(0)));
			 }else{
				 iv_a_bg.setBackgroundResource(R.drawable.sixmsg);
			 }
			 iv_a.setOnClickListener(onclickView(list.get(0)));
		 }else{
			 iv_a_bg.setVisibility(View.GONE); 
			 iv_a.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>1){
			
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
			 if(list.get(1).isLight.equals("0")){//isLight  0 未点亮  1 点亮
				 iv_c.setImageResource(0);
				 ImageLoader.getInstance().displayImage(list.get(1).titleMultiUrl, iv_c,App.getInstance().getListViewDisplayImageOptions());
//				 iv_c_bg.setBackgroundResource(R.drawable.sixmsg);
				 iv_c_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else if(list.get(1).isLight.equals("1")){
				 ImageLoader.getInstance().displayImage(list.get(1).titleMultiUrl, iv_c,App.getInstance().getListViewDisplayImageOptions());
				 iv_c.setImageResource(list.get(1).id);
//				 iv_c.setOnClickListener(onclickView(list.get(1)));
				 iv_c_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else{
				 iv_c_bg.setBackgroundResource(R.drawable.sixmsg);
			 }
			 iv_c.setOnClickListener(onclickView(list.get(1)));
		 }else{
			 iv_c_bg.setVisibility(View.GONE);
			 iv_c.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>2){
			
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
			 if(list.get(2).isLight.equals("0")){//isLight  0 未点亮  1 点亮
				 ImageLoader.getInstance().displayImage(list.get(2).titleMultiUrl, iv_d,App.getInstance().getListViewDisplayImageOptions());
//				 iv_d_bg.setBackgroundResource(R.drawable.sixmsg);
				 iv_d_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else if(list.get(2).isLight.equals("1")){
				 ImageLoader.getInstance().displayImage(list.get(2).titleMultiUrl, iv_d,App.getInstance().getListViewDisplayImageOptions());
//				 iv_d.setOnClickListener(onclickView(list.get(2)));
				 iv_d_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else{
				 iv_d_bg.setBackgroundResource(R.drawable.sixmsg);
			 }
			 iv_d.setOnClickListener(onclickView(list.get(2)));
		 }else{
			 iv_d_bg.setVisibility(View.GONE);
			 iv_d.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>3){
			 ll_left.setVisibility(View.VISIBLE);
			 ll_right.setVisibility(View.VISIBLE);
			 if(list.get(3).isLight.equals("0")){//isLight  0 未点亮  1 点亮
				 ImageLoader.getInstance().displayImage(list.get(3).titleMultiUrl, iv_e,App.getInstance().getListViewDisplayImageOptions());
				 iv_e_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else  if(list.get(3).isLight.equals("1")){
				 ImageLoader.getInstance().displayImage(list.get(3).titleMultiUrl, iv_e,App.getInstance().getListViewDisplayImageOptions());
				 iv_e_bg.setBackgroundResource(R.drawable.sixmsg1);
			 }else {
				 iv_e_bg.setBackgroundResource(R.drawable.sixmsg);
			 }
			 
			 
			 iv_e.setOnClickListener(onclickView(list.get(3)));
		 }else{
			 iv_e_bg.setVisibility(View.GONE);
			 iv_e.setVisibility(View.GONE);
		 }
	    
//		 if(list.size()>4){
//			 if(TextUtils.isEmpty(list.get(4).imageUrl)){
//				 iv_e.setImageResource(0);
//			 }else{
//				 ImageLoader.getInstance().displayImage(list.get(4).imageUrl, iv_e,App.getInstance().getListViewDisplayImageOptions());
//				 iv_e.setOnClickListener(onclickView(list.get(4)));
//			 }
//		 }else{
//			 iv_e_bg.setVisibility(View.GONE); 
//			 iv_e.setVisibility(View.GONE);
//		 }
 	     
		
	}
	
	
	private static OnClickListener onclickView(final Badge badge){
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = PullulateFm.CLICK_STATES;
				int strMedalId =Integer.valueOf(badge.medalId);
//				msg.arg1 = badge.id;
				msg.obj=badge;
				handler.sendMessage(msg);
//				App.toast("哈哈"+badge.badgeName);
				
			}
		};
	}
	
	
}
