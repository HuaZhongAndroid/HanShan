package com.bm.tzj.mine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mapapi.map.Text;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.city.City;
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
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class BindPhone extends BaseActivity implements OnClickListener{
	private UMShareAPI mShareAPI = null;
	private EditText et_phone ;
	private EditText et_code;
	private TextView tv_confirm;
	private TextView tv_verifcode ;
	TimeCount count;
	private String wechatJson;
	private City city;

	private TextView tv_nickname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_bind_phone);
		this.findViewById(R.id.rl_top).setVisibility(View.GONE);

		tv_nickname = this.findTextViewById(R.id.tv_nickname);

		mShareAPI = UMShareAPI.get( this );
		wechatJson = getIntent().getStringExtra(GlobalPrams.wechatJson);

		try {
			JSONObject o = new JSONObject(wechatJson);
			tv_nickname.setText("你好，"+o.getString("nickname"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		findViewById(R.id.ib_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BindPhone.this.finish();
			}
		});
		et_phone = (EditText)findViewById(R.id.et_phone);

		et_code = (EditText)findViewById(R.id.et_code);

		tv_confirm = (TextView)findViewById(R.id.tv_confirm);
		tv_verifcode =(TextView)findTextViewById(R.id.tv_verifcode);
		
		
		tv_verifcode.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_confirm:
			bindPhone();
			break;
		case R.id.tv_verifcode:
			//获取验证码
			getAuthCode(et_phone.getText().toString().trim());
			break;

		default:
			break;
		}

	}

	private void bindPhone() {
		final String strPhone = et_phone.getText().toString().replace(" ", "").trim();
		final String authCode = et_code.getText().toString().trim();
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
		HashMap<String, String> map = new HashMap<String, String>();
		showProgressDialog();
		
		map.put("phone", strPhone);
		map.put("authCode", authCode);
		
		map.put("wechatJson", wechatJson);
		city = App.getInstance().getCityCode();
		BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();
		map.put("longitude", info.lng+"");
		map.put("latitude", info.lat+"");

		map.put("deviceToken", Util.getIMEI(context));
		
		
		
		
		UserManager.getInstance().BindPhone(context, map, new ServiceCallback<CommonResult<User>>() {

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
					intent.putExtra("tag", 2);
					startActivity(intent);
					finish();
				}

			}
		});
		
		
		
		
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
				tv_confirm.setBackgroundResource(R.drawable.btn_login);
			}else{
				tv_confirm.setBackgroundResource(R.drawable.btn_login_yellow);
			}
		}  
	};

}
