package com.bm.tzj.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyCourseAdapter;
import com.bm.base.adapter.MyCoursebaoAdapter;
import com.bm.entity.Course;
import com.bm.entity.CourseBao;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的课程包界面
 */
public class MyCoursebaoAc extends BaseActivity implements SwipyRefreshLayout.OnRefreshListener {

    private ListView lv_listCourse;
    private String state;
    private ImageView img_noData;
    private MyCoursebaoAdapter adapter;
    private List<CourseBao> list = new ArrayList<CourseBao>();
    private SwipyRefreshLayout mSwipyRefreshLayout;
    /** 分页器 */
    public Pager pager = new Pager(10);

    Intent intent = null;
    private int pos=-1;

    private boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.my_course_framelayout);

        setTitleName("我的课程包");

        init();
    }

    public void init() {

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
                R.color.color3, R.color.color4);
        mSwipyRefreshLayout.setRefreshing(false); //暂时没有分页需求

        img_noData=(ImageView) findViewById(R.id.img_noData);
        lv_listCourse = (ListView) findViewById(R.id.lv_listCourse);

        adapter = new MyCoursebaoAdapter(context, list);
        lv_listCourse.setAdapter(adapter);

    }

    protected void onStart() {
        super.onStart();
        pager.setFirstPage();
        list.clear();
        getCoursebaoList();
    }

    private void getCoursebaoList() {
        if(isLoading)
            return;
        isLoading = true;
        HashMap<String, String> map = new HashMap<String, String>();
        if(null == App.getInstance().getUser()){
            map.put("userId", "0");
        }else{
            map.put("userId", App.getInstance().getUser().userid);
        }

        showProgressDialog();
        UserManager.getInstance().get_courseGroup_myCourseGroup(context, map, new ServiceCallback<CommonListResult<CourseBao>>() {
            @Override
            public void error(String msg) {
                hideProgressDialog();
                isLoading = false;
            }
            @Override
            public void done(int what, CommonListResult<CourseBao> obj) {
                hideProgressDialog();

                if(obj.data.size()>0){
                    list.addAll(obj.data);
                    pager.setCurrentPage(pager.nextPage(),list.size());
                    setData();
                }

                isLoading = false;
            }
        });
    }

    private void setData() {
        if (list.size() == 0) {
            img_noData.setVisibility(View.VISIBLE);
        } else {
            img_noData.setVisibility(View.GONE);
            lv_listCourse.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
//        if (direction == SwipyRefreshLayoutDirection.TOP) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Hide the refresh after 2sec
//                    ((MyCourseAc) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            pager.setFirstPage();
//                            list.clear();
//                            getCoursebaoList();
//                            // mSwipyRefreshLayout.setRefreshing(false);
//
//                            // 刷新设置
//                            mSwipyRefreshLayout.setRefreshing(false);
//
////							map.put("pageNum", pager.nextPage() + "");// 页码
////							pager.setCurrentPage(pager.nextPage(), list.size());
//                        }
//                    });
//                }
//            }, 2000);
//
//        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Hide the refresh after 2sec
//                    ((MyCourseAc) context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            getCoursebaoList();
//                            // 刷新设置
//                            mSwipyRefreshLayout.setRefreshing(false);
//                        }
//                    });
//                }
//            }, 2000);
//        }
    }
}
