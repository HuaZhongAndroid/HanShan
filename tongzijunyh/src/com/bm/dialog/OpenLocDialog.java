package com.bm.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
public class OpenLocDialog extends Dialog implements View.OnClickListener {
    private TextView bt_ok;
    private View iv_close;
    private Activity context;


    public OpenLocDialog(Activity context) {
        super(context, R.style.HongbaoDialog);
        this.getWindow().setWindowAnimations(R.style.HongbaoDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_open_loc);
        bt_ok = (TextView) this.findViewById(R.id.bt_ok);
        iv_close =  this.findViewById(R.id.iv_close);
//
//        this.findViewById(R.id.iv_close).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                cancel();
//                return false;
//            }
//        });

        bt_ok.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ok:
                getAppDetailSettingIntent();
                dismiss();
                break;
            case R.id.iv_close:
                context.finish();
                dismiss();
                break;
        }
    }

    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }
}
