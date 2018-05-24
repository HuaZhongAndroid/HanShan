package com.bm.tzj.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bm.base.BaseActivity;
import com.richer.tzj.R;

/**
 * 无教练
 * 
 * @author jiangsh
 * 
 */
public class NoTeacherAc extends BaseActivity implements OnClickListener {
	private Context context;
	private TextView tv_submit;
	
	public static NoTeacherAc intance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_no_teacher);
		context = this;
		setTitleName("我的顾问教练");
		intance = this;
		initView();
	}

	private void initView() {
		tv_submit = findTextViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
		setData();
	}

	private void setData() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:// 我要绑定
			Intent intent = new Intent(context, MyTeachersAc.class);
			intent.putExtra("pageType", "NoTeacherAc");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
