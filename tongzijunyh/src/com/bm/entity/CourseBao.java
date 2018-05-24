package com.bm.entity;

import java.io.Serializable;

/**
 * 课程包
 */
public class CourseBao implements Serializable {
    public String pkid;
    public String isBuy;
    public String name;
    public String money;
    public String imgLink;
    public String beginDate;
    public String endDate;
    public String courseNum;
    public String type;
    public String groupDate;

    public String isComplete;

    public String storeId; //门店id
    public String storeName; //门店名字
}
