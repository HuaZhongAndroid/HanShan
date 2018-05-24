package com.bm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * 文件下载器  线程不安全的
 */
public class FileDownloader {

	private int length;
	
	private int off;
	
	private boolean cancelState;

	HttpURLConnection conn = null;
	
	/**
	 * 从指定地址下载文件,该方法会一直阻塞直到文件下载完成或发生异常
	 * @param url 下载地址
	 * @param saveFile 保存文件
	 * @throws IOException
	 */
	public void downloadFile(URL url,File saveFile) throws IOException
	{
		InputStream is = null;
		FileOutputStream fos = null;
		
		try{
			cancelState = false;
			// 创建连接  
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(300000);
	        conn.setReadTimeout(300000);// 设置超时时间  
	//        conn.setRequestMethod("GET");
	//        conn.setRequestProperty("Charser", "GBK,utf-8;q=0.7,*;q=0.3");  
	        // 获取文件大小
	        length = conn.getContentLength();
	        // 创建输入流  
	        is = conn.getInputStream();  
	
	        // 判断文件目录是否存在  
	        if (!saveFile.getParentFile().exists()) {  
	        	saveFile.getParentFile().mkdirs();
	        } 
	        fos = new FileOutputStream(saveFile);  
	        
	        byte[] b = new byte[1024];
	        int len;
	        off = 0;
	        while ((len = is.read(b)) != -1) {
	        	if(cancelState)
	        		break;
	        	fos.write(b, 0, len);
	        	off += len;
	        }
	        
	        if(cancelState)
	        	Log.d("filedownloader", "删除文件："+saveFile.delete());
		}
		catch(IOException e)
		{
			Log.d("filedownloader", "下载文件时异常");
			Log.d("filedownloader", "删除文件："+saveFile.delete());
			throw e;
		}
		finally
		{
			if(conn != null)
				conn.disconnect();
			if(is != null)
				is.close();
			if(fos != null)
				fos.close();
		}
	}

	/**
	 * 取消当前的下载
	 */
	public void cancelDownload()
	{
		cancelState = true;
	}
	
	/**
	 * 下载是否被取消
	 * @return
	 */
	public boolean isCancelState() {
		return cancelState;
	}

	/**
	 * 返回下载文件的总字节数
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 返回当前已下载的字节数
	 * @return
	 */
	public int getOff() {
		return off;
	}
}
