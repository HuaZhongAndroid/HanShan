package com.bm.tzj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Baby;
import com.bm.entity.Course;
import com.bm.entity.Order;
import com.bm.share.ShareModel;
import com.bm.tzj.kc.GrowthCenterAc;
import com.bm.tzj.kc.PayInfoAc3;
import com.bm.tzj.mine.LoginAc;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.HashMap;

/**
 * 大露营详情 (周末也复用)
 */
public class LuyingDetailAc extends AbsCoursePayBaseAc implements View.OnClickListener {

    private TextView tv_baoming,tv_name,tv_age,tv_price,tv_renshu,tv_zhuyi,tv_describe;

    private ImageView img_collect,img_share,img_ad,btn_back;

    private HorizontalListView hlistview;

    private String goodsId;

    private Course data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView(R.layout.ac_luying_detail);

        rl_top.setVisibility(View.GONE);

        tv_baoming = this.findTextViewById(R.id.tv_baoming);
        tv_name = this.findTextViewById(R.id.tv_name);
        tv_age = this.findTextViewById(R.id.tv_age);
        tv_price = this.findTextViewById(R.id.tv_price);
        tv_renshu = this.findTextViewById(R.id.tv_renshu);
        tv_zhuyi = this.findTextViewById(R.id.tv_zhuyi);
        tv_describe = this.findTextViewById(R.id.tv_describe);

        img_collect = this.findImageViewById(R.id.img_collect);
        img_collect.setOnClickListener(this);
        img_share = this.findImageViewById(R.id.img_share);
        img_share.setOnClickListener(this);
        img_ad = this.findImageViewById(R.id.img_ad);
        btn_back = this.findImageViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        hlistview = (HorizontalListView)this.findViewById(R.id.hlistview);

        goodsId = this.getIntent().getStringExtra("goodsId");

        loadData();
    }

    @Override
    protected void onCreateOrderSuccess(Order order) {
        Intent intent = new Intent(context, PayInfoAc3.class);


        order.realName  = xz_child.realName;
        order.goodsType = data.goodsType;
        order.goodsTime = data.goodsTime;
        order.goodsName = data.goodsName;

        intent.putExtra("course", data);
        intent.putExtra("order",  order);
        intent.putExtra("child",xz_child);
        intent.putExtra("pageTag","AbsCoursePayBaseAc");
        startActivity(intent);
    }

    private void loadData()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goodsId", goodsId);
        if(null != App.getInstance().getUser()){
            map.put("userId", App.getInstance().getUser().userid);
        }else{
            map.put("userId", "0");
        }

        if(null != App.getInstance().getChild())
            map.put("babyId", App.getInstance().getChild().babyId);

        showProgressDialog();
        UserManager.getInstance().getTzjgoodsGoodsdetail2(context, map, new ServiceCallback<CommonResult<Course>>() {

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }

            @Override
            public void done(int what, CommonResult<Course> obj) {
                hideProgressDialog();
                if(null !=obj.data){
                    data=obj.data;
                    loadView();
                }
            }
        });
    }

    private void loadView()
    {
        ImageLoader.getInstance().displayImage(data.titleMultiUrl, img_ad, App.getInstance().getListViewDisplayImageOptions());
        tv_name.setText(data.goodsName);
        tv_age.setText(data.suitableAge+"岁");
        tv_price.setText("￥"+data.goodsPrice);
        tv_renshu.setText("已报名孩子 ("+data.enrollQuota+"/"+data.goodsQuota+")");
        tv_zhuyi.setText(Html.fromHtml(data.notice));
        tv_describe.setText(Html.fromHtml(data.coursePoints));

        hlistview.setAdapter(adapter);
        if(adapter.getCount() == 0)
            hlistview.setVisibility(View.GONE);

        if(Integer.valueOf(data.goodsQuota) - Integer.valueOf(data.enrollQuota) > 0)
        {
            tv_baoming.setBackgroundColor(0xffA59945);
            tv_baoming.setOnClickListener(this);
        }
        else
        {
            tv_baoming.setBackgroundColor(0xffDFDFDF);
        }

        isCollected = "1".equals(data.isCollected);
        if(isCollected)
            img_collect.setImageResource(R.drawable.icon_collection_off);
        else
            img_collect.setImageResource(R.drawable.icon_collection);  //未收藏图片

    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.babyList == null?0:data.babyList.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_child, parent, false);
            }

            final Baby baby = data.babyList.get(position);
            ImageView img_head = (ImageView) convertView.findViewById(R.id.img_head);
            ImageLoader.getInstance().displayImage(baby.babyAvatar, img_head, App.getInstance().getListViewDisplayImageOptions());
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_name.setText(baby.babyName);
            tv_name.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,GrowthCenterAc.class);
                    intent.putExtra("pageType","FindFm");
                    intent.putExtra("friendId",getNullData(baby.userId));
                    intent.putExtra("friendBabyId",getNullData(baby.babyId));
                    context.startActivity(intent);
                }
            });

            return convertView;
        }
    };

//    public void isLogin(){
//        UtilDialog.dialogTwoBtnContentTtile(context, "您还未登录，请先登录在操作", "取消","确定","提示",new Handler(){
//            public void handleMessage(android.os.Message msg) {
//                switch(msg.what){
//                    case 1:
//                        finish();
//                        MainAc.intance.finish();
//                        Intent intent = new Intent(context,LoginAc.class);
//                        startActivity(intent);
//                        break;
//                }
//            }
//        },1);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share: // 分享
                ShareModel model = new ShareModel();
                model.title = "我参加了" + data.goodsName + ",你也快来看看吧~";
                model.conent = "我参加了" + data.goodsName + ",你也快来看看吧~";
                model.targetUrl = BaseApi.SHARE_URL + "&userid=" + App.getInstance().getUser().userid + "&appVersion=" + Util.getAppVersion(context);
                share.shareInfo(model, 0);
                break;
            case R.id.img_collect:// 收藏
                if (null == App.getInstance().getUser()) {
                    isLogin();
                    return;
                }

                if (!isCollected) {
                    addCollection();
                } else {
                    delCollection();
                }
                break;
            case R.id.tv_baoming:
                super.goodsId = data.goodsId;
                super.storeId = "-1";
                super.type = "10";
                showPopupWindow(v);
                break;
        }
    }

    private boolean isCollected;//是否已收藏
    /**
     * 添加收藏
     */
    public void addCollection(){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("baseId", data.goodsId);//课程ID
        if(null != App.getInstance().getUser()){
            map.put("userid", App.getInstance().getUser().userid);//用户ID
        }else{
            map.put("userid", "0");//教练ID
        }

        showProgressDialog();
        UserManager.getInstance().getTzjgoodsGoodsCollect(context, map, new ServiceCallback<StringResult>() {

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
            @Override
            public void done(int what, StringResult obj) {
                hideProgressDialog();
                isCollected = true;
                img_collect.setImageResource(R.drawable.icon_collection_off);  //已收藏图片
//				flag=true;
            }
        });
    }

    /**
     * 删除收藏
     */
    public void delCollection(){
        if(null == App.getInstance().getUser()){
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("baseId", data.goodsId);//课程ID
        if(null != App.getInstance().getUser()){
            map.put("userid", App.getInstance().getUser().userid);//用户ID
        }else{
            map.put("userid", "0");//教练ID
        }
        map.put("delType", "1");//删除类型   0 全部清空  1 按课程id删除   2 删除失效课程

        showProgressDialog();
        UserManager.getInstance().getMyselfDelCollection(context, map, new ServiceCallback<StringResult>() {
            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
            @Override
            public void done(int what, StringResult obj) {
                hideProgressDialog();
                isCollected = false;
                img_collect.setImageResource(R.drawable.icon_collection);  //未收藏图片
//				flag=false;
            }
        });
    }
}
