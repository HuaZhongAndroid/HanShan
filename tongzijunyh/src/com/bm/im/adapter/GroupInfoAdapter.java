package com.bm.im.adapter;

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
 * 群信息适配器
 * 
 * @author shiyt
 * 
 */
public class GroupInfoAdapter extends BaseAd<User> {
	public GroupInfoAdapter(Context context,List<User> prolist){
		setActivity(context, prolist);
	}

	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if (convertView == null) {
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_gridview_groupperson,null);
			itemView.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			itemView.img_head = (ImageView) convertView.findViewById(R.id.img_head);
			convertView.setTag(itemView);
		} else {
			itemView = (ItemView) convertView.getTag();
		}

		User entity = mList.get(position);
		itemView.tv_name.setText(getNullData(entity.nickname));
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.img_head,App.getInstance().getheadImage());

		return convertView;
	}

	class ItemView {
		private TextView tv_name;
		private ImageView img_head;

	}
}
