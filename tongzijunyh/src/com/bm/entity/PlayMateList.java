package com.bm.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayMateList implements Serializable{
	private static final long serialVersionUID = -854599645189513140L;
	public String avatar;//家长头像
	public String nickname; //昵称
	public String articleId;//帖子id
	public String articleContent;// 帖子内容
	public String postUserId;//家长id
	public String babyid;
	public String postTime;//帖子创建时间
	public ArrayList<PlayMateList> path;//帖子图片
	public String praiseCount;// 点赞数量
	public String replyCount;//回复数量
	public String postDate;
	public ArrayList<PlayMateList> praiseUserName;
	public String userid;//点赞人列表
//	public String ctime;
	public String nickName;
	public String isArticleLaud;//点赞状态   0未赞  1已赞
	public String titleMultiUrl;
}
