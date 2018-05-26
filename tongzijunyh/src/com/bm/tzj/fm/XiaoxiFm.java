package com.bm.tzj.fm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.MessageManager;
import com.bm.app.App;
import com.bm.base.adapter.XiaoxiListAdapter;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.tzj.activity.CoachingAct;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.activity.NotifyAct;
import com.bm.tzj.city.City;
import com.bm.util.CacheUtil;
import com.bumptech.glide.Glide;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 消息中心
 */
public class XiaoxiFm extends Fragment implements View.OnClickListener {

    private View messageLayout = null;
    private City city = null;
    private User user = null;

    //没有数据的时候显示的空view
    private View emptyLayout;
    private ListView listView;
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    private ArrayList<XiaoxiList.MessageRecoBean> xiaoxiLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fm_xiaoxi, container,
                false);
        init();
        refreshData();
        return messageLayout;
    }

    private void init() {

        emptyLayout = messageLayout.findViewById(R.id.emptyLayout);
        listView = (ListView) messageLayout.findViewById(R.id.recyclerView);

        messageLayout.findViewById(R.id.coachingItem).setOnClickListener(this);
        messageLayout.findViewById(R.id.notifyItem).setOnClickListener(this);

        city = App.getInstance().getCityCode();
        user = App.getInstance().getUser();

        xiaoxiListXiaoxiListAdapter = new XiaoxiListAdapter<ItemTag, XiaoxiList.MessageRecoBean>() {
            @Override
            protected ItemTag instanceTAG(View view) {
                return new ItemTag(view);
            }

            @Override
            protected void handViewAndData(ItemTag tag, XiaoxiList.MessageRecoBean data) {
                Glide.with(getActivity())
                        .load(data.getTitleMultiUrl())
                        .placeholder(R.drawable.adv_default)
                        .error(R.drawable.adv_default)
                        .centerCrop()
                        .dontAnimate()
                        .into(tag.img_tu);
            }

            @Override
            protected int loadLayout() {
                return R.layout.item_list_message_list;
            }
        };
        xiaoxiListXiaoxiListAdapter.setActivity(getActivity(), xiaoxiLists);
        listView.setAdapter(xiaoxiListXiaoxiListAdapter);
    }

    private void refreshData() {
        getMessList();
    }

    private void getMessList() {
        final HashMap<String, String> map = new HashMap<String, String>();
        if (null != city && !TextUtils.isEmpty(city.regionName)) {
            map.put("regionName", city.regionName);//城市名称
            map.put("userId", user.userid);//用户id
        } else {
            map.put("regionName", "西安市");//城市名称
        }
        MessageManager.getInstance().getMessageList(getActivity(), map, new ServiceCallback<CommonResult<XiaoxiList>>() {

            final String CACHEKEY = "xiaoxiFm_message_list_cache";

            @Override
            public void done(int what, CommonResult<XiaoxiList> obj) {
                handDataShow(obj);
                CacheUtil.save(getActivity(), CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonResult<XiaoxiList> obj = new CommonResult<XiaoxiList>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonResult<XiaoxiList>) CacheUtil.read(getActivity(), CACHEKEY, map, type);
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
            xiaoxiLists.addAll(obj.data.getMessageReco());
            xiaoxiListXiaoxiListAdapter.notifyDataSetChanged();
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //教练点评
            case R.id.coachingItem:
                startActivity(new Intent(getActivity(), CoachingAct.class));
                break;
            //通知消息
            case R.id.notifyItem:
                startActivity(new Intent(getActivity(), NotifyAct.class));
                break;
        }
    }


    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private ImageView img_tu;

        public ItemTag(View iteView) {
            super(iteView);
            img_tu = (ImageView) iteView.findViewById(R.id.img_tu);
        }
    }


}
