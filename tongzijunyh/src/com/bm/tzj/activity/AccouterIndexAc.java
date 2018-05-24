package com.bm.tzj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Accouter;
import com.bm.entity.Adverts;
import com.bm.util.BaseDataUtil;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.tool.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 户外装备
 * @author Administrator
 *
 */
public class AccouterIndexAc extends BaseActivity implements OnPageChangeListener {

	private ViewPager vp_gg; //头部广告图片
	
	private List<Adverts> adList;
	
	private LinearLayout ll_tejia;
	
	private ListView lv_hot;
	
	private List<Accouter> hotList = new ArrayList<Accouter>();
	
	
	private List<Accouter> tejiaList;
	
	/** 分页器 */
	private Pager pager = new Pager(10);
	
	private View ll_head;
	
	private LinearLayout ll_dot;
	
	private Handler handler = new Handler();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		contentView(R.layout.ac_accouter_index);
		setTitleName("户外装备");
		super.setIbRightImg(R.drawable.button_fenlei);
		
		vp_gg = (ViewPager)this.findViewById(R.id.vp_gg);
		ll_dot = this.findLinearLayoutById(R.id.ll_dot);
		
		ll_tejia = this.findLinearLayoutById(R.id.ll_tejia);
		
		vp_gg.setAdapter(ggAdapter);
		vp_gg.setOnPageChangeListener(this);
		
		lv_hot = this.findListViewById(R.id.lv_hot);
		ll_head = this.findViewById(R.id.ll_head);
		((ViewGroup)ll_head.getParent()).removeView(ll_head);
		ll_head.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		lv_hot.addHeaderView(ll_head);
		
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
		
		loadTejia();
		
		loadGg();
		
		loadHot();
	}
	
	@Override
	protected void onDestroy() {
		nextPage_end = true;
		super.onDestroy();
	}

	/**
	 * 加载热销商品
	 */
	private void loadHot()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", "0");
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
	
	
	/**
	 * 加载广告图片
	 */
	private void loadGg()
	{
		String strJson = SharedPreferencesHelper.getString("sysAdvertsList",null);
		if(strJson != null)
		{
			CommonListResult<Adverts> result = AsyncHttpHelp.getGson().fromJson(strJson, new ServiceCallback<CommonListResult<Adverts>>(){
				@Override
				public void done(int what, CommonListResult<Adverts> obj) {
				}
				@Override
				public void error(String msg) {
				}}.type);
			adList = new ArrayList<Adverts>();
			for(Adverts ad : result.data)
			{
				if(ad.type.equals("40"))
				{
					adList.add(ad);
				}
			}
			
			ggAdapter.notifyDataSetChanged();
			
			for(int i=0; i<adList.size(); i++)
			{
				ll_dot.getChildAt(i).setVisibility(View.VISIBLE);
			}
			ll_dot.getChildAt(0).setSelected(true);
			handler.postDelayed(nextPage, 5000);
		}
	}
	
	
	private boolean nextPage_end = false;
	/**
	 * 每隔5秒钟切换到下一页
	 */
	private Runnable nextPage = new Runnable() {
		
		@Override
		public void run() {
			if(nextPage_end)
				return;
			int curpos = vp_gg.getCurrentItem();
			curpos += 1;

			if (curpos == ggAdapter.getCount()) {
				curpos = 0;
			}
			vp_gg.setCurrentItem(curpos, true);
			handler.postDelayed(nextPage, 5000);
		}
	};
	
	//加载特价商品列表
	private void loadTejia()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", "1");
		map.put("pageSize", "50");
		map.put("currentPage", "1");
		showProgressDialog();
		AsyncHttpHelp.httpGet(this,BaseApi.API_accouterList, map, new ServiceCallback<CommonListResult<Accouter>>(){
			@Override
			public void done(int what, CommonListResult<Accouter> obj) {
				hideProgressDialog();
				tejiaList = obj.data;
				setTejiaData();
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
	}
	
	//设置特价商品内容
	private void setTejiaData()
	{
		if(tejiaList != null)
		{
			for(Accouter a : tejiaList)
			{
				View item = LayoutInflater.from(this).inflate(R.layout.item_tejia_accouter, ll_tejia, false);
				if(tejiaList.indexOf(a) == 0)
					item.setPadding(4, 0, 0, 0);
				else if(tejiaList.indexOf(a) == tejiaList.size()-1)
					item.setPadding(0, 0, 22, 0);
				ImageView img_p = (ImageView)item.findViewById(R.id.img_p);
				TextView tv_name = (TextView)item.findViewById(R.id.tv_name);
				TextView tv_specialPrice = (TextView)item.findViewById(R.id.tv_specialPrice);
				TextView tv_price = (TextView)item.findViewById(R.id.tv_price);
				ImageLoader.getInstance().displayImage(a.uprightImage, img_p,App.getInstance().getAdvertisingImageOptions());
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
				
				ll_tejia.addView(item);
				final Accouter A = a;
				item.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, AccouterDetailAc.class);
						intent.putExtra("pkid", A.pkid);
						context.startActivity(intent);
					}
				});
			}
		}
	}
	
	private PagerAdapter ggAdapter = new PagerAdapter(){
		@Override
		public int getCount() {
			if (adList == null)
				return 0;
			return adList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {  
            container.removeView((View)object);
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, final int position) {
        	ImageView iv = new ImageView(context);
        	ImageLoader.getInstance().displayImage(adList.get(position).imageUrl, iv,App.getInstance().getAdvertisingImageOptions());
            container.addView(iv, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            iv.setScaleType(ScaleType.CENTER_CROP);
            
            iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(context, AccouterDetailAc.class);
//					intent.putExtra("pkid", adList.get(position).pkid);
//					context.startActivity(intent);
					
					Intent intent = new Intent(context, AccouterListAc.class);
					intent.putExtra("typename", adList.get(position).title);
					intent.putExtra("pkids", adList.get(position).fkids);
					context.startActivity(intent);
				}
			});
            
			return iv;  
        }  
	};

		
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
						String typeid = BaseDataUtil.zhuangBeiFenLeiList.get(position).storevalue;
						String typename = BaseDataUtil.zhuangBeiFenLeiList.get(position).showvalue;
						Intent intent = new Intent(context, AccouterListAc.class);
						intent.putExtra("typeid", typeid);
						intent.putExtra("typename", typename);
						context.startActivity(intent);
					}
				});
				
				return convertView;
			}});
		
		
		popupWindow.showAsDropDown(ib_right);
		
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
				convertView = LayoutInflater.from(context).inflate(R.layout.item_list_accouter_hot, null);
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


	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int pos) {
		// 更新指示器
		for (int i = 0; i < ggAdapter.getCount(); i++) {
			ll_dot.getChildAt(i).setSelected(i == pos);
		}
	}

}
