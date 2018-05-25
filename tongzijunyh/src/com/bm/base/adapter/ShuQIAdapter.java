package com.bm.base.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.entity.Course;
import com.bm.entity.CourseBean;
import com.bm.entity.Storelist;
import com.bm.tzj.activity.LeyuanAc;
import com.bm.tzj.activity.LuyingDetailAc;
import com.bm.util.GlideUtils;
import com.lib.widget.RatingBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * Created by zh on 2018/5/24.
 *
 * 暑期大露营
 */

public class ShuQIAdapter extends ArrayAdapter<CourseBean> {

    Context context;
    public ShuQIAdapter(@NonNull Context context) {
        super(context, 0);
        this.context   = context;
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shuqi, parent, false);
        }

       // final CourseBean data = courseList.get(position);
        final CourseBean data =getItem(position);
        // tv_name  tv_price  tv_time  tv_age  tv_adr
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
        TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time);
        TextView tv_age = (TextView)convertView.findViewById(R.id.tv_age);
        TextView tv_age2 = (TextView)convertView.findViewById(R.id.tv_age2);
        TextView tv_adr = (TextView)convertView.findViewById(R.id.tv_adr);
        ImageView img_a = (ImageView)convertView.findViewById(R.id.img_a);

        tv_name.setText(data.goodsName);
        tv_time.setText(data.goodsTime);
        tv_age.setText(data.dayiff+"天");
        tv_age2.setText(data.suitableAge+"岁");
        tv_price.setText("￥"+data.goodsPrice);
        tv_adr.setText(data.address);
        GlideUtils.loadRoundImg(context,data.titleMultiUrl,img_a,5);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, LuyingDetailAc.class);
                i.putExtra("goodsId",data.goodsId);
                context.startActivity(i);
            }
        });

        return convertView;
    }





}
