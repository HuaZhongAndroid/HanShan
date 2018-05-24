package com.bm.tzj.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.app.App;
import com.bm.entity.Badge;
import com.bm.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;


/**
 * 
 * 
 * 六边形  测试类
 * @author wangqiang
 *
 */
public class TestAbsulate extends Activity {
	
	
	
	
	private FrameLayout fm_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_test_abasu);
		
		
		fm_content = (FrameLayout)findViewById(R.id.fm_content);

		
		List<Badge> list = new ArrayList<Badge>();
		for(int i = 0;i<6;i++){
			Badge  badge = new Badge();
			badge.badgeName = "badge"+i;
			badge.imageUrl  = "http://pica.nipic.com/2007-11-09/200711912453162_2.jpg";
			list.add(badge);
		}
		
	     setViews(list);
	}
	
	
	
	
	private void  setViews(List<Badge> list){
		
		 List<List<Badge>> mlist = Util.subList(list,5);
		for(int i = 0;i<mlist.size();i++){
			View vm1 = LayoutInflater.from(this).inflate(R.layout.fm_view_six_item, null);
			fm_content.addView(vm1);
			setViewId(vm1,mlist.get(i));
			int width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			int height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			vm1.measure(width,height);
			int heights=vm1.getMeasuredHeight()-70; 
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vm1.getLayoutParams();
			lp.setMargins(0, heights*i, 0, 0);
		}
		
	}
	
	
	private void setViewId(View v,List<Badge> list){
		 ImageView iv_a,iv_b,iv_c,iv_d,iv_e, iv_a_bg,iv_b_bg,iv_c_bg,iv_d_bg,iv_e_bg;
		 
		 LinearLayout ll_left,ll_right;
		 iv_a_bg = (ImageView)v.findViewById(R.id.iv_a_bg);
		 iv_b_bg = (ImageView)v.findViewById(R.id.iv_b_bg);
		 iv_c_bg = (ImageView)v.findViewById(R.id.iv_c_bg);
		 iv_d_bg = (ImageView)v.findViewById(R.id.iv_d_bg);
		 iv_e_bg = (ImageView)v.findViewById(R.id.iv_e_bg);
		 
		 iv_a = (ImageView)v.findViewById(R.id.iv_a);
		 iv_b = (ImageView)v.findViewById(R.id.iv_b);
		 iv_c = (ImageView)v.findViewById(R.id.iv_c);
		 iv_d = (ImageView)v.findViewById(R.id.iv_d);
		 iv_e = (ImageView)v.findViewById(R.id.iv_e);
		 
		 ll_left = (LinearLayout)v.findViewById(R.id.ll_left);
		 ll_right = (LinearLayout)v.findViewById(R.id.ll_right);
		 
		 
		 if(list.size()>0){
			 ImageLoader.getInstance().displayImage(list.get(0).imageUrl, iv_a,App.getInstance().getListViewDisplayImageOptions());
		     iv_a.setOnClickListener(onclickView(list.get(0)));
		 }else{
			 iv_a_bg.setVisibility(View.GONE); 
			 iv_a.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>1){
			 ImageLoader.getInstance().displayImage(list.get(0).imageUrl, iv_b,App.getInstance().getListViewDisplayImageOptions());
		     iv_b.setOnClickListener(onclickView(list.get(1)));
		 }else{
			 iv_b_bg.setVisibility(View.GONE); 
			 iv_b.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>2){
			 ImageLoader.getInstance().displayImage(list.get(0).imageUrl, iv_c,App.getInstance().getListViewDisplayImageOptions());
	  	     iv_c.setOnClickListener(onclickView(list.get(2)));
		 }else{
			 iv_c_bg.setVisibility(View.GONE);
			 iv_c.setVisibility(View.GONE);
			 ll_left.setVisibility(View.INVISIBLE);
			 ll_right.setVisibility(View.INVISIBLE);
		 }
	     
		 if(list.size()>3){
			 ll_left.setVisibility(View.VISIBLE);
			 ll_right.setVisibility(View.VISIBLE);
			 ImageLoader.getInstance().displayImage(list.get(0).imageUrl, iv_d,App.getInstance().getListViewDisplayImageOptions());
			  iv_d.setOnClickListener(onclickView(list.get(3)));
		 }else{
			 iv_d_bg.setVisibility(View.GONE);
			 iv_d.setVisibility(View.GONE);
		 }
	    
		 if(list.size()>4){
			 ImageLoader.getInstance().displayImage(list.get(0).imageUrl, iv_e,App.getInstance().getListViewDisplayImageOptions());
		     iv_e.setOnClickListener(onclickView(list.get(4)));
		 }else{
			 iv_e_bg.setVisibility(View.GONE); 
			 iv_e.setVisibility(View.GONE);
		 }
  	     
		
	}
	
	
	private OnClickListener onclickView(final Badge badge){
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				App.toast("哈哈"+badge.badgeName);
			}
		};
	}
	

	

}
