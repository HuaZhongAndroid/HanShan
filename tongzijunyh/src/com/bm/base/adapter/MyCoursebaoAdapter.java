package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.bm.entity.HotGoods;
import com.bm.tzj.activity.CoursebaoAc;
import com.bm.tzj.mine.MyCourseDetailAc;
import com.bm.util.Util;
import com.lib.widget.RoundImageViewFive;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 我的课程适配器
 * @author wanghy
 *
 */
public class MyCoursebaoAdapter  extends BaseAd<CourseBao>{

    public MyCoursebaoAdapter(Context context, List<CourseBao> prolist){
        setActivity(context, prolist);
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
            convertView = mInflater.inflate(R.layout.item_list_mycoursebao, null);
            itemView.tv_mycourse_name = (TextView)convertView.findViewById(R.id.tv_mycourse_name);
            itemView.img_pic = (RoundImageViewFive)convertView.findViewById(R.id.img_pic);
            itemView.tv_mycourse_states = (TextView)convertView.findViewById(R.id.tv_mycourse_states);
            itemView.tv_mycourse_time = (TextView)convertView.findViewById(R.id.tv_mycourse_time);
            itemView.tv_mycourse_address = (TextView)convertView.findViewById(R.id.tv_mycourse_address);
            itemView.tv_mycourse_money = (TextView)convertView.findViewById(R.id.tv_mycourse_money);
            itemView.tv_mycourse_del = (TextView)convertView.findViewById(R.id.tv_mycourse_del);
            itemView.tv_mycourse_Endstates = (TextView)convertView.findViewById(R.id.tv_mycourse_Endstates);
            itemView.tv_mycourse_pay = (TextView)convertView.findViewById(R.id.tv_mycourse_pay);
            itemView.tv_mycourse_club = (TextView)convertView.findViewById(R.id.tv_mycourse_club);
            itemView.tv_mycourse_data = (TextView)convertView.findViewById(R.id.tv_mycourse_data);
            itemView.view=convertView.findViewById(R.id.view);

            convertView.setTag(itemView);
        }else{
            itemView = (ItemView)convertView.getTag();
        }

        final CourseBao entity= mList.get(position);
        ImageLoader.getInstance().displayImage(entity.imgLink, itemView.img_pic,App.getInstance().getHeadOptions());
        itemView.tv_mycourse_name.setText(getNullData(entity.name));//课程名称
        itemView.tv_mycourse_address.setText(getNullData(entity.storeName));//地址

//		String strEndTime = Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
//		if(strEndTime.length()>5){
//			strEndTime = strEndTime.substring(strEndTime.length()-5, strEndTime.length());
//		}
//		itemView.tv_mycourse_time.setText(Util.toString(getNullData(entity.startTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm")+"-"+strEndTime);//时间

        itemView.tv_mycourse_time.setText(entity.groupDate);//时间
//		itemView.tv_mycourse_data.setText(Util.toString(getNullData(entity.endTime),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm"));

//		String strPrice = getNullData(entity.goodsPrice);
//		if(strPrice.contains(".00")){
//			itemView.tv_mycourse_money.setText(getFormat(strPrice,0));//价格
//		}else{
//			itemView.tv_mycourse_money.setText(strPrice);//价格
//		}

        itemView.tv_mycourse_money.setText("￥"+getNullData(entity.money));//价格
//		itemView.tv_mycourse_club.setText(getNullData("城市营地"));//城市营地

        if(entity.isComplete.equals("1")){
            itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
            itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_lab_two));
            itemView.tv_mycourse_Endstates.setText("进行中");
            itemView.view.setVisibility(View.GONE);
            itemView.tv_mycourse_pay.setVisibility(View.GONE);
            itemView.tv_mycourse_del.setVisibility(View.GONE);
            itemView.tv_mycourse_states.setVisibility(View.GONE);
            itemView.tv_mycourse_Endstates.setVisibility(View.VISIBLE);
        }else if (entity.isComplete.equals("2")){
            itemView.tv_mycourse_Endstates.setVisibility(View.VISIBLE);
            itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
            itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_lab));
            itemView.tv_mycourse_Endstates.setText("已完成");
            itemView.view.setVisibility(View.VISIBLE);
            itemView.tv_mycourse_pay.setVisibility(View.GONE);
            itemView.tv_mycourse_del.setVisibility(View.GONE);
            itemView.tv_mycourse_states.setVisibility(View.VISIBLE);
        }else if (entity.isComplete.equals("3")){
            itemView.tv_mycourse_Endstates.setTextColor(context.getResources().getColor(R.color.white));
            itemView.tv_mycourse_Endstates.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collection_red));
            itemView.tv_mycourse_Endstates.setText("待付款");
            itemView.tv_mycourse_Endstates.setVisibility(View.VISIBLE);
            itemView.tv_mycourse_states.setVisibility(View.GONE);
            itemView.tv_mycourse_pay.setVisibility(View.VISIBLE);
            itemView.tv_mycourse_del.setVisibility(View.VISIBLE);
            itemView.view.setVisibility(View.VISIBLE);
        }

        /**
         * 跳转详情
         */
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, CoursebaoAc.class);
                intent.putExtra("data",entity);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public interface OnSeckillClick{
        void onSeckillClick(int position,int type);
    }
    class ItemView{
        private TextView tv_mycourse_states,tv_mycourse_time,tv_mycourse_data,tv_mycourse_address,tv_mycourse_money,tv_mycourse_name,tv_mycourse_del,tv_mycourse_Endstates,tv_mycourse_pay,tv_mycourse_club;
        private RoundImageViewFive img_pic;
        private View view;

    }
}
