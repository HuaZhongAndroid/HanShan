package com.bm.tzj.mine;

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
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyMessageAdapter;
import com.bm.entity.MessageList;
import com.bm.tzj.city.City;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.RefreshViewPD;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 我的消息
 * 
 * @author jiangsh
 * 
 */
public class MyMessageAc extends BaseActivity implements OnClickListener,OnRefreshListener {
	private Context context;
	private RefreshViewPD refresh_view;
	/** 分页器 */
	public Pager pager = new Pager(10);
	private ListView lv_listMessage;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	private ImageView img_noData;

	private MyMessageAdapter adapter;
	private List<MessageList> list = new ArrayList<MessageList>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mymessage);
		context = this;
		setTitleName("我的消息");
		initView();
	}
	

	private void initView() {
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		
		
		lv_listMessage = (ListView) findViewById(R.id.lv_listMessage);
		img_noData=(ImageView) findViewById(R.id.img_noData);

		adapter = new MyMessageAdapter(context, list);
		lv_listMessage.setAdapter(adapter);
		lv_listMessage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(context, DetialMyMessageAc.class);
				intent.putExtra("messageId", list.get(arg2).messageId);
				startActivity(intent);
			}	
		});
		
//		getMessageList();
	}
	/**
	 * 获取我的消息列表
	 * 
	 */
	public void getMessageList(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		City city = App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//
		}else{
			map.put("userId", "0");
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		UserManager.getInstance().getTzjmessageMessagelist(context, map, new ServiceCallback<CommonListResult<MessageList>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<MessageList> obj) {
				hideProgressDialog();
				if(null!=obj.data){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				setData();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		pager.setFirstPage();
		list.clear();
		getMessageList();
	}
	
	private void setData() {
		if (list.size() == 0) {
			img_noData.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			lv_listMessage.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}
	
//	private void setData() {
//		for (int i = 0; i < 5; i++) {
//			Model info = new Model();
//			info.name = "[第" + (i + 3) + "门晋级课程] 滚筒+鸟巢+西藏之课程";
//			info.time = "2015.12." + "1" + (i + 3) + " 10:30";
//			list.add(info);
//		}
//		adapter.notifyDataSetChanged();
//	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_clear:
//			dialogToast("清除失效课程");
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
					((MyMessageAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							list.clear();
							getMessageList();
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
					((MyMessageAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getMessageList();
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							

						}
					});
				}
			}, 2000);
		}
	}

}
