package com.bm.tzj.czzx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.HonoRollAdapter;
import com.bm.entity.User;
import com.bm.share.ShareModel;
import com.bm.tzj.kc.GrowthCenterAc;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 荣誉榜
 * 
 * @author shiyt
 * 
 */
public class HonoRollAc extends BaseActivity implements OnClickListener,OnRefreshListener{
	private Context mContext;
	private List<User> list = new ArrayList<User>();
	private HonoRollAdapter adapter;
	private ListView lv_honoroll;
	private TextView tv_xyx,tv_num;
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);
	
	String storeCount="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_honoroll);
		mContext = this;
		
		setTitleName("荣誉榜");
		init();
	}

	public void init() {
		mSwipyRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		
		tv_xyx=findTextViewById(R.id.tv_xyx);
		tv_num=findTextViewById(R.id.tv_num);
		lv_honoroll=findListViewById(R.id.lv_honoroll);
		adapter=new HonoRollAdapter(mContext, list);
		lv_honoroll.setAdapter(adapter);
		getData();
		
		
		
		tv_xyx.setOnClickListener(this);
		
		lv_honoroll.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(null != App.getInstance().getUser()){
					if(!list.get(arg2).userId.equals(App.getInstance().getUser().userid)){
						Intent intent = new Intent(mContext, GrowthCenterAc.class);
						intent.putExtra("pageType", "HonoRollAc");
						intent.putExtra("friendId",list.get(arg2).userId);
						intent.putExtra("friendBabyId",list.get(arg2).babyId);
						startActivity(intent);
					}
				}
			}
		});
	}
	
	/**
	 * 获取数据
	 */
	public void getData(){
		HashMap<String, String>map = new HashMap<String, String>();
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//教练ID
		}else{
			map.put("userId", "0");//教练ID
		}
		map.put("pageNum", pager.nextPageToStr());
		map.put("pageCount", "10");
		if(null != App.getInstance().getChild())
			map.put("babyId", App.getInstance().getChild().babyId);
		showProgressDialog();
		UserManager.getInstance().getTzjcasuserbabyBabylist(mContext, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if(null != obj.data ){
					
					if(obj.data.friendsBay.size()>0){
						list.addAll(obj.data.friendsBay);
						pager.setCurrentPage(pager.nextPage(),list.size()); 
					}
					storeCount = obj.data.sort;
					tv_num.setText(Html.fromHtml("<font color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\">第</font><font size=\"25\"  color=\""+mContext.getResources().getColor(R.color.txt_yellow_color)+"\">"+obj.data.sort+"</font><font  color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\">名</font>"));
					adapter.notifyDataSetChanged();
				}
			}
		});
		
//		for (int i = 0; i < 8; i++) {
//			list.add(new Model());
//		}
//		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((HonoRollAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							 getData();
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
					((HonoRollAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 getData();
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_xyx:
			ShareModel model = new ShareModel();
//			model.title = "君昂喊山";
			model.title="我在喊山荣誉榜比拼中获得了第"+Html.fromHtml("<font color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\"></font><font size=\"25\"  color=\""+mContext.getResources().getColor(R.color.txt_yellow_color)+"\">"+storeCount+"</font><font  color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\"></font>")+"名，不服气来比试比试~";
			model.conent = "我在喊山荣誉榜比拼中获得了第"+Html.fromHtml("<font color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\"></font><font size=\"25\"  color=\""+mContext.getResources().getColor(R.color.txt_yellow_color)+"\">"+storeCount+"</font><font  color=\""+mContext.getResources().getColor(R.color.txt_title_detail)+"\"></font>")+"名，不服气来比试比试~";
			model.targetUrl = BaseApi.SHARE_URL + "&userid="+App.getInstance().getUser().userid + "&appVersion="+Util.getAppVersion(context);
			share.shareInfo(model,0);
			break;

		default:
			break;
		}
		
	}
}
