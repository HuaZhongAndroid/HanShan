package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyExploreAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.PlayMateList;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 我的探索
 * 
 * @author wanghy
 * 
 */
public class MyExploreAc extends BaseActivity implements OnRefreshListener {
	
	private Context context;	
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);
	private ListView lv_listExplore;
	private ImageView img_noData;
	private MyExploreAdapter adapter;
	private List<PlayMateList> list = new ArrayList<PlayMateList>();
	public static final int MYECPLORE_CLICK_STATES=1003;
	int strPosition=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_myexplore);
		context = this;
		setTitleName("我的探索");
		initView();
	}
	
	private void initView(){
		img_noData = (ImageView) findViewById(R.id.img_noData);
		img_noData.setVisibility(View.GONE);
		mSwipyRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		
		lv_listExplore = (ListView) findViewById(R.id.lv_listExplore);

		adapter = new MyExploreAdapter(context, list,handler);
		lv_listExplore.setAdapter(adapter);
		
		getTSList();
	}
	
	
	
	/**
	 * 获取探索列表
	 */
	public void getTSList(){
		MainAc.intance.showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		City city  =App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("userid", App.getInstance().getUser().userid);
		map.put("pageCount", "10");//每页展示条数
		UserManager.getInstance().getMyProbeList(context, map, new ServiceCallback<CommonListResult<PlayMateList>>() {
			
			@Override
			public void error(String msg) {
				MainAc.intance.dialogToast(msg);
				MainAc.intance.hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<PlayMateList> obj) {
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
			lv_listExplore.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MYECPLORE_CLICK_STATES:
				strPosition = msg.arg1;
				UtilDialog.dialogTwoBtnContentTtile(context, "确定删除我的探索数据", "取消","确定","提示",handler,4);
				break;
			case 4://删除我的探索数据
				if(strPosition>-1){
					getDelArticle();
				}
				break;
			}
		};
	};
	
	/**
	 * 删除帖子
	 */
	public void getDelArticle(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", list.get(strPosition).articleId+"");
		showProgressDialog();
		UserManager.getInstance().getArticleDelArticle(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				list.remove(strPosition);
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
					((MyExploreAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							// mSwipyRefreshLayout.setRefreshing(false);
								getTSList(); 
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
					((MyExploreAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
								getTSList(); 
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}
}
