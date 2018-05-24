package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bm.tzj.mine.AddChildAc;
import com.lib.log.Lg;
import com.richer.tzj.R;

/**
 * 开启消息通知提示框
 */
public class OpenNotifyDialog extends Dialog implements View.OnClickListener{
    private TextView bt_ok;
    private Activity context;


    public OpenNotifyDialog(Activity context) {
        super(context, R.style.HongbaoDialog);
        this.getWindow().setWindowAnimations(R.style.HongbaoDialog);
        this.context = context;
        Lg.d("显示：1");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_open_notify);
        Lg.d("显示：2");
        bt_ok = (TextView)this.findViewById(R.id.bt_ok);

        this.findViewById(R.id.iv_close).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return false;
            }
        });

        bt_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bt_ok:
                openNotify();
                dismiss();
                break;
        }
    }

    public void openNotify()
    {
        Intent intent = new Intent();
        //android 8.0引导
        if(Build.VERSION.SDK_INT >=26){
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
        }
        //android 5.0-7.0
        if(Build.VERSION.SDK_INT >=21 && Build.VERSION.SDK_INT <26) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
        }
        //其他
        if(Build.VERSION.SDK_INT <21){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }

        context.startActivity(intent);
    }

}
