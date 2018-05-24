package com.bm.tzj.mine;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.bm.base.BaseFragmentActivity;
import com.bm.tzj.fm.PullulateFm;
import com.richer.tzj.R;

public class MyMedal extends BaseFragmentActivity {
	private PullulateFm pullulateFm;
	private FragmentManager fragmentManager;
	private FrameLayout content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_medal);
		setTitleName("我的勋章");
		initView();

	}


	private void initView() {
		content = (FrameLayout)findViewById(R.id.content);
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		pullulateFm = new PullulateFm();
		transaction.add(R.id.content, pullulateFm);
		transaction.commit();
		ib_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}



}
