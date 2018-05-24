package com.bm.im.fm;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.api.IMService;
import com.bm.entity.User;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;
import com.squareup.picasso.Picasso;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
    	DisplayImageOptions	headOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_avatar)
		.showImageForEmptyUri(R.drawable.default_avatar)
		.showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		// .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
		.bitmapConfig(Bitmap.Config.RGB_565).build();
        if(user != null && user.getAvatar() != null){
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                if(user.getAvatar().contains("http://")){
                	if(user.getAvatar().replace("http://", "").substring(0, 1).equals("f")){
                		getImage(username, imageView,context,headOptions);
                	}else{
                		ImageLoader.getInstance().displayImage(user.getAvatar(), imageView,headOptions);
                	}
                }else{
                	getImage(username, imageView,context,headOptions);
                }
                
            } catch (Exception e) {
                //正常的string路径
            	if(user.getAvatar().length()>0){
            		if(user.getAvatar().contains("http://")){
                    	if(user.getAvatar().replace("http://", "").substring(0, 1).equals("f")){
                    		getImage(username, imageView,context,headOptions);
                    	}else{
                    		ImageLoader.getInstance().displayImage(user.getAvatar(), imageView,headOptions);
                    	}
                    }else{
                    	getImage(username, imageView,context,headOptions);
                    }
            	}else{
            		imageView.setImageResource(R.drawable.default_avatar);
            	}
            }
        }else{
        	getImage(username, imageView,context,headOptions);
        }
    }
    /**
     * 设置群头像
     * @param username
     */
    public static void setGroupAvatar(Context context, String username, ImageView imageView){
    	DisplayImageOptions	headOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ease_groups_icon)
		.showImageForEmptyUri(R.drawable.ease_groups_icon)
		.showImageOnFail(R.drawable.ease_groups_icon).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		// .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
		.bitmapConfig(Bitmap.Config.RGB_565).build();
    	getGroupInfo(username, imageView,context,headOptions);
    }
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(Context context,String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null){
        		if(user.getNick() != null){
            		textView.setText(user.getNick());
            	}else if(user.getUsername() != null){
            		getName(context,user.getUsername(), textView);
            	}
        	}else{
        		getName(context,username, textView);
        	}
        	
        }
    }
    public static void getName(Context context,final String toChatUsername,final TextView tv){
    	HashMap<String, String> map = new HashMap<String, String>();
		map.put("userIdList", toChatUsername);
		IMService.getInstance().getImUserInfoList(context,map, new ServiceCallback<CommonListResult<User>>() {
			
			@Override
			public void error(String msg) {
				tv.setText(toChatUsername+"");
			}
			
			@Override
			public void done(int what, CommonListResult<User> obj) {
				if(null!=obj.data){
					tv.setText(obj.data.get(0).nickname);
				}else{
					tv.setText(toChatUsername+"");
				}
			}
		});
    }
    public static void getImage(final String toChatUsername,final ImageView imageView,final Context context,final DisplayImageOptions	headOptions){
    	HashMap<String, String> map = new HashMap<String, String>();
		map.put("userIdList", toChatUsername);
		IMService.getInstance().getImUserInfoList(context,map, new ServiceCallback<CommonListResult<User>>() {
			
			@Override
			public void error(String msg) {
				Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
			}
			
			@Override
			public void done(int what, CommonListResult<User> obj) {
				if(null!=obj.data){
					ImageLoader.getInstance().displayImage(obj.data.get(0).avatar, imageView,headOptions);
				}else{
					Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
				}
			}
		});
    }
    
	/**
	 * 群信息
	 */
	private static void getGroupInfo(final String toChatUsername,final ImageView imageView,final Context context,final DisplayImageOptions	headOptions) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("groupId",toChatUsername);
		IMService.getInstance().findGroupInfo(context, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				Picasso.with(context).load(R.drawable.ease_groups_icon).into(imageView);
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				if(null!=obj.data){
					ImageLoader.getInstance().displayImage(obj.data.groupPic, imageView,headOptions);
				}else{
					Picasso.with(context).load(R.drawable.ease_groups_icon).into(imageView);
				}
			}
		});
	}
}
