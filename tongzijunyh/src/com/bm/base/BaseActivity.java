package com.bm.base;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.dialog.ToastDialog;
import com.bm.dialog.UtilDialog;
import com.bm.entity.User;
import com.bm.share.ShareUtil;
import com.bm.tzj.mine.LoginAc;
import com.bm.util.ProDialoging;
import com.richer.tzj.R;

/**
 * Activity基类
 *
 */
@SuppressLint("NewApi")
public class BaseActivity extends Activity {

	public TextView tv_center, tv_right, tv_right_share, tv_right_fav,
			tv_right_share_left;
	private LinearLayout ll_content,ll_nodata;
	public ImageButton ib_left, ib_right;
	public RelativeLayout rl_top;
	public LinearLayout ll_right;
	protected View contentView;
	// public static View shareView;
	public String shareText;
	protected Activity context;
	public View line;
	public ProDialoging progressDialog;
	private ToastDialog toastDialog;
	public ShareUtil share;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		progressDialog = new ProDialoging(this);
		toastDialog = new ToastDialog(this);
		setContentView(R.layout.ac_base);
		context = this;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			View decor = window.getDecorView();
			decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int ui = decor.getSystemUiVisibility();
//			if (lightStatusBar) {
				ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//			} else {
//				ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//			}
				decor.setSystemUiVisibility(ui);
			}

			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
		}
		
		
		initView();
		// initShareView();
		share = new ShareUtil(this);
		registerReceiver(logout, new IntentFilter("logout"));
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	}

	BroadcastReceiver logout = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			finish();
		}
	};

	public ImageView findImageViewById(int resId) {
		return (ImageView) findViewById(resId);
	}

	public TextView findTextViewById(int resId) {
		return (TextView) findViewById(resId);
	}

	public ListView findListViewById(int resId) {
		return (ListView) findViewById(resId);
	}

	public RelativeLayout findRelativeLayoutById(int resId) {
		return (RelativeLayout) findViewById(resId);
	}

	public LinearLayout findLinearLayoutById(int resId) {
		return (LinearLayout) findViewById(resId);
	}

	public ScrollView findScrollViewById(int resId) {
		return (ScrollView) findViewById(resId);
	}

	public EditText findEditTextById(int resId) {
		return (EditText) findViewById(resId);
	}

	public Button findButtonById(int resId) {
		return (Button) findViewById(resId);
	}

	private void initView() {
		line = findViewById(R.id.line);
		ib_left = (ImageButton) findViewById(R.id.ib_left);
		ib_right = (ImageButton) findViewById(R.id.ib_right);
		tv_center = (TextView) findViewById(R.id.tv_center);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right_share = (TextView) findViewById(R.id.tv_right_share);
		tv_right_fav = (TextView) findViewById(R.id.tv_right_fav);
		tv_right_share_left = findTextViewById(R.id.tv_right_share_left);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_nodata =(LinearLayout) findViewById(R.id.ll_nodata);
		ll_right = (LinearLayout) findViewById(R.id.ll_right);

		rl_top = (RelativeLayout) findViewById(R.id.rl_top);

		ib_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				clickLeft();
			}
		});

		tv_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				clickRight();
			}
		});
		ib_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ibClickRight();
			}
		});
		
		rl_top.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (BaseActivity.this.getCurrentFocus() != null) {
					if (BaseActivity.this.getCurrentFocus().getWindowToken() != null) {
					 //调用系统自带的隐藏软键盘
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
					}
				
			}
				return false;
			}
		});
	}

	public boolean isLogin() {
		User user =   App.getInstance().getUser();
		if (user==null){
			UtilDialog.dialogToLogin(context,
					"您还未登录，请先登录在操作",
					"取消",
					"确定",
					"提示",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							int tag = (int) v.getTag();
							if (tag==1){
								finish();
								Intent intent = new Intent(BaseActivity.this, LoginAc.class);
								startActivity(intent);
							}
						}
					});
			return false;
		}
		return true;
	}

	/**
	 * 加入页面内容布局
	 * 
	 */
	protected void contentView(int layoutId) {
		contentView = getLayoutInflater().inflate(layoutId, null);
		if (ll_content.getChildCount() > 0) {
			ll_content.removeAllViews();
		}
		if (contentView != null) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			ll_content.addView(contentView, params);
		}
	}

	/**
	 * 
	 * 左边按钮点击事件
	 */
	public void clickLeft() {
		finish();
	}

	/**
	 * 
	 * 右边按钮点击事件
	 */
	public void clickRight() {
	}

	/**
	 * 
	 * 右边第二个按钮点击事件
	 */
	public void ibClickRight() {

	}

	/**
	 * 
	 * 右边有两个按钮的时候设置
	 * 
	 * @param imgId
	 */
	public void setIbRightImg(int imgId) {
		ib_right.setImageResource(imgId);
		ib_right.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 设置右边按钮图片
	 * 
	 * @param imgId
	 */
	public void setRightImg(int imgId) {
		tv_right.setBackgroundResource(imgId);
		tv_right.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 设置右边按钮名字
	 * 
	 * @param name
	 */
	public void setRightName(String name) {
		Log.i("BaseActivity", "tv_right----------- setRight>>>>");
		tv_right.setText(name);
		tv_right.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置标题栏名称
	 * 
	 * @param title
	 */
	public void setTitleName(String title) {
		tv_center.setText(title);
	}

	public void hideTopBar() {
		rl_top.setVisibility(View.GONE);
	}

	public void showRightLinear(int imgShare, int imgfav) {

		if (ll_right.getVisibility() == View.GONE) {

			ll_right.setVisibility(View.VISIBLE);
		}
		tv_right_share.setBackgroundResource(imgShare);
		tv_right_share.setVisibility(View.VISIBLE);
		tv_right_fav.setBackgroundResource(imgfav);
		tv_right_fav.setVisibility(View.VISIBLE);
		// tv_right_fav tv_right_share
	}

	/**
	 * 收藏
	 * 
	 * @param imgShare
	 */
	public void showRightLinear(int imgShare) {

		if (ll_right.getVisibility() == View.GONE) {

			ll_right.setVisibility(View.VISIBLE);
		}
		tv_right_share.setBackgroundResource(imgShare);
		tv_right_share.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置分享左边的图标
	 * @param imgSrc
	 */
	public void showRightShareLeft(int imgSrc) {
		if (ll_right.getVisibility() == View.GONE) {
			ll_right.setVisibility(View.VISIBLE);
		}
		tv_right_share_left.setBackgroundResource(imgSrc);
		tv_right_share_left.setVisibility(View.VISIBLE);
	}

	/** * 打开activity * @param ActivityClass */
	public void openActivity(Class<? extends Activity> ActivityClass) {
		Intent intent = new Intent(this, ActivityClass);
		startActivity(intent);
	}

	/** * 打开activity * @param ActivityClass */
	public void openActivity(Class<? extends Activity> ActivityClass, Bundle b) {
		Intent intent = new Intent(this, ActivityClass);
		intent.putExtras(b);
		startActivity(intent);
	}

	/**
	 *   * 判断值是否为空   * @param arg   * @return  
	 */
	public static String getNullData(String arg) {
		return arg == null ? "" : arg;
	}
	public static String getNullDataFor0(String arg) {
		return arg == null ? "0" : arg;
	}
	/**
	 * 判断值是否为空
	 * @param arg
	 * @return
	 */
	public static int getNullIntData(String arg) {
		if(TextUtils.isEmpty(arg)){
			return 0;
		}else {
			return Integer.parseInt(arg);
		}
	}

	public void showProgressDialog() {
		if(progressDialog!= null){
			if(this.isFinishing()){
				 progressDialog.dismiss();
			}else{
				progressDialog.show();
			}
			
		}
	}

	public void hideProgressDialog() {
		progressDialog.hide();
	}

	public void toast(String msg) {
		App.toast(msg);
	}

	/**
	 * 隐藏左边按钮
	 */
	public void hideLeft() {
		ib_left.setVisibility(View.GONE);
	}

	/**
	 * 提示对话框toast
	 * 
	 * @param msg
	 */
	public void dialogToast(String msg) {
		// App.dialogToast(this, msg, 2000);
		if (!isFinishing())
		toastDialog.show(msg, 2000);
	}

	/**
	 * 提示对话框toast
	 * 
	 * @param msg
	 * @param ms
	 *            显示时长
	 */
	public void dialogToast(String msg, int ms) {
		toastDialog.show(msg, ms);
	}

	public void dialogToastHandler(String msg, int ms, Activity ac) {
		toastDialog.showHandler(msg, ms, ac);
	}
	
	/**
	 * 判断是否有数据
	 */
	public void isHaveData(boolean flag){
		if(flag){
			ll_content.setVisibility(View.VISIBLE);
			ll_nodata.setVisibility(View.GONE);
		}else{
			ll_content.setVisibility(View.GONE);
			ll_nodata.setVisibility(View.VISIBLE);
		}
	}
	
	 /**
	   * 保留小数
	   * @return
	   */
	  public static String getFormat(String strArg,int digit){
		    BigDecimal bd = new BigDecimal(strArg);
			bd = bd.setScale(digit,BigDecimal.ROUND_HALF_UP); 
			return bd.toString();
	  }

	@Override
	protected void onDestroy() {
	  	if (progressDialog!=null)
		progressDialog.dismiss();
		if (toastDialog.isShowing()) {
			toastDialog.dismiss();
		}
		unregisterReceiver(logout);
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
		return super.onTouchEvent(event);
	}
}
