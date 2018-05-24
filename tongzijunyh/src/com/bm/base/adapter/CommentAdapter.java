package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Comment;
import com.bm.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 家长评论适配器
 * @author shiyt
 *
 */
public class CommentAdapter  extends BaseAd<Comment>{
	public CommentAdapter(Context context,List<Comment> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_comment, null);
			itemView.iv_sixview_head = (ImageView)convertView.findViewById(R.id.iv_sixview_head);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			itemView.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		Comment entity= mList.get(position);
		ImageLoader.getInstance().displayImage(entity.replyAvatar, itemView.iv_sixview_head,App.getInstance().getGrayHeadImage());
		itemView.tv_time.setText(Util.toString(getNullData(entity.replyTime),"yyyy-MM-dd HH:mm:ss","yyyy.MM.dd HH:mm"));//时间
		itemView.tv_content.setText(getNullData(entity.replyContent));//评论内容
		itemView.tv_name.setText(getNullData(entity.replyUserName));//评论人名称
		return convertView;
	}
	class ItemView{
		private ImageView iv_sixview_head;
		private TextView tv_name,tv_content,tv_time;
	}
}
