package com.bm.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 推荐课程
 * 
 * @author jiangsh
 * 
 */
public class HotGoods implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2954270580503532849L;
	/**
	 * 经度
	 */
	public String lon;
	/**
	 * 纬度
	 */
	public String lat;
	/**
	 * 课程id
	 */
	public String goodsId;
	public String regionId;
	/**
	 * 课程名称
	 */
	public String goodsName;
	/**
	 * 课程图片
	 */
	public String titleMultiUrl;

	/**
	 * 店面id
	 */
	public String storeId;
	/**
	 * 店面名称
	 */
	public String storeName;
	/**
	 * 店面地址/上课地址
	 */
	public String address;
	/**
	 * 价格
	 */
	public String goodsPrice;
	/**
	 * 类型
	 */
	public String goodsType;
	/**
	 * goodsQuota 总人数/名额 orderNum enrollQuota 已报名人数
	 */
	public String goodsQuota;
	public String enrollQuota;
	public String orderNum;
	/**
	 * 适合年龄段
	 */
	public String suitableAge;
	/**
	 * 上课开始时间
	 */
	public String startTime;
	/**
	 * 上课结束时间
	 */
	public String endTime;

	/**
	 * 教练id
	 */
	public String coachId;
	/**
	 * 教练名字
	 */
	public String coachName;
	/**
	 * 教练头像
	 */
	public String coachAvatar;
	/**
	 * 教练年龄
	 */
	public String coachAge;
	/**
	 * 教练评价0-5 星级
	 */
	public String coachLogistics;
	/**
	 * 课程要点
	 */
	public String coursePoints;
	/**
	 * 注意事项
	 */
	public String notice;
	/**
	 * 已报名宝贝数
	 */
	public String babyCount;
	/**
	 * 是否已买过0 未买 1 已买 2 待支付 如果户外和暑期大露营需要
	 */
	public String isBought;
	/**
	 * 宝贝
	 */
	public List<Baby> baby;

	public List<Baby> babyList;

	public String babyId;
	/**
	 * 门店集合
	 */
	public ArrayList<StoreInfo> storeInfo;

	/**
	 * 课程状态 1 未开始 2 进行中 3 已结束 4 待付款
	 */
	public String classStatus;

	public String orderId;// 订单ID
	public String payMoney;// 订单金额
	public String orderNumber; //订单号

	/**
	 * 是否收藏 未收藏 0 已收藏1
	 */
	public String isCollected;
	
	/**
	 * 课程状态  0：已失效 1：进行中
	 */
	public String courseStatus;
	
	public String baseImage;
	
	/**
	 * 是否评价   0未评价 1已评价
	 */
	public String isEvaluate="";
	
	/**
	 * 体验课程 = 1    晋级课程 = 2 
	 */
	public String goodsCategory="";
	
	public String baseId="";//基础数据ID
}
