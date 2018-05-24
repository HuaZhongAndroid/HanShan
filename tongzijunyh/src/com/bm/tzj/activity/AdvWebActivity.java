package com.bm.tzj.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.base.BaseActivity;
import com.bm.share.ShareModel;
import com.bm.util.GlobalPrams;
import com.richer.tzj.R;

public class AdvWebActivity extends BaseActivity implements OnClickListener{

	private String url;
	private WebView web_view;
	private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.activity_web);
		Intent intent =getIntent();
		url = intent.getStringExtra(GlobalPrams.WebUrl);
		setTitleName("");
		initView();
	}

	private void initView() {
		showProgressDialog();
		web_view = (WebView)findViewById(R.id.web_view);
		web_view.setWebViewClient(new MyWebViewClient());
		web_view.loadUrl(url);
		tv_right_share_left.setVisibility(View.VISIBLE);
		tv_right_share_left.setOnClickListener(this);
	}

	class MyWebViewClient extends WebViewClient{
		

		@SuppressLint("NewApi")
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override   
		public void onPageFinished(WebView view, String url) {  
			title = view.getTitle();
			setTitleName(view.getTitle()); 
			hideProgressDialog();
		}   
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_share_left:
			ShareModel model = new ShareModel();
			model.conent = url;
			model.title = title;
			model.targetUrl = url;
			share.shareInfo(model,0);
			break;

		default:
			break;
		}
	}
}
