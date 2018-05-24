package com.bm.entity;

import java.io.Serializable;

//孩子
public class Child implements Serializable {

	public String babyId; //id
	public String birthday; //生日
	public String age; //年龄
	public String gender; //性别 1 男  2 女  0 未知
	public String realName; //孩子姓名
	
	public String avatar; //头像

}
