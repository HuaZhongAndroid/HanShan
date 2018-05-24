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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.api.IMService;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Badge;
import com.bm.entity.User;
import com.bm.tzj.czzx.HonoRollAc;
import com.bm.tzj.czzx.MedalDetailAc;
import com.bm.util.SixPullulatePanelTwo;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 成长中心
 * @author shiyt
 *
 */
public class GrowthCenterAc extends BaseActivity implements OnClickListener {

	private Context context;
	private ListView lv_content;
	private LinearLayout ll_Msg;
	private TextView tv_get,tv_a_num,tv_b_num,tv_ry,tv_name,tv_age;

	private ImageView iv_sixview_head;
	private RelativeLayout rl_a,rl_b,rl_c;
	private FrameLayout fm_content;
	private ImageButton ib_left;
	int strMedalId=-1;
	String strIsMyFriend="";//是否是我的好友
	public static final int CLICK_STATES=1001;
	Intent intent;
	private String pageType;
	View view_1;
	
	private List<Badge> list = new ArrayList<Badge>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		contentView(R.layout.fm_pullulate);
//		setTitleName("成长中心");
		setContentView(R.layout.fm_pullulate);
		context = this;
		pageType=getIntent().getStringExtra("pageType");
		initView();
		isHaveData(false);
		
	}

	/**
	 * 初始化数据
	 */
	private void initView() {
		ib_left = (ImageButton) findViewById(R.id.ib_left);
		tv_age = findTextViewById(R.id.tv_age);
		tv_name = findTextViewById(R.id.tv_name);
		fm_content = (FrameLayout) findViewById(R.id.fm_content);
		view_1=findViewById(R.id.view_1);
		tv_a_num=findTextViewById(R.id.tv_a_num);
		tv_b_num=findTextViewById(R.id.tv_b_num);
		tv_ry = findTextViewById(R.id.tv_ry);
		
		rl_a=findRelativeLayoutById(R.id.rl_a);
		rl_b=findRelativeLayoutById(R.id.rl_b);
		rl_c=findRelativeLayoutById(R.id.rl_c);
		
		tv_get=findTextViewById(R.id.tv_get);
		tv_get.setOnClickListener(this);
		
		rl_a.setOnClickListener(this);
		rl_b.setOnClickListener(this);
		rl_c.setOnClickListener(this);
		ib_left.setOnClickListener(this);
		iv_sixview_head = (ImageView)findViewById(R.id.iv_sixview_head);
		
		/**从荣誉榜点击某个用户进入成长中心  隐藏荣誉排行*/
		if(pageType.equals("HonoRollAc")){
			rl_b.setVisibility(View.GONE);
			view_1.setVisibility(View.GONE);
			tv_ry.setText("已关注");
			tv_ry.setBackgroundResource(R.drawable.pullulate_label_one);
		}
//		else{
//			tv_ry.setText("荣誉榜");
//		}

		getBabyInfo();
		getMedalInfo();//获取勋章信息
	}
	
	/**
	 * 查询宝贝信息
	 */
	private void getBabyInfo() {
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(null != App.getInstance().getUser()){
			map.put("userid",getIntent().getStringExtra("friendId"));
		}else{
			map.put("userid","0");
		}
		
		map.put("friendId",  App.getInstance().getUser().userid);//好友ID
		map.put("babyId", getIntent().getStringExtra("friendBabyId"));
		UserManager.getInstance().getTzjcasuserbabySearchUserBabyInfo(context, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
				isHaveData(false);
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if(null != obj.data){
					setDate(obj.data);
				}
			}
		});
	}
	
	/**
	 * 获取勋章信息
	 */
	public void getMedalInfo(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", getIntent().getStringExtra("friendId"));//好友ID
		map.put("babyId", getIntent().getStringExtra("friendBabyId"));
		UserManager.getInstance().getTzjmedalMedallist(context, map, new ServiceCallback<CommonListResult<Badge>>() {
			
			@Override
			public void error(String msg) {
				isHaveData(false);
				hideProgressDialog();				
			}
			
			@Override
			public void done(int what, CommonListResult<Badge> obj) {
				isHaveData(true);
				hideProgressDialog();
				if(obj.data.size()>0){
					if(fm_content.getChildCount()>0){
						fm_content.removeAllViews();
					}
					for (int i = 0; i < obj.data.size(); i++) {
						list.add(obj.data.get(i));
					}
					if(list.size()%4==1){
						list.add(new Badge());
					}else if(list.size()%4==3){
						list.add(new Badge());
					} 
					
					SixPullulatePanelTwo.setViews(fm_content, list, context,handler);
				}
			}
		});
	}
	
	/**
	 * 加关注
	 */
//	public void addFriends(){
//		
//		HashMap<String, String> map = new HashMap<String, String>();
//		if(null == App.getInstance().getUser()){
//			map.put("userId", App.getInstance().getUser().userid);
//		}else{
//			map.put("userId", "0");
//		}
//		map.put("friendId",  getIntent().getStringExtra("friendId"));//好友ID
//		showProgressDialog();
//		UserManager.getInstance().getTzjfriendsAddfriends(context, map, new ServiceCallback<StringResult>() {
//			
//			@Override
//			public void error(String msg) {
//				hideProgressDialog();
//			}
//			
//			@Override
//			public void done(int what, StringResult obj) {
//				hideProgressDialog();
//				addFriend();
//	dialogToast("等待对方同意，添加您为好友");
//				finish();
//			}
//		});
//		
//	}
	
	/**
	 * 加好友  IM
	 */
	private void addFriend() {
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userid", "0");
		}else{
			map.put("userid", App.getInstance().getUser().userid);
			
		}
		map.put("friendId", getIntent().getStringExtra("friendId"));
		showProgressDialog();
		IMService.getInstance().addFriend(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				dialogToast("等待对方同意，添加您为好友");
			}
		});
	}
	
	public void setDate(User user){
		tv_name.setText(user.babyName);//宝贝姓名
		String strSex = user.babyGender;
		if(strSex.equals("1")){
			strSex="男";
		}else if(strSex.equals("2")){
			strSex="女";
		}else if(strSex.equals("0")){
			strSex="未知";
		}
		
		String address =getNullData(user.provinceName) +" "+getNullData(user.regionName);
		tv_age.setText(strSex+"  "+getNullData(user.babyAge)+"岁 |   "+ address);//宝贝性别 年龄地址
		tv_a_num.setText(user.medalNum==null?"0":user.medalNum);//宝贝勋章
//		tv_b_num.setText(user.honourSort==null?"0":"NO."+user.honourSort);//宝贝勋章
		
//		iv_sixview_head.setBackgroundDrawable(getResources().getDrawable(R.drawable.pullulate_head));
		if(null !=user.avatar && user.avatar.length()>0){
			ImageLoader.getInstance().displayImage(user.avatar, iv_sixview_head, App.getInstance().getListViewDisplayImageOptions());
		}else{
			iv_sixview_head.setBackgroundDrawable(getResources().getDrawable(R.drawable.pullulate_head));
		}
		
		
		
		strIsMyFriend = getNullData(user.isMyFriend);
		if(strIsMyFriend.equals("0")){
			tv_ry.setText("关注");
			tv_ry.setBackgroundResource(R.drawable.pullulate_label);
		}else if(strIsMyFriend.equals("1")){
			tv_ry.setText("已关注");
			tv_ry.setBackgroundResource(R.drawable.pullulate_label_one);
		}
		isHaveData(true);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CLICK_STATES:
				strMedalId = msg.arg1;
				Badge bInfo = (Badge) msg.obj;
				intent = new Intent(context, MedalDetailAc.class);
				intent.putExtra("medalInfo", bInfo);
				startActivity(intent);
				break;
			}
			}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_get:
			intent = new Intent(context, MedalDetailAc.class);
			startActivity(intent);
			break;
		case R.id.rl_a:
			break;
		case R.id.rl_b:
			break;
		case R.id.rl_c:
			if(strIsMyFriend.equals("0")){
				addFriend();//添加好友
			}else if(tv_ry.getText().toString().trim().equals("荣誉榜")){
				intent = new Intent(context, HonoRollAc.class);
				startActivity(intent);
			}
//			if(!pageType.equals("HonoRollAc")){
//				intent = new Intent(context, HonoRollAc.class);
//				startActivity(intent);
//			}else{
//				addFriends();
//			}
			break;
		case R.id.ib_left:
			finish();
			break;
		default:
			break;
		}
	}
}
