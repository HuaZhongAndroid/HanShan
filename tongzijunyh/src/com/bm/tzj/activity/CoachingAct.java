package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.MessageManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.XiaoxiListAdapter;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.tzj.city.City;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 教练点评界面
 */
public class CoachingAct extends BaseActivity {
    private City city = null;
    private User user = null;

    //没有数据的时候显示的空view
    private View emptyLayout;
    private ListView listView;
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    private ArrayList<XiaoxiList.AppraiseBean> xiaoxiLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_coaching_list);
        setTitleName("教练点评");
        init();
        getMessList();
    }


    private void init() {

        emptyLayout = findViewById(R.id.emptyLayout);
        listView = (ListView) findViewById(R.id.recyclerView);

        city = App.getInstance().getCityCode();
        user = App.getInstance().getUser();

        xiaoxiListXiaoxiListAdapter = new XiaoxiListAdapter<ItemTag, XiaoxiList.AppraiseBean>() {
            @Override
            protected ItemTag instanceTAG(View view) {
                return new ItemTag(view);
            }

            @Override
            protected void handViewAndData(ItemTag tag, final XiaoxiList.AppraiseBean data) {
                tag.titleTv.setText(data.getTitle());
                tag.contentTv.setText(data.getThinContent());
                tag.dateTv.setText(data.getCtime());
                tag.isReadView.setVisibility(data.getIsRead().equals("1") ? View.INVISIBLE : View.VISIBLE);

                tag.iteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpDetail(data.getMessageId(), data.getTitle());
                    }
                });

            }

            @Override
            protected int loadLayout() {
                return R.layout.item_list_coaching_list;
            }
        };
        xiaoxiListXiaoxiListAdapter.setActivity(this, xiaoxiLists);
        listView.setAdapter(xiaoxiListXiaoxiListAdapter);
    }


    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private TextView titleTv;
        private TextView contentTv;
        private TextView dateTv;
        private View isReadView;

        public ItemTag(View iteView) {
            super(iteView);
            titleTv = (TextView) iteView.findViewById(R.id.titleTv);
            contentTv = (TextView) iteView.findViewById(R.id.contentTv);
            dateTv = (TextView) iteView.findViewById(R.id.dateTv);
            isReadView = iteView.findViewById(R.id.isReadView);
        }
    }

    //请求数据
    private void getMessList() {
        final HashMap<String, String> map = new HashMap<String, String>();
        if (null != city && !TextUtils.isEmpty(city.regionName)) {
            map.put("regionName", city.regionName);//城市名称
            map.put("userId", user.userid);//用户id
        } else {
            map.put("regionName", "西安市");//城市名称
        }
        MessageManager.getInstance().getMessageList(this, map, new ServiceCallback<CommonResult<XiaoxiList>>() {

            final String CACHEKEY = "xiaoxiFm_message_list_cache";

            @Override
            public void done(int what, CommonResult<XiaoxiList> obj) {
                handDataShow(obj);
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonResult<XiaoxiList> obj = new CommonResult<XiaoxiList>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonResult<XiaoxiList>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    handDataShow(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }
        });
    }

    private void handDataShow(CommonResult<XiaoxiList> obj) {
        if (obj.data.getMessageReco() != null) {
            emptyLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            xiaoxiLists.addAll(obj.data.getAppraise());
            xiaoxiListXiaoxiListAdapter.notifyDataSetChanged();
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    private void jumpDetail(String messageId, String titleStr) {
        Intent intent = new Intent(this, XiaoXiDetailAct.class);
        intent.putExtra("messageId", messageId);
        intent.putExtra("titleStr", titleStr);
        startActivity(intent);
    }

}
