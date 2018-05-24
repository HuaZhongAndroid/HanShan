package com.bm.tzj.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.BaseAd;
import com.bm.entity.Child;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.bm.tzj.caledar.CalendarView_x;
import com.bm.tzj.mine.AddChildAc;
import com.bm.view.CircleImageView;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 课程包单个课程类型按时间查询课程列表
 */
public class CoursebaoCourseAc extends BaseActivity {

    private Course course;
    private CourseBao bao;

    private CalendarView_x cld_a;
    private ListView lv_data;

    private String date;

    private List<Course> courseList;
    private Course xzCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_coursebao_course);
        course = (Course)this.getIntent().getSerializableExtra("course");
        bao = (CourseBao)this.getIntent().getSerializableExtra("bao");
        this.setTitleName(course.goodsName);

        cld_a = (CalendarView_x)this.findViewById(R.id.cld_a);
        lv_data = this.findListViewById(R.id.lv_data);
        lv_data.setAdapter(adapter);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cld_a.setOnSelectedListener(new CalendarView_x.OnSelectedListener(){
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
        loadChildData();
        loadCourseList();
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return courseList==null?0:courseList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_coursebao_courselist, parent, false);
            }

            final Course data = courseList.get(position);

            ((TextView)convertView.findViewById(R.id.tv_name)).setText(data.goodsName);
            ((TextView)convertView.findViewById(R.id.tv_coachName)).setText("教练："+data.coachName);
            ((TextView)convertView.findViewById(R.id.tv_startTime)).setText(data.startTime);
            ((TextView)convertView.findViewById(R.id.tv_endTime)).setText(data.endTime+"结束");
//            ((TextView)convertView.findViewById(R.id.tv_price)).setText("￥"+data.goodsPrice);
            int sy = data.goodsQuota - data.enrollQuota;
            if(sy == 0) {
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("");
                convertView.findViewById(R.id.mengceng).setVisibility(View.VISIBLE);
            }
            else {
                ((TextView) convertView.findViewById(R.id.tv_renshu)).setText("剩余名额：" + sy + "人");
                convertView.findViewById(R.id.mengceng).setVisibility(View.GONE);
                convertView.findViewById(R.id.btn_yuyue).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        xzCourse = data;
                        if(childList.size()>0)
                            showPopupWindow(v);
                        else
                            //跳转到添加孩子
                            context.startActivity(new Intent(context, AddChildAc.class));
                    }
                });
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BaseGoodsDetailAc.class);
                    intent.putExtra("goodsId",data.goodsId);
                    intent.putExtra("goodsName",data.goodsName);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    };

    private void showPopupWindow(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void loadCourseList()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("baseId",course.goodsBaseId);
        map.put("date",date);
        map.put("storeId",bao.storeId);
        map.put("type","1");
        map.put("groupType",bao.type);
        this.showProgressDialog();
        UserManager.getInstance().get_tzjgoods_goodsListByDate(this, map, new ServiceCallback<CommonListResult<Course>>() {
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
                toast(msg);
            }
        });
    }

    private List<Child> childList = new ArrayList<Child>();
    private void loadChildData()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if(null == App.getInstance().getUser()){
            map.put("userId", "0");
        }else{
            map.put("userId", App.getInstance().getUser().userid);

        }
        this.showProgressDialog();
        UserManager.getInstance().getChildrenlist(this, map, new ServiceCallback<CommonListResult<Child>>(){
            @Override
            public void done(int what, CommonListResult<Child> obj) {
                hideProgressDialog();
                childList.clear();
                childList.addAll(obj.data);
//                changeChild(seletedChildPosition);
                if(childList.size()>0)
                    makePopWindow();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }});
    }
    private Child xz_child;
    private BaseAd<Child> childBaseAd = new BaseAd<Child>() {
        @Override
        protected View setConvertView(View convertView, final int position) {
            Viewholder viewholder = null;
            if(convertView  ==null){
                viewholder = new Viewholder();
                convertView	= mInflater.inflate(R.layout.item_child2, null);
                viewholder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                viewholder.img_head = (CircleImageView)convertView.findViewById(R.id.img_head);
                viewholder.v_xz = convertView.findViewById(R.id.v_xz);
                convertView.setTag(viewholder);
            }else{
                viewholder = (Viewholder)convertView.getTag();
            }
            final Child child = mList.get(position);
            if (position == mList.size()-1) {
                viewholder.tv_name.setText("添加孩子");
                ImageLoader.getInstance().displayImage(child.avatar, viewholder.img_head, App.getInstance().getAddImage());
                viewholder.v_xz.setVisibility(View.GONE);
            }else{
                viewholder.tv_name.setText(child.realName);
                ImageLoader.getInstance().displayImage(child.avatar, viewholder.img_head, App.getInstance().getGrayHeadImage());
                if(child == xz_child)
                {
                    viewholder.v_xz.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewholder.v_xz.setVisibility(View.GONE);
                }
            }
            viewholder.img_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == mList.size()-1) {
                        //跳转到添加孩子
                        context.startActivity(new Intent(context, AddChildAc.class));
                        popupWindow.dismiss();
                    }else{
                        //更换孩子
                        xz_child = child;
                        childBaseAd.notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }
        class Viewholder{
            TextView tv_name;
            CircleImageView img_head;
            View v_xz;
        }
    };
    private PopupWindow popupWindow;
    private HorizontalListView hlistView;
    private void makePopWindow() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View popLayout = LayoutInflater.from(this).inflate(com.richer.tzj.R.layout.layout_popupwindow_child2,null);
        popLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        hlistView =(HorizontalListView)popLayout.findViewById(R.id.hlistview);
        Child child =new Child();
        childList.add(child);
        childBaseAd.setActivity(context, childList);
        xz_child=childList.get(0);
        hlistView.setAdapter(childBaseAd);
        popupWindow.setContentView(popLayout);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x99000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        TextView btn_zhifu = (TextView)popLayout.findViewById(R.id.btn_zhifu);
        btn_zhifu.setText("预约");
        //预约按钮点击
        btn_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuyue();
                popupWindow.dismiss();
            }
        });
    }

    private void yuyue()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);//
        }else{
            map.put("userId", "0");//
        }
        map.put("goodsId",xzCourse.goodsId);
        if(null != xz_child)
            map.put("babyId", xz_child.babyId);
        else
            map.put("babyId", "-1");
        map.put("courseGroupId", bao.pkid);
        map.put("storeId", bao.storeId);
        this.showProgressDialog();
        UserManager.getInstance().tzjgoods_goodsBespeak(this, map, new ServiceCallback<StringResult>() {

            @Override
            public void done(int what, StringResult obj) {
                hideProgressDialog();
                toast("预约成功");
                finish();
            }
            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }
}
