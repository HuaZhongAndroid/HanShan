package com.bm.im.api;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.bm.api.IMService;
import com.bm.entity.User;
import com.bm.im.tool.DemoHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.exceptions.EaseMobException;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;

import cz.msebera.android.httpclient.util.TextUtils;


public class ImApi {
	
	
	 public static  final int CODE_CRETE = 1001;
	 public static  final int CODE_ADDMEMBERS = 2001;
	
	
	 
	/**
	 * 
	 * 
	 *  // 给环信 设置头像
        EaseUserUtils.setUserNick(username, holder.nameView);
        //设置头像
        
	 * 
	 * 
	 * 
	 */
	
	 
	
	 
	 
	 /**
	  * 
	  * 点击单人聊天  同步用户信息
	  * @param user
	  */
	 public static void syncUserInfo(User user){
		   	String strUserId ="";
		 	if(!TextUtils.isEmpty(user.userid)){
		 		strUserId=user.userid;
		 	}else if (!TextUtils.isEmpty(user.friendUserId)){
		 		strUserId=user.friendUserId;
		 	}else if (!TextUtils.isEmpty(user.userId)){
		 		strUserId=user.userId;
		 	}
		 
		    EaseUser muser = new EaseUser(strUserId);
		    muser.setUsername(strUserId);
		    muser.setAvatar(user.avatar);
		    muser.setNick(user.nickname);
			DemoHelper.getInstance().saveContact(muser);
	 }
	 
	 	/**
	 	 * 
	 	 * 获取群成员  同步群组成员信息
	 	 * @param groupId
	 	 */
		public static void syncGroupUserInfo(final Context context,final String groupId) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					try {
						 EMGroup returnGroup  = EMGroupManager.getInstance().getGroupFromServer(groupId);
						 EMGroup  eMGroup=  EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
						 List<String>  listMenber = eMGroup.getMembers();
						 if(listMenber.size()>0){
							 getAppSys(context,listMenber);
						 }
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						// 更新本地数据
					
				}
			}).start();
			
			
		   
		}
		
		
		
		/**
		 * 调用系统api获得用户信息
		 * @param listMenber
		 */
		public static void getAppSys(Context context,String  userId,final Handler handler){
			if(userId == null){
				return;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userIdList", userId);
			IMService.getInstance().getImUserInfoList(context,map, new ServiceCallback<CommonListResult<User>>() {
				@Override
				public void error(String msg) {
					System.out.println("调用系统api错");
				}
				
				@Override
				public void done(int what, CommonListResult<User> obj) {
					if(obj.data.size()>0){
						for(int i = 0;i<obj.data.size();i++){
							syncUserInfo(obj.data.get(i));
							ImApi.syncUserInfo(obj.data.get(i));
							if(null !=handler){
								handler.sendEmptyMessage(2);
							}
						}
					}
				}
			});
		}
		
		/**
		 * 调用系统api获得用户信息
		 * @param listMenber
		 */
		private static void getAppSys(Context context,List<String>  listMenber){
			String userId=listMenber.toString().replace("[", "").replace("]","");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userIdList", userId);
			IMService.getInstance().getImUserInfoList(context,map, new ServiceCallback<CommonListResult<User>>() {
				@Override
				public void error(String msg) {
				}
				
				@Override
				public void done(int what, CommonListResult<User> obj) {
					if(obj.data.size()>0){
						for(int i = 0;i<obj.data.size();i++){
							syncUserInfo(obj.data.get(i));
						}
					}
				}
			});
		}
	
	/**
	 * 
	 * 创建群组
	 * @param groupName
	 * @param desc  群描述
	 * @param members  群成员
	 */
	public static void createGroup(final String groupName,final String desc,final String[] members,final Handler handler){
		new Thread(new Runnable() {
			@Override
			public void run() {
				//needApprovalRequired:如果创建的公开群用需要户自由加入，就传false。否则需要申请，等群主批准后才能加入，传true
				try {
					
					EMGroup group = EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, false,200);//需异步处理
//					group.setOwner(App.getInstance().getUser().userId);
					System.out.println("groupInfo:"+group.getGroupId());
					Message msg = new Message();
					msg.obj=group.getGroupId();
					msg.what=CODE_CRETE;
					handler.sendMessage(msg);
					//handler.sendEmptyMessage(CODE_CRETE);
					
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
    public static void addUsersInGroup( EMGroup group,final List<String> newmembers){
    	for(int i = 0;i<newmembers.size();i++){
    		addUsersToGroup(group,newmembers.get(i));
    	}
    }
	/**
	 * 群聊加人
	 * @param group
	 * @param newmembers
	 */
	private static void addUsersToGroup(final EMGroup group,final String newmember){
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				    try {
				    	EMGroup returnGroup = EMGroupManager.getInstance().getGroupFromServer(group.getGroupId());
						// 更新本地数据
						EMGroup  eMGroup=  EMGroupManager.getInstance().createOrUpdateLocalGroup(returnGroup);
				    	
				    	// 创建者调用add方法
						//  判断是群主直接假如群组  一般成员调用invite方法
				    	if (EMChatManager.getInstance().getCurrentUser().equals(group.getOwner())) {
						    EMGroupManager.getInstance().addUsersToGroup(group.getGroupId(),new String[]{newmember} );
						} else {
						EMGroupManager.getInstance().inviteUser(group.getGroupId(), new String[]{newmember}, null);
						}
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}).start();
		
	}
	
	/**
	 * 
	 * 群聊减人 --删除多个  需循环
	 * @param group
	 * @param newmembers
	 */
	public static void delUsersToGroup(final String groupId,final String username){
		new Thread(new Runnable() {
			@Override
			public void run() {
				    try {
				    	
				    	EMGroupManager.getInstance().removeUserFromGroup(groupId, username);//需异步处理
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}).start();
	}
	
	
	
	/**
	 * 修改群名称
	 * @param groupId
	 */
	public static void changeGroupName(final String groupId,final String changedGroupName){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//groupId 需要改变名称的群组的id
					//changedGroupName 改变后的群组名称
					EMGroupManager.getInstance().changeGroupName(groupId,changedGroupName);//需异步处理
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 退出群组
	 * @param groupId
	 */
	public static void qutiGroup(final String groupId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroupManager.getInstance().exitFromGroup(groupId);//需异步处理
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 解散群聊
	 * @param groupId
	 */
	public static void delGroup(final String groupId){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMGroupManager.getInstance().exitAndDeleteGroup(groupId);//需异步处理
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	/**
	 *获取群聊列表
	 * @param groupId
	 * @throws EaseMobException 
	 */
	public static List<EMGroupInfo> getGroupList(){
		try {
			return EMGroupManager.getInstance().getAllPublicGroupsFromServer();
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
