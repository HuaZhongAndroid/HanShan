package com.bm.tzj.czzx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CourseAdapter;
import com.bm.entity.Badge;
import com.bm.entity.HotGoods;
import com.bm.tzj.city.City;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 勋章详情
 * @author shiyt
 *
 */
public class MedalDetailAc extends BaseActivity {
	private Context mContext;
	private ListView lv_course;
	private List<HotGoods> list = new ArrayList<HotGoods>();
	CourseAdapter adapter;
	private ImageView iv_sixview_heads,iv_c_bg,img_noData;
	private TextView tv_name;
	private View view_line;
	Badge badgeInfo;//勋章详情
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_medaldetail);
		mContext = this;
		setTitleName("勋章详情");
		init();
	}
	
	public void init(){
		view_line = findViewById(R.id.view_line);
		badgeInfo = (Badge) getIntent().getSerializableExtra("medalInfo");
		img_noData = findImageViewById(R.id.img_noData);
		iv_c_bg = findImageViewById(R.id.iv_c_bg);
		tv_name=findTextViewById(R.id.tv_name);
		iv_sixview_heads=(ImageView) findViewById(R.id.iv_sixview_heads);
		lv_course=findListViewById(R.id.lv_course);
		adapter = new CourseAdapter(mContext, list);
		lv_course.setAdapter(adapter);
		
//		lv_course.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				Intent intent =new Intent(mContext, CourseDetailAc.class);
//				intent.putExtra("degree", list.get(position).goodsType);
//				intent.putExtra("goodsId", list.get(position).goodsId);
//				intent.putExtra("pageTag", "2");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//				intent.putExtra("pageType", "MedalDetailAc");
//				startActivity(intent);
//			}
//		});
		getData();
	}
	
	public void getData(){
		if(badgeInfo.titleMultiUrl.length()>0){
			ImageLoader.getInstance().displayImage(badgeInfo.titleMultiUrl, iv_sixview_heads, App.getInstance().getListViewDisplayImageOptions());
		}
		
//		if(badgeInfo.isLight.equals("0")){//isLight  0 未点亮  1 点亮
//			iv_c_bg.setBackgroundResource(R.drawable.sixmsg);
//		 }else  if(badgeInfo.isLight.equals("1")){
//			 iv_c_bg.setBackgroundResource(R.drawable.sixmsg1);
//		 }else{
//			 iv_c_bg.setBackgroundResource(R.drawable.sixmsg);
//		 }
		
		
		tv_name.setText(badgeInfo.medalName);//勋章名称
		
		HashMap<String,String> map= new HashMap<String, String>();
		map.put("medalId", badgeInfo.medalId);//勋章ID
		
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//教练ID
		}else{
			map.put("userId", "0");//教练ID
		}
		City city = App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
		}
		
		UserManager.getInstance().getTzjgoodsGoodslistbymedal(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<HotGoods> obj) {
				if(obj.data.size()>0){
					if(obj.data.size()>3){
						for (int i = 0; i < 3; i++) {
							HotGoods hotGood = obj.data.get(i);
							list.add(hotGood);
						}
					}else{
						list.addAll(obj.data);
					}
					setData();
				}
			}
		});
	}
	
	private void setData() {
		if (list.size() == 0) {
			img_noData.setVisibility(View.VISIBLE);
			lv_course.setVisibility(View.GONE);
			view_line.setVisibility(View.VISIBLE);
		} else {
			img_noData.setVisibility(View.GONE);
			lv_course.setVisibility(View.VISIBLE);
			view_line.setVisibility(View.GONE);
		}
		adapter.notifyDataSetChanged();
	}
}
