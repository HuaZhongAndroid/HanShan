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
import com.bm.entity.Order;
import com.bm.tzj.kc.PayInfoAc2;
import com.bm.tzj.mine.AddCommentAc;
import com.bm.tzj.mine.MyCourseAc;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.HashMap;
import java.util.List;

/**
 * 课程包界面
 */
public class CoursebaoAc extends BaseActivity implements View.OnClickListener {

    private CourseBao bao;

    private ImageView iv_pic;
    private TextView btn_buy;
    private ListView lv_course;

    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_coursebao);

        bao = (CourseBao)this.getIntent().getSerializableExtra("data");
        setTitleName(bao.name);

        iv_pic = this.findImageViewById(R.id.iv_pic);
        btn_buy = this.findTextViewById(R.id.btn_buy);
        ImageLoader.getInstance().displayImage(bao.imgLink, iv_pic, App.getInstance().getListViewDisplayImageOptions());
        if("1".equals(bao.isBuy))
        {
            btn_buy.setVisibility(View.GONE);
        }
        else {
            btn_buy.setVisibility(View.VISIBLE);
            btn_buy.setOnClickListener(this);
        }

        lv_course = this.findListViewById(R.id.lv_course);
        lv_course.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadList();
    }

    private void loadList()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);//
        }else{
            map.put("userId", "0");//
        }
        map.put("isBuy",bao.isBuy);
        if(bao.isBuy == null)
        {
            if("3".equals(bao.isComplete))
                map.put("isBuy","0");
            else
                map.put("isBuy","1");
        }
        map.put("courseId",bao.pkid);
        this.showProgressDialog();
        UserManager.getInstance().get_courseGroup_detailList(context, map, new ServiceCallback<CommonListResult<Course>>() {

            @Override
            public void done(int what, CommonListResult<Course> obj) {
                hideProgressDialog();
                if (obj.data != null)
                {
                    courseList = obj.data;
//                    tab_adapter.notifyDataSetChanged();
//                    sIndex_changguan = 0;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return courseList == null?0:courseList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_coursebao_course, parent, false);
            }

            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            TextView tv_time = (TextView)convertView.findViewById(R.id.tv_time);
            TextView btn_pingjia = (TextView)convertView.findViewById(R.id.btn_pingjia);
            TextView btn_yuyue = (TextView)convertView.findViewById(R.id.btn_yuyue);
            View mengceng = convertView.findViewById(R.id.mengceng);

            final Course course = courseList.get(position);
            tv_name.setText(course.goodsName);
            if("1".equals(course.goodsStatus))
            {
                mengceng.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                btn_pingjia.setVisibility(View.GONE);
                btn_yuyue.setVisibility(View.VISIBLE);
                btn_yuyue.setText("预约");
                btn_yuyue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, CoursebaoCourseAc.class);
                        i.putExtra("course",course);
                        i.putExtra("bao",bao);
                        startActivity(i);
                    }
                });
            }
            else if("2".equals(course.goodsStatus))   //评价
            {
                mengceng.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                btn_pingjia.setVisibility(View.VISIBLE);
                btn_pingjia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AddCommentAc.class);
                        intent.putExtra("hotGoods", course);
                        startActivity(intent);
                    }
                });
                btn_yuyue.setVisibility(View.GONE);
            }
            else if("3".equals(course.goodsStatus))
            {
                mengceng.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                btn_pingjia.setVisibility(View.GONE);
                btn_yuyue.setVisibility(View.VISIBLE);
                btn_yuyue.setText("完成");
                btn_yuyue.setOnClickListener(null);
                tv_time.setText("上课时间："+course.goodsTime);
            }
            else{
                mengceng.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                btn_pingjia.setVisibility(View.GONE);
                btn_yuyue.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BaseGoodsDetailAc.class);
                    intent.putExtra("goodsId",course.goodsBaseId);
                    intent.putExtra("goodsName",course.goodsName);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_buy:
                buy();
                break;
        }
    }

    private void buy()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsId", bao.pkid);//课程ID
        map.put("storeId", bao.storeId); //门店id
        map.put("type", "20");
        if(null == App.getInstance().getUser()){
            map.put("userId", "0");
        }else{
            map.put("userId", App.getInstance().getUser().userid);
        }
        map.put("babyId", "-1");

        showProgressDialog();
        UserManager.getInstance().getbtsOrderInfo_addOrder(context, map, new ServiceCallback<CommonResult<Order>>() {
            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
            @Override
            public void done(int what, CommonResult<Order> obj) {
                hideProgressDialog();
                if(null !=obj.data){
                    Intent intent = new Intent(context, PayInfoAc2.class);
                    intent.putExtra("bao", bao);
                    intent.putExtra("order", obj.data);
                    intent.putExtra("pageTag","CoursebaoAc");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
