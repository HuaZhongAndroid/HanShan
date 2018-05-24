package com.bm.base.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.PlayMateList;
import com.bm.tzj.activity.ImageViewActivity;
import com.bm.tzj.kc.GrowthCenterAc;
import com.bm.tzj.ts.ExploreDetailAc;
import com.bm.util.Util;
import com.lib.widget.FuGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 探索适配器
 * @author jiangsh
 *
 */
@SuppressLint("NewApi")
public class FindAdapter  extends BaseAd<PlayMateList>{
	private PicAdapter picAdapter;
	private String tag;//标记是 1 玩伴儿还是探索2
	String [] paths;
	private String[] picArr = new String[]{
			"http://www.totalfitness.com.cn/upfile/681x400/20120323104125_324.jpg",
			"http://d.hiphotos.baidu.com/zhidao/pic/item/d31b0ef41bd5ad6e1e7e401f83cb39dbb7fd3cbb.jpg",
			"http://elegantliving.ceconline.com/TEAMSITE/IMAGES/SITE/ES/20080203_EL_ES_02_02.jpg"};
	
	
	List<PlayMateList> prolist;
	
	public FindAdapter(Context context,List<PlayMateList> prolist,String tag){
		setActivity(context, prolist);
		this.prolist = prolist;
		this.tag=tag;
	}
	private OnSeckillClick onSeckillClick;
	public void setOnSeckillClick(OnSeckillClick onSeckillClick){
		this.onSeckillClick = onSeckillClick;
	}
	public void notifu(String tag){
		this.tag=tag;
		notifyDataSetChanged();
	}
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.fm_find_list_item, null);
			itemView.tv_exploreName = (TextView)convertView.findViewById(R.id.tv_exploreName);
			itemView.tv_exploreImg = (ImageView)convertView.findViewById(R.id.tv_exploreImg);
			itemView.tv_exploreTime = (TextView)convertView.findViewById(R.id.tv_exploreTime);
			itemView.tv_explore_content = (TextView)convertView.findViewById(R.id.tv_explore_content);
			itemView.gv_explore_pic= (FuGridView)convertView.findViewById(R.id.gv_explore_pic);
			itemView.tv_explore_chart = (TextView)convertView.findViewById(R.id.tv_explore_chart);
			itemView.tv_explore_zan = (TextView)convertView.findViewById(R.id.tv_explore_zan);
			itemView.tv_explore_count = (TextView)convertView.findViewById(R.id.tv_explore_count);
			itemView.lv_listReplay = (ListView)convertView.findViewById(R.id.lv_listReplay);
			itemView.ll_jb=(LinearLayout) convertView.findViewById(R.id.ll_jb);
			itemView.img_zan = (ImageView)convertView.findViewById(R.id.img_zan);
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final PlayMateList entity= prolist.get(position);
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.tv_exploreImg,App.getInstance().getGrayHeadImage());
		itemView.tv_exploreName.setText(getNullData(entity.nickname));// 
		itemView.tv_exploreTime.setText(Util.toString(getNullData(entity.postDate), "yyyy-MM-dd HH:mm:ss",  "yyyy.MM.dd HH:mm"));//时间
		itemView.tv_explore_content.setText(getNullData(entity.articleContent));//评论内容
		itemView.tv_explore_zan.setText("赞  ("+getNullData(entity.praiseCount)+")");//点赞人数
		itemView.tv_explore_count.setText("评价  ("+getNullData(entity.replyCount)+")");//评论数
		
		if(entity.isArticleLaud.equals("1")){
			itemView.img_zan.setBackground(context.getResources().getDrawable(R.drawable.explore_zanname));
		}else{
			itemView.img_zan.setBackground(context.getResources().getDrawable(R.drawable.explore_zan));	
		}
		
		paths=new String[entity.path.size()];
		for (int i = 0; i < entity.path.size(); i++) {
			paths[i]=entity.path.get(i).titleMultiUrl;
		}
		
		if(tag.equals("1")){
			itemView.ll_jb.setVisibility(View.GONE);
		}else{
			itemView.ll_jb.setVisibility(View.VISIBLE);
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
				Intent intent = new Intent(context,
						ImageViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("images", paths);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context,ExploreDetailAc.class);
				intent.putExtra("type", tag);
				intent.putExtra("id", entity.articleId);
				intent.putExtra("strType", "FindFm");
				context.startActivity(intent);
			}
		});
		
		itemView.tv_explore_chart.setOnClickListener(new OnClickListener() {//举报
			
			@Override
			public void onClick(View v) {
				onSeckillClick.onSeckillClick(position,1);
			}
		});
//		itemView.tv_explore_count.setOnClickListener(new OnClickListener() {//留言
//			
//			@Override
//			public void onClick(View v) {
//				onSeckillClick.onSeckillClick(position,3);
//			}
//		});
		itemView.tv_explore_zan.setOnClickListener(new OnClickListener() {//点赞
			
			@Override
			public void onClick(View v) {
				onSeckillClick.onSeckillClick(position,2);
			}
		});
		
		itemView.tv_exploreImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toGrowthCenterAc(entity);
			}
		});
		itemView.tv_exploreName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toGrowthCenterAc(entity);
			}
		});
		
		return convertView;
	}
	
	private void toGrowthCenterAc(PlayMateList entity)
	{
		Intent intent = new Intent(context,GrowthCenterAc.class);
		intent.putExtra("pageType","FindFm");
		intent.putExtra("friendId",getNullData(entity.postUserId));
		intent.putExtra("friendBabyId",getNullData(entity.babyid));
		context.startActivity(intent);
	}
	
	class ItemView{
		private TextView tv_exploreName,tv_exploreTime,tv_explore_content,tv_explore_chart,tv_explore_zan,tv_explore_count;
		private ImageView tv_exploreImg,img_zan;
		private FuGridView gv_explore_pic;
		private ListView lv_listReplay;
		private LinearLayout ll_jb;
	
	}
	public interface OnSeckillClick{
		void onSeckillClick(int position,int tag);//1为举报，2为点赞，3为留言
	}
}
