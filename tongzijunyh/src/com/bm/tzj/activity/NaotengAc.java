package com.bm.tzj.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.entity.Changguan;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.bm.entity.Order;
import com.bm.entity.Storelist;
import com.bm.tzj.caledar.CalendarView_x;
import com.bm.tzj.kc.PayInfoAc3;
import com.bm.util.BitmapUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.richer.tzj.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 闹腾首页
 */
public class NaotengAc extends AbsCoursePayBaseAc {

    private ImageView iv_tab_bg;
    private HorizontalListView h_tab;

    private TextView tv_cg_name;

    private HorizontalListView h_list;
    private Course xzCourse;

    private List<Changguan> changguanList;
    private int sIndex_changguan;
    private View sTabView;

    private List<Course> courseList;

    private Storelist storelist; //门店

    private List<CourseBao> courseBaoList;

    private CalendarView_x cld_a;
    private String date; //当前选择的日期
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private ListView lv_course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_naoteng);
        context = this;

        storelist = (Storelist)this.getIntent().getSerializableExtra("storelist");

        this.setTitleName(storelist.storeName);

        h_list = (HorizontalListView)this.findViewById(R.id.h_list);
        h_list.setAdapter(baoAdapter);
        h_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseBao bao = courseBaoList.get(position);
                Intent intent = new Intent(context, CoursebaoAc.class);
                bao.storeId = storelist.storeId;
                bao.storeName = storelist.storeName;
                intent.putExtra("data",bao);
                startActivity(intent);
            }
        });

        cld_a = (CalendarView_x)this.findViewById(R.id.cld_a);
        date = sdf.format(cld_a.getSelected().getTime());
        cld_a.setOnSelectedListener(new CalendarView_x.OnSelectedListener(){
            @Override
            public void onSelected(Calendar d) {
                date = sdf.format(d.getTime());
                loadCourseList();
            }
        });

        lv_course = this.findListViewById(R.id.lv_course);
        lv_course.setAdapter(courseAdapter);

        tv_cg_name = this.findTextViewById(R.id.tv_cg_name);
        iv_tab_bg = this.findImageViewById(R.id.iv_tab_bg);
        h_tab = (HorizontalListView)this.findViewById(R.id.h_tab);
        h_tab.setAdapter(tab_adapter);
        loadChangguan();
        h_tab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sIndex_changguan = position;
                tv_cg_name.setText(changguanList.get(position).storeName);
//                setTabImgSize(view);
                tab_adapter.notifyDataSetChanged();
                cld_a.setSelectedIndex(0);
            }
        });
    }

    protected  void onStart()
    {
        super.onStart();
        loadCourseBao();
    }

    private void loadCourseList()
    {
        if(changguanList.size() == 0) return;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("storeId",changguanList.get(sIndex_changguan).storeId);
        map.put("date",date);
        map.put("type","2");
        map.put("goodsType","1");
        this.showProgressDialog();
        UserManager.getInstance().getGoodsListByStore(this, map, new ServiceCallback<CommonListResult<Course>>() {
            @Override
            public void done(int what, CommonListResult<Course> obj) {
                hideProgressDialog();
                courseList = obj.data;
                if(obj.data != null && obj.data.size() > 0) {
                    findViewById(R.id.v_none).setVisibility(View.GONE);
                }
                else
                {
                    findViewById(R.id.v_none).setVisibility(View.VISIBLE);
                }
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
            }
        });
    }

    BaseAdapter courseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return courseList == null?0:courseList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_store_leyuan, parent, false);
            }

            final Course data = courseList.get(position);

            ((TextView)convertView.findViewById(R.id.tv_name)).setText(data.goodsName);
            ((TextView)convertView.findViewById(R.id.tv_coachName)).setText("教练："+data.coachName);
            ((TextView)convertView.findViewById(R.id.tv_startTime)).setText(data.startTime);
            ((TextView)convertView.findViewById(R.id.tv_endTime)).setText(data.endTime+"结束");
            ((TextView)convertView.findViewById(R.id.tv_price)).setText("￥"+data.goodsPrice);
            int sy = data.goodsQuota - data.enrollQuota;
            if(sy == 0) {
                ((TextView)convertView.findViewById(R.id.btn_baoming)).setText("满员");
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("");
                convertView.findViewById(R.id.mengceng).setVisibility(View.VISIBLE);
            }
            else {
                ((TextView)convertView.findViewById(R.id.btn_baoming)).setText("报名");
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("剩余名额：" + sy + "人");
                convertView.findViewById(R.id.mengceng).setVisibility(View.GONE);
                convertView.findViewById(R.id.btn_baoming).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        xzCourse = data;
                        NaotengAc.super.goodsId = xzCourse.goodsId;
                        NaotengAc.super.storeId = xzCourse.storeId;
                        NaotengAc.super.type = "10";
                        showPopupWindow(v);
                    }
                });
            }

            return convertView;
        }
    };

    BaseAdapter baoAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return courseBaoList == null ? 0 : courseBaoList.size();
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

            ImageView iv_pic = (ImageView)convertView.findViewById(R.id.iv_pic);
            TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            TextView tv_des = (TextView)convertView.findViewById(R.id.tv_des);
            TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
            TextView btn_buy = (TextView)convertView.findViewById(R.id.btn_buy);

            final CourseBao bao = courseBaoList.get(position);
            ImageLoader.getInstance().displayImage(bao.imgLink, iv_pic, App.getInstance().getListViewDisplayImageOptions());
            tv_name.setText(bao.name);
            tv_des.setText("内含"+bao.courseNum+"节课程");
            tv_price.setText("￥"+bao.money);

//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, CoursebaoAc.class);
//                    bao.storeId = storelist.storeId;
//                    bao.storeName = storelist.storeName;
//                    intent.putExtra("data",bao);
//                    startActivity(intent);
//                }
//            });

            return convertView;
        }
    };

    private void loadCourseBao()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);//
        }else{
            map.put("userId", "0");//
        }
        map.put("storeId",storelist.storeId);
        map.put("type","1");
        this.showProgressDialog();
        UserManager.getInstance().get_courseGroup_list(context, map, new ServiceCallback<CommonListResult<CourseBao>>() {

            @Override
            public void done(int what, CommonListResult<CourseBao> obj) {
                hideProgressDialog();
                if (obj.data != null)
                {
                    courseBaoList = obj.data;
                    baoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }

    private void loadChangguan()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("storeId",storelist.storeId);
        this.showProgressDialog();
        UserManager.getInstance().get_tzjstore_venueList(context, map, new ServiceCallback<CommonListResult<Changguan>>() {

            @Override
            public void done(int what, CommonListResult<Changguan> obj) {
                hideProgressDialog();
                if (obj.data != null)
                {
                    changguanList = obj.data;
                    sIndex_changguan = 0;
                    if(changguanList.size()>0)
                        tv_cg_name.setText(changguanList.get(0).storeName);
                    tab_adapter.notifyDataSetChanged();
                    cld_a.setSelectedIndex(0);
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }

    private int yWidth,yHeight;
    BaseAdapter tab_adapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return changguanList == null ? 0 : changguanList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_tabpic, parent, false);
                yWidth = convertView.findViewById(R.id.iv_tab).getLayoutParams().width;
                yHeight = convertView.findViewById(R.id.iv_tab).getLayoutParams().height;
            }

            ImageView pic = (ImageView)convertView.findViewById(R.id.iv_tab);
            ImageLoader.getInstance().displayImage(changguanList.get(position).titleMultiUrl, pic, App.getInstance().getListViewDisplayImageOptions());
            pic.setTag(changguanList.get(position).titleMultiUrl);

            if(position == sIndex_changguan)
            {
                sTabView = convertView;
                ViewGroup.LayoutParams lp = pic.getLayoutParams();
                lp.width = (int)(yWidth*1.2);
                lp.height = (int)(yHeight*1.2);
                convertView.findViewById(R.id.sanjiao).setVisibility(View.VISIBLE);
                iv_tab_bg.setImageBitmap(null);
                ImageLoader.getInstance().loadImage(pic.getTag().toString(), App.getInstance().getheadImage(), new ImageLoadingListener(){
                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                    }
                    @Override
                    public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
                        if(bm != null) {
                            iv_tab_bg.setImageBitmap(BitmapUtil.fastblur(bm, 100));
                            bm.recycle();
                        }
                    }
                    @Override
                    public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    }
                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                    }});
            }
            else
            {
                ViewGroup.LayoutParams lp = pic.getLayoutParams();
                lp.width = yWidth;
                lp.height = yHeight;
                convertView.findViewById(R.id.sanjiao).setVisibility(View.INVISIBLE);
            }

            return convertView;
        }


    };

    private void setTabImgSize(View v)
    {
        if(sTabView != null)
        {
            ViewGroup.LayoutParams lp = sTabView.findViewById(R.id.iv_tab).getLayoutParams();
            lp.width = yWidth;
            lp.height = yHeight;
            sTabView.findViewById(R.id.iv_tab).setLayoutParams(lp);
            sTabView.findViewById(R.id.sanjiao).setVisibility(View.INVISIBLE);
            sTabView.invalidate();
        }

        sTabView = v;
        ViewGroup.LayoutParams lp = sTabView.findViewById(R.id.iv_tab).getLayoutParams();
        lp.width = (int)(yWidth*1.2);
        lp.height = (int)(yHeight*1.2);
        sTabView.findViewById(R.id.iv_tab).setLayoutParams(lp);
        sTabView.findViewById(R.id.sanjiao).setVisibility(View.VISIBLE);
        iv_tab_bg.setImageBitmap(null);
        ImageLoader.getInstance().loadImage(sTabView.findViewById(R.id.iv_tab).getTag().toString(), App.getInstance().getheadImage(), new ImageLoadingListener(){
            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }
            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
                if(bm != null)
                    iv_tab_bg.setImageBitmap(BitmapUtil.fastblur(bm, 100));
            }
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            }
            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }});

        sTabView.invalidate();
        h_tab.postInvalidate();
    }



    @Override
    protected void onCreateOrderSuccess(Order order) {
        Intent intent = new Intent(context, PayInfoAc3.class);

        order.realName  = xz_child.realName;
        order.goodsType = xzCourse.goodsType;
        order.goodsTime = xzCourse.goodsTime;
        order.goodsName = xzCourse.goodsName;

        intent.putExtra("course", xzCourse);
        intent.putExtra("order",  order);
        intent.putExtra("child",xz_child);
        intent.putExtra("storelist",storelist);
        intent.putExtra("pageTag","AbsCoursePayBaseAc");
        startActivity(intent);
    }
}
