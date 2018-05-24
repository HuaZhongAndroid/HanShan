package com.bm.tzj.city;

import java.io.Serializable;

public class City implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4222062062025279071L;
	public String name;
	public String pinyi;

	public String regionName;
	public String regionId;
	public String category;
	public String menuids;

	public City(String name, String pinyi) {
		super();
		this.name = name;
		this.pinyi = pinyi;
	}
	public City(String name, String regionId,String pinyi) {
		super();
		this.name = name;
		this.regionId = regionId;
	}
//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}
	
//	public String lat; //瞎jib定义
//	public String lng;
//	/**
//	 * 城市名称
//	 */
//	public String cityName;
	public String address;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

//	public String getRegionName() {
//		return regionName;
//	}
//
//	public void setRegionName(String regionName) {
//		this.regionName = regionName;
//	}

	public City() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

}
