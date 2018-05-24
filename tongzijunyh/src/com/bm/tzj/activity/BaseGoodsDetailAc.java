package com.bm.tzj.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Course;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.util.HashMap;

/**
 * 课程详情
 */
public class BaseGoodsDetailAc extends BaseActivity {

    private WebView webview;

    private String goodsId;

    private String goodsName;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView(R.layout.ac_base_goods_detail);

        webview = (WebView)findViewById(R.id.webview);

        goodsId = this.getIntent().getStringExtra("goodsId");
        goodsName = this.getIntent().getStringExtra("goodsName");
        setTitleName(goodsName+"");
        loadData();
    }

    private void loadData()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsId",goodsId);
        this.showProgressDialog();
        UserManager.getInstance().get_tzjgoods_baseGoodsDetail(this, map, new ServiceCallback<CommonResult<Course>>() {
            @Override
            public void done(int what, CommonResult<Course> obj) {
                hideProgressDialog();
                course = obj.data;
                if(course != null)
                {
                    Util.initViewWebData(webview, course.coursePoints+"");
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }
}
