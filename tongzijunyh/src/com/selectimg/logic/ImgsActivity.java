package com.selectimg.logic;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.base.BaseActivity;
import com.richer.tzj.R;
import com.selectimg.logic.ImgsAdapter.OnItemClickClass;

public class ImgsActivity extends BaseActivity {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	TextView choise_button;
	ArrayList<String> filelist;
	private ArrayList<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);
		imgGridView = (GridView) findViewById(R.id.gridView1);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		((TextView) this.findViewById(R.id.tv_title))
				.setText(fileTraversal.filename);
		filelist = new ArrayList<String>();
		filelist = this.getIntent().getStringArrayListExtra("list1");
		imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent,
				onItemClickClass,filelist);
		imgGridView.setAdapter(imgsAdapter);
		select_layout = (LinearLayout) findViewById(R.id.selected_image_layout);
		relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		choise_button = (TextView) findViewById(R.id.button3);
		hashImage = new HashMap<Integer, ImageView>();
		// imgGridView.setOnItemClickListener(this);
		choise_button.setText("完成(" + filelist.size() + ")");
		util = new Util(this);
	}

	class BottomImgIcon implements OnItemClickListener {

		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		}
	}

	public void cancelSelect(View v) {
		this.setResult(100);
		this.finish();
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox)
			throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(
				relativeLayout2.getMeasuredHeight() - 10,
				relativeLayout2.getMeasuredHeight() - 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		float alpha = 100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);

		if (filelist.size() >= ImgFileListActivity.imgCount) {
			Toast.makeText(this, "最多选择"+ImgFileListActivity.imgCount+"张照片", Toast.LENGTH_SHORT).show();
		} else {
			imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		}

		return imageView;
	}

	ImgCallBack imgCallBack = new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			select_layout.removeView(arg0);
			filelist.remove(filepath);
			choise_button.setText("完成(" + filelist.size() + ")");
		}
	}

	ImgsAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
				choise_button.setText("完成(" +filelist.size()
						+ ")");
			} else {
				if (filelist.size() >= ImgFileListActivity.imgCount) {
					Toast.makeText(ImgsActivity.this, "最多选择"+ImgFileListActivity.imgCount+"张照片",
							Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					checkBox.setChecked(true);
					Log.i("img", "img choise position->" + Position);
					ImageView imageView = iconImage(filapath, Position,
							checkBox);
					if (imageView != null) {
						hashImage.put(Position, imageView);
						filelist.add(filapath);
						select_layout.addView(imageView);
						choise_button.setText("完成("
								+ filelist.size() + ")");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view) {
		finish();
	}

	/**
	 * FIXME 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 *
	 * @param view
	 */
	public void sendfiles(View view) {
		Intent data = new Intent();
		data.putStringArrayListExtra("files", filelist);
		this.setResult(RESULT_OK, data);
		this.finish();
	}
}