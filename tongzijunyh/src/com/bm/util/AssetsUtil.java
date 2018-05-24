package com.bm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;

public class AssetsUtil {

	public static String getTxtFromAssets(Context ct, String fileName){ 
        try { 
             InputStreamReader inputReader = new InputStreamReader(ct.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            StringBuilder result=new StringBuilder();
            while((line = bufReader.readLine()) != null)
                result.append(line).append('\n');
            return result.toString();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        
        return null;
	} 
}
