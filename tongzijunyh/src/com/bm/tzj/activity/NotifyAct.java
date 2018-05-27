package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.XiaoxiListAdapter;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.tzj.city.City;
import com.bumptech.glide.Glide;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.util.ArrayList;

/**
 * 通知消息界面
 */
public class NotifyAct extends BaseActivity {

    private City city = null;
    private User user = null;

    //没有数据的时候显示的空view
    private View emptyLayout;
    private ListView listView;
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    private ArrayList<XiaoxiList.MessageAllBean> xiaoxiLists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_notify_xiaoxi_list);
        setTitleName("通知消息");
        init();
        getMessList();
    }


    private void init() {

        emptyLayout = findViewById(R.id.emptyLayout);
        listView = (ListView) findViewById(R.id.recyclerView);

        city = App.getInstance().getCityCode();
        user = App.getInstance().getUser();

        xiaoxiListXiaoxiListAdapter = new XiaoxiListAdapter<ItemTag, XiaoxiList.MessageAllBean>() {
            @Override
            protected ItemTag instanceTAG(View view) {
                return new ItemTag(view);
            }

            @Override
            protected void handViewAndData(ItemTag tag, final XiaoxiList.MessageAllBean data) {
                tag.titleTv.setText(data.getTitle());
                Glide.with(context)
                        .load(data.getTitleMultiUrl())
                        .placeholder(R.drawable.adv_default)
                        .error(R.drawable.adv_default)
                        .centerCrop()
                        .dontAnimate()
                        .into(tag.imageView);
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
                return R.layout.item_list_notify_list;
            }
        };
        xiaoxiListXiaoxiListAdapter.setActivity(this, xiaoxiLists);
        listView.setAdapter(xiaoxiListXiaoxiListAdapter);
    }


    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private TextView titleTv;
        private ImageView imageView;
        private TextView dateTv;
        private View isReadView;

        public ItemTag(View iteView) {
            super(iteView);
            titleTv = (TextView) iteView.findViewById(R.id.titleTv);
            imageView = (ImageView) iteView.findViewById(R.id.imageView);
            dateTv = (TextView) iteView.findViewById(R.id.dateTv);
            isReadView = iteView.findViewById(R.id.isReadView);
        }
    }

    //请求数据
    private void getMessList() {
//        final HashMap<String, String> map = new HashMap<String, String>();
//        if (null != city && !TextUtils.isEmpty(city.regionName)) {
//            map.put("regionName", city.regionName);//城市名称
//            map.put("userId", user.userid);//用户id
//        } else {
//            map.put("regionName", "西安市");//城市名称
//        }
//        MessageManager.getInstance().getMessageList(this, map, new ServiceCallback<CommonResult<XiaoxiList>>() {
//
//            final String CACHEKEY = "xiaoxiFm_message_list_cache";
//
//            @Override
//            public void done(int what, CommonResult<XiaoxiList> obj) {
//                handDataShow(obj);
//                CacheUtil.save(context, CACHEKEY, map, obj);
//            }
//
//            @Override
//            public void error(String msg) {
//                //从缓存中读取数据
//                CommonResult<XiaoxiList> obj = new CommonResult<XiaoxiList>() {
//                };
//                Type type = obj.getClass().getGenericSuperclass();
//                obj = (CommonResult<XiaoxiList>) CacheUtil.read(context, CACHEKEY, map, type);
//                if (obj != null) {
//                    handDataShow(obj);
//                    return;
//                }
//
//                //没有缓存时执行下面的逻辑
//                MainAc.intance.dialogToast(msg);
//            }
//        });

        CommonResult<XiaoxiList> obj = (CommonResult<XiaoxiList>) getIntent().getSerializableExtra("data");
        handDataShow(obj);
    }

    private void handDataShow(CommonResult<XiaoxiList> obj) {
        if (obj.data.getMessageReco() != null) {
            emptyLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            xiaoxiLists.addAll(obj.data.getMessageAll());
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