package com.bm.api;

import android.content.Context;
import android.text.TextUtils;

import com.bm.entity.Advertisement;
import com.bm.entity.AliOrder;
import com.bm.entity.Babys;
import com.bm.entity.Badge;
import com.bm.entity.Changguan;
import com.bm.entity.Child;
import com.bm.entity.Coach;
import com.bm.entity.CoachInfo;
import com.bm.entity.Comment;
import com.bm.entity.CommentList;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.bm.entity.CourseBean;
import com.bm.entity.Disclaimer;
import com.bm.entity.GoodsNumTime;
import com.bm.entity.GrowUp;
import com.bm.entity.HotGoods;
import com.bm.entity.Jiaotongfei;
import com.bm.entity.MessageDetail;
import com.bm.entity.MessageList;
import com.bm.entity.Order;
import com.bm.entity.PageDataList;
import com.bm.entity.PlayMateList;
import com.bm.entity.Province;
import com.bm.entity.RechargeConfig;
import com.bm.entity.StoreComment;
import com.bm.entity.Storelist;
import com.bm.entity.User;
import com.bm.entity.Version;
import com.bm.entity.WeixinOrder;
import com.bm.entity.Youhuiquan;
import com.bm.entity.ZhouMoCity;
import com.bm.entity.post.UserPost;
import com.bm.tzj.city.AllCity;
import com.bm.tzj.city.City;
import com.google.android.gms.games.Game;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * UserService提供 用户类的 接口数据获取等功能
 * 驼峰命名  以Manager结尾
 */
public class UserManager extends BaseApi {
    static final String TAG = UserManager.class.getSimpleName();
    private static UserManager mInstance;

    public static synchronized UserManager getInstance() {
        if (mInstance == null) {
            mInstance = new UserManager();
        }
        return mInstance;
    }


    /**
     * 获取用户信息
     */
    public void getTzjcasGetUserInfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_GETUSERINFO, map, callback);
        ;
    }

    /**
     * 获取订单信息（消费）
     */
    public void payAli(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<AliOrder>> callback) {
        AsyncHttpHelp.httpGet(context, API_PAY, map, callback);
    }

    /**
     * 获取订单信息（消费）
     */
    public void payWx(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<WeixinOrder>> callback) {
        AsyncHttpHelp.httpGet(context, API_PAY, map, callback);
    }

    /**
     * 获取订单信息（充值）
     */
    public void payrechargeAli(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<AliOrder>> callback) {
        AsyncHttpHelp.httpGet(context, API_PAYRECHARGE, map, callback);
    }

    /**
     * 获取订单信息（充值）
     */
    public void payrechargeWx(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<WeixinOrder>> callback) {
        AsyncHttpHelp.httpGet(context, API_PAYRECHARGE, map, callback);
    }

    /**
     * 获取额度列表（充值）
     */
    public void rechargeConfigList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<RechargeConfig>> callback) {
        AsyncHttpHelp.httpGet(context, API_rechargeConfig, map, callback);
    }
//  /**
//  *
//  *获取微信订单信息
//  */
// public void wxPayinfo(Context context,HashMap<String, String> map,final ServiceCallback<CommonResult<Order>> callback) {
//     AsyncHttpHelp.httpGet(context,API_WXPAYINFO, map, callback);;
// }
// /**
// *
// *获取微信充值信息
// */
//public void wxRechareInfo(Context context,HashMap<String, String> map,final ServiceCallback<CommonResult<Order>> callback) {
//    AsyncHttpHelp.httpGet(context,API_WXRECHAREINFO, map, callback);;
//}

    /**
     * 验证登录信息
     */
    public void getTzjcasLogin(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_LOGIN, map, serviceCallback);
    }

    /**
     * 绑定微信
     */

    public void bindWeichat(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_BIND_WECHAT, map, serviceCallback);
    }

    /**
     * 绑定手机
     */

    public void BindPhone(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_BIND_PHON, map, serviceCallback);
    }

    /**
     * 解绑旧手机，跟换新手机。当然不用你操心解绑
     */

    public void changePhone(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_UNBIND_PHON, map, serviceCallback);
    }

    /**
     * 解绑微信
     */

    public void unbindWeichat(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_UNBIND_WECHAT, map, serviceCallback);
    }

    /**
     * 再次绑定微信
     */

    public void bindWeichatTO(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_UNBIND_WECHAT_TO, map, serviceCallback);
    }

    /**
     * 获取验证码
     */
    public void getTzjcasSendcode(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_SENDCODE, map, callback);
        ;
    }

    /**
     * 重置密码
     */
    public void getTzjcasUpdatepass(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback, int pwdType) {
        if (pwdType == 1)
            AsyncHttpHelp.httpGet(context, API_TZJCAS_resetPayPwd, map, callback);
        else
            AsyncHttpHelp.httpGet(context, API_TZJCAS_UPDATEPASS, map, callback);
    }

    /**
     * 验证手机号和验证验证码是否正确
     */
    public void getTzjcasCheckcode(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_CHECKCODE, map, callback);
        ;
    }

    /**
     * 注册
     */
    public void getTzjcasRegister(Context context, UserPost uPost, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_REGISTER, uPost.toMap(), callback);
        ;
    }

    /**
     * 查询教练
     */
    public void getTzjcoachCoachlist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Coach>> serviceCallback) {
        AsyncHttpHelp.httpGet(context, API_TZJCOACH_COACHLIST, map, serviceCallback);
        ;
    }

    /**
     * 查询勋章
     */
    public void getTzjmedalMedallist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Badge>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJMEDAL_MEDALLIST, map, callback);
        ;
    }

    /**
     * 勋章详情
     */
    public void getTzjgoodsGoodslistbymedal(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSLISTBYMEDAL, map, callback);
        ;
    }

    /**
     * 加为好友  关注
     */
    public void getTzjfriendsAddfriends(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJFRIENDS_ADDFRIENDS, map, callback);
        ;
    }

    /**
     * 查询我的好友的宝贝  荣誉榜
     */
    public void getTzjcasuserbabyBabylist(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCASUSERBABY_BABYLIST, map, callback);
        ;
    }

    /**
     * 热门城市
     */
    public void getTzjtrendHotregion(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<City>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJTREND_HOTREGION, map, callback);
        ;
    }

    /**
     * 查询城市
     */
    public void getTzjtrendTrendregion(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<AllCity>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJTREND_TRENDREGION, map, callback);
        ;
    }

    /**
     * 查询最近访问城市
     */
    public void getLastCity(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_GET_LASTCITY, map, callback);
        ;
    }

    /**
     * 更新最近访问城市
     */
    public void updateLastCity(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_UPDATE_LASTCITY, map, callback);
        ;
    }

    /**
     * 是否有未读消息
     */
    public void getTzjmessageIsunreadmessage(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJMESSAGE_ISUNREADMESSAGE, map, callback);
        ;
    }

    /**
     * 广告
     */
    public void getTzjadvertAdvertlist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Advertisement>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJADVERT_ADVERTLIST, map, callback);
        ;
    }

    /**
     * 门店
     */
    public void getTzjstoreStorelist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Storelist>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJSTORE_STORELIST, map, callback);
    }

    /**
     * 获取周末成长营的城市地址
     */
    public void getZhouMoCitys(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<ZhouMoCity>> callback) {
        AsyncHttpHelp.httpGet(context, API_getZhouMo_citys, map, callback);
        ;
    }

    public void getStoreComment(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<PageDataList<StoreComment>>> callback) {
        AsyncHttpHelp.httpGet(context, API_GET_STORECOMMENT, map, callback);
    }

    public void getGoodsListByStore(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_goodsListByStore, map, callback);
    }

    public void get_tzjgoods_goodsListByDate(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjgoods_goodsListByDate, map, callback);
    }

    public void get_trafficFee_list(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Jiaotongfei>> callback) {
        AsyncHttpHelp.httpGet(context, API_trafficFee_list, map, callback);
    }

    /**
     * type
     * 3  周末成长营
     * 4  暑期大露营
     *
     * @param context
     * @param map
     * @param callback
     */
    public void get_tzjgoods_goodsListByType(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<CourseBean>> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjgoods_goodsListByType, map, callback);
    }

    public void get_courseGroup_list(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<CourseBao>> callback) {
        AsyncHttpHelp.httpGet(context, API_courseGroup_list, map, callback);
    }

    public void get_courseGroup_detailList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_courseGroup_detailList, map, callback);
    }

    public void get_tzjstore_venueList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Changguan>> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjstore_venueList, map, callback);
    }

    /**
     * 推荐课程（热门商品）
     */
    public void getTzjgoodsHotgoods(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_HOTGOODS, map, callback);
        ;
    }

    /**
     * 消息查询
     */
    public void getTzjmessageMessagelist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<MessageList>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJMESSAGE_MESSAGELIST, map, callback);
        ;
    }

    /**
     * 消息详情
     */
    public void getTzjmessageMessagedetail(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<MessageDetail>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJMESSAGE_MESSAGEDETAIL, map, callback);
        ;
    }

    /**
     * 查询课程(单广告，根据广告id查询课程详情)
     */
    public void getTzjgoodsGoodsdetailbyboard(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSDETAILBYBOARD, map, callback);
        ;
    }

    /**
     * 查询课程-场次
     */
    public void getTzjgoodsGoodnumoftime(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<GoodsNumTime>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODNUMOFTIME, map, callback);
        ;
    }

    /**
     * 根据挂广告id，关联出多个课程的课程列表
     */
    public void getTzjgoodsGoodslistadvert(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSLISTADVERT, map, callback);
        ;
    }

    /**
     * 根据课程id，查询课程详情，不分类别
     */
    public void getTzjgoodsGoodslisttype(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSLISTTYPE, map, callback);
        ;
    }

    /**
     * 根据课程id，查询课程详情，不分类别
     */
    public void getTzjgoodsGoodsdetail(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSDETAIL, map, callback);
        ;
    }

    /**
     * 根据课程id，查询课程详情，不分类别
     */
    public void getTzjgoodsGoodsdetail2(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSDETAIL2, map, callback);
    }

    /**
     * 列表展示门店下课程
     */
    public void getTzjgoodsGoodslist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSLIST, map, callback);
        ;
    }

    /**
     * 玩伴儿
     */
    public void getPlaymateList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_PLAY_MATELIST, map, callback);
        ;
    }

    /**
     * 探索
     */
    public void getProbeList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_PROBE_LIST, map, callback);
        ;
    }

    /**
     * 我的探索
     */
    public void getMyProbeList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_MYPROBE_LIST, map, callback);
        ;
    }

    /**
     * 探索详情
     */
    public void getProbeDetial(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_PROBE_DETIAL, map, callback);
        ;
    }

    /**
     * 探索详情评论列表
     */
    public void getCommentList(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<CommentList>> callback) {
        AsyncHttpHelp.httpGet(context, API_COMMENT_LIST, map, callback);
        ;
    }

    /**
     * 查询宝贝
     */
    public void getTzjcasuserbabyRegisteredbabylist(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Babys>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCASUSERBABY_REGISTEREDBABYLIST, map, callback);
        ;
    }

    /**
     * 教练详情
     */
    public void getTzjcoachCoachdetail(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<CoachInfo>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCOACH_COACHDETAIL, map, callback);
        ;
    }

    /**
     * 查询课程 （课程中心-课程详情-城市营地）
     */
    public void getTzjgoodsGoodslistbycoach(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Game>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSLISTBYCOACH, map, callback);
        ;
    }

    /**
     * 绑定和解除教练
     */
    public void getTzjcoachBindingcoach(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCOACH_BINDINGCOACH, map, callback);
        ;
    }

    /**
     * 立即购买
     */
    public void getTzjorderPaymentaccount(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJORDER_PAYMENTACCOUNT, map, callback);
    }

    /**
     * 订单生成
     */
    public void getTzjorderCreatebtsorder(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Order>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJORDER_CREATEBTSORDER, map, callback);
        ;
    }

    /**
     * 订单生成
     */
    public void getbtsOrderInfo_addOrder(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Order>> callback) {
        AsyncHttpHelp.httpGet(context, API_btsOrderInfo_addOrder, map, callback);
    }

    /**
     * 查询宝贝的相关信息  ？？？
     */
    public void getTzjcasuserbabySearchUserBabyInfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCASUSERBABY_SEARCHUSERBABYINFO, map, callback);
        ;
    }

    /**
     * 同教练端查询课程  我的课程
     */
    public void getTzjgoodsGoodscourseinfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSCOURSEINFO, map, callback);
    }

    /**
     * 我的课程包
     */
    public void get_courseGroup_myCourseGroup(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<CourseBao>> callback) {
        AsyncHttpHelp.httpGet(context, API_courseGroup_myCourseGroup, map, callback);
    }

    /**
     * 成长中心-列表
     */
    public void getGrowthRecord_list(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<GrowUp>> callback) {
        AsyncHttpHelp.httpGet(context, API_growthRecord_list, map, callback);
    }

    /**
     * 成长中心-删除
     */
    public void getGrowthRecord_delete(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_growthRecord_delete, map, callback);
    }

    /**
     * 查询我的优惠券
     *
     * @param context
     * @param map
     * @param callback
     */
    public void getMyYouhuiquan(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Youhuiquan>> callback) {
        AsyncHttpHelp.httpGet(context, API_userCoupon_list, map, callback);
        ;
    }

    /**
     * 删除我的课程
     */
    public void getTzjgoodsDeleteGoods(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_DELETEGOODS, map, callback);
        ;
    }

    /**
     * 更新用户信息
     */
    public void getTzjcasuserbabyUpdateUserBabyInfo(Context context, String imagePath, UserPost uPost, ServiceCallback<CommonResult<User>> callback) {
        List<File> files = new ArrayList<File>();
        if (!TextUtils.isEmpty(imagePath)) {
            files.add(new File(imagePath));
        }
        AsyncHttpHelp.httpPost(context, API_TZJCASUSERBABY_UPDATEUSERBABYINFO, uPost.toMap(), "avatar", files, callback);
    }

    /**
     * 创建、修改支付密码
     */
    public void getTzjaccountUpdatePayPassword(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJACCOUNT_UPDATEPAYPASSWORD, map, callback);
        ;
    }

    /**
     * 更新密码接口
     */
    public void getTzjaccountUpdatePassword(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJACCOUNT_UPDATEPASSWORD, map, callback);
        ;
    }


    /**
     * 收藏
     */
    public void getTzjgoodsGoodsCollect(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSCOLLECT, map, callback);
        ;
    }

    /**
     * 免责声明
     */
    public void getTzjgoodsGoodsState(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Disclaimer>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_GOODSSTATE, map, callback);
        ;
    }


    /**
     * 注册协议 帮助 关于软件
     */
    public void getTzjrichRichText(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Disclaimer>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJRICH_RICHTEXT, map, callback);
        ;
    }

    /**
     * 家长评价
     */
    public void getTzjcoachCoachCommentlist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Comment>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCOACH_COACHCOMMENTLIST, map, callback);
        ;
    }

    /**
     * 赞
     */
    public void getArticleAddLaud(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_ARTICLE_ADDLAUD, map, callback);
        ;
    }


    /**
     * 取消赞
     */
    public void getArticleDelLaud(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<PlayMateList>> callback) {
        AsyncHttpHelp.httpGet(context, API_ARTICLE_DELLAUD, map, callback);
        ;
    }

    /**
     * 添加回复   更新教练场馆评论
     */
    public void getArticleAddComment(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_ARTICLE_ADDCOMMENT, map, callback);
        ;
    }

    /**
     * 添加教练场馆评论
     */
    public void getGoodsAddComment(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJGOODS_ADDCOMMENT, map, callback);
        ;
    }


    /**
     * 举报
     */
    public void getArticleAddReport(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_ARTICLE_ADDREPORT, map, callback);
        ;
    }


    /**
     * 成长中心草稿查询
     */
    public void getGrowthRecord_getDraftInfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<GrowUp>> callback) {
        AsyncHttpHelp.httpGet(context, API_growthRecord_getDraftInfo, map, callback);
    }

    /**
     * 成长中心 删除附件图片
     */
    public void sysAttachment_deleteById(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_sysAttachment_deleteById, map, callback);
    }

    /**
     * 发帖
     */
    public void getArticleAddArticle(Context context, List<String> imagePath, HashMap<String, String> map, ServiceCallback<StringResult> callback) {
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < imagePath.size(); i++) {
            if (!TextUtils.isEmpty(imagePath.get(i))) {
                files.add(new File(imagePath.get(i)));
            }
        }
        AsyncHttpHelp.httpPost(context, API_ARTICLE_ADDARTICLE, map, "images", files, callback);
    }

    /**
     * 发成长记录
     */
    public void sendGrowUpRecord(Context context, List<String> imagePath, HashMap<String, String> map, ServiceCallback<StringResult> callback) {
//        List<File> files = new ArrayList<File>();
//        for (int i = 0; i < imagePath.size(); i++) {
//            if (!TextUtils.isEmpty(imagePath.get(i))) {
//                files.add(new File(imagePath.get(i)));
//            }
//        }
        AsyncHttpHelp.httpGet(context, API_SEND_GROWUP_RECORD, map, callback);
    }

    /**
     * 余额明细
     */
    public void getTzjorderUserOrderlist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Order>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJORDER_USERORDERLIST, map, callback);
        ;
    }

    /**
     * 删除帖子
     */
    public void getArticleDelArticle(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_ARTICLE_DELARTICLE, map, callback);
        ;
    }

    /**
     * 我的收藏
     */
    public void getMyselfMyCollection(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_MYSELF_MYCOLLECTION, map, callback);
        ;
    }

    /**
     * 我的积分
     */
    public void getMyselfMyIntegral(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Order>> callback) {
        AsyncHttpHelp.httpGet(context, API_MYSELF_MYINTEGRAL, map, callback);
        ;
    }

    /**
     * 删除收藏
     */
    public void getMyselfDelCollection(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_MYSELF_DELCOLLECTION, map, callback);
        ;
    }

    /**
     * 意见反馈
     */
    public void getMyselfAddFeedBack(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_MYSELF_ADDFEEDBACK, map, callback);
        ;
    }


    /**
     * 查询省市区信息
     */
    public void getTzjtrendAllSearchregion(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Province>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJTREND_SEARCHTRENDREGIONALL, map, callback);
    }

    /**
     * 检查版本更新
     */
    public void getTzjversionSearchVerson(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Version>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJVERSION_SEARCHVERSION, map, callback);
        ;
    }

    /**
     * 查询教练和报名
     */
    public void getTzgoodsCoachBabyInfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<HotGoods>> callback) {
        AsyncHttpHelp.httpGet(context, API_GET_GOODS_COACHBABYINFO, map, callback);
        ;
    }

    /**
     * 验证手机  是注册还是绑定 第三方登录
     */
    public void getCheckPhone(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_GET_CHECKPHONE, map, callback);
        ;
    }


    /**
     * 查询孩子列表
     */
    public void getChildrenlist(Context context, HashMap<String, String> map, final ServiceCallback<CommonListResult<Child>> callback) {
        AsyncHttpHelp.httpGet(context, BaseApi.API_GETCHILDREN, map, callback);
        ;
    }

    /**
     * 成长中心上传封面背景
     */
    public void API_UPLOAD_COVER(Context context, String imagePath, HashMap<String, String> map, ServiceCallback<CommonResult<StringResult>> callback) {
        List<File> files = new ArrayList<File>();
        if (!TextUtils.isEmpty(imagePath)) {
            files.add(new File(imagePath));
        }
        AsyncHttpHelp.httpPost(context, API_UPLOAD_COVER, map, "file", files, callback);
    }


    /**
     * 课程包-预约
     */
    public void tzjgoods_goodsBespeak(Context context, HashMap<String, String> map, final ServiceCallback<StringResult> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjgoods_goodsBespeak, map, callback);
    }


    /**
     * 验证手机  是注册还是绑定 第三方登录
     */
    public void get_tzjgoods_baseGoodsDetail(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjgoods_baseGoodsDetail, map, callback);
        ;
    }
}
