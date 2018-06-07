package com.bm.tzj.mine;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseCaptureActivity;
import com.bm.dialog.NumberPickerDialog;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.entity.Child;
import com.bm.util.HttpUtil;
import com.bm.util.Util;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddChildAc extends BaseCaptureActivity implements OnClickListener {

	private Context context;
	
	private Handler handler = new Handler();
	
	private ImageView iv_sixview_head,ib_sixview_head,lv_sixview_head;
	private LinearLayout ll_babySex, ll_birthday,ll_sixview_head;
	private TextView tv_babySex, tv_birthday, tv_save;
	private RadioButton rb_female,rb_male;
	private RadioGroup rg_group ;
	private EditText et_babyName;
	NumberPickerDialog dailogA, dailogB;
	private String babySex = "", imagePath = "";
	
	private ThreeButtonDialog buttonDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		contentView(R.layout.ac_add_child);
		setTitleName("添加孩子信息");
		babySex = "";
		initView();
	}
	boolean isCheckSex = false;
	private void initView() {
		//ll_babySex = findLinearLayoutById(R.id.ll_babySex);
		ll_sixview_head = findLinearLayoutById(R.id.ll_sixview_head);
		//ll_babySex.setOnClickListener(this);
		ll_birthday = findLinearLayoutById(R.id.ll_birthday);
		ll_birthday.setOnClickListener(this);
		tv_save = findTextViewById(R.id.tv_save);
		tv_save.setOnClickListener(this);
		rb_male = (RadioButton) findViewById(R.id.rb_male);
		rg_group = (RadioGroup) findViewById(R.id.rg_group);
		rb_female = (RadioButton) findViewById(R.id.rb_female);
		//rb_male.setButtonDrawable(R.drawable.radio);
		//rb_female.setButtonDrawable(R.drawable.radio);
		et_babyName = findEditTextById(R.id.et_babyName);

		tv_birthday = findTextViewById(R.id.tv_birthday);
		//tv_babySex = findTextViewById(R.id.tv_babySex);
		
		iv_sixview_head = (ImageView)findViewById(R.id.iv_sixview_head);
		ib_sixview_head = (ImageView)findViewById(R.id.ib_sixview_head);
		lv_sixview_head = (ImageView)findViewById(R.id.lv_sixview_head);
		ib_sixview_head.setOnClickListener(this);
		iv_sixview_head.setImageResource(R.drawable.moren_head);
		
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
		_year = c.get(Calendar.YEAR)-1;
		_month = c.get(Calendar.MONTH);
		_day = c.get(Calendar.DAY_OF_MONTH);
		//setDate();
		rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				if(checkedId == rb_male.getId()) {
					babySex="男";
				}else if(checkedId == rb_female.getId()){
					babySex="女";
				}
				tv_save.setEnabled(true);
				isCheckSex = true;
			}
		});

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
				if (s.length()==0){
					tv_save.setEnabled(false);
				}else if (s.toString().trim().length()>0&&isCheckSex&&
						tv_birthday.getText().toString().trim().length()>0)
					tv_save.setEnabled(true);
			    }
		});
	}
	
	/**
	 * 设置信息
	 */
//	public void setDate() {
//		dailogB = new NumberPickerDialog(context, sexArr, "请选择性别",
//				new NumberPickerDialog.SelectValue() {
//
//					@Override
//					public void getValue(int arg) {
//						System.out.println("sex"+sexArr[arg]);
//						babySex = sexArr[arg];
//					}
//				}, new NumberPickerDialog.CancleClick() {
//
//					@Override
//					public void click(View arg) {
//						dailogB.dismiss();
//					}
//
//					@Override
//					public void clickConform(View arg) {
//						tv_babySex.setText(babySex);
//						dailogB.dismiss();
//
//					}
//				});
//	}
	
	@Override
	protected void onPhotoTaked(String photoPath) {
		imagePath = photoPath;
		iv_sixview_head.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage("file://"+photoPath, lv_sixview_head, App.getInstance().getListViewDisplayImageOptions());
		tv_save.setEnabled(true);
	}
	
	private void submitInfo()
	{
		String baByName = et_babyName.getText().toString().trim();
		String birthday = tv_birthday.getText().toString().trim();
		String badysex = "";

		if (baByName.length() ==0) {
			dialogToast("宝宝姓名不能为空");
			return;
		}
		if (birthday.length() ==0) {
			dialogToast("出生日期不能为空");
			return;
		}
		if (babySex.length() ==0) {
			dialogToast("孩子性别不能为空");
			return;
		}
		if(babySex.equals("男"))
			badysex = "1";
		else
			badysex = "2";
		
//		if(TextUtils.isEmpty(imagePath))
//		{
//			dialogToast("请上传孩子头像照片");
//			return;
//		}
		
		if (App.getInstance().getUser() == null) {
			return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);
		}
		map.put("realName", baByName);
		map.put("birthday", birthday);
		map.put("gender", badysex);
//		List<File> files = new ArrayList<File>();
//		if (!TextUtils.isEmpty(imagePath)) {
//			files.add(new File(imagePath));
//		}
		
		showProgressDialog();
		
			AsyncHttpHelp.httpGet(context, BaseApi.API_ADDCHILD, map, new ServiceCallback<CommonResult<Child>>(){
				@Override
				public void done(int what, CommonResult<Child> obj) {
					if(!TextUtils.isEmpty(imagePath))
					{
						HashMap<String,String> paramMap = new HashMap<String,String>();
						paramMap.put("babyId", obj.data.babyId);
						HttpUtil.uploadData(BaseApi.API_UPLOADCHILDAvatar, "avatar", new File(imagePath), paramMap, new HttpUtil.OnResponseListener() {
							@Override
							public void onSuccess(String result) {
								handler.post(new Runnable(){
									@Override
									public void run() {
										hideProgressDialog();
										finish();
										App.toast("孩子添加成功");
									}});
							}
							@Override
							public void onError(String error) {
								handler.post(new Runnable(){
									@Override
									public void run() {
										hideProgressDialog();
										finish();
										App.toast("孩子添加成功,但是头像上传可能失败。");
									}});
							}
						});
					}
					else
					{
						hideProgressDialog();
						finish();
						App.toast("孩子添加成功");
					}
				}
				
				@Override
				public void error(String msg) {
					hideProgressDialog();
					dialogToast(msg);
				}
			});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_birthday:
			Dialog dialog = onCreateDialog();
			dialog.show();
			break;
//		case R.id.ll_babySex:
//			dailogB.show();
//			break;
		case R.id.tv_save:
			submitInfo();
			break;
		case R.id.ib_sixview_head:
			buttonDialog.show();
			break;
		default:
			break;
		}
	}

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
		DatePickerDialog d =  new DatePickerDialog(this, new OnDateSetListener() {
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
				tv_save.setEnabled(true);
			}
		}, _year, _month, _day);

		d.getDatePicker().setMaxDate(new Date().getTime());
//		d.getDatePicker().setMinDate(minDate);
		
		return d;
	}

	@Override
	protected void onPhotoListTaked(List<String> photoPath) {
		// TODO Auto-generated method stub
		
	}
}
