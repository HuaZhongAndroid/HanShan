package com.bm.entity;

import java.io.Serializable;
import java.util.List;


/**
 * 课程信息
 */
public class Course implements Serializable{
    private static final long serialVersionUID = -7819498102619483872L;
    
    /**
    *
    *课程id
    */
   public String goodsId;

   public String goodsBaseId; //基础课程id

   public String goodsStatus; //状态  1：预约；2：评价；3：完成；0：无显示按钮（未购买）

   /**
    *
    *课程名称
    */
   public String goodsName;
   public String name;
   /**
    *
    *课程图片
    */
   public String titleMultiUrl;

    /**
     * 支付状态 0未支付 1已支付
     */
   public String payStatus;
   /**
    * 状态
    */
   public String state;
   /**
    *
    *门店id
    */
   public String storeId;
   /**
    *
    *门店名称/地址
    */
   public String storeName;

   /**
    *课程介绍
    */
   public String coursePoints;

   /**
    *注意事项
    */
   public String notice;

   /**
    *
    */
   public String address;
   /**
    *
    *课程价格
    */
   public String goodsPrice;

   /**
    * 课程名额
    */
   public int goodsQuota;

   /**
    * 已报名人数
    */
   public int enrollQuota;

   /**
    *课程类型 1闹腾 2乐园 3周末 4大露营
    */
   public String goodsType;

   /**
    *是否购买 1是 0否
    */
   public String isBought;
   /**
    *是否收藏 1是 0否
    */
   public String isCollected;
   /**
    *是否需要交通费 1是 0否
    */
   public String isNeedTrafficFee;
   public String isTraffic;
   public String evaluateuserid;

    /**
     * 订单id
     */
   public String orderId;
   /**
    * 订单号
    */
   public String orderNumber;

   /**
    *已报名孩子列表
    */
   public List<Baby> babyList;

   /**
    */
   public String suitableAge;
   /**
    *教练id
    */
   public String coachId;
   /**
    *
    */
   public String coachName;
   /**
    *
    *上课开始时间   2016.01.12 14：22
    */
   public String startTime;
   /**
    *
    *上课结束时间   2016.01.12 14：22
    */
   public String endTime;


   public String goodsTime;//课程时间（显示用这个）

    /**
     * 是否需要评论 1是 0否（0已评论）
     */
   public String isComment;
   /**
    *
    *
    */
   public String coachAvatar;
   /**
    *
    *
    */
   public String coachAge;
   /**
    *
    *
    */
   public String babyId;
   /**
    *
    *
    */
   public String babyName;
   /**
    *
    *
    */
   public String babyAvatar;
   
   /**
    * 课程状态   1 未开始  2 进行中  3 已结束  4 待付款
    */
   public String classStatus;
   
   /**
    *
    *
    */
//   public String baby;
   /**
    *
    *
    */
//   public String storeInfo;

    
    
}
