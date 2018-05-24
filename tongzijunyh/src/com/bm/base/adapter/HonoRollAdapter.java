package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 荣誉榜适配器
 * @author shiyt
 *
 */
public class HonoRollAdapter  extends BaseAd<User>{
	public HonoRollAdapter(Context context,List<User> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_honoroll, null);
			itemView.iv_sixview_head = (ImageView)convertView.findViewById(R.id.iv_sixview_head);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.tv_num = (TextView)convertView.findViewById(R.id.tv_num);
			itemView.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
			itemView.tv_category = (TextView)convertView.findViewById(R.id.tv_category);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		User entity= mList.get(position);
		itemView.tv_name.setText(getNullData(entity.babyName));//姓名
		itemView.tv_age.setText(getNullData(entity.babyAge)==""?"0岁":entity.babyAge+"岁"); //年龄
//		itemView.tv_category.setText(getNullData(entity.medalNum)); //季能章
		itemView.tv_num.setText(getNullData(entity.medalNum)==""?"0枚":entity.medalNum+"枚");//数量
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.iv_sixview_head,App.getInstance().getGrayHeadImage());
		return convertView;
	}
	class ItemView{
		private ImageView iv_sixview_head;
		private TextView tv_name,tv_num,tv_age,tv_category;
	}
}
