package com.bm.tzj.caledar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CaledarTimeListAdapter;
import com.bm.entity.Child;
import com.bm.entity.GoodsNumTime;
import com.bm.tzj.caledar.CalendarView.OnCalendarViewListener;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.widget.FuGridView;
import com.richer.tzj.R;

public class CaledarAc extends BaseActivity {

	private Context context ;
	private CaledarTimeListAdapter adapter ;
	private List<GoodsNumTime> list = new ArrayList<GoodsNumTime>();
	
	private FuGridView gv_explore_time;

	private String storeId;//门店id
	private String goodsId;//课程id
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_acledar);
		setTitleName("选择时间");
		context = this;
		storeId=getIntent().getStringExtra("storeId");
		goodsId=getIntent().getStringExtra("goodsId");
		init();

	}

	private void init() {
		// 在代码中
		CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
		// 设置标注日期
		List<Date> markDates = new ArrayList<Date>();
		markDates.add(new Date());
		calendarView.setMarkDates(markDates);

		// 设置点击操作
		calendarView.setOnCalendarViewListener(new OnCalendarViewListener() {

			@Override
			public void onCalendarItemClick(CalendarView view, Date date) {
				// TODO Auto-generated method stub
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//				Toast.makeText(CaledarAc.this, format.format(date),Toast.LENGTH_SHORT).show();
//				if(format.format(date).equals("2016年01月15日")){
//					list.clear();
//					setData();
//				}else{
//					list.clear();
//					adapter.notifyDataSetChanged();
//				}
				getGoodNumofTimeList(format.format(date));
			}
		});
		
		gv_explore_time = (FuGridView) findViewById(R.id.gv_explore_time);
		
		adapter = new CaledarTimeListAdapter(context,list);
		gv_explore_time.setAdapter(adapter);
		
		
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		getGoodNumofTimeList(format.format(new Date()));
		
//		gv_explore_time.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				//是否购买  0未买 1已买
//				if(list.get(arg2).isBought.equals("0")){
//					if(Integer.valueOf(list.get(arg2).personCount)>0){
//						Intent  intent =new Intent(context, CourseDetailAc.class);
//						intent.putExtra("goodsId", list.get(arg2).goodsId);
//						intent.putExtra("pageType", "CaledarAc");
//						intent.putExtra("startTime", list.get(arg2).startTime);
//						intent.putExtra("endTime", list.get(arg2).endTime);
//						intent.putExtra("pageTag", "2");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//						setResult(002, intent);
//						finish();
//					}
//				}
//
//			}
//		});
	}
	/**
	 * 获取场次列表
	 */
	public void getGoodNumofTimeList(String date){
		list.clear();
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("storeId", storeId);//门店名称
		map.put("goodsId", goodsId);//课程名称
		if(!TextUtils.isEmpty(date)){
			map.put("date", date);//时间
		}
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//用户ID
		}
		Child child = App.getInstance().getChild();
		if(null != child)
			map.put("babyId", child.babyId);
		UserManager.getInstance().getTzjgoodsGoodnumoftime(context, map, new ServiceCallback<CommonListResult<GoodsNumTime>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<GoodsNumTime> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					list.addAll(obj.data);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
//	private void setData() {
//		for (int i = 0; i < 6; i++) {
//			Model info = new Model();
//			info.name = "剩余: " + (i + 3) ;
//			info.time = "1"+ (i + 3) + ":00 — 1"+(i + 4)+":00";
//			list.add(info);
//		}
//		adapter.notifyDataSetChanged();
//	}

}
