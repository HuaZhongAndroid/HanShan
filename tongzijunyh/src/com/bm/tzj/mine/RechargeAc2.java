package com.bm.tzj.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.OpenLocDialog;
import com.bm.dialog.ToastDialog;
import com.bm.entity.RechargeConfig;
import com.bm.tzj.city.City;
import com.bm.util.BDLocationHelper;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 充值页面web实现
 */
public class RechargeAc2 extends BaseActivity {

    private WebView webContent;
    public static RechargeAc2 intance;
    String url = BaseApi.API_HOST + "/tongZiJun/app/recharge.html?userid=%s&latitude=%s&longitude=%s";
    String userId = "";
    String latitude = "";
    String longitude = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_recharge2);
        context = this;
        intance = this;
        setTitleName("充值中心");
        webContent = (WebView)findViewById(R.id.webContent);
        webContent.setWebViewClient(new MyWebViewClient());
        // 启用javascript
        webContent.getSettings().setJavaScriptEnabled(true);

        //先检查定位权限 如果没有开启定位权限 则去开启 如果不开启则退出此界面，开启权限后开始定位，获取经纬度，传入url
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("sdf","onRequestPermissionsResult");
                //initLocate();
            } else {
                Toast.makeText(context,"因为您拒绝了开启定位,退出充值",Toast.LENGTH_LONG).show();//Info(mActivity, "权限被拒绝，某些功能将无法使用");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
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
                }else {
                    latitude = info.lat+"";
                    longitude = info.lng+"";
                    userId = App.getInstance().getUser().userid;
                    url = String.format(url,userId,latitude,longitude);
                    Log.e("url","url = "+url);
                    webContent.loadUrl(url);
                }

            }
        });
    }

    class MyWebViewClient extends WebViewClient {
        @SuppressLint("NewApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //setTitleName(view.getTitle());
            hideProgressDialog();
        }
        @Override
        // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 判断url链接中是否含有某个字段，如果有就执行指定的跳转（不执行跳转url链接），如果没有就加载url链接
            Log.e("Loading",url);
            Map<String,String> map = getMap(url);
            if (map!=null){
                String reConId =  map.get("reConId");
                String payType =  map.get("payType");
                Log.e("reConId","reConId = "+reConId);
                Log.e("payType","payType = "+payType);
            }
            return true;
        }
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url){
//            Log.e("shouldInterceptRequest",url);
//            return null;
//        }
    }
    private Map<String,String> getMap(String str){
        if (str.contains("tzj://recharge?")) {
            Map<String, String> map = new HashMap<>();
            str = str.substring(str.indexOf("?")+1, str.length());
            String split[] = str.split("&");
            for (int i = 0; i < split.length; i++) {
                String sp[] = split[i].split("=");
                if (sp.length==2)
                map.put(sp[0], sp[1]);
            }
            return map;
        }
        return null;
    }
    private String  getValue(String str,String key){
        String value = "";
        if (!str.contains(key))return value;
        int index = str.indexOf(key);
        //截取指定字符之后的字符串
        value = str.substring(index,str.length());
        Log.e("value1",value);
        //int start = index+key.length();
        //value = value.substring(start,value.length());

        return value;
    }


}
