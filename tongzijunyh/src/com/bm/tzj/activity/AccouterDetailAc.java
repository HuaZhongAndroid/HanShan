package com.bm.tzj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Accouter;
import com.bm.share.ShareModel;
import com.bm.util.Util;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

public class AccouterDetailAc extends BaseActivity implements OnClickListener {

	private WebView webContent;
	
	private String pkid;
	private Accouter accouter;
	
	private ImageView img_bg;
	private TextView tv_name,tv_price,tv_specialPrice;
	
	private ListView lv_hot;
	
	private List<Accouter> hotList = new ArrayList<Accouter>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_accouterdetail);
		this.setTitleName("商品详情");
		
		pkid = this.getIntent().getStringExtra("pkid");
		
		webContent = (WebView)this.findViewById(R.id.webContent);
		img_bg = this.findImageViewById(R.id.img_bg);
		
		tv_name = this.findTextViewById(R.id.tv_name);
		tv_price = this.findTextViewById(R.id.tv_price);
		tv_specialPrice = this.findTextViewById(R.id.tv_specialPrice);
		
		lv_hot = this.findListViewById(R.id.lv_hot);
		lv_hot.setAdapter(hotAdapter);
		
		this.findViewById(R.id.bt_buy).setOnClickListener(this);
		this.findViewById(R.id.img_share).setOnClickListener(this);
		
		loadDetail();
		
		loadHot();
	}
	
	/**
	 * 加载热销商品
	 */
	private void loadHot()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", "0");
		map.put("pageSize", "3");
		map.put("currentPage", "1");
		showProgressDialog();
		AsyncHttpHelp.httpGet(this,BaseApi.API_accouterList, map, new ServiceCallback<CommonListResult<Accouter>>(){
			@Override
			public void done(int what, CommonListResult<Accouter> obj) {
				hideProgressDialog();
				if(obj.data != null)
				{
					hotList.addAll(obj.data);
					hotAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
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
	
	
	private void loadDetail()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pkid", pkid);
		showProgressDialog();
		AsyncHttpHelp.httpGet(this,BaseApi.API_accouterInfo, map, new ServiceCallback<CommonResult<Accouter>>(){
			@Override
			public void done(int what, CommonResult<Accouter> obj) {
				hideProgressDialog();
				accouter = obj.data;
				if(accouter != null)
				{
					tv_name.setText(accouter.name);
					tv_price.setText(accouter.price);
					tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
					tv_specialPrice.setText(accouter.specialPrice);
					if(accouter.specialPrice != null && Double.parseDouble(accouter.specialPrice) != 0.0)
					{
						tv_specialPrice.setText(accouter.specialPrice);
						
						if(accouter.price != null)
						{
							tv_price.setText(accouter.price);
							tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
						}
					}
					else
					{
						tv_specialPrice.setText(accouter.price);
						tv_price.setVisibility(View.GONE);
					}
					
					ImageLoader.getInstance().displayImage(accouter.acrossImage, img_bg,App.getInstance().getAdvertisingImageOptions());
				
//					webContent.loadDataWithBaseURL("file://", accouter.detail,"text/html", "UTF-8", "about:blank");
					Util.initViewWebData(webContent, accouter.detail+"");
				}
			}
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_buy:
			gotoTaobao();
			break;
		case R.id.img_share: // 分享
			ShareModel model = new ShareModel();
			model.title = "我购买了"+accouter.name+",你也快来看看吧~";
			model.conent = "我购买了"+accouter.name+",你也快来看看吧~";
			model.targetUrl = BaseApi.SHARE_URL + "&userid="+App.getInstance().getUser().userid + "&appVersion="+Util.getAppVersion(context);
			share.shareInfo(model,0);
			break;
		}
	}
	
	
	private void gotoTaobao()
	{
		ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);    
		clipboardManager.setPrimaryClip(ClipData.newPlainText(null, accouter.taoBaoCode));  // 将内容set到剪贴板  
		Intent intent;
	 	PackageManager packageManager = this.getPackageManager(); 
	 	intent = packageManager.getLaunchIntentForPackage("com.taobao.taobao"); 
	 	if(intent == null)
	 	{
	 		dialogToast("请先下载淘宝客户端");
	 		return;
	 	}
	 	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
	 	this.startActivity(intent);
	}

}
