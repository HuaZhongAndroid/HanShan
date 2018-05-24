package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CourseAdapter;
import com.bm.base.adapter.RegistrationAdapter;
import com.bm.entity.Baby;
import com.bm.entity.Dictionary;
import com.bm.entity.HotGoods;
import com.bm.entity.Model;
import com.bm.entity.StoreInfo;
import com.bm.tzj.activity.LocationMapAc;
import com.bm.tzj.kc.CoachInformationAc;
import com.bm.tzj.kc.CourseCompanionAc;
import com.bm.util.BaseDataUtil;
import com.bm.util.Util;
import com.bm.view.FlowLayout;
import com.bm.view.TagAdapter;
import com.bm.view.TagFlowLayout;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.widget.BannerView;
import com.lib.widget.BannerView.OnBannerClickListener;
import com.lib.widget.FuGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 我的课程详情
 * 
 * @author shiyt
 * 
 */
public class MyCourseDetailAc extends BaseActivity implements OnClickListener {
	private Context mContext;
	private CourseAdapter adapter;
	private ScrollView sv_list;
	private RegistrationAdapter registrAdapter;
	private List<Model> list =new ArrayList<Model>();
	private List<Baby> listPerson =new ArrayList<Baby>();
//	private FuListView lv_course;
	private FuGridView gv_person;
	private LinearLayout ll_jlinfo,ll_bmrs,ll_kc_one,ll_kc_two,ll_address;
	private com.bm.view.RatingBar sectorView;
	BannerView banner;
	private ImageView img_course,img_head,img_bg;
	private TextView tv_name,tv_price,tv_category,tv_age,tv_time,tv_address/*,tv_coursecontent*/,tv_username
					 ,tv_jlage/*,tv_zysx*/,tv_num,tv_two_address;
					// ,tv_register,tv_collet,tv_share;
	private TagFlowLayout mFlowLayout;
	private String[] mVals;
	private WebView webview;
	private ArrayList<StoreInfo> storeInfos=new ArrayList<StoreInfo>();
	private int index=0;//标记选择的哪个门店的地址，为了获取相应的经纬度,默认是第一个
	private LinearLayout  ll_map;
	private String[] picArr = new String[]{
			"http://www.totalfitness.com.cn/upfile/681x400/20120323104125_324.jpg",
			"http://d.hiphotos.baidu.com/zhidao/pic/item/d31b0ef41bd5ad6e1e7e401f83cb39dbb7fd3cbb.jpg",
			"http://elegantliving.ceconline.com/TEAMSITE/IMAGES/SITE/ES/20080203_EL_ES_02_02.jpg"};
	/**1 户外俱乐部  2暑期大露营   3城市营地*/
	private String degree="",strCoachId=""; 
	private HotGoods hotGoods;
	private int  babyCount;//报名人数
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mycourse_detail);
		mContext = this;
		setTitleName("课程详情");
		init();
	}
	
	public void init(){
		degree=getIntent().getStringExtra("degree");
		
		webview = (WebView) findViewById(R.id.webview);//课程要点
		webview.setBackgroundColor(0);
		webview.getBackground().setAlpha(0);
		
		sv_list = findScrollViewById(R.id.sv_list);
		tv_num = findTextViewById(R.id.tv_num);
		img_bg=findImageViewById(R.id.img_bg);
		gv_person=(FuGridView) findViewById(R.id.gv_person);
		registrAdapter=new RegistrationAdapter(mContext, listPerson);
		gv_person.setAdapter(registrAdapter);
		
		ll_map=findLinearLayoutById(R.id.ll_map);
		img_course=findImageViewById(R.id.img_course);
		img_head=findImageViewById(R.id.img_head);
		sectorView=(com.bm.view.RatingBar) findViewById(R.id.sectorView);
		tv_name=findTextViewById(R.id.tv_name);
		tv_price=findTextViewById(R.id.tv_price);
		tv_category=findTextViewById(R.id.tv_category);
		tv_age=findTextViewById(R.id.tv_age);
		tv_time=findTextViewById(R.id.tv_time);
		ll_bmrs=findLinearLayoutById(R.id.ll_bmrs);
		tv_address=findTextViewById(R.id.tv_address);
//		tv_coursecontent=findTextViewById(R.id.tv_coursecontent);
		tv_username=findTextViewById(R.id.tv_username);
		tv_jlage=findTextViewById(R.id.tv_jlage);
//		tv_zysx=findTextViewById(R.id.tv_zysx);
		ll_jlinfo=findLinearLayoutById(R.id.ll_jlinfo);
		/**根据不同分类显示布局*/
		ll_kc_one=findLinearLayoutById(R.id.ll_kc_one);
		ll_address=findLinearLayoutById(R.id.ll_address);
		tv_two_address = findTextViewById(R.id.tv_two_address);
//		ll_kc_two=findLinearLayoutById(R.id.ll_kc_two);
		
//		int w = App.getInstance().getScreenWidth();
//		int h = (300*w)/700;
//		fm_image.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,h));
		
		ll_map.setOnClickListener(this);
		ll_jlinfo.setOnClickListener(this);
		ll_bmrs.setOnClickListener(this);
		
		initViewPager();
		
		gv_person.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent(mContext, CourseCompanionAc.class);
				intent.putExtra("goodsId", hotGoods.goodsId);
				intent.putExtra("babyCount", hotGoods.babyCount);
				startActivity(intent);
			}
		});
		 
		ll_kc_one.setVisibility(View.VISIBLE);
		sv_list.smoothScrollTo(0, 0);
		getCourseDetial();
	}
	// 初始化viewpager
		public void initViewPager() {
			banner = (BannerView) findViewById(R.id.bannerView);
			banner.setOnBannerClickListener(new OnBannerClickListener() {
				@Override
				public void OnBannerClicked(int pos) {
					
				}
			});
			banner.update(picArr);
		}
		
		/**
		 * 获取课程详情
		 */
		public void getCourseDetial(){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("goodsId", getIntent().getStringExtra("goodsId"));
			if(null != App.getInstance().getUser()){
				map.put("userId", App.getInstance().getUser().userid);
			}
			if(null != App.getInstance().getChild())
				map.put("babyId", App.getInstance().getChild().babyId);
			
			showProgressDialog();
			UserManager.getInstance().getTzjgoodsGoodsdetail(mContext, map, new ServiceCallback<CommonResult<HotGoods>>() {
				
				@Override
				public void error(String msg) {
					hideProgressDialog();
					dialogToast(msg);
				}
				
				@Override
				public void done(int what, CommonResult<HotGoods> obj) {
					hideProgressDialog();
					if(null !=obj.data){
						hotGoods=obj.data;
						setData(obj.data);
//						getHotGoods();
					}
				}
			});
		}
		
	/**
	 * 获取详情信息
	 */
	public void setData(HotGoods data){
		strCoachId = data.coachId;
		babyCount = Integer.valueOf(getNullIntData(data.babyCount));
		
		ImageLoader.getInstance().displayImage(data.coachAvatar,img_head, App.getInstance().getHeadOptions());//教练头像
		ImageLoader.getInstance().displayImage(data.titleMultiUrl, img_bg,App.getInstance().getAdvertisingImageOptions());
		tv_name.setText(data.goodsName); // 课程名称
		tv_price.setText("￥"+data.goodsPrice); // 课程价格
		Map<String, Dictionary> dicMap = BaseDataUtil.shiHeNianLingMap;
		if(dicMap != null)
		{
			Dictionary dic = dicMap.get(data.suitableAge);
			if(dic != null)
			{
				tv_age.setText(dic.showvalue);
				this.findTextViewById(R.id.tv_ages).setText(dic.showvalue);
			}
		}
//		if("1".equals(data.suitableAge)){
//			tv_age.setText("3-4岁");// 适合年龄适合年龄 1：3-4岁  2：5-8岁  3：9岁以上
//			this.findTextViewById(R.id.tv_ages).setText("3-4岁");
//		}else if("2".equals(data.suitableAge)){
//			tv_age.setText("5-8岁");
//			this.findTextViewById(R.id.tv_ages).setText("5-8岁");
//		}else if("3".equals(data.suitableAge)){
//			tv_age.setText("9岁以上");
//			this.findTextViewById(R.id.tv_ages).setText("9岁以上");
//		}
//		tv_coursecontent.setText(getNullData(data.coursePoints));// 课程内容
		//课程要点
		if (data.coursePoints != null) {
			Util.initViewWebData(webview, data.coursePoints + "");
		} else {
			webview.setLayoutParams(new LayoutParams(0, 0));
		}
		tv_username.setText(getNullData(data.coachName));// 教练名
		tv_jlage.setText(getNullData(data.coachAge)==""?"0岁":data.coachAge+"岁");// 教练年龄
//		tv_zysx.setText(getNullData(data.notice));// 注意事项
		tv_num.setText(data.babyCount);//报名人数
		
		if(null!=data.baby&&data.baby.size()>0){
			listPerson.clear();
			if(data.baby.size()>5){
				for (int i = 0; i < 5; i++) {
					listPerson.add(data.baby.get(i));
				}
			}else{
				listPerson.addAll(data.baby);
			}
			registrAdapter.notifyDataSetChanged();
		}
		
			sectorView.setRating(getNullIntData(data.coachLogistics)-1);
			tv_address.setText(getNullData(data.address));// 地址
		if(data.goodsType.equals("1")){
			tv_category.setText("城市营地");// 课程分类
			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","HH:mm"));//时间
		}else if(data.goodsType.equals("2")){
			tv_category.setText("暑期大露营");// 课程分类
			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","MM-dd"));//时间
		}else{
			tv_category.setText("周末户外");// 课程分类
			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","MM-dd"));//时间
		}
		
		
		//门店
//		mVals=new String[data.storeInfo.size()];
//		for (int i = 0; i < data.storeInfo.size(); i++) {
//			mVals[i]=data.storeInfo.get(i).storeName;
//		}
//		storeInfos=data.storeInfo;
//		setMD();
//		if(null!=storeInfos&&storeInfos.size()>0){
//			tv_two_address.setText(storeInfos.get(0).address);
//		}
		
		
	}
	
	public void getDataPerson(){
		for (int i = 0; i < 5; i++) {
			listPerson.add(new Baby());
		}
		registrAdapter.notifyDataSetChanged();
	}
	
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

		};
	};
	
	
	// 门店
		private void setMD() {
			final LayoutInflater mInflater = LayoutInflater.from(this);
			mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
			// mFlowLayout.setMaxSelectCount(3);
			TagAdapter<String> adapter;
			mFlowLayout.setAdapter(adapter=new TagAdapter<String>(mVals) {

				@Override
				public View getView(FlowLayout parent, int position, String s) {
					TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
					tv.setText(s);
					return tv;
				}
			});
			adapter.setSelectedList(0);
			mFlowLayout
					.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
						@Override
						public boolean onTagClick(View view, int position,
								FlowLayout parent) {
							
//							index=Integer.valueOf(selectPosSet.toString().replace("[", "").replace("]", ""));
							index=position;
							tv_two_address.setText(storeInfos.get(index).address);
							return true;
						}
					});

			mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
				@Override
				public void onSelected(Set<Integer> selectPosSet) {
					
				}
			});
		}

	

	@Override
	public void onClick(View v) {
		Intent intent =null;
		switch (v.getId()) {
		case R.id.ll_bmrs: // 报名人数
			if(babyCount>0){
				intent = new Intent(mContext, CourseCompanionAc.class);
				intent.putExtra("goodsId", hotGoods.goodsId);
				intent.putExtra("babyCount", hotGoods.babyCount);
				startActivity(intent);
			}
			break; 
		case R.id.ll_map:  //地图
			if( hotGoods.lon.length() == 0 || hotGoods.lat.length() == 0){
				dialogToast("经纬度为空");
				return;
			}
			intent = new Intent(this,LocationMapAc.class);
			intent.putExtra("longitude", hotGoods.lon);
			intent.putExtra("latitude",  hotGoods.lat);
			startActivity(intent);
			break;
		case R.id.ll_jlinfo:  //教练信息
			if(TextUtils.isEmpty(strCoachId)){
				dialogToast("教练信息为空");
			}else{
				intent = new Intent(mContext, CoachInformationAc.class);
				intent.putExtra("title", "教练信息");
				intent.putExtra("coachId", strCoachId);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
}
