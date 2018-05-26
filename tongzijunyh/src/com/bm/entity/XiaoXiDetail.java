package com.bm.entity;

import java.io.Serializable;

public class XiaoXiDetail implements Serializable
{


    /**
     * messageId : 413
     * title : 兄弟情深
     * sendObj : 4
     * gender : null
     * suitableAge : null
     * content : null
     * titleMultiUrl : null
     * ctime : 1486720725000
     * mtime : null
     * regionId : null
     * thinContent : 您参与的课程兄弟情深已被教练评价完成。是否通过：通过,教练评语: 3343131331
     * goodsId : null
     * userId : null
     * isRecommend : null
     */

    private int messageId;
    private String title;
    private int sendObj;
    private Object gender;
    private Object suitableAge;
    private String content;
    private Object titleMultiUrl;
    private long ctime;
    private Object mtime;
    private Object regionId;
    private String thinContent;
    private Object goodsId;
    private Object userId;
    private Object isRecommend;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSendObj() {
        return sendObj;
    }

    public void setSendObj(int sendObj) {
        this.sendObj = sendObj;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getSuitableAge() {
        return suitableAge;
    }

    public void setSuitableAge(Object suitableAge) {
        this.suitableAge = suitableAge;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getTitleMultiUrl() {
        return titleMultiUrl;
    }

    public void setTitleMultiUrl(Object titleMultiUrl) {
        this.titleMultiUrl = titleMultiUrl;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public Object getMtime() {
        return mtime;
    }

    public void setMtime(Object mtime) {
        this.mtime = mtime;
    }

    public Object getRegionId() {
        return regionId;
    }

    public void setRegionId(Object regionId) {
        this.regionId = regionId;
    }

    public String getThinContent() {
        return thinContent;
    }

    public void setThinContent(String thinContent) {
        this.thinContent = thinContent;
    }

    public Object getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Object goodsId) {
        this.goodsId = goodsId;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Object isRecommend) {
        this.isRecommend = isRecommend;
    }
}
