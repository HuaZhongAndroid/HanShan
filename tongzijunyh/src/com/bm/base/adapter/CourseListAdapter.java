package com.bm.base.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Dictionary;
import com.bm.entity.HotGoods;
import com.bm.util.BaseDataUtil;
import com.bm.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 课程列表适配器
 * @author shiyt
 *
 */
public class CourseListAdapter  extends BaseAd<HotGoods>{
	public CourseListAdapter(Context context,List<HotGoods> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_courselist, null);
			itemView.img_pic = (ImageView)convertView.findViewById(R.id.img_pic);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
			itemView.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			
			itemView.tv_num = (TextView)convertView.findViewById(R.id.tv_num);
			itemView.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
			itemView.tv_age2 = (TextView)convertView.findViewById(R.id.tv_age2);
			itemView.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
			itemView.tv_address = (TextView)convertView.findViewById(R.id.tv_address);
			itemView.tv_category = (TextView)convertView.findViewById(R.id.tv_category);
			itemView.ll_age=(LinearLayout) convertView.findViewById(R.id.ll_age);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		HotGoods entity= mList.get(position);
		//3 户外俱乐部  2暑期大露营   1城市营地
		if(entity.goodsType.equals("3")){
			itemView.tv_address.setVisibility(View.VISIBLE);
			itemView.tv_category.setText("周末户外");
			itemView.tv_time.setVisibility(View.VISIBLE);
			itemView.tv_date.setVisibility(View.VISIBLE);
			itemView.ll_age.setVisibility(View.VISIBLE);
			itemView.tv_age2.setVisibility(View.GONE);
		}else if(entity.goodsType.equals("2")){
			itemView.tv_address.setVisibility(View.VISIBLE);
			itemView.tv_category.setText("暑期大露营");
			itemView.tv_time.setVisibility(View.VISIBLE);
			itemView.tv_date.setVisibility(View.VISIBLE);
			itemView.ll_age.setVisibility(View.VISIBLE);
			itemView.tv_age2.setVisibility(View.GONE);
		}else{
			itemView.tv_address.setVisibility(View.GONE);
			itemView.tv_category.setText("城市营地");
			itemView.tv_time.setVisibility(View.GONE);
			itemView.tv_date.setVisibility(View.GONE);
			itemView.ll_age.setVisibility(View.GONE);
			itemView.tv_age2.setVisibility(View.VISIBLE);
		}
		
		itemView.tv_name.setText(getNullData(entity.goodsName));  //名称
//		itemView.tv_date.setText(getNullData(entity.startTime));  //日期
//		itemView.tv_time.setText(getNullData(entity.startTime));  //时间
		String strEndTime = Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
		if(strEndTime.length()>5){
			strEndTime = strEndTime.substring(strEndTime.length()-5, strEndTime.length());
		} 
		
		String strStartTime = Util.toString(getNullData(entity.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
		if(strStartTime.length()>5){
			strStartTime = strStartTime.substring(strStartTime.length()-5, strStartTime.length());
		} 
		itemView.tv_time.setText(Util.toString(getNullData(entity.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		itemView.tv_date.setText(Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));
		
		String strNum = getNullData(entity.orderNum)==""?"0":entity.orderNum;
		String strGoodsQuota = getNullData(entity.goodsQuota)==""?"0":entity.goodsQuota;
		itemView.tv_num.setText("报名人数 "+strNum+"/"+strGoodsQuota);  //报名人数
		 //年龄 适合年龄 1：3-4岁  2：5-8岁  3：9岁以上
		Map<String, Dictionary> dicMap = BaseDataUtil.shiHeNianLingMap;
		if(dicMap != null)
		{
			Dictionary dic = dicMap.get(entity.suitableAge);
			if(dic != null)
			{
				itemView.tv_age.setText(dic.showvalue);
				itemView.tv_age2.setText(dic.showvalue);
			}
		}
//		if("1".equals(getNullData(entity.suitableAge))){
//			itemView.tv_age.setText("适合年龄3-4岁");
//			itemView.tv_age2.setText("适合年龄3-4岁");
//		}else if("2".equals(getNullData(entity.suitableAge))){
//			itemView.tv_age.setText("适合年龄5-8岁");
//			itemView.tv_age2.setText("适合年龄5-8岁");
//		}else if("3".equals(getNullData(entity.suitableAge))){
//			itemView.tv_age.setText("适合年龄9岁以上");
//			itemView.tv_age2.setText("适合年龄9岁以上");
//		}
		itemView.tv_price.setText("￥"+getNullData(entity.goodsPrice));  //价格
		itemView.tv_address.setText(getNullData(entity.address));  //地址
		
//		ImageLoader.getInstance().displayImage("http://img.taopic.com/uploads/allimg/110102/292-11010221093278.jpg", itemView.img_pic,App.getInstance().getListViewDisplayImageOptions());
		ImageLoader.getInstance().displayImage(getNullData(entity.titleMultiUrl), itemView.img_pic,App.getInstance().getAdvertisingImageOptions());
		return convertView;
	}
	class ItemView{
		private ImageView img_pic;
		private TextView tv_name,tv_date,tv_time,tv_num,tv_age,tv_price,tv_address,tv_category,tv_age2;
		private LinearLayout ll_age;
	}
}
