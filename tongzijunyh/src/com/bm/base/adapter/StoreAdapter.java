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
import com.bm.entity.Storelist;
import com.bm.tzj.activity.LeyuanAc;
import com.bm.tzj.activity.LeyuanStoreAc;
import com.bm.tzj.activity.NaotengAc;
import com.bm.util.GlideUtils;
import com.bumptech.glide.Glide;
import com.lib.widget.RatingBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * Created by zh on 2018/5/24.
 *
 * 室内体验   闹腾训练
 *
 */

public class StoreAdapter extends ArrayAdapter<Storelist> {

    Context context;
    int type = -1;
    public StoreAdapter(@NonNull Context context) {
        super(context, 0);
        this.context   = context;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_storelist2, parent, false);
            }
            final Storelist data = getItem(position);
            ((TextView)convertView.findViewById(R.id.tv_name)).setText(data.storeName);
            ((TextView)convertView.findViewById(R.id.tv_phone)).setText(data.tel);
            ((TextView)convertView.findViewById(R.id.tv_adr)).setText(data.address);
           // Glide.with(context).load(data.acrossImage).into((ImageView)convertView.findViewById(R.id.img_tu));

            GlideUtils.loadRoundImg(context,data.acrossImage,(ImageView)convertView.findViewById(R.id.img_tu),5);
            //ImageLoader.getInstance().displayImage(data.acrossImage, (ImageView)convertView.findViewById(R.id.img_tu),App.getInstance().getListViewDisplayImageOptions());
            RatingBarView custom_ratingbar = (RatingBarView)convertView.findViewById(R.id.custom_ratingbar);
            custom_ratingbar.setClickable(false);
            custom_ratingbar.setStar(Integer.valueOf(data.rankLogistics==null?"0":data.rankLogistics), true);

//            //点击图片进入门店详情
//            convertView.findViewById(R.id.img_tu).setOnClickListener(new android.view.View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context, LeyuanAc.class);
//                    i.putExtra("data",data);
//                    context.startActivity(i);
//                }
//            });

            //进入课程界面
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (type==1){
                        Intent i = new Intent(context, NaotengAc.class);
                        i.putExtra("storelist",data);
                        context.startActivity(i);
                    }else {
                        Intent  i = new Intent(context, LeyuanStoreAc.class);
                        i.putExtra("data",data);
                        context.startActivity(i);
                    }
                }
            });

            return convertView;
    }





}
