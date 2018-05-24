package com.bm.tzj.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.base.BaseActivity;
import com.richer.tzj.R;

/**
 * 优惠券
 * 
 */
public class MyYouhuiquanAc extends BaseActivity implements OnClickListener {
	private Context context;

	private TextView tv_10, tv_20, tv_30, tv_90;

	private int tv_titleId[] = { R.id.tv_total, R.id.tv_notStart,
			R.id.tv_underway, R.id.tv_end, R.id.tv_payments };
	private TextView tv_title[] = new TextView[5];
	private ViewPager vPager;
	private List<MyYouhuiquanListFrameLayout> dataList = new ArrayList<MyYouhuiquanListFrameLayout>();
	public static MyYouhuiquanAc intance = null;
	
	public static final String INTENTKEY_INDEX = "INTENTKEY_INDEX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_myyouhuiquan);
		context = this;
		setTitleName("优惠券");
		initView();
		intance = this;
		
		//初始化进入分类tab
		int index = this.getIntent().getIntExtra(INTENTKEY_INDEX, 0);
		selectTieleChange(index);
		vPager.setCurrentItem(index);
	}

	/**
	 * 清除状态
	 */
	private void clearState() {

		tv_10.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_20.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_30.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_90.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_10.setBackgroundColor(0xffF1EAED);
		tv_20.setBackgroundColor(0xffF1EAED);
		tv_30.setBackgroundColor(0xffF1EAED);
		tv_90.setBackgroundColor(0xffF1EAED);
	}

	private void initView() {
		tv_10 = findTextViewById(R.id.tv_10);
		tv_20 = findTextViewById(R.id.tv_20);
		tv_30 = findTextViewById(R.id.tv_30);
		tv_90 = findTextViewById(R.id.tv_90);


		tv_10.setOnClickListener(this);
		tv_20.setOnClickListener(this);
		tv_30.setOnClickListener(this);
		tv_90.setOnClickListener(this);

		vPager = (ViewPager) findViewById(R.id.vPager);
		for (int i = 0; i < tv_title.length; i++) {
			tv_title[i] = (TextView) findViewById(tv_titleId[i]);
		}

		dataList.add(new MyYouhuiquanListFrameLayout(context, "10")); // 未领取
		dataList.add(new MyYouhuiquanListFrameLayout(context, "20")); // 已领取
		dataList.add(new MyYouhuiquanListFrameLayout(context, "30")); // 已结束
		dataList.add(new MyYouhuiquanListFrameLayout(context, "90")); // 已过期
		vPager.setAdapter(pagerAdapter);

		vPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				selectTieleChange(arg0);
				dataList.get(arg0).reFresh();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	
	

	private PagerAdapter pagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(dataList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(dataList.get(position));
			return dataList.get(position);
		}

	};

//	private int oldSelect = 0;

	private void selectTieleChange(int Index) {
		clearState();
		switch (Index) {
		case 0:
			tv_10.setTextColor(getResources().getColor(R.color.white));
			tv_10.setBackgroundResource(R.drawable.kc_shape);
			break;
		case 1:
			tv_20.setTextColor(getResources().getColor(R.color.white));
			tv_20.setBackgroundResource(R.drawable.kc_shape);
			break;
		case 2:
			tv_30.setTextColor(getResources().getColor(R.color.white));
			tv_30.setBackgroundResource(R.drawable.kc_shape);
			break;
		case 3:
			tv_90.setTextColor(getResources().getColor(R.color.white));
			tv_90.setBackgroundResource(R.drawable.kc_shape);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		clearState();
		switch (view.getId()) {
		case R.id.tv_10:
			selectTieleChange(0);
			vPager.setCurrentItem(0);
			break;
		case R.id.tv_20:
			selectTieleChange(1);
			vPager.setCurrentItem(1);
			break;
		case R.id.tv_30:
			selectTieleChange(2);
			vPager.setCurrentItem(2);
			break;
		case R.id.tv_90:
			selectTieleChange(3);
			vPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(null != dataList.get(0)){
			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).reFresh();
			}
		}
		
	}

}
