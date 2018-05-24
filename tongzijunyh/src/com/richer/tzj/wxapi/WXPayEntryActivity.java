package com.richer.tzj.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bm.app.App;
import com.bm.pay.weixin.PayActivity;
import com.bm.tzj.activity.AbsCoursePayBaseAc;
import com.bm.tzj.activity.LeyuanStoreAc;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.kc.PayInfoAc;
import com.bm.tzj.kc.PayInfoAc2;
import com.bm.tzj.mine.RechargeAc;
import com.bm.tzj.mine.RechargeAc2;
import com.richer.tzj.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_results);
        
    	api = WXAPIFactory.createWXAPI(this, PayActivity.APP_ID);
        api.handleIntent(getIntent(), this);
        
        
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		  if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
	            int code = resp.errCode;
	            String msg = "";
	            switch (code) {
	            case 0:
	                msg = "支付成功！";
	                if(PayActivity.pageType.equals("CourseDetailAc")){//课程详情
	                	if(null != PayInfoAc.intance ){
	                		PayInfoAc.intance.finish();
	                	}
//	                	if(null != CourseDetailAc.intances){
//	                		CourseDetailAc.intances.finish();
//	                	}
	                }else if(PayActivity.pageType.equals("AbsCoursePayBaseAc")) {//课程支付
						App.toast("支付成功!您可以前往我的-我的课程查看已购买的课程!");
						if (null != PayInfoAc2.intance) {
							PayInfoAc2.intance.finish();
						}
						if (null != AbsCoursePayBaseAc.intances) {
							AbsCoursePayBaseAc.intances.finish();
						}
					}else if("CoursebaoAc".equals(PayActivity.pageType)){//课程包购买
						MainAc.intance.isDefult =0;
						if (null != PayInfoAc2.intance) {
							PayInfoAc2.intance.finish();
						}
						App.toast("支付成功!");
					}
					else if (PayActivity.pageType.equals("MyCourse")){//我的课程
//	                	if(null != MyCourseAc.intance ){
//	                		MyCourseAc.intance.finish();
//	                	}
	                	PayInfoAc.intance.finish();
	                }else if(PayActivity.pageType.equals("RechargeAc")){//充值
	                	if(null != RechargeAc2.intance){
	                		RechargeAc2.intance.finish();
	                	}
	                }
	                MainAc.intance.getUserInfo();
	                finish();
	                break;
	           case -1:
	                msg = "支付失败！";
	                finish();
	                break;
	            case -2:
	                msg = "您取消了支付！";
	                finish();
	               break;
	 
	            default:
	                msg = "支付失败！";
	                finish();
	                break;
	           }
	           App.toast(msg);
		  }

//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("提示");
//			builder.setMessage("返回状态："+String.valueOf(resp.errCode));
//			builder.show();
//		}
	}
}