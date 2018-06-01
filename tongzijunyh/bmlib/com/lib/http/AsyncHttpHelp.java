package com.lib.http;

import android.content.Context;
import android.text.TextUtils;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.http.result.BaseResult;
import com.lib.log.Lg;
import com.lib.tool.JsonFormatTool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.richer.tzj.R;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class AsyncHttpHelp {

	// private static String
	// url="https://103.21.116.141:8443/huafa/app/userUnRead/getAllUnreadCount.do?type=1&cityId=310100&userId=1e55ff20a7264a0a9a3ebe02b066a5f8&";

	// private static String url =
	// "http://112.64.173.178/huafa/app/userUnRead/getAllUnreadCount.do?type=1&cityId=310100&userId=1e55ff20a7264a0a9a3ebe02b066a5f8&";

	static String userId = "";
	public static void httpGet(Context context, String url,HashMap<String, String> params,ServiceCallback callback) {
		url = BaseApi.API_URL_PRE + url;
		String logStr = new String(url);
		String urlStr = new String(url);
		urlStr+="?";
		logStr+="?";
		try {
			if(params != null){
				userId = App.getInstance().getUser().userid;
				if (!params.containsKey("userId")&&!TextUtils.isEmpty(userId)) {
					params.put("userId", userId);
				}
				
				Iterator<Entry<String, String>> it = params.entrySet().iterator();
				
				while(it.hasNext()){
					Entry<String,String> entry = it.next();
					if(entry.getValue()!=null){
					urlStr+=entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "utf-8")+"&";
					logStr+=entry.getKey()+"="+entry.getValue()+"&";
					}
				}
				params = null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Lg.e("get url:", logStr);
		AsyncHttpClient client = new AsyncHttpClient();
//		client.setTimeout(1000);
//		client.setConnectTimeout(1000);
		client.setMaxRetriesAndTimeout(2, 1000);
		client.setSSLSocketFactory(getSSL());
		callback.setUrl(url);
		client.get(context, urlStr, null, getReponHandler(callback));
	}

	public static void httpPost(Context context,String url, Map<String, String> map, String fileName, List<File> files,ServiceCallback callback) {
		url = BaseApi.API_URL_PRE + url;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		String logStr = new String(url);
		try {
			if (map != null) {
				Set<String> keys = map.keySet();
				if (!keys.contains("userId")) {
					if (!TextUtils.isEmpty(userId)) {
						params.put("userId",userId);
					}
				}
				for (Iterator<String> i = keys.iterator(); i.hasNext();) {
					String key = i.next();
					 
					logStr+=key+"="+map.get(key)+"&";
					 params.put(key,map.get(key));
//					params.put(key, URLEncoder.encode(map.get(key), "utf-8"));
				}
			}
			File[] filess=new File[files.size()];
			if (files != null) {
				for (int i = 0; i < files.size(); i++) {
					filess[i]=files.get(i);
				}
				params.put(fileName, filess);
			} 
			Lg.e("url:", url);
			Lg.e("post参数:", params.toString());
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		client.setTimeout(80*1000);
//		client.setSSLSocketFactory(getSSL());
		Lg.i("params",params.toString());
		client.post(context, url, params, getReponHandler(callback));

	}
	
	private static ResponseHandlerInterface getReponHandler(final ServiceCallback callback) {
		final BaseResult r = null;
		return new TextHttpResponseHandler() {
			
			public boolean getUseSynchronousMode() {
				return false;
			};
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				Lg.e("网络请求失败:", arg2+" url = "+callback.url);
				arg3.printStackTrace();
				callback.error("网络请求失败");
				App.toast("网络请求失败");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
//				System.out.println("返回结果:" + arg2);
				Lg.i(callback.url+"的返回结果:", "blue:"+JsonFormatTool.formatJson(arg2));
				BaseResult r = null;
				
				try {
					r = getGson().fromJson(arg2, callback.type);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (r == null
						|| (r.status != BaseResult.STATUS_SUCCESS && r.msg == null)) {
					callback.error(App.getInstance().getString(
							R.string.connect_server_failed));
				} else if (r.status != BaseResult.STATUS_SUCCESS) {
					callback.error(r.msg);
				} else {
					callback.done(0, r);
				}
			}
		};
	}

	
	/**
	 * 提供默认日期解析格式的Gson对象
	 * 
	 * @return
	 */
	public static Gson getGson() {
		return new GsonBuilder().setDateFormat(DefaultDateFormat).create();
	}
	public static final String DefaultDateFormat = "yyyy/MM/dd HH:mm:ss";
	
	

	
	/**
	 * 
	 * https  支持
	 * @return
	 */
	private static MySSLSocketFactory getSSL(){
		/// We initialize a default Keystore
				KeyStore trustStore;
				MySSLSocketFactory socketFactory = null;
				try {
					trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
					// 如需要加入证书在这里设置
					trustStore.load(null, null);
					socketFactory = new MySSLSocketFactory(trustStore);
					socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				} catch (KeyStoreException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					e.printStackTrace();
				} catch (UnrecoverableKeyException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (CertificateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return socketFactory;
	}
	
	
	
}
