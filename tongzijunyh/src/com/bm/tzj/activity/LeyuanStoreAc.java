package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.entity.Course;
import com.bm.entity.Order;
import com.bm.entity.Storelist;
import com.bm.tzj.caledar.CalendarView_x;
import com.bm.tzj.kc.PayInfoAc3;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 乐园课程选择
 */
public class LeyuanStoreAc extends AbsCoursePayBaseAc implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private Storelist storelist;
    private String date;
    private SimpleDateFormat sdf;

    private ListView lv_storeList;

    private List<Course> courseList;

    private TextView tv_address;
    private ImageView img_ad;

    private Course xzCourse;
    private AppBarLayout app_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_leyuan_store1);
        storelist = (Storelist) this.getIntent().getSerializableExtra("data");
        setTitleName(storelist.storeName);

        app_bar = (AppBarLayout) this.findViewById(R.id.app_bar);
        //app_bar.getLayoutParams().height = App.getInstance().getScreenWidth() / 2;
        app_bar.addOnOffsetChangedListener(this);

        tv_address = this.findTextViewById(R.id.tv_address);
        tv_address.setOnClickListener(this);
        tv_address.setText(storelist.address);

        img_ad = this.findImageViewById(R.id.img_ad);
        ImageLoader.getInstance().displayImage(storelist.acrossImage, img_ad, App.getInstance().getListViewDisplayImageOptions());


        lv_storeList = this.findListViewById(R.id.lv_storeList);
        courseList = new ArrayList<Course>();
        lv_storeList.setAdapter(adapter);


        sdf = new SimpleDateFormat("yyyy-MM-dd");
        CalendarView_x cld_a = (CalendarView_x) this.findViewById(R.id.cld_a);
        cld_a.setOnSelectedListener(new CalendarView_x.OnSelectedListener() {
            @Override
            public void onSelected(Calendar d) {
                date = sdf.format(d.getTime());
                loadCourseList();
            }
        });
        date = sdf.format(cld_a.getSelected().getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCourseList();
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

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_store_leyuan, parent, false);
            }

            final Course data = courseList.get(position);

            ((TextView) convertView.findViewById(R.id.tv_name)).setText(data.goodsName);
            ((TextView) convertView.findViewById(R.id.tv_coachName)).setText("教练：" + data.coachName);
            ((TextView) convertView.findViewById(R.id.tv_startTime)).setText(data.startTime);
            ((TextView) convertView.findViewById(R.id.tv_endTime)).setText(data.endTime + "结束");
            ((TextView) convertView.findViewById(R.id.tv_price)).setText("￥" + data.goodsPrice);
            int sy = data.goodsQuota - data.enrollQuota;
            if (sy == 0) {
                ((TextView) convertView.findViewById(R.id.btn_baoming)).setText("满员");
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("");
                convertView.findViewById(R.id.mengceng).setVisibility(View.VISIBLE);
            } else {
                ((TextView) convertView.findViewById(R.id.btn_baoming)).setText("报名");
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("剩余名额：" + sy + "人");
                convertView.findViewById(R.id.mengceng).setVisibility(View.GONE);
                convertView.findViewById(R.id.btn_baoming).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xzCourse = data;
                        LeyuanStoreAc.super.goodsId = xzCourse.goodsId;
                        LeyuanStoreAc.super.storeId = storelist.storeId;
                        LeyuanStoreAc.super.type = "10";
                        showPopupWindow(v);
                    }
                });
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BaseGoodsDetailAc.class);
                    intent.putExtra("goodsId", data.goodsId);
                    intent.putExtra("goodsName", data.goodsName);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    };

    private void loadCourseList() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("storeId", storelist.storeId);
        map.put("type", "1");
        map.put("goodsType", "2");
        map.put("date", date);
        this.showProgressDialog();
        UserManager.getInstance().getGoodsListByStore(this, map, new ServiceCallback<CommonListResult<Course>>() {
            @Override
            public void done(int what, CommonListResult<Course> obj) {
                hideProgressDialog();

                if (obj.data != null)
                    courseList = obj.data;
                if (courseList.size() > 0)
                    findViewById(R.id.v_none).setVisibility(View.GONE);
                else
                    findViewById(R.id.v_none).setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address:
                Intent i = new Intent(context, LeyuanAc.class);
                i.putExtra("data", storelist);
                startActivity(i);
                break;
        }
    }


    @Override
    protected void onCreateOrderSuccess(Order order) {
        Intent intent = new Intent(context, PayInfoAc3.class);

        order.realName = xz_child.realName;
        order.goodsType = xzCourse.goodsType;
        order.goodsTime = xzCourse.goodsTime;
        order.goodsName = xzCourse.goodsName;

        intent.putExtra("course", xzCourse);
        intent.putExtra("order", order);
        intent.putExtra("child", xz_child);
        intent.putExtra("storelist", storelist);
        intent.putExtra("pageTag", "AbsCoursePayBaseAc");
        startActivity(intent);
    }
    /**
     * 监听滑倒顶部悬浮 然后改变标题
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            Log.e("fff", "到顶了 " + verticalOffset);
            String titleName =  "%s年%s月";
            if (date.contains("-")&&date.length()>=2){
                String split[] = date.split("-");
                titleName = String.format(titleName,split[0],split[1]);
                setTitleName(titleName);
            }else {
                setTitleName(date);
            }
        } else if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() / 3 * 2) {
            Log.d("fff", "快到顶了 " + verticalOffset);
            setTitleName(storelist.storeName);
        } else {
            Log.d("fff", "在中间 " + verticalOffset);
        }
    }
}
