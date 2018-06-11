package com.bm.tzj.fm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.MessageManager;
import com.bm.app.App;
import com.bm.base.BaseFragmentActivity;
import com.bm.base.adapter.XiaoxiListAdapter;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.tzj.activity.CoachingAct;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.activity.NotifyAct;
import com.bm.tzj.activity.XiaoXiDetailAct;
import com.bm.tzj.city.City;
import com.bm.util.CacheUtil;
import com.bm.util.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.widget.ReboundScrollView;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.request.Resize;

/**
 * 消息中心
 */
public class XiaoxiFm extends Fragment implements View.OnClickListener
        , SwipyRefreshLayout.OnRefreshListener, ReboundScrollView.LoadChangeListener {

    private View messageLayout = null;
    //城市
    private City city = null;
    //用户
    private User user = null;
    //没有数据的时候显示的空view
    private View emptyLayout;
    //消息列表
    private com.lib.widget.FuListView listView;
    private TextView tv_jiaolian;
    private TextView tv_tongzhi;
    private View radCountCoachingView;
    private View radCountNotifyView;
    //推荐消息列表适配器
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    //推荐消息列表
    private ArrayList<XiaoxiList.MessageRecoBean> xiaoxiLists = new ArrayList<>();

    private ReboundScrollView top_layout;
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
        top_layout = (ReboundScrollView) messageLayout.findViewById(R.id.top_layout);
        top_layout.setLoadChangeListener(this);
        radCountCoachingView = messageLayout.findViewById(R.id.radCountCoachingView);
        radCountNotifyView = messageLayout.findViewById(R.id.radCountNotifyView);
        tv_jiaolian = (TextView) messageLayout.findViewById(R.id.tv_jiaolian);
        tv_tongzhi = (TextView) messageLayout.findViewById(R.id.tv_tongzhi);
        listView = (com.lib.widget.FuListView) messageLayout.findViewById(R.id.recyclerView);

        messageLayout.findViewById(R.id.coachingItem).setOnClickListener(this);
        messageLayout.findViewById(R.id.notifyItem).setOnClickListener(this);

        city = App.getInstance().getCityCode();
        user = App.getInstance().getUser();

        xiaoxiListXiaoxiListAdapter = new XiaoxiListAdapter<ItemTag, XiaoxiList.MessageRecoBean>() {
            @Override
            protected ItemTag instanceTAG(View view) {
                return new ItemTag(view);
            }

            //处理item和bean的数据绑定
            @Override
            protected void handViewAndData(final ItemTag tag, final XiaoxiList.MessageRecoBean data) {
               tag. img_tu.getOptions().setResize(new Resize(getScreenWidth(), (int) (getScreenWidth()*0.625f), Resize.Mode.EXACTLY_SAME));
                //获取图片真正的宽高
                Glide.with(getContext())
                        .load(data.getTitleMultiUrl())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                tag.img_tu.getOptions().setResize(new Resize(getScreenWidth(), getImgDisplanHeight(width, height), Resize.Mode.EXACTLY_SAME));
//                                tag.img_tu.getOptions().setResize(new Resize(width, height, Resize.Mode.EXACTLY_SAME));
                                Log.e("img", "width " + width); //200px
                                Log.e("img", "height " + height); //200px
                            }
                        });

                //获取图片显示在ImageView后的宽高
                Glide.with(getActivity())
                        .load(data.getTitleMultiUrl())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                Log.d("img", "onException " + e.toString());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap bitmap, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
//                                tag.img_tu.getOptions().setResize(new Resize(width, height, Resize.Mode.EXACTLY_SAME));
                                Log.d("img", "width2 " + width); //400px
                                Log.d("img", "height2 " + height); //400px
                                return false;
                            }
                        }).into(tag.img_tu);
//                Glide.with(getActivity())
//                        .load(data.getTitleMultiUrl())
//                        .placeholder(R.drawable.adv_default)
//                        .error(R.drawable.adv_default)
//                        .centerCrop()
//                        .dontAnimate()
//                        .into(tag.img_tu);

                tag.iteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (App.getInstance().getUser() == null) return;
                        jumpDetail(data.getMessageId(), data.getTitle());
                        data.setIsRead("1");
                    }
                });
            }

            //加载布局
            @Override
            protected int loadLayout() {
                return R.layout.item_list_message_list;
            }
        };
        xiaoxiListXiaoxiListAdapter.setActivity(getActivity(), xiaoxiLists);
        listView.setAdapter(xiaoxiListXiaoxiListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainAc.intance != null)
            getMessList();
        Log.e("xiaoxiFm", "刷新消息");
    }

    //刷新数据
    private void refreshData() {
        if (App.getInstance().getUser() != null) {
            ((BaseFragmentActivity) getActivity()).showProgressDialog();
            getMessList();
        }
    }

    //请求消息列表
    private void getMessList() {
        final HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) return;
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
                isLoad = true;
                hideProgressDialog();
                handDataShow(obj);
                CacheUtil.save(getActivity(), CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                isLoad = true;
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


    private void gotos() {
//        String url =  model.url;
//        if (TextUtils.isEmpty(url))   return;
//        if (url.contains("http://")||url.contains("https://")){
//            Intent intent = new Intent(getContext(), MyWebActivity.class);
//            intent.putExtra("Url",url);
//        }
    }

    //处理界面上的显示
    private void handDataShow(CommonResult<XiaoxiList> obj) {
        if (obj.data.getMessageReco() != null) {
            emptyLayout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            xiaoxiLists.clear();
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
                if (MainAc.intance.isLogin())
                    startActivity(new Intent(getActivity(), CoachingAct.class)
                            .putExtra("data", obj));
                break;
            //通知消息
            case R.id.notifyItem:
                if (MainAc.intance.isLogin())
                    startActivity(new Intent(getActivity(), NotifyAct.class)
                            .putExtra("data", obj));
                break;
        }
    }


    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
//        if (direction == SwipyRefreshLayoutDirection.TOP) {
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            xiaoxiLists.clear();
//                            getMessList();
//                            // 刷新设置
//                            refreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//            }, 2000);
//        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Hide the refresh after 2sec
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            getMessList();
//                            // 刷新设置
//                            refreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//            }, 2000);
//        }
    }

    boolean isLoad = true;

    @Override
    public synchronized void onLoadChangeListener(int deltaY) {
        if (deltaY > 200 && isLoad) {
            isLoad = false;
            xiaoxiLists.clear();
            refreshData();
        } else if (deltaY < -200 && isLoad) {

        }
    }

    //消息列表item的tag
    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private SketchImageView img_tu;

        public ItemTag(View iteView) {
            super(iteView);
            img_tu = (SketchImageView) iteView.findViewById(R.id.img_tu);
            //img_tu.getOptions().setResize(new Resize(getScreenWidth(), (int) (getScreenWidth()*0.625f), Resize.Mode.EXACTLY_SAME));
        }
    }

    //刷新红点
    private void refreshRedPoint(CommonResult<XiaoxiList> obj) {
        this.obj = obj;
        boolean isRead = false;
        if (obj == null || obj.data == null) {
            radCountCoachingView.setVisibility(View.GONE);
            radCountNotifyView.setVisibility(View.GONE);
            return;
        }
        if (obj.data.getAppraise() == null || obj.data.getAppraise().size() < 1) {
            radCountCoachingView.setVisibility(View.GONE);
            tv_jiaolian.setText("暂无点评。报名课程，获得更多教练点评");
        } else {
            boolean hasNotRead = false;
            for (XiaoxiList.AppraiseBean appraiseBean : obj.data.getAppraise()) {
                tv_jiaolian.setText("" + appraiseBean.getTitle());
                if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                    hasNotRead = true;
                    isRead = true;
                    tv_jiaolian.setText("" + appraiseBean.getTitle());
                    break;
                }
            }
            radCountCoachingView.setVisibility(hasNotRead ? View.VISIBLE : View.GONE);
        }
        if (obj.data.getMessageAll() == null || obj.data.getMessageAll().size() < 1) {
            radCountNotifyView.setVisibility(View.GONE);
            tv_tongzhi.setText("暂无新消息");
        } else {
            boolean hasNotRead = false;
            for (XiaoxiList.MessageAllBean appraiseBean : obj.data.getMessageAll()) {
                tv_tongzhi.setText(appraiseBean.getTitle() + "");
                if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                    hasNotRead = true;
                    isRead = true;
                    tv_tongzhi.setText(appraiseBean.getTitle() + "");
                    break;
                }
            }
            radCountNotifyView.setVisibility(hasNotRead ? View.VISIBLE : View.GONE);
        }
        if (isRead) {
            MainAc.intance.showRed(true);
        } else {
            MainAc.intance.showRed(false);
        }
    }

    //隐藏对话框
    public void hideProgressDialog() {
        ((BaseFragmentActivity) getActivity()).hideProgressDialog();
    }

    //跳转到详情页面
    private void jumpDetail(String messageId, String titleStr) {
        Intent intent = new Intent(getActivity(), XiaoXiDetailAct.class);
        intent.putExtra("messageId", messageId);
        intent.putExtra("titleStr", titleStr);
        startActivity(intent);
    }

    //获取图片应该显示的高度
    private  int getImgDisplanHeight(int imgWidth, int imgHeight) {
        float screenHeight = getScreenWidth()/imgWidth*imgHeight;
        return (int) screenHeight;
    }

    //获取屏幕的宽
    private  int getScreenWidth() {
        DisplayMetrics dm = App.getInstance().getResources().getDisplayMetrics();
        Log.e("getScreenWidth", dm.widthPixels + "");
        return dm.widthPixels -dp2px(getContext(),28);
    }
    private int dp2px(Context context, int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }

}
