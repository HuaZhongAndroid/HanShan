package com.bm.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.entity.Course;
import com.bm.entity.Jiaotongfei;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.richer.tzj.R;
import com.sina.weibo.sdk.api.share.Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 交通费选择框
 */
public class JiaotongfeiPop extends PopupWindow {

    private Context context;
    private List<Jiaotongfei> dataList;

    private List<List<Jiaotongfei>> hangList = new ArrayList<List<Jiaotongfei>>();

    private OnClickListener listener;

    private Jiaotongfei selected;

    public JiaotongfeiPop(Context context) {
        super(context);

        this.context = context;

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View popLayout = LayoutInflater.from(context).inflate(R.layout.pop_jiaotongfei,null);
        popLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(popLayout);
        this.setBackgroundDrawable(new ColorDrawable(0x99000000));
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        ListView lv_a = (ListView)popLayout.findViewById(R.id.lv_a);
        lv_a.setAdapter(adapter);

        loadData();
    }

    private void loadData()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        UserManager.getInstance().get_trafficFee_list(context, map, new ServiceCallback<CommonListResult<Jiaotongfei>>() {
            @Override
            public void done(int what, CommonListResult<Jiaotongfei> obj) {

                if(obj.data != null && obj.data.size() > 0) {
                    dataList = obj.data;
                    String price = dataList.get(0).price;
                    List<Jiaotongfei> hang = new ArrayList<Jiaotongfei>();
                    hangList.add(hang);
                    for(Jiaotongfei data:dataList)
                    {
                        if (data.price==null)continue;
                        if(!data.price.equals(price))
                        {
                            price = data.price;
                            hang = new ArrayList<Jiaotongfei>();
                            hangList.add(hang);
                        }
                        hang.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void error(String msg) {
            }
        });
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return hangList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_pop_jiaotongfei, parent, false);
            }

            TextView tv_price = (TextView)convertView.findViewById(R.id.tv_price);
            GridView gridView = (GridView)convertView.findViewById(R.id.gridView);
            GridAdapter gridAdapter = new GridAdapter();
            gridAdapter.jtfList = hangList.get(position);
            gridView.setAdapter(gridAdapter);

            tv_price.setText("￥"+hangList.get(position).get(0).price);

            return convertView;
        }
    };

    private class GridAdapter extends BaseAdapter
    {
        List<Jiaotongfei> jtfList;

        @Override
        public int getCount() {
            return jtfList==null?0:jtfList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_jiaotongfei, parent, false);
            }

            final Jiaotongfei jiaotongfei = jtfList.get(position);
            TextView tv_city = (TextView)convertView.findViewById(R.id.tv_city);
            tv_city.setText(jiaotongfei.city);
            if(jiaotongfei == selected)
            {
                tv_city.setTextColor(0xffffffff);
                tv_city.setBackgroundResource(R.drawable.bg_jiaotongfei_btn_a);
            }
            else {
                tv_city.setTextColor(0xff333333);
                tv_city.setBackgroundResource(R.drawable.bg_jiaotongfei_btn);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected == jiaotongfei)
                    {
                        selected = null; //取消选择
                    }
                    else {
                        selected = jiaotongfei;
                    }
                    adapter.notifyDataSetChanged();
                    if(listener != null)
                        listener.onclick(selected);
                    dismiss();
                }
            });

            return convertView;
        }
    }

    public void setSListener(OnClickListener l)
    {
        listener = l;
    }

    public static interface OnClickListener
    {
        public void onclick(Jiaotongfei jiaotongfei);
    }
}
