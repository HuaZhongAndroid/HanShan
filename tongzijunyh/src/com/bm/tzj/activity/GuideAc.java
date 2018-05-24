package com.bm.tzj.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.app.App;
import com.bm.base.ViewPagerAdapter;
import com.bm.tzj.mine.LoginAc;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;


/**
 * 引导页
 * @author wanghy
 *
 */
public class GuideAc extends Activity implements OnClickListener, OnPageChangeListener{

	private ViewPager vp;  
	private ViewPagerAdapter vpAdapter;  
	private List<View> views; 
	private Button btn_dian;
	private WindowManager manager;
	//引导图片资源  
	private static final int[] pics = { R.drawable.page_a,  
		R.drawable.page_b, R.drawable.page_c, R.drawable.page_d  }; 


	private Context context;

	//记录当前选中位置  
	private int currentIndex;  
	final String INITIALIZED = "initialized";
	private String someString;

	/** Called when the activity is first created. */  
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.ac_grides);  
		this.context = this;
		initView();
		//         strPageType = getIntent().getStringExtra("pageType");
		//         if(strPageType.equals("StartAc")){
		//        	 initView();  
		//         }else{
		//        	 initSecond();
		//         }

	}  

	public void initView(){
		boolean hasPreferences = SharedPreferencesHelper.getBoolean(INITIALIZED);
		if(hasPreferences) {
			
			if (App.getInstance().getUser() != null) {
				Intent intent = new Intent(this,MainAc.class);
				startActivity(intent);
				finish();
			}else{
				Intent intent = new Intent(this,LoginAc.class);
				startActivity(intent);
				finish();
			}
		}else {
			initSecond();
			someString = "some default value";
		}
//		Editor editor = myPrefs.edit();
//
//		editor.putBoolean(INITIALIZED, true);
//		editor.putString("someString", someString);
//		// Write other values as desired
//		editor.commit();
	}


	private void initSecond(){
		views = new ArrayList<View>();  

		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,  
				LinearLayout.LayoutParams.WRAP_CONTENT);  

		//初始化引导图片列表  
		for(int i=0; i<pics.length; i++) {  
			ImageView iv = new ImageView(this);  
			iv.setLayoutParams(mParams);  
			iv.setBackgroundResource(pics[i]);
			views.add(iv);  
		}  
		vp = (ViewPager) findViewById(R.id.vp_guide);  
		//初始化Adapter  
		vpAdapter = new ViewPagerAdapter(views);  
		vp.setAdapter(vpAdapter); 
		vp.setPageMargin(0);
		//绑定回调  
		vp.setOnPageChangeListener(this);  
		btn_dian = (Button) findViewById(R.id.btn_dian);
		manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth() / 6 * 2;
		int height = manager.getDefaultDisplay().getHeight() / 35 * 3;
		btn_dian.setMinimumWidth(width);
		btn_dian.setMinimumHeight(height);
		btn_dian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferencesHelper.saveBoolean(INITIALIZED, true);
				Intent intent = new Intent(GuideAc.this,LoginAc.class);
				startActivity(intent);
				finish();
			}
		});

		btn_dian.setVisibility(View.GONE);
	}
	/** 
	 *设置当前的引导页  
	 */  
	private void setCurView(int position)  
	{  
		if (position < 0 || position >= pics.length) {  
			return;  
		}  

		vp.setCurrentItem(position);  
	}  

	/** 
	 *这只当前引导小点的选中  
	 */  
	private void setCurDot(int positon)  
	{  
		if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {  
			return;  
		}  

		/* dots[positon].setEnabled(false);  
         dots[currentIndex].setEnabled(true);  */

		currentIndex = positon;  
	}  

	//当滑动状态改变时调用  
	@Override  
	public void onPageScrollStateChanged(int arg0) {  
		// TODO Auto-generated method stub  

	}  

	//当当前页面被滑动时调用  
	@Override  
	public void onPageScrolled(int arg0, float arg1, int arg2) {  
		// TODO Auto-generated method stub  

	}  

	//当新的页面被选中时调用  
	@Override  
	public void onPageSelected(int arg0) {  
		//设置底部小点选中状态  
		setCurDot(arg0);  
		if(arg0 == 3){
			btn_dian.setVisibility(View.VISIBLE);
		}else{
			btn_dian.setVisibility(View.GONE);
		}
	}  

	@Override  
	public void onClick(View v) {  
		int position = (Integer)v.getTag();  
		setCurView(position);  
		setCurDot(position);  
	} 

}
