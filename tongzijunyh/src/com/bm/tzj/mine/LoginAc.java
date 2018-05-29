package com.bm.tzj.mine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
import com.bm.tzj.kc.DisclaimerAc;
import com.bm.util.BDLocationHelper;
import com.bm.util.GlobalPrams;
import com.bm.util.MD5Util;
import com.bm.util.TimeCount;
import com.bm.util.Util;
import com.google.gson.Gson;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

/**
 * 登录
 * @author shiyt
 *
 */
public class LoginAc extends BaseActivity implements OnClickListener {
	private Context context;
	private EditText et_phone,et_code;
	private TextView tv_login,tv_other, tv_verifcode;
	private ImageView img_checkbox;
	private boolean isChecked;
	private UMShareAPI mShareAPI = null;
	public static LoginAc intatce;
	int loginCount = 1;
	String strPhone="", strPassWord="";
	String typeFlag="";// 1,手机号码 2,微信

	TimeCount count;
	private ImageView imgtb_weichat;
	private City city;
	private final String NO_BIND_WECHAT = "NO_BIND_WECHAT";
	private final String NO_BIND_PHONE = "NO_BIND_PHONE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_login2);
		context = this;
		setTitleName("家长登录");
		setRightName("注册");
		rl_top.setVisibility(View.GONE);
		mShareAPI = UMShareAPI.get( this );
		intatce = this;
		hideLeft();
		initView();
	}

	@Override
	public void clickRight() {
		super.clickRight();
		Intent intent = new Intent(context, RegistreAc.class);
		intent.putExtra("pageType", "RegistreAc");
		intent.putExtra("loginType", "");
		startActivity(intent);
	}

	public void initView(){
		findViewById(R.id.tv_xieyi).setOnClickListener(this);
		et_phone=findEditTextById(R.id.et_phone);
		et_code=findEditTextById(R.id.et_code);
		tv_verifcode=findTextViewById(R.id.tv_verifcode);
		tv_login=findTextViewById(R.id.tv_login);
		tv_other=findTextViewById(R.id.tv_other);
		img_checkbox=findImageViewById(R.id.img_checkbox);
		imgtb_weichat =(ImageView)findViewById(R.id.imgtb_weichat);
		tv_verifcode.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		tv_other.setOnClickListener(this);
		imgtb_weichat.setOnClickListener(this);
		et_code.addTextChangedListener(textWatcher);
		isChecked = true;
		if (SharedPreferencesHelper.contain("phone")) {
			strPhone = SharedPreferencesHelper.getString("phone");
			et_phone.setText(strPhone);
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param phone 手机号码
	 *            
	 */
	public void getAuthCode(final String phone) {
		if(TextUtils.isEmpty(phone)){
			dialogToast("手机号码不能为空");
			return;
		}else{
			if (!Util.isMobileNO(phone)) {
				dialogToast("请输入正确的手机号码");
				return;
			} 
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		showProgressDialog();
		UserManager.getInstance().getTzjcasSendcode(context,map,new ServiceCallback<StringResult>() {

			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				count = new TimeCount(120000, 1000, tv_verifcode, et_phone,context);
				count.start();
				et_phone.setFocusable(false);
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
		});
	}


	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.tv_verifcode:
			//获取验证码
			getAuthCode(et_phone.getText().toString().trim());
			break;
		case R.id.tv_login:
			typeFlag="1";
			loginApp("");
			break;
		case R.id.tv_other:
			intent = new Intent(context, MainAc.class);
			//intent.putExtra("tag", 0);//游客登录
			startActivity(intent);
			//清空数据
			App.getInstance().setUser(null);
			SharedPreferencesHelper.saveBoolean("isCheck", false);// 清空记住密码的信息
			SharedPreferencesHelper.saveString("phone", "");
			SharedPreferencesHelper.saveString("password", "");
			SharedPreferencesHelper.saveString("userid", "");//保存用户ID

			finish();
			break;
		case R.id.imgtb_weichat:
			typeFlag="2";
			addAuth();
			break;
		case R.id.tv_xieyi:
			intent = new Intent(context, DisclaimerAc.class);
			intent.putExtra("pageType", "RegistrAc");
			startActivity(intent);
			break;
		default:
			break;
		}
	}


	/**
	 * 登录
	 */
	private void loginApp(final String openId) {
		final String strPhone = et_phone.getText().toString().replace(" ", "").trim();
		final String authCode = et_code.getText().toString().trim();

		HashMap<String, String> map = new HashMap<String, String>();
		if(typeFlag.equals("1")){//普通登录
			if (strPhone.length() == 0) {
				dialogToast("手机号码不能为空");
				return;
			} else {
				if (!Util.isMobileNO(strPhone)) {
					dialogToast("手机号码不正确");
					return;
				} 
			}

			if (authCode.length() == 0) {
				dialogToast("验证码不能为空");
				return;
			}
		}else{
			if(openId.length()>0){//第三方登录
				map.put("wechatJson", openId);
			}else{
				map.put("wechatJson", "");
			}
		}
		map.put("loginType", typeFlag);
		map.put("phone", strPhone);
		map.put("authCode", authCode);
		map.put("deviceToken", Util.getIMEI(context));
		city = App.getInstance().getCityCode();
		BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();
		map.put("longitude", info.lng+"");
		map.put("latitude", info.lat+"");
		showProgressDialog();
		UserManager.getInstance().getTzjcasLogin(context,map, new ServiceCallback<CommonResult<User>>() {

			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if (obj != null && obj.data != null) {

					if (TextUtils.isEmpty(obj.data.userid)) {//先判断userid，确定是否存在这个用户 
						if (typeFlag.equals("1")) {//手机登录,绑定微信去
							Intent intent = new Intent(LoginAc.this,BindWeichat.class);
							intent.putExtra("phone", strPhone);
							startActivity(intent);
						}else{//微信登录，绑定手机去
							Intent intent = new Intent(LoginAc.this,BindPhone.class);
							intent.putExtra(GlobalPrams.wechatJson, openId);
							startActivity(intent);	
						}

					}else{//老用户。
						if (TextUtils.isEmpty(obj.data.wechatId)) {//微信id为空，执行绑定微信操作
							Intent intent = new Intent(LoginAc.this,BindWeichat.class);
							intent.putExtra("phone", strPhone);
							startActivity(intent);
						}else{
							SharedPreferencesHelper.saveString("userid", obj.data.userid);//保存用户ID
							App.getInstance().setUser(obj.data);// 存储用户信息
							if(obj.data.babyList!=null && obj.data.babyList.size()>0)
								App.getInstance().setChild(obj.data.babyList.get(obj.data.babyList.size()-1));
							Set<String> tags = new HashSet<String>();
							tags.add(obj.data.phone);
							tags.add(obj.data.pushid);
							String city = BDLocationHelper.getCacheLocation().city;
							if(city == null)
								city = "";
							city = city.replace("市", "");
							city = MD5Util.md5(city);//MD5
							tags.add(city);
							JPushInterface.setTags(context, tags, new TagAliasCallback(){
								@Override
								public void gotResult(int arg0, String arg1, Set<String> arg2) {
									Log.d("JPushInterface.setTags:"+arg0);
								}});
							Intent intent = new Intent(LoginAc.this,MainAc.class);
							intent.putExtra("tag", 0);
							startActivity(intent);
							finish();

						}

					}

				} else {
					dialogToast("登录失败");
				}
			}
		});
	}

	/**
	 * 
	 */
	private  void addAuth(){
		SHARE_MEDIA platform = null;
		if (typeFlag.equals("3")){
			platform = SHARE_MEDIA.WEIXIN;
		}else  if (typeFlag.equals("2")){
			platform = SHARE_MEDIA.WEIXIN;
		}else if (typeFlag.equals("1")){
			platform = SHARE_MEDIA.QQ;
		}

		showProgressDialog();
		mShareAPI.doOauthVerify(LoginAc.this, platform, umAuthListener);
	}

	//授权
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			if (data!=null){
				mShareAPI.getPlatformInfo(LoginAc.this, platform, userInfoAuthListener);
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
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			hideProgressDialog();
			if (data!=null){
				if(!TextUtils.isEmpty(typeFlag)){
					if(typeFlag.equals("2")){
						System.out.println("微信返回所有数据:"+data.toString());
						Gson gson = new Gson();
						System.out.println("json格式微信数据"+gson.toJson(data));
						loginApp(gson.toJson(data));
					}
				}
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
	 * 取消授权
	 * @param type
	 */
	private  void deleteAuth(int type){

		SHARE_MEDIA platform = null;
		if (type == 2){
			platform = SHARE_MEDIA.SINA;
		}else if (type == 1){
			platform = SHARE_MEDIA.QQ;
		}else if (type == 3){
			platform = SHARE_MEDIA.WEIXIN;
		}
		hideProgressDialog();
		mShareAPI.deleteOauth(LoginAc.this, platform, umdelAuthListener);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		deleteAuth(1);
	}

	/** delauth callback interface**/
	private UMAuthListener umdelAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}
	private TextWatcher textWatcher = new TextWatcher() {  

		@Override  
		public void onTextChanged(CharSequence s, int start, int before,  
				int count) {  


		}  

		@Override  
		public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {  

		}  

		@Override  
		public void afterTextChanged(Editable s) {  
			if (TextUtils.isEmpty(et_code.getText().toString())) {
				tv_login.setBackgroundResource(R.drawable.btn_login);
			}else{
				tv_login.setBackgroundResource(R.drawable.btn_login_yellow);
			}
		}  
	};

	public void getUser(String userId) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId);

		UserManager.getInstance().getTzjcasGetUserInfo(context, map, new ServiceCallback<CommonResult<User>>() {

			@Override
			public void error(String msg) {
				dialogToast(msg);
			}

			@Override
			public void done(int what, CommonResult<User> obj) {
				if (obj != null && obj.data != null) {
					App.getInstance().setUser(null);
					App.getInstance().setUser(obj.data);// 存储用户信息

					if(null != MainAc.intance){
						MainAc.intance.finish();
					}
					Intent intent = new Intent(context, MainAc.class);
					intent.putExtra("tag", 0);
					startActivity(intent);
					finish();

				} else {
					dialogToast("获取数据失败");

				}

			}
		});
	}
}
