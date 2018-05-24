package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.PlayMateList;
import com.bm.tzj.activity.ImageViewActivity;
import com.bm.tzj.mine.MyExploreAc;
import com.bm.tzj.ts.ExploreDetailAc;
import com.bm.util.Util;
import com.lib.widget.FuGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 探索适配器
 * @author wanghy
 *
 */
public class MyExploreAdapter  extends BaseAd<PlayMateList>{
	private PicAdapter picAdapter;
	String [] paths;
	Handler handler;
	private String[] picArr = new String[]{
			"http://www.totalfitness.com.cn/upfile/681x400/20120323104125_324.jpg",
			"http://d.hiphotos.baidu.com/zhidao/pic/item/d31b0ef41bd5ad6e1e7e401f83cb39dbb7fd3cbb.jpg",
			"http://elegantliving.ceconline.com/TEAMSITE/IMAGES/SITE/ES/20080203_EL_ES_02_02.jpg"};
	
	
	public MyExploreAdapter(Context context,List<PlayMateList> prolist,Handler strHandler){
		setActivity(context, prolist);
		this.handler = strHandler;
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_myexplore, null);
			itemView.tv_exploreName = (TextView)convertView.findViewById(R.id.tv_exploreName);
			itemView.tv_exploreImg = (ImageView)convertView.findViewById(R.id.tv_exploreImg);
			itemView.tv_exploreTime = (TextView)convertView.findViewById(R.id.tv_exploreTime);
			itemView.tv_explore_content = (TextView)convertView.findViewById(R.id.tv_explore_content);
			itemView.gv_explore_pic= (FuGridView)convertView.findViewById(R.id.gv_explore_pic);
			itemView.tv_explore_chart = (TextView)convertView.findViewById(R.id.tv_explore_chart);
			itemView.tv_explore_zan = (TextView)convertView.findViewById(R.id.tv_explore_zan);
			itemView.tv_explore_count = (TextView)convertView.findViewById(R.id.tv_explore_count);
			itemView.lv_listReplay = (ListView)convertView.findViewById(R.id.lv_listReplay);
			itemView.tv_exploreDel = (TextView)convertView.findViewById(R.id.tv_exploreDel);
			itemView.img_zan = (ImageView)convertView.findViewById(R.id.img_zan);
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final PlayMateList entity= mList.get(position);
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.tv_exploreImg,App.getInstance().getGrayHeadImage());
		itemView.tv_exploreName.setText(getNullData(entity.nickname));//课程名称
		itemView.tv_exploreTime.setText(Util.toString(getNullData(entity.postDate), "yyyy-MM-dd HH:mm:ss",  "yyyy.MM.dd HH:mm"));//时间
		itemView.tv_explore_content.setText(getNullData(entity.articleContent));//评论内容
//		itemView.tv_explore_chart.setText("(30)");
		itemView.tv_explore_zan.setText("赞  ("+getNullData(entity.praiseCount)+")");//点赞人数
		itemView.tv_explore_count.setText("评价  ("+getNullData(entity.replyCount)+")");//评论数
		
		if(entity.isArticleLaud.equals("1")){
			itemView.img_zan.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.explore_zanname));
		}else{
			itemView.img_zan.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.explore_zan));	
		}
		
		paths=new String[entity.path.size()];
		for (int i = 0; i < entity.path.size(); i++) {
			paths[i]=entity.path.get(i).titleMultiUrl;
		}
		
		
		picAdapter = new PicAdapter(context,paths);
		itemView.gv_explore_pic.setAdapter(picAdapter);
		
		itemView.gv_explore_pic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				paths = new String[entity.path.size()];
				for (int i = 0; i < entity.path.size(); i++) {
					paths[i] = entity.path.get(i).titleMultiUrl;
				}
				Intent intent = new Intent(context, ImageViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("images",paths);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		
		itemView.tv_exploreDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = MyExploreAc.MYECPLORE_CLICK_STATES;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,ExploreDetailAc.class);
				intent.putExtra("type", entity.articleId);
				intent.putExtra("id", entity.articleId);
				intent.putExtra("strType", "MyExplorDetail");
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	class ItemView{
		private TextView tv_exploreName,tv_exploreTime,tv_explore_content,tv_explore_chart,tv_explore_zan,tv_explore_count,tv_exploreDel;
		private ImageView tv_exploreImg,img_zan;
		private FuGridView gv_explore_pic;
		private ListView lv_listReplay;
	
	}
}
