package com.bm.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.bm.dialog.ToastDialog;
import com.bm.entity.Child;
import com.bm.entity.User;
import com.bm.im.tool.DemoHelper;
import com.bm.tzj.city.City;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.lib.tool.SharedPreferencesHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.richer.tzj.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Application类
 */
public class App extends Application {
    static final String TAG = App.class.getSimpleName();
    private Handler handler;
    private static App instance;
    public int mode_w = 480;
    public int mode_h = 800;

    /**
     * 列表中显示图片的选项
     */
    private DisplayImageOptions listViewDisplayImageOptions;
    private DisplayImageOptions advertisingImageOptions;//广告位
    private DisplayImageOptions headImage;
    private DisplayImageOptions addImage;
    private DisplayImageOptions bgImage;

    /**
     * 大图显示imageloder
     */
    private DisplayImageOptions bigImageOptions;


    /**
     * 列表中显示图片的选项
     */
    private DisplayImageOptions headOptions;

    /**
     * 群列表中显示图片的选项
     */
    private DisplayImageOptions groupHeadOptions;
    /**
     * 灰色头
     */
    private DisplayImageOptions grayHeadImage;

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        //腾讯的日志收集器  和SDKInitializer 冲突了 错误 build.gradle 中的 ndk 代码
        CrashReport.initCrashReport(getApplicationContext(), "bc02630693", true);
        SDKInitializer.initialize(this);
        instance = this;
        super.onCreate();
        handler = new Handler();
        // Log.i(TAG, "酒食网 启动...");
        DemoHelper.getInstance().init(this);
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);


        initImageLoader(this);
        UMShareAPI.get(this);
        // 设置列表图片头像显示配置
        headImage = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defa_head)
                .showImageForEmptyUri(R.drawable.defa_head)
                .showImageOnFail(R.drawable.defa_head).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        addImage = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.add_child2)
                .showImageForEmptyUri(R.drawable.add_child2)
                .showImageOnFail(R.drawable.add_child2).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        bgImage = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_dark)
                .showImageForEmptyUri(R.drawable.bg_dark)
                .showImageOnFail(R.drawable.bg_dark).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        grayHeadImage = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_avatar)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        // 设置群头像显示
        groupHeadOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ease_groups_icon)
                .showImageForEmptyUri(R.drawable.ease_groups_icon)
                .showImageOnFail(R.drawable.ease_groups_icon).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        // 设置列表图片显示配置
        listViewDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defult_shape)
                .showImageForEmptyUri(R.drawable.defult_shape)
                .showImageOnFail(R.drawable.defult_shape).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        // 设置广告位显示配置
        advertisingImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.adv_default)
                .showImageForEmptyUri(R.drawable.adv_default)
                .showImageOnFail(R.drawable.adv_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        bigImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defult_shape)
                .showImageForEmptyUri(R.drawable.defult_shape)
                .showImageOnFail(R.drawable.defult_shape).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        headOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.defult_shape)
                .showImageForEmptyUri(R.drawable.defult_shape)
                .showImageOnFail(R.drawable.defult_shape).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        //   友盟  各个平台的配置，建议放在全局Application或者程序入口
        {
            //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//	    	 PlatformConfig.setWeixin("wxee3498723b5ba695", "f488ed3e543789b4d01b1c7bbd6d8cb0");
            PlatformConfig.setWeixin("wxee3498723b5ba695", "f488ed3e543789b4d01b1c7bbd6d8cb0");

            PlatformConfig.setSinaWeibo("3078096499", "646f3d6a55eadc1e28bf8d5c68a73fe8");
            //QQ
            PlatformConfig.setQQZone("1105258523", "3NOaSqi4ybnf6jf8");
            //新浪微博
//	        PlatformConfig.setSinaWeibo("3078096499", "646f3d6a55eadc1e28bf8d5c68a73fe8");
//	        //QQ
//	        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");

        }
    }

    public static App getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setUser(User user) {
        Child child = this.getChild();
        if (user != null && child != null) {
            user.babyAge = child.age;
            user.babyId = child.babyId;
            user.babyName = child.realName;
            user.babyBirthday = child.birthday;
            user.babyGender = child.gender;
        }
        SharedPreferencesHelper.saveJSON("user", user);
        SharedPreferencesHelper.saveJSON("child", child);
    }

    public User getUser() {
        User user = SharedPreferencesHelper.getJSON("user", User.class);
        return user;
    }

    public void setChild(Child child) {
        SharedPreferencesHelper.saveJSON("child", child);
        User user = this.getUser();
        this.setUser(user);
    }

    public Child getChild() {
        Child child = SharedPreferencesHelper.getJSON("child", Child.class);
        return child;
    }


    /**
     * 保存城市code
     */
    public void saveCityCode(City city) {
        SharedPreferencesHelper.saveJSON("cityCode", city);
    }

    //删除城市code
    public void removeCityCode() {
        SharedPreferencesHelper.remove("cityCode");
    }

    //获取城市code
    public City getCityCode() {
        City city = SharedPreferencesHelper.getJSON("cityCode", City.class);
        return city;
    }

    //假如获取为空，这里默认为北京的城市code
    public String getDefaultCityCode() {
        return "110100";
    }

    public int getMode_w() {
        return mode_w;
    }

    public void setMode_w(int mode_w) {
        this.mode_w = mode_w;
    }

    public int getMode_h() {
        return mode_h;
    }

    public void setMode_h(int mode_h) {
        this.mode_h = mode_h;
    }

    private static Toast toast;

    public static void toast(int resId) {
        toast(getInstance().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void toastShort(int resId) {
        toast(getInstance().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void toastLong(int resId) {
        toast(getInstance().getString(resId), Toast.LENGTH_LONG);
    }

    public static void toast(String s) {
        toast(s, Toast.LENGTH_SHORT);
    }

    public static void toastShort(String s) {
        toast(s, Toast.LENGTH_SHORT);
    }

    public static void toastLong(String s) {
        toast(s, Toast.LENGTH_LONG);
    }

    /**
     * 对话框样式的toast
     *
     * @param context
     * @param msg     内容
     * @param ms      时间
     */
    public static ToastDialog dialogToast(Context context, String msg, int ms) {
        return new ToastDialog(context).show(msg, ms);
    }

    private static void toast(String s, int length) {
        try {
            if (toast != null) {
                toast.setText(s);
            } else {
                toast = Toast.makeText(getInstance(), s, length);
            }
            toast.show();
        } catch (Exception e) {
        }
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public DisplayImageOptions getListViewDisplayImageOptions() {
        return listViewDisplayImageOptions;
    }

    public DisplayImageOptions getAdvertisingImageOptions() {
        return advertisingImageOptions;
    }

    public DisplayImageOptions getheadImage() {
//		return headImage;
        return grayHeadImage;
    }

    public DisplayImageOptions getAddImage() {
        return addImage;
    }

    public DisplayImageOptions getBgImage() {
        return bgImage;
    }


    public DisplayImageOptions getBigImageOptions() {
        return bigImageOptions;
    }

    public DisplayImageOptions getHeadOptions() {
        return headOptions;
    }

    public DisplayImageOptions getGroupHeadOptions() {
        return groupHeadOptions;
    }

    public DisplayImageOptions getGrayHeadImage() {
        return grayHeadImage;
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) App.instance.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) App.instance.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }
}
