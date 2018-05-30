package com.bm.tzj.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.entity.Order;
import com.bm.share.ShareModel;
import com.bm.tzj.kc.PayInfoAc3;
import com.richer.tzj.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            Map<String, String> map = getMap(url);
            if (map != null) {
                goodsId = map.get("goodsId");
                storeId = map.get("storeId");
                goodsType = map.get("goodsType");
                goodsTime = map.get("goodsTime");
                goodsName = map.get("goodsName");
                type = 10+"";
                showPopupWindow(web_view);
                return true;
            }
            return false;
        }
    }
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






    /**
     * 保存图片到相册
     * @param bmp 获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public static void saveBmp2Gallery(Context ctx, Bitmap bmp, String picName) {

        String fileName = null;
        //系统相册目录
        String galleryPath= Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                +File.separator+"Camera"+File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName+ ".jpg");

            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        }finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //通知相册更新
        MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
                bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        ctx.sendBroadcast(intent);


    }


}
