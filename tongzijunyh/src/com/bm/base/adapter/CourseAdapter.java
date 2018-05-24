package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.HotGoods;
import com.bm.util.Util;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 推荐课程适配器
 * @author shiyt
 *
 */
public class CourseAdapter  extends BaseAd<HotGoods>{
	String type;
	public CourseAdapter(Context context,List<HotGoods> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_course, null);
			itemView.tv_mycourse_name = (TextView)convertView.findViewById(R.id.tv_mycourse_name);
			itemView.tv_mycourse_endtime = (TextView)convertView.findViewById(R.id.tv_mycourse_endtime);
			itemView.img_pic = (RoundImageViewFive)convertView.findViewById(R.id.img_pic);
			itemView.tv_mycourse_time = (TextView)convertView.findViewById(R.id.tv_mycourse_time);
			itemView.tv_mycourse_address = (TextView)convertView.findViewById(R.id.tv_mycourse_address);
			itemView.tv_mycourse_money = (TextView)convertView.findViewById(R.id.tv_mycourse_money);
			itemView.tv_category = (TextView)convertView.findViewById(R.id.tv_category);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		HotGoods entity= mList.get(position);
		//3 户外俱乐部  2暑期大露营   1城市营地 
		if(entity.goodsType.equals("3")){
			itemView.tv_category.setText("周末户外");
//			itemView.tv_mycourse_time.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setText(getNullData(entity.address));//地址
		}else if(entity.goodsType.equals("2")){
			itemView.tv_category.setText("暑期大露营");
//			itemView.tv_mycourse_time.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setText(getNullData(entity.address));//地址
		}else{
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_category.setText("城市营地");
			itemView.tv_mycourse_address.setVisibility(View.GONE);
//			itemView.tv_mycourse_time.setVisibility(View.GONE);
//			itemView.tv_mycourse_address.setText(getNullData(entity.storeName));//门店地址
		}
		ImageLoader.getInstance().displayImage(entity.titleMultiUrl, itemView.img_pic,App.getInstance().getListViewDisplayImageOptions());
//		ImageLoader.getInstance().displayImage("http://xbyx.cersp.com/xxzy/UploadFiles_7930/200808/20080810110053944.jpg", itemView.img_pic,App.getInstance().getListViewDisplayImageOptions());
		itemView.tv_mycourse_name.setText(getNullData(entity.goodsName));//课程名称
		String strEndTime = Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
		if(strEndTime.length()>5){
			strEndTime = strEndTime.substring(strEndTime.length()-5, strEndTime.length());
		} 
		itemView.tv_mycourse_time.setText(Util.toString(getNullData(entity.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		itemView.tv_mycourse_endtime.setText(Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		itemView.tv_mycourse_money.setText(getNullData(entity.goodsPrice)==""?"￥0":"￥"+getNullData(entity.goodsPrice));//价格
		
		return convertView;
	}
	class ItemView{
		private TextView tv_mycourse_time,tv_mycourse_address,tv_mycourse_money,tv_mycourse_name,tv_category,tv_mycourse_endtime;
		private RoundImageViewFive img_pic;
	}
}
