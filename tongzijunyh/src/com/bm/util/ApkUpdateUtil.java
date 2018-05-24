package com.bm.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bm.view.NumberSeekBar;
import com.richer.tzj.R;

public class ApkUpdateUtil {
	
	private static final String save_path = "/yingjidownload";
	private static final String apk_filename = "yingji.apk";
	

//	public static boolean checkUpdate(final Context c,Handler handler)
//	{
//		try {
//			
//			Map<String,String> map = new HashMap<String,String>();
////			String packagename = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).packageName;
//			int vCode = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
//			map.put("appversion", vCode+"");
//			
//			JSONObject result = JSONObject.parseObject(HttpUtil.exeRequestSynchronized(URLPath.UPDATE_CHECK, map));
//				
//				int newV = result.getIntValue("APP_VERSION_CODE");
//				final String newVinfo = result.getString("APP_VERSION_TIP");
//				final String downloadURL = result.getString("APP_VERSION_URL");
//			
////			if(newV - vCode > 3)
////			{
////				//版本过旧，强制更新
////				handler.post(new Runnable(){
////					@Override
////					public void run() {
////						new AlertDialog.Builder(c).setTitle("当前客户端版本过旧，已无法使用，请立即更新。").setCancelable(false)
////						.setMessage(newVinfo)
////						.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
////							@Override
////							public void onClick(DialogInterface d, int arg1) {
////								if(downloadApk(c))
////									waitDialog = ProgressDialog.show(c, null, "更新包下载中,请您稍待片刻...");
////							}
////						})
////						.setNegativeButton("退出", new DialogInterface.OnClickListener() {
////							@Override
////							public void onClick(DialogInterface dialog, int which) {
////								CloseActivityClass.exitClient(c); //防止从大厅返回到登陆界面
////								 System.exit(0);
////							}
////						})
////						.show();
////					}});
////				return true;
////			}
////			else if(vCode < newV)
////			{
//				handler.post(new Runnable(){
//					@Override
//					public void run() {
//						new AlertDialog.Builder(c).setTitle("检测到更新包，是否现在下载安装？")
//						.setMessage(newVinfo)
//						.setPositiveButton("开始下载", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface d, int arg1) {
//								downloadApk(c,downloadURL);
//							}
//						})
//						.setNegativeButton("稍候下载", null)
//						.show();
//					}});
//				return true;
////			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	private static boolean downloading=false;
	private static ProgressDialog waitDialog = null;
	
	/**
	 * 下载apk并安装
	 * @return 成功开始下载后返回true  false表示无法开始下载
	 */
	public static boolean downloadApk(final Context c,String downloadURL)
	{
		if(downloading)
		{
			Toast.makeText(c, "下载中，请稍候..", Toast.LENGTH_LONG).show();
			return false;
		}

		final Handler handler = new Handler();

		// 判断SD卡是否存在，并且是否具有读写权限
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
        	Toast.makeText(c, "SD卡不可用", Toast.LENGTH_LONG).show();
        	return false;
        }
        File savePath = new File(Environment.getExternalStorageDirectory(), save_path);
        final File apkFile = new File(savePath, apk_filename);

        final URL url;
		try {
			url = new URL(downloadURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Toast.makeText(c, "下载地址错误", Toast.LENGTH_LONG).show();
			return false;
		}

        final FileDownloader fd = new FileDownloader();

        final NotificationManager nm = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notif = new Notification();
        notif.icon = R.drawable.ic_launchers;
        notif.tickerText = "更新包下载中...";
        notif.contentView = new RemoteViews(c.getPackageName(),R.layout.notification_update);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask(){
        	private int pre = 0;
			@Override
			public void run() {
				if(notif.flags == Notification.FLAG_NO_CLEAR)  //下载完成后将通知设置为不可清除状态
					return;
				int length = fd.getLength();
		        int off = fd.getOff();
		        final String text = "更新包下载中...\n"+String.format("%.2f",off/1024.0/1024)+"MB/"+String.format("%.2f",length/1024.0/1024)
		        		+"MB 速度:"+String.format("%.2f",(off-pre)/1024.0)+"KB/s";
		        pre = off;
		        notif.contentView.setProgressBar(R.id.update_download_progress, length, off, false);
		        notif.contentView.setTextViewText(R.id.update_progress_text, text);
		        PendingIntent pIntent = PendingIntent.getActivity(c, 0, new Intent(), 0);
				notif.contentIntent = pIntent;
		        if(notif.flags != Notification.FLAG_NO_CLEAR)
		        {
		        	nm.notify(0, notif);
			        if(waitDialog != null)
			        {
			        	handler.post(new Runnable(){
							@Override
							public void run() {
								waitDialog.setMessage(text);
							}});
			        }
		        }
			}}, 1000, 1000);
        new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					fd.downloadFile(url, apkFile);

					// 通过Intent安装APK文件
			        Intent i = new Intent(Intent.ACTION_VIEW);
			        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");

					//通知已下载完成
					notif.flags = Notification.FLAG_NO_CLEAR;
					notif.contentView.setProgressBar(R.id.update_download_progress, fd.getLength(), fd.getLength(), false);
					String text = "更新包下载完成，点击安装。";
					notif.contentView.setTextViewText(R.id.update_progress_text, text);
					PendingIntent pIntent = PendingIntent.getActivity(c, 0, i, 0);
					notif.contentIntent = pIntent;
					nm.notify(0, notif);

					//如有等待对话框时
					if(waitDialog != null)
			        {
			        	handler.post(new Runnable(){
							@Override
							public void run() {
								waitDialog.setMessage("更新包下载完成，点击通知栏安装");
							}});
			        }

			        c.startActivity(i);
				} catch (IOException e) {
					e.printStackTrace();
					handler.post(new Runnable(){
						@Override
						public void run() {
							Toast.makeText(c, "更新包下载失败，请稍候重试。", Toast.LENGTH_LONG).show();
							if(waitDialog != null)
							{
								waitDialog.setMessage("更新包下载失败，请稍候重试。");
								waitDialog.setCancelable(true);
								waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
									@Override
									public void onCancel(DialogInterface dialog) {
										System.exit(0);
									}
								});
//								waitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new OnClickListener(){
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										System.exit(0);
//									}});
							}
						}});
					notif.flags = Notification.FLAG_NO_CLEAR;
					nm.cancel(0);
					downloading = false;
				}
				timer.cancel();
			}}).start();

        Toast.makeText(c, "开始下载更新包", Toast.LENGTH_LONG).show();
        downloading = true;
        return true;
	}

	/**
	 * 下载apk并安装
	 * @return 成功开始下载后返回true  false表示无法开始下载
	 */
	public static boolean downloadApk(final View v, String downloadURL)
	{
		final Context c = v.getContext();
		if(downloading)
		{
			Toast.makeText(c, "下载中，请稍候..", Toast.LENGTH_LONG).show();
			return false;
		}

		final Handler handler = new Handler();

		// 判断SD卡是否存在，并且是否具有读写权限
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(c, "SD卡不可用", Toast.LENGTH_LONG).show();
			return false;
		}
		File savePath = new File(Environment.getExternalStorageDirectory(), save_path);
		final File apkFile = new File(savePath, apk_filename);

		final URL url;
		try {
			url = new URL(downloadURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Toast.makeText(c, "下载地址错误", Toast.LENGTH_LONG).show();
			return false;
		}

		final FileDownloader fd = new FileDownloader();


		View win = LayoutInflater.from(c).inflate(R.layout.dialog_upgrade_progess, null);
		final PopupWindow popupWindow = new PopupWindow(win,
				AbsoluteLayout.LayoutParams.MATCH_PARENT, AbsoluteLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

		final NumberSeekBar bar = (NumberSeekBar)win.findViewById(R.id.bar_download);
		bar.setTextColor(0xffffffff);
		bar.setTextSize(Util.spToPx(13, c.getResources()));

		win.findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		final Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			private int pre = 0;
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						int length = fd.getLength();
						int off = fd.getOff();
						Log.d("fff","length   "+length+"  off "+off);
						if(length <= 0)
							bar.setProgress(0);
						else
							bar.setProgress(off* 100/length);
					}
				});

			}}, 1000, 1000);
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					fd.downloadFile(url, apkFile);

					// 通过Intent安装APK文件
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
					c.startActivity(i);
				} catch (IOException e) {
					e.printStackTrace();
					handler.post(new Runnable(){
						@Override
						public void run() {
							Toast.makeText(c, "更新包下载失败，请稍候重试。", Toast.LENGTH_LONG).show();
						}});
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						popupWindow.dismiss();
					}});
				downloading = false;
				timer.cancel();
			}}).start();

//		Toast.makeText(c, "开始下载更新包", Toast.LENGTH_LONG).show();
		downloading = true;
		return true;
	}
}
