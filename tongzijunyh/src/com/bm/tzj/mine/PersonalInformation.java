package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureActivity;
import com.bm.dialog.AddSelectDialog;
import com.bm.dialog.NumberPickerDialog;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.entity.Province;
import com.bm.entity.User;
import com.bm.entity.post.UserPost;
import com.bm.tzj.activity.MainAc;
import com.bm.util.Util;
import com.bm.view.CircleImageView;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 个人信息
 * 
 * @author wanghy
 * 
 */
public class PersonalInformation extends BaseCaptureActivity implements
		OnClickListener {
	private Context context;
	
	private CircleImageView iv_sixview_head;
	private LinearLayout ll_sex, ll_city, ll_babySex, ll_birthday;
	private TextView tv_sex, tv_city, tv_babySex, tv_birthday, tv_save;
	private EditText et_userName, et_telPhone, et_address, et_babyName;
	NumberPickerDialog dailogA, dailogB;
	private String[] sexArr = new String[] { "男", "女" };
	private String sex = "男", babySex = "男", imagePath = "";
	String strProvinceName="",strCityName="",strAreaName="",strPageType="";

	AddSelectDialog diaAddr;
	private ThreeButtonDialog buttonDialog;
	public List<String> uploadListImg = new ArrayList<String>();
	
	String strUserName,strSex,strBabySex,strCity,strPhone,strAddress,strBabyName,strBirthday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_personal_information);
		context = this;
		setTitleName("个人信息");
		strPageType = getIntent().getStringExtra("pageType");
		
		initView();
		if(strPageType.equals("MineFm")){
			setRightName("编辑");
		}
		setFoucs(false);//默认不能编辑
	}
	@Override
	protected void onResume() {
		super.onResume();
//		setRightName("编辑");
//		setFoucs(false);//默认不能编辑
	}
	@Override
	public void clickRight() {
		super.clickRight();
		if("编辑".equals(tv_right.getText().toString())){
			setRightName("取消");
			setFoucs(true);
		}else if("取消".equals(tv_right.getText().toString())){
			setRightName("编辑");
			setFoucs(false);
		}
	}
	
	
	
	/**
	 * 设置焦点
	 */
	private void setFoucs(boolean flag){
		iv_sixview_head.setClickable(flag);
		
		et_userName.setEnabled(flag);
		if(flag){et_userName.setHint("请输入家长姓名");}else{et_userName.setHint("");}
		if(flag){tv_sex.setHint("请选择性别");}else{tv_sex.setHint("");}
		if(flag){tv_babySex.setHint("请选择孩子性别");}else{tv_babySex.setHint("");}
		if(flag){tv_city.setHint("请选择所在城市");}else{tv_city.setHint("");}
		
		et_telPhone.setEnabled(flag);
		if(flag){et_telPhone.setHint("请输入手机");}else{et_telPhone.setHint("");}
		
		ll_sex.setClickable(flag);
		if(flag){ll_sex.requestFocus();}
		
		ll_city.setClickable(flag);
		if(flag){ll_sex.requestFocus();}
		
		et_address.setEnabled(flag);
		if(flag){et_address.setHint("请输入联系地址");}else{et_address.setHint("");}
		
		et_babyName.setEnabled(flag);
		if(flag){et_babyName.setHint("请输入孩子姓名");}else{et_babyName.setHint("");}
		
		ll_babySex.setClickable(flag);
		if(flag){ll_babySex.requestFocus();}
		
		ll_birthday.setClickable(flag);
		if(flag){
			tv_birthday.setHint("请输入出生日期");}else{tv_birthday.setHint("");
			ll_birthday.requestFocus();}
		
		if(flag){
			tv_save.setVisibility(View.VISIBLE);
		}else{
			tv_save.setVisibility(View.GONE);
		}
		
	}
	private void initView() {
		ll_sex = findLinearLayoutById(R.id.ll_sex);
		ll_sex.setOnClickListener(this);
		ll_city = findLinearLayoutById(R.id.ll_city);
		ll_city.setOnClickListener(this);
		ll_babySex = findLinearLayoutById(R.id.ll_babySex);
		ll_babySex.setOnClickListener(this);
		ll_birthday = findLinearLayoutById(R.id.ll_birthday);
		ll_birthday.setOnClickListener(this);
		tv_save = findTextViewById(R.id.tv_save);
		tv_save.setOnClickListener(this);
		
		et_userName = findEditTextById(R.id.et_userName);
		et_telPhone = findEditTextById(R.id.et_telPhone);
		et_address = findEditTextById(R.id.et_address);
		et_babyName = findEditTextById(R.id.et_babyName);

		tv_birthday = findTextViewById(R.id.tv_birthday);
		tv_babySex = findTextViewById(R.id.tv_babySex);
		tv_city = findTextViewById(R.id.tv_city);
		tv_sex = findTextViewById(R.id.tv_sex);
		
		iv_sixview_head = (CircleImageView)findViewById(R.id.iv_sixview_head);
		iv_sixview_head.setOnClickListener(this);
		buttonDialog = new ThreeButtonDialog(context).setFirstButtonText("拍照")
				.setBtn1Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						takePhoto();
					}
				}).setThecondButtonText("从手机相册选择")
				.setBtn2Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						pickPhoto();
					}
				}).autoHide();

		c = Calendar.getInstance();
		_year = c.get(Calendar.YEAR);
		_month = c.get(Calendar.MONTH);
		_day = c.get(Calendar.DAY_OF_MONTH);
		setDate();
		
		et_babyName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				et_userName.setText(et_babyName.getText().toString().trim()+"的家长");
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
	 */
	public void setDate() {
		getAllCityInfo();
		
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
						System.out.println("sex"+sexArr[arg]);
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
		
	    User uInfo = App.getInstance().getUser();
		if(null == uInfo){
			strUserName="请输入家长姓名";
			strSex="请选择性别";
			strBabySex="请选择孩子性别";
			strCity="请选择所在城市";
			strPhone="请输入手机";
			strAddress="请输入联系地址";
			strBabyName="请输入孩子姓名";
			strBirthday="请输入出生日期";
		}else{
			strUserName=uInfo.nickname;
			if("1".equals(uInfo.gender)){
				strSex="男";
			}else if("2".equals(uInfo.gender)){
				strSex="女";
			}else {
				strSex="未知";
			}
			if("1".equals(uInfo.babyGender)){
				strBabySex="男";
			}else if("2".equals(uInfo.babyGender)){
				strBabySex="女";
			}else {
				strBabySex="未知";
			}
			strCity = getNullData(uInfo.provinceName)+" "+getNullData(uInfo.regionName)+" "+getNullData(uInfo.areaName);
			strPhone = getNullData(uInfo.phone);
			strAddress =getNullData(uInfo.address);
			strBabyName =getNullData(uInfo.babyName);
			strBirthday =Util.toString(getNullData(uInfo.babyBirthday), "yyyy-MM-dd HH:mm", "yyyy-MM-dd");
		}
		
		strProvinceName = uInfo.provinceName;
		strCityName = uInfo.regionName;
		strAreaName = uInfo.areaName;
		
		et_userName.setText(strUserName);
		tv_babySex.setText(strBabySex);
		tv_sex.setText(strSex);
		tv_city.setText(strCity);
		et_telPhone.setText(strPhone);
		et_address.setText(strAddress);
		et_babyName.setText(strBabyName);
		tv_birthday.setText(strBirthday);
		ImageLoader.getInstance().displayImage(uInfo.avatar+"", iv_sixview_head, App.getInstance().getGrayHeadImage());
		
		
		
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
		return new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				_year = year;
				_month = monthOfYear;
				_day = dayOfMonth;
				times = _year + "-" + (_month + 1) + "-" + _day;

				// if(year>)
				int day = Util.comparisonTime(times,
						Util.getCurrentDateString());
				if (day < 0) {
					dialogToast("出生日期不能是未来时间");
					tv_birthday.setText(Util.getCurrentDateString());
					return;
				}
				tv_birthday.setText(Util.setDate(times));
			}
		}, _year, _month, _day);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_sex:
			dailogA.show();
			break;
		case R.id.ll_birthday:
			dialogToast("不可更改宝宝出生日期");
//			Dialog dialog = onCreateDialog();
//			dialog.show();
			break;
		case R.id.ll_city:
			diaAddr.show();
			break;
		case R.id.ll_babySex:
			dailogB.show();
			break;
		case R.id.tv_save:
			submitInfo();
			break;
		case R.id.iv_sixview_head:
			buttonDialog.show();
			break;
		default:
			break;
		}
	}

	private void submitInfo() {
		String userName = et_userName.getText().toString().trim();
//		String phone = et_telPhone.getText().toString().trim();
		String city = tv_city.getText().toString().trim();
		String baByName = et_babyName.getText().toString().trim();
		String birthday = tv_birthday.getText().toString().trim();
		String address = et_address.getText().toString().trim();

		if (userName.length() ==0) {
			dialogToast("家长姓名不能为空");
			return;
		}
		
		if (userName.length() > 8) {
			dialogToast("家长姓名长度不能大于8");
			return;
		}

//		if (phone.length() ==0) {
//			dialogToast("手机号码不能为空");
//			return;
//		}

//		if (!Util.isMobileNO(phone)) {
//			dialogToast("手机号码格式不正确");
//			return;
//		}
		
		if (city.length() ==0) {
			dialogToast("所在城市不能为空");
			return;
		}
		
//		if (baByName.length() ==0) {
//			dialogToast("宝宝姓名不能为空");
//			return;
//		}
//
//		if (birthday.length() ==0) {
//			dialogToast("出生日期不能为空");
//			return;
//		}
		
		if (App.getInstance().getUser() == null) {
			return;
		}
		
		UserPost post = new UserPost();
		if (sex.equals("男")) {
			post.gender = "1";
		} else {
			post.gender = "2";
		}
		
//		if (babySex.equals("男")) {
//			post.babyGender = "1";
//		} else {
//			post.babyGender = "2";
//		}
		
		post.userId = App.getInstance().getUser().userid;
//		post.birthday = birthday;
		post.nickName = userName;
//		post.babyName =baByName; 
		post.regionName = strCityName;
		post.areaName = strAreaName;
		post.provinceName = strProvinceName;
		post.phone = "-1";
		post.address = address;
		post.coachId=getNullData(App.getInstance().getUser().coachId);
		post.coachName=getNullData(App.getInstance().getUser().coachName);
		
		
		showProgressDialog();
		UserManager.getInstance().getTzjcasuserbabyUpdateUserBabyInfo(context, imagePath, post, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				finish();
				MainAc.intance.getUserInfo();
				dialogToastHandler("修改成功", 1000, PersonalInformation.this);
			}
		});
	}

	@Override
	protected void onPhotoTaked(String photoPath) {
		imagePath = photoPath;
		ImageLoader.getInstance().displayImage("file://"+photoPath, iv_sixview_head, App.getInstance().getListViewDisplayImageOptions());
		uploadListImg.clear();
		uploadListImg.add(photoPath);
	}
	@Override
	protected void onPhotoListTaked(List<String> photoPath) {
		// TODO Auto-generated method stub
		
	}
}
