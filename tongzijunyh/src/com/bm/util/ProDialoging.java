package com.bm.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.lib.widget.refush.CircleProgressBar;
import com.richer.tzj.R;


public class ProDialoging extends Dialog {  
    Context context;  
  
  
//    private ProgressBarCircularIndeterminate pg_groress;
    private CircleProgressBar pg_groress;
    public ProDialoging(Context context) {  
        super(context, R.style.DialogLoading);  
        this.context = context;  
    }  
  
    public void onCreate(Bundle savedInstanceState) {  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        requestWindowFeature(Window.PROGRESS_VISIBILITY_ON);  
        super.onCreate(savedInstanceState);  
  
       View v =  LayoutInflater.from(context).inflate(R.layout.pro_dialog, null);
        
        pg_groress  = (CircleProgressBar)v.findViewById(R.id.progressBar);
        pg_groress.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
  
        // 布局  
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(  
                FrameLayout.LayoutParams.MATCH_PARENT,  
                FrameLayout.LayoutParams.WRAP_CONTENT);  
        lp.gravity = Gravity.CENTER;  
  
        addContentView(v, lp);  
  
        setCanceledOnTouchOutside(false);  
    }  
  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            // 按下了键盘上返回按钮  
            this.hide();  
            return true;  
        }  
        return false;  
    }  
  
}  