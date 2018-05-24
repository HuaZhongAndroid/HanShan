package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bm.base.BaseAd;
import com.bm.entity.StoreInfo;
import com.richer.tzj.R;

/**
 * 选择门店适配器
 * @author wanghy
 *
 */
public class CheckStoreAdapter  extends BaseAd<StoreInfo>{
	public CheckStoreAdapter(Context context,List<StoreInfo> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_checkstore, null);
			itemView.tv_storeName = (TextView)convertView.findViewById(R.id.tv_storeName);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		StoreInfo entity= mList.get(position);
		itemView.tv_storeName.setText(getNullData(entity.storeName));
		
		
		
		return convertView;
	}
	class ItemView{
		private TextView tv_storeName;
	
	}
}
