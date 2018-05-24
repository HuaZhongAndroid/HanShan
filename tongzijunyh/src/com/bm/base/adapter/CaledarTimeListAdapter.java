package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.base.BaseAd;
import com.bm.entity.GoodsNumTime;
import com.bm.util.Util;
import com.richer.tzj.R;

/**
 * 时间适配器
 * @author shiyt
 *
 */
public class CaledarTimeListAdapter  extends BaseAd<GoodsNumTime>{
	public CaledarTimeListAdapter(Context context,List<GoodsNumTime> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_caledartime, null);
			itemView.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			itemView.tv_count = (TextView)convertView.findViewById(R.id.tv_count);
			itemView.ll_bg = (LinearLayout)convertView.findViewById(R.id.ll_bg);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		GoodsNumTime entity= mList.get(position);
		if(Integer.valueOf(entity.personCount)>0){
			itemView.tv_count.setText("剩余："+getNullData(entity.personCount)+"人");
		}else{
			itemView.tv_count.setText("已满");
		}
		itemView.tv_time.setText(Util.toString(getNullData(entity.startTime), "yyyy-MM-dd HH:ss", "HH:ss")+"—"+Util.toString(getNullData(entity.endTime), "yyyy-MM-dd HH:ss", "HH:ss"));
		
		
		if(entity.isBought.equals("1")){  //是否购买
			itemView.ll_bg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rl_selected));
		}else{
			itemView.ll_bg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rl_noselected));
		}
		
		return convertView;
	}
	class ItemView{
		private TextView tv_time,tv_count;
		private LinearLayout ll_bg;
	}
}
