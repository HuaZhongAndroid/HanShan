package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.CoachDiploma;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 获得证书适配器
 * @author shiyt
 *
 */
public class CertificateAdapter  extends BaseAd<CoachDiploma>{
	public CertificateAdapter(Context context,List<CoachDiploma> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_gridview_certificate, null);
			itemView.img_head = (RoundImageViewFive)convertView.findViewById(R.id.img_head);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		CoachDiploma entity= mList.get(position);
		itemView.tv_name.setText(getNullData(entity.diplomaName));
		ImageLoader.getInstance().displayImage(entity.diplomaTitleMultiUrl, itemView.img_head,App.getInstance().getListViewDisplayImageOptions());
		return convertView;
	}
	class ItemView{
		private RoundImageViewFive img_head;
		private TextView tv_name;
	}
}
