package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.CommentList;
import com.bm.tzj.kc.GrowthCenterAc;
import com.richer.tzj.R;

/**
 *探索 评论的适配器
 * @author wanghy
 *
 */
public class MyExploreReplayAdapter  extends BaseAd<CommentList>{
	public MyExploreReplayAdapter(Context context,List<CommentList> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_myexplore_replay, null);
			itemView.tv_replay_name = (TextView)convertView.findViewById(R.id.tv_replay_name);
			itemView.tv_replay_content = (TextView)convertView.findViewById(R.id.tv_replay_content);
			itemView.img_pic = (ImageView) convertView.findViewById(R.id.img_pic);
			
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final CommentList entity= mList.get(position);
		itemView.tv_replay_name.setText(getNullData(entity.replyUserName)+":");//姓名
		itemView.tv_replay_content.setText(getNullData(entity.replyContent));//内容
		
		if(position==0){
			itemView.img_pic.setVisibility(View.VISIBLE);
		}else{
			itemView.img_pic.setVisibility(View.INVISIBLE);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!getNullData(entity.userid).equals(App.getInstance().getUser().userid)){
					Intent intent = new Intent(context,GrowthCenterAc.class);
					intent.putExtra("pageType","ExploreDetailAc");
					intent.putExtra("friendId",getNullData(entity.userid));
					intent.putExtra("friendBabyId",getNullData(entity.babyid));
					context.startActivity(intent);
				}else{
					
				}
				
			}
		});
		
		
		return convertView;
	}
	class ItemView{
		private TextView tv_replay_name,tv_replay_content;
		private ImageView img_pic;
	
	}
}
