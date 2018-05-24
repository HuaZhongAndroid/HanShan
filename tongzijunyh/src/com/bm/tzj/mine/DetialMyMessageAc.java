package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.MessageDetail;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

import cz.msebera.android.httpclient.util.TextUtils;

/**
 * 消息详情
 * 
 * @author jiangsh
 * 
 */
public class DetialMyMessageAc extends BaseActivity  {
	private Context context;
	private TextView tv_title,tv_date;
	private ImageView iv_pic;
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_detial_message);
		context = this;
		setTitleName("消息详情");
		initView();
		getMessageDetial();
	}
	

	private void initView() {
		webview = (WebView) findViewById(R.id.webview);//课程要点
		webview.setBackgroundColor(0);
		webview.getBackground().setAlpha(0);
		
		tv_title=findTextViewById(R.id.tv_title);
		iv_pic=findImageViewById(R.id.iv_pic);
		tv_date=findTextViewById(R.id.tv_date);
	}

	private void setData(MessageDetail detail) {
//		tv_date.setText(detail.ctime);
//		ImageLoader.getInstance().displayImage(detail.titleMultiUrl, iv_pic,App.getInstance().getHeadOptions());
		
		if(!TextUtils.isEmpty(detail.content)){//富文本内容
			webview.setVisibility(View.VISIBLE);
			tv_title.setVisibility(View.GONE);
			if(detail.content!=null){
				Util.initViewWebData(webview, detail.content+"");
			}else{
				webview.setLayoutParams(new LayoutParams(0,0));
			}
		}
		if(!TextUtils.isEmpty(detail.thinContent)){//文本内容
			webview.setVisibility(View.GONE);
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(detail.thinContent);
			
		}
		
		
	}
	/**
	 * 获取消息详情
	 */
	public void getMessageDetial(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("messageId", getIntent().getStringExtra("messageId"));
		if(null != App.getInstance().getUser()){
			map.put("userId",  App.getInstance().getUser().userid);
		}else{
			map.put("userId",  "0");
		}
		showProgressDialog();
		UserManager.getInstance().getTzjmessageMessagedetail(context, map, new ServiceCallback<CommonResult<MessageDetail>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<MessageDetail> obj) {
				hideProgressDialog();
				if(null !=obj.data){
					setData(obj.data);
				}
			}
		});
	}

}
