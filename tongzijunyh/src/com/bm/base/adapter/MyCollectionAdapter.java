package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.HotGoods;
import com.bm.tzj.mine.MyCollectionAc;
import com.bm.util.Util;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 我的课程适配器
 * @author wanghy
 *
 */
public class MyCollectionAdapter  extends BaseAd<HotGoods>{
	private int index=-1;
	Handler handler;
	public MyCollectionAdapter(Context context,List<HotGoods> prolist,Handler strHandler){
		this.handler = strHandler;
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_mycourse, null);
			itemView.tv_mycourse_name = (TextView)convertView.findViewById(R.id.tv_mycourse_name);
			itemView.img_pic = (RoundImageViewFive)convertView.findViewById(R.id.img_pic);
			itemView.tv_mycourse_states = (TextView)convertView.findViewById(R.id.tv_mycourse_states);
			itemView.tv_mycourse_time = (TextView)convertView.findViewById(R.id.tv_mycourse_time);
			itemView.tv_mycourse_address = (TextView)convertView.findViewById(R.id.tv_mycourse_address);
			itemView.tv_mycourse_money = (TextView)convertView.findViewById(R.id.tv_mycourse_money);
			itemView.tv_mycourse_del = (TextView)convertView.findViewById(R.id.tv_mycourse_del);
			itemView.tv_mycourse_Endstates = (TextView)convertView.findViewById(R.id.tv_mycourse_Endstates);
			itemView.tv_mycourse_pay = (TextView)convertView.findViewById(R.id.tv_mycourse_pay);
			itemView.tv_mycourse_data = (TextView)convertView.findViewById(R.id.tv_mycourse_data);
			itemView.tv_mycourse_club = (TextView)convertView.findViewById(R.id.tv_mycourse_club);
			
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final HotGoods entity= mList.get(position);
		index=position;
		itemView.tv_mycourse_name.setText(getNullData(entity.goodsName));//课程名称
		itemView.tv_mycourse_address.setText(getNullData(entity.address));//地址
		itemView.tv_mycourse_money.setText(getNullData(entity.goodsPrice)==null?"￥0":"￥"+entity.goodsPrice);//价格
		
		itemView.tv_mycourse_states.setVisibility(View.GONE);
		itemView.tv_mycourse_pay.setVisibility(View.GONE);

		//3 户外俱乐部  2暑期大露营   1城市营地 
		if(entity.goodsType.equals("1")){
			itemView.tv_mycourse_address.setVisibility(View.GONE);
//			itemView.tv_mycourse_address.setText(getNullData(entity.storeName));//门店地址
			itemView.tv_mycourse_club.setText("城市营地");// 课程分类
		}else if(entity.goodsType.equals("2")){
			itemView.tv_mycourse_club.setText("暑期大露营");// 课程分类
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setText(getNullData(entity.address));//地址
		}else{
			itemView.tv_mycourse_club.setText("周末户外");// 课程分类
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setVisibility(View.VISIBLE);
			itemView.tv_mycourse_address.setText(getNullData(entity.address));//地址
		}
		
//		String strEndTime = Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
//		if(strEndTime.length()>5){
//			strEndTime = strEndTime.substring(strEndTime.length()-5, strEndTime.length());
//		} 
		
		itemView.tv_mycourse_time.setText(Util.toString(getNullData(entity.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		itemView.tv_mycourse_data.setText(Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));//时间
		
		if(entity.courseStatus.equals("0")){
			itemView.tv_mycourse_Endstates.setText("已失效");
			itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
			itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_lab));
		}else if(entity.courseStatus.equals("1")){
			itemView.tv_mycourse_Endstates.setText("进行中");
			itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
			itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_lab_two));
		}else{
			itemView.tv_mycourse_Endstates.setText("未开始");
			itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
			itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_lab));
		}
		ImageLoader.getInstance().displayImage(entity.baseImage, itemView.img_pic,App.getInstance().getHeadOptions());
		
		
		itemView.tv_mycourse_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = MyCollectionAc.MYCOLLECTION_CLICK_STATES;
				msg.arg1 = index;
				handler.sendMessage(msg);
			}
		});
		
//		/**
//		 * 跳转详情
//		 */
//		convertView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				//已失效的课程无法点击看详情
//				if(!entity.courseStatus.equals("0")){
//					Intent intent = new Intent(context,CourseDetailAc.class);
//					intent.putExtra("degree", mList.get(position).goodsType);
//					intent.putExtra("goodsId", mList.get(position).goodsId);
//					intent.putExtra("pageType", "MyCollection");
//					intent.putExtra("pageTag", "2");//pageTag 1基础数据（首页推荐课程，广告位选择，类别搜索）  2商品信息
//					context.startActivity(intent);
//				}
//			}
//		});
		
		return convertView;
	}
	public interface OnSeckillClick{
		void onSeckillClick(int position,int type);
	}
	
	class ItemView{
		private TextView tv_mycourse_club,tv_mycourse_states,tv_mycourse_time,tv_mycourse_address,tv_mycourse_money,tv_mycourse_name,tv_mycourse_del,tv_mycourse_Endstates,tv_mycourse_pay,tv_mycourse_data;
		private RoundImageViewFive img_pic;
	
	}
}
