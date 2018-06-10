package com.bm.tzj.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.ZhouMoAdapter1;
import com.bm.entity.CourseBean;
import com.bm.entity.ZhouMoCity;
import com.bm.util.CacheUtil;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * 周末列表页面
 */
public class ZhoumoAc extends BaseActivity implements AdapterView.OnItemClickListener {

    private ImageView defultImg;

    private ListView lv_content2;
    private ZhouMoAdapter1 adapter2;
    private LinearLayout lay_city; //  当城市个数大于1  则显示


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_zhoumo1);
        setTitleName("周末成长营");
        lay_city = (LinearLayout) findViewById(R.id.lay_city);
        adapter2 = new ZhouMoAdapter1(context);
        lv_content2 = (ListView) findViewById(R.id.lv_content2);
        lv_content2.setAdapter(adapter2);
        lv_content2.setOnItemClickListener(this);
        getZhouMoCitys();
    }

    /**
     * @param types    3  周末成长营 4  暑期大露营
     * @param regionId 城市id【getZhouMoCitys接口中的regionId参数】
     */
    //    周末成长营
    //    http://59.110.62.10:8888/tongZiJun/api/tzjgoods/goodsListByType?goodsType=3&typeCode=
    //    暑期大露营
    //    http://59.110.62.10:8888/tongZiJun/api/tzjgoods/goodsListByType?goodsType=4&typeCode=
    private void loadCourseList(final int types, final String regionId) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsType", types + "");
        map.put("typeCode", regionId + "");//regionId
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
                } else {
                    findViewById(R.id.defultImg).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取周末成长营  城市地址
     * <p>
     * http://59.110.62.10:8888/tongZiJun/api/tzjgoods/getTrend
     */
    public void getZhouMoCitys() {
        final HashMap<String, String> map = new HashMap<String, String>();
        UserManager.getInstance().getZhouMoCitys(context, map, new ServiceCallback<CommonListResult<ZhouMoCity>>() {
            final String CACHEKEY = "CourseFm_getZhouMoCitys";

            @Override
            public void error(String msg) {
                //从缓存中读取数据
                CommonListResult<ZhouMoCity> obj = new CommonListResult<ZhouMoCity>() {
                };
                Type type = obj.getClass().getGenericSuperclass();
                obj = (CommonListResult<ZhouMoCity>) CacheUtil.read(context, CACHEKEY, map, type);
                if (obj != null) {
                    doResult(obj);
                    return;
                }

                //没有缓存时执行下面的逻辑
                MainAc.intance.dialogToast(msg);
            }

            @Override
            public void done(int what, CommonListResult<ZhouMoCity> obj) {
                doResult(obj);
                //缓存结果
                CacheUtil.save(context, CACHEKEY, map, obj);
            }

            private void doResult(CommonListResult<ZhouMoCity> obj) {
                if (null != obj.data && obj.data.size() > 0) {
                    if (obj.data.size() == 1) {
                        lay_city.setVisibility(View.GONE);
                    } else {
                        addZhouMoCity(obj.data);
                        lay_city.setVisibility(View.VISIBLE);
                    }
                    String regionId = obj.data.get(0).regionId;
                    loadCourseList(3, regionId);
                    //loadCourseList(4, regionId);
                }
            }

        });
    }

    /**
     * 周末城市 添加
     *
     * @param list
     */
    LinearLayout lastView;

    private void addZhouMoCity(List<ZhouMoCity> list) {
        lay_city.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            ZhouMoCity city = list.get(i);
            LinearLayout view = (LinearLayout) View.inflate(context, R.layout.fm_course3_item_citys, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_city);
            View line = view.findViewById(R.id.line);
            textView.setText(city.regionName);
            textView.setTag(city.regionId);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.golden_1));
                line.setBackgroundColor(getResources().getColor(R.color.golden_1));
                line.setVisibility(View.VISIBLE);
                lastView = view;
            } else {
                textView.setTextColor(getResources().getColor(R.color.gray_1));
                line.setBackgroundColor(getResources().getColor(R.color.gray_1));
                line.setVisibility(View.INVISIBLE);
            }
            lay_city.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView city;
                    View line;
                    if (lastView != null) {
                        city = (TextView) lastView.getChildAt(0);
                        line = lastView.getChildAt(1);
                        city.setTextColor(getResources().getColor(R.color.gray_1));
                        line.setVisibility(View.INVISIBLE);
                    }
                    LinearLayout newLay = (LinearLayout) v;
                    city = (TextView) newLay.getChildAt(0);
                    line = newLay.getChildAt(1);
                    city.setTextColor(getResources().getColor(R.color.golden_1));
                    line.setVisibility(View.VISIBLE);
                    lastView = newLay;

                    String regionId = (String) city.getTag();
                    loadCourseList(3, regionId);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CourseBean courseBean = (CourseBean) parent.getAdapter().getItem(position);
//        Intent  intent = new Intent(context, LuyingDetailAc.class);
//        intent.putExtra("goodsId", courseBean.goodsId);
//        startActivity(intent);
    }
}
