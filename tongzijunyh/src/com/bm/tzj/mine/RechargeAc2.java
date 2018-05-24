package com.bm.tzj.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baidu.location.BDLocation;
import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.OpenLocDialog;
import com.bm.entity.RechargeConfig;
import com.bm.util.BDLocationHelper;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;

import java.util.HashMap;

/**
 * 充值页面web实现
 */
public class RechargeAc2 extends BaseActivity {

    private WebView webContent;

    public static RechargeAc2 intance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_recharge2);
        context = this;
        intance = this;

        webContent = (WebView)findViewById(R.id.webContent);
        webContent.setWebViewClient(new MyWebViewClient());
        // 启用javascript
        webContent.getSettings().setJavaScriptEnabled(true);
        webContent.loadUrl(BaseApi.API_HOST + "/tongZiJun/app/recharge.html");
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkLoc();
    }

    //检测定位
    private void checkLoc()
    {
        BDLocationHelper.locate(context, new BDLocationHelper.MyLocationListener() {
            @Override
            public void success(BDLocation location) {
                BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();

                if(info == null || info.lat < 1 || info.lng < 1)
                {
                    OpenLocDialog dialog = new OpenLocDialog(context);
                    dialog.show();
                    dialog.setCancelable(false);
                }

            }
        });
    }

    class MyWebViewClient extends WebViewClient {
        @SuppressLint("NewApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setTitleName(view.getTitle());
            hideProgressDialog();
        }
    }
}
