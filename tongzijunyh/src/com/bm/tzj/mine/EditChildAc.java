package com.bm.tzj.mine;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditChildAc extends BaseCaptureActivity implements OnClickListener {

	private Context context;

	private Handler handler = new Handler();

	private ImageView iv_sixview_head;
	private TextView tv_birthday, tv_save;
	private EditText et_babyName;
	NumberPickerDialog dailogB;
	private String babySex = "", imagePath = "";

	private RadioButton rb_female, rb_male;
	private RadioGroup rg_group;

	private ThreeButtonDialog buttonDialog;

	private Child child;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		contentView(R.layout.ac_add_child);
		setTitleName("修改孩子信息");
		child = (Child) this.getIntent().getSerializableExtra("child");
		initView();
	}

	private void initView() {
		findViewById(R.id.ll_birthday).setOnClickListener(this);
//		this.findViewById(R.id.iv_birthday_arrow).setVisibility(View.INVISIBLE);

		tv_save = findTextViewById(R.id.tv_save);
		tv_save.setOnClickListener(this);
		tv_save.setEnabled(true);
		et_babyName = findEditTextById(R.id.et_babyName);

		tv_birthday = findTextViewById(R.id.tv_birthday);
		rb_male = (RadioButton) findViewById(R.id.rb_male);
		rg_group = (RadioGroup) findViewById(R.id.rg_group);
		rb_female = (RadioButton) findViewById(R.id.rb_female);
//		rb_male.setButtonDrawable(R.drawable.radio);
//		rb_female.setButtonDrawable(R.drawable.radio);

		findViewById(R.id.iv_sixview_head).setVisibility(View.GONE);
		iv_sixview_head = (ImageView) findViewById(R.id.lv_sixview_head);
		findViewById(R.id.ib_sixview_head).setOnClickListener(this);
		iv_sixview_head.setImageResource(R.drawable.moren_head);
		et_babyName.setText(child.realName);
		babySex = ("1".equals(child.gender) ? "男" : "女");
		rb_male.setChecked("1".equals(child.gender));
		rb_female.setChecked(!"1".equals(child.gender));

		tv_birthday.setText(child.birthday);
		ImageLoader.getInstance().displayImage(child.avatar, iv_sixview_head, App.getInstance().getheadImage());

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

//		Date date = Util.StringToDate(child.birthday, "yyyy-MM-dd");
//		c = Calendar.getInstance();
//		c.setTime(date);
//		_year = c.get(Calendar.YEAR);
//		_month = c.get(Calendar.MONTH);
//		_day = c.get(Calendar.DAY_OF_MONTH);

		rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb_male.getId()) {
					babySex = "男";
				} else if (checkedId == rb_female.getId()) {
					babySex = "女";
				}
				tv_save.setEnabled(true);
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
				if (s.length() == 0) {
					tv_save.setEnabled(false);
				} else {
					tv_save.setEnabled(true);
				}

			}
		});
	}


	@Override
	protected void onPhotoTaked(String photoPath) {
		imagePath = photoPath;
		ImageLoader.getInstance().displayImage("file://" + photoPath, iv_sixview_head, App.getInstance().getListViewDisplayImageOptions());
		tv_save.setEnabled(true);
	}

	private void submitInfo() {
		String baByName = et_babyName.getText().toString().trim();
		String birthday = tv_birthday.getText().toString().trim();
		String babysex = babySex;

		if (baByName.length() == 0) {
			dialogToast("宝宝姓名不能为空");
			return;
		}
		if (birthday.length() == 0) {
			dialogToast("出生日期不能为空");
			return;
		}
		if (babysex.length() == 0) {
			dialogToast("孩子性别不能为空");
			return;
		}
		if (babysex.equals("男"))
			babysex = "1";
		else
			babysex = "2";

//		if(TextUtils.isEmpty(imagePath))
//		{
//			dialogToast("请上传孩子头像照片");
//			return;
//		}

		if (App.getInstance().getUser() == null) {
			return;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("babyId", child.babyId);
		map.put("realName", baByName);
		map.put("birthday", birthday);
		map.put("gender", babysex);
//		List<File> files = new ArrayList<File>();
//		if (!TextUtils.isEmpty(imagePath)) {
//			files.add(new File(imagePath));
//		}

		showProgressDialog();

		AsyncHttpHelp.httpGet(context, BaseApi.API_EDITCHILD, map, new ServiceCallback<CommonResult<Child>>() {
			@Override
			public void done(int what, CommonResult<Child> obj) {
				if (!TextUtils.isEmpty(imagePath)) {
					HashMap<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("babyId", child.babyId);
					HttpUtil.uploadData(BaseApi.API_UPLOADCHILDAvatar, "avatar", new File(imagePath), paramMap, new HttpUtil.OnResponseListener() {
						@Override
						public void onSuccess(String result) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									hideProgressDialog();
									finish();
									App.toast("孩子信息修改成功");
								}
							});
						}

						@Override
						public void onError(String error) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									hideProgressDialog();
									finish();
									App.toast("孩子信息修改成功,但是修改头像可能失败。");
								}
							});
						}
					});
				} else {
					hideProgressDialog();
					finish();
					App.toast("孩子信息修改成功");
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
					TimePickerView dialog = initTimePicker();
					dialog.show();
				break;
			case R.id.ll_babySex:
				dailogB.show();
				break;
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

//	private DatePickerDialog datePicker;
//
//	private Calendar c;
//	private int _year, _month, _day;
//	private String times;
//
//	private Dialog onCreateDialog() {
//		DatePickerDialog d = new DatePickerDialog(this, new OnDateSetListener() {
//			@Override
//			public void onDateSet(DatePicker view, int year, int monthOfYear,
//								  int dayOfMonth) {
//				_year = year;
//				_month = monthOfYear;
//				_day = dayOfMonth;
//				times = _year + "-" + (_month + 1) + "-" + _day;
//
//				// if(year>)
//				int day = Util.comparisonTime(times,
//						Util.getCurrentDateString());
//				if (day < 0) {
//					dialogToast("出生日期不能是未来时间");
//					tv_birthday.setText(Util.getCurrentDateString());
//					return;
//				}
//				tv_birthday.setText(Util.setDate(times));
//				tv_save.setEnabled(true);
//			}
//		}, _year, _month, _day);
//
//		d.getDatePicker().setMaxDate(new Date().getTime());
////		d.getDatePicker().setMinDate(minDate);
//
//		return d;
//	}


	private TimePickerView initTimePicker()  {//Dialog 模式下，在底部弹出

		TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date, View v) {
				//Toast.makeText(EditChildAc.this, getTime(date), Toast.LENGTH_SHORT).show();
				String times = getTime(date);

				// if(year>)
				int day = Util.comparisonTime(times,
						Util.getCurrentDateString());
				if (day < 0) {
					dialogToast("出生日期不能是未来时间");
					tv_birthday.setText(Util.getCurrentDateString());
					return;
				}
				tv_birthday.setText(times);
				tv_save.setEnabled(true);
			}
		}).build();
		try{
			String dates = tv_birthday.getText().toString();
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dates);
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			pvTime.setDate(cal);
		}catch (Exception e){

		}

		return   pvTime;
	}

	private String getTime(Date date) {//可根据需要自行截取数据显示
		Log.d("getTime()", "choice date millis: " + date.getTime());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}


	@Override
	protected void onPhotoListTaked(List<String> photoPath) {
		// TODO Auto-generated method stub
		
	}
}
