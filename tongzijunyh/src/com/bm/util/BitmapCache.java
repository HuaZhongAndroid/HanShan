package com.bm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache {

	
	/** 缓存空间大小15MB. */
	public static int cacheSize = 8 * 1024 * 1024; 
	 /**等待中的线程*/
    private static final List<HashMap<String, Runnable>> waitRunnableList = new ArrayList<HashMap<String, Runnable>>();
    /**正在下载中的线程*/
    private static final HashMap<String, Runnable> runRunnableCache = new HashMap<String, Runnable>();
	 /**锁对象*/
    public static final ReentrantLock lock = new ReentrantLock();
	private static LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(cacheSize){
		protected int sizeOf(String key, Bitmap bitmap) { 
			return bitmap.getRowBytes() * bitmap.getHeight();        
	    }

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			//if(D) Log.d(TAG, "LruCache:移除了"+key);
		}
	};
	
	
	/**
	 * 描述：从缓存中获取这个Bitmap.
	 *
	 * @param key the key
	 * @return the bitmap from mem cache
	 */
	public  static Bitmap getBitmapFromCache(String key) {  
		  return bitmapCache.get(key); 
		  
	} 
	
	
	/**
	 * 描述：从缓存删除.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeBitmapFromCache(String key){
		try {
			lock.lock();
			if (getBitmapFromCache(key) != null) {  
				bitmapCache.remove(key);
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			lock.unlock();
		}
	}
	
	/**
	 * 描述：清空缓存的Bitmap.
	 */
	public  static void removeAllBitmapFromCache() { 
		  bitmapCache.evictAll();  
	}
	
	/**
	 * 描述：从缓存删除一个等待线程.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeWaitRunnableFromCache(String key){
		try {
			lock.lock();
			for(int i=0;i<waitRunnableList.size();i++){
				HashMap<String, Runnable> runnableMap = waitRunnableList.get(i);
				Runnable runnable = runnableMap.get(key);
				if (runnable != null) {  
					//if(D) Log.d(TAG, "从缓存删除并唤醒:"+runnable);
					synchronized(runnable){
					   runnable.notify();
					}
					waitRunnableList.remove(runnableMap);
					i--;
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	
	/**
	 * 描述：从缓存一个正在执行的线程.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeRunRunnableFromCache(String key){
		if (getRunRunnableFromCache(key) != null) {  
			runRunnableCache.remove(key);
		}  
	}
	  /**
		 * 描述：从缓存中获取这个正在执行线程.
		 *
		 * @param key the key
		 * @return the runnable
		 */
		public  static Runnable getRunRunnableFromCache(String key) {  
			  return runRunnableCache.get(key);  
		}
}
