package com.bm.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
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

import com.bm.dialog.ToastDialog;
import com.bm.util.ProDialoging;
import com.richer.tzj.R;

/**
 * Activity基类
 *
 */
public class BaseFragmentActivity extends FragmentActivity {

	public static TextView tv_center, tv_right, tv_right_share, tv_right_fav;
	private LinearLayout ll_content;
	public static RelativeLayout rl_top;
	public static LinearLayout ll_right;
	protected View contentView;
	public View line;
	// public static View shareView;
	public static String shareText;
	public ImageButton ib_left;
	private Context context;
	public ProDialoging progressDialog;
	private ToastDialog toastDialog;
//	private ProgressDialog progressDialo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_base);

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

		progressDialog = new ProDialoging(this);
//		progressDialo = new ProgressDialog(this);
		toastDialog = new ToastDialog(this);
		context = this;
		initView();
		registerReceiver(logout, new IntentFilter("logout"));
		// initShareView();
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
	public void showProgressDialog() {
		try
		{
			progressDialog.show();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void hideProgressDialog() {
		if(progressDialog.isShowing()){
			progressDialog.cancel();
		}
//		progressDialo.hide();
	}
	private void initView() {
		ib_left = (ImageButton) findViewById(R.id.ib_left);
		tv_center = (TextView) findViewById(R.id.tv_center);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right_share = (TextView) findViewById(R.id.tv_right_share);
		tv_right_fav = (TextView) findViewById(R.id.tv_right_fav);
		line = findViewById(R.id.line);
		
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
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
	}

	/**
	 * 加入页面内容布局
	 * 
	 * @param view
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

	public void clickLeft() {
	}

	public void clickRight() {
	}

	public void setRightImg(int imgId) {
		tv_right.setBackgroundResource(imgId);
		tv_right.setVisibility(View.VISIBLE);
	}

	public void setRightName(String name) {
		tv_right.setText(name);
		tv_right.setVisibility(View.VISIBLE);
	}

	public void setTitleName(String title) {
		tv_center.setText(title);
	}

	public void showRightLinear(int imgShare, int imgfav) {

		if (ll_right.getVisibility() == View.GONE) {

			ll_right.setVisibility(View.VISIBLE);
		}
		tv_right_share.setBackgroundResource(imgShare);
		tv_right_fav.setBackgroundResource(imgfav);
		// tv_right_fav tv_right_share
	}
	
	/**
	 * 隐藏左边按钮
	 */
	public void hideLeft() {
		ib_left.setVisibility(View.GONE);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
//		EaseUI.getInstance().getNotifier().reset();
	}
	
	
	/**
	 * 设置上面背景名称
	 * 
	 * @param title
	 */
	
	public void setTopBg(int bg) {
		rl_top.setBackgroundResource(bg);;
	}

	public void hideTopBar() {
		rl_top.setVisibility(View.GONE);
	}
	@Override
	protected void onDestroy() {
		progressDialog.dismiss();
		unregisterReceiver(logout);
		super.onDestroy();
	}
	
	/**
	 * 提示对话框toast
	 * 
	 * @param msg
	 */
	public void dialogToast(String msg) {
		// App.dialogToast(this, msg, 2000);
		try {
			toastDialog.show(msg, 2000);
		} catch (Exception e) {
			// TODO: handle exception
			//Log.e("error", "You Activity is not running! "+msg);
		}
	}
//	public void postShowProgressDialog(final String msg){
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				progressDialo.show(msg);
//			}
//		});
//	}
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
}
