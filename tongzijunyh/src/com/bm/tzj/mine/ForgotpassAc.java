package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

/**
 * 重置密码
 * @author shiyt
 *
 */
public class ForgotpassAc  extends BaseActivity implements OnClickListener {
	Context mContext;
	private EditText et_pwd,et_confirmpwd;
	private TextView tv_submit;
	private String pageType="";
	String strPwd,strConfirmPwd,strPhone="";
	public static ForgotpassAc intance;
	
	private int pwdType;  //0登陆密码   1支付密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_forgotpass);
		setTitleName("重置密码");
		mContext = this;
		intance = this;
		pageType=getIntent().getStringExtra("pageType");
		pwdType = this.getIntent().getIntExtra("pwdType", 0);
		init();
	}

	public void init() {
		strPhone = getIntent().getStringExtra("phone");
		et_pwd=findEditTextById(R.id.et_pwd);
		et_confirmpwd=findEditTextById(R.id.et_confirmpwd);
		tv_submit=findTextViewById(R.id.tv_submit);
		
		/**注册进入*/
		if("RegistreAc".equals(pageType)){
			tv_submit.setText("下一步");
			setTitleName("密码设置");
		}
		
		tv_submit.setOnClickListener(this);
	}
	
	public void saveInfo(){
		strPwd = et_pwd.getText().toString().trim();
		strConfirmPwd = et_confirmpwd.getText().toString().trim();
		
		if(strPwd.length()==0){
			dialogToast("请输入密码");
			return;
		}
		
		if(strConfirmPwd.length()==0){
			dialogToast("请输入密码");
			return;
		}
		
		if (strPwd.length() >= 6 && strPwd.length() <= 16) {//判断密码长度大于6小于16
			if (!strPwd.equals(strConfirmPwd)) {//判断2次密码是否一致
				dialogToast("两次密码输入不一致");
				return;
			} 
		} else {
			dialogToast("密码输入不符合规范");
			return;
		}
		/**注册进入*/
		if("RegistreAc".equals(pageType)){
			finish();
			Intent intent = new Intent(mContext, PerfectInfoAc.class);
			intent.putExtra("passWord", strConfirmPwd);
			intent.putExtra("pageTag", pageType);
			intent.putExtra("phone", strPhone);
			
			startActivity(intent);
		}else{
			getUpdatePwd(strPhone,strPwd);
		}
		
	}
	
	/**
	 * 修改密码
	 */
	public void getUpdatePwd(String phone ,String pwd){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("password", pwd);
		if(App.getInstance().getUser() != null)
		{
			map.put("userid", App.getInstance().getUser().userid);
			map.put("userId", App.getInstance().getUser().userid);
		}
		
		showProgressDialog();
		UserManager.getInstance().getTzjcasUpdatepass(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				finish();
				RetrievePasswordAc.intance.finish();
				hideProgressDialog();
				
				
			}
		}, pwdType);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			saveInfo();
			break;
		default:
			break;
		}
	}
}
	
