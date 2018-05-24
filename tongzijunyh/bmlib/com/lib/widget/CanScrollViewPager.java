package com.lib.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CanScrollViewPager extends ViewPager{
    public CanScrollViewPager(Context context) {
        super(context);
    }
    public CanScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        //return super.canScroll(v, checkV, dx, x, y);
        if (v instanceof ScaleImageView) {
            return ((ScaleImageView) v).canScrollHor(-dx);
        } else if(v instanceof ViewPager && v!=this){
        	//Log.i("CanScrollViewPager", "内部Viewpager滑动");
        	//return ((ScaleImageView) v).canScrollHor(-dx);
        	//Point p = UITools.getPos(v);
        	//Log.i(VIEW_LOG_TAG, v.getClass().getName()+"viewpager位置:"+p.x+","+p.y+" 当前触点:"+x+","+y);
        	//return super.canScroll(v, checkV, dx, x, y);
        	return true;
        }else{
        	return super.canScroll(v, checkV, dx, x, y);
        }
    }
}
