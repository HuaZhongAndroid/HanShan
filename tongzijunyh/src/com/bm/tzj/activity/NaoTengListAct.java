package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bm.api.UserManager;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.ShuQIAdapter;
import com.bm.base.adapter.StoreAdapter;
import com.bm.entity.CourseBean;
import com.bm.entity.Storelist;
import com.bm.util.BDLocationHelper;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 大露营列表页面
 */
public class NaoTengListAct extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    private ListView lv_content2;
    private StoreAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_naoteng_list);
        int types = getIntent().getIntExtra("type",1);
        String name = getIntent().getStringExtra("title");
        setTitleName(name);
        adapter2 = new StoreAdapter(context);
        lv_content2 = (ListView) findViewById(R.id.lv_content2);
        lv_content2.setAdapter(adapter2);
        lv_content2.setOnItemClickListener(this);
        //loadCourseList(4,"");
        getStorelist(types);
    }

    /**
     * 获取门店
     *
     * @param types 1,闹腾训练中心 2，室内体验馆
     */
//    室内体验馆
//    http://59.110.62.10:8888/tongZiJun/api/tzjstore/storelist?regionName=西安市&lon=1&lat=1&type=2
//    闹腾生存适能训练中心
//    http://59.110.62.10:8888/tongZiJun/api/tzjstore/storelist?regionName=西安市&lon=1&lat=1&type=1
    public void getStorelist(final int types) {
        BDLocationHelper.LocationInfo info = BDLocationHelper.getCacheLocation();
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("regionName", info.city);//城市名称
        map.put("lat",info.lat+"");
        map.put("lon",info.lng+"");
        map.put("type", "" + types);
        UserManager.getInstance().getTzjstoreStorelist(context, map, new ServiceCallback<CommonListResult<Storelist>>() {
            final String CACHEKEY = "CourseFm_getStorelist";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<Storelist> obj = new CommonListResult<Storelist>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<Storelist>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }
                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<Storelist> obj) {
                if (obj != null) Log.e("obj", "" + obj.data);
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<Storelist> obj) {
                if (null != obj.data&&obj.data.size()>0) {
                    adapter2.clear();
                    adapter2.setType(types);
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
