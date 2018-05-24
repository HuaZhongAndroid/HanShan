package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.HashMap;
import java.util.List;

/**
 * 周末列表页面
 */
public class ZhoumoAc extends BaseActivity {

    private HorizontalListView hlistview;
    private ListView lv_course;

    private List<CourseBao> list1;
    private List<Course> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_zhoumo);
        setTitleName("周末成长营");

        hlistview = (HorizontalListView)this.findViewById(R.id.hlistview);
        lv_course = this.findListViewById(R.id.lv_course);
        hlistview.setAdapter(adapter1);
        lv_course.setAdapter(adapter2);

        loadList2();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadList1();
    }

    private void loadList1()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);//
        }else{
            map.put("userId", "0");//
        }
        map.put("storeId","-1");
        map.put("type","2");
        this.showProgressDialog();
        UserManager.getInstance().get_courseGroup_list(context, map, new ServiceCallback<CommonListResult<CourseBao>>() {
            @Override
            public void done(int what, CommonListResult<CourseBao> obj) {
                hideProgressDialog();
                if (obj.data != null)
                {
                    list1 = obj.data;
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }

    private void loadList2()
    {
        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("storeId",data.storeId);
        map.put("goodsType","4");
        this.showProgressDialog();
        UserManager.getInstance().get_tzjgoods_goodsListByType(this, map, new ServiceCallback<CommonListResult<Course>>() {
            @Override
            public void done(int what, CommonListResult<Course> obj) {
                hideProgressDialog();

                if(obj.data != null)
                    list2 = obj.data;
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
            }
        });
    }

    private BaseAdapter adapter1 = new BaseAdapter() {
        @Override
        public int getCount() {
            return list1 == null ? 0: list1.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_coursebao, parent, false);
            }

            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            TextView tv_des = (TextView)convertView.findViewById(R.id.tv_des);
            TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
            ImageView iv_pic = (ImageView)convertView.findViewById(R.id.iv_pic);

            final CourseBao data = list1.get(position);
            tv_name.setText(data.name);
            tv_des.setText("内含"+data.courseNum+"节课程");
            tv_price.setText("￥"+data.money);
            ImageLoader.getInstance().displayImage(data.imgLink, iv_pic, App.getInstance().getListViewDisplayImageOptions());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CoursebaoAc.class);
                    data.storeId = "-1";
                    intent.putExtra("data",data);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    };

    BaseAdapter adapter2 = new BaseAdapter() {
        @Override
        public int getCount() {
            return list2 == null ? 0:list2.size();
        }

        @Override
        public Object getItem(int position) {
            return list2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
            }

            final Course data = list2.get(position);

            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            TextView tv_renshu = (TextView)convertView.findViewById(R.id.tv_renshu);
            TextView tv_age = (TextView)convertView.findViewById(R.id.tv_age);
            TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
            ImageView img_a = (ImageView)convertView.findViewById(R.id.img_a);

            tv_name.setText(data.goodsName);
            tv_time.setText(data.goodsTime);
            tv_age.setText(data.suitableAge+"岁");
            tv_price.setText("￥"+data.goodsPrice);
            int i = data.goodsQuota - data.enrollQuota;
            if(i == 0)
                tv_renshu.setText("报名人数已满");
            else
                tv_renshu.setText("剩余名额"+i+"人");

            ImageLoader.getInstance().displayImage(data.titleMultiUrl, img_a, App.getInstance().getListViewDisplayImageOptions());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, LuyingDetailAc.class);
                    i.putExtra("goodsId",data.goodsId);
                    startActivity(i);
                }
            });

            return convertView;
        }
    };
}
