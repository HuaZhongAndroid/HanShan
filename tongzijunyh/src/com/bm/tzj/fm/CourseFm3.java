package com.bm.tzj.fm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.adapter.ShuQIAdapter;
import com.bm.base.adapter.StoreAdapter;
import com.bm.base.adapter.ZhouMoAdapter;
import com.bm.entity.Advertisement;
import com.bm.entity.CourseBean;
import com.bm.entity.Storelist;
import com.bm.entity.ZhouMoCity;
import com.bm.tzj.activity.LeyuanAc;
import com.bm.tzj.activity.LuyingAc;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.activity.MyWebActivity;
import com.bm.tzj.activity.NaoTengListAct;
import com.bm.tzj.activity.ZhoumoAc;
import com.bm.tzj.city.Activity01;
import com.bm.tzj.city.City;
import com.bm.tzj.kc.CourseListAc;
import com.bm.util.BDLocationHelper;
import com.bm.util.CacheUtil;
import com.bumptech.glide.Glide;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * 发现
 * <p>
 * 1 广告
 * 2，室内体验
 * 3，闹腾训练中心
 * 4，周末
 * 5  暑假
 */
@SuppressLint("NewApi")
public class CourseFm3 extends BaseFm implements AppBarLayout.OnOffsetChangedListener,
        MainAc.OnTabActivityResultListener {

    City city;
    String cityName = "西安市";
    List<ZhouMoCity> zhouMoCityList;//周末成长营 地址列表
    /**
     * 搜索框
     */
    private AppBarLayout app_bar;  //
    private CollapsingToolbarLayout toolbar_layout;  //
    private TextView tv_location;   //城市地址
    private LinearLayout ll_search; //搜索框
    private ImageView img_find;
    private TextView tv_find;


    /**
     * 轮播图
     */
    private BGABanner bannerView;
    private List<Advertisement> listAdvment = new ArrayList<Advertisement>();

    /**
     * 闹腾生存适能训练中心
     */
    private LinearLayout lay1;
    private TextView tv_title1;
    private TextView tv_more1;
    private com.lib.widget.FuListView lv_content1;
    private StoreAdapter adapter1;

    /**
     * 室内体验馆
     */
    private LinearLayout lay11;
    private TextView tv_title11;
    private TextView tv_more11;
    private com.lib.widget.FuListView lv_content11;
    private StoreAdapter adapter11;

    /**
     * 周末成长营
     */

    private LinearLayout lay2;
    private TextView tv_title2;
    private TextView tv_more2;
    private TextView tv_hyh;        //换一换
    private com.lib.widget.FuListView lv_content2;
    private ZhouMoAdapter adapter2;

    private LinearLayout lay_city; //  当城市个数大于1  则显示
    private TextView tv_text2;     //  当城市个数小雨等于1  则显示

    /**
     * 暑期大露营
     */
    private LinearLayout lay3;
    private TextView tv_title3;
    private TextView tv_more3;
    private com.lib.widget.FuListView lv_content3;
    private ShuQIAdapter adapter3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fm_course3, container,
                false);
        context = this.getActivity();
        city = App.getInstance().getCityCode();
        initCity(messageLayout);
        initView(messageLayout);
        initRefreshView(messageLayout);
        initBannerView(messageLayout);
        initOnClickListener();
        refresh();
        return messageLayout;
    }

    private void initRefreshView(View v) {
        final RefreshLayout refreshLayout = (RefreshLayout) v.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
                // 因为接口有四个 所以无法做到在请求结束关闭  只能模拟等待3秒后关闭
                refreshLayout.finishRefresh(3000/*,false*/);//传入false表示刷新失败
                //refreshLayout.finishRefresh();
            }
        });
    }

    private void initOnClickListener() {
        ll_search.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        MainAc.intance.setOnTabActivityResultListener(this);

        tv_more1.setOnClickListener(this);
        tv_more11.setOnClickListener(this);
        tv_more2.setOnClickListener(this);
        tv_hyh.setOnClickListener(this);
        tv_more3.setOnClickListener(this);
    }

    /**
     * 搜索栏 和 选择城市
     */


    private void initCity(View v) {
        toolbar_layout = (CollapsingToolbarLayout) v.findViewById(R.id.toolbar_layout);
        app_bar = (AppBarLayout) v.findViewById(R.id.app_bar);
        app_bar.getLayoutParams().height = App.getInstance().getScreenWidth() / 2;
        app_bar.addOnOffsetChangedListener(this);

        img_find = (ImageView) v.findViewById(R.id.img_find);
        tv_find = (TextView) v.findViewById(R.id.tv_find);
        tv_location = (TextView) v.findViewById(R.id.tv_location);
        ll_search = (LinearLayout) v.findViewById(R.id.ll_search);

        if (null != city && !TextUtils.isEmpty(city.regionName)) {
            String strCity = city.regionName;
            tv_location.setText(city.regionName + "");//城市名称
            //包含市 则截取
            strCity = strCity.substring(strCity.length() - 1, strCity.length());
            if (strCity.equals("市")) {
                tv_location.setText(city.regionName.substring(0, city.regionName.length() - 1));
            }
        } else {
            tv_location.setText("西安");//城市名称
        }
    }

    private void initView(View v) {

        lay1 = (LinearLayout) v.findViewById(R.id.lay1);
        tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
        tv_more1 = (TextView) v.findViewById(R.id.tv_more1);
        lv_content1 = (com.lib.widget.FuListView) v.findViewById(R.id.lv_content1);
        adapter1 = new StoreAdapter(getContext());
        lv_content1.setAdapter(adapter1);

        lay11 = (LinearLayout) v.findViewById(R.id.lay11);
        tv_title11 = (TextView) v.findViewById(R.id.tv_title11);
        tv_more11 = (TextView) v.findViewById(R.id.tv_more11);
        lv_content11 = (com.lib.widget.FuListView) v.findViewById(R.id.lv_content11);
        adapter11 = new StoreAdapter(getContext());
        lv_content11.setAdapter(adapter11);


        lay2 = (LinearLayout) v.findViewById(R.id.lay2);
        tv_title2 = (TextView) v.findViewById(R.id.tv_title2);
        tv_more2 = (TextView) v.findViewById(R.id.tv_more2);
        lay_city = (LinearLayout) v.findViewById(R.id.lay_city);
        tv_text2 = (TextView) v.findViewById(R.id.tv_text2);
        tv_hyh = (TextView) v.findViewById(R.id.tv_hyh);
        adapter2 = new ZhouMoAdapter(getContext());
        lv_content2 = (com.lib.widget.FuListView) v.findViewById(R.id.lv_content2);
        lv_content2.setAdapter(adapter2);

        lay3 = (LinearLayout) v.findViewById(R.id.lay3);
        tv_title3 = (TextView) v.findViewById(R.id.tv_title3);
        tv_more3 = (TextView) v.findViewById(R.id.tv_more3);
        lv_content3 = (com.lib.widget.FuListView) v.findViewById(R.id.lv_content3);
        adapter3 = new ShuQIAdapter(getContext());
        lv_content3.setAdapter(adapter3);
    }

    /**
     * 初始化轮播图
     */
    int screenWidth = 0;
    int screenHeight = 0;
    int bannerHeight = 0;

    private void initBannerView(View v) {
        screenWidth = App.getInstance().getScreenWidth();
        screenHeight = App.getInstance().getScreenHeight();
        if (screenWidth != 0 && screenHeight != 0) {
            bannerHeight = screenWidth / 16 * 9;
        }
        Log.e("screenWidth", "screenWidth = " + screenWidth);
        Log.e("screenHeight", "screenHeight = " + screenHeight);
        Log.e("bannerHeight", "bannerHeight = " + bannerHeight);
        bannerView = (BGABanner) v.findViewById(R.id.bannerView);
//        if (bannerHeight != 0) {
//            //bannerView.setMinimumHeight(bannerHeight);
//            bannerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, bannerHeight));
//        }
        bannerView.setAdapter(new BGABanner.Adapter<ImageView, Advertisement>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, Advertisement model, int position) {
                itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity())
                        .load(model.titleMultiUrl)
                        .placeholder(R.drawable.adv_default)
//                        .error(R.drawable.adv_default)
                        .dontAnimate()
                        .into(itemView);
            }
        });
        bannerView.setDelegate(new BGABanner.Delegate<ImageView, Advertisement>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, Advertisement model, int position) {
                //Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                String url = model.url;
                if (TextUtils.isEmpty(url)) return;
                if (url.contains("http://") || url.contains("https://")) {
                    Intent intent = new Intent(getContext(), MyWebActivity.class);
                    intent.putExtra("Title", "收入明细");
                    intent.putExtra("Url", url);
                } else if (url.contains("tzj://advert?")) {
                    //跳转门店
//                    String str = url.replace("tzj://advert?","");
//                    if (!str.contains("="))return;
//                    String[] split  =   str.split("=");
//                    if (split!=null&&split.length==2){
////                        Intent i = new Intent(context, LeyuanAc.class);
////                        i.putExtra("sid", split[1]);
////                        startActivity(i);
//                    }
                }

            }
        });
    }

    /**
     * 获取广告位
     */
    public void getAdvertlist() {

        final HashMap<String, String> map = new HashMap<String, String>();
        if (null != city && !TextUtils.isEmpty(city.regionName)) {
            map.put("regionName", city.regionName);//城市名称
        } else {
            map.put("regionName", "西安市");//城市名称
        }
        UserManager.getInstance().getTzjadvertAdvertlist(context, map, new ServiceCallback<CommonListResult<Advertisement>>() {
            final String CACHEKEY = "CourseFm_getAdvertlist";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<Advertisement> obj = new CommonListResult<Advertisement>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<Advertisement>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<Advertisement> obj) {
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<Advertisement> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    listAdvment.clear();
                    listAdvment.addAll(obj.data);
                    if (obj.data.size() > 0) {
                        bannerView.setAutoPlayAble(listAdvment.size() > 1);
                        bannerView.setData(listAdvment, new ArrayList<String>(listAdvment.size()));
                    }
                }
            }
        });
    }


    /**
     * @param types    3  周末成长营 4  暑期大露营
     * @param regionId 城市id【getZhouMoCitys接口中的regionId参数】
     */
    //    周末成长营
    //    http://59.110.62.10:8888/tongZiJun/api/tzjgoods/goodsListByType?goodsType=3&typeCode=
    //    暑期大露营
    //    http://59.110.62.10:8888/tongZiJun/api/tzjgoods/goodsListByType?goodsType=4&typeCode=
    private void loadCourseList(final int types, final String regionId) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsType", types + "");
        map.put("typeCode", regionId + "");//regionId
        UserManager.getInstance().get_tzjgoods_goodsListByType(getActivity(), map, new ServiceCallback<CommonListResult<CourseBean>>() {
            final String CACHEKEY = "CourseFm_tzjgoods_goodsListByType";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<CourseBean> obj = new CommonListResult<CourseBean>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<CourseBean>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }
                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<CourseBean> obj) {
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<CourseBean> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    if (types == 3) {
                        tv_more2.setVisibility(View.GONE);
                        tv_hyh.setVisibility(View.GONE);

                        adapter2.clear();
                        adapter2.setList(obj.data);
                        adapter2.showData(3, false);
                        adapter2.notifyDataSetChanged();
                        lay2.setVisibility(View.VISIBLE);
                        if (obj.data.size() > 3) {
                            tv_more2.setVisibility(View.VISIBLE);
                            tv_hyh.setVisibility(View.VISIBLE);
                        }
                    } else {
                        adapter3.clear();
                        adapter3.addAll(cutList(obj.data, 3));
                        adapter3.notifyDataSetChanged();
                        lay3.setVisibility(View.VISIBLE);
                        if (obj.data.size() > 3)
                            tv_more3.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (types == 3) {
                        lay2.setVisibility(View.GONE);
                    } else {
                        lay3.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    /**
     * 获取周末成长营  城市地址
     * <p>
     * http://59.110.62.10:8888/tongZiJun/api/tzjgoods/getTrend
     */
    public void getZhouMoCitys() {
        final HashMap<String, String> map = new HashMap<String, String>();
        UserManager.getInstance().getZhouMoCitys(context, map, new ServiceCallback<CommonListResult<ZhouMoCity>>() {
            final String CACHEKEY = "CourseFm_getZhouMoCitys";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<ZhouMoCity> obj = new CommonListResult<ZhouMoCity>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<ZhouMoCity>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<ZhouMoCity> obj) {
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<ZhouMoCity> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    if (obj.data.size() == 1) {
                        lay_city.setVisibility(View.GONE);
                        tv_text2.setVisibility(View.VISIBLE);
                    } else {
                        addZhouMoCity(obj.data);
                        lay_city.setVisibility(View.VISIBLE);
                        tv_text2.setVisibility(View.GONE);
                    }
                    String regionId = obj.data.get(0).regionId;
                    loadCourseList(3, regionId);
                    loadCourseList(4, regionId);
                }
            }

        });
    }

    /**
     * 周末城市 添加
     *
     * @param list
     */
    LinearLayout lastView;

    private void addZhouMoCity(List<ZhouMoCity> list) {
        lay_city.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            ZhouMoCity city = list.get(i);
            LinearLayout view = (LinearLayout) View.inflate(getContext(), R.layout.fm_course3_item_citys, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_city);
            View line = view.findViewById(R.id.line);
            textView.setText(city.regionName);
            textView.setTag(city.regionId);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.golden_1));
                line.setBackgroundColor(getResources().getColor(R.color.golden_1));
                line.setVisibility(View.VISIBLE);
                lastView = view;
            } else {
                textView.setTextColor(getResources().getColor(R.color.gray_1));
                line.setBackgroundColor(getResources().getColor(R.color.golden_1));
                line.setVisibility(View.INVISIBLE);
            }
            lay_city.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView city;
                    View line;
                    if (lastView != null) {
                        city = (TextView) lastView.getChildAt(0);
                        line = lastView.getChildAt(1);
                        city.setTextColor(getResources().getColor(R.color.gray_1));
                        line.setVisibility(View.INVISIBLE);
                    }
                    LinearLayout newLay = (LinearLayout) v;
                    city = (TextView) newLay.getChildAt(0);
                    line = newLay.getChildAt(1);
                    city.setTextColor(getResources().getColor(R.color.golden_1));
                    line.setVisibility(View.VISIBLE);
                    lastView = newLay;

                    String regionId = (String) city.getTag();
                    loadCourseList(3, regionId);
                }
            });
        }
    }

    /**
     * 截取指定大小的list
     *
     * @param list
     * @param size
     * @return
     */
    private ArrayList cutList(ArrayList list, int size) {
        if (list.size() <= size) {
            return list;
        } else {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < size; i++) {
                arrayList.add(list.get(i));
            }
            return arrayList;
        }


    }

    /**
     * 获取门店
     *
     * @param types 1,闹腾训练中心 2，室内体验馆
     */
//    室内体验馆
//    http://59.110.62.10:8888/tongZiJun/api/tzjstore/storelist?regionName=西安市&lon=1&lat=1&type=2
//    闹腾生存适能训练中心
//    http://59.110.62.10:8888/tongZiJun/api/tzjstore/storelist?regionName=西安市&lon=1&lat=1&type=1
    public void getStorelist(final int types) {
        BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();

        final HashMap<String, String> map = new HashMap<String, String>();
        if (null != city && !TextUtils.isEmpty(city.regionName)) {
            map.put("regionName", city.regionName);//城市名称
        } else {
            map.put("regionName", "西安");//城市名称
        }
        map.put("lat", info.lat + "");
        map.put("lon", info.lng + "");
        map.put("type", "" + types);
        UserManager.getInstance().getTzjstoreStorelist(context, map, new ServiceCallback<CommonListResult<Storelist>>() {
            final String CACHEKEY = "CourseFm_getStorelist";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<Storelist> obj = new CommonListResult<Storelist>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<Storelist>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }
                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<Storelist> obj) {
                if (obj != null) Log.e("obj", "" + obj.data);
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<Storelist> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    if (types == 1) {
                        adapter1.clear();
                        adapter1.setType(1);
                        adapter1.addAll(cutList(obj.data, 2));
                        adapter1.notifyDataSetChanged();
                        lay1.setVisibility(View.VISIBLE);
                        if (obj.data.size() > 2)
                            tv_more1.setVisibility(View.VISIBLE);
                    } else {
                        adapter11.clear();
                        adapter11.setType(2);
                        adapter11.addAll(cutList(obj.data, 2));
                        adapter11.notifyDataSetChanged();
                        lay11.setVisibility(View.VISIBLE);
                        if (obj.data.size() > 2)
                            tv_more1.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (types == 1) {
                        lay1.setVisibility(View.GONE);
                        adapter1.setType(1);
                    } else {
                        lay11.setVisibility(View.GONE);
                        adapter11.setType(2);
                    }

                }
            }
        });
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            Log.d("fff", "到顶了 " + verticalOffset);
            toolbar_layout.setContentScrimColor(0xffffffff);
            tv_location.setTextColor(0xff333333);
            Drawable d = ContextCompat.getDrawable(context, R.drawable.xiala_hei);
            d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
            tv_location.setCompoundDrawables(null, null, d, null);
            tv_find.setHintTextColor(0xff999999);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_hui));
        } else if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() / 3 * 2) {
            Log.d("fff", "快到顶了 " + verticalOffset);
            toolbar_layout.setContentScrimColor(0x00ffffff);
            tv_location.setTextColor(0xff333333);
            Drawable d = ContextCompat.getDrawable(context, R.drawable.xiala_hei);
            d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
            tv_location.setCompoundDrawables(null, null, d, null);
            tv_find.setHintTextColor(0xff999999);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_hui));
        } else {
            // Log.e("eeee", "在中间 " + verticalOffset);
            tv_location.setTextColor(0xffffffff);
            Drawable d = ContextCompat.getDrawable(context, R.drawable.xiala_bai);
            d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
            tv_location.setCompoundDrawables(null, null, d, null);
            tv_find.setHintTextColor(0xffffffff);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_bai));
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_location: //选择城市
                intent = new Intent(context, Activity01.class);
                MainAc.intance.startActivityForResult(intent, 1);
                break;
            case R.id.ll_search://搜索
                intent = new Intent(context, CourseListAc.class);
                intent.putExtra("title", "搜索");
                startActivity(intent);
                break;
            case R.id.tv_more1://闹腾 查看更多
                intent = new Intent(context, NaoTengListAct.class);
                intent.putExtra("title", "闹腾训练中心");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.tv_more11://室内 查看更多
                intent = new Intent(context, NaoTengListAct.class);
                intent.putExtra("title", "室内体验馆");
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.tv_more2://周末成长营 查看更多
                intent = new Intent(context, ZhoumoAc.class);
                intent.putExtra("title", "搜索");
                startActivity(intent);
                break;
            case R.id.tv_hyh://周末成长营 换一换
                if (adapter2 != null)
                    adapter2.showData(3, true);
                break;
            case R.id.tv_more3://暑期大露营 查看更多
                intent = new Intent(context, LuyingAc.class);
                intent.putExtra("title", "搜索");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (5 == resultCode) {
            String cityName = data.getStringExtra("cityName"); // 城市名称
            city = (City) data.getSerializableExtra("city");
            if (city != null) {
                tv_location.setText(cityName);
                String strCity = cityName.substring(cityName.length() - 1, cityName.length());
                if (strCity.equals("市")) {
                    tv_location.setText(cityName.substring(0, cityName.length() - 1));
                }
                App.getInstance().saveCityCode(city);
                //csOptionsView();
                refresh();
            }
        }
    }

    public void refresh() {
        getAdvertlist();
        getStorelist(1);
        getStorelist(2);
        getZhouMoCitys();
    }

    public void updateView() {
    }

    public void setFoucs() {
    }
}
