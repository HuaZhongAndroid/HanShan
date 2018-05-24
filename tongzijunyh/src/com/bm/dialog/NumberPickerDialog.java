package com.bm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.lib.widget.CustomNumberPicker;
import com.richer.tzj.R;

/**
 * 
 * 
 * number PickDialo
 * @author wangqiang 2014-8-13
 * 
 */
public class NumberPickerDialog extends Dialog {

	private Context context;
	SelectValue selectFace;
	private Button btn_cancel;
	private Button btn_confirm;
	private TextView tv;
	private CustomNumberPicker np_picker;
	private String[] dataArray;
	private String tag;

	public NumberPickerDialog(Context context, String[] dataArray,String tag,SelectValue selectFace,CancleClick cancleClick) {
		super(context, R.style.MyDialog);

		this.context = context;
		this.tag = tag;
		this.selectFace = selectFace;
		this.dataArray = dataArray;
		this.cancleClick = cancleClick;
		getWindow().getAttributes().windowAnimations = R.style.SlideUpDialogAnimation;
		
		
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.gravity = Gravity.BOTTOM | Gravity.CENTER;
		getWindow().setAttributes(wl);

		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_screening_view);
		// 设置视图宽度为屏幕宽度
		View root = findViewById(R.id.root);
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) root
				.getLayoutParams();
		lp.width = getContext().getResources().getDisplayMetrics().widthPixels;
		root.setLayoutParams(lp);

		np_picker = (CustomNumberPicker) findViewById(R.id.np_picker);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		tv=(TextView) findViewById(R.id.tv);
		tv.setText(tag);
		
		np_picker.setDisplayedValues(dataArray);
		np_picker.setMinValue(0);
		np_picker.setMaxValue(dataArray.length - 1);
		np_picker.setValue(0);
		np_picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		
		
		np_picker.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker arg0, int arg1, int newVal) {
				// TODO Auto-generated method stub
				selectFace.getValue(newVal);
				

			}
		});
		
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Log.e("tag111", arg0 + "");
				cancleClick.click(arg0);
			}
		});

		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cancleClick.clickConform(arg0);
			}
		});

	}

	
	
	

	
	CancleClick cancleClick;
	
	public interface CancleClick{
		public void click(View arg);
		public void clickConform(View arg);
	}

	public interface SelectValue {
		public void getValue(int arg);
	}
	public interface SetName{
		public void setName(TextView tv);
	}

}