package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyAccountBalanceAdapter;
import com.bm.entity.Order;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.fm.MineFm;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.Pager;
import com.lib.tool.SharedPreferencesHelper;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

/**
 * 账户余额
 * 
 * @author wanghy
 * 
 */
public class AccountBalanceAc extends BaseActivity implements OnClickListener,OnRefreshListener {

	private Context context;
	/** 分页器 */
	public Pager pager = new Pager(10);
	private ListView lv_listAccount;
	private SwipyRefreshLayout mSwipyRefreshLayout;

	private MyAccountBalanceAdapter adapter;
	private List<Order> list = new ArrayList<Order>();
	private TextView tv_money,tv_recharge;
	private TextView tv_czMoney,tv_zsMoney;
	private ImageView img_noData;
	public static AccountBalanceAc intance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_accountbalance_new);
		context = this;
		intance = this;
		setTitleName("账单明细");
		initView();
	}

	private void initView() {
		img_noData = findImageViewById(R.id.img_noData);
		img_noData.setVisibility(View.VISIBLE);
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		
		
		lv_listAccount = (ListView) findViewById(R.id.lv_listAccount);

		adapter = new MyAccountBalanceAdapter(context, list,"1");
		lv_listAccount.setAdapter(adapter);

		tv_recharge = findTextViewById(R.id.tv_recharge);
		tv_money = (TextView) findViewById(R.id.tv_money);
		TextPaint tp = tv_money.getPaint();
		tp.setFakeBoldText(true);
		tv_recharge.setOnClickListener(this);
		
		tv_czMoney = (TextView) findViewById(R.id.tv_czMoney);
		tv_zsMoney = (TextView) findViewById(R.id.tv_zsMoney);
	}
	@Override
	protected void onResume() {
		super.onResume();
		getRefreshData();
	}
	
	public void getRefreshData(){
		getUser();
//		if(null != App.getInstance().getUser()){
//			System.out.println("wanghaiyan5");
//			tv_money.setText(App.getInstance().getUser().account == null?"0.00":Util.toNumber("0.00",Float.parseFloat(App.getInstance().getUser().account)));//余额
//		}
		getBbalance();
	}
	
	
	/**
	 * 查询余额
	 */
	public void getBbalance(){
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
			
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		showProgressDialog();
		UserManager.getInstance().getTzjorderUserOrderlist(context, map, new ServiceCallback<CommonListResult<Order>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<Order> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					list.addAll(obj.data);
					pager.setCurrentPage(pager.nextPage(),list.size()); 
				}
				if(null != App.getInstance().getUser()){
					tv_money.setText(App.getInstance().getUser().accountMoney == null?"0.00":App.getInstance().getUser().accountMoney);//余额
					tv_czMoney.setText(App.getInstance().getUser().rechargeBalance == null?"0.00":App.getInstance().getUser().rechargeBalance);//余额
					tv_zsMoney.setText(App.getInstance().getUser().giveBalance == null?"0.00":App.getInstance().getUser().giveBalance);//余额
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
			lv_listAccount.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_recharge:
			openActivity(RechargeAc2.class);
			break;
		default:
			break;
		}
	}
	

	/*
	 * 获取用户信息
	 */
	public void getUser() {
		HashMap<String, String> map = new HashMap<String, String>();
		User user = App.getInstance().getUser();
		User userPost = new User();
		if (null != user.userid) {
			map.put("userId", user.userid);
		}else {
			map.put("userId", SharedPreferencesHelper.getString("userid"));
		}
		
		UserManager.getInstance().getTzjcasGetUserInfo(context, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				MainAc.intance.toast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				if (obj != null && obj.data != null) {
					App.getInstance().setUser(null);
					App.getInstance().setUser(obj.data);// 存储用户信息
					tv_money.setText(obj.data.accountMoney == null?"0.00":obj.data.accountMoney);//余额
					
					//刷新个人中心的数据
					if(MineFm.instance!=null){
						MineFm.instance.hideOrView();
					}
					
				} else {
					dialogToast("获取数据失败");
					MainAc.intance.toast("获取数据失败");
				}
				
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
					((AccountBalanceAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							list.clear();
							getRefreshData();
							getBbalance();
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
					((AccountBalanceAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getBbalance();
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							

						}
					});
				}
			}, 2000);
		}
	}
}
