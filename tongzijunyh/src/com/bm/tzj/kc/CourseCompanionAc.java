package com.bm.tzj.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CourseCompanionAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Baby;
import com.bm.entity.Babys;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.LoginAc;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 课程玩伴儿
 * 
 * @author Administrator
 * 
 */
public class CourseCompanionAc extends BaseActivity implements
		CourseCompanionAdapter.OnSeckillClick, OnRefreshListener {
	private Context mContext;
	private ListView lv_course;
	private TextView tv_baby_count;
	private List<Baby> list = new ArrayList<Baby>();
	private CourseCompanionAdapter adapter;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_coursecompanion);
		mContext = this;
		setTitleName("课程玩伴儿");
		init();
	}

	public void init() {
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		tv_baby_count=findTextViewById(R.id.tv_baby_count);
		lv_course = findListViewById(R.id.lv_course);
		
		tv_baby_count.setText("已报名宝贝("+getIntent().getStringExtra("babyCount")+")");
		
		adapter = new CourseCompanionAdapter(mContext, list);
		lv_course.setAdapter(adapter);
		adapter.setOnSeckillClick(this);
		getBabyList();
	}

	/**
	 * 获取课程玩伴儿
	 */
	public void getBabyList() {
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goodsId", getIntent().getStringExtra("goodsId"));// 商品id
		map.put("pageNum", pager.nextPageToStr());// 查询页数
		map.put("pageCount", "10");// 每页展示条数
		UserManager.getInstance().getTzjcasuserbabyRegisteredbabylist(mContext,
				map, new ServiceCallback<CommonResult<Babys>>() {

					@Override
					public void error(String msg) {
						dialogToast(msg);
						hideProgressDialog();
					}

					@Override
					public void done(int what, CommonResult<Babys> obj) {
						hideProgressDialog();
						if (null != obj.data&&null!=obj.data.registeredBabyResult2List) {
							pager.setCurrentPage(pager.nextPage(), list.size());
							list.addAll(obj.data.registeredBabyResult2List);
							adapter.notifyDataSetChanged();
						}
					}
				});
	}


	@Override
	public void onSeckillClick(int position) {
		if(null == App.getInstance().getUser()){
			UtilDialog.dialogTwoBtnContentTtile(mContext, "您还未登录，请先登录在操作", "取消","确定","提示",handler,1);
			return;
		}
		if(null != App.getInstance().getUser()){
			if(!list.get(position).userId.equals(App.getInstance().getUser().userid)){
				Intent intent = new Intent(mContext, GrowthCenterAc.class);
				intent.putExtra("pageType", "HonoRollAc");
				intent.putExtra("friendId", list.get(position).userId);//宝贝ID
				intent.putExtra("friendBabyId", list.get(position).babyId);//宝贝ID
				startActivity(intent);
			}
		}
		
		
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 1:
				finish();
				MainAc.intance.finish();
				Intent intent = new Intent(mContext,LoginAc.class);
				startActivity(intent);
				break;
			}
		};
	};

	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((CourseCompanionAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							list.clear();
							getBabyList();
							// mSwipyRefreshLayout.setRefreshing(false);

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
					((CourseCompanionAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getBabyList();
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}
}
