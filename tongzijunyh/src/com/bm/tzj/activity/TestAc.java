package com.bm.tzj.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bm.base.BaseCaptureActivity;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.im.ac.FriendAc;
import com.bm.im.ac.GroupInfoAc;
import com.bm.share.ShareModel;
import com.bm.share.ShareUtil;
import com.bm.tzj.mine.MyExploreAc;
import com.richer.tzj.R;

public class TestAc extends BaseCaptureActivity implements OnClickListener {

	
	private ThreeButtonDialog buttonDialog;
	private Button btn_map,btn_share,btn_friend,btn_group,btn_Personal;
	public ShareUtil share;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		contentView(R.layout.ac_test);
		
		initView();

		share = new ShareUtil(this);
	}
	
	private void initView(){
		btn_map = findButtonById(R.id.btn_map);
		btn_map.setOnClickListener(this);
		
		btn_Personal = findButtonById(R.id.btn_Personal);
		btn_Personal.setOnClickListener(this);
		
		btn_share=findButtonById(R.id.btn_share);
		btn_friend=findButtonById(R.id.btn_friend);
		btn_group=findButtonById(R.id.btn_group);
		btn_group.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_friend.setOnClickListener(this);
		
		
		buttonDialog = new ThreeButtonDialog(this).setFirstButtonText("拍照")
				.setBtn1Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						takePhoto();
					}
				}).setThecondButtonText("从手机相册选择")
				.setBtn2Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						pickPhoto();
						
//						pickPhotoCostome();
					}
				}).autoHide();
		
		
		
		
		Button btnone = findButtonById(R.id.btnone);
		btnone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//buttonDialog.show();
				testGet();
			}
		});
	}
	
	@Override
	protected void onPhotoTaked(String photoPath) {
		
		System.out.println("图片地址："+photoPath);
		File file = new File(photoPath);
		List<File> filelist = new ArrayList<File>();
		filelist.add(file);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("provinceId", "310000");
		map.put("freshThingDetail", "123");
		map.put("freshThingtypeId", "6");
		map.put("cityId", "310100");
		map.put("userId", "4e8a79c08db046d6b15e4c771b8321be");
		//AsyncHttpHelp.httpPost(this, map,"allMultiFile",filelist);
		
	}
	
	public void testGet(){
		//http://112.64.173.178/huafa/app/userUnRead/getAllUnreadCount.do?type=1&cityId=310100&userId=1e55ff20a7264a0a9a3ebe02b066a5f8&
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("type", "1");
		map.put("cityId", "310100");
		map.put("userId", "1e55ff20a7264a0a9a3ebe02b066a5f8");
		
//		UserManager.getInstance().login(this, map, new ServiceCallback<CommonListResult<ReadCount>>() {
//			
//			@Override
//			public void error(String msg) {
//				
//			}
//			
//			@Override
//			public void done(int what, CommonListResult<ReadCount> obj) {
//				System.out.println("xxxsd:"+obj.data.get(0).count);
//			}
//		});
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.btn_map:
			intent = new Intent(this,LocationMapAc.class);
			intent.putExtra("longitude", "121.475816");
			intent.putExtra("latitude", "31.312117");
			startActivity(intent);
			break;
		case R.id.btn_Personal:
//			openActivity(PersonalInformation.class);
//			openActivity(MyComment.class);
			openActivity(MyExploreAc.class);
//			openActivity(MyCourse.class);
			
			break;

		case R.id.btn_share:
			ShareModel model = new ShareModel();
			model.title = "测试";
			model.conent = "测试内容";
			model.urlImg = "";
			model.targetUrl="http://www.baidu.com";
			share.shareInfo(model,0);
			break;
		case R.id.btn_friend:
			intent = new Intent(this, FriendAc.class);
			startActivity(intent);
			break;
		case R.id.btn_group:
			intent = new Intent(this, GroupInfoAc.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPhotoListTaked(List<String> photoPath) {
		// TODO Auto-generated method stub
		
	}
	

}
