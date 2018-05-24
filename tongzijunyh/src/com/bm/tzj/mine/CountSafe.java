package com.bm.tzj.mine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.UtilDialog;
import com.bm.entity.User;
import com.bm.tzj.city.City;
import com.bm.util.BDLocationHelper;
import com.bm.util.MD5Util;
import com.google.gson.Gson;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

public class CountSafe extends BaseActivity implements OnClickListener{
	private TextView tv_unbind;
	private TextView tv_change_phone;
	private LinearLayout ll_d;
	private User user;
	private TextView phone_num;
	private TextView weichat_name;
	private TextView tv_tobind;
	private UMShareAPI mShareAPI = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_count_safe);
		setTitleName("账户安全");
		context=this;
		user = App.getInstance().getUser();
		mShareAPI = UMShareAPI.get( this );
		initView();
	}


	private void initView(){
		tv_unbind =(TextView)findViewById(R.id.tv_unbind);
		tv_tobind =(TextView)findViewById(R.id.tv_tobind);

		if (TextUtils.isEmpty(user.wechatId)) {
			tv_unbind.setVisibility(View.GONE);
			tv_tobind.setVisibility(View.VISIBLE);
		}else{
			tv_unbind.setVisibility(View.VISIBLE);
			tv_tobind.setVisibility(View.GONE);
		}
		tv_change_phone =(TextView)findViewById(R.id.tv_change_phone);
		phone_num =(TextView)findViewById(R.id.phone_num);
		weichat_name =(TextView)findViewById(R.id.weichat_name);

		phone_num.setText(user.phone);
		weichat_name.setText(user.wechatName);

		ll_d =(LinearLayout)findViewById(R.id.ll_d);
		ll_d.setOnClickListener(this);
		tv_unbind.setOnClickListener(this);
		tv_tobind.setOnClickListener(this);
		tv_change_phone.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_unbind:
			UtilDialog.dialogZhouTwoBtnContentTtile(context, "解除关联后，使用此微信登录将无法查看当前账号内容", "取消", "确定", "确定解除关联吗？",handler, 1);
			break;
		case R.id.tv_tobind:
			SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
			showProgressDialog();
			mShareAPI.doOauthVerify(CountSafe.this, platform, umAuthListener);
			break;
		case R.id.tv_change_phone:
			Intent intent2 = new Intent(context,ChangePhoneAc.class);
			startActivity(intent2);
			break;
		case R.id.ll_d:
			Intent intent = new Intent(context, PwdSetAc.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}





	private void binWeichatTo(String wechatJson) {
		HashMap<String, String> map = new HashMap<String, String>();
		showProgressDialog();

		map.put("userId", user.userid);
		map.put("wechatJson", wechatJson);

		UserManager.getInstance().bindWeichatTO(context, map, new ServiceCallback<CommonResult<User>>() {

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}

			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if (obj != null && obj.data != null) {
					SharedPreferencesHelper.saveString("userid", obj.data.userid);//保存用户ID
					App.getInstance().setUser(obj.data);// 存储用户信息
					if(obj.data.babyList!=null && obj.data.babyList.size()>0)
						App.getInstance().setChild(obj.data.babyList.get(obj.data.babyList.size()-1));
					Set<String> tags = new HashSet<String>();
					tags.add(obj.data.phone);
					tags.add(obj.data.pushid);
					String city = BDLocationHelper.getCacheLocation().city;
					city = city.replace("市", "");
					city = MD5Util.md5(city);//MD5
					tags.add(city);
					JPushInterface.setTags(context, tags, new TagAliasCallback(){
						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							Log.d("JPushInterface.setTags:"+arg0);
						}});
					tv_unbind.setVisibility(View.VISIBLE);
					tv_tobind.setVisibility(View.GONE);

					weichat_name.setText(obj.data.wechatName);
				}

			}
		});

	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1://解除微信绑定
				unbinWeichat();
				break;
			case 4://清除本地缓存
				break;
			}
		};
	};



	private void unbinWeichat() {
		HashMap<String, String> map = new HashMap<String, String>();
		showProgressDialog();

		map.put("userId", user.userid);

		UserManager.getInstance().unbindWeichat(context, map, new ServiceCallback<CommonResult<User>>() {

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}

			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if (obj != null && obj.data != null) {
					SharedPreferencesHelper.saveString("userid", obj.data.userid);//保存用户ID
					App.getInstance().setUser(obj.data);// 存储用户信息
					if(obj.data.babyList!=null && obj.data.babyList.size()>0)
						App.getInstance().setChild(obj.data.babyList.get(obj.data.babyList.size()-1));
					Set<String> tags = new HashSet<String>();
					tags.add(obj.data.phone);
					tags.add(obj.data.pushid);
					String city = BDLocationHelper.getCacheLocation().city;
					city = city.replace("市", "");
					city = MD5Util.md5(city);//MD5
					tags.add(city);
					JPushInterface.setTags(context, tags, new TagAliasCallback(){
						@Override
						public void gotResult(int arg0, String arg1, Set<String> arg2) {
							Log.d("JPushInterface.setTags:"+arg0);
						}});
					tv_unbind.setVisibility(View.GONE);
					tv_tobind.setVisibility(View.VISIBLE);

					weichat_name.setText("");
				}

			}
		});

	}


	//授权
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			if (data!=null){
				mShareAPI.getPlatformInfo(CountSafe.this, platform, userInfoAuthListener);
				hideProgressDialog();
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			t.printStackTrace();
			hideProgressDialog();
			dialogToast("微信授权异常");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			hideProgressDialog();
			dialogToast("微信授权异常");
		}
	};

	//获取用户信息
	private UMAuthListener userInfoAuthListener = new UMAuthListener() {
		private City city;

		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			hideProgressDialog();
			if (data!=null){

				Gson gson = new Gson();
				String wechatJson = gson.toJson(data);
				binWeichatTo(wechatJson);

			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			t.printStackTrace();
			hideProgressDialog();
			dialogToast("微信授权异常");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			hideProgressDialog();
			dialogToast("微信授权异常");
		}
	};

	/**
	 * 获取用户信息
	 */
	public void getUserInfo() {

		//当登录后刷新进入首页
		User user = App.getInstance().getUser();
		HashMap<String, String> map = new HashMap<String, String>();

		if (user!= null) {
			if (null != user.userid) {
				map.put("userId", user.userid);
			}else {
				map.put("userId", SharedPreferencesHelper.getString("userid"));
			}

			UserManager.getInstance().getTzjcasGetUserInfo(context, map, new ServiceCallback<CommonResult<User>>() {

				@Override
				public void error(String msg) {
					App.toast(msg);
				}

				@Override
				public void done(int what, CommonResult<User> obj) {
					if (obj != null && obj.data != null) {
						App.getInstance().setUser(obj.data);// 存储用户信息
						phone_num.setText(obj.data.phone);
						weichat_name.setText(obj.data.wechatName);

					} else {
						dialogToast("获取数据失败");
						App.toast("获取数据失败");
					}

				}
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getUserInfo();
	}

}
