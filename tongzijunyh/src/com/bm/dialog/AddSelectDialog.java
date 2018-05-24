package com.bm.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bm.entity.DistrictModel;
import com.bm.entity.Province;
import com.byl.datepicker.wheelview.OnWheelChangedListener;
import com.byl.datepicker.wheelview.WheelView;
import com.byl.datepicker.wheelview.adapter.ArrayWheelAdapter;
import com.richer.tzj.R;

/**
 * 
 * 省市区选择 dialog
 * number PickDialo
 * @author wangqiang 2014-8-13
 * 
 */
public class AddSelectDialog extends Dialog implements OnWheelChangedListener{

	private Context context;
	private Button btn_cancel;
	private Button btn_confirm;
	private TextView tv;
	private List<Province> list;
	
	
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	public AddSelectDialog(Context context,CancleClick cancleClick,List<Province> list) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.cancleClick = cancleClick;
		this.list = list;
		getWindow().getAttributes().windowAnimations = R.style.SlideUpDialogAnimation;
		
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.BOTTOM | Gravity.CENTER;
		getWindow().setAttributes(wl);

		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_addrselect_view);
		// 设置视图宽度为屏幕宽度
		View root = findViewById(R.id.root);
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) root
				.getLayoutParams();
		lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
		root.setLayoutParams(lp);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		
		setUpViews();
		setUpListener();
		setUpData();
		
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Log.e("tag111", arg0 + "");
				cancleClick.click(arg0);
			}
		});

		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String addr = showSelectedResult();
				cancleClick.clickConform(addr,mCurrentProviceName,mCurrentCityName,mCurrentDistrictName);
			}
		});

	}
	
	CancleClick cancleClick;
	
	public interface CancleClick{
		public void click(View arg);
		public void clickConform(String arg,String provinceName,String cityName,String areaName);
	}

	
	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	private String[] mProvinceDatasId;
	/**
	 * key - 省 value - 市
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mCitisDatasMapId = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mDistrictDatasId = new HashMap<String, String[]>();
	
	/**
	 * key - 区 values - 邮编
	 */
	private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentDistrictName ="";
	
	/**
	 * 省份Id
	 */
	private String mCurrentProvinceId;
	
	/**
	 * 城市Id
	 */
	private String mCurrentCityId;
	
	/**
	 * 地区Id
	 */
	private String mCurrentDistrictId;
	
	/**
	 * 当前区的邮政编码
	 */
	private String mCurrentZipCode ="";
	
	/**
	 * 解析省市区的XML数据
	 */
	
    private void initProvinceDatas()
	{
    	List<Province> provinceList = null;
		provinceList = list;
		
		try {
		if (provinceList!= null && !provinceList.isEmpty()) {
			mCurrentProviceName = provinceList.get(0).regionName;
			mCurrentProvinceId=provinceList.get(0).regionId;
			List<Province> cityList = provinceList.get(0).listCity;
			if (cityList!= null && !cityList.isEmpty()) {
				mCurrentCityName = cityList.get(0).regionName;
				mCurrentCityId= cityList.get(0).regionId;
				List<Province> districtList = cityList.get(0).listArea;
				mCurrentDistrictName = districtList.get(0).regionName;
				mCurrentDistrictId= districtList.get(0).regionId;
			}
		}
		mProvinceDatas = new String[provinceList.size()];
		mProvinceDatasId=new String[provinceList.size()];
    	for (int i=0; i< provinceList.size(); i++) {
    		// 遍历所有省的数据
    		mProvinceDatas[i] = provinceList.get(i).regionName;
    		mProvinceDatasId[i]=provinceList.get(i).regionId;
    		List<Province> cityList = provinceList.get(i).listCity;
    		String[] cityNames = new String[cityList.size()];
    		String[] cityId=new String[cityList.size()];
    		for (int j=0; j< cityList.size(); j++) {
    			// 遍历省下面的所有市的数据
    			cityNames[j] = cityList.get(j).regionName;
    			cityId[j]=cityList.get(j).regionId;
    			List<Province> districtList = cityList.get(j).listArea;
    			String[] distrinctNameArray = new String[districtList.size()];
    			String[] distrinctIdArray = new String[districtList.size()];
    			DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
    			for (int k=0; k<districtList.size(); k++) {
    				// 遍历市下面所有区/县的数据
    				DistrictModel districtModel = new DistrictModel(districtList.get(k).regionName,districtList.get(k).regionId);
    				// 区/县对于的邮编，保存到mZipcodeDatasMap
    				distrinctArray[k] = districtModel;
    				distrinctNameArray[k] = districtModel.getName();
    				distrinctIdArray[k]=districtList.get(k).regionId;
    			}
    			// 市-区/县的数据，保存到mDistrictDatasMap
    			mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
    			mDistrictDatasId.put(cityId[j], distrinctIdArray);
    			
    		}
    		// 省-市的数据，保存到mCitisDatasMap
    		mCitisDatasMap.put(provinceList.get(i).regionName, cityNames);
    		mCitisDatasMapId.put(provinceList.get(i).regionId, cityId);
    	}
        } catch (Throwable e) {  
            e.printStackTrace();  
        } finally {
        	
        } 
	}
	
    
    
    private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
	}
	
	private void setUpListener() {
    	// 添加change事件
    	mViewProvince.addChangingListener(this);
    	// 添加change事件
    	mViewCity.addChangingListener(this);
    	// 添加change事件
    	mViewDistrict.addChangingListener(this);
    }
	
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		mCurrentCityId=mCitisDatasMapId.get(mCurrentProvinceId)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		
		if(mDistrictDatasMap.get(mCurrentCityName).length>0){
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
//			mCurrentDistrictId = mDistrictDatasId.get(mCurrentCityId)[0];
		}
	
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		mCurrentProvinceId=mProvinceDatasId[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		
		if(cities.length>0){
			updateAreas();
		}
	}
	
	private String showSelectedResult() {
		return mCurrentProviceName+" "+mCurrentCityName+" "+mCurrentDistrictName;
//		Toast.makeText(CaledarAc.this, "当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
//				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();
	}
	
	
	

}