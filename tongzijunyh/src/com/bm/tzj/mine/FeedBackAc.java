package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

/**
 * 意见反馈
 * 
 * @author shiyt
 * 
 */
public class FeedBackAc extends BaseActivity implements OnClickListener {
	private Context context;
	private EditText et_content, et_phone;
	private TextView tv_save,tv_prompt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_feedback);
		context = this;
		setTitleName("意见反馈");
		initView();
	}
	
	public void initView(){
		et_content=findEditTextById(R.id.et_content);
		et_phone=findEditTextById(R.id.et_phone);
		tv_save=findTextViewById(R.id.tv_save);
		et_content=findEditTextById(R.id.et_content);
		tv_prompt = findTextViewById(R.id.tv_prompt);
		
		tv_save.setOnClickListener(this);
		
		et_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				tv_prompt.setText(s.length()+"/100");
			}
		});
	}
	
	/**
	 *提交意见反馈
	 */
	private void addFeedBack(){
		if(TextUtils.isEmpty(et_content.getText())){
			dialogToast("反馈内容不能为空");
			return;
		}
		if(TextUtils.isEmpty(et_phone.getText())){
			dialogToast("电话不能为空");
			return;
		}
		
		if(!Util.isMobileNO(et_phone.getText().toString().trim())){
			dialogToast("请输入正确的手机号码");
			return;
		}
		
		showProgressDialog();
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		map.put("content", et_content.getText().toString());
		map.put("phone", et_phone.getText().toString());
		map.put("source", "1");//1：用户端  2：教练端
		UserManager.getInstance().getMyselfAddFeedBack(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				toast("反馈成功");
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_save:
			addFeedBack();
			break;
		default:
			break;
		}
		
	}
}
