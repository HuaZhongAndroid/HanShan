package com.bm.entity;


/**
 * 
 * 版本信息
 * 
 * @author wangqiang
 *
 */
public class Version {

	/**
	 * 版本号
	 */
	public String vCode = "";

	/**
	 * 版本名称
	 */
	public String vName;

	/**
	 * 是否强制更新   1 强制更新   0 非强制
	 */
	public String isMustUpdate = "";
	
	/**
	 * 安装路径
	 */
	public String filepath = "";

	/**
	 * 版本详情
	 */
	public String content;

}
