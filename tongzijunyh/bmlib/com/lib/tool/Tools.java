package com.lib.tool;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
/**
 * 基本工具类
 *
 */
public class Tools {
    static final String TAG = "Tools";
    
	/**
	 * 获取当前应用程序的版本名称(versionName)
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
		String appVersion = "";
		PackageManager manager = context.getPackageManager();  
		try {  
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);  
			appVersion = info.versionName; // 版本名   
			int currentVersionCode = info.versionCode; // 版本号   
			System.out.println(currentVersionCode + " " + appVersion);  
		} catch (NameNotFoundException e) {  
			// TODO Auto-generated catch blockd   
			e.printStackTrace();  
		}
		return appVersion;  
	}
	
	/**
	 * 获取当前应用程序的版本号(versionCode)
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){
		int appVersion = 0;
		PackageManager manager = context.getPackageManager();  
		try {  
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);  
			 appVersion = info.versionCode; // 版本号   
		} catch (NameNotFoundException e) {  
			// TODO Auto-generated catch blockd   
			e.printStackTrace();  
		}
		return appVersion;  
	}
    
    /**
     * 判断Network是否开启(包括移动网络和wifi)
     * @return
     */
    public static boolean isNetworkEnabled(Context context) {
        return (isWIFIEnabled(context) || isTelephonyEnabled(context));
    }

    /**
     * 判断移动网络是否开启
     * @return
     */
    public static boolean isTelephonyEnabled(Context context) {
        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
            }
        }
        return enable;
    }

    /**
     * 判断wifi是否开启
     */
    public static boolean isWIFIEnabled(Context context) {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            enable = true;
        }
        return enable;
    }
    
    /**
     * 判断是否联网
     */
    public static boolean isConnection(Context context) {
        boolean enable = false;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if(netInfo != null) {
            enable = true;
        }
        return enable;
    }
}
