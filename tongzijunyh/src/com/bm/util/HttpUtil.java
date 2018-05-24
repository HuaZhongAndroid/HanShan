package com.bm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONObject;

import android.util.Log;

import com.bm.api.BaseApi;


public class HttpUtil {

	public static interface OnResponseListener
	{
		public void onSuccess(String result);
		public void onError(String error);
	}
	
	private static synchronized void uploadFileData(final String urlPath,
			final String fileKey, final ContentBody fileBody,
			final Map<String, String> paramMap, final OnResponseListener l) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MultipartEntity multipartEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE,
							"----------ThIs_Is_tHe_bouNdaRY_$", Charset
									.defaultCharset());
					for (String key : paramMap.keySet()) {
						multipartEntity.addPart(
								key,
								new StringBody(paramMap.get(key), Charset
										.forName("UTF-8")));
					}
					multipartEntity.addPart(fileKey, fileBody);

					HttpPost request = new HttpPost(BaseApi.API_URL_PRE + urlPath);
					request.setEntity(multipartEntity);
					request.addHeader("Content-Type",
							"multipart/form-data; boundary=----------ThIs_Is_tHe_bouNdaRY_$");

					DefaultHttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 12000);
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 12000);
					HttpResponse response = httpClient.execute(request);
					int status = response.getStatusLine().getStatusCode();
					Log.d(HttpUtil.class.getSimpleName(), "状态码：" + status);
					if (status != 200)
						throw new Exception("服务器异常");
					StringBuilder result = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));
					for (String s = reader.readLine(); s != null; s = reader
							.readLine()) {
						result.append(s);
						result.append('\n');
					}

					Log.d("http", "http result    " + result);
					JSONObject json = new JSONObject(result.toString());

					boolean success = json.getInt("status") == 1;
					String message = json.getString("msg");
					String data = json.getString("data");
					if (!success)
						throw new Exception(message);

					if (l != null)
						l.onSuccess(data);

				} catch (Exception e) {
					e.printStackTrace();
					if (l != null)
						l.onError(e.getMessage());
				}
			}
		}).start();
	}
	
	/**
	 * 上传字节数组形式的文件
	 */
	public static void uploadData(String urlPath, String fileKey, String fileName, String fileMime, 
			byte[] fileData, 
			Map<String,String> paramMap, OnResponseListener l)
	{
		uploadFileData(urlPath,fileKey, new ByteArrayBody(fileData, fileMime, fileName),paramMap, l);
	}
	
	/**
	 * 上传File
	 */
	public static void uploadData(String urlPath, String fileKey, 
			File fileData, 
			Map<String,String> paramMap, OnResponseListener l)
	{
		uploadFileData(urlPath,fileKey, new FileBody(fileData),paramMap, l);
	}
	
//	/**
//	 * 上传输入流形式的文件
//	 */
//	public static void uploadData(String urlPath, String fileKey, String fileName, String fileMime, 
//			InputStream in, 
//			final Map<String,String> paramMap, final OnResponseListener l)
//	{
//		uploadFileData(urlPath,fileKey, new InputStreamBody(in,fileMime,fileName),paramMap, l);
//	}
}
