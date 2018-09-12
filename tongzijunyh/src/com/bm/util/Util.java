package com.bm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bm.app.App;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.richer.tzj.R;

public class Util {

	

	  private static String FirstFolder="leju";//一级目录  
	  public final static String ROOTPATH=Environment.getExternalStorageDirectory()+File.separator+FirstFolder+File.separator;  
	 
	  /**
		 * 字符转转日期
		 * 
		 * @throws ParseException
		 */
		public static String toString(String str, String oldStyle, String NewStyle) {
			if(TextUtils.isEmpty(str)){
				return "";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(NewStyle);
			SimpleDateFormat format = new SimpleDateFormat(oldStyle);
			try {
				return dateFormat.format(format.parse(str));
			} catch (ParseException e) {
				e.printStackTrace();
				return "";
			}

		}
	  
	  
	  
	  /**
		 * Convert Dp to Pixel
		 */
		public static int dpToPx(float dp, Resources resources){
			float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
			return (int) px;
		}
	/**
	 * Convert sp to Pixel
	 */
	public static int spToPx(float sp, Resources resources){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
		return (int) px;
	}
	  
	  /**
	   *密码
	   * @return
	   */
	  public static boolean isPwd(String pwd) {
		   int length = pwd.length();
		   if(length>=6&& length<20){
			   return true;
		   }else{
			   return false;
		   }
		  }
	  
	
	  /**
	   * 手机验证
	   * @param mobiles
	   * @return
	   */
	  public static boolean isMobileNO(String mobiles) {
//		  Pattern p = Pattern
//		  .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		  Pattern p = Pattern
					.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		  Matcher m = p.matcher(mobiles);

		  return m.matches();
		  }
	  
	  /**
	   * 手机验证
	   * @param mobiles
	   * @return
	   */
//	  public static boolean isMobileNO(String mobiles) {
//	    boolean flag = false;
//	    try {
//	        //13********* ,15********,18*********
//	        Pattern p = Pattern
//	                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//	        Matcher m = p.matcher(mobiles);
//	        flag = m.matches();
//	    } catch (Exception e) {
//	        flag = false;
//	    }
//	    return flag;
//       }
	  
	    /**
	     * 验证邮政编码  
	     * @param post
	     * @return
	     */
	    public static boolean checkPost(String post){  
	        if(post.matches("[1-9]\\d{5}(?!\\d)")){  
	            return true;  
	        }else{  
	            return false;  
	        }  
	    } 
	    
	    /**
		 * 获取设备id
		 * @return
		 */
		public static String getIMEI(Context context){
			TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String m_szImei = TelephonyMgr.getDeviceId(); 
			return m_szImei;
		}
		
		/**
		 * 获取app版本号
		 * @param context
		 * @return
		 */
		public static String getAppVersion(Context context)
		{
			try {
				PackageManager manager = context.getPackageManager();
				PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				String version = info.versionName;
				return version;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	  
	  /**
	   * 是否为视频频
	   * @param suffix
	   * @return
	   */
	  public static boolean fileIsVideo(String suffix) {
			String[] fileIsAudios = new String[] { ".3gp", ".avi", ".mp4", ".mpeg",
					".mpe", ".mpg4", ".rmvb", ".3gp" };
			for (String s : fileIsAudios) {
				if (suffix.toLowerCase().endsWith(s)) {
					return true;
				}
			}
			return false;
		}

	  /**
	   * 是否为音频
	   * @param suffix
	   * @return
	   */
		public static boolean fileIsAudio(String suffix) {
			String[] fileIsAudios = new String[] { ".amr", ".mp3", ".ogg", ".mp2",
					".m3u", ".m4a", ".m4b", ".m4p", ".wav", ".wma", ".wmv" };
			for (String s : fileIsAudios) {
				if (suffix.toLowerCase().endsWith(s)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * 是否为图片
		 */
		public static boolean fileIsImage(String suffix) {
			String[] fileIsAudios = new String[] { ".jpg", ".png", ".jpeg" };
			for (String s : fileIsAudios) {
				if (suffix.toLowerCase().endsWith(s)) {
					return true;
				}
			}
			return false;
		}
		
		
		/**
		 * 
		 * @param in  要拷贝文件的流
		 * @param dest  拷贝后文件保存的路径
		 * @return
		 */
		public static boolean copyfile(InputStream in, File dest){
			try{
				OutputStream out = new FileOutputStream(dest);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				System.out.println("文件复制成功.");
				return true;
			}catch(FileNotFoundException ex){
				System.out.println("文件复制失败:"+ex.getMessage() + " in the specified directory.");
			}catch(IOException e){
				System.out.println("文件复制失败:"+e.getMessage());			
			}
			return false;
		}
		
	  /**
	   * 创建系统文件夹，用来存放图片等 
	   * @param context
	   * @return
	   */
	   public static boolean isCreateRootPath(Context context){
	 	  boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);  
	 	     if(!sdCardExist)  {//如果不存在SD卡，进行提示  
	 	          Toast.makeText(context, "请插入外部SD存储卡", Toast.LENGTH_SHORT).show();  
	 	          return false;
	 	     }else{//如果存在SD卡，判断文件夹目录是否存在  
	 	         File dirFirstFile=new File(ROOTPATH);//新建一级主目录  
	 	         if(!dirFirstFile.exists()){//判断文件夹目录是否存在  
	 	              dirFirstFile.mkdir();//如果不存在则创建  
	 	         }  
	 	        return true;
	 	     }  
	   }
	   public static InputStream openStream(Context context, Uri uri){
			InputStream is = null;
			try {
				is = context.getContentResolver().openInputStream(uri);
				Log.i("xx", "文件流打开成功，路径="+uri.getPath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return is;
		}
	
	   public static void copyfile(InputStream in, String dtFile){
			try{
				//File f1 = new File(srFile);
				File f2 = new File(dtFile);
				OutputStream out = new FileOutputStream(f2);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				System.out.println("File copied.");
			}catch(FileNotFoundException ex){
				System.out.println(ex.getMessage() + " in the specified directory.");
				System.exit(0);
			}
			catch(IOException e){
				System.out.println(e.getMessage());			
			}
		}
	   /**
		 * 根据uris获取路径
		 * @param context
		 * @param uri
		 * @return
		 */
		public static String getImagePathFromUriPath(Context context, Uri uri)
	    {
			ContentResolver resolver = context.getContentResolver();
	       String fileName = null;
	       Uri filePathUri = uri;
	       if (uri != null)
	       {
	           if (uri.getScheme().toString().compareTo("content") == 0)
	           {
	               // content://开头的uri
	              Cursor cursor = resolver.query(uri, null, null, null, null);
	              if (cursor != null && cursor.moveToFirst())
	              {
	                  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	                  fileName = cursor.getString(column_index); // 取出文件路径
	 
	                  // Android 4.1 更改了SD的目录，sdcard映射到/storage/sdcard0
	                  if (!fileName.startsWith("/storage") && !fileName.startsWith("/mnt"))
	                  {
	                     // 检查是否有”/mnt“前缀
	                     fileName = "/mnt" + fileName;
	                  }
	                  cursor.close();
	              }
	           }
	           else if (uri.getScheme().compareTo("file") == 0) // file:///开头的uri
	           {
	              fileName = filePathUri.toString();// 替换file://
	              fileName = filePathUri.toString().replace("file://", "");
	              int index = fileName.indexOf("/sdcard");
	              fileName  = index == -1 ? fileName : fileName.substring(index);
	              if (!fileName.startsWith("/mnt"))
	              {
	                  // 加上"/mnt"头
	                 // fileName += "/mnt";
	              }
	           }
	       }
	       return fileName;
	    }

		

	/**
	 * imageloder 防止图片混乱显示，在listview等控件中
	 */
	public static DisplayImageOptions imageLoderAppInit() {
		DisplayImageOptions listViewDisplayImageOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.image_loading)
				.showImageForEmptyUri(R.drawable.image_loading)
				.showImageOnFail(R.drawable.image_loading).cacheInMemory()
				.cacheOnDisc().build();
		return listViewDisplayImageOptions;
	}

	/**
	 * 将时间unix转换为int类型
	 * 
	 * @param timeString
	 * @param format
	 * @return
	 */
	public static String getDateToString(String timeString) {
		String time = timeString.replace(":", "-");
		return time;
	}

	public static String getDateToStringShu(String timeString) {
		String time = timeString.replace(":", "/");
		return time;
	}

	/**
	 * 时间分隔符-换成。
	 * 
	 * @param timeString
	 * @return
	 */
	public static String getDateToStringDian(String timeString) {
		String time = timeString.replace("-", ".");
		return time;
	}

	/**
	 * 、转换时间
	 * 
	 * @param timeString
	 * @param format
	 * @return
	 */
	public static String getParseDateString(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		time = sdf.format(new Date());
		return time;
	}

	
	/**
	 * 、转换时间
	 * 
	 * @param timeString
	 * @param format
	 * @return
	 */
	public static String getParseDate(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//time = ;
		return sdf.format(time);
	}

	/**
	 * 按指定格式转换为字符串
	 * @param time
	 * @param geshi
     * @return
     */
	public static String getStringDate(Date time, String geshi)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(geshi);
		return sdf.format(time);
	}

	public static Date StringToDate(String time, String geshi)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(geshi);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 取得当前时间类型
	 * 
	 * @param timeString
	 * @param format
	 * @return
	 */
	public static String getCurrentDateString() {
		String time = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		time = sdf.format(new Date());
		return time;
	}

	public static String getCurrentDateStr() {
		String time = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
		time = sdf.format(new Date());
		return time;
	}

	public static boolean isNull(String str) {
		if ("".equals(str) && str == null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 计算金额
	 * 
	 * @param argStr
	 * @return
	 */
	public static String getFloatDotStr(String argStr) {
		float arg = Float.valueOf(argStr);
		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(arg);
	}

	/**
	 * 计算绘制界面UIbutton
	 * 
	 * @param view
	 */
	public static void resetRL(View... view) {
		float rote = HandlerUI.getWidthRoate(App.getInstance().getMode_w());
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2
					.getLayoutParams();
			layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
			layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
			layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
			layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	public static void resetRLBack(View... view) {
		float rote = HandlerUI.getWidthRoate(App.getInstance().getMode_w());
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view2
					.getLayoutParams();
			layoutParams.height = (int) (layoutParams.height * rote);
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 计算绘制界面UIbutton
	 * 
	 * @param view
	 */
	public static void resetLL(View... view) {
		float rote = HandlerUI.getWidthRoate(App.getInstance().getMode_w());
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2
					.getLayoutParams();
			layoutParams.leftMargin = (int) (layoutParams.leftMargin * rote);
			layoutParams.rightMargin = (int) (layoutParams.rightMargin * rote);
			layoutParams.topMargin = (int) (layoutParams.topMargin * rote);
			layoutParams.bottomMargin = (int) (layoutParams.bottomMargin * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	public static void resetLLBack(View... view) {
		float rote = HandlerUI.getWidthRoate(App.getInstance().getMode_w());
		if (view == null || rote == 1) {
			return;
		}
		for (View view2 : view) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view2
					.getLayoutParams();
			layoutParams.height = (int) (layoutParams.height * rote);
			layoutParams.width = (int) (layoutParams.width * rote);
			view2.setLayoutParams(layoutParams);
		}
	}

	/**
	 * 时间字符串转换成毫秒
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTime(String date) {
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mdate = null;
		long beginTime = 0;
		try {
			mdate = sDate.parse(date);
			beginTime = mdate.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beginTime;

	}
	
	  /**
     * 设置时间
     * @param timeString
     * @return
     */
    public static String setDate(String timeString){
		String year="";
		String mouth="";
		String day="";
		String[] str=timeString.split("-");
		year=str[0];
		mouth=str[1];
		day=str[2];
		if(Integer.valueOf(mouth)<10)mouth="0"+mouth;
		if(Integer.valueOf(day)<10)day="0"+day;
		
		return year+"-"+mouth+"-"+day;
		
	}
    
	
	/**
	* 获取两个日期之间的间隔天数
	* @return
	*/
	public static int comparisonTime(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date_start = null;
		Date date_end = null;
		try {
			date_start= sdf.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			date_end= sdf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	        Calendar fromCalendar = Calendar.getInstance(); 
	        fromCalendar.setTime(date_start); 
	        fromCalendar.set(Calendar.HOUR_OF_DAY, 0); 
	        fromCalendar.set(Calendar.MINUTE, 0); 
	        fromCalendar.set(Calendar.SECOND, 0); 
	        fromCalendar.set(Calendar.MILLISECOND, 0); 
	 
	        Calendar toCalendar = Calendar.getInstance(); 
	        toCalendar.setTime(date_end); 
	        toCalendar.set(Calendar.HOUR_OF_DAY, 0); 
	        toCalendar.set(Calendar.MINUTE, 0); 
	        toCalendar.set(Calendar.SECOND, 0); 
	        toCalendar.set(Calendar.MILLISECOND, 0); 
	 
	        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
	
	
	
	/**
	 * 
	 * 但传递数据位知识库的时候，返回富文本数据，不是url
	 */
	public static void initViewWebData(WebView webview, String data) {
		// String msg =
		// "<p>强总测试</p><p><img src=\"http://10.58.128.166:8080/bluemobi/richtext/14/1009/96971412825309233.jpg\" alt=\"\" /><br /></p><p>测试结束</p>";
		Document d = Jsoup.parse(data);
		Elements imgs = d.getElementsByTag("img");
		for (int i = 0; i < imgs.size(); i++) {
			Element e = imgs.get(i);
			e.attr("style", "");
		}
		webview.setVisibility(View.VISIBLE);
		d.head()
				.append("<style type=\"text/css\">img{ width:100%; height:auto; }</style>");
		webview.loadDataWithBaseURL(null, d.html(), "text/html", "utf-8", null);

	}
	
	
	/**
	 * 
	 *  将list按blockSize大小等分，最后多余的单独一份 
	 * @param list
	 * @param blockSize
	 * @return
	 */
    public static <T> List<List<T>> subList(List<T> list, int blockSize) {  
        List<List<T>> lists = new ArrayList<List<T>>();  
        if (list != null && blockSize > 0) {  
            int listSize = list.size();  
            if(listSize<=blockSize){  
                lists.add(list);  
                return lists;  
            }  
            int batchSize = listSize / blockSize;  
            int remain = listSize % blockSize;  
            for (int i = 0; i < batchSize; i++) {  
                int fromIndex = i * blockSize;  
                int toIndex = fromIndex + blockSize;  
                System.out.println("fromIndex=" + fromIndex + ", toIndex=" + toIndex);  
                lists.add(list.subList(fromIndex, toIndex));  
            }  
            if(remain>0){  
                System.out.println("fromIndex=" + (listSize-remain) + ", toIndex=" + (listSize));  
                lists.add(list.subList(listSize-remain, listSize));  
            }  
        }  
        return lists;  
    }  
    
    
    public static String toNumber(String pattern,Float str){
		 DecimalFormat decimalFormat =new DecimalFormat(pattern);//构造方法的字符格式这里如果小数不足2位,会以0补足.
	     String distanceString = decimalFormat.format(str);//format 返回的是字符串
		return distanceString;
	}
    
    

    /** 保存方法 */
    public static boolean saveBitmap(String path, Bitmap bm) {
     File f = new File("/sdcard/"+path, System.currentTimeMillis()+".jpg");
     if(!f.getParentFile().exists())
     {
    	 f.getParentFile().mkdirs();
     }
     if (f.exists()) {
    	 f.delete();
     }
     try {
      FileOutputStream out = new FileOutputStream(f);
      bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
      Log.i("fffff", "已经保存");
      return true;
     } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
     } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
     }

     return false;
    }

	
	
}
