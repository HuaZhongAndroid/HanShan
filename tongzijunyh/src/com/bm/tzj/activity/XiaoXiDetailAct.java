package com.bm.tzj.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.bm.api.MessageManager;
import com.bm.base.BaseActivity;
import com.bm.entity.XiaoXiDetail;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 消息详情界面
 */
public class XiaoXiDetailAct extends BaseActivity {


    private String titleStr;
    private TextView detailTv;
    private String messageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_xiaoxi_detail);

        titleStr = getIntent().getStringExtra("titleStr");
        messageId = getIntent().getStringExtra("messageId");
        setTitleName(titleStr);
        detailTv = (TextView) findViewById(R.id.detailTv);
        getMessageDetail();
    }


    //请求数据
    private void getMessageDetail() {
        final HashMap<String, String> map = new HashMap<String, String>();

        map.put("messageId", messageId);
        MessageManager.getInstance().getMessageDetailById(this, map, new ServiceCallback<CommonResult<XiaoXiDetail>>() {

            final String CACHEKEY = "xiaoxiAc_detail_message_detail_cache" + messageId;

            @Override
            public void done(int what, CommonResult<XiaoXiDetail> obj) {
                handDataShow(obj);
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonResult<XiaoXiDetail> obj = new CommonResult<XiaoXiDetail>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonResult<XiaoXiDetail>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    handDataShow(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }
        });
    }

    private void handDataShow(CommonResult<XiaoXiDetail> obj) {
        if (TextUtils.isEmpty(obj.data.getContent())){
            detailTv.setText(Html.fromHtml(obj.data.getThinContent()));
        }else {
            detailTv.setText(Html.fromHtml(obj.data.getContent()));
        }
    }


}
