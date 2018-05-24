package com.bm.im.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



/**
 * 
 * 
 * Android 本地广播  用来集成IM  用户信息同步使用
 * @author wangqiang
 *
 */
public class LocalReceiver extends BroadcastReceiver {

	
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "received in LocalReceiver", Toast.LENGTH_SHORT)
				.show();
		
		

	}

	
	

}
