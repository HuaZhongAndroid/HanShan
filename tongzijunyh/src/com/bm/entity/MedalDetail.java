package com.bm.entity;

import java.io.Serializable;

/**
 * 勋章详情
 * @author wanghy
 *
 */
public class MedalDetail implements Serializable{
    private static final long serialVersionUID = -7819498102619483872L;
    
    public String topImage;
    /**
     * 
     * 课程id
     * 
     */
    public String baseId;
    
    /**
     * 
     * 课程名称
     * 
     */
    public String name;
    
    /**
     * 
     * 课程图片
     * 
     */
    public String baseImage;
    
    
    /**
     * 门店id
     * 
     */
    public String storeId;
    
    /**
     * 
     * 门店名称
     */
    public String storeName;
    
    
    /**
     * 门店地址/上课地址   如果城市营地，就是上课地址。否则就是门店地址
     * 
     */
    public String address;
    /**
     * 价格
     */
    public String price;
    /**
     *类型   1 城市营地 2 暑期大露营 3 户外俱乐部
     */
    public String type;
    /**
     *教练id
     */
    public String coachId;
    /**
     *教练名字
     */
    public String coachName;
    /**
     *上课开始时间
     */
    public String startTime;
    /**
     *上课结束时间
     */
    public String endTime;
    
}
