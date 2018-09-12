package com.bm.tzj.fm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.dialog.UtilDialog;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.AccountBalanceAc;
import com.bm.tzj.mine.FeedBackAc;
import com.bm.tzj.mine.LoginAc;
import com.bm.tzj.mine.MyChildrenAc;
import com.bm.tzj.mine.MyCollectionAc;
import com.bm.tzj.mine.MyCourseAc;
import com.bm.tzj.mine.MyCoursebaoAc;
import com.bm.tzj.mine.MyMessageAc;
import com.bm.tzj.mine.MyTeachersAc;
import com.bm.tzj.mine.MyYouhuiquanAc;
import com.bm.tzj.mine.PersonalInformation;
import com.bm.tzj.mine.RechargeAc2;
import com.bm.tzj.mine.SettingAc;
import com.bm.util.BitmapUtil;
import com.bm.view.CircleImageView;
import com.lib.widget.ReboundScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的
 *
 * @author wangqiang
 */
@SuppressLint("NewApi")
public class MineFm extends Fragment implements OnClickListener {
    private Context context;
    private ReboundScrollView sv;
    private CircleImageView iv_sixview_head;
    private ImageView iv_headbg;
    private TextView tv_money, tv_moneyX, tv_zengsong, tv_zengsongX, tv_login, tv_allCourse, tv_recharge;
    private TextView tv_integral, tv_name, tv_message, tv_right;
    private ViewGroup ll_person, ll_accountBalance, ll_zengsong, ll_zhangdan, ll_integral, ll_wdl, ll_dl;
    private ThreeButtonDialog buttonDialog;
    public List<String> uploadListImg = new ArrayList<String>();
    public LinearLayout ll_e, ll_f, ll_g;
    public LinearLayout ll_course1, ll_course2, ll_course3, ll_course4, ll_course5;
    public static MineFm instance = null;
//	public ImageView img_read;

    private String touxiang;
//	private LinearLayout ll_medal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.fm_mine, container,
                false);
        context = getActivity();
        instance = this;
        initView(messageLayout);
        return messageLayout;

    }


    /**
     * 设置焦点
     */
    public void setFoucs() {
        sv.smoothScrollTo(0, 0);
    }

    private void initView(View view) {
        ll_wdl = (ViewGroup) view.findViewById(R.id.ll_wdl);
        ll_wdl.setOnClickListener(this);
        ll_dl = (ViewGroup) view.findViewById(R.id.ll_dl);
        iv_sixview_head = (CircleImageView) view.findViewById(R.id.iv_sixview_head);
        iv_headbg = (ImageView) view.findViewById(R.id.iv_headbg);
//		img_read = (ImageView)view.findViewById(R.id.img_read);
        //iv_sixview.addBorderResource(5, R.color.bg_blue);
//		iv_sixview.setCornerRadius(10);
//		iv_sixview.setImageResource(R.drawable.testss);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        view.findViewById(R.id.ll_mychildren).setOnClickListener(this);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        tv_moneyX = (TextView) view.findViewById(R.id.tv_moneyX);
        tv_zengsong = (TextView) view.findViewById(R.id.tv_zengsong);
        tv_zengsongX = (TextView) view.findViewById(R.id.tv_zengsongX);
        tv_integral = (TextView) view.findViewById(R.id.tv_integral);
        sv = (ReboundScrollView) view.findViewById(R.id.sv);
        ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
        ll_person.setOnClickListener(this);
        ll_e = (LinearLayout) view.findViewById(R.id.ll_e);
        ll_e.setOnClickListener(this);
        ll_f = (LinearLayout) view.findViewById(R.id.ll_f);
        ll_f.setOnClickListener(this);
        ll_g = (LinearLayout) view.findViewById(R.id.ll_g);
        ll_g.setOnClickListener(this);
        ll_integral = (LinearLayout) view.findViewById(R.id.ll_integral);
        ll_accountBalance = (LinearLayout) view.findViewById(R.id.ll_accountBalance);
        ll_accountBalance.setOnClickListener(this);
        ll_zengsong = (LinearLayout) view.findViewById(R.id.ll_zengsong);
        ll_zengsong.setOnClickListener(this);

        ll_zhangdan = (LinearLayout) view.findViewById(R.id.ll_zhangdan);
        ll_zhangdan.setOnClickListener(this);
        ll_integral.setOnClickListener(this);
        iv_sixview_head.setOnClickListener(this);

        tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_name = (TextView) view.findViewById(R.id.tv_name);

//	    tv_name.setOnClickListener(this);
        tv_message.setOnClickListener(this);

        tv_login = (TextView) view.findViewById(R.id.tv_login);
//	    tv_login.setOnClickListener(this);

        tv_allCourse = (TextView) view.findViewById(R.id.tv_allCourse);
        tv_allCourse.setOnClickListener(this);
        tv_allCourse.setTag(0);
        ll_course1 = (LinearLayout) view.findViewById(R.id.ll_course1);
        ll_course1.setOnClickListener(this);
        ll_course1.setTag(1);
        ll_course2 = (LinearLayout) view.findViewById(R.id.ll_course2);
        ll_course2.setOnClickListener(this);
        ll_course2.setTag(2);
        ll_course3 = (LinearLayout) view.findViewById(R.id.ll_course3);
        ll_course3.setOnClickListener(this);
        ll_course3.setTag(3);
        ll_course4 = (LinearLayout) view.findViewById(R.id.ll_course4);
        ll_course4.setOnClickListener(this);
        ll_course4.setTag(4);
        ll_course5 = (LinearLayout) view.findViewById(R.id.ll_course5);
        ll_course5.setOnClickListener(this);
        ll_course5.setTag(5);


        tv_recharge = (TextView) view.findViewById(R.id.tv_recharge);
        tv_recharge.setOnClickListener(this);
        view.findViewById(R.id.ll_chongzhi).setOnClickListener(this);

        buttonDialog = new ThreeButtonDialog(context).setFirstButtonText("拍照")
                .setBtn1Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        MainAc.intance.takep();
                    }
                }).setThecondButtonText("从手机相册选择")
                .setBtn2Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        MainAc.intance.pickp();
                    }
                }).autoHide();
        initData();
    }

    //判断用户是否登录现实  头像信息或者用户信息
    public void hideOrView() {
        if (App.getInstance().getUser() == null) {//未登录
            ll_wdl.setVisibility(View.VISIBLE);
            ll_dl.setVisibility(View.GONE);
        } else {
            ll_wdl.setVisibility(View.GONE);
            ll_dl.setVisibility(View.VISIBLE);
        }

        initData();
    }

    public void initData() {
        User uInfo = App.getInstance().getUser();
        //判断是否登陆 没登陆 就是游客模式
        if (uInfo != null) {
            String money = uInfo.rechargeBalance == null ? "0.00" : uInfo.rechargeBalance;
//				String money = uInfo.rechargeBalance == null?"0.00":Util.toNumber("0.00",Float.parseFloat(uInfo.rechargeBalance));
            tv_money.setText(money.substring(0, money.indexOf('.')));//余额小数点前面数字
            tv_moneyX.setText(money.substring(money.indexOf('.'), money.length()));//余额小数
            String zengsong = uInfo.giveBalance == null ? "0.00" : uInfo.giveBalance;
//				String zengsong = uInfo.giveBalance == null?"0.00":Util.toNumber("0.00",Float.parseFloat(uInfo.giveBalance));
            tv_zengsong.setText(zengsong.substring(0, zengsong.indexOf('.')));//余额小数点前面数字
            tv_zengsongX.setText(zengsong.substring(zengsong.indexOf('.'), zengsong.length()));//余额小数

            if (null != uInfo.userCouponCount) {//优惠券
                tv_integral.setText(uInfo.userCouponCount);
            }

            if (uInfo.avatar != null && !uInfo.avatar.equals(touxiang)) {
                touxiang = uInfo.avatar;
                ImageLoader.getInstance().displayImage(uInfo.avatar, iv_sixview_head, App.getInstance().getheadImage());
//					ImageLoader.getInstance().displayImage(child.avatar, iv_headbg, App.getInstance().getheadImage());
                iv_headbg.setImageBitmap(null);
                ImageSize mImageSize = new ImageSize(100, 100);
                ImageLoader.getInstance().loadImage(uInfo.avatar, mImageSize, App.getInstance().getheadImage(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
                        if (bm != null)
                            iv_headbg.setImageBitmap(BitmapUtil.fastblur(bm, 100));
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    }

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                    }
                });
            }


            tv_name.setText(uInfo.nickname);
//				String strSex = child.gender;
//				if("1".equals(strSex)){
//					strSex="男";
//				}else if("2".equals(strSex)){
//					strSex="女";
//				}else {
//					strSex="未知";
//				}
//				String strAgeAddress = child.age==""?"0岁":child.age+"岁 ";
//				tv_message.setText(strSex+" "+strAgeAddress);//宝贝性别 年龄

            String address = PullulateFm.getNullData(uInfo.regionName);
            tv_message.setText(address);
        } else {//游客状态
            ll_wdl.setVisibility(View.VISIBLE);
            ll_dl.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View view) {
        if (!MainAc.intance.isLogin()) return;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_mychildren://我的孩子
                intent = new Intent(context, MyChildrenAc.class);
                startActivity(intent);
                break;
            case R.id.tv_allCourse://我的课程
            case R.id.ll_course1://我的课程
            case R.id.ll_course2://我的课程
            case R.id.ll_course3://我的课程
            case R.id.ll_course4://我的课程
                intent = new Intent(context, MyCourseAc.class);
                intent.putExtra(MyCourseAc.INTENTKEY_INDEX, ((Integer) view.getTag()).intValue());
                startActivity(intent);
                break;
            case R.id.ll_course5:
                startActivity(new Intent(context, MyCoursebaoAc.class));
                break;
            case R.id.ll_c://我的收藏
                intent = new Intent(context, MyCollectionAc.class);
                startActivity(intent);
                break;
//		case R.id.ll_medal://我的勋章
//			if (MainAc.intance.tag == 2) {
//				intent = new Intent(context, MyMedal.class);
//				startActivity(intent);
//			} else {
//				isLogin();
//			}
//			break;
            case R.id.ll_zengsong:
            case R.id.ll_zhangdan:
            case R.id.ll_accountBalance://账户明细
                intent = new Intent(context, AccountBalanceAc.class);
                startActivity(intent);
                break;
            case R.id.tv_recharge://充值
            case R.id.ll_chongzhi:
                intent = new Intent(context, RechargeAc2.class);
                startActivity(intent);
                break;
            case R.id.ll_d://我的消息
                intent = new Intent(context, MyMessageAc.class);
                startActivity(intent);
                break;
            case R.id.ll_e:  //顾问教练
//				if(uInfos.coachId == null ||uInfos.coachName == null ){
//					intent = new Intent(context, NoTeacherAc.class);
//					startActivity(intent);
//				}else{
//					intent = new Intent(context,CoachInformationAc.class);
//					intent.putExtra("title", "顾问教练");
//					intent.putExtra("coachId", uInfos.coachId);
//					intent.putExtra("pageType", "MineFm");
//					startActivity(intent);
//				}
                    intent = new Intent(context, MyTeachersAc.class);
                    intent.putExtra("pageType", "NoTeacherAc");
                    startActivity(intent);

                break;
            case R.id.ll_f:  //意见反馈
                intent = new Intent(context, FeedBackAc.class);
                startActivity(intent);
                break;
            case R.id.ll_g:  //设置
                intent = new Intent(context, SettingAc.class);
                startActivity(intent);
                break;
            case R.id.ll_integral:  //积分明细
                Intent i = new Intent(context, MyYouhuiquanAc.class);
                context.startActivity(i);
                break;
            case R.id.ll_person: //个人信息
                intent = new Intent(context, PersonalInformation.class);
                intent.putExtra("pageType", "MineFm");
                startActivity(intent);
                break;
            case R.id.ll_wdl:
                intent = new Intent(context, LoginAc.class);
                startActivity(intent);
                break;
            case R.id.iv_sixview_head:
                intent = new Intent(context, PersonalInformation.class);
                intent.putExtra("pageType", "MineFm");
                startActivity(intent);
                break;
            case R.id.tv_name:
                MainAc.intance.changeTab(0);
                break;
            case R.id.tv_message:
                intent = new Intent(context, PersonalInformation.class);
                intent.putExtra("pageType", "MineFm");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    // 设置图片
    public void setImage() {
        ImageLoader.getInstance().displayImage("file://" + uploadListImg.get(0), iv_sixview_head, App.getInstance().getListViewDisplayImageOptions());
        //sendPics(uploadListImg);
    }
}
