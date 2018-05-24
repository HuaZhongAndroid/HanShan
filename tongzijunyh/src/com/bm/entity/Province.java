package com.bm.entity;

import java.util.List;

/**
 * 
 * 省市区
 * 
 * @author wangqiang
 *
 */
public class Province {

	public String regionId = "";

	public String regionName;

	public String parentRegionId = "";
	public String sortOrder = "";

	public String grade;
	public List<Province> listCity;
	public List<Province> listArea;

}
