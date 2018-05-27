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

import com.bm.api.BaseApi;
import com.bm.entity.CourseBean;
import com.bm.tzj.activity.CourseWebActivity;
import com.bm.tzj.activity.LuyingDetailAc;
import com.bm.util.GlideUtils;
import com.richer.tzj.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by zh on 2018/5/24.
 *
 * 周末成长营 适配器
 */

public class ZhouMoAdapter extends ArrayAdapter<CourseBean> {

    List<CourseBean> list ;

    Context context;
    public ZhouMoAdapter(Context context) {
        super(context, 0);
        this.context   = context;
    }

    public void setList(List<CourseBean> list) {
        this.list = list;
    }

    public List<CourseBean> getList() {
        return list;
    }

    /**
     * 显示 count 个 数
     * @param count
     */
    public void showData(int count,boolean isShuffle){
        if (list==null) return;
        if (list.size()<=0) return;
        //打乱重新排序
        if (isShuffle)
        Collections.shuffle(list);
        clear();
        if (count<list.size()){
            for (int i = 0; i <count ; i++) {
                add(list.get(i));
            }
        }else {
            addAll(list);
        }
        notifyDataSetChanged();

    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_zhoumo, parent, false);
        }

       // final CourseBean data = courseList.get(position);
        final CourseBean data =getItem(position);
        // tv_name  tv_price  tv_time  tv_age  tv_adr
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        //报名人数
        TextView totalEnroll = (TextView)convertView.findViewById(R.id.totalEnroll);
        TextView tv_text2 = (TextView)convertView.findViewById(R.id.tv_text2);
        TextView tv_age = (TextView)convertView.findViewById(R.id.tv_age);
        TextView tv_adr = (TextView)convertView.findViewById(R.id.tv_adr);
        ImageView img_a = (ImageView)convertView.findViewById(R.id.img_a);

        tv_name.setText(data.title);
        totalEnroll.setText(data.totalEnroll+"人已报名");
        tv_text2.setText(data.subtitle);
        tv_age.setText(data.latelyCourseDate+".开营");
        tv_adr.setText(data.latelyCourse);
        GlideUtils.loadRoundImg(context,data.cover,img_a,20);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CourseWebActivity.class);
                //（share  1，分享 0不分享  urlType 0代表APP内打开，1代表分享页 ）
                String url = BaseApi.API_HOST+"/tongZiJun/app/specialColumn.html?" +
                        "specialColumnid=%s&share=%s&urlType=%s";
                i.putExtra(CourseWebActivity.WebUrl,String.format(url,data.pkid,0,0));
                i.putExtra(CourseWebActivity.Titele,data.title);
                context.startActivity(i);
            }
        });

        return convertView;
    }





}
