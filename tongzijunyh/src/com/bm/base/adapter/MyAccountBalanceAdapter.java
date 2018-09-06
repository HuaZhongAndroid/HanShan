package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bm.base.BaseAd;
import com.bm.entity.Order;
import com.richer.tzj.R;

/**
 * 账户余额适配器
 * @author wanghy
 *
 */
public class MyAccountBalanceAdapter  extends BaseAd<Order>{
	String strType;
	public MyAccountBalanceAdapter(Context context,List<Order> prolist,String type){
		setActivity(context, prolist);
		this.strType = type;
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_myaccount, null);
			itemView.tv_accoundTitle = (TextView)convertView.findViewById(R.id.tv_accoundTitle);
			itemView.tv_accoundMoney = (TextView)convertView.findViewById(R.id.tv_accoundMoney);
			itemView.tv_accoundTime = (TextView)convertView.findViewById(R.id.tv_accoundTime);
			itemView.tv_couponMoney = (TextView)convertView.findViewById(R.id.tv_couponMoney);
			itemView.tv_word = (TextView)convertView.findViewById(R.id.tv_word);
			
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		Order entity= mList.get(position);
		if(strType.equals("1")){//账号余额
			/**1充值   0消费  */
			if(entity.changeStatus.equals("0")){
				itemView.tv_accoundMoney.setText(getNullData("- "+entity.paymentAccount));//价格
			}else{
				itemView.tv_accoundMoney.setText(getNullData("+ "+entity.paymentAccount));//价格
			}
			itemView.tv_accoundTitle.setText(getNullData(entity.goodsName));//名称
//			itemView.tv_accoundTime.setText(Util.toString(getNullData(entity.cDate), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"));//时间
			itemView.tv_accoundTime.setText(entity.cDate);
		}else{//积分    1：购买赠送  2：评价赠送
			itemView.tv_accoundMoney.setText(getNullData("+ "+entity.integral));//价格
			
//			itemView.tv_accoundTime.setText(Util.toString(getNullData(entity.cdate), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd"));//时间
			itemView.tv_accoundTime.setText(entity.cdate);
			if(entity.integralType.equals("1")){
				itemView.tv_accoundTitle.setText("【购买赠送】"+getNullData(entity.integralName));//名称
			}else{
				itemView.tv_accoundTitle.setText("【评价赠送】"+getNullData(entity.integralName));//名称
			}
		}

		if (Float.parseFloat(entity.couponMoney)>0) {
			itemView.tv_word.setVisibility(View.VISIBLE);
			itemView.tv_couponMoney.setVisibility(View.VISIBLE);
			itemView.tv_couponMoney.setText("-"+entity.couponMoney);
		}else{
			itemView.tv_word.setVisibility(View.GONE);
			itemView.tv_couponMoney.setVisibility(View.GONE);
		}
		
		
		
		
		return convertView;
	}
	class ItemView{
		public TextView tv_word;
		private TextView tv_accoundTitle,tv_accoundMoney,tv_accoundTime,tv_couponMoney;
	
	}
}
