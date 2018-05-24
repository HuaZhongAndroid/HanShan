package com.bm.tzj.mine;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.AddSelectDialog;
import com.bm.dialog.NumberPickerDialog;
import com.bm.entity.Child;
import com.bm.entity.Province;
import com.bm.entity.User;
import com.bm.entity.post.UserPost;
import com.bm.im.tool.Constant;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.CoachInformationAc;
import com.bm.util.BDLocationHelper;
import com.bm.util.MD5Util;
import com.bm.util.Util;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;
import com.umeng.socialize.utils.Log;

/**
 * 完善信息
 * @author shiyt
 *
 */
public class PerfectInfoAc extends BaseActivity implements OnClickListener {
	private Context context;
	private TextView tv_csrq,tv_sex,tv_city,tv_bb_sex,tv_jl,tv_submit,tv_babySex;
	private EditText et_jz_name,et_phone,et_jz_lxdz,et_bb_name,et_jl, et_tuijianma;
	NumberPickerDialog dailogA, dailogB;
	private String[] sexArr = new String[] { "男", "女" };
	private String sex = "男", babySex = "男";
	AddSelectDialog diaAddr;
	String strProvinceName="",strCityName="",strAreaName="";
	String strPageTag="";//Login 第三方登录完善信息  RegistreAc注册完善信息
	String authCode = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_perfectinfo);
		context = this;
		setTitleName("完善信息");
		initView();
	}
	
	public void initView(){
		strPageTag = getIntent().getStringExtra("pageTag");
		authCode = getIntent().getStringExtra("authCode");
		tv_csrq=findTextViewById(R.id.tv_csrq);
		tv_babySex=findTextViewById(R.id.tv_babySex);
		tv_city=findTextViewById(R.id.tv_city);
		tv_sex=findTextViewById(R.id.tv_sex);
		tv_jl=findTextViewById(R.id.tv_jl);
		tv_submit=findTextViewById(R.id.tv_submit);
		
		et_jz_name=findEditTextById(R.id.et_jz_name);
		et_phone=findEditTextById(R.id.et_phone);
		et_jz_lxdz=findEditTextById(R.id.et_jz_lxdz);
		et_bb_name=findEditTextById(R.id.et_bb_name);
		et_jl=findEditTextById(R.id.et_jl);
		et_tuijianma=findEditTextById(R.id.et_tuijianma);
		
		tv_csrq.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		tv_jl.setOnClickListener(this);
		
		tv_sex.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_babySex.setOnClickListener(this);
		
		c = Calendar.getInstance();
		_year = c.get(Calendar.YEAR);
		_month = c.get(Calendar.MONTH);
		_day = c.get(Calendar.DAY_OF_MONTH);
		setDate();
		
		et_bb_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				et_jz_name.setText(et_bb_name.getText().toString().trim()+"的家长");
			}
		});
	}
	
	/**
	 * 获取省市区信息
	 */
	public void getAllCityInfo(){
		String strJson = SharedPreferencesHelper.getString("allcitys",null);
		if(strJson != null)
		{
			CommonListResult<Province> citys = AsyncHttpHelp.getGson().fromJson(strJson, new ServiceCallback<CommonListResult<Province>>(){
				@Override
				public void done(int what, CommonListResult<Province> obj) {
				}
				@Override
				public void error(String msg) {
				}}.type);
			if(citys != null && citys.data.size() > 0)
			{
				diaAddr = new AddSelectDialog(context, new AddSelectDialog.CancleClick() {
					
					@Override
					public void clickConform(String arg,String provinceName, String cityName, String areaName) {
						tv_city.setText(arg);
						strProvinceName = provinceName;
						strCityName = cityName;
						strAreaName = areaName;
						diaAddr.dismiss();
					}
					
					@Override
					public void click(View arg) {
						diaAddr.dismiss();
					}
				},citys.data);
			}
		}
		else
		{
			UserManager.getInstance().getTzjtrendAllSearchregion(context,new HashMap<String, String>(), new ServiceCallback<CommonListResult<Province>>() {
				@Override
				public void error(String msg) {
				}
				@Override
				public void done(int what, CommonListResult<Province> obj) {
					if(obj.data.size()>0){
						
						diaAddr = new AddSelectDialog(context, new AddSelectDialog.CancleClick() {
							
							@Override
							public void clickConform(String arg,String provinceName, String cityName, String areaName) {
								tv_city.setText(arg);
								strProvinceName = provinceName;
								strCityName = cityName;
								strAreaName = areaName;
								diaAddr.dismiss();
							}
							
							@Override
							public void click(View arg) {
								diaAddr.dismiss();
							}
						},obj.data);
					}
				}
			});
		}
		
	}
	
	/**
	 * 设置信息
	 * 
	 * @param map
	 */
	public void setDate() {
		getAllCityInfo();
		et_phone.setText(getIntent().getStringExtra("phone").toString().trim());
		
		dailogA = new NumberPickerDialog(context, sexArr, "请选择性别",
				new NumberPickerDialog.SelectValue() {

					@Override
					public void getValue(int arg) {
						sex = sexArr[arg];
					}
				}, new NumberPickerDialog.CancleClick() {

					@Override
					public void click(View arg) {
						dailogA.dismiss();
					}

					@Override
					public void clickConform(View arg) {
						tv_sex.setText(sex);
						dailogA.dismiss();

					}
				});

		dailogB = new NumberPickerDialog(context, sexArr, "请选择性别",
				new NumberPickerDialog.SelectValue() {

					@Override
					public void getValue(int arg) {
						babySex = sexArr[arg];
					}
				}, new NumberPickerDialog.CancleClick() {

					@Override
					public void click(View arg) {
						dailogB.dismiss();
					}

					@Override
					public void clickConform(View arg) {
						tv_babySex.setText(babySex);
						dailogB.dismiss();

					}
				});

		/**
		 * 
		 * 地址选择控件
		 */
//		diaAddr = new AddSelectDialog(context, new AddSelectDialog.CancleClick() {
//			
//			@Override
//			public void clickConform(String arg,String provinceName, String cityName, String areaName) {
//				tv_city.setText(arg);
//				strAreaName = areaName;
//				strCityName = cityName;
//				strProvinceName = provinceName;
//				diaAddr.dismiss();
//			}
//			
//			@Override
//			public void click(View arg) {
//				diaAddr.dismiss();
//			}
//		});
	};
	
	/**
	 * 时间控件
	 * 
	 * @return
	 */
	// 时间控件

	private DatePickerDialog datePicker;

	private Calendar c;
	private int _year, _month, _day;
	private String times;

	private Dialog onCreateDialog() {
		return new DatePickerDialog(context, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				_year = year;
				_month = monthOfYear;
				_day = dayOfMonth;
				times = _year + "-" + (_month + 1) + "-" + _day;
				// _day);
				tv_csrq.setText(Util.setDate(times));
				// _day);
			}
		}, _year, _month, _day);

	}
	
	HashMap<String, String> map;
	private void submitInfo() {
		String userName = et_jz_name.getText().toString().trim();
		String phone = et_phone.getText().toString().trim();
		String city = tv_city.getText().toString().trim();
		String baByName = et_bb_name.getText().toString().trim();
		String birthday = tv_csrq.getText().toString().trim();
		String regionName = strCityName;
		String address = et_jz_lxdz.getText().toString().trim();
		String coachId = et_jl.getText().toString().trim();
		String coachName = tv_jl.getText().toString().trim();
		String inviteCode = et_tuijianma.getText().toString().trim();
		
		map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("authCode", authCode);
		map.put("deviceToken", Util.getIMEI(context));

		if (baByName.length() ==0) {
			dialogToast("孩子姓名不能为空");
			return;
		}
		
		if (birthday.length() ==0 || birthday.equals("未选择")) {
			dialogToast("出生日期不能为空");
			return;
		}
		
		if (userName.length() ==0) {
			dialogToast("家长姓名不能为空");
			return;
		}
		
		if (userName.length() > 8) {
			dialogToast("家长姓名长度不能大于8");
			return;
		}

		if (phone.length() ==0) {
			dialogToast("手机号码不能为空");
			return;
		}

		if (!Util.isMobileNO(phone)) {
			dialogToast("手机号码格式不正确");
			return;
		}
		
		if (city.length() ==0 || city.equals("未选择")) {
			dialogToast("所在城市不能为空");
			return;
		}

		
//		if (sex.length() ==0) {
//			dialogToast("性别不能为空");
//			return;
//		}
		
//		if (App.getInstance().getUser() == null) {
//			return;
//		}
		
		UserPost post = new UserPost();
		if (sex.equals("男")) {
			post.gender = "1";
//			map.put("gender", "1");
		} else {
			post.gender = "2";
//			map.put("gender", "2");
		}
		
		if (babySex.equals("男")) {
			post.babyGender = "1";
//			map.put("babyGender", "1");
		} else {
			post.babyGender = "2";
//			map.put("babyGender", "2");
		}
//		post.userId = App.getInstance().getUser().userid;
		post.birthday = birthday+" 00:00:00";
		post.nickname = userName;
		post.realName =baByName; 
		post.regionName = strCityName;
		post.areaName = strAreaName;
		post.provinceName = strProvinceName;
		post.phone = phone;
		post.address = address;
		post.coachId=coachId+"";
		post.coachName=coachName+"";
		post.password = getIntent().getStringExtra("passWord");
		post.sendPhone = getIntent().getStringExtra("phone");
		post.inviteCode = inviteCode;
		
		if(strPageTag.equals("Login")){
			post.loginType= getIntent().getStringExtra("loginType");
			post.openId= getIntent().getStringExtra("openId");
		}
		
		showProgressDialog();
		UserManager.getInstance().getTzjcasRegister(context, post,new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				finish();
				if(strPageTag.equals("RegistreAc")){//注册进来
//					ForgotpassAc.intance.finish();
					LoginAc.intatce.finish();
//					doLoginAgain(map);
					loginApp(obj.data);
					getBaby(obj.data.userid);
				}
				RegistreAc.intance.finish();
				if(null != MyTeachersAc.intanece){
					MyTeachersAc.intanece.finish();
				}
				
				if(null != CoachInformationAc.intance){
					CoachInformationAc.intance.finish();
				}
				
				dialogToastHandler("操作成功", 1000, PerfectInfoAc.this);
			}
		});
	}
	
	
//	void doLoginAgain(HashMap<String, String>map){
//		UserManager.getInstance().getTzjcasLogin(context,map, new ServiceCallback<CommonResult<User>>() {
//			
//			@Override
//			public void error(String msg) {
//				dialogToast(msg);
//				hideProgressDialog();
//			}
//			@Override
//			public void done(int what, CommonResult<User> obj) {
//				hideProgressDialog();
//				if (obj != null && obj.data != null) {
////					if(openId.length()>0){//第三方登录
////						if(obj.data.loginStatus.equals("0")){
////							Intent intent = new Intent(context, RegistreAc.class);//注册
////							intent.putExtra("pageType", "Login");
////							intent.putExtra("openId", openId);
////							intent.putExtra("loginType", typeFlag);
////							intent.putExtra("loginStatus", obj.data.loginStatus);
////							startActivity(intent);
////							return;
////						}
////					}
//					
////					//判断  记住密码 按钮的状态
////					if (isChecked) {
////						SharedPreferencesHelper.saveBoolean("isCheck", true);// 记住手机号码和密码
////						SharedPreferencesHelper.saveString("phone", strPhone);//保存密码
////						SharedPreferencesHelper.saveString("password", pwd);//保存密码
////					}else{
////						SharedPreferencesHelper.saveBoolean("isCheck", false);// 清空记住密码的信息
////						SharedPreferencesHelper.saveString("phone", "");
////						SharedPreferencesHelper.saveString("password", "");
////							
////					}
//					SharedPreferencesHelper.saveString("userid", obj.data.userid);//保存用户ID
////					SharedPreferencesHelper.saveString("passWord", pwd);//保存密码
//					
//					App.getInstance().setUser(obj.data);// 存储用户信息
//					if(obj.data.babyList!=null && obj.data.babyList.size()>0)
//						App.getInstance().setChild(obj.data.babyList.get(obj.data.babyList.size()-1));
//					
//					//jpush设置tag
//					Set<String> tags = new HashSet<String>();
//					tags.add(obj.data.phone);
//					tags.add(obj.data.pushid);
//					String city = BDLocationHelper.getCacheLocation().city;
//					city = city.replace("市", "");
//					city = MD5Util.md5(city);//MD5
//					tags.add(city);
//					JPushInterface.setTags(context, tags, new TagAliasCallback(){
//						@Override
//						public void gotResult(int arg0, String arg1, Set<String> arg2) {
//							Log.d("JPushInterface.setTags:"+arg0);
//						}});
//					
//					if(null != MainAc.intance){
//						MainAc.intance.finish();
//					}
//					Intent intent = new Intent(context, MainAc.class);
//					intent.putExtra("tag", 2);
//					startActivity(intent);
//					finish();
//				} else {
//					dialogToast("登录失败");
//				}
//			}
//		});
//	}
	
	
	
	/**
	 * 默认登录
	 */
	private void loginApp(User user) {
					
					//判断  记住密码 按钮的状态
						SharedPreferencesHelper.saveBoolean("isCheck", true);// 记住手机号码和密码
						SharedPreferencesHelper.saveString("phone", user.phone);//保存密码
//						SharedPreferencesHelper.saveString("password", pwd);//保存密码
						
						
					SharedPreferencesHelper.saveString("userid", user.userid);//保存用户ID
//					SharedPreferencesHelper.saveString("passWord", pwd);//保存密码
					
					App.getInstance().setUser(user);// 存储用户信息
					/**
					 * 这儿并没有娃的信息，存不存没啥区别。放着吧
					 */
					if(user.babyList!=null && user.babyList.size()>0)
						App.getInstance().setChild(user.babyList.get(user.babyList.size()-1));
					
					//jpush设置tag
					Set<String> tags = new HashSet<String>();
					tags.add(user.phone);
					tags.add(user.pushid);
					String city = BDLocationHelper.getCacheLocation().city;
					city = city.replace("市", "");
					city = MD5Util.md5(city);//MD5
					tags.add(city);
					JPushInterface.setTags(context, tags, new TagAliasCallback(){
						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							Log.d("JPushInterface.setTags:"+arg0);
						}});
					
					if(null != MainAc.intance){
						MainAc.intance.finish();
					}
					Intent intent = new Intent(context, MainAc.class);
					intent.putExtra("tag", 2);
					startActivity(intent);
					finish();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.out.println("#################"+resultCode);
//		if (4 == resultCode) {
//			
//			
//		}
//	}
	
	
	void getBaby(String userId){
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("userId", userId);
		UserManager.getInstance().getChildrenlist(this, map2, new ServiceCallback<CommonListResult<Child>>(){
			@Override
			public void done(int what, CommonListResult<Child> obj) {
				hideProgressDialog();
				if(obj.data!=null && obj.data.size()>0)
					App.getInstance().setChild(obj.data.get(obj.data.size()-1));
				
				
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_csrq:
			Dialog dialog = onCreateDialog();
			dialog.show();
			break;
		case R.id.tv_submit:
			Constant.COACH_ID="";//清空教练ID
			Constant.COACH_NAME="";//清空教练名称
			submitInfo();
			break;
		case R.id.tv_jl:
			Intent intent = new Intent(context, MyTeachersAc.class);
			intent.putExtra("pageType", "PerfectInfoAc");
			startActivityForResult(intent,1);
//			startActivity(intent);
			break;
		case R.id.tv_sex:
			dailogA.show();
			break;
		case R.id.tv_babySex:
			dailogB.show();
			break;
		case R.id.tv_city:
			diaAddr.show();
			break;
		default:
			break;
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		et_jl.setText(getNullData(Constant.COACH_ID));// 教练ID
		tv_jl.setText(getNullData(Constant.COACH_NAME));// 教练名称
	}
}
