package com.bm.util;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

import com.lib.widget.ButtonsDialog;

/**
 * 添加图片、音频、视频等等的工具包
 *
 */
public class PostAttachUtils {
	public static final int REQUEST_VIDEO_CAPTURE = 1;
	public static final int REQUEST_AUDIO_CAPTURE = 2;
	public static final int REQUEST_EMOJI = 3;
	public static final int REQUEST_CAMERA = 4;
	/**
	 * 相册
	 */
	public static final int REQUEST_ALBUM = 5;
	

	/**
	 * 获取录制的视频(requestCode = REQUEST_VIDEO_CAPTURE)<br />
	 * 在返回的数据中使用intent.getData()获取音频Uri
	 * @param context
	 * @return boolean 是否调用成功
	 */
	public static boolean captureVideo(Activity context){
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
			context.startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
			return true;
		}
		return false;
	}
	
	/**
	 * 获取录制的音频(requestCode = REQUEST_AUDIO_CAPTURE)<br />
	 * 在返回的数据中使用intent.getData()获取音频Uri
	 * @param context
	 * @return boolean 是否调用成功
	 */
	public static boolean captureAudio(Activity context){
		//Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		//intent.setType("audio/*");
//		intent.setType("audio/3gp"); //String AUDIO_AMR = "audio/amr";
//		intent.setClassName("com.android.soundrecorder",
//		"com.android.soundrecorder.SoundRecorder");
		Intent intent = new Intent("android.provider.MediaStore.RECORD_SOUND");
	//	android.provider.MediaStore.RECORD_SOUND
		//intent.setClassName("com.android.soundrecorder","com.android.soundrecorder.SoundRecorder");
		context.startActivityForResult(intent, REQUEST_AUDIO_CAPTURE);
		/*MediaRecorder mRecorder = new MediaRecorder();  
        //设置音源为Micphone  
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
        //设置封装格式  
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat);  
        mRecorder.setOutputFile(mFileName);  
        //设置编码格式  
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
  
        try {  
            mRecorder.prepare();  
        } catch (IOException e) {  
            Log.e(LOG_TAG, "prepare() failed");  
        }  
  
        mRecorder.start();  */
		return true;
	}
	
	/**
	 * 获取表情(requestCode = REQUEST_EMOJI)<br />
	 * 在返回的数据中使用intent.get...Extra(EmojiActivity.IMOJI_TAG)
	 * @param context
	 * @return
	 */
	/*public static boolean chooseEmoji(Activity context){
		Intent intent = new Intent(context, EmojiActivity.class);  
		context.startActivityForResult(intent, REQUEST_EMOJI);
		return true;
	}*/
	
	/**
	 * 调用系统相机拍照(requestCode = REQUEST_CAMERA)<br />
	 * 
	 * @param context
	 * @param output Uri输出照片的位置 生成方法如下:Uri.fromFile(file) 
	 * @return
	 */
	public static boolean capture(Activity context, Uri output){
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
			context.startActivityForResult(intent, REQUEST_CAMERA);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 从相册选择照片(requestCode = REQUEST_ALBUM)<br />
	 * @param context
	 * @return
	 */
	public static boolean pickPhoto(Activity context){
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			context.startActivityForResult(intent, REQUEST_ALBUM);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 选择音频
	 * @param context
	 * @return
	 */
	public static boolean pickAudio(Activity context){
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("audio/*");
			context.startActivityForResult(intent, REQUEST_AUDIO_CAPTURE);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 选择视频
	 * @param context
	 * @return
	 */
	public static boolean pickVideo(Activity context){
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("video/*");
			context.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
		} catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 选择图片
	 */
	public static void picture(final Activity context,final long tmpFileIdx){
		//提示对话框
		new ButtonsDialog(context, new String[]{"拍照", "相册"}, new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case 0:
					File file = new File(Util.ROOTPATH,"pic"+tmpFileIdx+".jpg");
				      capture(context, Uri.fromFile(file));
					break;
				case 1:
					  pickPhoto(context);
					break;
				}
			}
		}, Gravity.CENTER).autoDismiss().show();
	}
	
	/**
	 * 选择音频
	 */
	public static void audio(final Activity context){
		//提示对话框
		new ButtonsDialog(context, new String[]{"录音", "选择录音"}, new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case 0:
					captureAudio(context);
					break;
				case 1:
					pickAudio(context);
					break;
				}
			}
		}, Gravity.CENTER).autoDismiss().show();
	}
	
	/**
	 * 选择视频
	 */
	public static void video(final Activity context){
		//提示对话框
		new ButtonsDialog(context, new String[]{"摄像", "选择视频"}, new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case 0:
					captureVideo(context);
					break;
				case 1:
					pickVideo(context);
					break;
				}
			}
		}, Gravity.CENTER).autoDismiss().show();
	}
	
	
	
}
