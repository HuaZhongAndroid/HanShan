package com.bm.tzj.mine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
import com.bm.util.BDLocationHelper;
import com.bm.util.MD5Util;
import com.bm.util.Util;
import com.google.gson.Gson;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

public class BindWeichat extends BaseActivity{
	private UMShareAPI mShareAPI = null;
	private String strPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.ac_bind_weichat);
		contentView(R.layout.ac_bind_weichat);
		this.findViewById(R.id.rl_top).setVisibility(View.GONE);
		mShareAPI = UMShareAPI.get( this );
		strPhone = getIntent().getStringExtra("phone");
		findViewById(R.id.ib_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BindWeichat.this.finish();
			}
		});

		findViewById(R.id.tv_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
				showProgressDialog();
				mShareAPI.doOauthVerify(BindWeichat.this, platform, umAuthListener);
			}
		});
	}

	//授权
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			if (data!=null){
				mShareAPI.getPlatformInfo(BindWeichat.this, platform, userInfoAuthListener);
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
				String openId = gson.toJson(data);

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phone", strPhone);
				map.put("wechatJson", openId);
				map.put("deviceToken", Util.getIMEI(context));
				city = App.getInstance().getCityCode();
				BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();
				map.put("longitude", info.lng+"");
				map.put("latitude", info.lat+"");
				showProgressDialog();
				UserManager.getInstance().bindWeichat(context, map, new ServiceCallback<CommonResult<User>>() {

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

							if(null != MainAc.intance){
								MainAc.intance.finish();
							}
							Intent intent = new Intent(context, MainAc.class);
							intent.putExtra("tag", 0);
							startActivity(intent);
							finish();
						}

					}
				});

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
}
