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

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.adapter.MyCourseAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Child;
import com.bm.entity.Course;
import com.bm.entity.HotGoods;
import com.bm.entity.Order;
import com.bm.tzj.kc.EvaluateShowAc;
import com.bm.tzj.kc.PayInfoAc;
import com.bm.tzj.kc.PayInfoAc2;
import com.bm.tzj.kc.PayInfoAc3;
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
 * 我的课程
 * 
 * @author wanghy
 * 
 */
public class MyCourseListFrameLayout extends FrameLayout implements
		MyCourseAdapter.OnSeckillClick,OnRefreshListener {
	private Context context;
	private RefreshViewPD refresh_view;
	private ListView lv_listCourse;
	private String state;
	private ImageView img_noData;
	private MyCourseAdapter adapter;
	private List<Course> list = new ArrayList<Course>();
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);
	Intent intent = null;
	private int pos=-1;
	
	private boolean isLoading = false;

	public MyCourseListFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		MyCourseAc.intance.isHaveData(false);
		init();
	}

	public MyCourseListFrameLayout(Context context, String state) {
		super(context);
		this.context = context;
		this.state = state;
		init();
	}

	public void init() {
		// instance=this;
		LayoutInflater.from(context).inflate(R.layout.my_course_framelayout,
				this);

		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		
		img_noData=(ImageView) findViewById(R.id.img_noData);
		lv_listCourse = (ListView) findViewById(R.id.lv_listCourse);

		adapter = new MyCourseAdapter(context, list,state);
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
		getCourseList();
	}

	private void getCourseList() {
		if(isLoading)
			return;
		isLoading = true;
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);
			
		}
		map.put("type", state);//课程状态   0 所有  1 未开始  2 进行中  3 已结束  4 待付款
		map.put("pageSize", "10");//每页展示条数
		map.put("currentPage", pager.nextPageToStr());
		
		MyCourseAc.intance.showProgressDialog();
		UserManager.getInstance().getTzjgoodsGoodscourseinfo(context, map, new ServiceCallback<CommonListResult<Course>>() {
			
			@Override
			public void error(String msg) {
				MyCourseAc.intance.hideProgressDialog();
				MyCourseAc.intance.dialogToast(msg);
				MyCourseAc.intance.isHaveData(false);
				isLoading = false;
			}
			
			@Override
			public void done(int what, CommonListResult<Course> obj) {
				MyCourseAc.intance.hideProgressDialog();
				
				if(obj.data.size()>0){
					list.addAll(obj.data);
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					setData();
					MyCourseAc.intance.isHaveData(true);
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
		if (type == 1) {// 删除
			UtilDialog.dialogTwoBtnContentTtile(context, null, "否","删除","是否删除该课程？",handler,2);
		} else if (type == 2) {// 去评价
			intent = new Intent(context, AddCommentAc.class);
			intent.putExtra("hotGoods", list.get(position));
//			intent.putExtra("degree", list.get(position).goodsType);
			MyCourseAc.intance.startActivity(intent);
		} else if (type == 3) {// 去付款

//			Intent intent = new Intent(context, PayInfoAc3.class);
//			Order orderInfo = new Order();
//			orderInfo.realName = list.get(position).babyName;
//			orderInfo.goodsType = list.get(position).goodsType;
//			orderInfo.goodsTime = list.get(position).goodsTime;
//			orderInfo.goodsName = list.get(position).goodsName;
//			intent.putExtra("order", orderInfo);
//			MyCourseAc.intance.startActivity(intent);


			intent = new Intent(context, PayInfoAc3.class);
			intent.putExtra("pageTag", "MyCourse");
			intent.putExtra("course", list.get(position));
			Order orderInfo = new Order();
			orderInfo.storeId =  list.get(position).storeId+"";
			orderInfo.storeName =  list.get(position).storeName+"";
			orderInfo.realName = list.get(position).babyName;
			orderInfo.goodsType = list.get(position).goodsType;
			orderInfo.goodsId = list.get(position).goodsId;
			orderInfo.goodsTime = list.get(position).goodsTime;
			orderInfo.goodsName = list.get(position).goodsName;

			orderInfo.orderId =  list.get(position).orderId+"";
			orderInfo.goodsMoney = list.get(position).goodsPrice+"";
			orderInfo.orderNumber = list.get(position).orderNumber;
			orderInfo.babyId = list.get(position).babyId;
			intent.putExtra("order", orderInfo);
			Child child = new Child();
			child.babyId = list.get(position).babyId;
			child.realName = list.get(position).babyName;
			intent.putExtra("child", child);
			MyCourseAc.intance.startActivity(intent);
		}
		else if (type == 4) //查看评价
		{
			intent = new Intent(context, EvaluateShowAc.class);
			intent.putExtra("hotGoods", list.get(position));
			MyCourseAc.intance.startActivity(intent);
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:// 删除事件
				if(pos>-1){
					delCourse();
				}
				break;
		};
	};
	};
	
	/**
	 * 删除我的课程信息
	 */
	public void delCourse(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderId", list.get(pos).orderNumber);
		MyCourseAc.intance.showProgressDialog();
		UserManager.getInstance().getTzjgoodsDeleteGoods(context, map, new ServiceCallback<StringResult>() {
			@Override
			public void error(String msg) {
				MyCourseAc.intance.hideProgressDialog();
				MyCourseAc.intance.dialogToast(msg);
			}
			@Override
			public void done(int what, StringResult obj) {
				MyCourseAc.intance.hideProgressDialog();
				MyCourseAc.intance.dialogToast("删除成功");
				list.remove(pos);
				adapter.notifyDataSetChanged();				
			}
		});
	}
	
	
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((MyCourseAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							list.clear();
							getCourseList();
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
					((MyCourseAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getCourseList();
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							

						}
					});
				}
			}, 2000);
		}
	}

}
