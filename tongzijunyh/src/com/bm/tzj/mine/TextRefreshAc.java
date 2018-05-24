package com.bm.tzj.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.bm.base.BaseActivity;
import com.lib.widget.refush.NsRefreshLayout;
import com.richer.tzj.R;

public class TextRefreshAc extends BaseActivity implements OnClickListener,NsRefreshLayout.NsRefreshLayoutController, NsRefreshLayout.NsRefreshLayoutListener {
	private Context context;
	private NsRefreshLayout refresh_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_textrefresh);
		context = this;
		setTitleName("刷新");
		initView();
	}
	
	
	public void initView(){
		refresh_view = (NsRefreshLayout)findViewById(R.id.swipyrefreshlayout);
		refresh_view.setRefreshLayoutController(this);
		refresh_view.setRefreshLayoutListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
//		case R.id.tv_other:
//			break;
		
		default:
			break;
		}
	}


	@Override
	public void onRefresh() {
		refresh_view.postDelayed(new Runnable() {
            @Override
            public void run() {
            	System.out.println("wanghy");
				refresh_view.finishPullRefresh();
            }
        }, 1000);
	}


	@Override
	public void onLoadMore() {
		refresh_view.postDelayed(new Runnable() {
          @Override
          public void run() {
          	System.out.println("wanghy1");
        	  refresh_view.finishPullLoad();
          }
      }, 1000);
	}


	@Override
	public boolean isPullRefreshEnable() {
		return true;
	}


	@Override
	public boolean isPullLoadEnable() {
		return true;
	}
	
	
	
}
