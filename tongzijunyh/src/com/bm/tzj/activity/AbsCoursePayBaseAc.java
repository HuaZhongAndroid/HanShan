package com.bm.tzj.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.BaseAd;
import com.bm.entity.Child;
import com.bm.entity.Order;
import com.bm.tzj.kc.PayInfoAc2;
import com.bm.tzj.mine.AddChildAc;
import com.bm.view.CircleImageView;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 课程购买创建订单和选择孩子逻辑都在这
 */
abstract public class AbsCoursePayBaseAc extends BaseActivity {
    public static AbsCoursePayBaseAc intances;

    private PopupWindow popupWindow;
    private HorizontalListView hlistView;
    protected Child xz_child; //当前选择的孩子
    private List<Child> childList = new ArrayList<Child>();

    protected String goodsId, storeId, type; //支付创建订单所必需的参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intances = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadChildData();
    }

    /**
     * 弹出孩子选择框，进行支付
     * @param view
     */
    protected void showPopupWindow(View view) {
        if(childList.size()==0) {
            //跳转到添加孩子
            context.startActivity(new Intent(context, AddChildAc.class));
        }

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

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

            return convertView;
        }

        class Viewholder{
            TextView tv_name;
            CircleImageView img_head;
            View v_xz;
        }
    };


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

        hlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == childList.size()-1) {
                    //跳转到添加孩子
                    context.startActivity(new Intent(context, AddChildAc.class));
                    popupWindow.dismiss();
                }else{
                    //更换孩子
                    xz_child = childList.get(position);
                    childBaseAd.notifyDataSetChanged();
                }
            }
        });

        //支付按钮点击
        popLayout.findViewById(R.id.btn_zhifu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 生成订单
     */
    protected void createOrder(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsId", goodsId);//课程ID
        map.put("storeId", storeId); //门店id
        map.put("type", type);
        if(null == App.getInstance().getUser()){
            map.put("userId", "0");
        }else{
            map.put("userId", App.getInstance().getUser().userid);
        }
        if(null != xz_child)
            map.put("babyId", xz_child.babyId);
        else
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
                    onCreateOrderSuccess(obj.data);
                }
            }
        });
    }

    /**
     * 订单创建成功回调
     * @param order
     */
    abstract protected void onCreateOrderSuccess(Order order);
}
