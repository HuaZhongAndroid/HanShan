package com.bm.tzj.mine;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.UtilDialog;
import com.bm.im.tool.DemoHelper;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.DisclaimerAc;
import com.bm.util.DataCleanManager;
import com.easemob.EMCallBack;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;
import com.umeng.socialize.utils.Log;

/**
 * 设置
 * 
 * @author jiangsh
 * 
 */
public class SettingAc extends BaseActivity implements OnClickListener {
	private Context context;
	private LinearLayout  ll_c, ll_d, ll_e,ll_f;
	private TextView tv_submit;
	
	public static SettingAc intance;
	final String INITIALIZED = "initialized";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_setting);
		context = this;
		setTitleName("设置");
		intance = this;
		initView();
	}

	private void initView() {
		line.setVisibility(View.GONE);
		ll_c = findLinearLayoutById(R.id.ll_c);
		ll_d = findLinearLayoutById(R.id.ll_d);
		ll_e = findLinearLayoutById(R.id.ll_e);
		ll_f=findLinearLayoutById(R.id.ll_f);
		tv_submit = findTextViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
		ll_c.setOnClickListener(this);
		ll_d.setOnClickListener(this);
		ll_e.setOnClickListener(this);
		ll_f.setOnClickListener(this);
//		setData();
	}

//	private void setData() {
//		User uInfo = App.getInstance().getUser();
//		if(null !=uInfo ){
//			if(null == uInfo.paymentPassword){ //默认没有设置支付密码
//				ll_f.setVisibility(View.VISIBLE);
//			}else{
//				if(uInfo.paymentPassword.equals("0")){//0 没有设置支付密码  1 设置支付密码
//					ll_f.setVisibility(View.VISIBLE);
//				}else{
//					ll_f.setVisibility(View.GONE);
//				}
//			}
//			
//		}
//		
//	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.tv_submit:// 退出当前用户
			UtilDialog.dialogTwoBtnContentTtile(context, "确定要退出登录么", "取消","确定","提示",handler,1);
			break;
		case R.id.ll_c:// 帮助中心
			intent = new Intent(context, DisclaimerAc.class);
			intent.putExtra("pageType", "HelpAc");
			startActivity(intent);
			break;
		case R.id.ll_d:// 关于软件
			intent = new Intent(context, DisclaimerAc.class);
			intent.putExtra("pageType", "AboutAc");
			startActivity(intent);
			break;
		case R.id.ll_e:// 清除本地缓存
			UtilDialog.dialogTwoBtnContentTtile(context, "确定要清除本地缓存", "取消","确定","提示",handler,4);
			break;
		case R.id.ll_f:// 账号安全
			intent = new Intent(context, CountSafe.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	/**
	 * 退出IM
	 */
	private void quitIM() {
		MainAc.intance.showProgressDialog();
		
		DemoHelper.getInstance().logout(false, new EMCallBack() {
			@Override
			public void onSuccess() {
				MainAc.intance.runOnUiThread(new Runnable() {
					public void run() {
						// 重新显示登陆页面
						// startActivity(new Intent(MainActivity.this,
						// LoginActivity.class));
						MainAc.intance.finish();
						finish();
						App.getInstance().setUser(null);
						App.getInstance().setChild(null);
						
						//jpush清理tag
						JPushInterface.setTags(context, new HashSet<String>(), new TagAliasCallback(){
							@Override
							public void gotResult(int arg0, String arg1, Set<String> arg2) {
								Log.d("清理tag -- JPushInterface.setTags:"+arg0);
							}});
//						SharedPreferencesHelper.saveBoolean("isCheck", false);// 清空记住密码的信息
//						SharedPreferencesHelper.saveString("phone", "");
//						SharedPreferencesHelper.saveString("password", "");
//						SharedPreferencesHelper.saveString("userid", "");//保存用户ID
						SharedPreferencesHelper.saveBoolean(INITIALIZED, false);
						App.getInstance().setUser(null);
//						getActivity().stopService(new Intent(context, SingleSignOnServer.class));
						startActivity(new Intent(context, LoginAc.class));
						MainAc.intance.hideProgressDialog();
//						App.getInstance().saveCityCode(null);
					}
				});
			}
			@Override
			public void onProgress(int progress, String status) {
			}
			@Override
			public void onError(int code, String message) {
				MainAc.intance.hideProgressDialog();
				MainAc.intance.dialogToast("退出失败");
			}
		});
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://退出登录
				quitIM();
				break;
			case 4://清除本地缓存
				MainAc.intance.showProgressDialog();
				DataCleanManager.cleanInternalCache(context);
				MainAc.intance.hideProgressDialog();
				break;
			}
		};
	};

}
