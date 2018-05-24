package com.easemob.easeui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
//                int avatarResId = Integer.parseInt(user.getAvatar());
            	System.out.println("王海燕"+user.getAvatar());
                ImageLoader.getInstance().displayImage(user.getAvatar(), imageView,headOptions);
            } catch (Exception e) {
                //正常的string路径
            	if(user.getAvatar().length()>0){
            		System.out.println("王海燕1"+user.getAvatar());
            		ImageLoader.getInstance().displayImage(user.getAvatar(), imageView,headOptions);
            	}else{
            		imageView.setImageResource(R.drawable.default_avatar);
            	}
            }
        }else{
        	imageView.setImageResource(R.drawable.default_avatar);
        }
    }
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null){
        		if(user.getNick() != null){
            		textView.setText(user.getNick());
            	}else if(user.getUsername() != null){
            		textView.setText(user.getUsername());
            	}
        	}else{
        		textView.setText(username);
        	}
        	
        }
    }
    
}
