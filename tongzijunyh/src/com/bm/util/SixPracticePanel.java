package com.bm.util;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
 * 教练顾问的  六边形   图片显示
 * @author wanghy
 *
 */
public class SixPracticePanel {

	
	
	static Handler handler;
	public static  void  setViews(FrameLayout fm_view, List<Badge> list,Context context,Handler strHandler){
		handler = strHandler;
		 List<List<Badge>> mlist = Util.subList(list,5);
		for(int i = 0;i<mlist.size();i++){
			View vm1 = LayoutInflater.from(context).inflate(R.layout.fm_view_six_item_practice, null);
			fm_view.addView(vm1);
			setViewId(vm1,mlist.get(i));
			int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			vm1.measure(width,height);
			int heights=vm1.getMeasuredHeight()-80; 
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vm1.getLayoutParams();
			lp.setMargins(0, heights*i, 0, 0);
		}
		
	}
	
	
	private static void setViewId(View v,List<Badge> list){

		ImageView iv_a,iv_b,iv_c,iv_d,iv_e;
		 
		 LinearLayout ll_left,ll_right,ll_one,ll_two,ll_three,ll_four,ll_five,ll_one_default,ll_two_default,ll_three_default,ll_four_default,ll_five_default;
		 FrameLayout fl_five,fl_four,fl_three,fl_two,fl_one;
		 
		 iv_a = (ImageView)v.findViewById(R.id.iv_a);
		 iv_b = (ImageView)v.findViewById(R.id.iv_b);
		 iv_c = (ImageView)v.findViewById(R.id.iv_c);
		 iv_d = (ImageView)v.findViewById(R.id.iv_d);
		 iv_e = (ImageView)v.findViewById(R.id.iv_e);
		 
		 ll_left = (LinearLayout)v.findViewById(R.id.ll_left);
		 ll_right = (LinearLayout)v.findViewById(R.id.ll_right);
		 fl_one = (FrameLayout)v.findViewById(R.id.fl_one);
		 fl_two = (FrameLayout)v.findViewById(R.id.fl_two);
		 fl_three = (FrameLayout)v.findViewById(R.id.fl_three);
		 fl_four = (FrameLayout)v.findViewById(R.id.fl_four);
		 fl_five = (FrameLayout)v.findViewById(R.id.fl_five);
		 
		 ll_one_default = (LinearLayout)v.findViewById(R.id.ll_one_default);
		 ll_two_default = (LinearLayout)v.findViewById(R.id.ll_two_default);
		 ll_three_default = (LinearLayout)v.findViewById(R.id.ll_three_default);
		 ll_four_default = (LinearLayout)v.findViewById(R.id.ll_four_default);
		 ll_five_default = (LinearLayout)v.findViewById(R.id.ll_five_default);
		 
		 ll_one = (LinearLayout)v.findViewById(R.id.ll_one);
		 ll_two = (LinearLayout)v.findViewById(R.id.ll_two);
		 ll_three = (LinearLayout)v.findViewById(R.id.ll_three);
		 ll_four = (LinearLayout)v.findViewById(R.id.ll_four);
		 ll_five = (LinearLayout)v.findViewById(R.id.ll_five);
		 
		 
		 if(list.size()>0){
			 if(TextUtils.isEmpty(list.get(0).avatar)){
				 ll_one_default.setVisibility(View.VISIBLE);
				 ll_one.setVisibility(View.GONE);
				 fl_one.setBackgroundResource(R.drawable.six_xing);
				 ll_left.setVisibility(View.GONE);
				 ll_right.setVisibility(View.GONE);
			 }else{
				 ImageLoader.getInstance().displayImage(list.get(0).avatar, iv_a,App.getInstance().getListViewDisplayImageOptions());
				 iv_a.setOnClickListener(onclickView(list.get(0)));
			 }
		 }else{
			 iv_a.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>1){
			 if(TextUtils.isEmpty(list.get(1).avatar)){
				 ll_two_default.setVisibility(View.VISIBLE);
				 ll_two.setVisibility(View.GONE);
				 fl_two.setBackgroundResource(R.drawable.six_xing);
				 ll_left.setVisibility(View.GONE);
				 ll_right.setVisibility(View.GONE);
			 }else{
				 ImageLoader.getInstance().displayImage(list.get(1).avatar, iv_b,App.getInstance().getListViewDisplayImageOptions());
			     iv_b.setOnClickListener(onclickView(list.get(1)));
			 }
		 }else{
			 iv_b.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>2){
			 if(TextUtils.isEmpty(list.get(2).avatar)){
				 ll_three_default.setVisibility(View.VISIBLE);
				 ll_three.setVisibility(View.GONE);
				 fl_three.setBackgroundResource(R.drawable.six_xing);
				 ll_left.setVisibility(View.GONE);
				 ll_right.setVisibility(View.GONE);
			 }else{
				 ImageLoader.getInstance().displayImage(list.get(2).avatar, iv_c,App.getInstance().getListViewDisplayImageOptions());
		  	     iv_c.setOnClickListener(onclickView(list.get(2)));
			 }
		 }else{
			 iv_c.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>3){
			 if(TextUtils.isEmpty(list.get(3).avatar)){
				 ll_four_default.setVisibility(View.VISIBLE);
				 ll_four.setVisibility(View.GONE);
				 fl_four.setBackgroundResource(R.drawable.six_xing);
				 ll_left.setVisibility(View.GONE);
				 ll_right.setVisibility(View.GONE);
			 }else{
//				 ll_left.setVisibility(View.VISIBLE);
//				 ll_right.setVisibility(View.VISIBLE);
				 ImageLoader.getInstance().displayImage(list.get(3).avatar, iv_d,App.getInstance().getListViewDisplayImageOptions());
				  iv_d.setOnClickListener(onclickView(list.get(3)));
			 }
		 }else{
			 iv_d.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	    
		 if(list.size()>4){
			 if(TextUtils.isEmpty(list.get(4).avatar)){
				 ll_five_default.setVisibility(View.VISIBLE);
				 ll_five.setVisibility(View.GONE);
				 fl_five.setBackgroundResource(R.drawable.six_xing);
			 }else{
				 ImageLoader.getInstance().displayImage(list.get(4).avatar, iv_e,App.getInstance().getListViewDisplayImageOptions());
			     iv_e.setOnClickListener(onclickView(list.get(4)));
			 }
		 }else{
			 iv_e.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	}
	
	private static OnClickListener onclickView(final Badge badge){
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = PullulateFm.CLICK_STATES;
				int strCoachId = Integer.valueOf(badge.coachId);
				msg.arg1 = strCoachId;
				handler.sendMessage(msg);
			}
		};
	}
	
	
}
