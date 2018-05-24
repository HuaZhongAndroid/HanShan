package com.bm.tzj.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureActivity;
import com.bm.base.BaseFragmentActivity;
import com.bm.base.adapter.SendNewPostPicTwoAdapter;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.dialog.UtilDialog;
import com.bm.entity.GrowUp;
import com.bm.entity.GrowUpImg;
import com.bm.entity.ImageTag;
import com.bm.tzj.city.City;
import com.bm.tzj.fm.FindFm;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.widget.FuGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 成长中心发布成长记录
 * 
 * @author jiangsh
 * 
 */
public class SendGrowUpAc extends BaseCaptureActivity implements OnClickListener {
//	private TextView tv_submit;
	private EditText et_content;
	private FuGridView fgv_addImage;
	private ThreeButtonDialog buttonDialog;

	public List<String> uploadListImg = new ArrayList<String>(); //要上传的图片

	private List<GrowUpImg> attachmentlist = new ArrayList<GrowUpImg>(); //之前已经上传的图片

	private LinearLayout ll_send_parent;

	private GrowUp data;

	private boolean isSaveCaogao; //是否提示保存草稿
	

	private final int IMG_COUNT_MAX = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_send_grow_up);
		setTitleName("成长记录");
		init();

		isSaveCaogao = true;
		data = (GrowUp)this.getIntent().getSerializableExtra("data");
		if(data == null)
			loadCaogao();
		else
		{
			isSaveCaogao = false;
			setData();
		}
	}

	private void setData()
	{
		if(data.attachmentlist != null) {
			attachmentlist = data.attachmentlist;
			adapter.notifyDataSetChanged();
		}
		if(data.content != null)
		{
			et_content.setText(data.content);
		}
	}

	//载入草稿
	private void loadCaogao()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);
		}
		if(App.getInstance().getChild() == null)
			map.put("babyId", "-1");
		else
			map.put("babyId", App.getInstance().getChild().babyId);

		showProgressDialog();
		UserManager.getInstance().getGrowthRecord_getDraftInfo(this, map, new ServiceCallback<CommonResult<GrowUp>>(){

			@Override
			public void done(int what, CommonResult<GrowUp> obj) {
				hideProgressDialog();
				if(obj.data != null) {
					data = obj.data;
					setData();
				}
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
//				toast(msg);
//				finish();
			}});
	}


	BaseAdapter adapter = new BaseAdapter() {
		@Override
		public int getCount() {
			int s  = attachmentlist.size() + uploadListImg.size();
			if(s < IMG_COUNT_MAX)
				s+=1;
			return s;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Log.d("fffffff","getView " + position);
			if(convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_send_pic_two, parent, false);
				int s = (int)(context.getResources().getDisplayMetrics().widthPixels /3.3);
				convertView.getLayoutParams().width = s;
				convertView.getLayoutParams().height = s;
			}

			ImageView iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
			View iv_delete = convertView.findViewById(R.id.iv_delete);
			convertView.setOnClickListener(null);

			if(position == attachmentlist.size() + uploadListImg.size())
			{
				iv_delete.setVisibility(View.GONE);
//				iv_pic.setImageResource(R.drawable.pic_add);
				String  imgstr = "drawable://" + R.drawable.pic_add;
				ImageLoader.getInstance().displayImage(imgstr, iv_pic, App.getInstance().getListViewDisplayImageOptions());
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						buttonDialog.show();
					}
				});
			}
			else if(position < attachmentlist.size())
			{
				iv_delete.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(attachmentlist.get(position).url, iv_pic, App.getInstance().getListViewDisplayImageOptions());
				iv_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						delAttachment(attachmentlist.get(position));
					}
				});
			}
			else
			{
				iv_delete.setVisibility(View.VISIBLE);
				final String src = uploadListImg.get(position-attachmentlist.size());
				ImageLoader.getInstance().displayImage("file://"+src, iv_pic, App.getInstance().getListViewDisplayImageOptions());
				iv_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						uploadListImg.remove(src);
						adapter.notifyDataSetChanged();
					}
				});
			}

			return convertView;
		}
	};

	//删除服务端的图片附件
	private void delAttachment(final GrowUpImg img)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pkid",img.pkid);
		showProgressDialog();
		UserManager.getInstance().sysAttachment_deleteById(this, map, new ServiceCallback<StringResult>(){
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				attachmentlist.remove(img);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				toast(msg);
			}
		});
	}

	@Override
	public void clickLeft() {
		backCheck();
	}

	@Override
	public void onBackPressed()
	{
		backCheck();
	}

	private void backCheck()
	{
		if (isSaveCaogao && (!TextUtils.isEmpty(et_content.getText().toString()) || uploadListImg.size()>0) )
		{
			UtilDialog.dialogTwoBtnContentTtile(context, null, "退出","保存","是否保存为草稿？",new Handler(){
				public void handleMessage(android.os.Message msg) {
					if(msg.what == 14) {
						finish();
						return;
					}

					addArticle("0");
				}
			},1);
		}
		else
		{
			finish();
		}
	}

	private void init() {
		ll_send_parent = findLinearLayoutById(R.id.ll_send_parent);
		fgv_addImage = (FuGridView) findViewById(R.id.fgv_addImage);
//		tv_submit = findTextViewById(R.id.tv_submit);
		et_content = findEditTextById(R.id.et_content);
		et_content.clearFocus();
//		tv_submit.setOnClickListener(this);
		ll_right.setVisibility(View.VISIBLE);
		tv_right_share_left.setText("发布");
		tv_right_share_left.setVisibility(View.VISIBLE);
		tv_right_share_left.setOnClickListener(this);
		buttonDialog = new ThreeButtonDialog(context).setFirstButtonText("拍照")
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
						pickPhotoList(IMG_COUNT_MAX-uploadListImg.size()-attachmentlist.size());
					}
				}).autoHide();

		fgv_addImage.setAdapter(adapter);

		ll_send_parent.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (SendGrowUpAc.this.getCurrentFocus() != null) {
						if (SendGrowUpAc.this.getCurrentFocus()
								.getWindowToken() != null) {
							// 调用系统自带的隐藏软键盘
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											SendGrowUpAc.this
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
					if (SendGrowUpAc.this.getCurrentFocus() != null) {
						if (SendGrowUpAc.this.getCurrentFocus()
								.getWindowToken() != null) {
							// 调用系统自带的隐藏软键盘
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											SendGrowUpAc.this
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



	@Override
	protected void onPhotoTaked(String photoPath) {
		uploadListImg.add(photoPath);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 照片多选以后回调
	 * @param photoPath
	 */
	protected void onPhotoListTaked(List<String> photoPath){
		uploadListImg.addAll(photoPath);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right_share_left:
			addArticle("1");
			break;
		default:
			break;
		}
	}
	
	/**
	 * 发帖
	 * saveType 0保存草稿，1正常发布
	 */
	private void addArticle(final String saveType){
		
//		tv_right_share_left.setClickable(false);
//		tv_right_share_left.setFocusable(false);
//		tv_right_share_left.setText("发布中...");

		if(et_content.getText().toString().trim().length() == 0 && attachmentlist.size() + uploadListImg.size() == 0)
		{
			toast("发布内容不能为空");
			return;
		}
		
		HashMap<String, String> map =new HashMap<String, String>();

		if(data != null)
			map.put("pkid", data.pkid);
		map.put("userId", App.getInstance().getUser().userid);
		if(App.getInstance().getChild() == null)
			map.put("babyId", "-1");
		else
			map.put("babyId", App.getInstance().getChild().babyId);
		map.put("content", et_content.getText().toString());
		map.put("saveType", saveType);
		showProgressDialog();
		UserManager.getInstance().sendGrowUpRecord(context, uploadListImg, map, new ServiceCallback<StringResult>() {
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
//				tv_right_share_left.setClickable(true);
//				tv_right_share_left.setFocusable(true);
//				tv_right_share_left.setText("发布");
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				if ("0".equals(saveType)) {
					toast("草稿保存成功");
				}else{
					toast("发布成功");
				}
				finish();
			}
		});
	}
	
}
