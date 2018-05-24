package com.bm.share;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.richer.tzj.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class ShareUtil {
	
	private Activity context;
	ShareModel shareModel;
	
	public ShareUtil(Activity context){
		this.context =  context;
	}

	 /**
	  * 分享开始
	  * @param context
	  * @param model
	  * @param type   0  开启分享面板分享 1.新浪 2.微信 3.微信朋友圈 4QQ  5QQ空间
	  */
      public void shareInfo(final ShareModel model,int type){
    	  UMImage image  = null;
    	  shareModel = model;
    	  switch(type){
    	  case 0:
    		  /**普通分享面板，分享相同一致内容  添加分享的平台**/
              new ShareAction(context).setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,SHARE_MEDIA.QZONE)
                     .setShareboardclickCallback(shareBoardlistener)
                     .setListenerList(umShareListener,umShareListener)
                     .setShareboardclickCallback(shareBoardlistener)
                     .open();
    		  break;
    	  case 1:
    		  new ShareAction(context).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
              .withText(shareModel.conent)
              .withTitle(shareModel.title)
              .withTargetUrl(shareModel.targetUrl)
              .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
              .share();
    		  break;
    	  case 2:
    		  new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
              .withText(shareModel.conent)
              .withTitle(shareModel.title)
              .withTargetUrl(shareModel.targetUrl)
              .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
              .share();
    		  break;
    	  case 3:
              new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
              .withText(shareModel.conent)
                .withTitle(shareModel.title)
              .withTargetUrl(shareModel.targetUrl)
              .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
              .share();
    		  break;
    		  
    	  case 4:
              new ShareAction(context).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
              .withText(shareModel.conent)
              .withTitle(shareModel.title)
              .withTargetUrl(shareModel.targetUrl)
              .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
              .share();
              break;
          case 5:
              new ShareAction(context).setPlatform(SHARE_MEDIA.QZONE).setCallback(umShareListener)
              .withText(shareModel.conent)
              .withTitle(shareModel.title)
              .withTargetUrl(shareModel.targetUrl)
              .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
              .share();
              break;
    	  }
    	  
    	  
      }
      
      
      //信息配置
      //UMImage image = new UMImage(ShareActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
      //UMImage image = new UMImage(ShareActivity.this, "http://blog.thegmic.com/wp-content/uploads/2012/05/umeng.jpg");
//      Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.info_icon_1);
//      UMImage image = new UMImage(ShareActivity.this, bitmap);
//      UMusic music = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//      music.setTitle("sdasdasd");
//      music.setThumb(new UMImage(ShareActivity.this, "http://www.umeng.com/images/pic/social/chart_1.png"));
//      UMVideo video = new UMVideo("http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");
	 
      private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
          @Override
          public void onclick(SnsPlatform snsPlatform,SHARE_MEDIA share_media) {
        	  
        	  
        	  if(share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
        		  new ShareAction(context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                  .withText(shareModel.conent)
                    .withTitle(shareModel.title)
                  .withTargetUrl(shareModel.targetUrl)
                  .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
                  .share();
        	  }else{
        		  new ShareAction(context).setPlatform(share_media).setCallback(umShareListener)
	                .withText(shareModel.conent)
	                .withTitle(shareModel.title)
	                .withTargetUrl(shareModel.targetUrl)
	                .withMedia(new UMImage(context, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launchers)))
	                .share();
        	  }
        	  
          }
      };
	
      
      

      private UMShareListener umShareListener = new UMShareListener() {
          @Override
          public void onResult(SHARE_MEDIA platform) {
              Toast.makeText(context, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onError(SHARE_MEDIA platform, Throwable t) {
              Toast.makeText(context,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onCancel(SHARE_MEDIA platform) {
              Toast.makeText(context,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
          }
      };


}
	
