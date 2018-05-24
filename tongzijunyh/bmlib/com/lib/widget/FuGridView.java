package com.lib.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class FuGridView extends GridView {     
    public FuGridView(Context context, AttributeSet attrs) {     
        super(context, attrs);     
    }     
    
    public FuGridView(Context context) {     
        super(context);     
    }     
    
    public FuGridView(Context context, AttributeSet attrs, int defStyle) {     
        super(context, attrs, defStyle);     
    }     
    
    @Override     
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {     
    
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);     
        super.onMeasure(widthMeasureSpec, expandSpec);     
    }     

}
