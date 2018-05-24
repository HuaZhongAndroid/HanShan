package com.bm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 用户数据
 *
 */
public class User implements Serializable{
    private static final long serialVersionUID = -7819498102619483872L;
    
    
    public List<Child> babyList;
    
    //2.0新增属性
    public String rechargeBalance;  //充值余额
    public String giveBalance; //赠送余额
    
    public String userCouponCount; //优惠券数量

    
    public String messageCount; //新消息数量
    
    
    public String recNumber;
    
    public String topImage;
    
    public String pushid; //jpush用
    
    /**
     * 
     * 用户id
     * 
     */
    public String userid;
    /**
     * 该id目前仍然在用。可能是环信相关
     */
    public String userId;
    
    
    public String babyId;
    
    /**
     * 
     * 用户姓名
     * 
     */
    public String username;
    
    /**
     * 
     * 手机号
     * 
     */
    public String phone;
    
    
    /**
     * 密码
     * 
     */
    public String password;
    
    /**
     * 
     * 昵称
     */
    public String nickname;
    
    
    /**
     * 性别  1:男 2：女 0：未知
     * 
     */
    public String gender;
    /**
     * 城市
     */
    public String regionName;
    /**
     *地址
     */
    public String address;
    /**
     *宝贝姓名
     */
    public String babyName;
    /**
     *宝贝性别
     */
    public String babyGender = "";
//    /**
//     *出生日期
//     */
//    public String birthday="2001-02-05";
    /**
     *宝贝出生日期
     */
    public String babyBirthday = "2001-02-05";
    /**
     *教练ID
     */
    public String coachId;
    /**
     *教练名称
     */
    public String coachName;
    /**
     *账户余额
     */
    public String accountMoney;
    /**
     *是否有支付密码   0 没有设置支付密码  1 设置支付密码
     */
    public String paymentPassword;
    /**
     *荣誉榜
     */
    public String honourSort;
    /**
     *已获勋章
     */
    public String medalNum;
    /**
     *用户头像
     */
    public String avatar;
    /**
     *宝贝年龄
     */
    public String babyAge;
    /**
     *用户积分
     */
    public String score;
    
    /**
     *用户积分
     */
    public String userType;
    
    /**
     * 是否有未读消息  
     */
    public String isMessage;
    
    /**
     * 我的排名
     */
    public String sort;
    
    /**
     * 好友信息
     */
    public List<User> friendsBay;
    
    public String areaName;//区域名称
    public String provinceName;//省
    
    /**
     * 是否是我的好友 0不是 1是
     */
    public String isMyFriend;
    
    
    /**
     * 群信息
     */
    /**
     * 群Id
     */
    public String groupId;
    /**
     * 群头像
     */
    public String groupPic;
    /**
     * 群昵称
     */
    public String groupName;
    /**
     * 群英会数量
     */
    public String userCount;
    /**
     * 群用户
     */
    public ArrayList<User> groupUserList;
    /**
     * 0:非好友关系  1:好友关系 3等待同意
     */
    public String friStatus;
    
    public String friendUserId;
    
//    public String isFirst="";//是否是第一次登录 1第一次登录 2第一次绑定
    
    /**
     *  0：注册   1：第二次登录  2：绑定
     */
    public String loginStatus;
    
    
    /**
     * 2.5之后，有了微信登录，新增字段
     * 微信登录
     */
    
    public String wechatId;
    
    public String wechatName;
    
    public String wechatHeadimg;
    /**
     * 成长中心背景图
     */
    public String coverImg;
    

}
