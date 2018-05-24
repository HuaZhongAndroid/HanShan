package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.base.BaseActivity;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Version;
import com.bm.tzj.kc.DisclaimerAc;
import com.bm.util.UpdateUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.tool.Tools;
import com.richer.tzj.R;

/**
 * 检查更新
 * 
 * @author jiangsh
 * 
 */
public class UpdateVAc extends BaseActivity implements OnClickListener {
	private Context context;
	private TextView tv_submit,tv_verson,tv_newVerson,tv_service;
	private ImageView iv_pic;
	String version="";//安装包版本号
	String uploadUrl  = null;//更新地址
	int allVersionCode = 0;
	boolean isHideClancle = false;
	String text = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_updatev);
		context = this;
		setTitleName("检查更新");
		initView();
	}

	private void initView() {
		tv_service = findTextViewById(R.id.tv_service);
		tv_newVerson = findTextViewById(R.id.tv_newVerson);
		iv_pic = findImageViewById(R.id.iv_pic);
		tv_submit = findTextViewById(R.id.tv_submit);
		tv_verson=findTextViewById(R.id.tv_verson);
		tv_submit.setOnClickListener(this);
		tv_service.setOnClickListener(this);
		setData();
	}

	private void setData() {
		version = Tools.getVersionName(this);
		tv_verson.setText("版本 " + version);//当前版本：
		getNewVersion();
	}
	
	/**
	 * 
	 * 
	 * 获得最新版本
	 * 
	 */
	public void getNewVersion(){
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("apkType", "1");// 1用户端 2教练端
		showProgressDialog();
		UserManager.getInstance().getTzjversionSearchVerson(context, map, new ServiceCallback<CommonResult<Version>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonResult<Version> obj) {
				hideProgressDialog();
				if(null != obj.data){
					uploadUrl = obj.data.filepath;
					
//					//更新方式 0手动更新 1强制更新
					int way = getNullIntData(obj.data.isMustUpdate);
					allVersionCode = getNullIntData(obj.data.vCode);
					int code = Tools.getVersionCode(context);
					
					tv_verson.setText("版本 " + obj.data.vName);
					
					if(allVersionCode > code){
						if(way == 0){
							text = "有新版本需要更新，是否下载";
							isHideClancle = false;
						}else{
							text = "有新版本需要更新，请点击下载";
							isHideClancle = true;
						}
						tv_submit.setText("点此进行版本升级");
						tv_submit.setClickable(true);
						tv_submit.setFocusable(true);
						tv_submit.setFocusableInTouchMode(true);
					}else{
						tv_submit.setText("当前已是最新版本");
						tv_submit.setClickable(false);
						tv_submit.setFocusable(false);
						tv_submit.setFocusableInTouchMode(false);
					}
				}
			}
		});
	}
	
	public void getVersion(){
		UtilDialog.dialogVersion(isHideClancle,context, text, "取消", "确定",new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				int what = msg.what;
				switch (what) {
				case 100:// 取消事件
					
					break;
				case 101:// 确定事件
					download(uploadUrl);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		});
	}
	
	public void download(String url){
		UpdateUtil util = new UpdateUtil(context);
		util.showDialog(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:// 版本更细
			int code = Tools.getVersionCode(context);
			if(code != allVersionCode){
				getVersion();
			}
			break;
		case R.id.tv_service:
			Intent intent = new Intent(this,DisclaimerAc.class);
			intent.putExtra("pageType", "UpdateVAc");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
