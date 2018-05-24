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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CoachAdapter;
import com.bm.entity.Coach;
import com.bm.entity.User;
import com.bm.tzj.city.City;
import com.bm.tzj.kc.CoachInformationAc;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;

/**
 * 我的私人教练
 * 
 * @author jiangsh
 * 
 */
public class MyTeachersAc extends BaseActivity implements OnClickListener{
	private Context context;
	private EditText et_coachName;
	private CoachAdapter coachAdpter;
//	private SwipyRefreshLayout mSwipyRefreshLayout;
//	/** 分页器 */
//	public Pager pager = new Pager(10);
	private TextView tv_submit;
	private FrameLayout fm_content;
	private List<Coach> list = new ArrayList<Coach>();
	public static final int MYTEACHER_CLICK_STATES=1001;
	public static MyTeachersAc intanece = null;
	Intent intent;
	String strPageType="";
	private User user;
	private ListView listView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_my_teacher);
		context = this;
		setTitleName("我的顾问教练");
		intanece = this;
		user = App.getInstance().getUser();
		initView();
	}

	private void initView() {
		strPageType = getIntent().getStringExtra("pageType");
		tv_submit = findTextViewById(R.id.tv_submit);
		listView = (ListView)findViewById(R.id.listView);
		tv_submit.setOnClickListener(this);
//		mSwipyRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
//		mSwipyRefreshLayout.setOnRefreshListener(this);
//		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
//		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		fm_content = (FrameLayout)findViewById(R.id.fm_content);
		et_coachName = (EditText)findEditTextById(R.id.et_coachName);
		coachAdpter =new CoachAdapter(context, list);
		listView.setAdapter(coachAdpter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intent = new Intent(context, CoachInformationAc.class);
				intent.putExtra("title", "顾问教练");
				intent.putExtra("pageType", strPageType);
				intent.putExtra("coachId", list.get(position).getCoachId());//教练ID
				startActivity(intent);
			}
		});
	}

	private void setData() {
		list.clear();
		HashMap<String, String> map = new HashMap<String, String>();
		City city = App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安");//城市名称
		}
		map.put("coachName", et_coachName.getText().toString().trim()+"");//教练名称
		map.put("userId", user.userid);
//		map.put("pageNum", "1");
//		map.put("pageCount", "100");
		showProgressDialog();
		UserManager.getInstance().getTzjcoachCoachlist(context, map, new ServiceCallback<CommonListResult<Coach>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<Coach> obj) {
				hideProgressDialog();
				if(fm_content.getChildCount()>0){
					fm_content.removeAllViews();
				}
				if(obj.data.size()>0){
					list.addAll(obj.data);
//					SixPracticePanel.setViews(fm_content, list,context,handler);
					coachAdpter.notifyDataSetChanged();
				}
			}
		});
		

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:// 
			setData();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		setData();
		super.onResume();
	}
	
//	@Override
//	public void onRefresh(SwipyRefreshLayoutDirection direction) {
//		if (direction == SwipyRefreshLayoutDirection.TOP) {
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// Hide the refresh after 2sec
//					((MyTeachersAc) context).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							 pager.setFirstPage();
//							 list.clear();
//
//							// 刷新设置
//							mSwipyRefreshLayout.setRefreshing(false);
//
//							setData();
//							// map.put("pageNum", pager.nextPage() + "");// 页码
//							// pager.setCurrentPage(pager.nextPage(),
//							// list.size());
//						}
//					});
//				}
//			}, 2000);
//
//		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//			new Handler().postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					// Hide the refresh after 2sec
//					((MyTeachersAc) context).runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							 setData();
//
//							// 刷新设置
//							mSwipyRefreshLayout.setRefreshing(false);
//
//						}
//					});
//				}
//			}, 2000);
//		}
//	}

}
