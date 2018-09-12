package com.bm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */
public class GrowUp implements Serializable {

    public String content;
    public String storeName;
    public String coachName;
    public String coachId;
    public String userId;
    public String babyId;
    public String goodsId;
    public String storeId;
    public String goodsType;
    public String goodsName;
    public String coachHead;

    public String pkid;

    public String ctime;
    public String recordFlag;

    public String isAssess; //是否评价  0  未评价


    public List<GrowUpImg> attachmentlist;
}
//          "coachName":"贝·史密吴玉伟",
//          "pkid":159,
//          "attachmentlist":
//          [
//          {
//          "ctime":"2018-09-51 14:51:51",
//          "pkid":325,
//          "factName":"6db7f1aa-5219-4088-b22e-8e13c594e536.jpg?",
//          "url":"http://han-shan-test.oss-cn-hangzhou.aliyuncs.com/img/user/growth/6db7f1aa-5219-4088-b22e-8e13c594e536.jpg?"
//          }
//
//          ],
//          "goodsId":"3359",
//          "storeId":"108",
//          "userId":1040,
//          "coachId":"301",
//          "content":"哈哈哈",
//          "goodsType":"1",
//          "babyId":1089,
//          "isAssess":"",
//          "ctime":"2018-09-10 14:51:49",
//          "recordFlag":2,
//          "storeName":"金蛇大厅",
//          "coachHead":"/user/avatar/9372ee4c-73ca-4eac-b36a-4c0a3237003e.png",
//          "goodsName":""
//          },