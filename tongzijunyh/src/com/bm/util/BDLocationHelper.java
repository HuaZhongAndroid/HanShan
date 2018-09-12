package com.bm.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lib.tool.SharedPreferencesHelper;

public class BDLocationHelper {
	private static LocationClient bdLocationClient;
	//private static GeofenceClient bdGeofenceClient;
	private static BDLocationListener bdLocationListener;
	private static ArrayList<MyLocationListener> listeners = new ArrayList<BDLocationHelper.MyLocationListener>(); 

	/**
	 * 定位一次
	 * @param context 
	 * @param listener
	 */
	public static void locate(Context context, final MyLocationListener listener){
		//初始化
//		if(bdLocationClient == null || bdLocationListener==null){
			//初始化定位
			bdLocationClient = new LocationClient(context);
			bdLocationListener = new BDLocationListener() {

				@Override
				public void onReceiveLocation(BDLocation location) {
					bdLocationClient.stop();
					//Receive Location 
					StringBuffer sb = new StringBuffer(256);
					sb.append("time : ");
					sb.append(location.getTime());
					sb.append("\nerror code : ");
					sb.append(location.getLocType());
					sb.append("\nlatitude : ");
					sb.append(location.getLatitude());
					sb.append("\nlontitude : ");
					sb.append(location.getLongitude());
					sb.append("\nradius : ");
					sb.append(location.getRadius());
					if (location.getLocType() == BDLocation.TypeGpsLocation){
						sb.append("\nspeed : ");
						sb.append(location.getSpeed());
						sb.append("\nsatellite : ");
						sb.append(location.getSatelliteNumber());
						sb.append("\ndirection : ");
						sb.append("\naddr : ");
						sb.append(location.getAddrStr());
					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
						sb.append("\naddr : ");
						sb.append(location.getAddrStr());
						//运营商信息
						sb.append("\noperationers : ");
					}
					
					Log.d("fffffffff","addr:"+sb.toString());
					
					if(location != null){
						LocationInfo info = new LocationInfo();
						info.district = location.getDistrict();
						info.province = location.getProvince();
						info.city = location.getCity();
						info.lat = location.getLatitude();
						info.lng = location.getLongitude();
						info.address = location.getAddrStr();
						SharedPreferencesHelper.saveJSON("cache_location", info);
					}
//					for(MyLocationListener l : listeners){
//						try{
//							l.success(location);
//						}catch(Exception e){
//							//listeners.remove(l);
//							e.printStackTrace();
//						}
//					}
					
					if(listener != null)
						listener.success(location);
				}


				
			};
			bdLocationClient.registerLocationListener(bdLocationListener);
			//bdGeofenceClient = new GeofenceClient(context);
//		}
//		if(!listeners.contains(listener)){
//			listeners.add(listener);
//		}
//		listeners.add(listener);
		if(!bdLocationClient.isStarted()){
			//开始定位
			LocationClientOption option = new LocationClientOption();
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(10000); // 10分钟扫描1次
			// 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
			option.setAddrType("all");
			// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
//			option.setPoiExtraInfo(true);
			// 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
			option.setProdName("通过GPS定位我当前的位置");
			// 禁用启用缓存定位数据
			option.disableCache(true);
			// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
//			option.setPoiNumber(3);
			// 设置定位方式的优先级。
			// 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
			option.setPriority(LocationClientOption.GpsFirst);
			bdLocationClient.setLocOption(option);
			bdLocationClient.start();
		}
	}

	public interface MyLocationListener {
		public void success(BDLocation location);
	}

	public static class LocationInfo{
		public double lat = -1;
		public double lng = -1;
		public String city;
		public String province;
		public String address;
		/**
		 * 区县
		 */
		public String district;
	}
	
	

	/**
	 * 
	 * 得到缓存到share里面的定位信息
	 * @return
	 */
	public static LocationInfo getCacheLocation(){
		LocationInfo info = SharedPreferencesHelper.getJSON("cache_location", LocationInfo.class);
		if (info==null){
		return new 	LocationInfo();
		}
		return info;
	}

//	/**
//	 * 移除监听器
//	 * @param listener
//	 */
//	public void removeListener(MyLocationListener listener){
//		listeners.remove(listener);
//	}
//
//	/**
//	 * 添加监听器
//	 * @param listener
//	 */
//	public void addListener(MyLocationListener listener){
//		if(!listeners.contains(listener)){
//			listeners.add(listener);
//		}
//	}
	
	public static void stopLocation(){
		bdLocationClient.stop();
	}
	
}
