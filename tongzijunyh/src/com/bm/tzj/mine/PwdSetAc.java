package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
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
 * 支付密码设置
 * 
 * @author jsh
 * 
 */
public class PwdSetAc extends BaseActivity implements OnClickListener {
	Context mContext;
	private EditText et_pwd, et_confirmpwd;
	private TextView tv_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_pwdset);
		setTitleName("设置支付密码");
		mContext = this;
		init();
	}

	public void init() {
		et_pwd = findEditTextById(R.id.et_pwd);
		et_confirmpwd = findEditTextById(R.id.et_confirmpwd);
		tv_submit = findTextViewById(R.id.tv_submit);

		tv_submit.setOnClickListener(this);
	}
	
	/**
	 * 提交
	 */
	public void submit(){
		String strPwd = et_pwd.getText().toString().trim();
		String confirmPwd = et_confirmpwd.getText().toString().trim();
		
		if(null == App.getInstance().getUser()){
			dialogToast("用户信息获取失败");
			return;
		}
		
		
		if(strPwd.length()==0){
			dialogToast("请输入新支付密码");
			return;
		}
		
		
		if (strPwd.length() >= 6 && strPwd.length() <= 16) {//判断密码长度大于6小于16
			if (!strPwd.equals(confirmPwd)) {//判断2次密码是否一致
				dialogToast("两次密码输入不一致");
				return;
			} 
		} else {
			dialogToast("密码输入不符合规范");
			return;
		}
		
//		if(!strPwd.equals(confirmPwd)){
//			dialogToast("两次密码输入不一致");
//			return;
//		}
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("accountId", App.getInstance().getUser().userid+"");
		map.put("paymentPassword", confirmPwd);
		showProgressDialog();
		UserManager.getInstance().getTzjaccountUpdatePayPassword(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				if(SettingAc.intance != null)
					SettingAc.intance.finish();
				finish();
				dialogToast("设置成功");
				MainAc.intance.getUserInfo();//刷新用户信息
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			submit();
			break;
		default:
			break;
		}
	}
}
