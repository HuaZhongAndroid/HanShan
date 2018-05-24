package com.bm.tzj.fm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.adapter.FindAdapter;
import com.bm.entity.PlayMateList;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
import com.bm.tzj.ts.SendContentAc;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 
 * 探索
 * 
 * @author jiangsh
 * 
 */
@SuppressLint("NewApi")
public class FindFm extends Fragment implements OnClickListener,FindAdapter.OnSeckillClick,OnRefreshListener {
	private Context context;
	private int index;//记录点击第几项回复
	private TextView tv_a;// 玩伴儿
	private TextView tv_b;// 探索
//	private TextView tv_c;// 发帖
	private ImageView img_c;//发帖
	private FrameLayout fl_send;

	private View v_1, v_2, v_3;
	private ListView lv_content;// 显示内容
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private ImageView img_noData;
	/** 分页器 */
	public Pager pager = new Pager(10);
	public static FindFm intance;
	private FindAdapter adapter;
	public List<PlayMateList> list = new ArrayList<PlayMateList>();

	public  String tag="2";//标记是1玩伴儿还是2探索
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageLayout = inflater.inflate(R.layout.fm_find, container,
				false);

		context = this.getActivity();
		initView(messageLayout);
		intance = this;
		// App.toast("HealthRecordFm");
		return messageLayout;
	}
	
	/**
	 * 初始化数据
	 * 
	 * @param v
	 */
	private void initView(View v) {
		img_noData = (ImageView) v.findViewById(R.id.img_noData);
		img_noData.setVisibility(View.GONE);
		mSwipyRefreshLayout = (SwipyRefreshLayout)v.findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		
		// 初始化view
		v_1 =(View) v.findViewById(R.id.v_1);
		v_2 =(View) v.findViewById(R.id.v_2);
		v_3 =(View) v.findViewById(R.id.v_3);
		tv_a = (TextView) v.findViewById(R.id.tv_a);
		tv_b = (TextView) v.findViewById(R.id.tv_b);
		img_c = (ImageView) v.findViewById(R.id.img_c);
		fl_send = (FrameLayout) v.findViewById(R.id.fl_send);
		
//		tv_c = (TextView) v.findViewById(R.id.tv_c);
		
		lv_content = (ListView) v.findViewById(R.id.lv_content);
		// 初始化监听
		tv_a.setOnClickListener(this);
		tv_b.setOnClickListener(this);
		fl_send.setOnClickListener(this);
//		img_c.setOnClickListener(this);
//		tv_c.setOnClickListener(this);

		adapter = new FindAdapter(context, list,tag);
		lv_content.setAdapter(adapter);
		adapter.setOnSeckillClick(this);
		RefreshDate(tag);
	}
	
	public void RefreshDate(String intTag){
		 pager.setFirstPage();
		 list.clear();
		 clearState();
		if(intTag.equals("1")){
			getWBList();
			v_1.setVisibility(View.VISIBLE);
			tv_a.setTextColor(getResources().getColor(R.color.app_dominantHue));
		}else if(intTag.equals("2")){
//			intTag.equals("11");
			getTSList();
			v_2.setVisibility(View.VISIBLE);
			tv_b.setTextColor(getResources().getColor(R.color.app_dominantHue));
		}
	}
	
	
	/**
	 * 获取玩伴列表
	 */
	public void getWBList(){
		MainAc.intance.showProgressDialog();
		final HashMap<String, String> map = new HashMap<String, String>();
		if(null != App.getInstance().getUser()){
			map.put("userid", App.getInstance().getUser().userid);//用户id
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		UserManager.getInstance().getPlaymateList(context, map, new ServiceCallback<CommonListResult<PlayMateList>>() {
			
			final String CACHEKEY = "FindFm_getWBList";
			
			@Override
			public void error(String msg) {
				//从缓存中读取数据
				CommonListResult<PlayMateList> obj = new CommonListResult<PlayMateList>(){}; 
				Type type = obj.getClass().getGenericSuperclass();
				obj = (CommonListResult<PlayMateList>)CacheUtil.read(context, CACHEKEY, map, type);
				if(obj != null)
				{
					doResult(obj);
					return;
				}
				
				//没有缓存时执行下面的逻辑
				MainAc.intance.dialogToast(msg);
				MainAc.intance.hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<PlayMateList> obj) {
				doResult(obj);
				
				//缓存结果
				CacheUtil.save(context, CACHEKEY,map, obj);
			}
			
			private void doResult(CommonListResult<PlayMateList> obj)
			{
				MainAc.intance.hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
	}
	/**
	 * 获取探索列表
	 */
	public void getTSList(){
		MainAc.intance.showProgressDialog();
		final HashMap<String, String> map = new HashMap<String, String>();
		City city  =App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		if(null != App.getInstance().getUser()){
			map.put("userid", App.getInstance().getUser().userid);
		}
		map.put("pageCount", "10");//每页展示条数
		UserManager.getInstance().getProbeList(context, map, new ServiceCallback<CommonListResult<PlayMateList>>() {
			
			final String CACHEKEY = "FindFm_getTSList";
			
			@Override
			public void error(String msg) {
				//从缓存中读取数据
				CommonListResult<PlayMateList> obj = new CommonListResult<PlayMateList>(){}; 
				Type type = obj.getClass().getGenericSuperclass();
				obj = (CommonListResult<PlayMateList>)CacheUtil.read(context, CACHEKEY, map, type);
				if(obj != null)
				{
					doResult(obj);
					return;
				}
				
				//没有缓存时执行下面的逻辑
				MainAc.intance.dialogToast(msg);
				MainAc.intance.hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<PlayMateList> obj) {
				doResult(obj);
				
				//缓存结果
				CacheUtil.save(context, CACHEKEY, map, obj);
			}
			
			private void doResult(CommonListResult<PlayMateList> obj)
			{
				MainAc.intance.hideProgressDialog();
				
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
			lv_content.setVisibility(View.VISIBLE);
		}
		adapter.notifu(tag);
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		RefreshDate(tag);
//	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_a:// 玩伴儿
			tag="1";
			clearState();
			 pager.setFirstPage();
			 list.clear();
			getWBList();
			v_1.setVisibility(View.VISIBLE);
			tv_a.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case R.id.tv_b:// 探索
			tag="2";
			clearState();
			 pager.setFirstPage();
			 list.clear();
			getTSList();
			v_2.setVisibility(View.VISIBLE);
			tv_b.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case R.id.fl_send:// 发帖
			intent = new Intent(context,SendContentAc.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 清除状态
	 */
	private void clearState() {
		v_1.setVisibility(View.GONE);
		v_2.setVisibility(View.GONE);

		tv_a.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_b.setTextColor(getResources().getColor(R.color.course_bg_textColor));
	}
	
	//清除状态
//	private void clearState(){
//		tv_a.setBackgroundColor(context.getResources().getColor(R.color.wbe));
//		tv_a.setTextColor(context.getResources().getColor(R.color.wbe_txt));
//		
//		tv_b.setBackgroundColor(context.getResources().getColor(R.color.wbe));
//		tv_b.setTextColor(context.getResources().getColor(R.color.wbe_txt));
//	}

	@Override
	public void onSeckillClick(int position, int tag) {
		index=position;
		if(tag==3){
			//UtilDialog.dialogPromtMessage(context);
		}
	}
	
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MainAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							// mSwipyRefreshLayout.setRefreshing(false);
							 if("1".equals(tag)){
								 getWBList();
							 }else {
								getTSList(); 
							 }
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
					((MainAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if("1".equals(tag)){
								 getWBList();
							 }else {
								getTSList(); 
							 }
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
						}
					});
				}
			}, 2000);
		}
	}
}
