package com.bm.entity;

import java.io.Serializable;

/**
 * 教练评价
 * @author wanghy
 *
 */
public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4030862251398509961L;

	/**
	 * 评价人id
	 */
	public String replyUserId = "";

	/**
	 * 评价人名称
	 */
	public String replyUserName;

	/**
	 * 评价人头像
	 */
	public String replyAvatar = "";
	/**
	 * 评价时间
	 */
	public String replyTime = "";

	/**
	 * 评价内容
	 */
	public String replyContent;

}
