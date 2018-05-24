package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyCollectionAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.HotGoods;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.lib.tool.Pager;
import com.lib.widget.RefreshViewPD;
import com.richer.tzj.R;

/**
 * 我的收藏
 * 
 * @author wanghy
 * 
 */
public class MyCollectionAc extends BaseActivity implements OnClickListener {
	private Context context;
	private RefreshViewPD refresh_view;
	/** 分页器 */
	public Pager pager = new Pager(10);
	private ListView lv_listCollection;

	private MyCollectionAdapter adapter;
	private List<HotGoods> list = new ArrayList<HotGoods>();
	private LinearLayout ll_date;
	private ImageView img_noData;
	private TextView tv_clear;
	public static final int MYCOLLECTION_CLICK_STATES=1002;
	int strPosition=-1;
	int invalidCount = 0;//失效的课程数量
	String invalidBaseIds = "";//失效的课程ID
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mycollection);
		context = this;
		setTitleName("我的收藏");
		setRightName("清空");
		initView();
	}
	
	@Override
	public void clickRight() {
		// TODO Auto-generated method stub
		super.clickRight();
		if(list.size()>0){
			UtilDialog.dialogTwoBtnContentTtile(context, "确定清空我的收藏数据", "取消","确定","提示",handler,2);
		}else{
			dialogToast("暂时没有数据可以清空");
		}
		
	}

	private void initView() {
		ll_date = findLinearLayoutById(R.id.ll_date);
		img_noData = findImageViewById(R.id.img_noData);
		refresh_view = (RefreshViewPD) findViewById(R.id.refresh_view);
		lv_listCollection = (ListView) findViewById(R.id.lv_listCollection);

		adapter = new MyCollectionAdapter(context, list,handler);
		lv_listCollection.setAdapter(adapter);

		tv_clear = findTextViewById(R.id.tv_clear);
		tv_clear.setOnClickListener(this);
		getMyCollecton();
	}

	/**
	 * 获取收藏数据
	 */
	private void getMyCollecton() {
		list.clear();
		if(null == App.getInstance().getUser()){
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
		}
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		showProgressDialog();
		UserManager.getInstance().getMyselfMyCollection(context, map, new  ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
					for(int i=0;i<obj.data.size();i++){
						if(obj.data.get(i).courseStatus.equals("0")){
							invalidCount++;
							invalidBaseIds +=obj.data.get(i).goodsId+",";
						}
					}
				}
				setData();
			}
		});
		
	}
	
	private void setData() {
		if (list.size() == 0) {
			ll_date.setVisibility(View.GONE);
			img_noData.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			ll_date.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_clear:
			if(invalidCount>0){
				UtilDialog.dialogTwoBtnContentTtile(context, "确定清除失效的课程数据", "取消","确定","提示",handler,3);
			}else{
				dialogToast("暂时没有失效的数据可以清除");
			}
			
			break;
		default:
			break;
		}
	}
	
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:// 确定事件
				delCollection("0");
				break;
			case 3:
				delCollection("2");
				break;
			case 4:
				delCollection("1");
				adapter.notifyDataSetChanged();
				break;
			case MYCOLLECTION_CLICK_STATES:
				strPosition = msg.arg1;
				UtilDialog.dialogTwoBtnContentTtile(context, "确定删除我收藏的数据", "取消","确定","提示",handler,4);
				break;
			}
		};
	};
	
	/**
	 * 删除收藏
	 */
	public void delCollection(final String strType){
		HashMap<String, String> map = new HashMap<String, String>();
		if(strType.equals("1")){
			map.put("baseId", list.get(strPosition).goodsId+"");//课程ID
		}else if(strType.equals("2")){
			map.put("baseIds", invalidBaseIds);//失效课程ID
		}
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
		}
		map.put("delType", strType);//删除类型   0 全部清空  1 按课程id删除   2 删除失效课程
		
		showProgressDialog();
		UserManager.getInstance().getMyselfDelCollection(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				if(strType.equals("0")){
					list.clear();
					adapter.notifyDataSetChanged();
				}else if(strType.equals("1")){
					list.remove(strPosition);
					adapter.notifyDataSetChanged();
				}else{
					getMyCollecton();
				}
			}
		});
	}

}
