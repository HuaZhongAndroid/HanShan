package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.adapter.MyYouhuiquanAdapter;
import com.bm.entity.Youhuiquan;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.CourseListAc;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.lib.tool.Pager;
import com.lib.widget.RefreshViewPD;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 我的优惠券
 * 
 */
public class MyYouhuiquanListFrameLayout extends FrameLayout implements
		MyYouhuiquanAdapter.OnSeckillClick,OnRefreshListener {
	private Context context;
	private RefreshViewPD refresh_view;
	private ListView lv_listCourse;
	private String state;
	private ImageView img_noData;
	private MyYouhuiquanAdapter adapter;
	private List<Youhuiquan> list = new ArrayList<Youhuiquan>();
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);
	Intent intent = null;
	private int pos=-1;
	
	private boolean isLoading = false;

	public MyYouhuiquanListFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		MyYouhuiquanAc.intance.isHaveData(false);
		init();
	}

	public MyYouhuiquanListFrameLayout(Context context, String state) {
		super(context);
		this.context = context;
		this.state = state;
		init();
	}

	public void init() {
		// instance=this;
		LayoutInflater.from(context).inflate(R.layout.my_youhuiquan_framelayout,
				this);

		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		
		img_noData=(ImageView) findViewById(R.id.img_noData);
		lv_listCourse = (ListView) findViewById(R.id.lv_listCourse);

		adapter = new MyYouhuiquanAdapter(context, list,state);
		lv_listCourse.setAdapter(adapter);
		adapter.setOnSeckillClick(this);

		// adapter.setClickDelete(new ClickDelete() {
		// @Override
		// public void delete(String id) {
		// delActivityId = id;
		// UtilDialog.dialogTwoBtnResultCode(context, "您确定要删除该条活动数据",
		// "取消","确定",handler,2);
		// // deleteAc(id);
		// }
		// });
	}

	public void reFresh() {
		pager.setFirstPage();
		list.clear();
		getYouhuiquanList();
	}

	private void getYouhuiquanList() {
		if(isLoading)
			return;
		isLoading = true;
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
			
		}
		map.put("status", state);//课程状态   0 所有  1 未开始  2 进行中  3 已结束  4 待付款
		map.put("pageCount", "10");//每页展示条数
		map.put("pageNum", pager.nextPageToStr());
		
		MyYouhuiquanAc.intance.showProgressDialog();
		UserManager.getInstance().getMyYouhuiquan(context, map, new ServiceCallback<CommonListResult<Youhuiquan>>() {
			
			@Override
			public void error(String msg) {
				MyYouhuiquanAc.intance.hideProgressDialog();
				MyYouhuiquanAc.intance.dialogToast(msg);
				MyYouhuiquanAc.intance.isHaveData(false);
				isLoading = false;
			}
			
			@Override
			public void done(int what, CommonListResult<Youhuiquan> obj) {
				MyYouhuiquanAc.intance.hideProgressDialog();
				if(obj.data.size()>0){
					list.addAll(obj.data);
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					setData();
					MyYouhuiquanAc.intance.isHaveData(true);
				}
					
				isLoading = false;
			}
		});
	}
	
	private void setData() {
		if (list.size() == 0) {
			img_noData.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			lv_listCourse.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onSeckillClick(int position, int type) {
		pos=position;
		if (type == 1) {// 立即领取
			lijilingqu(list.get(position));
		} else if (type == 2) {// 立即使用
			if("-1".equals(list.get(position).goodsType))
			{
				intent = new Intent(context, MainAc.class);
				intent.putExtra("TabSelection", 0);
				MyYouhuiquanAc.intance.startActivity(intent);
			}
			if("1".equals(list.get(position).goodsType))
			{
				intent = new Intent(context, CourseListAc.class);
				intent.putExtra("title", "城市营地");
				MyYouhuiquanAc.intance.startActivity(intent);
			}
			if("2".equals(list.get(position).goodsType))
			{
				intent = new Intent(context, CourseListAc.class);
				intent.putExtra("title", "周末户外");
				MyYouhuiquanAc.intance.startActivity(intent);
			}
			if("3".equals(list.get(position).goodsType))
			{
				intent = new Intent(context, CourseListAc.class);
				intent.putExtra("title", "暑期大露营");
				MyYouhuiquanAc.intance.startActivity(intent);
			}
		}
	}
	
	/**
	 * 立即领取优惠券
	 * @param o
	 */
	private void lijilingqu(Youhuiquan o)
	{
		MyYouhuiquanAc.intance.showProgressDialog();
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("userid", App.getInstance().getUser().userid);
		map.put("couponid", o.couponid);
		AsyncHttpHelp.httpGet(context,BaseApi.API_receiveCoupon, map, new ServiceCallback<StringResult>(){
			@Override
			public void done(int what, StringResult obj) {
				MyYouhuiquanAc.intance.hideProgressDialog();
				reFresh();
				MyYouhuiquanAc.intance.dialogToast("领取成功");
			}
			@Override
			public void error(String msg) {
				MyYouhuiquanAc.intance.hideProgressDialog();
				MyYouhuiquanAc.intance.dialogToast(msg);
			}});
	}
	
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MyYouhuiquanAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							list.clear();
							getYouhuiquanList();
							// mSwipyRefreshLayout.setRefreshing(false);
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							
//							map.put("pageNum", pager.nextPage() + "");// 页码
//							pager.setCurrentPage(pager.nextPage(), list.size());
						}
					});
				}
			}, 2000);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MyYouhuiquanAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getYouhuiquanList();
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							

						}
					});
				}
			}, 2000);
		}
	}

}
