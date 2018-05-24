package com.bm.entity.post;

import com.bm.base.BasePostEntity;


/**
 * 
 * 提交用户信息
 * @author wangqiang
 *
 */
public class UserPost extends BasePostEntity{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String userId;
	public String coachId;//教练ID
	public String coachName;//教练名称

	public String babyName;//宝贝名称
	public String babyGender;//宝贝性别
	public String titleMultiUrl;
	public String phone;
	public String nickName;//昵称
	public String nickname;//昵称
	public String gender;//性别
	public String address;//地址
	public String birthday;//出生日期
	public String password;//密码
	public String regionName;//城市名称
	public String areaName;//区域名称
	public String provinceName;//省
	
	public String realName;
	public String openId;//第三方openId
	public String loginType;//第三方登录类型  1 QQ  2 微博登录  3 微信登录
	
	public String sendPhone;
	
	public String inviteCode;  //注册推荐码
}
