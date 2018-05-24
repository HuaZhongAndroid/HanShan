package com.bm.tzj.kc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.base.BaseActivity;
import com.bm.base.adapter.CheckStoreAdapter;
import com.bm.entity.StoreInfo;
import com.richer.tzj.R;

/**
 * 选择门店
 * @author shiyt
 *
 */
public class CheckStoreAc extends BaseActivity {
	private ListView lv_storeList;
	private ImageView img_noData;
	private Context mContext;
	private CheckStoreAdapter adapter;
	private List<StoreInfo> storeList= new ArrayList<StoreInfo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_checkstore);
		mContext = this;
		setTitleName("选择门店");
		init();
	}
	
	public void init(){
		storeList = (List<StoreInfo>) getIntent().getSerializableExtra("storeList");
		img_noData = findImageViewById(R.id.img_noData);
		img_noData.setVisibility(View.GONE);
		lv_storeList = findListViewById(R.id.lv_storeList);
		adapter = new CheckStoreAdapter(mContext, storeList);
		lv_storeList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
//		lv_storeList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//					Intent  intent =new Intent(mContext, CourseDetailAc.class);
//					intent.putExtra("pageTag", "2");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//					intent.putExtra("tag", arg2+"");
//					setResult(004, intent);
//					finish();
//			}
//		});
	}
	

}
