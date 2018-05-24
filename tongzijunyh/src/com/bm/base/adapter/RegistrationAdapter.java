package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Baby;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 报名人适配器
 * @author shiyt
 *
 */
public class RegistrationAdapter  extends BaseAd<Baby>{
	public RegistrationAdapter(Context context,List<Baby> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_gridview_registration, null);
			itemView.img_head = (ImageView)convertView.findViewById(R.id.img_head);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		Baby entity= mList.get(position);
		ImageLoader.getInstance().displayImage(entity.babyAvatar, itemView.img_head,App.getInstance().getheadImage());
		return convertView;
	}
	class ItemView{
		private ImageView img_head;
	}
}
