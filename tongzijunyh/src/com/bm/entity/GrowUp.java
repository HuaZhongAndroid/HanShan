package com.bm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */
public class GrowUp implements Serializable {

    public String content;
    public String storeName;
    public String coachName;
    public String coachId;
    public String userId;
    public String babyId;
    public String goodsId;
    public String storeId;
    public String goodsType;
    public String goodsName;


    public String pkid;

    public String ctime;
    public String recordFlag;

    public List<GrowUpImg> attachmentlist;
}
