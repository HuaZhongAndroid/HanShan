package com.bm.im.tool;

import android.os.Handler;
import android.util.Log;

import com.bm.app.App;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

public class IMTool {

	
	
	
	/**
	 * IM登录
	 */
  public static void loginIM(final Handler handler ){
	  if(App.getInstance().getUser()==null){
		  return;
	  }
		
		final String currentUsername  =/*"wangqiang"*/App.getInstance().getUser().userid;
		String currentPassword  = "123456";
		// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
					@Override
					public void onSuccess() {
//						if (!progressShow) {
//							return;
//						}
						// 登陆成功，保存用户名
						DemoHelper.getInstance().setCurrentUserName(currentUsername);
						// 注册群组和联系人监听
						DemoHelper.getInstance().registerGroupAndContactListener();

						// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
						// ** manually load all local groups and
					    EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						
						// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
								App.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity", "update current user nick fail");
						}
						//异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
						DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
						System.out.println("xxx-------Success");
						handler.sendEmptyMessage(0);
//						Toast.makeText(context,"登录成功",
//								Toast.LENGTH_SHORT).show();
//						if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//							pd.dismiss();
//						}
//						// 进入主页面
//						Intent intent = new Intent(LoginActivity.this,
//								MainActivity.class);
//						startActivity(intent);
//						
//						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
//						if (!progressShow) {
//							return;
//						}
//						runOnUiThread(new Runnable() {
//							public void run() {
//								//pd.dismiss();
//								Toast.makeText(context, getString(R.string.Login_failed) + message,
//										Toast.LENGTH_SHORT).show();
//							}
//						});
					}
				});
		
		
	}
}
