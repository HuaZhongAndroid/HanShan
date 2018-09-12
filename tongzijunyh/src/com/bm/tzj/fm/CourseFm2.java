package com.bm.tzj.fm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.adapter.CourseGalleryAdapter;
import com.bm.base.adapter.ImagePagerAdapters280;
import com.bm.dialog.AddBodyDialog;
import com.bm.entity.Advertisement;
import com.bm.entity.Adverts;
import com.bm.entity.Storelist;
import com.bm.entity.User;
import com.bm.tzj.activity.AccouterIndexAc;
import com.bm.tzj.activity.LeyuanAc;
import com.bm.tzj.activity.LeyuanStoreAc;
import com.bm.tzj.activity.LuyingAc;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.activity.MainAc.OnTabActivityResultListener;
import com.bm.tzj.activity.NaotengAc;
import com.bm.tzj.activity.ZhoumoAc;
import com.bm.tzj.city.Activity01;
import com.bm.tzj.city.City;
import com.bm.tzj.kc.CourseListAc;
import com.bm.tzj.mine.MyMessageAc;
import com.bm.tzj.mine.RechargeAc2;
import com.bm.util.BDLocationHelper;
import com.bm.util.CacheUtil;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.Pager;
import com.lib.tool.UITools;
import com.lib.widget.RatingBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 课程
 */
@SuppressLint("NewApi")
public class CourseFm2 extends Fragment implements OnClickListener,AppBarLayout.OnOffsetChangedListener,
        OnTabActivityResultListener{

    private boolean notFlush = false; //标识此次界面返回不刷新数据

    private PopupWindow popupWindow;

    private Toolbar toolbar;

//    private ArrayList<Child> childrenList;

    private int type;
    private LinearLayout s_six;

    private AppBarLayout app_bar;
    private ImageView img_find;
    private TextView tv_find,tv_yj;

    private Context context;
    private ImageView iv_yj, iv_six_a, iv_six_b, iv_six_c, iv_six_d,iv_six_e,iv_six_f,img_read,img_default;
    private LinearLayout ll_search,ll_default;
    //	private FrameLayout fl;
//	private HorizontalListView h_list;
    private CourseGalleryAdapter adapter;
    private ListView lv_content;
    List<Storelist> prolist = new ArrayList<Storelist>();
    private TextView tv_location; //tv_poptip;
    City city  =null;
    /** 分页器 */
    public Pager pager = new Pager(10);
    private List<Advertisement> listAdvment = new ArrayList<Advertisement>();

    //	private ScrollView sv;
    private ViewPager vp_ads;
    private LinearLayout ll_vp_indicator;
    private FrameLayout fm_image;
    String[] imagesSrc = new String[] {
            "http://images.hisupplier.com/var/userImages/201312/25/145025923484_s.jpg",
            "http://images.hisupplier.com/var/userImages/201403%2F20%2F211154839619.jpg",
            "http://ww1.sinaimg.cn/mw600/d1946ec0jw1e5nntcqu71j21kw11xnek.jpg" };


    private int index=-1;//判断是单广告还是多广告
    private ArrayList<Advertisement> advertisements=new ArrayList<Advertisement>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fm_course, container,
                false);
        context = this.getActivity();
        city  =App.getInstance().getCityCode();
        initView(messageLayout);
        // App.toast("HealthRecordFm");
        return messageLayout;
    }

    /**
     * 初始化数据
     *
     * @param v
     */
    private void initView(View v) {

        app_bar = (AppBarLayout)v.findViewById(R.id.app_bar);
        app_bar.getLayoutParams().height = App.getInstance().getScreenWidth()/2;
        app_bar.addOnOffsetChangedListener(this);
        toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        img_find = (ImageView)v.findViewById(R.id.img_find);
        tv_find = (TextView) v.findViewById(R.id.tv_find);
        tv_yj = (TextView) v.findViewById(R.id.tv_yj);

        img_default = (ImageView) v.findViewById(R.id.img_default);
//		sv = (ScrollView) v.findViewById(R.id.sv);
        lv_content = (ListView) v.findViewById(R.id.lv_content);
        iv_yj = (ImageView) v.findViewById(R.id.iv_yj);
        v.findViewById(R.id.btn_yj).setOnClickListener(this);
//		fl = (FrameLayout) v.findViewById(R.id.fl);
//		h_list = (HorizontalListView) v.findViewById(R.id.h_list);
        img_read=(ImageView) v.findViewById(R.id.img_read);
        tv_location = (TextView) v.findViewById(R.id.tv_location);
//		 mRefreshLayout = (NsRefreshLayout) v.findViewById(R.id.swipyrefreshlayout);
//		 mRefreshLayout.setRefreshLayoutController(this);
//		 mRefreshLayout.setRefreshLayoutListener(this);

        vp_ads = (ViewPager) v.findViewById(R.id.vp_ads);
        ll_vp_indicator = (LinearLayout) v.findViewById(R.id.ll_vp_indicator);
        fm_image = (FrameLayout)v.findViewById(R.id.fm_image);


        if(null!=city&&!TextUtils.isEmpty(city.regionName)){
            String strCity =city.regionName;
            tv_location.setText(city.regionName+"");//城市名称

            //包含市 则截取
            strCity=strCity.substring(strCity.length()-1,strCity.length());
            if(strCity.equals("市")){
                tv_location.setText(city.regionName.substring(0,city.regionName.length()-1));
            }
        }else{
            tv_location.setText("西安");//城市名称
        }

        initH(v);
        init();
        setFoucs();
        getIsMessage();
        getAdvertlist();

    }

    /**
     * 设置焦点
     *
     */
    public void setFoucs() {
//		sv.smoothScrollTo(0, 0);
        lv_content.setFocusable(false);
    }

    private void initH(View v) {
        iv_six_a = (ImageView) v.findViewById(R.id.iv_six_a);
        iv_six_b = (ImageView) v.findViewById(R.id.iv_six_b);
        iv_six_c = (ImageView) v.findViewById(R.id.iv_six_c);
        iv_six_d = (ImageView) v.findViewById(R.id.iv_six_d);
        iv_six_e = (ImageView) v.findViewById(R.id.iv_six_e);
        iv_six_f = (ImageView) v.findViewById(R.id.iv_six_f);

        int wwidth = (int)(App.getInstance().getScreenWidth()/4.6);
        v.findViewById(R.id.six_a).getLayoutParams().width = wwidth;
        v.findViewById(R.id.six_b).getLayoutParams().width = wwidth;
        v.findViewById(R.id.six_c).getLayoutParams().width = wwidth;
        v.findViewById(R.id.six_d).getLayoutParams().width = wwidth;
        v.findViewById(R.id.six_e).getLayoutParams().width = wwidth;
        v.findViewById(R.id.six_f).getLayoutParams().width = wwidth;
        int nwidth = (int)(wwidth/1.5);
        iv_six_a.getLayoutParams().width = nwidth;
        iv_six_a.getLayoutParams().height = nwidth;
        iv_six_b.getLayoutParams().width = nwidth;
        iv_six_b.getLayoutParams().height = nwidth;
        iv_six_c.getLayoutParams().width = nwidth;
        iv_six_c.getLayoutParams().height = nwidth;
        iv_six_d.getLayoutParams().width = nwidth;
        iv_six_d.getLayoutParams().height = nwidth;
        iv_six_e.getLayoutParams().width = nwidth;
        iv_six_e.getLayoutParams().height = nwidth;
        iv_six_f.getLayoutParams().width = nwidth;
        iv_six_f.getLayoutParams().height = nwidth;
        v.findViewById(R.id.xian_a).getLayoutParams().width = nwidth/2;
        v.findViewById(R.id.xian_b).getLayoutParams().width = nwidth/2;
        v.findViewById(R.id.xian_c).getLayoutParams().width = nwidth/2;
        v.findViewById(R.id.xian_d).getLayoutParams().width = nwidth/2;


        ll_search = (LinearLayout) v.findViewById(R.id.ll_search);

        s_six = (LinearLayout) v.findViewById(R.id.six_a);
        v.findViewById(R.id.six_a).setOnClickListener(this);
        v.findViewById(R.id.six_b).setOnClickListener(this);
        v.findViewById(R.id.six_c).setOnClickListener(this);
        v.findViewById(R.id.six_d).setOnClickListener(this);
        v.findViewById(R.id.six_e).setOnClickListener(this);
        v.findViewById(R.id.six_f).setOnClickListener(this);


        ll_search.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        MainAc.intance.setOnTabActivityResultListener(this);

        vp_ads.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                // 更新指示器
                for (int i = 0; i < vp_ads.getAdapter().getCount(); i++) {
                    ll_vp_indicator.getChildAt(i).setSelected(i == pos);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }


    //控制4个栏目导航按钮的显示隐藏
    private void csOptionsView()
    {
        if(city.menuids== null || city.menuids.trim().length() == 0)
            return;
        String[] sm = city.menuids.split(",");
        View[] v4 = new View[]{this.getView().findViewById(R.id.six_a),this.getView().findViewById(R.id.six_b),this.getView().findViewById(R.id.six_c),this.getView().findViewById(R.id.six_d)};
        for(View av:v4)
        {
            av.setVisibility(View.GONE);
        }
        for(int i = 0; i<sm.length; i++)
        {
            int t = Integer.valueOf(sm[i]);
            View v = v4[t-1];
            if(i ==0 && v.getId() != s_six.getId())
            {
                type=t;
                select_six((LinearLayout) v);
            }
            v.setVisibility(View.VISIBLE);
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 1:
                    break;
            }
        };
    };

    /**
     * 每隔6秒钟切换到下一页
     */
    private Runnable nextPage = new Runnable() {
        @Override
        public void run() {
            int curpos = vp_ads.getCurrentItem();
            curpos += 1;

            if (vp_ads.getAdapter() != null) {
                if (curpos == vp_ads.getAdapter().getCount()) {
                    curpos = 0;
                }
                ll_vp_indicator.getChildAt(curpos).setSelected(true);
                vp_ads.setCurrentItem(curpos, true);
                handler.postDelayed(nextPage, 3000);
            }
        }
    };

    private ImagePagerAdapters280 imgAdapter;

    /**
     *
     *
     * 加载首页广告图片
     *
     * adpositionidType  1 内部富文本
     * adpositionidType  2 外边链接
     * @param pictures
     */
    private void addImgToViewPager(String[] pictures){
        handler.removeCallbacks(nextPage);
        ll_vp_indicator.removeAllViews();
        for (int i = 0; i < pictures.length; i++) {
            // 添加一个小圆点
            LinearLayout dot = new LinearLayout(context);
            LayoutParams lp = new LinearLayout.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            dot.setBackgroundResource(R.drawable.dot_item_with_view_pager);
            int m = UITools.dip2px(6);
            lp.setMargins(m, m, m, m);
            dot.setLayoutParams(lp);
            ll_vp_indicator.addView(dot, lp);
        }
        // 为了在OnPageChangeListener中引用ViewPager
        ll_vp_indicator.getChildAt(0).setSelected(true);

        imgAdapter = new ImagePagerAdapters280(context, pictures);
        vp_ads.setAdapter(imgAdapter);
        handler.postDelayed(nextPage, 6000);

        //广告图片点击事件 这版暂时去掉
//        imgAdapter.setOnItemClickListener(new ImagePagerAdapters280.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(ImageView view, int position) {
//                System.out.println(advertisements.size()+"#"+position+"wanghaiyan1#"+advertisements.get(position).boardId);
//                if("0".equals(advertisements.get(position).boardType)){//0 单个广告类型 1多个广告类型
//                    getGood(advertisements.get(position).boardId);
//                }else{
//                    Intent intent = new Intent(context,CourseListAc.class);
//                    intent.putExtra("boardId", advertisements.get(position).boardId);
//                    intent.putExtra("tag", "002");
//                    context.startActivity(intent);
//                }
//            }
//        });


    }


    // 初始化课程
    public void init() {
//        listAdapter = new CourseListAdapter(context, list);
        lv_content.setAdapter(proListAdapter);

        type = 1;
        getStorelist();

//        getChildrenList();

        updateView();
    }


    //弹出孩子选择框

    /**
     * 获取未读消息
     */
    public void getIsMessage(){

        final HashMap<String, String> map = new HashMap<String, String>();
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);
        }else{
            map.put("userId", "0");
        }
        if(null!=city&&!TextUtils.isEmpty(city.regionName)){
            map.put("regionName", city.regionName);//城市名称
        }else{
            map.put("regionName", "西安市");//城市名称
        }
        MainAc.intance.showProgressDialog();
        UserManager.getInstance().getTzjmessageIsunreadmessage(context, map, new ServiceCallback<CommonResult<User>>() {

            final String CACHEKEY = "CourseFm_getIsMessage";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonResult<User> obj = new CommonResult<User>(){};
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonResult<User>)CacheUtil.read(context, CACHEKEY, map, type);
                if(obj != null)
                {
                    doResult(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.hideProgressDialog();
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonResult<User> obj) {
                doResult(obj);

                //缓存结果
                CacheUtil.save(context, CACHEKEY,map, obj);
            }

            private void doResult(CommonResult<User> obj)
            {
                MainAc.intance.hideProgressDialog();
                if(null !=obj.data){
                    if("1".equals(obj.data.isMessage)){
                        img_read.setVisibility(View.VISIBLE);
                    }else if("0".equals(obj.data.isMessage)){
                        img_read.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    /**
     * 获取广告位
     */
    public void getAdvertlist(){

        final HashMap<String, String> map = new HashMap<String, String>();
//		if(null != App.getInstance().getUser()){
//			map.put("babyAge", App.getInstance().getUser().babyAge+"");
//		}
        if(null!=city&&!TextUtils.isEmpty(city.regionName)){
            map.put("regionName", city.regionName);//城市名称
        }else{
            map.put("regionName", "西安市");//城市名称
        }
        UserManager.getInstance().getTzjadvertAdvertlist(context, map, new ServiceCallback<CommonListResult<Advertisement>>() {

            final String CACHEKEY = "CourseFm_getAdvertlist";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<Advertisement> obj = new CommonListResult<Advertisement>(){};
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<Advertisement>)CacheUtil.read(context, CACHEKEY, map, type);
                if(obj != null)
                {
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

            private void doResult(CommonListResult<Advertisement> obj)
            {
                if(null!=obj.data&&obj.data.size()>0){
                    listAdvment.clear();
                    advertisements.clear();
                    listAdvment.addAll(obj.data);
                    if(obj.data.size()>0){
                        img_default.setVisibility(View.GONE);
                        fm_image.setVisibility(View.VISIBLE);
                        imagesSrc = new String[listAdvment.size()];
                        for(int i = 0;i<listAdvment.size();i++){
                            imagesSrc[i] = listAdvment.get(i).titleMultiUrl;
                        }
                        advertisements.addAll(obj.data);
                        addImgToViewPager(imagesSrc);

                    }
                }else{
                    img_default.setVisibility(View.VISIBLE);
                    fm_image.setVisibility(View.GONE);
                }

                if(imgAdapter !=null){
                    imgAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //门店列表适配器
    private BaseAdapter proListAdapter = new BaseAdapter(){
        @Override
        public int getCount() {
            return prolist.size();
        }

        @Override
        public Object getItem(int position) {
            return prolist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_storelist, parent, false);
            }
            final Storelist data = prolist.get(position);
            ((TextView)convertView.findViewById(R.id.tv_name)).setText(data.storeName);
            ((TextView)convertView.findViewById(R.id.tv_phone)).setText(data.tel);
            ((TextView)convertView.findViewById(R.id.tv_adr)).setText(data.address);
            ImageLoader.getInstance().displayImage(data.titleMultiUrl, (ImageView)convertView.findViewById(R.id.img_tu),App.getInstance().getListViewDisplayImageOptions());
            RatingBarView custom_ratingbar = (RatingBarView)convertView.findViewById(R.id.custom_ratingbar);
            custom_ratingbar.setClickable(false);
            custom_ratingbar.setStar(Integer.valueOf(data.rankLogistics==null?"0":data.rankLogistics), true);
            if (position==0)
            {
                convertView.findViewById(R.id.tv_zuijin).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.tv_juli).setVisibility(View.GONE);
            }
            else
            {
                convertView.findViewById(R.id.tv_zuijin).setVisibility(View.GONE);
                convertView.findViewById(R.id.tv_juli).setVisibility(View.VISIBLE);
                ((TextView)convertView.findViewById(R.id.tv_juli)).setText(data.distance);
            }

            //点击图片进入门店详情
            convertView.findViewById(R.id.img_tu).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, LeyuanAc.class);
                    i.putExtra("data",data);
                    startActivity(i);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    notFlush = true;
                    switch (type)
                    {
                        case  1: //闹腾
                            Intent i = new Intent(context, NaotengAc.class);
                            i.putExtra("storelist",data);
                            startActivity(i);
                            break;
                        case 2: //乐园
                            i = new Intent(context, LeyuanStoreAc.class);
                            i.putExtra("data",data);
                            startActivity(i);
                            break;
                        case 3: //周末
                            break;
                        case 4: //大露营
                            i = new Intent(context, LuyingAc.class);
                            i.putExtra("data",data);
                            startActivity(i);
                            break;
                    }

                }
            });

            return convertView;
        }
    };
    /**
     * 获取门店
     */
    public void getStorelist(){
        prolist.clear();
        proListAdapter.notifyDataSetChanged();
        BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();
        final HashMap<String, String> map = new HashMap<String, String>();
        if(null!=city&&!TextUtils.isEmpty(city.regionName)){
            map.put("regionName", city.regionName);//城市名称
        }else{
            map.put("regionName", "西安市");//城市名称
        }
        map.put("lat",info.lat+"");
        map.put("lon",info.lng+"");
        map.put("type",""+type);

        UserManager.getInstance().getTzjstoreStorelist(context, map, new ServiceCallback<CommonListResult<Storelist>>() {

            final String CACHEKEY = "CourseFm_getStorelist";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<Storelist> obj = new CommonListResult<Storelist>(){};
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<Storelist>)CacheUtil.read(context, CACHEKEY,map, type);
                if(obj != null)
                {
                    doResult(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<Storelist> obj) {
                doResult(obj);

                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<Storelist> obj)
            {
                if(null!=obj.data){
                    prolist = obj.data;
                    proListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void select_six(LinearLayout ll)
    {
        ((TextView)s_six.getChildAt(1)).setTextColor(0xff999999);
        s_six.getChildAt(2).setVisibility(View.GONE);

        s_six = ll;
        ((TextView)s_six.getChildAt(1)).setTextColor(0xff333333);
        s_six.getChildAt(2).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_yj:
                intent = new Intent(context, MyMessageAc.class);
                startActivity(intent);
                break;
            case R.id.six_a: // 闹腾
                type=1;
                select_six((LinearLayout) v);
                getStorelist();
                break;
            case R.id.six_b: // 乐园
                type=2;
                select_six((LinearLayout) v);
                getStorelist();
                break;
            case R.id.six_c: // 周末
                Intent i = new Intent(context, ZhoumoAc.class);
                startActivity(i);
                break;
            case R.id.six_d:  //大露营
                i = new Intent(context, LuyingAc.class);
                startActivity(i);
                break;
            case R.id.six_e:  //装备
                intent = new Intent(context, AccouterIndexAc.class);
                startActivity(intent);
                break;
            case R.id.six_f:  //充值
                intent = new Intent(context, RechargeAc2.class);
                startActivity(intent);
                break;
            case R.id.ll_search: //导航栏搜索
                intent = new Intent(context, CourseListAc.class);
                intent.putExtra("title", "搜索");
                startActivity(intent);
                break;
            case R.id.tv_location:
                intent = new Intent(context, Activity01.class);
                MainAc.intance.startActivityForResult(intent, 1);
                // startActivity(intent);
                break;
//            case R.id.iv_sixview_head:  //弹出孩子选择框
//                if(MainAc.intance.tag ==2){
//                    showPopupWindow();
////                    tv_poptip.setAnimation(null);
////                    tv_poptip.setVisibility(View.GONE);
//                    SharedPreferencesHelper.saveBoolean("CourseTip", true);
//                }else{
////				isLogin();
//                }
//                break;
            default:
                break;
        }
    }
    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (5 == resultCode) {
            String cityName = data.getStringExtra("cityName"); // 城市名称
            city = (City)data.getSerializableExtra("city");
            if(city != null) {
                tv_location.setText(cityName);
                String strCity = cityName.substring(cityName.length() - 1, cityName.length());
                if (strCity.equals("市")) {
                    tv_location.setText(cityName.substring(0, cityName.length() - 1));
                }
                App.getInstance().saveCityCode(city);
                csOptionsView();
                refresh();
            }
        }
    }


//    /**
//     * 通过广告ID获取商品id
//     */
//    public void getGood(String boardId){
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("boardId", boardId);
//        if(null != App.getInstance().getUser()){
//            map.put("userId", App.getInstance().getUser().userid);
//        }else{
//            map.put("userId", "0");
//        }
//        if(null != App.getInstance().getChild())
//            map.put("babyId", App.getInstance().getChild().babyId);
//        MainAc.intance.showProgressDialog();
//        UserManager.getInstance().getTzjgoodsGoodsdetailbyboard(context, map, new ServiceCallback<CommonResult<HotGoods>>() {
//
//            @Override
//            public void error(String msg) {
//                MainAc.intance.hideProgressDialog();
//                MainAc.intance.dialogToast(msg);
//            }
//
//            @Override
//            public void done(int what, CommonResult<HotGoods> obj) {
//                MainAc.intance.hideProgressDialog();
//                if(null !=obj.data){
//                    Intent intent = new Intent(context,CourseDetailAc.class);
//                    intent.putExtra("goodsId", obj.data.goodsId);
//                    intent.putExtra("degree",  obj.data.goodsType);
//                    intent.putExtra("pageTag", "1");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//
//                    context.startActivity(intent);
//                }
//            }
//        });
//    }


    public void refresh()
    {
        if(notFlush)
        {
            notFlush = false;
            return;
        }
        MainAc.intance.showProgressDialog();
        pager.setFirstPage();
        getAdvertlist();
        getStorelist();
        getIsMessage();
        updateView();
//        getChildrenList();
    }


    public void updateView()
    {
//        Child child = App.getInstance().getChild();
//        if(child != null)
//            ImageLoader.getInstance().displayImage(child.avatar, iv_sixview_head, App.getInstance().getheadImage());
//
////		SharedPreferencesHelper.saveBoolean("CourseTip", false);
//        if(CourseFm.this.isVisible() && childrenList!=null && childrenList.size()>1 && !SharedPreferencesHelper.getBoolean("CourseTip"))
//        {
//            popTip();
//        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        if(verticalOffset == 0)
//        {
//            Log.d("fff","到底了 "+verticalOffset);
//        }
        if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange())
        {
            Log.d("fff","到顶了 "+verticalOffset);
            tv_location.setTextColor(0xff333333);
            Drawable d = ContextCompat.getDrawable(context,R.drawable.xiala_hei);
            d.setBounds(0,0,d.getMinimumWidth(),d.getMinimumHeight());
            tv_location.setCompoundDrawables(null,null,d,null);
            tv_find.setHintTextColor(0xff999999);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_hui));
            tv_yj.setTextColor(0xff333333);
            iv_yj.setImageDrawable(context.getResources().getDrawable(R.drawable.xiaoxi_hei));
        }
        else if(Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange()/3*2)
        {
            Log.d("fff","快到顶了 "+verticalOffset);
            tv_location.setTextColor(0xff333333);
            Drawable d = ContextCompat.getDrawable(context,R.drawable.xiala_hei);
            d.setBounds(0,0,d.getMinimumWidth(),d.getMinimumHeight());
            tv_location.setCompoundDrawables(null,null,d,null);
            tv_find.setHintTextColor(0xff999999);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_hui));
            tv_yj.setTextColor(0xff333333);
            iv_yj.setImageDrawable(context.getResources().getDrawable(R.drawable.xiaoxi_hei));
        }
        else
        {
            Log.d("fff","在中间 "+verticalOffset);
            tv_location.setTextColor(0xffffffff);
            Drawable d = context.getResources().getDrawable(R.drawable.xiala_bai);
            d.setBounds(0,0,d.getMinimumWidth(),d.getMinimumHeight());
            tv_location.setCompoundDrawables(null,null,d,null);
            tv_find.setHintTextColor(0xffffffff);
            img_find.setImageDrawable(context.getResources().getDrawable(R.drawable.find_bai));
            tv_yj.setTextColor(0xffffffff);
            iv_yj.setImageDrawable(context.getResources().getDrawable(R.drawable.xiaoxi_bai));
        }

    }

    private void getAddBody()
    {
        User uInfo = App.getInstance().getUser();
        if(null == uInfo){
            return;
        }
        HashMap<String,String> map = new HashMap<String,String>();
        AsyncHttpHelp.httpGet(context, BaseApi.API_sysAdvertsList, map, new ServiceCallback<CommonListResult<Adverts>>(){
            @Override
            public void done(int what, CommonListResult<Adverts> obj) {
                if(Integer.valueOf(obj.data.size()) > 0)
                {
                    AddBodyDialog dialog = new AddBodyDialog((Activity)context);
                    dialog.show();
                }
            }
            @Override
            public void error(String msg) {
            }});;
    }
}
