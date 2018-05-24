package com.lib.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
 
public class FuListView extends ListView { 
	
    public FuListView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 
 
    public FuListView(Context context) { 
        super(context); 
    } 
 
    public FuListView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
 
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        
    	int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
                MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
 
} 
