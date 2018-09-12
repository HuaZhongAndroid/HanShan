package com.bm.api;

import android.content.Context;

import com.bm.entity.Course;
import com.bm.entity.User;
import com.bm.entity.XiaoXiDetail;
import com.bm.entity.XiaoxiList;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;

import java.util.HashMap;


/**
 * MessageManager提供 消息类的 接口数据获取等功能
 * 驼峰命名  以Manager结尾
 */
public class MessageManager extends BaseApi {
    static final String TAG = MessageManager.class.getSimpleName();
    private static MessageManager mInstance;

    public static synchronized MessageManager getInstance() {
        if (mInstance == null) {
            mInstance = new MessageManager();
        }
        return mInstance;
    }


    /**
     * 获取消息列表
     */
    public void getMessageList(Context context, HashMap<String, String> map, ServiceCallback<CommonResult<XiaoxiList>> callback) {
        AsyncHttpHelp.httpGet(context, API_MESSAEG_get_message_list, map, callback);
    }

    /**
     * 获取消息列表
     */
    public void getMessageDetailById(Context context, HashMap<String, String> map, ServiceCallback<CommonResult<XiaoXiDetail>> callback) {
        AsyncHttpHelp.httpGet(context, API_MESSAGE_get_detail, map, callback);
    }


    /**
     * 获取用户信息
     */
    public void getTzjcasGetUserInfo(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback) {
        AsyncHttpHelp.httpGet(context, API_TZJCAS_GETUSERINFO, map, callback);
        ;
    }


    /**
     * 验证手机  是注册还是绑定 第三方登录
     */
    public void get_tzjgoods_baseGoodsDetail(Context context, HashMap<String, String> map, final ServiceCallback<CommonResult<Course>> callback) {
        AsyncHttpHelp.httpGet(context, API_tzjgoods_baseGoodsDetail, map, callback);
        ;
    }
}
