package com.bm.tzj.fm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.bm.base.BaseFragmentActivity;
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
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 消息中心
 */
public class XiaoxiFm extends Fragment implements View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {

    private View messageLayout = null;
    private City city = null;
    private User user = null;
    //没有数据的时候显示的空view
    private View emptyLayout;
    private ListView listView;
    private View radCountCoachingView;
    private View radCountNotifyView;
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    private ArrayList<XiaoxiList.MessageRecoBean> xiaoxiLists = new ArrayList<>();

    private SwipyRefreshLayout refreshLayout;
    private CommonResult<XiaoxiList> obj = null;

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
        refreshLayout = (SwipyRefreshLayout) messageLayout.findViewById(R.id.refreshLayout);
        refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        refreshLayout.setColorScheme(R.color.color1, R.color.color2,
                R.color.color3, R.color.color4);
        refreshLayout.setOnRefreshListener(this);
        radCountCoachingView = messageLayout.findViewById(R.id.radCountCoachingView);
        radCountNotifyView = messageLayout.findViewById(R.id.radCountNotifyView);
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
        ((BaseFragmentActivity) getActivity()).showProgressDialog();
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
                hideProgressDialog();
                handDataShow(obj);
                CacheUtil.save(getActivity(), CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
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
            refreshRedPoint(obj);
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
                startActivity(new Intent(getActivity(), CoachingAct.class)
                        .putExtra("data", obj));
                break;
            //通知消息
            case R.id.notifyItem:
                startActivity(new Intent(getActivity(), NotifyAct.class)
                        .putExtra("data", obj));
                break;
        }
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            xiaoxiLists.clear();
                            getMessList();
                            // 刷新设置
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }, 2000);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Hide the refresh after 2sec
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getMessList();
                            // 刷新设置
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }, 2000);
        }
    }


    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private ImageView img_tu;

        public ItemTag(View iteView) {
            super(iteView);
            img_tu = (ImageView) iteView.findViewById(R.id.img_tu);
        }
    }

    //刷新红点
    private void refreshRedPoint(CommonResult<XiaoxiList> obj) {
        this.obj = obj;
        if (obj == null || obj.data == null) {
            radCountCoachingView.setVisibility(View.GONE);
            radCountNotifyView.setVisibility(View.GONE);
            return;
        } else {
            if (obj.data.getAppraise() == null || obj.data.getAppraise().size() < 1) {
                radCountCoachingView.setVisibility(View.GONE);
            } else {
                boolean hasNotRead = false;
                for (XiaoxiList.AppraiseBean appraiseBean : obj.data.getAppraise()) {
                    if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                        hasNotRead = true;
                    }
                }
                radCountCoachingView.setVisibility(hasNotRead ? View.VISIBLE : View.GONE);
            }
            if (obj.data.getMessageAll() == null || obj.data.getMessageAll().size() < 1) {
                radCountNotifyView.setVisibility(View.GONE);
            } else {
                boolean hasNotRead = false;
                for (XiaoxiList.MessageAllBean appraiseBean : obj.data.getMessageAll()) {
                    if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                        hasNotRead = true;
                    }
                }
                radCountNotifyView.setVisibility(hasNotRead ? View.VISIBLE : View.GONE);
            }
        }
    }

    public void hideProgressDialog() {
        ((BaseFragmentActivity) getActivity()).hideProgressDialog();
    }

}
