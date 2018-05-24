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
 * 我的课程
 * 
 * @author wanghy
 * 
 */
public class MyCourseAc extends BaseActivity implements OnClickListener {
	private Context context;

	private TextView tv_total, tv_notStart, tv_underway, tv_end, tv_payments;
	private View v_1, v_2, v_3, v_4, v_5;

	private int tv_titleId[] = { R.id.tv_total, R.id.tv_notStart,
			R.id.tv_underway, R.id.tv_end, R.id.tv_payments };
	private TextView tv_title[] = new TextView[5];
	private ViewPager vPager;
	private List<MyCourseListFrameLayout> dataList = new ArrayList<MyCourseListFrameLayout>();
	public static MyCourseAc intance = null;
	
	public static final String INTENTKEY_INDEX = "INTENTKEY_INDEX";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mycourse);
		context = this;
		setTitleName("我的课程");
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
		v_1.setVisibility(View.GONE);
		v_2.setVisibility(View.GONE);
		v_3.setVisibility(View.GONE);
		v_4.setVisibility(View.GONE);
		v_5.setVisibility(View.GONE);

		tv_total.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_notStart.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_underway.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_end.setTextColor(getResources().getColor(R.color.course_bg_textColor));
		tv_payments.setTextColor(getResources().getColor(R.color.course_bg_textColor));
	}

	private void initView() {
		tv_total = findTextViewById(R.id.tv_total);
		tv_notStart = findTextViewById(R.id.tv_notStart);
		tv_underway = findTextViewById(R.id.tv_underway);
		tv_end = findTextViewById(R.id.tv_end);
		tv_payments = findTextViewById(R.id.tv_payments);

		v_1 = findViewById(R.id.v_1);
		v_2 = findViewById(R.id.v_2);
		v_3 = findViewById(R.id.v_3);
		v_4 = findViewById(R.id.v_4);
		v_5 = findViewById(R.id.v_5);

		tv_total.setOnClickListener(this);
		tv_notStart.setOnClickListener(this);
		tv_underway.setOnClickListener(this);
		tv_end.setOnClickListener(this);
		tv_payments.setOnClickListener(this);

		vPager = (ViewPager) findViewById(R.id.vPager);
		for (int i = 0; i < tv_title.length; i++) {
			tv_title[i] = (TextView) findViewById(tv_titleId[i]);
		}

		dataList.add(new MyCourseListFrameLayout(context, "0")); // 全部
		dataList.add(new MyCourseListFrameLayout(context, "1")); // 未开始
		dataList.add(new MyCourseListFrameLayout(context, "2")); // 进行中
		dataList.add(new MyCourseListFrameLayout(context, "3")); // 已结束
		dataList.add(new MyCourseListFrameLayout(context, "4")); // 待付款
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
		//		if (oldSelect == Index) {
		//			return;
		//		}
		// oldSelect = Index;
		// for (int i = 0; i < tv_title.length; i++) {
		// if (Index == i) {
		// tv_title[i].setTextColor(getResources().getColor(R.color.white));
		// tv_title[i].setBackgroundColor(getResources().getColor(R.color.app_dominantHue));
		// } else {
		// tv_title[i].setTextColor(getResources().getColor(R.color.course_bg_textColor));
		// tv_title[i].setBackgroundColor(getResources().getColor(R.color.course_bg_title));
		// }
		// }
		clearState();
		switch (Index) {
		case 0:
			v_1.setVisibility(View.VISIBLE);
			tv_total.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case 1:
			v_2.setVisibility(View.VISIBLE);
			tv_notStart.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case 2:
			v_3.setVisibility(View.VISIBLE);
			tv_underway.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case 3:
			v_4.setVisibility(View.VISIBLE);
			tv_end.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		case 4:
			v_5.setVisibility(View.VISIBLE);
			tv_payments.setTextColor(getResources().getColor(R.color.app_dominantHue));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		clearState();
		switch (view.getId()) {
		case R.id.tv_total:
			selectTieleChange(0);
			vPager.setCurrentItem(0);
			break;
		case R.id.tv_notStart:
			selectTieleChange(1);
			vPager.setCurrentItem(1);
			break;
		case R.id.tv_underway:
			selectTieleChange(2);
			vPager.setCurrentItem(2);
			break;
		case R.id.tv_end:
			selectTieleChange(3);
			vPager.setCurrentItem(3);
			break;
		case R.id.tv_payments:
			selectTieleChange(4);
			vPager.setCurrentItem(4);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(null != dataList.get(0)){
			for (int i = 0; i < dataList.size(); i++) {
				dataList.get(i).reFresh();
			}
		}
		
	}

}
