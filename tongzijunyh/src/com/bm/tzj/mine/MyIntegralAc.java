package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyAccountBalanceAdapter;
import com.bm.entity.Order;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 我的积分
 * 
 * @author shiyt
 * 
 */
public class MyIntegralAc extends BaseActivity implements OnRefreshListener {
	private Context context;
	private ListView lv_integral;
	private List<Order> list = new ArrayList<Order>();
	private MyAccountBalanceAdapter adapter;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	public Pager pager = new Pager(10);
	private ImageView img_noData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_integral);
		context = this;
		setTitleName("积分明细");
		initView();
	}

	public void initView() {
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);

		lv_integral = findListViewById(R.id.lv_integral);
		img_noData = findImageViewById(R.id.img_noData);
		adapter = new MyAccountBalanceAdapter(context, list,"2");
		lv_integral.setAdapter(adapter);
		getMyIntegral();
	}
	
//	/**
//	 * 获取积分信息
//	 */
//	public void getIntegralInfo(){
//		HashMap<String, String> map = new HashMap<String, String>();
//		if(null == App.getInstance().getUser()){
//			map.put("userId", "0");
//		}else{
//			map.put("userId", App.getInstance().getUser().userid);
//			
//		}
//		map.put("pageNum", pager.nextPageToStr());//查询页数
//		map.put("pageCount", "10");//每页展示条数
//		
//		getMyIntegral();;
//	}
	/**
	 * 我的积分
	 */
	private void getMyIntegral() {
		if(null == App.getInstance().getUser()){
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
			
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		showProgressDialog();
		UserManager.getInstance().getMyselfMyIntegral(context, map, new  ServiceCallback<CommonListResult<Order>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<Order> obj) {
				hideProgressDialog();
				if(null!=obj.data){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
		
//		for (int i = 0; i < 5; i++) {
//			Model info = new Model();
//			info.name = "[第" + (i + 3) + "门晋级课程] 滚筒+鸟巢+西藏之课程";
//			info.address = "上海龙之梦" + (i + 3);
//			info.time = "2015-12-" + "1" + (i + 3) + " 10:30-12:30";
//			info.money = "￥9" + (i + 3);
//			info.degree = i+"";
//			list.add(info);
//		}
		
		setData();
	}
	
	private void setData() {
		if (list.size() == 0) {
			img_noData.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			lv_integral.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MyIntegralAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							 getMyIntegral();
							 mSwipyRefreshLayout.setRefreshing(false);

							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

							// map.put("pageNum", pager.nextPage() + "");// 页码
							// pager.setCurrentPage(pager.nextPage(),
							// list.size());
						}
					});
				}
			}, 2000);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MyIntegralAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 getMyIntegral();

							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}
}
