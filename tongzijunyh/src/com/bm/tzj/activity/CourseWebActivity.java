package com.bm.tzj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.base.BaseActivity;
import com.bm.share.ShareModel;
import com.bm.util.GlobalPrams;
import com.richer.tzj.R;

public class CourseWebActivity extends BaseActivity implements OnClickListener {
    public final static String Titele = "titele";
    public final static String WebUrl = "WebUrl";

    private String url;
    private WebView web_view;
    private String title;

//    1、周末：http://59.110.62.10:8888/tongZiJun/app/specialColumn.html?specialColumnid=专栏id&share=1&urlType=0
//            （share  1，分享 0
//    不分享 urlType 0代表APP内打开，1代表分享页 ）
//            2、暑期：http://59.110.62.10:8888/tongZiJun/app/outdoors_details.html?goodsId=3319&urlType=0&shares=1
//            （参数，商品id，share  1，分享 0不分享urlType 0代表APP内打开，1代表分享页 ）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_web);
        Intent intent = getIntent();
        url = intent.getStringExtra(WebUrl);
        Log.e("WebUrl", url);
        title = intent.getStringExtra(Titele);
        setTitleName(title);
        initView();
    }

    private void initView() {
        showProgressDialog();
        tv_right_share_left.setVisibility(View.VISIBLE);
        tv_right_share_left.setOnClickListener(this);

        web_view = (WebView) findViewById(R.id.web_view);
        web_view.setWebViewClient(new MyWebViewClient());
        //设置
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setBlockNetworkImage(false);


        web_view.loadUrl(url);
    }

    class MyWebViewClient extends WebViewClient {


        @SuppressLint("NewApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//			title = view.getTitle();
//			setTitleName(view.getTitle());
            hideProgressDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_share_left:
                String shardUrl = url.replace("share=1", "share=0");
                //shardUrl = shardUrl.replace("urlType=0","share=1");
                ShareModel model = new ShareModel();
                model.conent = url;
                model.title = title;
                model.targetUrl = shardUrl;
                share.shareInfo(model, 0);
                break;

            default:
                break;
        }
    }
}
