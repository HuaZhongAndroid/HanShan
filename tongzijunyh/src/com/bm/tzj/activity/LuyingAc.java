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
import com.bm.entity.Storelist;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 大露营列表页面
 */
public class LuyingAc extends BaseActivity implements View.OnClickListener {

    private Storelist data;


    private ListView lv_course;

    private List<Course> courseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_luying);
        data = (Storelist) this.getIntent().getSerializableExtra("data");

        setTitleName("暑期大露营");

        lv_course = this.findListViewById(R.id.lv_course);
        courseList = new ArrayList<Course>();
        lv_course.setAdapter(adapter);

        loadCourseList();
    }

    private void loadCourseList()
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
                    courseList = obj.data;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
            }
        });
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return courseList.size();
        }

        @Override
        public Object getItem(int position) {
            return courseList.get(position);
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

            final Course data = courseList.get(position);

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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_address:
                Intent intent = new Intent(this,LocationMapAc.class);
                intent.putExtra("longitude", data.lon);
                intent.putExtra("latitude", data.lat);
                startActivity(intent);
                break;
        }
    }
}
