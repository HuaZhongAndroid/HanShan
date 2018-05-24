package com.bm.im.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 我的群适配器
 * @author shiyt
 *
 */
public class MyGroupAdapter  extends BaseAd<User>{
	private String tag;
	public MyGroupAdapter(Context context,List<User> prolist,String tag){
		setActivity(context, prolist);
		this.tag=tag;
	}
	
	@Override
	protected View setConvertView(View convertView, int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_group, null);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.img_head = (ImageView)convertView.findViewById(R.id.img_head);
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		User entity= mList.get(position);
		if("group".equals(tag)){
			itemView.tv_name.setText(entity.groupName);
			if(TextUtils.isEmpty(entity.groupPic)){
				itemView.img_head.setImageResource(R.drawable.ease_groups_icon);
			}else{
				ImageLoader.getInstance().displayImage(entity.groupPic, itemView.img_head,App.getInstance().getGrayHeadImage());
			}
		}else{
			itemView.tv_name.setText(entity.nickname);
			ImageLoader.getInstance().displayImage(entity.avatar, itemView.img_head,App.getInstance().getGrayHeadImage());
		}
		
		
		return convertView;
	}
	class ItemView{
		private TextView tv_name;
		private ImageView img_head;
	
	}
}
