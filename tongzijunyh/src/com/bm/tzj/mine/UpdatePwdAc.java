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
import com.bm.tzj.activity.MainAc;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

/**
 * 修改密码
 * @author shiyt
 *
 */
public class UpdatePwdAc extends BaseActivity implements OnClickListener{
	private Context context;
	private EditText et_oldpwd,et_newpwd,et_confirmpwd;
	private TextView tv_submit;
	String strOldPwd,strNewPwd,strConfirmPwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_updatepwd);
		context = this;
		setTitleName("修改登录密码");
		initView();
	}
	
	public void initView(){
		et_oldpwd=findEditTextById(R.id.et_oldpwd);
		et_newpwd=findEditTextById(R.id.et_newpwd);
		et_confirmpwd=findEditTextById(R.id.et_confirmpwd);
		tv_submit=findTextViewById(R.id.tv_submit);
		
		tv_submit.setOnClickListener(this);
	}
	
	public void updatePaw(){
		strOldPwd = et_oldpwd.getText().toString().trim();
		strNewPwd = et_newpwd.getText().toString().trim();
		strConfirmPwd = et_confirmpwd.getText().toString().trim();
		
		if(strOldPwd.length()==0){
			dialogToast("请输入原密码");
			return;
		}
		
		if(strNewPwd.length()==0){
			dialogToast("请输入新密码！");
			return;
		}
		
		if(strConfirmPwd.length()==0){
			dialogToast("请输入新密码！");
			return;
		}
		
		if (strOldPwd.length() >= 6 && strOldPwd.length() <= 16) {//判断密码长度大于6小于16
			if(strNewPwd.length() >= 6 && strNewPwd.length() <= 16){
				if (!strNewPwd.equals(strConfirmPwd)) {//判断2次密码是否一致
					dialogToast("两次密码输入不一致");
					return;
				}
			}else{
				dialogToast("密码输入不符合规范");
				return;
			}
			 
		} else {
			dialogToast("密码输入不符合规范");
			return;
		}
		
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		map.put("oldpassword", strOldPwd);
		map.put("newpassword", strNewPwd);
		
		showProgressDialog();
		UserManager.getInstance().getTzjaccountUpdatePassword(context, map,new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				dialogToast("密码修改成功");
//				SharedPreferencesHelper.saveString("passWord", strNewPwd);
				finish();
				SettingAc.intance.finish();
				MainAc.intance.finish();
				Intent intent = new Intent(UpdatePwdAc.this ,LoginAc.class);
				startActivity(intent);
			}
		} );
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			updatePaw();
			break;
		default:
			break;
		}
		
	}
}
