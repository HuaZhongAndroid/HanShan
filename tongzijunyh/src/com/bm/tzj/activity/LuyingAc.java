package com.bm.tzj.activity;

import android.content.Intent;
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
import com.bm.base.BaseActivity;
import com.bm.base.adapter.ShuQIAdapter;
import com.bm.base.adapter.ZhouMoAdapter;
import com.bm.entity.Course;
import com.bm.entity.CourseBean;
import com.bm.entity.Storelist;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 大露营列表页面
 */
public class LuyingAc extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ListView lv_content2;
    private ShuQIAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_luying);
        setTitleName("暑期大露营");
        adapter2 = new ShuQIAdapter(context);
        lv_content2 = (ListView) findViewById(R.id.lv_content2);
        lv_content2.setAdapter(adapter2);
        lv_content2.setOnItemClickListener(this);
        loadCourseList(4,"");
    }
    private void loadCourseList(final int types, final String regionId) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsType", types + "");

        UserManager.getInstance().get_tzjgoods_goodsListByType(context, map, new ServiceCallback<CommonListResult<CourseBean>>() {
            final String CACHEKEY = "CourseFm_tzjgoods_goodsListByType";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<CourseBean> obj = new CommonListResult<CourseBean>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<CourseBean>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }
                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<CourseBean> obj) {
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }
            private void doResult(CommonListResult<CourseBean> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    adapter2.clear();
                    adapter2.addAll(obj.data);
                    adapter2.notifyDataSetChanged();
                    findViewById(R.id.defultImg).setVisibility(View.GONE);
                }else {
                    findViewById(R.id.defultImg).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_address:
                Intent intent = new Intent(this,LocationMapAc.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         CourseBean  courseBean = (CourseBean) parent.getAdapter().getItem(position);
        Intent  intent = new Intent(context, LuyingDetailAc.class);
        intent.putExtra("goodsId", courseBean.goodsId);
        startActivity(intent);
    }
}
