package com.bm.tzj.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.bm.base.BaseActivity;
import com.bm.util.AssetsUtil;
import com.richer.tzj.R;

public class RechargeXieyiAc extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_recharge_xieyi);
		
		super.setTitleName("充值协议");
		
		TextView tv_content = this.findTextViewById(R.id.tv_content);
		tv_content.setText(AssetsUtil.getTxtFromAssets(this, "xieyi.txt"));
	}
}
