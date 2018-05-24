package com.bm.api;

import java.util.HashMap;

import android.content.Context;

import com.bm.entity.GroupInfo;
import com.bm.entity.User;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.BaseResult;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;

public class IMService  extends BaseApi {
	private static IMService mInstance;

	public static synchronized IMService getInstance() {
		if (mInstance == null) {
			mInstance = new IMService();
		}
		return mInstance;
	}
	
	
	
	/**
	 * 根据用户名称或手机号码查找聊天用户
	 * @param loginPost 
	 * @param callback
	 */
	public void getSearchUserList(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_SEARCHUSERLIST, map,callback);
	}
	
	
	/**
	 * 批量获取用户头像和昵称
	 * @param loginPost 
	 * @param callback
	 */
	public void getImUserInfoList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_GETIMUSERINFOLIST, map, callback);
	}
	
	/**
	 * 添加一个群
	 * @param loginPost 
	 * @param callback
	 */
	public void addGroup(HashMap<String, String> map, final ServiceCallback<BaseResult> callback){
//		asyncFormPostNoFileFms(API_IM_ADDGROUP, map,callback);
	}
	
	/**
	 * 解散一个群
	 * @param loginPost 
	 * @param callback
	 */
	public void removeGroup(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_REMOVEGROUP, map,callback);
	}
	
	/**
	 * 修改一个群
	 * @param loginPost 
	 * @param callback
	 */
	public void updateGroup(HashMap<String, String> map, final ServiceCallback<BaseResult> callback){
//		asyncFormPostNoFileFms(API_IM_UPDATEGROUPINFO, map,callback);
	}
	
	/**
	 * 查看一个群信息
	 * @param loginPost 
	 * @param callback
	 */
	public void getImGroupInfo(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_GROUPINFO, map,callback);
	}
	
	/**
	 * 搜索群
	 * @param loginPost 
	 * @param callback
	 */
	public void searchGroupList(HashMap<String, String> map, final ServiceCallback<CommonListResult<GroupInfo>> callback){
//		asyncFormPostNoFileFms(API_IM_SEARCHGROUPLIST, map,callback);
	}
	
	
	
	
	/**
	 * 邀请他人入群
	 * @param loginPost 
	 * @param callback
	 */
	public void addUserListGroup(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_ADDUSERLISTTOGROUP, map,callback);
	}
	
	
	/**
	 * 退群批量
	 * @param loginPost 
	 * @param callback
	 */
	public void removeUserList(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_REMOVEUSERLIST, map,callback);
	}
	
	
	/**
	 * 退群单人
	 * @param loginPost 
	 * @param callback
	 */
	public void removeUser(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_REMOVEUSER, map,callback);
	}
	
	/**
	 * 查询群成员列表
	 * @param loginPost 
	 * @param callback
	 */
	public void findGroupUserList(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_FINDGROUPUSERLIST, map,callback);
	}
	
	/**
	 * 群主移交群
	 * @param loginPost 
	 * @param callback
	 */
	public void transferGroup(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_TRANSFERGROUP, map,callback);
	}
	
	/**
	 * 查找用户的群列表
	 * @param loginPost 
	 * @param callback
	 */
	public void findUserGroupList(HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
//		asyncFormPostNoFileFms(API_IM_FINDUSERGROUPLIST, map,callback);
	}
	
	/**
	 *  好友-我的好友
	 * @param loginPost 
	 * @param callback
	 */
	public void findFriendList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_FRIENDLIST, map, callback);
	}
	/**
	 *  好友-群
	 * @param loginPost 
	 * @param callback
	 */
	public void findGroupList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_GROUPLIST, map, callback);
	}
	/**
	 *  群信息
	 * @param loginPost 
	 * @param callback
	 */
	public void findGroupInfo(Context context,HashMap<String, String> map, final ServiceCallback<CommonResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_GROUPDETAIL, map, callback);
	}
	/**
	 *  查找玩伴儿
	 * @param loginPost 
	 * @param callback
	 */
	public void findNewFriendList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_USERFRIENDLIST, map, callback);
	}
	/**
	 *  查找朋友-好朋友
	 * @param loginPost 
	 * @param callback
	 */
	public void findMyFriendList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_STATUSLIST, map, callback);
	}
	/**
	 *  查找朋友-找朋友
	 * @param loginPost 
	 * @param callback
	 */
	public void getFriendsList(Context context,HashMap<String, String> map, final ServiceCallback<CommonListResult<User>> callback){
		AsyncHttpHelp.httpGet(context, API_SEARCH_USERLIST, map, callback);
	}
	/**
	 *  加好友
	 * @param loginPost 
	 * @param callback
	 */
	public void addFriend(Context context,HashMap<String, String> map, final ServiceCallback<StringResult> callback){
		AsyncHttpHelp.httpGet(context, API_ADD_FRIEND, map, callback);
	}
}