package com.bm.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 教练信息
 *
 */
public class CoachInfo implements Serializable{
    private static final long serialVersionUID = -7819498102619483872L;
    
    /**
     *教练ID
     */
    public String coachId;
    /**
     *教练名称
     */
    public String coachName;
    /**
     * 
     * 教练年龄
     * 
     */
    public String coachAge;
    
    
    /**
     * 教练的评价
     * 
     */
    public String coachLogistics;
    
    /**
     * 
     * 教练简介
     */
    public String coachProfile;
    
    /**
     *教练头像
     */
    public String avatar;
    
    /**
     * 课程信息
     */
    public List<HotGoods> goodsInfo;
    
    /**
     * 评论信息
     */
    public List<Comment> comment;
    
    /**
     * 是不是我的绑定教练  0 不是  1 是
     */
    public String isBind;
    
    /**
     * 证书
     */
    public List<CoachDiploma>  coachDiploma;
    
    
}
