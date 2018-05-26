package com.bm.entity;

import java.util.List;

/**
 * 消息列表界面
 */
public class XiaoxiList {


    /**
     * title : 邀请朋友，优惠多多！
     * titleMultiUrl : http://softlst.com:8888/img/message/mesimage/37ead498-dddd-4840-8cb2-d799a312d280.png
     * ctime : 05-19
     * messageId : 536
     * content : <p>邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！</p>
     * <p>
     * thinContent :
     * isRead : 1
     */

    private List<MessageRecoBean> messageReco;
    /**
     * title : 邀请朋友，优惠多多！
     * titleMultiUrl : http://softlst.com:8888/img/message/mesimage/37ead498-dddd-4840-8cb2-d799a312d280.png
     * ctime : 05-19
     * messageId : 536
     * content : <p>邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！邀请朋友，优惠多多！</p>
     * <p>
     * thinContent :
     * isRead : 1
     */

    private List<MessageAllBean> messageAll;
    /**
     * title : 我的XIN课程
     * titleMultiUrl : http://softlst.com:8888/img
     * ctime : 05-10
     * messageId : 437
     * content : null
     * thinContent : 您参与的课程我的XIN课程已被教练评价完成。是否通过：通过, 教练评语:季节变化
     * isRead : 1
     */

    private List<AppraiseBean> appraise;

    public List<MessageRecoBean> getMessageReco() {
        return messageReco;
    }

    public void setMessageReco(List<MessageRecoBean> messageReco) {
        this.messageReco = messageReco;
    }

    public List<MessageAllBean> getMessageAll() {
        return messageAll;
    }

    public void setMessageAll(List<MessageAllBean> messageAll) {
        this.messageAll = messageAll;
    }

    public List<AppraiseBean> getAppraise() {
        return appraise;
    }

    public void setAppraise(List<AppraiseBean> appraise) {
        this.appraise = appraise;
    }

    public static class MessageRecoBean {
        private String title;
        private String titleMultiUrl;
        private String ctime;
        private String messageId;
        private String content;
        private String thinContent;
        private String isRead;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleMultiUrl() {
            return titleMultiUrl;
        }

        public void setTitleMultiUrl(String titleMultiUrl) {
            this.titleMultiUrl = titleMultiUrl;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getThinContent() {
            return thinContent;
        }

        public void setThinContent(String thinContent) {
            this.thinContent = thinContent;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }
    }

    public static class MessageAllBean {
        private String title;
        private String titleMultiUrl;
        private String ctime;
        private String messageId;
        private String content;
        private String thinContent;
        private String isRead;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleMultiUrl() {
            return titleMultiUrl;
        }

        public void setTitleMultiUrl(String titleMultiUrl) {
            this.titleMultiUrl = titleMultiUrl;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getThinContent() {
            return thinContent;
        }

        public void setThinContent(String thinContent) {
            this.thinContent = thinContent;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }
    }

    public static class AppraiseBean {
        private String title;
        private String titleMultiUrl;
        private String ctime;
        private String messageId;
        private Object content;
        private String thinContent;
        private String isRead;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleMultiUrl() {
            return titleMultiUrl;
        }

        public void setTitleMultiUrl(String titleMultiUrl) {
            this.titleMultiUrl = titleMultiUrl;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getThinContent() {
            return thinContent;
        }

        public void setThinContent(String thinContent) {
            this.thinContent = thinContent;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }
    }
}
