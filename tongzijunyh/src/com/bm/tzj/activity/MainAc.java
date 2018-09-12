package com.bm.tzj.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bm.api.BaseApi;
import com.bm.api.MessageManager;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureFragmentActivity;
import com.bm.dialog.AddBodyDialog;
import com.bm.dialog.GuanggaoDialog;
import com.bm.dialog.UtilDialog;
import com.bm.dialog.YouhuiquanDialog;
import com.bm.entity.Adverts;
import com.bm.entity.Child;
import com.bm.entity.User;
import com.bm.entity.XiaoxiList;
import com.bm.entity.Youhuiquan;
import com.bm.im.api.ImApi;
import com.bm.im.tool.IMTool;
import com.bm.share.ShareUtil;
import com.bm.tzj.city.City;
import com.bm.tzj.fm.CourseFm3;
import com.bm.tzj.fm.GrowUpFragment;
import com.bm.tzj.fm.MineFm;
import com.bm.tzj.fm.PullulateFm;
import com.bm.tzj.fm.XiaoxiFm;
import com.bm.tzj.mine.AccountBalanceAc;
import com.bm.tzj.mine.LoginAc;
import com.bm.tzj.mine.SettingAc;
import com.bm.util.CacheUtil;
import com.bm.view.TabMyView;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.lib.widget.BadgeView;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 主页
 *
 * @author Administrator
 */
@SuppressLint("NewApi")

public class MainAc extends BaseCaptureFragmentActivity implements OnClickListener/*, EMEventListener*/ {

    static final String TAG = MainAc.class.getSimpleName();

    private CourseFm3 indexFm;  //发现
    private XiaoxiFm messageFm; //消息
    private MineFm mineFm; //我的
    private GrowUpFragment growUpFragment;//成长中心重做
//	private PullulateFm pullulateFm;

//	private View indexLayout;
//	private View messageLayout;
//	private View findLayout;
//	private View meLayout;


//	private View pullulateLayout;

//	private ImageView iv_a;
//	private ImageView iv_b;
//	private ImageView iv_c;
//	private ImageView iv_d;
//	private ImageView iv_e;


    private TabMyView[] tabs = new TabMyView[4];

    //private ImageView img_read;
    private FragmentManager fragmentManager;

    private Context context;
    public static MainAc intance = null;
    public static int isDefult = 0;//是否是第一次默认加载

    private Handler mHandler = new Handler();

    /**
     * 每次启动程序 是否是第一次定位
     */
    //	private boolean isFirstLocation = true;

    //判断右上角的位置
    private int position = -1;
    //判断是用户登录还是游客登陆
    // public int tag = -1;//以前是用来判断游客 现在改成 判断 user是否为null
    public ShareUtil share;

    //	private TextView tv_yqcdqr;
    public static int isLoginTag = 0;
    BadgeView badge = null;

    private Timer popTimer; //首页弹窗任务计时器
    private int popTimerPause; //中断标识
    private TimerTask popTask = new TimerTask() {
        private int step = 0;

        @Override
        public void run() {
            if (popTimerPause > 0)
                return;
            step++;
            switch (step) {
                case 1:
                    Log.d("fff", "step1");

                    handler.sendEmptyMessage(11);
                    break;
                case 2:
                    Log.d("fff", "step3");
                    handler.sendEmptyMessage(22);
                    break;
                case 3:
                    Log.d("fff", "step2");
                    handler.sendEmptyMessage(33);

                    break;
                case 4:
                    Log.d("fff", "step4");
                    this.cancel(); //最后一步时终止定时器
                    break;
            }
        }
    };

    private void popAddChildDialog() {
        popTimerPause++; //中断标识+1
        Calendar c = Calendar.getInstance();//
        final int day = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int lodDay = SharedPreferencesHelper.getInt("LastTimeToShow");
        Child child = App.getInstance().getChild();
        if (App.getInstance().getUser()!=null&&child == null && day != lodDay) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    AddBodyDialog dialog = new AddBodyDialog((Activity) context);
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Log.d("ffff", "AddBodyDialog onDismiss");
                            popTimerPause--; //取消中断
                            SharedPreferencesHelper.saveInt("LastTimeToShow", day);
                        }
                    });
                }
            });
        } else {
            popTimerPause--; //取消中断
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        popTimer = new Timer(); //首页弹窗任务计时器
        popTimerPause = 0; //中断标识
        popTimer.schedule(popTask, 1000, 2000); //弹窗任务执行间隔


        // Util.isCreateRootPath(this);
        contentView(R.layout.ac_mian);

        share = new ShareUtil(this);
        context = this;
        // tag = getIntent().getIntExtra("tag", 0);
        intance = this;
        rl_top.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        initView();

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        rl_top.setVisibility(View.GONE);
        hideLeft();
        //拉去消息界面的消息 提前判断是否显示红点
        getMessList();

    }

    @Override
    protected void onDestroy() {
        popTimer.cancel();
        popTimer = null;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        popTimerPause--; //取消中断
    }

    @Override
    protected void onStop() {
        popTimerPause++; //中断标识+1
        //EMChatManager.getInstance().unregisterEventListener(this);
        super.onStop();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int i = intent.getIntExtra("TabSelection", 0);
        setTabSelection(i);
    }

    /**
     * 加载广告图片
     */
    private void loadGg() {
        popTimerPause++; //中断标识+1
        String strJson = SharedPreferencesHelper.getString("sysAdvertsList", null);
        if (strJson != null) {
            CommonListResult<Adverts> result = AsyncHttpHelp.getGson().fromJson(strJson, new ServiceCallback<CommonListResult<Adverts>>() {
                @Override
                public void done(int what, CommonListResult<Adverts> obj) {
                }

                @Override
                public void error(String msg) {
                }
            }.type);
            //判断是否有广告，其实是判断是否调用过优惠券的方法
            boolean hasGuangGao = false;
            for (Adverts ad : result.data) {
                if (ad.type.equals("20"))//有广告先处理广告
                {

                    if ((ad.imageUrl + ad.linkUrl).equals(SharedPreferencesHelper.getString("adverts"))) {//如果两个广告相同，不弹了，直接交给下一步弹窗逻辑
                        popTimerPause--;
                        hasGuangGao = true;
                        break;

                    } else {//如果两个广告不同，弹出来。并且保存新广告
                        hasGuangGao = true;
                        SharedPreferencesHelper.saveString("adverts", (ad.imageUrl + ad.linkUrl));
                        GuanggaoDialog dialog = new GuanggaoDialog(MainAc.this);
                        dialog.setAd(ad);
                        dialog.setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                popTimerPause--;
                            }
                        });
                    }

                }
            }

            //循环走完了，但仍然没有广告，那就去走下一步
            if (!hasGuangGao) {
                popTimerPause--;
            }

        }


    }

    /**
     * 获取优惠券
     */
    private void getYouhuiquan() {
        popTimerPause++; //中断标识+1
        User uInfo = App.getInstance().getUser();
        if (null == uInfo) {
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", uInfo.userid);
        AsyncHttpHelp.httpGet(context, BaseApi.API_gainLoginCoupon, map, new ServiceCallback<CommonListResult<Youhuiquan>>() {
            @Override
            public void done(int what, CommonListResult<Youhuiquan> obj) {
                if (obj != null) {
                    if (obj.data.get(0).pkid.equals(SharedPreferencesHelper.getString("Youhuiquan"))) {//pkid相同，不弹了，直接返回
                        popTimerPause--;
                        return;
                    } else {//如果pkid不同，存新的，并且弹出
                        SharedPreferencesHelper.saveString("Youhuiquan", obj.data.get(0).pkid);
                        YouhuiquanDialog dialog = new YouhuiquanDialog((Activity) context);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                popTimerPause--;
                            }
                        });
                    }

                    //					MainAc.intance.getUserInfo();
                } else {
                    popTimerPause--;
                }
            }

            @Override
            public void error(String msg) {
                popTimerPause--;
            }
        });
        ;
    }

    private void initView() {
        //		tv_yqcdqr=(TextView)findViewById(R.id.tv_yqcdqr);
//		indexLayout = findViewById(R.id.indexLayout);
//		messageLayout = findViewById(R.id.messageLayout);
//		findLayout = findViewById(R.id.findLayout);
//		meLayout = findViewById(R.id.meLayout);


//		pullulateLayout=findViewById(R.id.pullulateLayout);


//		iv_a = (ImageView) findViewById(R.id.iv_a);
//		iv_b = (ImageView) findViewById(R.id.iv_b);
//		iv_c = (ImageView) findViewById(R.id.iv_c);
//		iv_d = (ImageView) findViewById(R.id.iv_d);
//		iv_e = (ImageView) findViewById(R.id.iv_e);

        //img_read = (ImageView) findViewById(R.id.img_read);

        LinearLayout ll = (LinearLayout) findViewById(R.id.tabs_ll);

        String[] txts = {"发现", "成长", "消息", "我的"};
        int[] redIdN = {R.drawable.ic_one_n, R.drawable.ic_two_n, R.drawable.ic_three_n, R.drawable.ic_four_n};
        int[] redIdP = {R.drawable.ic_one_p, R.drawable.ic_two_p, R.drawable.ic_three_p, R.drawable.ic_four_p};

        for (int i = 0; i < ll.getChildCount(); i++) {
            TabMyView tab = ((TabMyView) ll.getChildAt(i));
            tabs[i] = tab;
            tab.setValue(txts[i], redIdN[i], redIdP[i]);
            tabs[i].setOnClickListener(this);
            tabs[i].setId(770 + i);
            // tab.setColor(Color.parseColor("#a2a2a2"),Color.parseColor("#a2953e"));
        }
//		pullulateLayout = tabs[1];

//		indexLayout.setOnClickListener(this);
//		messageLayout.setOnClickListener(this);
//		findLayout.setOnClickListener(this);
//		meLayout.setOnClickListener(this);


//		pullulateLayout.setOnClickListener(this);
        badge = new BadgeView(context, tabs[1]);
//		badge = new BadgeView(context, messageLayout);
        badge.setBackgroundResource(R.drawable.dot_red);
        //		loacationInfo();

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {

//		iv_a.setImageResource(R.drawable.m_3_h);
//		iv_b.setImageResource(R.drawable.m_2_h);
//		iv_c.setImageResource(R.drawable.m_4_h);
//		iv_d.setImageResource(R.drawable.m_5_h);
//		iv_e.setImageResource(R.drawable.m_1_h);

        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setSelect(false);
        }

//		indexLayout.setBackgroundResource(R.color.transparent);
//		messageLayout.setBackgroundResource(R.color.transparent);
//		findLayout.setBackgroundResource(R.color.transparent);
//		meLayout.setBackgroundResource(R.color.transparent);

//		pullulateLayout.setBackgroundResource(R.color.transparent);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (indexFm != null) {
            transaction.hide(indexFm);
        }
        if (messageFm != null) {
            transaction.hide(messageFm);
        }
        if (mineFm != null) {
            transaction.hide(mineFm);
        }
        if (growUpFragment != null) {
            transaction.hide(growUpFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 770:
                rl_top.setVisibility(View.GONE);
                setTabSelection(0);
                setTitleName("发现");
                hideLeft();
                position = 0;
                break;


            case 770 + 1:
                setTabSelection(1);
                rl_top.setVisibility(View.GONE);
                setTitleName("成长中心");
                setRightName("");
                position = 1;
                hideLeft();
                break;
            case 770 + 2:
                setTabSelection(2);
                rl_top.setVisibility(View.GONE);
                position = 2;
                break;
            case 770 + 3:
                rl_top.setVisibility(View.GONE);
                setTabSelection(3);
                setTitleName("我的");
                setRightName("设置");
                hideLeft();
                position = 3;
                break;
            default:
                break;
        }

    }

    /**
     * 换tab
     */
    public void changeTab(int index) {
        if (0 == index) {
            setTabSelection(0);
            setTitleName("成长中心");
            setRightName("");
            rl_top.setVisibility(View.GONE);
            position = 0;
            hideLeft();
        }
    }

    @Override
    public void clickRight() {
        super.clickRight();
        //		if(position==3){
        //			openActivity(SendContentAc.class);
        //		}else
        if (position == 4) {
            Intent intent = new Intent(context, SettingAc.class);
            startActivity(intent);
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                tabs[0].setSelect(true);
                if (indexFm == null) {
                    indexFm = new CourseFm3();
                    transaction.add(R.id.content, indexFm);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(indexFm);
                    indexFm.setFoucs();
                    indexFm.updateView();
                }

                break;
            case 1:
                tabs[1].setSelect(true);
//				iv_e.setImageResource(R.drawable.m_1);
                // indexLayout.setBackgroundResource(R.drawable.radio_black_btn);
                if (growUpFragment == null) {
                    growUpFragment = new GrowUpFragment();
                    transaction.add(R.id.content, growUpFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(growUpFragment);
//					GrowUpFragment.updateView();
                }
                break;
            case 2:
                tabs[2].setSelect(true);
                if (messageFm == null) {
                    messageFm = new XiaoxiFm();
                    transaction.add(R.id.content, messageFm);
                } else {
                    transaction.show(messageFm);
                }
                break;
            case 3:
                tabs[3].setSelect(true);
                if (mineFm == null) {
                    mineFm = new MineFm();
                    transaction.add(R.id.content, mineFm);
                } else {
                    transaction.show(mineFm);
                    mineFm.setFoucs();
                    mineFm.initData();
                }
                break;
            default:
                break;
        }
        transaction.commit();

    }

    public boolean isLogins() {
        User user = App.getInstance().getUser();
        if (user == null) {
            return false;
        }
        return true;
    }

    public boolean isLogin() {
        User user = App.getInstance().getUser();
        if (user == null) {
            UtilDialog.dialogTwoBtnContentTtile(context,
                    "您还未登录，请先登录在操作",
                    "取消",
                    "确定",
                    "提示",
                    handler, 1);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }


    // 拍照
    public void takep() {
        takePhoto();
    }

    // 获取照片
    public void pickp() {
        pickPhoto();
    }

    @Override
    protected void onPhotoTaked(String photoPath) {
        if (null != mineFm || position == 4) {
            mineFm.uploadListImg.add(photoPath);
            mineFm.setImage();
        }
        if (null != growUpFragment || position == 0) {
            growUpFragment.setbg(photoPath);
        }
    }

    /**
     * msg = wat = 0  login
     * <p>
     * msg= wat = 2   user refush
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 11:
                    popAddChildDialog();
                    break;
                case 22:
                    loadGg();
                    break;
                case 33:
                    getYouhuiquan();
                    break;
                case 1:
                    finish();
                    Intent intent = new Intent(context, LoginAc.class);
                    startActivity(intent);
                    break;
                case 14:
                    setTabSelection(0);
                    rl_top.setVisibility(View.GONE);
                    hideLeft();
                    position = 2;
                    break;
            }


        }

        ;
    };

    public void toast(String msg) {
        App.toast(msg);
    }


//    /**
//     * 监听事件
//     */
//    @Override
//    public void onEvent(EMNotifierEvent event) {
//        switch (event.getEvent()) {
//            case EventNewMessage: // 普通消息
//            {
//                EMMessage message = (EMMessage) event.getData();
//                /**
//                 *
//                 * handler  message  wat = 2
//                 */
//                ImApi.getAppSys(context, message.getUserName(), handler);
//                // 提示新消息
//                DemoHelper.getInstance().getNotifier().onNewMsg(message);
//
//                refreshUIWithMessage();
//                break;
//            }
//
//            case EventOfflineMessage: {
//                refreshUIWithMessage();
//                break;
//            }
//
//            case EventConversationListChanged: {
//                refreshUIWithMessage();
//                break;
//            }
//
//            default:
//                break;
//        }
//    }


    //	/**
    //	 * 重新登录则定位一次
    //	 */
    //	 private void isLoginLocatin(){
    //		 if(isLoginTag ==1){
    //			 loacationInfo();
    //			 isLoginTag = 0;
    //		 }
    //
    //
    //	 }

    /**
     * 获取是否有新消息
     */
    private void getMessageSt() {

    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        //当登录后刷新进入首页
        User user = App.getInstance().getUser();
        HashMap<String, String> map = new HashMap<String, String>();
        if (user != null) {
            map.put("userId", user.userid);
            UserManager.getInstance().getTzjcasGetUserInfo(context, map, new ServiceCallback<CommonResult<User>>() {

                @Override
                public void error(String msg) {
                    MainAc.intance.toast(msg);
                }

                @Override
                public void done(int what, CommonResult<User> obj) {
                    if (obj != null && obj.data != null) {
                        App.getInstance().setUser(obj.data);// 存储用户信息

                        //						if(App.getInstance().getCityCode()!=null){
                        //							Util.setPushTag(context, App.getInstance().getCityCode().cityId+"", obj.data.communityId);
                        //						}
                        IMTool.loginIM(handler);  //handler  message  wat = 2
                        ImApi.getAppSys(context, App.getInstance().getUser().userid, handler);

                        //刷新个人中心的数据
                        if (MineFm.instance != null) {
                            MineFm.instance.hideOrView();
                        }

                        if (PullulateFm.instance != null) {
                            PullulateFm.instance.getDate();
                        }

                        if (null != AccountBalanceAc.intance) {
                            AccountBalanceAc.intance.getRefreshData();
                        }

                        if (obj.data.messageCount != null && !"".equals(obj.data.messageCount)) {
                            if (Integer.parseInt(obj.data.messageCount) > 0) {
                                //   img_read.setVisibility(View.VISIBLE);
//								if(MineFm.instance!=null){
//									MineFm.instance.img_read.setVisibility(View.VISIBLE);
//								}
                            } else {
                                // img_read.setVisibility(View.GONE);
//								if(MineFm.instance!=null){
//									MineFm.instance.img_read.setVisibility(View.GONE);
//								}
                            }
                        }

                    } else {
                        dialogToast("获取数据失败");
                        MainAc.intance.toast("获取数据失败");
                    }

                }
            });
        }
    }


//    /**
//     * 获取未读消息数
//     *
//     * @return
//     */
//    public int getUnreadMsgCountTotal() {
//        int unreadMsgCountTotal = 0;
//        int chatroomUnreadMsgCount = 0;
//        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
//        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
//            if (conversation.getType() == EMConversationType.ChatRoom)
//                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
//        }
//        return unreadMsgCountTotal - chatroomUnreadMsgCount;
//    }
//
//    private void refreshUIWithMessage() {
//        runOnUiThread(new Runnable() {
//            public void run() {
//                // 刷新bottom bar消息未读数
//                int count = getUnreadMsgCountTotal();
//                if (count > 0) {
//                    badge.setText("" + count);
//                    badge.setGravity(Gravity.CENTER);
//                    badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//                    badge.show();
//                } else {
//                    badge.hide();
//                }
//                // 当前页面如果为聊天历史页面，刷新此页面
//                if (messageFm != null) {
////					messageFm.refresh();
//                }
//            }
//        });
//    }


    private OnTabActivityResultListener onTabActivityResultListener;

    public void setOnTabActivityResultListener(
            OnTabActivityResultListener onTabActivityResultListener) {
        this.onTabActivityResultListener = onTabActivityResultListener;
    }

    public interface OnTabActivityResultListener {
        public void onTabActivityResult(int requestCode, int resultCode, Intent data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent mIntent) {
        super.onActivityResult(requestCode, resultCode, mIntent);
        if (onTabActivityResultListener != null)
            onTabActivityResultListener.onTabActivityResult(requestCode, resultCode, mIntent);
    }


    public void getMessList() {
        User user = App.getInstance().getUser();
        if (user == null) return;
        final City city = App.getInstance().getCityCode();
        if (city == null) return;
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("regionName", city.regionName);//城市名称
        map.put("userId", user.userid);//用户id

        MessageManager.getInstance().getMessageList(this, map, new ServiceCallback<CommonResult<XiaoxiList>>() {

            final String CACHEKEY = "xiaoxiFm_message_list_cache";

            @Override
            public void done(int what, CommonResult<XiaoxiList> obj) {
                hideProgressDialog();
                isShowRed(obj);
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                //从缓存中读取数据
                CommonResult<XiaoxiList> obj = new CommonResult<XiaoxiList>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonResult<XiaoxiList>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    isShowRed(obj);
                    return;
                }
                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            public void isShowRed(CommonResult<XiaoxiList> obj) {
                boolean isRead = false;
                if (obj == null && obj.data == null) {
                    showRed(isRead);
                    return;
                }
                if (obj.data.getAppraise() != null && obj.data.getAppraise().size() >0) {
                    for (XiaoxiList.AppraiseBean appraiseBean : obj.data.getAppraise()) {
                        if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                            isRead = true;
                            break;
                        }
                    }

                }
                if (obj.data.getMessageAll()!= null && obj.data.getMessageAll().size() >0) {
                    for (XiaoxiList.MessageAllBean appraiseBean : obj.data.getMessageAll()) {
                        if (appraiseBean.getIsRead().equalsIgnoreCase("0")) {
                            isRead = true;
                            break;
                        }
                    }
                }
                showRed(isRead);
            }
        });
    }

    /**
     * 消息显示红点
     * @param show
     */
    public void showRed(boolean show) {
        tabs[2].showRed(show);
    }
}
