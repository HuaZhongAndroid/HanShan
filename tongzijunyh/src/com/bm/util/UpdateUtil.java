package com.bm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lib.log.Lg;
import com.richer.tzj.R;

public class UpdateUtil {

	 Dialog dialog;
	 Context context;
	 public  UpdateUtil(Context context){
		this.context = context;
		
		DataCleanManager.cleanApplicationData(context);
		
	 }

	/**
	 * 下载更新弹出框
	 */
	public  void showDialog(final String url) {
		Lg.i("#############url=", url);
		/** 设置dialog的style */
		dialog = new Dialog(context, R.style.mydailog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		/** 设置dialog的自定义布局 */
		View vDialog = LayoutInflater.from(context).inflate(
				R.layout.v_dialog_updateversion, null);
		dialog.setContentView(vDialog);

		/** 设置dialog的布局。 */
		WindowManager wm = ((Activity) context).getWindowManager();
		Display display = wm.getDefaultDisplay();
		android.view.WindowManager.LayoutParams lp = dialog.getWindow()
				.getAttributes();
		lp.width = display.getWidth();
		lp.height = LayoutParams.WRAP_CONTENT;
		dialog.getWindow().setAttributes(lp);
		/** 设置dialog位于中间 */
		Window window = dialog.getWindow();
		window.setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		pb = (ProgressBar) vDialog.findViewById(R.id.down_pb);
		// tv = (TextView) vDialog.findViewById(R.id.tv);
		new Thread() {
			public void run() {
				try {
					down_file(url, "/sdcard/");// 下载文件，参数：第一个URL，第二个存放路径
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	//下载文件
	public void down_file(String url, String path) throws IOException {
		Lg.i("@@@@url=", url+"@@@@path="+path);
//		System.out.println("#################url="+url+"path="+path);
		// 下载函数
		filename = url.substring(url.lastIndexOf("/") + 1);
		// 获取文件名
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		filePath = path + filename;
		FileOutputStream fos = new FileOutputStream(path + filename);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
//		System.out.println("cccccccccccc");
		sendMsg(0);
//		System.out.println("ddddddddddddd");
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;

			sendMsg(1);// 更新进度条
		} while (true);
		sendMsg(2);// 通知下载完成
		try {
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}

	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler1.sendMessage(msg);
	}
	
	 ProgressBar pb;
	// TextView tv;
	int fileSize;
	int downLoadFileSize;
	String fileEx, fileNa, filename;
	private String filePath;
	int currentVersionCode=0;
	String currentVersionName="";
	int androidNumber = 0;
	String androidName="";
	private Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					pb.setMax(fileSize);
				case 1:
					pb.setProgress(downLoadFileSize);
					int result = downLoadFileSize * 100 / fileSize;
					// tv.setText(result + "%");
					break;
				case 2:
					dialog.hide();
					Intent installIntent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.fromFile(new File(filePath));
					installIntent.setDataAndType(uri,
							"application/vnd.android.package-archive");
					installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent mPendingIntent = PendingIntent.getActivity(
							context, 0, installIntent, 0);
					context.startActivity(installIntent);// 下载完成之后自动弹出安装界面
					break;

				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(context, error, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 清空用户数据
	 */
//	private void cleantLogin() {
//		showProgressDialog();
//		UserService.getInstance().submitUserLogout(
//				new HashMap<String, String>(),
//				new ServiceCallback<BaseResult>() {
//
//					@Override
//					public void error(String msg) {
//						hideProgressDialog();
//						dialogToast(msg);
//
//					}
//
//					@Override
//					public void done(int what, BaseResult obj) {
//						hideProgressDialog();
//						if (obj.status == 1) {
//							// Push: 删除tag调用方式
//							List<String> tags = Utils.getTagsList(App
//									.getInstance().getUser().tag);
//							PushManager.delTags(getApplicationContext(), tags);
//							/** 关闭推送 */
//							PushManager.stopWork(SettingAc.this);
//							SharedPreferencesHelper.saveBoolean("isTSC", true);
//
////							SharedPreferencesHelper.saveBoolean("isCheck",false);// 清空记住密码的信息
////							SharedPreferencesHelper.saveString("phone", "");
////							SharedPreferencesHelper.saveString("password", "");
//							
//							// 清除用户数据
//							App.getInstance().setUser(null);
//							sendBroadcast(new Intent("logout"));
//							startActivity(new Intent(SettingAc.this,
//									LoginAc.class));
//						}
//					}
//				});
//
//	}
}
