package com.bm.tzj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Accouter;
import com.bm.util.BaseDataUtil;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 户外装备
 * @author Administrator
 *
 */
public class AccouterListAc extends BaseActivity {

	
	
	private ListView lv_hot;
	
	private List<Accouter> hotList = new ArrayList<Accouter>();
	
	
	/** 分页器 */
	private Pager pager = new Pager(10);
	
	
	
	private String typeid;
	private String typename;
	
	private String pkids;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		typeid = this.getIntent().getStringExtra("typeid");
		typename = this.getIntent().getStringExtra("typename");
		pkids = this.getIntent().getStringExtra("pkids");
		
		contentView(R.layout.ac_accouter_list);
		setTitleName(typename);
		
//		if(pkids == null)
//			super.setIbRightImg(R.drawable.button_fenlei);
		
		
		
		
		lv_hot = this.findListViewById(R.id.lv_hot);
		
		lv_hot.setAdapter(hotAdapter);
		lv_hot.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 判断滚动到底部
					if (lv_hot.getLastVisiblePosition() == (lv_hot.getCount() - 1)) {
						loadHot(); //加载下一页
					}
					// 判断滚动到顶部

					if (lv_hot.getFirstVisiblePosition() == 0) {
					}

					break;
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}   });
		
		
		loadHot();
	}
	
	/**
	 * 加载分类商品
	 */
	private void loadHot()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", "-1");
		map.put("typeid", typeid);
		map.put("pkids", pkids);
		map.put("pageSize", "10");
		map.put("currentPage", pager.nextPageToStr());
		showProgressDialog();
		AsyncHttpHelp.httpGet(this,BaseApi.API_accouterList, map, new ServiceCallback<CommonListResult<Accouter>>(){
			@Override
			public void done(int what, CommonListResult<Accouter> obj) {
				hideProgressDialog();
				if(obj.data != null)
				{
					hotList.addAll(obj.data);
					pager.setCurrentPage(pager.nextPage(),hotList.size());
					hotAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
	}
	
	
	
	
	
		
	public void ibClickRight()
	{
		//弹出分类选择
		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_accouter_fenlei, null);
		final PopupWindow popupWindow = new PopupWindow(contentView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		
		GridView gv = (GridView)contentView.findViewById(R.id.grid_content);
		gv.setAdapter(new BaseAdapter(){
			@Override
			public int getCount() {
				if(BaseDataUtil.zhuangBeiFenLeiList == null)
					return 0;
				return BaseDataUtil.zhuangBeiFenLeiList.size();
			}
			@Override
			public Object getItem(int position) {
				return BaseDataUtil.zhuangBeiFenLeiList.get(position);
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				if(convertView == null)
				{
					convertView = LayoutInflater.from(context).inflate(R.layout.item_accouter_fenlei, null);
				}
				
				TextView tv_fenlei = (TextView)convertView.findViewById(R.id.tv_fenlei);
				tv_fenlei.setText(BaseDataUtil.zhuangBeiFenLeiList.get(position).showvalue);
				
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						typeid = BaseDataUtil.zhuangBeiFenLeiList.get(position).storevalue;
						typename = BaseDataUtil.zhuangBeiFenLeiList.get(position).showvalue;
						refresh();
					}
				});
				
				return convertView;
			}});
		
		
		popupWindow.showAsDropDown(ib_right);
	}

	private void refresh()
	{
		pager.setFirstPage();
		hotList.clear();
		loadHot();
		this.setTitleName(typename);
	}
	
	private BaseAdapter hotAdapter = new BaseAdapter(){
		@Override
		public int getCount() {
			return hotList.size();
		}

		@Override
		public Object getItem(int position) {
			return hotList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.item_list_accouter, null);
			}
			
			ImageView img_p = (ImageView)convertView.findViewById(R.id.img_p);
			TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			TextView tv_specialPrice = (TextView)convertView.findViewById(R.id.tv_specialPrice);
			TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
			
			final Accouter a = hotList.get(position);
			ImageLoader.getInstance().displayImage(a.acrossImage, img_p,App.getInstance().getAdvertisingImageOptions());
			tv_name.setText(a.name);
			if(a.specialPrice != null && Double.parseDouble(a.specialPrice) != 0.0)
			{
				if(a.specialPrice.indexOf(".00") != -1)
					tv_specialPrice.setText("￥"+a.specialPrice.substring(0, a.specialPrice.indexOf(".00")));
				else
					tv_specialPrice.setText(a.specialPrice);
				
				if(a.price != null)
				{
					if(a.price.indexOf(".00") != -1)
						tv_price.setText("￥"+a.price.substring(0, a.price.indexOf(".00")));
					else
						tv_price.setText(a.price);
					tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				}
			}
			else
			{
				if(a.price.indexOf(".00") != -1)
					tv_specialPrice.setText("￥"+a.price.substring(0, a.price.indexOf(".00")));
				else
					tv_specialPrice.setText(a.price);
				
				tv_price.setVisibility(View.GONE);
			}
			
			
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, AccouterDetailAc.class);
					intent.putExtra("pkid", a.pkid);
					context.startActivity(intent);
				}
			});
			
			return convertView;
		}};

}
