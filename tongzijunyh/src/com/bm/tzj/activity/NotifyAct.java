package com.bm.tzj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.XiaoxiListAdapter;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.tzj.city.City;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import java.util.ArrayList;

import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.request.Resize;

import static com.tencent.open.utils.Global.getContext;

/**
 * 通知消息界面
 */
public class NotifyAct extends BaseActivity {

    //城市
    private City city = null;
    //用户
    private User user = null;

    //没有数据的时候显示的空view
    private View emptyLayout;
    //通知消息列表view
    private ListView listView;
    //通知消息列表适配器
    private XiaoxiListAdapter xiaoxiListXiaoxiListAdapter = null;
    //通知消息列表
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
            protected void handViewAndData(final ItemTag tag, final XiaoxiList.MessageAllBean data) {
                tag.titleTv.setText(data.getTitle());

                tag. imageView.getOptions().setResize(new Resize(getScreenWidth(), (int) (getScreenWidth()*0.625f), Resize.Mode.EXACTLY_SAME));
                //获取图片真正的宽高
                Glide.with(context)
                        .load(data.getTitleMultiUrl())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                tag.imageView.getOptions().setResize(new Resize(getScreenWidth(), getImgDisplanHeight(width, height), Resize.Mode.EXACTLY_SAME));
//                                tag.img_tu.getOptions().setResize(new Resize(width, height, Resize.Mode.EXACTLY_SAME));
                            }
                        });

                //获取图片显示在ImageView后的宽高
                Glide.with(context)
                        .load(data.getTitleMultiUrl())
                        .asBitmap()//强制Glide返回一个Bitmap对象
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap bitmap, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
//                                tag.img_tu.getOptions().setResize(new Resize(width, height, Resize.Mode.EXACTLY_SAME));
                                return false;
                            }
                        }).into(tag.imageView);


//                Glide.with(context)
//                        .load(data.getTitleMultiUrl())
//                        .placeholder(R.drawable.adv_default)
//                        .error(R.drawable.adv_default)
//                        .centerCrop()
//                        .dontAnimate()
//                        .into(tag.imageView);
                tag.dateTv.setText(data.getCtime());
                tag.isReadView.setVisibility(data.getIsRead().equals("1") ? View.INVISIBLE : View.VISIBLE);

                tag.iteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.setIsRead("1");
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

    @Override
    protected void onResume() {
        super.onResume();
        if (xiaoxiListXiaoxiListAdapter != null) {
            xiaoxiListXiaoxiListAdapter.notifyDataSetChanged();
        }
    }

    //    通知消息的listview 中的itemTag
    static class ItemTag extends XiaoxiListAdapter.XiaoXitemViewTag {

        private TextView titleTv;
        private SketchImageView imageView;
        private TextView dateTv;
        private View isReadView;

        public ItemTag(View iteView) {
            super(iteView);
            titleTv = (TextView) iteView.findViewById(R.id.titleTv);
            imageView = (SketchImageView) iteView.findViewById(R.id.imageView);
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

    //处理有无数据的逻辑
    private void handDataShow(CommonResult<XiaoxiList> obj) {
        if (obj != null && obj.data.getMessageReco() != null) {
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

    //获取图片应该显示的高度
    private  int getImgDisplanHeight(int imgWidth, int imgHeight) {
        float screenHeight = getScreenWidth()/imgWidth*imgHeight;
        return (int) screenHeight;
    }
    private int dp2px(Context context, int dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,context.getResources().getDisplayMetrics());
    }
    //获取屏幕的宽
    private  int getScreenWidth() {
        DisplayMetrics dm = App.getInstance().getResources().getDisplayMetrics();
        Log.e("debug_screen_wsirg", dm.widthPixels + "");
        return dm.widthPixels-dp2px(this,28);
    }
}
