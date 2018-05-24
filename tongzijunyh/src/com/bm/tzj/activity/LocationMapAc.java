package com.bm.tzj.activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bm.base.BaseActivity;
import com.richer.tzj.R;

/**
 * 定位地图
 * 
 * @author wanghy
 * 
 */
public class LocationMapAc extends BaseActivity implements OnMarkerClickListener {
	/**
	 * 地图
	 */
	private Context context;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private InfoWindow mInfoWindow;
	private String locationLongitude;
	private String locationLatitude;
//	private String locationStadiumName;
//	private String locationAddress;
	

	
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_map);
		context = this;
		locationLongitude = getIntent().getStringExtra("longitude");//经度
		locationLatitude = getIntent().getStringExtra("latitude");//纬度
//		locationStadiumName = getIntent().getStringExtra("stadiumName");
//		locationAddress = getIntent().getStringExtra("address");
		setTitleName("地图");
		initView();
	}

	public void initView() {
		mMapView = (MapView) findViewById(R.id.map_view);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMarkerClickListener(this);
		initData();
	}

	private void initData() {
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		setData();
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		/**
		 * 地图覆盖物
		 */
		String lng = locationLongitude;
		String lat = locationLatitude;
		initOverlay(lng, lat);
	}

	public void initOverlay(String lng, String lat) {
		// Intent intent = this.getIntent();

		LatLng llA = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lng));
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9);
		
		mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

		mBaiduMap.addOverlay(ooA);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(Double
				.parseDouble(lat), Double.parseDouble(lng)));
		mBaiduMap.setMapStatus(u);
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 回收 bitmap 资源
		bdA.recycle();
	}
	BitmapDescriptor bitDes;
	BitmapDescriptorFactory mf;
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (bitDes != null) {
			bitDes.recycle();
			bitDes = null;
		}
		if (mf != null) {
			mf = null;
		}
		final int index = marker.getZIndex();
		Button btn = new Button(getApplicationContext());
//		btn.setText("" + locationStadiumName + "\n" + "地址："  + locationAddress);
		btn.setBackgroundColor(getResources().getColor(
				android.R.color.darker_gray));

		mf = new BitmapDescriptorFactory();
		bitDes = mf.fromView(btn);
		LatLng ll = marker.getPosition();
		Point p = mBaiduMap.getProjection().toScreenLocation(ll);
		p.y -= 47;
		LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);

		mInfoWindow = new InfoWindow(bitDes, ll, -47,
				new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {

					}
				});

		mBaiduMap.showInfoWindow(mInfoWindow);
		return false;
	}
}
