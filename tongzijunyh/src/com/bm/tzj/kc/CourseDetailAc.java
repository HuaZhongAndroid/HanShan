//package com.bm.tzj.kc;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.WebView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.bm.api.BaseApi;
//import com.bm.api.UserManager;
//import com.bm.app.App;
//import com.bm.base.BaseActivity;
//import com.bm.base.adapter.CourseAdapter;
//import com.bm.base.adapter.RegistrationAdapter;
//import com.bm.dialog.UtilDialog;
//import com.bm.entity.Baby;
//import com.bm.entity.Dictionary;
//import com.bm.entity.HotGoods;
//import com.bm.entity.Order;
//import com.bm.entity.StoreInfo;
//import com.bm.share.ShareModel;
//import com.bm.tzj.activity.LocationMapAc;
//import com.bm.tzj.activity.MainAc;
//import com.bm.tzj.caledar.CaledarAc;
//import com.bm.tzj.city.City;
//import com.bm.tzj.mine.LoginAc;
//import com.bm.util.BaseDataUtil;
//import com.bm.util.Util;
//import com.bm.view.TagFlowLayout;
//import com.lib.http.ServiceCallback;
//import com.lib.http.result.CommonListResult;
//import com.lib.http.result.CommonResult;
//import com.lib.http.result.StringResult;
//import com.lib.widget.BannerView;
//import com.lib.widget.BannerView.OnBannerClickListener;
//import com.lib.widget.FuListView;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.richer.tzj.R;
//
///**
// * 课程详情
// *
// * @author shiyt
// *
// */
//public class CourseDetailAc extends BaseActivity implements OnClickListener {
//	private Context mContext;
//	private ScrollView sv;
//	private CourseAdapter adapter;
//	private RegistrationAdapter registrAdapter;
//	private List<HotGoods> list = new ArrayList<HotGoods>();
//	private List<Baby> listPerson = new ArrayList<Baby>();
//	private FuListView lv_course;
//	private com.lib.widget.FuGridView gv_person;
//	private WebView webview,webview_zysx;
//	private View view_play,view_bottom;
//
////	RatingBar room_ratingbar;
//	private com.bm.view.RatingBar sectorView;
//	private TagFlowLayout mFlowLayout;
//	private LinearLayout ll_mzsm, ll_jlinfo,ll_bmrs,ll_time,ll_kc_one,ll_kc_two,ll_map_two,ll_bm,ll_checkMd,ll_coach,ll_wbr;
//	private ImageView img_course, img_head,img_collect,img_share,img_bg;
//	private TextView tv_name, tv_price, tv_category, tv_age, tv_time,tv_time2,tv_age2,
//			tv_address, tv_coursecontent, tv_username, tv_jlage/*, tv_zysx*/,
//			tv_register, tv_collet, tv_share,tv_two_address,tv_num,tv_mdAddress;
//	BannerView banner;
//	private LinearLayout ll_map;
//	private boolean flag=false;
//	private String[] picArr = new String[] {
//			"http://www.totalfitness.com.cn/upfile/681x400/20120323104125_324.jpg",
//			"http://d.hiphotos.baidu.com/zhidao/pic/item/d31b0ef41bd5ad6e1e7e401f83cb39dbb7fd3cbb.jpg",
//			"http://elegantliving.ceconline.com/TEAMSITE/IMAGES/SITE/ES/20080203_EL_ES_02_02.jpg" };
//	private String[] mVals;
//	private ArrayList<StoreInfo> storeInfos=new ArrayList<StoreInfo>();
//	private int index=0;//标记选择的哪个门店的地址，为了获取相应的经纬度,默认是第一个
//	//3 户外俱乐部  2暑期大露营   1城市营地
//	private String strTag="",degree="",strCoachId="",strGoodsType="",strLon=""/*经度*/,strLat=""/*纬度*/,strStoreId=""/*门店ID*/,strStoreName=""/*门店名称*/,strStoreAddress=""/*门店地址*/;
//	private int  babyCount;//报名人数
//	private String  isBought ;// 是否报名 0 未买 1 已买 2 待支付
//	private boolean isCollected;//是否收藏
//	String checkStoreGoodsId="";//选择门店后的课程
//	City city=null;
//	public static CourseDetailAc intances;
//	private HotGoods hotGoods;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		contentView(R.layout.ac_coursedetail);
//		mContext = this;
//		intances = this;
//		city = App.getInstance().getCityCode();
//		setTitleName("课程详情");
//		degree=getIntent().getStringExtra("degree");
//		init();
//	}
//	/**
//	 * 设置焦点
//	 *
//	 */
//	public void setFoucs() {
//		sv.smoothScrollTo(0, 0);
//		lv_course.setFocusable(false);
//	}
//	public void init() {
//		webview = (WebView) findViewById(R.id.webview);//课程要点
//		webview.setBackgroundColor(0);
//		webview.getBackground().setAlpha(0);
//
//
//		webview_zysx = (WebView) findViewById(R.id.webview_zysx);//课程注意事项
//		webview_zysx.setBackgroundColor(0);
//		webview_zysx.getBackground().setAlpha(0);
//
//		view_bottom = findViewById(R.id.view_bottom);
//		view_play = findViewById(R.id.view_play);
//		ll_checkMd = findLinearLayoutById(R.id.ll_checkMd);
//		ll_checkMd.setOnClickListener(this);
//		tv_mdAddress = findTextViewById(R.id.tv_mdAddress);
//		ll_coach = findLinearLayoutById(R.id.ll_coach);
//		ll_wbr = findLinearLayoutById(R.id.ll_wbr);
//
//		tv_age2 = findTextViewById(R.id.tv_age2);
//		ll_bm = findLinearLayoutById(R.id.ll_bm);
//		sv = (ScrollView) findViewById(R.id.sv);
//		lv_course = (FuListView) findViewById(R.id.lv_course);
//		adapter = new CourseAdapter(mContext, list);
//		lv_course.setAdapter(adapter);
//
//		gv_person = (com.lib.widget.FuGridView) findViewById(R.id.gv_person);
//		registrAdapter = new RegistrationAdapter(mContext, listPerson);
//		gv_person.setAdapter(registrAdapter);
//		sectorView=(com.bm.view.RatingBar) findViewById(R.id.sectorView);
////		room_ratingbar = (RatingBar) findViewById(R.id.room_ratingbar);
//		ll_mzsm = findLinearLayoutById(R.id.ll_mzsm);
//		ll_map = findLinearLayoutById(R.id.ll_map);
//		img_course = findImageViewById(R.id.img_course);
//		img_head = findImageViewById(R.id.img_head);
//		ll_bmrs=findLinearLayoutById(R.id.ll_bmrs);
//		tv_name = findTextViewById(R.id.tv_name);
//		tv_price = findTextViewById(R.id.tv_price);
//		ll_time=findLinearLayoutById(R.id.ll_time);
//		tv_category = findTextViewById(R.id.tv_category);
//		tv_age = findTextViewById(R.id.tv_age);
//		tv_time = findTextViewById(R.id.tv_time);
//		tv_time2=findTextViewById(R.id.tv_time2);
//		tv_address = findTextViewById(R.id.tv_address);
//		tv_two_address = findTextViewById(R.id.tv_two_address);
//		tv_coursecontent = findTextViewById(R.id.tv_coursecontent);
//		tv_username = findTextViewById(R.id.tv_username);
//		tv_jlage = findTextViewById(R.id.tv_jlage);
////		tv_zysx = findTextViewById(R.id.tv_zysx);
//		ll_jlinfo = findLinearLayoutById(R.id.ll_jlinfo);
//		img_bg=findImageViewById(R.id.img_bg);
//		img_collect = findImageViewById(R.id.img_collect);
//		img_share=findImageViewById(R.id.img_share);
//		tv_register = findTextViewById(R.id.tv_register);
//		tv_num=findTextViewById(R.id.tv_num);
//		/**根据不同分类显示布局*/
//		ll_kc_one=findLinearLayoutById(R.id.ll_kc_one);
//		ll_kc_two=findLinearLayoutById(R.id.ll_kc_two);
//		ll_map_two=findLinearLayoutById(R.id.ll_map_two);
//		if(degree.equals("1")){
//			ll_kc_two.setVisibility(View.VISIBLE);
//			ll_kc_one.setVisibility(View.GONE);
//		}else{
//			ll_kc_one.setVisibility(View.VISIBLE);
//			ll_kc_two.setVisibility(View.GONE);
//		}
//
//		// int w = App.getInstance().getScreenWidth();
//		// int h = (300*w)/700;
//		// fm_image.setLayoutParams(new
//		// LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,h));
//		img_collect.setOnClickListener(this);
//		img_share.setOnClickListener(this);
//		tv_register.setOnClickListener(this);
//
//		ll_bmrs.setOnClickListener(this);
//		ll_mzsm.setOnClickListener(this);
//
//		ll_map.setOnClickListener(this);
//		ll_jlinfo.setOnClickListener(this);
//		ll_time.setOnClickListener(this);
//		ll_map_two.setOnClickListener(this);
//		getCourseDetial(getIntent().getStringExtra("goodsId"),getIntent().getStringExtra("pageTag"));//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2门店选择
////		getDataPerson();
//
//		initViewPager();
//		setFoucs();
////		room_ratingbar.setIsIndicator(true);// 禁止点击星星
//		gv_person.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Intent intent = new Intent(mContext, CourseCompanionAc.class);
//				intent.putExtra("goodsId", hotGoods.goodsId);
//				intent.putExtra("babyCount", hotGoods.babyCount);
//				startActivity(intent);
//			}
//		});
//	}
//
//	// 初始化viewpager
//	public void initViewPager() {
//		banner = (BannerView) findViewById(R.id.bannerView);
//		banner.setOnBannerClickListener(new OnBannerClickListener() {
//			@Override
//			public void OnBannerClicked(int pos) {
//
//			}
//		});
//		banner.update(picArr);
//	}
//
//	/**
//	 *设置详情信息
//	 */
//	public void setData(HotGoods data,String tag) {
//		strTag = tag;
//		strCoachId = data.coachId;
//		strLon = data.lon;
//		strLat = data.lat;
//		ImageLoader.getInstance().displayImage(data.titleMultiUrl, img_bg,App.getInstance().getAdvertisingImageOptions());
//
//		tv_name.setText(data.goodsName); // 课程名称
//		tv_price.setText("￥"+data.goodsPrice); // 课程价格
//		Map<String, Dictionary> dicMap = BaseDataUtil.shiHeNianLingMap;
//		if(dicMap != null)
//		{
//			Dictionary dic = dicMap.get(data.suitableAge);
//			if(dic != null)
//			{
//				tv_age.setText(dic.showvalue);
//				tv_age2.setText(dic.showvalue);
//				this.findTextViewById(R.id.tv_ages).setText(dic.showvalue);
//			}
//		}
////		if("1".equals(data.suitableAge)){
////			tv_age.setText("3-4岁");// 适合年龄适合年龄 1：3-4岁  2：5-8岁  3：9岁以上
////			tv_age2.setText("3-4岁");
////			this.findTextViewById(R.id.tv_ages).setText("3-4岁");
////		}else if("2".equals(data.suitableAge)){
////			tv_age.setText("5-8岁");
////			tv_age2.setText("5-8岁");
////			this.findTextViewById(R.id.tv_ages).setText("5-8岁");
////		}else if("3".equals(data.suitableAge)){
////			tv_age.setText("9岁以上");
////			tv_age2.setText("9岁以上");
////			this.findTextViewById(R.id.tv_ages).setText("9岁以上");
////		}
//
//		tv_address.setText(data.address);// 地址
//		tv_num.setText(data.babyCount);//报名人数
//		if(Integer.valueOf(getNullData(data.babyCount))==0){
//			view_play.setVisibility(View.GONE);
//			gv_person.setVisibility(View.GONE);
//		}
//
//		babyCount = Integer.valueOf(getNullIntData(data.babyCount));
//
//		ImageLoader.getInstance().displayImage(data.coachAvatar,img_head, App.getInstance().getheadImage());//教练头像
//		tv_username.setText(data.coachName);// 教练名
//		tv_jlage.setText(getNullData(data.coachAge)==""?"0岁":data.coachAge+"岁");// 教练年龄
//		 if(null!=data.baby&&data.baby.size()>0){
//				listPerson.clear();
//				if(data.baby.size()>5){
//					for (int i = 0; i < 5; i++) {
//						listPerson.add(data.baby.get(i));
//					}
//				}else{
//					listPerson.addAll(data.baby);
//				}
//				registrAdapter.notifyDataSetChanged();
//			}
//
////		room_ratingbar.setRating(Integer.valueOf("3.0"));// 星星个数
//		sectorView.setRating(getNullIntData(data.coachLogistics)-1);
//		strGoodsType = data.goodsType;
//		if(data.goodsType.equals("1")){
//			tv_category.setText("城市营地");// 课程分类
//			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","HH:mm"));//时间
//		}else if(data.goodsType.equals("2")){
//			tv_category.setText("暑期大露营");// 课程分类
//			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","MM-dd"));//时间
//		}else{
//			tv_category.setText("周末户外");// 课程分类
//			tv_time.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","MM-dd"));//时间
//		}
//
//		//门店
//		if("1".equals(tag)){//基础数据
//			mVals=new String[data.storeInfo.size()];
//			for (int i = 0; i < data.storeInfo.size(); i++) {
//				mVals[i]=data.storeInfo.get(i).storeName;
//			}
//			storeInfos=data.storeInfo;
//			setMD();
////			if(null!=storeInfos&&storeInfos.size()>0){
////				tv_two_address.setText(storeInfos.get(0).address);
////			}
//
//			if(data.goodsType.equals("1")){
//				ll_coach.setVisibility(View.GONE);
//				ll_wbr.setVisibility(View.GONE);
//			}else{
//				ll_coach.setVisibility(View.VISIBLE);
//				ll_wbr.setVisibility(View.VISIBLE);
//			}
//		}
//
//
//		isBought = data.isBought;
//		if(isBought.equals("1")){
//			tv_register.setText("已报名");
//			tv_register.setBackgroundColor(getResources().getColor(R.color.detail_color));
//			if(data.goodsType.equals("1")){
//				tv_time2.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","HH:mm"));//时间
//				tv_two_address.setText(getNullData(data.address));//门店地址
//				tv_mdAddress.setText(getNullData(data.storeName));//门店名称
//				ll_coach.setVisibility(View.VISIBLE);
//				ll_wbr.setVisibility(View.VISIBLE);
//
//			}
//		}else if(isBought.equals("2")){
//			tv_register.setText("待付款");
//			tv_register.setBackgroundColor(getResources().getColor(R.color.txt_yellow_color));
//			if(data.goodsType.equals("1")){
//				tv_time2.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","HH:mm"));//时间
//				tv_two_address.setText(getNullData(data.address));//门店地址
//				tv_mdAddress.setText(getNullData(data.storeName));//门店名称
//				ll_coach.setVisibility(View.VISIBLE);
//				ll_wbr.setVisibility(View.VISIBLE);
//			}
//		}else{
//			tv_register.setText("立即报名");
//			tv_register.setBackgroundColor(getResources().getColor(R.color.txt_yellow_color));
//		}
//
//		/**
//		 * 是否收藏  isCollected 未收藏 0 已收藏1
//		 */
//		if(getNullData(data.isCollected).equals("1")){
//			//已收藏
//			img_collect.setImageResource(R.drawable.icon_collection_off);  //已收藏图片
//			isCollected = true;
//
//		}else{
//			//未收藏
//			img_collect.setImageResource(R.drawable.icon_collection);  //未收藏图片
//			isCollected = false;
//		}
//
//		//课程要点
//		if(data.coursePoints!=null){
//			Util.initViewWebData(webview, data.coursePoints+"");
//		}else{
//			webview.setLayoutParams(new LayoutParams(0,0));
//		}
//
//		//注意事项
//		if(data.notice!=null){
////			tv_zysx.setText(Html.fromHtml(data.notice));
//			Util.initViewWebData(webview_zysx, data.notice+"");
//		}else{
//			webview_zysx.setLayoutParams(new LayoutParams(0,0));
//		}
//
//		if(null!=getIntent().getStringExtra("pageType")&&"CoachInformationAc".equals(getIntent().getStringExtra("pageType"))){//顾问教练   课程详情
//			view_bottom.setVisibility(View.GONE);
//			ll_bm.setVisibility(View.GONE);
//			ll_coach.setVisibility(View.GONE);//教练信息 隐藏
//			if(data.goodsType.equals("1")){
//				strStoreId = hotGoods.storeId;
//				strStoreAddress = getNullData(hotGoods.address);
//				strStoreName = getNullData(hotGoods.storeName);
//				tv_two_address.setText(getNullData(hotGoods.address));//门店地址
//				tv_mdAddress.setText(getNullData(hotGoods.storeName));//门店名称
//				tv_time2.setText(Util.toString(getNullData(data.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"—"+Util.toString(getNullData(data.endTime),"yyyy-MM-dd HH:mm:ss","HH:mm"));//时间
//			}
//
//		}else{
//			ll_bm.setVisibility(View.VISIBLE);
////			ll_coach.setVisibility(View.VISIBLE);//教练信息 显示
//		}
//
//		//从门店列表进来  直接将门店赋值
//		if(null!=getIntent().getStringExtra("pageType")&&"StoreListAc".equals(getIntent().getStringExtra("pageType"))){
//			HotGoods info = (HotGoods) getIntent().getSerializableExtra("info");
//			strStoreId = info.storeId;
//			strStoreAddress = getNullData(info.address);
//			strStoreName = getNullData(info.storeName);
//			tv_two_address.setText(strStoreAddress);//门店地址
//			tv_mdAddress.setText(strStoreName);//门店名称
//			strLon = getIntent().getStringExtra("lon");
//			strLat = getIntent().getStringExtra("lat");
//			ll_coach.setVisibility(View.GONE);
//		}
//
//	}
//
//	private Handler handler = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch(msg.what){
//			case 1:
//				finish();
//				MainAc.intance.finish();
//				Intent intent = new Intent(mContext,LoginAc.class);
//				startActivity(intent);
//				break;
//			}
//		};
//	};
//
//	@Override
//	public void onClick(View v) {
//		Intent intent = null;
//		switch (v.getId()) {
//		case R.id.ll_mzsm:// 免责声明
//			intent = new Intent(mContext, DisclaimerAc.class);
//			intent.putExtra("degree", getIntent().getStringExtra("degree"));
//			startActivity(intent);
//			break;
//		case R.id.img_share: // 分享
//			ShareModel model = new ShareModel();
//			model.title = "我参加了"+hotGoods.goodsName+",你也快来看看吧~";
//			model.conent = "我参加了"+hotGoods.goodsName+",你也快来看看吧~";
//			model.targetUrl = BaseApi.SHARE_URL + "&userid="+App.getInstance().getUser().userid + "&appVersion="+Util.getAppVersion(context);
//			share.shareInfo(model,0);
//			break;
//		case R.id.img_collect:// 收藏
//			if(null == App.getInstance().getUser()){
//				isLogin();
//				return;
//			}
//
//			if("1".equals(strTag)){
//				if(strGoodsType.equals("1")){
//					if(TextUtils.isEmpty(tv_mdAddress.getText())||"未选择".equals(tv_mdAddress.getText().toString())){
//						dialogToast("请先选择门店");
//						return;
//					}
//					if(TextUtils.isEmpty(tv_time2.getText())||"未选择".equals(tv_time2.getText().toString())){
//						dialogToast("请选择时间");
//						return;
//					}
//				}
//			}
//
//			if(!isCollected){
//				addCollection();
//			}else{
//				delCollection();
//			}
//			break;
//		case R.id.ll_time:// 时间
////			if(isBought.equals("0")){
//				if(TextUtils.isEmpty(tv_mdAddress.getText())||"未选择".equals(tv_mdAddress.getText().toString())){
//					dialogToast("请先选择门店");
//					return;
//				}
//				if(!"CoachInformationAc".equals(getIntent().getStringExtra("pageType"))){//顾问教练   课程详情
//					intent = new Intent(mContext, CaledarAc.class);
//					if(null!=getIntent().getStringExtra("pageType")&&"StoreListAc".equals(getIntent().getStringExtra("pageType"))){//门店列表进来
//						intent.putExtra("storeId", strStoreId);//门店id
//					}else{
//						intent.putExtra("storeId", storeInfos.get(index).storeId);//门店id
//					}
//					intent.putExtra("goodsId", hotGoods.goodsId);//课程id
//					startActivityForResult(intent, 001);
//				}
////			}
//			break;
//		case R.id.tv_register: // 立即报名
//			if(null == App.getInstance().getUser()){
//				isLogin();
//				return;
//			}
//			if(strGoodsType.equals("1")){
//				if(TextUtils.isEmpty(tv_mdAddress.getText())||"未选择".equals(tv_mdAddress.getText().toString())){
//					dialogToast("门店不能为空");
//					return;
//				}
//				if(TextUtils.isEmpty(tv_time2.getText())||"未选择".equals(tv_time2.getText().toString())){
//					dialogToast("上课时间不能为空");
//					return;
//				}
//			}
//			if(isBought.equals("0")){//立即报名
//				createOrder();
//			}else if (isBought.equals("2")){//待付款
//			    intent = new Intent(mContext, PayInfoAc.class);
//				intent.putExtra("hotGoods", hotGoods);
//				intent.putExtra("pageTag", "CourseDetailAc");
//				Order oInfo = new Order();
//				oInfo.orderId = hotGoods.orderId;
//				oInfo.payMoney = hotGoods.payMoney;
//				oInfo.orderNumber = hotGoods.orderNumber;
//
//				intent.putExtra("order", oInfo);
//				startActivity(intent);
//			}
////			intent = new Intent(mContext, PayInfoAc.class);
////			startActivity(intent);
//			break;
//		case R.id.ll_bmrs: // 报名人数
//			if(babyCount>0){
//				intent = new Intent(mContext, CourseCompanionAc.class);
//				intent.putExtra("goodsId", hotGoods.goodsId);
//				intent.putExtra("babyCount", hotGoods.babyCount);
//				startActivity(intent);
//			}
//			break;
//		case R.id.ll_map: // 地图
//			if(strLon.length() == 0 || strLat.length() == 0){
//				dialogToast("经纬度为空");
//				return;
//			}
//			System.out.println("strLon="+strLon+"strLat="+strLat);
//			intent = new Intent(this, LocationMapAc.class);
//			intent.putExtra("longitude", strLon);
//			intent.putExtra("latitude",strLat);
//			startActivity(intent);
//			break;
//		case R.id.ll_map_two: //带名店的地址
//			if(TextUtils.isEmpty(tv_two_address.getText())||"".equals(tv_two_address.getText().toString())){
//				dialogToast("门店地址为空，先选择门店");
//				return;
//			}
//			System.out.println("strLon"+strLon+"strLat="+strLat);
//			intent = new Intent(this, LocationMapAc.class);
//			intent.putExtra("longitude", strLon);
//			intent.putExtra("latitude", strLat);
////			intent.putExtra("longitude", storeInfos.get(index).lon);
////			intent.putExtra("latitude", storeInfos.get(index).lat);
//			startActivity(intent);
//			break;
//		case R.id.ll_jlinfo: // 教练信息
//			if(TextUtils.isEmpty(strCoachId)){
//				dialogToast("教练信息为空");
//			}else{
//				intent = new Intent(mContext, CoachInformationAc.class);
//				intent.putExtra("title", "教练信息");
//				intent.putExtra("coachId", strCoachId);
//				startActivity(intent);
//			}
//			break;
//		case R.id.ll_checkMd://选择门店
//			//从门店列表进来  直接将门店赋值
//			if(!"CoachInformationAc".equals(getIntent().getStringExtra("pageType"))){//顾问教练   课程详情
//				if(null!=getIntent().getStringExtra("pageType")&&!"StoreListAc".equals(getIntent().getStringExtra("pageType"))){
//					intent = new Intent(mContext, CheckStoreAc.class);
//					intent.putExtra("storeList", storeInfos);
//					startActivityForResult(intent, 004);
//				}else{
//
//					if(TextUtils.isEmpty(tv_mdAddress.getText())||"未选择".equals(tv_mdAddress.getText().toString())){
//						intent = new Intent(mContext, CheckStoreAc.class);
//						intent.putExtra("storeList", storeInfos);
//						startActivityForResult(intent, 004);
//					}
//				}
//			}
//			break;
//		default:
//			break;
//		}
//	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode==002){
//			checkStoreGoodsId=data.getStringExtra("goodsId");
//			String myStartTime=data.getStringExtra("startTime");
//			String myEndTime=data.getStringExtra("endTime");
//			if(null!=data){
//				getCourseDetial(checkStoreGoodsId,"2");
//				String start="";
//				String end="";
//				start=Util.toString(getNullData(myStartTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
//				end=Util.toString(getNullData(myEndTime),"yyyy-MM-dd HH:mm:ss","HH:mm");
//				tv_time2.setText(start+"—"+end);//时间
//				getCoachBabyInfo(checkStoreGoodsId);
//			}
//		}else if (resultCode==004){
//			index=Integer.valueOf(data.getStringExtra("tag"));
//			tv_two_address.setText(storeInfos.get(index).address);//门店地址
//			tv_mdAddress.setText(storeInfos.get(index).storeName);//门店名称
//			strLat = storeInfos.get(index).lat;
//			strLon = storeInfos.get(index).lon;
//
//		}
//
//
//	}
//	// 门店
//	private void setMD() {
//		//201603-28 当前页面选择门店
////		final LayoutInflater mInflater = LayoutInflater.from(this);
////		mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
////		TagAdapter<String> adapter;
////		mFlowLayout.setAdapter(adapter=new TagAdapter<String>(mVals) {
////
////			@Override
////			public View getView(FlowLayout parent, int position, String s) {
////				TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
////				tv.setText(s);
////				return tv;
////			}
////		});
////		adapter.setSelectedList(0);
////		mFlowLayout
////				.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
////					@Override
////					public boolean onTagClick(View view, int position,
////							FlowLayout parent) {
////
//////						index=Integer.valueOf(selectPosSet.toString().replace("[", "").replace("]", ""));
////						index=position;
////						tv_two_address.setText(storeInfos.get(index).address);
////						tv_time2.setText("未选择");
////						return true;
////					}
////				});
////
////		mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
////			@Override
////			public void onSelected(Set<Integer> selectPosSet) {
////
////			}
////		});
//
//	}
//
//	public void isLogin(){
//		UtilDialog.dialogTwoBtnContentTtile(mContext, "您还未登录，请先登录在操作", "取消","确定","提示",handler,1);
//	}
//
//	/**
//	 * 获取课程详情
//	 */
//	public void getCourseDetial(String goodsId,final String tag){
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("goodsId", goodsId);
//		map.put("tag", tag);//判断是1基础数据 和2门店选中
//		if(null != App.getInstance().getUser()){
//			map.put("userId", App.getInstance().getUser().userid);
//		}else{
//			map.put("userId", "0");
//		}
//
//		if(null != App.getInstance().getChild())
//			map.put("babyId", App.getInstance().getChild().babyId);
//
//		showProgressDialog();
//		UserManager.getInstance().getTzjgoodsGoodsdetail(mContext, map, new ServiceCallback<CommonResult<HotGoods>>() {
//
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonResult<HotGoods> obj) {
//				hideProgressDialog();
//				if(null !=obj.data){
//					hotGoods=obj.data;
//					setData(obj.data,tag);
//					getHotGoods(obj.data.baseId+"");
//				}
//			}
//		});
//	}
//	/**
//	 * 获取推荐课程
//	 */
//	public void getHotGoods(String goodIds){
//		HashMap<String, String> map = new HashMap<String, String>();
//		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
//			map.put("regionName", city.regionName);//城市名称
//		}else{
//			map.put("regionName", "西安市");//城市名称
//		}
//		if(null != App.getInstance().getUser()){
//			map.put("babyAge", App.getInstance().getUser().babyAge);//宝贝年龄
//		}
//		map.put("pageNum", "1");//查询页数
//		map.put("pageCount", "3");//每页展示条数
//		map.put("goodsType", getIntent().getStringExtra("degree"));//类别
//		if(strGoodsType.equals("1")){
//			map.put("baseId", goodIds);//城市营地
//		}else{
//			map.put("goodsId", getIntent().getStringExtra("goodsId"));//课程ID
//
//		}
//
//		UserManager.getInstance().getTzjgoodsHotgoods(mContext, map, new ServiceCallback<CommonListResult<HotGoods>>() {
//
//			@Override
//			public void error(String msg) {
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonListResult<HotGoods> obj) {
//				list.clear();
//				if(null!=obj.data){
//					list.addAll(obj.data);
//					adapter.notifyDataSetChanged();
//
//					// 推荐课程点击事件
//					lv_course.setOnItemClickListener(new OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> arg0, View arg1,
//								int position, long arg3) {
//							Intent intent = new Intent(mContext, CourseDetailAc.class);
//							intent.putExtra("degree", list.get(position).goodsType);
//							intent.putExtra("goodsId", list.get(position).goodsId);
//							if(strGoodsType.equals("1")){
//								intent.putExtra("pageTag","1");
//							}else{
//								intent.putExtra("pageTag",getIntent().getStringExtra("pageTag"));//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索，基础数据）  2商品信息
//							}
//							intent.putExtra("pageType","CourseDetailAc");
//							startActivity(intent);
//						}
//					});
//				}
//			}
//		});
//	}
//
//	/**
//	 * 生成订单
//	 */
//	public void createOrder(){
//		HashMap<String, String> map = new HashMap<String, String>();
//		if(strGoodsType.equals("1")){
//			map.put("goodsId", checkStoreGoodsId);//课程ID
//		}else{
//			map.put("goodsId", getIntent().getStringExtra("goodsId"));//课程ID
//		}
//		if(null == App.getInstance().getUser()){
//			map.put("userId", "0");
//		}else{
//			map.put("userId", App.getInstance().getUser().userid);
//
//		}
//		if(null != App.getInstance().getChild())
//			map.put("babyId", App.getInstance().getChild().babyId);
//
//		showProgressDialog();
//		UserManager.getInstance().getTzjorderCreatebtsorder(mContext, map, new ServiceCallback<CommonResult<Order>>() {
//
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonResult<Order> obj) {
//				hideProgressDialog();
//				if(null !=obj.data){
//					Intent intent = new Intent(mContext, PayInfoAc.class);
//					hotGoods.orderId = obj.data.orderId;
//					hotGoods.payMoney = obj.data.payMoney;
//					intent.putExtra("hotGoods", hotGoods);
//					intent.putExtra("pageTag", "CourseDetailAc");
//					intent.putExtra("order",  obj.data);
//					startActivity(intent);
//
//					flag=true;
//					isBought = "2";
//					tv_register.setText("待付款");
//					tv_register.setBackgroundColor(getResources().getColor(R.color.txt_yellow_color));
//				}
//			}
//		});
//	}
//
//	/**
//	 * 添加收藏
//	 */
//	public void addCollection(){
//
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("baseId", getIntent().getStringExtra("goodsId"));//课程ID
//		if(null != App.getInstance().getUser()){
//			map.put("userid", App.getInstance().getUser().userid);//用户ID
//		}else{
//			map.put("userid", "0");//教练ID
//		}
//
//		showProgressDialog();
//		UserManager.getInstance().getTzjgoodsGoodsCollect(mContext, map, new ServiceCallback<StringResult>() {
//
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, StringResult obj) {
//				hideProgressDialog();
//				isCollected = true;
//				img_collect.setImageResource(R.drawable.icon_collection_off);  //已收藏图片
////				flag=true;
//			}
//		});
//	}
//
//	/**
//	 * 删除收藏
//	 */
//	public void delCollection(){
//		if(null == App.getInstance().getUser()){
//			return;
//		}
//
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("baseId", getIntent().getStringExtra("goodsId"));//课程ID
//		if(null != App.getInstance().getUser()){
//			map.put("userid", App.getInstance().getUser().userid);//用户ID
//		}else{
//			map.put("userid", "0");//教练ID
//		}
//		map.put("delType", "1");//删除类型   0 全部清空  1 按课程id删除   2 删除失效课程
//
//		showProgressDialog();
//		UserManager.getInstance().getMyselfDelCollection(mContext, map, new ServiceCallback<StringResult>() {
//
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, StringResult obj) {
//				hideProgressDialog();
//				isCollected = false;
//				img_collect.setImageResource(R.drawable.icon_collection);  //未收藏图片
////				flag=false;
//			}
//		});
//	}
//
//	/**
//	 * 城市营地  根据门店ID
//	 */
//	public void getCoachBabyInfo(String goodsId){
//		HashMap<String, String> map = new HashMap<String, String>();
//		if(null!=getIntent().getStringExtra("pageType")&&"StoreListAc".equals(getIntent().getStringExtra("pageType"))){//门店列表进来
//			map.put("storeId", strStoreId);//门店ID
//		}else{
//			map.put("storeId", storeInfos.get(index).storeId);//门店ID
//		}
//
//		map.put("goodsId", goodsId);
//		UserManager.getInstance().getTzgoodsCoachBabyInfo(mContext, map, new ServiceCallback<CommonResult<HotGoods>>() {
//
//			@Override
//			public void error(String msg) {
//				dialogToast(msg);
//			}
//
//			@Override
//			public void done(int what, CommonResult<HotGoods> obj) {
//				if(null != obj.data){
//					ll_coach.setVisibility(View.VISIBLE);
//					ll_wbr.setVisibility(View.VISIBLE);
//					HotGoods hotGood = obj.data;
//					ImageLoader.getInstance().displayImage(hotGood.coachAvatar,img_head, App.getInstance().getheadImage());//教练头像
//					tv_username.setText(hotGood.coachName);// 教练名
//					tv_jlage.setText(getNullData(hotGood.coachAge)==""?"0岁":hotGood.coachAge+"岁");// 教练年龄
//
//					 if(null!=hotGood.baby&&hotGood.baby.size()>0){
//							listPerson.clear();
//							if(hotGood.baby.size()>5){
//								for (int i = 0; i < 5; i++) {
//									listPerson.add(hotGood.baby.get(i));
//								}
//							}else{
//								listPerson.addAll(hotGood.baby);
//							}
//							registrAdapter.notifyDataSetChanged();
//						}
//
//				}
//			}
//		});
//
//	}
//}
