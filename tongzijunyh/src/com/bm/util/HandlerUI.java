package com.bm.util;

import java.util.HashMap;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.bm.app.App;


public class HandlerUI {

	
	
	public static final String systemWidth = "width";
	public static final String systemHeight = "height";
	private static HashMap<String, Integer> map;

	
	
	public static float getWidthRoate(int width) {
		if (map == null) {
			map = new HashMap<String, Integer>();
			Display display = ((WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			map.put(systemWidth, screenWidth);
			map.put(systemHeight, screenHeight);
		}
		return (map.get(systemWidth) * 1.00f) / width;
	}
}
