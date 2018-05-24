package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.DisclaimerAc;
import com.bm.util.TimeCount;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.tool.SharedPreferencesHelper;
import com.richer.tzj.R;

/**
 * 注册
 * @author shiyt
 *
 */
public class RegistreAc extends BaseActivity implements OnClickListener{
	private Context context;
	private LinearLayout ll_check;
	private EditText et_phone, et_code;
	private TextView tv_next, tv_verifcode,tv_xy,tv_content;
	private ImageView img_checkbox;
	private boolean isChecked=false;
	int loginCount = 1;
	TimeCount count;
	Intent intent;
	public static RegistreAc intance;
	String strPageType="";//Login 登录  RegistreAc 用户注册
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_registre);
		context = this;
		setTitleName("注册");
		intance = this;
		initView();
	}
	
	public void initView(){
		strPageType = getIntent().getStringExtra("pageType");
		img_checkbox=findImageViewById(R.id.img_checkbox);
		ll_check=findLinearLayoutById(R.id.ll_check);
		tv_next = findTextViewById(R.id.tv_next);
		tv_verifcode = findTextViewById(R.id.tv_verifcode);
		et_phone = findEditTextById(R.id.et_phone);
		et_code = findEditTextById(R.id.et_code);
		tv_xy=findTextViewById(R.id.tv_xy);
		tv_content=findTextViewById(R.id.tv_content);
		tv_next.setOnClickListener(this);
		tv_verifcode.setOnClickListener(this);
		ll_check.setOnClickListener(this);
		tv_xy.setOnClickListener(this);
		
		tv_content.setText(Html.fromHtml("<font color=\""+context.getResources().getColor(R.color.txt_name)+"\">(请在</font><font size=\"25\"  color=\""+context.getResources().getColor(R.color.txt_yellow_color)+"\">120</font><font  color=\""+context.getResources().getColor(R.color.txt_name)+"\">秒内完成验证，超时请点击重新发送)</font>"));
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
		if(strPageType.equals("Login")){
			map.put("loginType", getIntent().getStringExtra("loginType"));  // 空 普通验证 1 QQ  2 微博登录  3 微信登录
		}
		map.put("type", "1");//验证入口  1 注册 2 忘记密码
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
	
	
	/**
	 * 验证手机号码和短信验证码
	 * 
	 * @param phone
	 *            手机号码
	 * @param authCode
	 *            验证码
	 */
	public void getCheckAuthCode(String phone,final String authCode) {
		if(TextUtils.isEmpty(phone)){
			dialogToast("手机号码不能为空");
			return;
		}else{
			if (!Util.isMobileNO(phone)) {
				dialogToast("请输入正确的手机号码");
				return;
			} 
		}
		
		if(TextUtils.isEmpty(authCode)){
			dialogToast("验证码不能为空");
			return;
		}
		
		if(!isChecked){
			dialogToast("请阅读注册协议并打对勾。");
			return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("authCode", authCode);
		map.put("type", "1");//验证入口  1 注册 2 忘记密码
		showProgressDialog();
		UserManager.getInstance().getTzjcasCheckcode(context,map,new ServiceCallback<StringResult>() {

			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				
				if(strPageType.equals("Login")){ //第三方登录验证 是绑定还是注册
					checkPhone();
				}else{//注册流程
					intent = new Intent(context, PerfectInfoAc.class);
					intent.putExtra("pageTag", "RegistreAc");
					intent.putExtra("authCode", authCode);
					intent.putExtra("phone", et_phone.getText().toString().trim());
					startActivity(intent);
				}
				
				
				
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
		});
	}
	
	/**
	 * 验证第三方走绑定还是注册流程
	 */
	public void checkPhone(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", et_phone.getText().toString().trim());
		map.put("loginType", getIntent().getStringExtra("loginType"));
//		map.put("loginStatus", getIntent().getStringExtra("loginStatus"));
		map.put("openId", getIntent().getStringExtra("openId"));
		
		showProgressDialog();
		UserManager.getInstance().getCheckPhone(context, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();	
				if(null != obj.data){
					if(obj.data.loginStatus.equals("2")){//绑定流程  登录成功
						SharedPreferencesHelper.saveString("userid", obj.data.userid);//保存用户ID
						
						App.getInstance().setUser(obj.data);// 存储用户信息
						Intent intent = new Intent(context, MainAc.class);
						intent.putExtra("tag", 2);
						startActivity(intent);
						finish();
						LoginAc.intatce.finish();
					}else if(obj.data.loginStatus.equals("1")){//注册流程
//						intent = new Intent(context, ForgotpassAc.class);
						intent = new Intent(context, PerfectInfoAc.class);
						intent.putExtra("pageType", "RegistreAc");
						intent.putExtra("phone", et_phone.getText().toString().trim());
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_check:
			if(isChecked){
				img_checkbox.setImageResource(R.drawable.login_no_selected);
				isChecked = false;
			}else{
				img_checkbox.setImageResource(R.drawable.login_selected);
				isChecked = true;
			}
			
			break;
		case R.id.tv_next:  //下一步
//			if(!isChecked){
//				dialogToast("请阅读注册协议并打对勾。");
//			}else{
				getCheckAuthCode(et_phone.getText().toString().trim(),et_code.getText().toString().trim());//验证手机号码和验证码是否正确
//			}
			break;
		case R.id.tv_verifcode:  //获取验证码
			getAuthCode(et_phone.getText().toString().trim());
			break;
		case R.id.tv_xy:  //注册协议
			intent = new Intent(context, DisclaimerAc.class);
			intent.putExtra("pageType", "RegistrAc");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
