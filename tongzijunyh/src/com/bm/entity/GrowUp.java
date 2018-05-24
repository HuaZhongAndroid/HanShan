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
    public String userId;
    public String babyId;
    public String pkid;
    public String ctime;
    public String recordFlag;

    public List<GrowUpImg> attachmentlist;
}
