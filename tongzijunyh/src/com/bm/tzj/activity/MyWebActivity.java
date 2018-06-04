package com.bm.tzj.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.webview_layout);
        setTitleName("收入明细");
        LinearLayout web_ll = findLinearLayoutById(R.id.web_ll);
        url = getIntent().getStringExtra("Url");
        Log.e("WebView","url = "+url);
        AgentWeb agentWeb =   AgentWeb.with(this)//传入Activity
                .setAgentWebParent(web_ll, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
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
}
