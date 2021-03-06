package com.bm.tzj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.CoachInfo;
import com.bm.entity.User;
import com.just.agentweb.AgentWeb;
import com.richer.tzj.R;

/**
 * Create by zwonb on 2018/5/30
 */
public class MyWebActivity extends BaseActivity {

    String url = "";
    String userId = "";
    String Title ="标题";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.webview_layout);
        LinearLayout web_ll = findLinearLayoutById(R.id.web_ll);
        url = getIntent().getStringExtra("Url");
        Title = getIntent().getStringExtra("Title");
        setTitleName(Title);
        Log.e("WebView","url = "+url);
        AgentWeb agentWeb =   AgentWeb.with(this)//传入Activity
                .setAgentWebParent(web_ll, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()//
                .ready()
                .go(url);
        //调用 Javascript
//        JsAccessEntrace js = agentWeb.getJsAccessEntrace();//.quickCallJs("callByAndroid");
//        js.quickCallJs("js方法名");
        //Javascript 调 android 官网说的不是很详细 待研究
//        agentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(agentWeb,this));
//        window.android.callAndroid();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.e("MyWebActivity", "onProgressChanged: " );
            //do you work
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
           if (Title==null){
               Title= title;
               setTitleName(title);
           }
        }
    };
}
