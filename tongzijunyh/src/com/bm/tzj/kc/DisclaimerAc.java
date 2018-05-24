package com.bm.tzj.kc;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout.LayoutParams;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Disclaimer;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

/**
 * 免责声明
 * @author shiyt
 *
 */
public class DisclaimerAc extends BaseActivity {
	private Context mContext;
	private WebView webview;
//	private TextView tv_mzsm;
	private String pageType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_disclaimer);
		mContext = this;
		pageType=getIntent().getStringExtra("pageType");
		
		
		init();
		if("RegistrAc".equals(pageType)){
			setTitleName("用户协议");
			getRichText(1);
//			getRegistrationAgreement();
		}else if("HelpAc".equals(pageType)){
			setTitleName("帮助中心");
			webview.setWebChromeClient(wvcc);
//			getRichText(2);
			getHelp();
		}else if("AboutAc".equals(pageType)){
			setTitleName("关于软件");
			getRichText(3);
//			getAbout();
		}else if ("UpdateVAc".equals(pageType)){
			setTitleName("服务条款");
			getRichText(1);
		}else{
			setTitleName("免责声明");
			getDisclaimer();
		}
		
	}
	
	public void init(){
//		tv_mzsm=findTextViewById(R.id.tv_mzsm);
		webview = (WebView) findViewById(R.id.webview);
        //允许JavaScript执行  
		webview.getSettings().setJavaScriptEnabled(true);     
		webview.getSettings().setAllowFileAccess(true);  
		webview.getSettings().setDomStorageEnabled(true);
		
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.setWebViewClient(new MyWebViewClient()); 
		
		
//		webview.setBackgroundColor(0);
//		webview.getBackground().setAlpha(0);
//		
//		Util.initViewWebData(webview, "1.免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明<br/><br/>2.免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明<br/><br/>3.免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明免责声明");
	}
	
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
		   webview.goBack();// 返回前一个页面
		   setTitleName(webview.getTitle());
		   return true;
	  }
	  return super.onKeyDown(keyCode, event);
	 }
	
	/**
	 * 获取免责声明
	 */
	public void getDisclaimer(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", getIntent().getStringExtra("degree"));//类别
		
		showProgressDialog();
		UserManager.getInstance().getTzjgoodsGoodsState(mContext, map, new ServiceCallback<CommonResult<Disclaimer>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<Disclaimer> obj) {
				hideProgressDialog();
				if(null !=obj.data){
					//显示富文本信息  
					if(obj.data.stateIos!=null){
						Util.initViewWebData(webview, obj.data.stateIos+"");
//						tv_mzsm.setText(Html.fromHtml(obj.data.stateIos));
					}else{
						webview.setLayoutParams(new LayoutParams(0,0));
					}
				}
				
			}
		});
	} 
	
	
	public void getRichText(final int strType){
		webview.setVisibility(View.VISIBLE);
//		tv_mzsm.setVisibility(View.GONE);
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("richType","1");//1:用户端  2:教练端
		
		showProgressDialog();
		UserManager.getInstance().getTzjrichRichText(mContext, map, new ServiceCallback<CommonResult<Disclaimer>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<Disclaimer> obj) {
				hideProgressDialog();
				if(null !=obj.data){
					String strContent = "";
					if(strType == 1){//注册协议
						strContent = obj.data.richRegister;
					}else if (strType == 2){//帮助
						strContent = obj.data.richHelp;
					}else if (strType == 3){//关于软件
						strContent = obj.data.richAbout;
					}
					
					
					//显示富文本信息  
					if(strContent!=null){
						Util.initViewWebData(webview, strContent+"");
					}else{
						webview.setLayoutParams(new LayoutParams(0,0));
					}
				}
				
			}
		});
	} 
	
	
	private boolean bl_finished = false;
	class MyWebViewClient extends WebViewClient{
	
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.startsWith("tel:")){
				 Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url)); 
	                startActivity(intent); 
			}else{
				view.loadUrl(url);
			}
			//System.out.println("url-load-web:"+url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("fffffff", "onPageFinished");
			bl_finished = true;
			if("HelpAc".equals(pageType))
				setTitleName(webview.getTitle());
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d("fffffff", "onPageStarted");
			bl_finished = false;
		}
}

WebChromeClient wvcc = new WebChromeClient() {  
    @Override  
    public void onReceivedTitle(WebView view, String title) {  
        super.onReceivedTitle(view, title);  
        Log.d("ANDROID_LAB", "TITLE=" + title);  
        Log.d("ANDROID_LAB", "webview TITLE=" + webview.getTitle());
        if(bl_finished)
        	setTitleName(title);
    }

//	@Override
//	public void onProgressChanged(WebView view, int newProgress) {
//		// TODO Auto-generated method stub
//		super.onProgressChanged(view, newProgress);
//		if(newProgress == 100)
//		{
//			Log.d("ffff", "getTitle  "+webview.getTitle());
//		}
//	}  

}; 
	
	/**
	 * 注册协议
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public void getRegistrationAgreement(){
		webview.setVisibility(View.VISIBLE);
//		tv_mzsm.setVisibility(View.GONE);
		
		
        //找到Html文件，也可以用网络上的文件  
		webview.loadUrl("file:///android_asset/registers.htm");  
	}
	
	/**
	 * 帮助
	 */
	public void getHelp(){
		webview.setVisibility(View.VISIBLE);
//		tv_mzsm.setVisibility(View.GONE);
		String url = BaseApi.API_URL_PRE + "p/r?c=1001&os=aos&userid=" + App.getInstance().getUser().userid
				+ "&appVersion=" + Util.getAppVersion(context);
//		webview.loadUrl("file:///android_asset/help.htm");  
		webview.loadUrl(url);
	}
	
	/**
	 * 关于软件
	 */
	public void getAbout(){
		webview.setVisibility(View.VISIBLE);
//		tv_mzsm.setVisibility(View.GONE);
		webview.getSettings().setJavaScriptEnabled(true);  
		webview.loadUrl("file:///android_asset/about.htm");  
	}
}
