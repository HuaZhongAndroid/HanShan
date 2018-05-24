package com.bm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bm.api.BaseApi;
import com.bm.entity.Dictionary;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;

public class BaseDataUtil {

	public static Map<String, Dictionary> shiHeNianLingMap;
	
	public static List<Dictionary> zhuangBeiFenLeiList;
	
	
	public static void LoadBaseData(Context context)
	{
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("code", "shiHeNianLing");
		AsyncHttpHelp.httpGet(context,BaseApi.API_dictionaryList, map, new ServiceCallback<CommonListResult<Dictionary>>(){
			@Override
			public void done(int what, CommonListResult<Dictionary> obj) {
				if(obj.data != null)
				{
					shiHeNianLingMap = new HashMap<String,Dictionary>();
					for(Dictionary d:obj.data)
					{
						shiHeNianLingMap.put(d.storevalue, d);
					}
				}
			}
			@Override
			public void error(String msg) {
			}});
		
		map.put("code", "zhuangBeiFenLei");
		AsyncHttpHelp.httpGet(context,BaseApi.API_dictionaryList, map, new ServiceCallback<CommonListResult<Dictionary>>(){
			@Override
			public void done(int what, CommonListResult<Dictionary> obj) {
				if(obj.data != null)
				{
					zhuangBeiFenLeiList = obj.data;
				}
			}
			@Override
			public void error(String msg) {
			}});
	}
	
	
	
}
