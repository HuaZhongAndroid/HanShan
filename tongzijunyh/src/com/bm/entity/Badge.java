package com.bm.entity;

import java.io.Serializable;

public class Badge  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -952765387649897407L;
	public String badgeName;
	public String badgeId;
	
	public String imageUrl;
	
	public int id;
	public int state;
	
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
	 * 勋章ID
	 */
	public String medalId = "";

	/**
	 * 勋章图片路径
	 */
	public String titleMultiUrl;

	/**
	 * 是否点亮   0 未点亮  1 点亮
	 */
	public String isLight = "";
	
	/**
	 * 是否已获得 0: 未获得 1：已获得
	 */
	public String isAcquired = "";
	
	public String medalName="";//勋章名称

	
}
