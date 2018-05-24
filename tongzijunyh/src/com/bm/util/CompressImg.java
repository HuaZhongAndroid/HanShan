package com.bm.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class CompressImg {

	
	public static Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);
		bm = rotateBitmap(bm, degree);
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	
	/** 保存方法 
	 * 
	 * 
	 * File newFile = new File(this.getCacheDir().getAbsolutePath()+System.currentTimeMillis()+".jpg");
	 * 
	 * */
	public static void saveBitmap(String oldPath,String newPath) {
		Bitmap bm = getSmallBitmap(oldPath);
		
		Log.e("TAG", "保存图片");
		File f = new File(newPath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.i("TAG", "已经保存");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}
	
	
	private static int readPictureDegree(String path) { 
        int degree  = 0; 
        try { 
                ExifInterface exifInterface = new ExifInterface(path); 
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 
                switch (orientation) { 
                case ExifInterface.ORIENTATION_ROTATE_90: 
                        degree = 90; 
                        break; 
                case ExifInterface.ORIENTATION_ROTATE_180: 
                        degree = 180; 
                        break; 
                case ExifInterface.ORIENTATION_ROTATE_270: 
                        degree = 270; 
                        break; 
                } 
        } catch (IOException e) { 
                e.printStackTrace(); 
        } 
        return degree; 
    }
	
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){ 
        if(bitmap == null) 
            return null ; 
         
        int w = bitmap.getWidth(); 
        int h = bitmap.getHeight(); 
 
        // Setting post rotate to 90  
        Matrix mtx = new Matrix(); 
        mtx.postRotate(rotate); 
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true); 
    }

	
}
