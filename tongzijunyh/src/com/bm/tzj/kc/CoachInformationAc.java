package com.bm.tzj.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CertificateAdapter;
import com.bm.base.adapter.CommentAdapter;
import com.bm.base.adapter.CourseAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.CoachDiploma;
import com.bm.entity.CoachInfo;
import com.bm.entity.Comment;
import com.bm.entity.HotGoods;
import com.bm.im.tool.Constant;
import com.bm.tzj.activity.ImageViewActivity;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.LoginAc;
import com.bm.tzj.mine.MyTeachersAc;
import com.bm.tzj.mine.NoTeacherAc;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.widget.FuGridView;
import com.lib.widget.FuListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 教练信息
 * @author shiyt
 *
 */
public class CoachInformationAc extends BaseActivity implements OnClickListener {
	private Context mContext;
	private FuListView lv_course,lv_comment;
	private FuGridView gv_certificate;
	private ScrollView sv_coach;
	private List<HotGoods> listCourse = new ArrayList<HotGoods>();  //带过的课
	private List<Comment> listComment = new ArrayList<Comment>();  //评论
	private List<CoachDiploma> listCertificate = new ArrayList<CoachDiploma>();  //证书
	String [] paths;
	private CourseAdapter courseAdapter;
	private CertificateAdapter certiAdapter;
	private CommentAdapter commentAdapter;
	private ImageView img_pic,img_more;
	private com.bm.view.RatingBar room_ratingbar;
	private TextView tv_jcbd,tv_name,tv_age,tv_coursecontent;
	private LinearLayout ll_comment,ll_courseContent;
	String title="",strPageType="",strCoachId="";
	String strIsBind="";//是否绑定顾问教练
	int strType =-1; //解绑还是绑定   1 绑定  0解除
	CoachInfo cInfo;
	public static CoachInformationAc intance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_coachinformation);
		mContext = this;
		intance = this;
		title=getIntent().getStringExtra("title");
		strPageType = getIntent().getStringExtra("pageType");
		strCoachId = getIntent().getStringExtra("coachId");//教练ID
		if(title.equals("教练信息")){
			setTitleName("教练信息");	
		}else{
			if(strPageType.equals("NoTeacherAc") || strPageType.equals("PerfectInfoAc")){//我的
				setTitleName("教练信息");	
			}else {
				setTitleName("顾问教练");	
			}
			
		}
		
		init();
		setFoucs();
	}
	
	public void init(){
		sv_coach = findScrollViewById(R.id.sv_coach);
		img_more = findImageViewById(R.id.img_more);
		ll_courseContent = findLinearLayoutById(R.id.ll_courseContent);
		tv_age = findTextViewById(R.id.tv_age);
		tv_coursecontent = findTextViewById(R.id.tv_coursecontent);
		tv_name = findTextViewById(R.id.tv_name);
		ll_comment=findLinearLayoutById(R.id.ll_comment);
		tv_jcbd=findTextViewById(R.id.tv_jcbd);
		img_pic=(ImageView) findViewById(R.id.img_pic);

		room_ratingbar=(com.bm.view.RatingBar) findViewById(R.id.room_ratingbar);
		lv_course=(FuListView) findViewById(R.id.lv_course);
		lv_comment=(FuListView) findViewById(R.id.lv_comment);
		gv_certificate=(FuGridView) findViewById(R.id.gv_certificate);
		
		courseAdapter=new CourseAdapter(mContext, listCourse);
		lv_course.setAdapter(courseAdapter);
		
		certiAdapter=new CertificateAdapter(mContext, listCertificate);
		gv_certificate.setAdapter(certiAdapter);
		
		gv_certificate.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(mContext, ImageViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("images",paths);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		
		commentAdapter=new CommentAdapter(mContext, listComment);
		lv_comment.setAdapter(commentAdapter);
		
		tv_jcbd.setOnClickListener(this);
		ll_comment.setOnClickListener(this);
		
//		lv_course.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				Intent intent = new Intent(mContext, CourseDetailAc.class);
//				intent.putExtra("degree", listCourse.get(position).goodsType);
//				intent.putExtra("goodsId", listCourse.get(position).goodsId);
//				intent.putExtra("pageType", "CoachInformationAc");
//				intent.putExtra("pageTag", "2");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//
//
//				startActivity(intent);
//			}
//		});
		
		getCoachInfo();//获取教练信息
		
//		if(title.equals("教练信息")){
//			img_more.setVisibility(View.VISIBLE);
//			tv_jcbd.setTextColor(getResources().getColor(R.color.white));
//			tv_jcbd.setBackgroundResource(R.drawable.coach_btn);
//			tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.coach_btn));
//			ll_courseContent.setVisibility(View.VISIBLE);
//		}else{
//			img_more.setVisibility(View.GONE);
//			ll_courseContent.setVisibility(View.GONE);
//			tv_jcbd.setTextColor(getResources().getColor(R.color.txt_yellow_color));
//			tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bd));
//			if(strPageType.equals("MineFm")){
//				tv_jcbd.setText("解绑教练");
//			}else if (strPageType.equals("NoTeacherAc")){
//				tv_jcbd.setText("绑定教练");
//			}	
//		}
	}
	
	/**
	 * 设置焦点
	 * 
	 */
	public void setFoucs() {
		sv_coach.smoothScrollTo(0, 0);
//		lv_course.setFocusable(false);
	}
	
	/**
	 * 获取教练信息
	 */
	public void getCoachInfo(){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", strCoachId);//教练ID
		if(null != App.getInstance().getUser()){
			map.put("userId", App.getInstance().getUser().userid);//教练ID
		}else{
			map.put("userId", "0");//教练ID
		}
		showProgressDialog();
		
		UserManager.getInstance().getTzjcoachCoachdetail(mContext, map, new ServiceCallback<CommonResult<CoachInfo>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<CoachInfo> obj) {
				hideProgressDialog();
				cInfo = obj.data;
				setCoachDate();
			}
		});
	}
	
	public void setCoachDate(){
		String strCoachName = getNullData(cInfo.coachName);
		if(strCoachName.length()>4){
			strCoachName = strCoachName.substring(0,4)+"...";
		}
		tv_name.setText(strCoachName);//教练名称
		tv_age.setText(getNullData(cInfo.coachAge)==""?"0岁":cInfo.coachAge+"岁");//年龄
		tv_coursecontent.setText("个人介绍："+getNullData(cInfo.coachProfile));
		ImageLoader.getInstance().displayImage(cInfo.avatar+"", img_pic, App.getInstance().getAdvertisingImageOptions());
		

		room_ratingbar.setRating(getNullIntData(cInfo.coachLogistics)-1);
		
		if(cInfo.goodsInfo.size()>0){
			if(cInfo.goodsInfo.size()>5){
				for (int i = 0; i < 5; i++) {
					listCourse.add(cInfo.goodsInfo.get(i));
				}
			}else{
				listCourse.addAll(cInfo.goodsInfo);
			}
			
			courseAdapter.notifyDataSetChanged();
		}
		
		
		if(cInfo.comment.size()>0){
			if(cInfo.goodsInfo.size()>5){
				for (int i = 0; i < 5; i++) {
					listComment.add(cInfo.comment.get(i));
				}
			}else{
				listComment.addAll(cInfo.comment);
			}
			
			commentAdapter.notifyDataSetChanged();
		}
		listCertificate.clear();
		
		if(cInfo.coachDiploma.size()>0){
			listCertificate.addAll(cInfo.coachDiploma);
			certiAdapter.notifyDataSetChanged();
			
			paths=new String[cInfo.coachDiploma.size()];
			for (int i = 0; i < cInfo.coachDiploma.size(); i++) {
				paths[i]=cInfo.coachDiploma.get(i).diplomaTitleMultiUrl;
			}
		}

		strIsBind = cInfo.isBind;
		
		if(title.equals("教练信息")){
			img_more.setVisibility(View.VISIBLE);
			ll_courseContent.setVisibility(View.VISIBLE);
			
			if(cInfo.isBind.equals("0")){   //是不是我的绑定教练    0 不是  1 是
				tv_jcbd.setText("绑定为顾问教练");
				tv_jcbd.setTextColor(getResources().getColor(R.color.white));
				tv_jcbd.setBackgroundResource(R.drawable.coach_btn);
				tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.coach_btn));
			}else if (cInfo.isBind.equals("1")){
				tv_jcbd.setText("解绑顾问教练");	
				tv_jcbd.setTextColor(getResources().getColor(R.color.txt_yellow_color));
				tv_jcbd.setBackgroundResource(R.drawable.btn_bd);
				tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bd));
			}
		}else{
			img_more.setVisibility(View.GONE);
			ll_courseContent.setVisibility(View.GONE);
			tv_jcbd.setTextColor(getResources().getColor(R.color.txt_yellow_color));
			tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bd));
			if(strPageType.equals("MineFm")){//我的
				tv_jcbd.setText("解除绑定");
			}else if (strPageType.equals("NoTeacherAc")){//没有绑定顾问教练进来
				tv_jcbd.setText("绑定为顾问教练");
				tv_jcbd.setTextColor(getResources().getColor(R.color.white));
				tv_jcbd.setBackgroundResource(R.drawable.coach_btn);
				tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.coach_btn));
			}else if(strPageType.equals("PerfectInfoAc")){//完善个人信息进来
				tv_jcbd.setText("绑定为顾问教练");
				tv_jcbd.setTextColor(getResources().getColor(R.color.white));
				tv_jcbd.setBackgroundResource(R.drawable.coach_btn);
				tv_jcbd.setBackgroundDrawable(getResources().getDrawable(R.drawable.coach_btn));
			}
		}
		
//		ImageLoader.getInstance().displayImage("http://v1.qzone.cc/avatar/201403/30/09/33/533774802e7c6272.jpg!200x200.jpg", img_pic, App.getInstance().getHeadOptions());
		
	}
	
	public void checkBind(){
		String strContent="";
		if(title.equals("教练信息")){
			if(strType == 1){
				strContent="确定要绑定该教练信息";
			}else if(strType == 0){
				strContent="确定要解绑该教练信息";
			}
		}else{
			if(strType == 1){
				strContent="确定要绑定该顾问信息";
			}else if(strType == 0){
				strContent="确定要解绑该顾问信息";
			}
		}
		UtilDialog.dialogTwoBtnContentTtile(mContext, strContent, "取消","确定","提示",handler,1);
	}
	
	/**
	 * 绑定/解绑教练信息
	 */
	public void getBindCoachInfo(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", strCoachId);
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);
			
		}
		map.put("optType", strType+"");
		showProgressDialog();
		UserManager.getInstance().getTzjcoachBindingcoach(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				dialogToast("操作成功");
				MainAc.intance.getUserInfo();//刷新用户信息
				getCoachInfo();
				
				if(!title.equals("教练信息")){
					finish();
				}
				
			}
		});
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://退出登录
				getBindCoachInfo();
				break;
			case 2://
//				finish();
//				if(null != CourseDetailAc.intances){
//					CourseDetailAc.intances.finish();
//				}
				MainAc.intance.finish();
				Intent intent = new Intent(mContext,LoginAc.class);
				startActivity(intent);
				
				
				
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.tv_jcbd://绑定教练
			if(title.equals("教练信息")){
				if(null == App.getInstance().getUser()){
					UtilDialog.dialogTwoBtnContentTtile(mContext, "您还未登录，请先登录在操作", "取消","确定","提示",handler,2);
					return;
				}
				if(strIsBind.equals("1")){
					strType=0;
				}else if (strIsBind.equals("0")){
					strType=1;
				}
				checkBind();
			}else{
				if(strPageType.equals("PerfectInfoAc")){//完善个人信息进来
					//MyTeachersAc.intanece.finish();
					if (MyTeachersAc.intanece!=null){
						MyTeachersAc.intanece.finish();
					}
					Constant.COACH_ID=strCoachId;
					Constant.COACH_NAME=cInfo.coachName;
//					Intent intents=new Intent();
//					intents.putExtra("coachId", strCoachId);//教练ID
//					intents.putExtra("coachName", cInfo.coachName);//教练名称
//					intents.setClass(CoachInformationAc.this, PerfectInfoAc.class);
//					setResult(4, intents);
					finish();
				}else if(strPageType.equals("NoTeacherAc")){
					if (NoTeacherAc.intance!=null&&MyTeachersAc.intanece!=null){
						NoTeacherAc.intance.finish();
						MyTeachersAc.intanece.finish();
					}
					strType=1;
					checkBind();
//					getBindCoachInfo();//绑定教练
				}else if(strPageType.equals("MineFm")){
					strType=0;
					checkBind();
//					getBindCoachInfo();//解除教练
				}
			}
			
			break;
		case R.id.ll_comment:
			if(title.equals("教练信息")){
//				if(listComment.size()>0){
//					intent = new Intent(mContext, CommentAc.class);
//					startActivity(intent);
//				}else{
//					dialogToast("该教练暂无评论");
//				}
				intent = new Intent(mContext, CommentAc.class);
				intent.putExtra("coachInfo", cInfo);
				startActivity(intent);
			}
			
			break;

		default:
			break;
		}
		
	}
}
