package com.bm.util;

/**
 * 加密工具类
 * Created on 2013-8-26 下午5:28:00
 * @author ystu
 */
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class MD5Util {
	
	public static String md5(String str) {
		StringBuilder sign = new StringBuilder();
		try {
			byte[] bytes = null;
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				bytes = md5.digest(str.toString().getBytes("UTF-8"));
			} catch (GeneralSecurityException ex) {
				throw new IOException(ex);
			}
			// 将MD5输出的二进制结果转换为小写的十六进制

			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(bytes[i] & 0xFF);
				if (hex.length() == 1) {
					sign.append("0");
				}
				sign.append(hex);
			}
		} catch (Exception e) {
			e.printStackTrace();
			sign = new StringBuilder("error:" + str);
		}
		return sign.toString();
	}
	
	public static void main(String[] args) {
		//北京=692e92669c0ca340eff4fdcef32896ee
		System.out.println(MD5Util.md5("北京"));
	}

}