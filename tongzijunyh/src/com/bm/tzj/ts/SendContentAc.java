package com.bm.tzj.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureActivity;
import com.bm.base.adapter.SendNewPostPicTwoAdapter;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.entity.ImageTag;
import com.bm.tzj.city.City;
import com.bm.tzj.fm.FindFm;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.lib.widget.FuGridView;
import com.richer.tzj.R;

/**
 * 发帖
 * 
 * @author jiangsh
 * 
 */
public class SendContentAc extends BaseCaptureActivity implements
		SendNewPostPicTwoAdapter.OnItemClickListener, OnClickListener {
	private Context mContext;
	private TextView tv_submit;
	private EditText et_content;
	private FuGridView fgv_addImage;
	private SendNewPostPicTwoAdapter adapterPic = null;
	private ThreeButtonDialog buttonDialog;
	public List<ImageTag> listImg = new ArrayList<ImageTag>();
	public List<String> uploadListImg = new ArrayList<String>();
	private boolean isShowDelete = false;
	private LinearLayout ll_send_parent;
	
	private final int IMG_COUNT_MAX = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_send_content);
		mContext = this;
		setTitleName("发帖");
		init();
	}

	@Override
	public void clickLeft() {
		super.clickLeft();
		finish();
	}

	private void init() {
		ll_send_parent = findLinearLayoutById(R.id.ll_send_parent);
		fgv_addImage = (FuGridView) findViewById(R.id.fgv_addImage);
		tv_submit = findTextViewById(R.id.tv_submit);
		et_content = findEditTextById(R.id.et_content);
		et_content.clearFocus();
		tv_submit.setOnClickListener(this);
		buttonDialog = new ThreeButtonDialog(mContext).setFirstButtonText("拍照")
				.setBtn1Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						takePhoto();
					}
				}).setThecondButtonText("从手机相册选择")
				.setBtn2Listener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
//						pickPhoto();
						pickPhotoList(IMG_COUNT_MAX-uploadListImg.size());
					}
				}).autoHide();

		ImageTag tag2 = new ImageTag();
		tag2.setImageStr("");
		tag2.setImgTag(true);
		listImg.add(tag2);
		adapterPic = new SendNewPostPicTwoAdapter(mContext, listImg);
		fgv_addImage.setAdapter(adapterPic);

		fgv_addImage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (listImg.size() > IMG_COUNT_MAX) {
					dialogToast("最多只能上传"+IMG_COUNT_MAX+"张图片！");
					return;
				}
				if (listImg.size() == 1) {
					buttonDialog.show();
				} else if (listImg.size() > 1) {
					if (arg2 == (listImg.size() - 1)) {
						if (uploadListImg.size() < IMG_COUNT_MAX) {
							buttonDialog.show();
							if (isShowDelete) {
								isShowDelete = false;
							}
							adapterPic.setIsShowDelete(isShowDelete);
						}

					} else {
						// 删除图片
						// listImg.remove(arg2);

					}
				}
			}
		});
		fgv_addImage.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (isShowDelete) {
					isShowDelete = false;
				} else {
					isShowDelete = true;
				}
				adapterPic.setIsShowDelete(isShowDelete);
				return false;
			}
		});
		adapterPic.setClickListener(this);

		// final InputMethodManager manager = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		ll_send_parent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (SendContentAc.this.getCurrentFocus() != null) {
						if (SendContentAc.this.getCurrentFocus()
								.getWindowToken() != null) {
							// 调用系统自带的隐藏软键盘
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											SendContentAc.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}

				}
				return false;
			}
		});

		fgv_addImage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (SendContentAc.this.getCurrentFocus() != null) {
						if (SendContentAc.this.getCurrentFocus()
								.getWindowToken() != null) {
							// 调用系统自带的隐藏软键盘
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											SendContentAc.this
													.getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}

				}
				return false;
			}
		});

	}

	public void notifyList() {
		listImg.clear();
		for (int i = 0; i < uploadListImg.size(); i++) {
			ImageTag tag = new ImageTag();
			tag.setImageStr(uploadListImg.get(i));
			tag.setImgTag(false);
			listImg.add(tag);
		}
		if (uploadListImg.size() < IMG_COUNT_MAX) {
			ImageTag tag2 = new ImageTag();
			tag2.setImageStr("");
			tag2.setImgTag(true);
			listImg.add(tag2);
		}
		adapterPic.notifyDataSetChanged();
	}

	// 删除图片
	@Override
	public void onItemClick(ImageView view, int position) {
		uploadListImg.remove(position);
		notifyList();

	}

	@Override
	protected void onPhotoTaked(String photoPath) {
		uploadListImg.add(photoPath);
		notifyList();
	}
	
	/**
	 * 照片多选以后回调
	 * @param photoPath
	 */
	protected void onPhotoListTaked(List<String> photoPath){
		uploadListImg.addAll(photoPath);
		notifyList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			addArticle();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 发帖
	 */
	private void addArticle(){
		
		if(TextUtils.isEmpty(et_content.getText())){
			dialogToast("内容不能为空");
			return;
		}
		showProgressDialog();
		tv_submit.setClickable(false);
		tv_submit.setFocusable(false);
		tv_submit.setText("发布中...");
		
		HashMap<String, String> map =new HashMap<String, String>();
		map.put("postUserId", App.getInstance().getUser().userid);
		map.put("articleContent", et_content.getText().toString());
		City city  =App.getInstance().getCityCode();
		if(null!=city&&!TextUtils.isEmpty(city.regionName)){
			map.put("regionName", city.regionName);//城市名称
		}else{
			map.put("regionName", "西安市");//城市名称
//			map.put("regionName", "上海市");//城市名称
		}
		
//		map.put("provinceId", "310000");
//		map.put("userId", "4e8a79c08db046d6b15e4c771b8321be");
//		map.put("cityId", "310100");
//		map.put("freshThingtypeId", "1");
//		map.put("freshThingDetail",et_content.getText().toString());
		
		UserManager.getInstance().getArticleAddArticle(mContext, uploadListImg, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
				tv_submit.setClickable(true);
				tv_submit.setFocusable(true);
				tv_submit.setText("发布");
			}
			
			@Override
			public void done(int what, StringResult obj) {
				finish();
				hideProgressDialog();
				toast("发帖成功");
				if(null != FindFm.intance){
					FindFm.intance.RefreshDate("2");
				}
				
			}
		});
	}

}
