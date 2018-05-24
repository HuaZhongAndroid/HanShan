package com.bm.tzj.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CourseListAdapter;
import com.bm.entity.HotGoods;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 课程列表
 * @author shiyt
 *
 */
public class CourseListAc extends BaseActivity implements OnClickListener,OnRefreshListener {
	private Context mContext;
	private ListView lv_course;
	private CourseListAdapter adapter;
	private List<HotGoods> list = new ArrayList<HotGoods>();
	private TextView tv_search;
	private EditText et_key;
	private LinearLayout ll_search;
	private String pageType;
	private String title;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private String keyWord="";
	City city =null;
	private ImageView img_noData;
	/** 分页器 */
	public Pager pager = new Pager(10);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_courselist);
		mContext = this;
		pageType=getIntent().getStringExtra("pageType");
		title=getIntent().getStringExtra("title");
		setTitleName(title);
		city = App.getInstance().getCityCode();
		init();
	}
	
	public void init(){
		img_noData = findImageViewById(R.id.img_noData);
		img_noData.setVisibility(View.GONE);
		mSwipyRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		
		tv_search=findTextViewById(R.id.tv_search);
		et_key=findEditTextById(R.id.et_key);
		ll_search=findLinearLayoutById(R.id.ll_search);	
		lv_course=(ListView) findViewById(R.id.lv_course);
		adapter=new CourseListAdapter(mContext,list);
		lv_course.setAdapter(adapter);
		
		//从搜索进入显示搜索框 其他隐藏
		if("搜索".equals(title)){
			ll_search.setVisibility(View.VISIBLE);
			
		}else{
			if("001".equals(getIntent().getStringExtra("tag"))){//门店
				getGoodsList();
			}else if("002".equals(getIntent().getStringExtra("tag"))){//多广告
				setTitleName("课程列表");
				getGoodslistadvert();
			}else{
				getGoodslisttype(title);
			}
		}
		
//		lv_course.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				Intent intent = new Intent(mContext, CourseDetailAc.class);
//				intent.putExtra("degree", list.get(position).goodsType);
//				intent.putExtra("goodsId", list.get(position).goodsId);
//				intent.putExtra("pageTag", "1");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品
//				if("001".equals(getIntent().getStringExtra("tag"))){//门店
//					intent.putExtra("pageType","StoreListAc");
////					intent.putExtra("position",position+"");
//					intent.putExtra("lon", getIntent().getStringExtra("lon"));
//					intent.putExtra("lat", getIntent().getStringExtra("lat"));
//				}else{
//					intent.putExtra("pageType","CourseListAc");
//
//				}
//				intent.putExtra("info", list.get(position));
//
//				startActivity(intent);
//			}
//		});
		tv_search.setOnClickListener(this);
		
	}
	/**
	 * 获取推荐课程
	 */
	public void getHotGoods(String goodsName){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		if(null != App.getInstance().getUser()){
			map.put("babyAge", App.getInstance().getUser().babyAge+"");
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		map.put("goodsName", goodsName);//每页展示条数
		UserManager.getInstance().getTzjgoodsHotgoods(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				MainAc.intance.dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				hideProgressDialog();
				if(null!=obj.data){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
	}
	/**
	 * 获取城市营地
	 */
	public void getGoodslisttype(String titles){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		if(null != App.getInstance().getUser()){
			map.put("babyAge", App.getInstance().getUser().babyAge);//宝贝年龄
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		if(titles.equals("城市营地")){
			map.put("goodsType", "1");//类别
		}else if(titles.equals("周末户外")){
			map.put("goodsType", "3");//类别
		}else if(titles.equals("暑期大露营")){
			map.put("goodsType", "2");//类别
		}
		
		
		UserManager.getInstance().getTzjgoodsGoodslisttype(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
					
				}
				setData();
			}
		});
	}
	
	private void setData() {
		if (list.size() == 0) {
			img_noData.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			lv_course.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 获取门店列表
	 */
	public void getGoodsList(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		if(null != App.getInstance().getUser()){
			map.put("babyAge", App.getInstance().getUser().babyAge);//宝贝年龄
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		map.put("storeId", getIntent().getStringExtra("storeId"));//门店id
		UserManager.getInstance().getTzjgoodsGoodslist(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
	}
	/**
	 * 获取门多广告商品列表
	 */
	public void getGoodslistadvert(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		map.put("boardId", getIntent().getStringExtra("boardId"));//广告id
		if(null != App.getInstance().getUser()){
			map.put("babyAge", App.getInstance().getUser().babyAge+"");
		}
		
//		babyAge
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		UserManager.getInstance().getTzjgoodsGoodslistadvert(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
	}
 
	
	@Override
	public void onClick(View v) {
		keyWord=et_key.getText().toString();
		switch (v.getId()) {
		case R.id.tv_search:  //搜索
			list.clear();
			pager.setFirstPage();
			getHotGoods(keyWord);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((CourseListAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							 if("001".equals(getIntent().getStringExtra("tag"))){//门店
									getGoodsList();
								}else if("002".equals(getIntent().getStringExtra("tag"))){//多广告
									getGoodslistadvert();
								}else{
									getGoodslisttype(title);
								}
//							 if("001".equals(getIntent().getStringExtra("tag"))){
//									getGoodsList();
//								}else{
//									if("搜索".equals(title)){
//										getHotGoods(keyWord);
//									}else{
//										getGoodslisttype(title);
//									}
//								}

							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

							
						}
					});
				}
			}, 2000);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((CourseListAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if("001".equals(getIntent().getStringExtra("tag"))){//门店
								getGoodsList();
							}else if("002".equals(getIntent().getStringExtra("tag"))){//多广告
								getGoodslistadvert();
							}else{
								getGoodslisttype(title);
							}
//							if("001".equals(getIntent().getStringExtra("tag"))){
//								getGoodsList();
//							}else{
//								if("搜索".equals(title)){
//									getHotGoods(keyWord);
//								}else{
//									getGoodslisttype(title);
//								}
//							}
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}
}
