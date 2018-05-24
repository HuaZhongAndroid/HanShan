package com.bm.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayMateList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -854599645189513140L;

	/**
	 *  家长头像
	 */
	public String avatar;
	/**
	 *  昵称
	 */
	public String nickname;
	/**
	 *  帖子id
	 */
	public String articleId;
	/**
	 *  帖子内容
	 */
	public String articleContent;
	/**
	 *  家长id
	 */
	public String postUserId;
	
	public String babyid;
	
	/**
	 *  帖子创建时间
	 */
	public String postTime;
	/**
	 * 帖子图片
	 */
	public ArrayList<PlayMateList> path;
	/**
	 *  点赞数量
	 */
	public String praiseCount;
	/**
	 *  回复数量
	 */
	public String replyCount;
	
	public String postDate;
	
	
	
	public ArrayList<PlayMateList> praiseUserName;
	/**
	 * 点赞人列表
	 */
	public String userid;
//	public String ctime;
	public String nickName;
	
	/**
	 * 点赞状态   0未赞  1已赞
	 */
	public String isArticleLaud;
	
	public String titleMultiUrl;
}
