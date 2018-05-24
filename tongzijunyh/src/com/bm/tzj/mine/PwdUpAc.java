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
 * 支付密码修改
 * 
 * @author jsh
 * 
 */
public class PwdUpAc extends BaseActivity implements OnClickListener {
	Context mContext;
	private EditText et_pwd, et_confirmpwd,et_oldpwd;
	private TextView tv_submit, tv_forget;
	String strOldPwd="", strPwd="",strConfirmPwd="";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_pwdup);
		setTitleName("修改支付密码");
		mContext = this;
		init();
	}

	public void init() {
		et_pwd = findEditTextById(R.id.et_pwd);
		et_confirmpwd = findEditTextById(R.id.et_confirmpwd);
		tv_submit = findTextViewById(R.id.tv_submit);
		et_oldpwd=findEditTextById(R.id.et_oldpwd);
		tv_submit.setOnClickListener(this);
		
		tv_forget = this.findTextViewById(R.id.tv_forget);
		tv_forget.setOnClickListener(this);
	}
	
	public void saveInfo(){
		strOldPwd = et_oldpwd.getText().toString().trim();
		strPwd = et_pwd.getText().toString().trim();
		strConfirmPwd = et_confirmpwd.getText().toString().trim();
		
		
		if(strOldPwd.length()==0){
			dialogToast("请输入原支付密码");
			return;
		}
		if(strPwd.length()==0){
			dialogToast("请输入新支付密码");
			return;
		}
		
		if(strConfirmPwd.length()==0){
			dialogToast("请输入新支付密码");
			return;
		}
		
		if (strPwd.length() < 6 || strPwd.length() > 16) {//判断密码长度大于6小于16
			dialogToast("密码输入不符合规范");
			return;
		}
		
		if (!strPwd.equals(strConfirmPwd)) {//判断2次密码是否一致
			dialogToast("两次密码输入不一致");
			return;
		} 
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("accountId", App.getInstance().getUser().userid+"");
		map.put("paymentPassword", strConfirmPwd);
		map.put("oldPaymentPassword", strOldPwd);
		showProgressDialog();
		UserManager.getInstance().getTzjaccountUpdatePayPassword(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				finish();
				SettingAc.intance.finish();
				dialogToast("修改成功");	
			}
		});
		
			
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			saveInfo();
			break;
		case R.id.tv_forget:
			Intent intent = new Intent(this, RetrievePasswordAc.class);
			intent.putExtra("pwdType", 1);
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
	}
}
