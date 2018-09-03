package com.bm.api;


/**
 * Service基类<br />
 * 存放 API接口地址等
 * <p>
 * http://112.64.173.178/huafa/app/userUnRead/getAllUnreadCount.do
 */
public class BaseApi {

    //	public static final String API_HOST = "http://v250.tzj.softlst.com:8888";//正式服务器
   // public static final String API_HOST = "http://59.110.62.10:8888";//测试服务器
    public static final String API_HOST = "http://www.softlst.com:8888";//测试服务器
//	public static final String API_HOST = "http://192.168.0.105:8888";//文强服务器
//	public static final String API_HOST = "http://192.168.0.107:8888";//阳阳

    //	public static final String API_URL_PRE = API_HOST + "/tongzijun/api/";
    public static String API_URL_PRE = API_HOST + "/tongZiJun/api/";

    //测试切换服务器用
    public static final String API_HOST1 = "http://59.110.62.10:8888";//阿里云服务器
    public static final String API_HOST2 = "http://101.201.149.186:8888";//测试服务器


    public static final String SHARE_URL = API_URL_PRE + "p/r?c=1002&os=aos";//分享地址

    /**
     * 登录接口
     */
    public static final String API_USER_LOGIN = "member/login.do";

    public static final String API_GETALLUNREADCOUNT = "userUnRead/getAllUnreadCount.do";

    /**---------IM--------*/
    /**
     * 根据用户名称或手机号码查找聊天用户
     */
    public static final String API_IM_SEARCHUSERLIST = "im/searchUserList.do";
    /**
     * 批量获取用户头像和昵称
     */
    public static final String API_IM_USERINFOLIST = "im/getImUserInfoList.do";
    /**
     * 添加一个群
     */
    public static final String API_IM_ADDGROUP = "im/addGroup.do";
    /**
     * 解散一个群
     */
    public static final String API_IM_REMOVEGROUP = "im/removeGroup.do";
    /**
     * 修改一个群
     */
    public static final String API_IM_UPDATEGROUPINFO = "im/updataGroupInfo.do";
    /**
     * 查看一个群信息
     */
    public static final String API_IM_GROUPINFO = "im/getGroupInfo.do";
    /**
     * 搜索群
     */
    public static final String API_IM_SEARCHGROUPLIST = "im/searchGroupList.do";
    /**
     * 邀请他人入群
     */
    public static final String API_IM_ADDUSERLISTTOGROUP = "im/addUserListToGroup.do";
    /**
     * 退群批量
     */
    public static final String API_IM_REMOVEUSERLIST = "iim/removeUserList.do";
    /**
     * 退群单人
     */
    public static final String API_IM_REMOVEUSER = "im/removeUser.do";
    /**
     * 查询群成员列表
     */
    public static final String API_IM_FINDGROUPUSERLIST = "im/findGroupUserList.do";
    /**
     * 群主移交群
     */
    public static final String API_IM_TRANSFERGROUP = "im/transferGroup.do";
    /**
     * 查找用户的群列表
     */
    public static final String API_IM_FINDUSERGROUPLIST = "im/findUserGroupList.do";

    /**
     * 通讯录
     */
    public static final String API_IM_FINDADDRESSLIST = "im/findaddressList.do";


    /**
     * 获取广告
     */
    public static final String API_sysAdvertsList = "sysAdverts/list.do";
    /**
     * 孩子列表
     */
    public static final String API_GETCHILDREN = "casUserBaby/list.do";
    /**
     * 数据字典
     */
    public static final String API_dictionaryList = "dictionary/list";
    /**
     * 新增孩子
     */
    public static final String API_ADDCHILD = "casUserBaby/add.do";
    /**
     * 上传孩子头像
     */
    public static final String API_UPLOADCHILDAvatar = "casUserBaby/uploadAvatar.do";
    /**
     * 修改孩子
     */
    public static final String API_EDITCHILD = "casUserBaby/update";


    /**
     * 领取红包
     */
    public static final String API_getRedBag = "rechargeUserInfo/getRedBag.do";
    /**
     * 登陆获取优惠券
     */
    public static final String API_gainLoginCoupon = "userCoupon/gainLoginCoupon";
    /**
     * 版本更新检测
     */
    public static final String API_versionCheck = "sysVersion/check";


    /**
     * 查询用户信息
     */
    public static final String API_TZJCAS_GETUSERINFO = "casUser/getUserInfo.do";
    /**
     * 充值
     */
    public static final String API_PAYRECHARGE = "deal/recharge";
    /**
     * 充值额度列表
     */
    public static final String API_rechargeConfig = "deal/rechargeConfigList";
    /**
     * 消费，第三方支付
     */
    public static final String API_PAY = "btsOrderInfo/payThird.do";
//	/**
//	 *
//	 *充值
//	 */
//	public static final String API_PAYRECHARGE="deal/payrecharge.do";
    /**
     * 微信支付
     */
    public static final String API_WXPAYINFO = "btsOrderInfo/wxPayinfo.do";
    /**
     * 微信充值
     */
    public static final String API_WXRECHAREINFO = "deal/wxRechargeinfo";

    /**
     * 登录
     */
    public static final String API_TZJCAS_LOGIN = "casUser/login.do";


    /**
     * 绑定微信
     */
    public static final String API_BIND_WECHAT = "casUser/bindWechat";


    /**
     * 绑定手机
     */
    public static final String API_BIND_PHON = "casUser/bindPhone";

    /**
     * 解绑旧手机，跟换新手机。当然不用你操心解绑
     */
    public static final String API_UNBIND_PHON = "casUser/relievePhone";


    /**
     * 解绑微信
     */
    public static final String API_UNBIND_WECHAT = "casUser/relieveWechat";

    /**
     * 再次绑定手机
     */
    public static final String API_UNBIND_WECHAT_TO = "casUser/bindWechatTO";

    /**
     * 获取验证码
     */
    public static final String API_TZJCAS_SENDCODE = "casUser/sendcode.do";
    /**
     * 重置密码
     */
    public static final String API_TZJCAS_UPDATEPASS = "casUser/updatepass.do";
    /**
     * 重置支付密码
     */
    public static final String API_TZJCAS_resetPayPwd = "casUser/resetPayPwd.do";

    /**
     * 验证手机号和验证验证码是否正确
     */
    public static final String API_TZJCAS_CHECKCODE = "casUser/checkcode.do";
    /**
     * 注册
     */
    public static final String API_TZJCAS_REGISTER = "casUser/register.do";
    /**
     * 查询教练
     */
    public static final String API_TZJCOACH_COACHLIST = "tzjcoach/coachlist.do";
    /**
     * 查询勋章
     */
    public static final String API_TZJMEDAL_MEDALLIST = "tzjmedal/medallist.do";
    /**
     * 加为好友  关注
     */
    public static final String API_TZJFRIENDS_ADDFRIENDS = "tzjfriends/addfriends.do";
    /**
     * 查询我的好友的宝贝  荣誉榜
     */
    public static final String API_TZJCASUSERBABY_BABYLIST = "casUserBaby/babylist.do";
    /**
     * 勋章详情
     */
    public static final String API_TZJGOODS_GOODSLISTBYMEDAL = "tzjgoods/goodslistbymedal.do";
    /**
     * 热门城市
     */
    public static final String API_TZJTREND_HOTREGION = "tzjtrend/hotregion.do";
    /**
     * 查询城市
     */
    public static final String API_TZJTREND_TRENDREGION = "tzjtrend/trendregion.do";
    /**
     * 查询最近访问城市
     */
    public static final String API_GET_LASTCITY = "casUser/getlastcity.do";
    /**
     * 更新最近访问城市
     */
    public static final String API_UPDATE_LASTCITY = "casUser/updatelastcity.do";
    /**
     * 是否有未读消息
     */
    public static final String API_TZJMESSAGE_ISUNREADMESSAGE = "tzjmessage/isunreadmessage.do";
    /**
     * 广告
     */
    public static final String API_TZJADVERT_ADVERTLIST = "tzjadvert/advertlist.do";

    /**
     * 门店
     */
    public static final String API_TZJSTORE_STORELIST = "tzjstore/storelist.do";


    /**
     * 推荐课程（热门商品）
     */
    public static final String API_TZJGOODS_HOTGOODS = "tzjgoods/hotgoods.do";
    /**
     * 消息查询
     */
    public static final String API_TZJMESSAGE_MESSAGELIST = "tzjmessage/messagelist.do";
    /**
     * 消息详情
     */
    public static final String API_TZJMESSAGE_MESSAGEDETAIL = "tzjmessage/messagedetail.do";
    /**
     * 查询课程(单广告，根据广告id查询课程详情)
     */
    public static final String API_TZJGOODS_GOODSDETAILBYBOARD = "tzjgoods/goodsdetailbyboard.do";
    /**
     * 查询课程-场次
     */
    public static final String API_TZJGOODS_GOODNUMOFTIME = "tzjgoods/goodnumoftime.do";
    /**
     * 根据挂广告id，关联出多个课程的课程列表
     */
    public static final String API_TZJGOODS_GOODSLISTADVERT = "tzjgoods/goodslistadvert.do";
    /**
     * 首页-点类别-进入的课程列表页面
     */
    public static final String API_TZJGOODS_GOODSLISTTYPE = "tzjgoods/goodslisttype.do";
    /**
     * 根据课程id，查询课程详情，不分类别
     */
    public static final String API_TZJGOODS_GOODSDETAIL = "tzjgoods/goodsdetail.do";

    /**
     * 根据课程id，查询课程详情，不分类别
     */
    public static final String API_TZJGOODS_GOODSDETAIL2 = "tzjgoods/goodsDetail";
    /**
     * 商品详情
     */
    public static final String API_accouterInfo = "accouter/info";
    /**
     * 商品列表
     */
    public static final String API_accouterList = "accouter/list";
    /**
     * 列表展示门店下课程
     */
    public static final String API_TZJGOODS_GOODSLIST = "tzjgoods/goodslist.do";
    /**
     * 玩伴儿
     */
    public static final String API_PLAY_MATELIST = "article/playmateList.do";
    /**
     * 探索
     */
    public static final String API_PROBE_LIST = "article/probeList.do";
    /**
     * 我的探索
     */
    public static final String API_MYPROBE_LIST = "article/myProbeList.do";
    /**
     * 探索详情
     */
    public static final String API_PROBE_DETIAL = "article/probe.do";
    /**
     * 探索详情评论列表
     */
    public static final String API_COMMENT_LIST = "article/commentList.do";
    /**
     * 查询宝贝
     */
    public static final String API_TZJCASUSERBABY_REGISTEREDBABYLIST = "casUserBaby/registeredbabylist.do";
    /**
     * 教练详情
     */
    public static final String API_TZJCOACH_COACHDETAIL = "tzjcoach/coachdetail.do";
    /**
     * 查询课程 （课程中心-课程详情-城市营地）
     */
    public static final String API_TZJGOODS_GOODSLISTBYCOACH = "tzjgoods/goodslistbycoach.do";
    /**
     * 绑定和解除教练
     */
    public static final String API_TZJCOACH_BINDINGCOACH = "tzjcoach/bindingcoach.do";
    /**
     * 立即购买
     */
//	public static final String API_TZJORDER_PAYMENTACCOUNT="btsOrderInfo/paymentaccount.do";
    public static final String API_TZJORDER_PAYMENTACCOUNT = "btsOrderInfo/payAccount";
    /**
     * 订单生成
     */
//	public static final String API_TZJORDER_CREATEBTSORDER="btsOrderInfo/createbtsorder.do";
    public static final String API_TZJORDER_CREATEBTSORDER = "btsOrderInfo/createOrder.do";
    /**
     * 订单生成
     * 2.5
     */
    public static final String API_btsOrderInfo_addOrder = "btsOrderInfo/addOrder";

    /**
     * 自动选择优惠券
     */
    public static final String API_pickCoupon = "userCoupon/pickCoupon";
    /**
     * 领取优惠券
     */
    public static final String API_receiveCoupon = "userCoupon/receiveCoupon";
    /**
     * 查询我的优惠券
     */
    public static final String API_userCoupon_list = "userCoupon/list";

    /**
     * 成长中心-列表
     */
    public static final String API_growthRecord_list = "growthRecord/list";

    /**
     * 成长中心-删除
     */
    public static final String API_growthRecord_delete = "growthRecord/delete";

    /**
     * 查询宝贝的相关信息   ？？？
     */
    public static final String API_TZJCASUSERBABY_SEARCHUSERBABYINFO = "casUserBaby/searchUserBabyInfo.do";
    /**
     * 同教练端查询课程  我的课程
     */
    public static final String API_TZJGOODS_GOODSCOURSEINFO = "tzjgoods/myGoodsList";

    /**
     * 删除我的课程
     */
    public static final String API_TZJGOODS_DELETEGOODS = "tzjgoods/deletegoods";
    /**
     * 我的课程包
     */
    public static final String API_courseGroup_myCourseGroup = "courseGroup/myCourseGroup";
    /**
     * 更新用户信息
     */
    public static final String API_TZJCASUSERBABY_UPDATEUSERBABYINFO = "casUserBaby/updateUserBabyInfo.do";
    /**
     * 创建、修改支付密码
     */
    public static final String API_TZJACCOUNT_UPDATEPAYPASSWORD = "deal/updatePayPassword.do";

    /**
     * 更新密码接口
     */
    public static final String API_TZJACCOUNT_UPDATEPASSWORD = "casUser/updatepassword.do";

    /**
     * 收藏
     */
    public static final String API_TZJGOODS_GOODSCOLLECT = "tzjgoods/goodsCollect.do";
    /**
     * 免责声明
     */
    public static final String API_TZJGOODS_GOODSSTATE = "tzjgoods/goodsState.do";

    /**
     * 注册协议 帮助 关于软件
     */
    public static final String API_TZJRICH_RICHTEXT = "rich/richText";


    /**
     * 家长评价
     */
    public static final String API_TZJCOACH_COACHCOMMENTLIST = "tzjcoach/coachCommentlist.do";

    /**
     * 查看课程评价
     */
    public static final String API_tzjcomment = "tzjcomment/findContent.do";
    /**
     * 查看教练点评
     */
    public static final String API_coachCommentByBaby = "tzjcoach/coachCommentByBaby";


    /**
     * 玩伴
     */
    public static final String API_ARTICLE_PLAYMATELIST = "article/playmateList.do";
    /**
     * 探索
     */
    public static final String API_ARTICLE_PROBELIST = "article/probeList.do";
    /**
     * 赞
     */
    public static final String API_ARTICLE_ADDLAUD = "article/addLaud.do";
    /**
     * 取消赞
     */
    public static final String API_ARTICLE_DELLAUD = "article/dellaud.do";
    /**
     * 添加回复
     */
    public static final String API_ARTICLE_ADDCOMMENT = "article/addComment.do";

    /**
     * 添加课程评论
     */
    public static final String API_TZJGOODS_ADDCOMMENT = "tzjgoods/addComment";
    /**
     * 举报
     */
    public static final String API_ARTICLE_ADDREPORT = "article/addReport.do";
    /**
     * 发帖
     */
    public static final String API_ARTICLE_ADDARTICLE = "article/addArticle.do";
    /**
     * 发布成长记录
     */
    public static final String API_SEND_GROWUP_RECORD = "growthRecord/add";

    /**
     * 成长记录草稿查询
     */
    public static final String API_growthRecord_getDraftInfo = "growthRecord/getDraftInfo";

    /**
     * 成长记录 删除附件
     */
    public static final String API_sysAttachment_deleteById = "sysAttachment/deleteById";

    /**
     * 探索详情
     */
    public static final String API_ARTICLE_PROBE = "article/probe.do";
    /**
     * 帖子评论列表
     */
    public static final String API_ARTICLE_COMMENTLIST = "article/commentList.do";

    /**
     * 余额明细
     */
    public static final String API_TZJORDER_USERORDERLIST = "btsOrderInfo/userBalancelist.do";
    /**
     * 删除帖子
     */
    public static final String API_ARTICLE_DELARTICLE = "article/delArticle.do";
    /**
     * 我的收藏
     */
    public static final String API_MYSELF_MYCOLLECTION = "myself/myCollection.do";
    /**
     * 删除收藏
     */
    public static final String API_MYSELF_DELCOLLECTION = "myself/delCollection.do";
    /**
     * 意见反馈
     */
    public static final String API_MYSELF_ADDFEEDBACK = "myself/addFeedBack.do";
    /**
     * 我的积分
     */
    public static final String API_MYSELF_MYINTEGRAL = "myself/myIntegral.do";
    /**
     * 查询省市区信息
     */
    public static final String API_TZJTREND_SEARCHTRENDREGIONALL = "tzjtrend/searchtrendregionall.do";
    /**
     * 版本更新
     */
    public static final String API_TZJVERSION_SEARCHVERSION = "tzjversion/searchversion.do";
    /**
     * im start
     */
    /**
     * 好友-我的好友
     */
    public static final String API_SEARCH_FRIENDLIST = "im/searchFriendList.do";
    /**
     * 好友-我的群
     */
    public static final String API_SEARCH_GROUPLIST = "im/searchGroupList.do";
    /**
     * 查询朋友
     */
    public static final String API_SEARCH_USERFRIENDLIST = "im/searchUserFriendList.do";
    /**
     * 添加新朋友-找朋友
     */
    public static final String API_SEARCH_USERLIST = "im/searchUserList.do";
    /**
     * 添加新朋友-我的新朋友
     */
    public static final String API_SEARCH_STATUSLIST = "im/searchStatusList.do";
    /**
     * 添加好友
     */
    public static final String API_ADD_FRIEND = "im/addFriend.do";
    /**
     * 群信息
     */
    public static final String API_SEARCH_GROUPDETAIL = "im/searchGroupDetail.do";

    /**
     * 批量获取用户头像和昵称
     */
    public static final String API_GETIMUSERINFOLIST = "im/getImUserInfoList.do";
    /**
     * 查询教练和报名
     */
    public static final String API_GET_GOODS_COACHBABYINFO = "tzjgoods/coachbabyinfobystore.do";

    /**
     * 验证手机  是注册还是绑定 第三方登录
     */
    public static final String API_GET_CHECKPHONE = "casUser/checkPhone";
    /**
     * 成长中心上传封面背景
     */
    public static final String API_UPLOAD_COVER = "casUser/uploadCoverImg";


    /**
     *
     */
    public static final String API_GET_STORECOMMENT = "tzjstore/getCommentList";

    /**
     * 按门店或场馆查课程(乐园)
     */
    public static final String API_goodsListByStore = "tzjgoods/goodsListByStore";

    /**
     * 按门店或场馆查课程(乐园)
     */
    public static final String API_tzjgoods_goodsListByType = "tzjgoods/goodsListByType";

    /**
     * 课程包列表
     */
    public static final String API_courseGroup_list = "courseGroup/list";
    /**
     * 课程包详情
     */
    public static final String API_courseGroup_detailList = "courseGroup/detailList";

    /**
     * 场馆列表
     */
    public static final String API_tzjstore_venueList = "tzjstore/venueList";

    /**
     * 按时间查课程包的课程
     */
    public static final String API_tzjgoods_goodsListByDate = "tzjgoods/goodsListByDate";

    /**
     * 课程包-预约
     */
    public static final String API_tzjgoods_goodsBespeak = "tzjgoods/goodsBespeak";

    /**
     * 交通费查询
     */
    public static final String API_trafficFee_list = "trafficFee/list";

    /**
     * 交通费查询
     */
    public static final String API_tzjgoods_baseGoodsDetail = "tzjgoods/baseGoodsDetail";
    /**
     * 获取周末成长营的城市地址
     */
    public static final String API_getZhouMo_citys = "tzjgoods/getTrend";


    /**
     * 获取消息列表
     */
    public static final String API_MESSAEG_get_message_list = "tzjmessage/messagelist.do";



    public static final String API_MESSAGE_get_detail="tzjmessage/messagedetail";

}
