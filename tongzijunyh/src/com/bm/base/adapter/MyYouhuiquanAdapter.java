package com.bm.base.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.base.BaseAd;
import com.bm.entity.Youhuiquan;
import com.richer.tzj.R;

/**
 * 我的优惠券列表适配器
 *
 */
public class MyYouhuiquanAdapter  extends BaseAd<Youhuiquan>{
	
	private static Map<String,String> aMap = new HashMap<String,String>();
	static{
		aMap.put("-1", "所有课程通用");
		aMap.put("1", "闹腾生存适能专用");
		aMap.put("2", "君昂喊山乐园专用");
		aMap.put("3", "周末成长营专用");
		aMap.put("4", "暑期大露营专用");
		aMap.put("5", "闹腾课程包专用");
		aMap.put("6", "周末课程包专用");
	}
	
	String strState="";
	public MyYouhuiquanAdapter(Context context,List<Youhuiquan> prolist,String state){
		setActivity(context, prolist);
		this.strState = state;
	}
	private OnSeckillClick onSeckillClick;
	public void setOnSeckillClick(OnSeckillClick onSeckillClick){
		this.onSeckillClick = onSeckillClick;
	}
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_myyouhuiquan, null);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.tv_money = (TextView)convertView.findViewById(R.id.tv_money);
			itemView.tv_youxiaoqi = (TextView)convertView.findViewById(R.id.tv_youxiaoqi);
			itemView.bt_lingqu = convertView.findViewById(R.id.bt_lingqu);
			itemView.bt_shiyong = convertView.findViewById(R.id.bt_shiyong);
			itemView.img_st = (ImageView)convertView.findViewById(R.id.img_st);
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final Youhuiquan entity= mList.get(position);
		itemView.tv_name.setText(entity.goodsType+"");//优惠券使用分类
		itemView.tv_money.setText("￥"+entity.money);//金额
		itemView.tv_youxiaoqi.setText("有效期  "+entity.beginDate + "-" + entity.endDate);
		
		if("10".equals(entity.status)) // 未领取
		{
			itemView.tv_name.setTextColor(context.getResources().getColor(R.color.app_dominantHue));
			itemView.tv_money.setTextColor(context.getResources().getColor(R.color.app_dominantHue));
			itemView.img_st.setVisibility(View.GONE);
			itemView.bt_lingqu.setVisibility(View.VISIBLE);
			itemView.bt_shiyong.setVisibility(View.GONE);
		}
		else if("20".equals(entity.status)) // 已领取
		{
			itemView.tv_name.setTextColor(context.getResources().getColor(R.color.app_dominantHue));
			itemView.tv_money.setTextColor(context.getResources().getColor(R.color.app_dominantHue));
			itemView.img_st.setVisibility(View.GONE);
			itemView.bt_lingqu.setVisibility(View.GONE);
			itemView.bt_shiyong.setVisibility(View.VISIBLE);
		}
		else if("30".equals(entity.status)) // 已使用
		{
			itemView.tv_name.setTextColor(context.getResources().getColor(R.color.course_textAddress));
			itemView.tv_money.setTextColor(context.getResources().getColor(R.color.course_textAddress));
			itemView.img_st.setVisibility(View.VISIBLE);
			itemView.img_st.setImageResource(R.drawable.yishiyong);
			itemView.bt_lingqu.setVisibility(View.GONE);
			itemView.bt_shiyong.setVisibility(View.GONE);
		}
		else if("90".equals(entity.status)) // 已过期
		{
			itemView.tv_name.setTextColor(context.getResources().getColor(R.color.course_textAddress));
			itemView.tv_money.setTextColor(context.getResources().getColor(R.color.course_textAddress));
			itemView.img_st.setVisibility(View.VISIBLE);
			itemView.img_st.setImageResource(R.drawable.yiguoqi);
			itemView.bt_lingqu.setVisibility(View.GONE);
			itemView.bt_shiyong.setVisibility(View.GONE);
		}
		
		
		
		//立即领取
		itemView.bt_lingqu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSeckillClick.onSeckillClick(position,1);
			}
		});
		
		//立即使用
		itemView.bt_shiyong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					onSeckillClick.onSeckillClick(position,2);
			}
		});
		
		return convertView;
	}
	
	public interface OnSeckillClick{
		void onSeckillClick(int position,int type);
	}
	class ItemView{
		private TextView tv_name,tv_money,tv_youxiaoqi;
		private View bt_lingqu,bt_shiyong;
		private ImageView img_st;
	
	}
}
