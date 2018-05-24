package com.bm.base;

import java.io.File;
import java.util.List;
import java.util.UUID;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.bm.app.App;
import com.bm.util.CompressImg;
import com.selectimg.logic.ImgFileListActivity;
/**
 * 照相Activity基类
 *
 */
public abstract class BaseCaptureActivity extends BaseActivity{
	private String TAG="BaseAbActivity";
	//用户选取的原始图片
	protected File CapturePhotoFile;
	//用户选取的裁剪后的图片
	protected File CropPhotoFile;
	/* 用来标识请求照片多选的activity */
	protected static final int PHOTOS_WITH_DATA = 3024;
	/* 用来标识请求照相功能的activity */
	protected static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	protected static final int PHOTO_PICKED_WITH_DATA = 3021;
	/**
	 * 裁剪图片返回
	 */
	protected static final int PHOTO_CROP = 3022;
	/* 拍照的照片存储位置 */
	protected File PHOTO_DIR = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PHOTO_DIR = getExternalCacheDir();
		CapturePhotoFile = new File(PHOTO_DIR, "tmp_capture.jpg");
	}
	
	/**
	 * 从相册多选照片
	 */
	protected void pickPhotoList(int count){
		Intent intent = new Intent();
		intent.setClass(this,ImgFileListActivity.class);
		ImgFileListActivity.imgCount = count;
		startActivityForResult(intent,PHOTOS_WITH_DATA);
	}
	
	/**
	 * 从相册选择照片
	 */
	protected void pickPhoto(){
		try {
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setType("image/*");
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			App.toast("没有找到照片");
		}
	}
	
	/**
	 * 拍照获取图片
	 * 拍照以后的图片保存到mCurrentPhotoFile
	 */
	Uri imageUri = null;
	protected void takePhoto() {
//		String status = Environment.getExternalStorageState();
//		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
//		if (!status.equals(Environment.MEDIA_MOUNTED)) {
//			App.toast("没有可用的存储卡");
//			return;
//		}
		try {
			CapturePhotoFile = new File(PHOTO_DIR, UUID.randomUUID()+".jpg");
		    imageUri = Uri.fromFile(CapturePhotoFile);  
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
			startActivityForResult(intent, CAMERA_WITH_DATA);
			
		} catch (Exception e) {
			App.toast("未找到系统相机程序");
		}
	}
	
	/**
	 * 图片拍照以后回调
	 * @param photoPath
	 */
	protected abstract void onPhotoTaked(String photoPath);
	
	/**
	 * 照片多选以后回调
	 * @param photoPath
	 */
	protected abstract void onPhotoListTaked(List<String> photoPath);
	
	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent mIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		//从相册选择图片返回
		case PHOTOS_WITH_DATA:
			List<String> pathList = mIntent.getStringArrayListExtra("files");
			onPhotoListTaked(pathList);
			break;
		//从相册选择图片返回
		case PHOTO_PICKED_WITH_DATA:
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
//			CompressImg.saveBitmap(currentFilePath, currentFilePath);   //居然直接修改原图质量，谁写的~！！！！
			onPhotoTaked(currentFilePath);
			
			//跳转裁剪
			//如果图片不在存储卡中，读取图片到缓存文件夹
//			if(currentFilePath==null || currentFilePath.length() == 0){
//				Log.i(TAG, "图片不在存储卡中！！！！》》》》》》》》》》》》》》");
//				//showProgressDialog("正在读取图片, 请稍后...");
//				
//				showProgressDialog();
//				ImageTool.saveToFile(this, uri, CapturePhotoFile, new Handler(
//						new Callback() {
//							@Override
//							public boolean handleMessage(Message arg0) {
//								hideProgressDialog();
//								if (CapturePhotoFile.exists()) {
//									cropImage();
//								}
//								return true;
//						}}));
//			}else{
//				//如果图片存在，直接读取
//				CapturePhotoFile = new File(currentFilePath);
//				if (CapturePhotoFile.exists()) {
//					cropImage();
//				}
//			}
			break;
		//拍照返回
		case CAMERA_WITH_DATA:
//			cropImage();//跳转裁剪
			
			String paths = CapturePhotoFile.getAbsolutePath();
			CompressImg.saveBitmap(paths, paths);
			onPhotoTaked(paths);
			break;
		//裁剪返回(裁剪返回以后，默认保存到CropPhotoFile)
		case PHOTO_CROP:
			if(CropPhotoFile!=null && CropPhotoFile.exists()){
				String path = CropPhotoFile.getAbsolutePath();
				Log.i("i", "裁剪后得到的图片的路径是 = " + path);
				onPhotoTaked(path);
			}
			break;
		}
	}
	
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (uri.getAuthority()==null || uri.getAuthority().length() ==0 ) {
			return null;
		}
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = managedQuery(uri, projection, null, null, null);
//		int column_index = cursor
//				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//		cursor.moveToFirst();
//		String path = cursor.getString(column_index);
		
		
		 String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 Cursor cursor = getContentResolver().query(uri,filePathColumn, null, null, null);
		 cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String path = cursor.getString(columnIndex);
		cursor.close();
		return path;
	}

	/**
	 * 裁剪图片
	 * @param requestCode
	 * @param source 原始图片文件
	 * @param output 输出文件
	 */
	public void cropImage(){
		Log.i(TAG, ">>>>>>>>>>>>>\n裁剪图片\n>>>>>>>>>>>>>>>>");
		try{
			Intent intent = new Intent("com.android.camera.action.CROP");
			//intent.setType("image/*");
			intent.setDataAndType(Uri.fromFile(CapturePhotoFile), "image/*");
			intent.putExtra("crop", "true");
			//intent.putExtra("aspectX", 1);
			//intent.putExtra("aspectY", 1);
			//intent.putExtra("outputX", 222);
			//intent.putExtra("outputY", 222);
			intent.putExtra("return-data", false);
			//intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			CropPhotoFile = new File(PHOTO_DIR, UUID.randomUUID()+".jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CropPhotoFile));
			startActivityForResult(intent, PHOTO_CROP);
		}catch(Exception e){
			e.printStackTrace();
			if(CapturePhotoFile.exists()){
				String path = CapturePhotoFile.getAbsolutePath();
				Log.i("i", "获取的图片的路径是 = " + path);
				onPhotoTaked(path);
			}
		}
	}
	
}
