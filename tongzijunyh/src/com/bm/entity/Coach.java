package com.bm.entity;

import java.io.Serializable;

public class Coach implements Serializable {
	
	private String coachId;
	
	private String coachName;
	
	private String  avatar;
	private String  coachAge;
	/**
	 * 评价星级
	 */
	private String  coachLogistics;
	/**
	 * 评价
	 */
	private String  coachProfile;
	/**
	 * 是否绑定
	 * 当前用户是否绑定该教练 0未绑定 1已绑定
	 */
	private String  isBinding;
	/**
	 * 性别
	 * 1 男  2 女  0 未知
	 */
	private String  gender;
	public String getCoachId() {
		return coachId;
	}
	public void setCoachId(String coachId) {
		this.coachId = coachId;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCoachAge() {
		return coachAge;
	}
	public void setCoachAge(String coachAge) {
		this.coachAge = coachAge;
	}
	public String getCoachLogistics() {
		return coachLogistics;
	}
	public void setCoachLogistics(String coachLogistics) {
		this.coachLogistics = coachLogistics;
	}
	public String getCoachProfile() {
		return coachProfile;
	}
	public void setCoachProfile(String coachProfile) {
		this.coachProfile = coachProfile;
	}
	public String getIsBinding() {
		return isBinding;
	}
	public void setIsBinding(String isBinding) {
		this.isBinding = isBinding;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
