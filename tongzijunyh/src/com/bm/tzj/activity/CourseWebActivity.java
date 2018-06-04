package com.bm.tzj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.entity.Order;
import com.bm.share.ShareModel;
import com.bm.tzj.kc.PayInfoAc3;
import com.richer.tzj.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class CourseWebActivity extends AbsCoursePayBaseAc implements OnClickListener {
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

    String goodsTime;//上课时间
    String goodsName;
    String goodsType; //课程内型
    @Override
    protected void onCreateOrderSuccess(Order order) {
        Intent intent = new Intent(context, PayInfoAc3.class);
        order.realName  = xz_child.realName;
        order.goodsType = goodsType;
        order.goodsTime = goodsTime;
        order.goodsName = goodsName;
        intent.putExtra("order", order);
        startActivity(intent);
        finish();
    }

    private void initView() {
        showProgressDialog();
        ib_right.setVisibility(View.VISIBLE);
        ib_right.setImageResource(R.drawable.ic_share);
        ib_right.setOnClickListener(this);

        web_view = (WebView) findViewById(R.id.web_view);
        web_view.setWebViewClient(new MyWebViewClient());
        //设置
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setBlockNetworkImage(false);
        //去除缓存设置
        web_view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //自适应屏幕
        web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.loadUrl(url);
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

        @Override
        // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 判断url链接中是否含有某个字段，如果有就执行指定的跳转（不执行跳转url链接），如果没有就加载url链接
            Log.e("Loading", url);
            if (url.contains("tzj://enroll?")){
                if (!isLogin())return true;
                Map<String, String> map = getMap(url);
                if (map != null) {
                    goodsId = map.get("goodsId");
                    storeId = map.get("storeId");
                    goodsType = map.get("goodsType");
                    goodsTime = map.get("goodsTime");
                    goodsName = map.get("goodsName");//
                    try {
                        goodsName = URLDecoder.decode(goodsName, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    type = 10+"";
                    showPopupWindow(web_view);
                }
                return true;
            }else if (url.contains("tzj://originImg?url=")){
                String imgUrl  = url.replace("tzj://originImg?url=","");
                //查看调用Android图片
                Log.e("imgUrl " ,imgUrl);
                Intent intent = new Intent(context,
                        ImageViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", new String[]{imgUrl});
                bundle.putInt("position", 0);
                intent.putExtras(bundle);
                context.startActivity(intent);
                return true;
            }else if(url.startsWith("http://") || url.startsWith("https://")){
                return false;
            }
            return true;
        }
    }
    //tzj://originImg?url=[图片]http://192.168.1.102:8888/img//goods/base/90923e29-91fa-4d88-9c9c-3c5074c6081d.jpg
    private Map<String, String> getMap(String str) {
        if (str.contains("tzj://enroll?")) {
            Map<String, String> map = new HashMap<>();
            str = str.substring(str.indexOf("?") + 1, str.length());
            String split[] = str.split("&");
            for (int i = 0; i < split.length; i++) {
                String sp[] = split[i].split("=");
                if (sp.length != 2) continue;
                Log.e(sp[0], sp[0] + " = " + sp[1]);
                map.put(sp[0], sp[1]);
            }
            return map;
        }
        return null;
    }



}
