package com.bm.entity;

import java.io.Serializable;

/**
 * 门店评论
 */
public class StoreComment implements Serializable {
    public String content; //评论内容
    public String userid;//用户id
    public String evaluateId;//评论id
    public String avatar;//头像
    public String ctime;//评论时间
    public String nickname;//昵称
}
