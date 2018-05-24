package com.bm.tzj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.PageDataList;
import com.bm.entity.StoreComment;
import com.bm.entity.Storelist;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.Pager;
import com.lib.widget.RatingBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 乐园-门店详情
 */
public class LeyuanAc extends BaseActivity implements View.OnClickListener {

    private Storelist data;

    private TextView tv_name,tv_juli,tv_address,tv_phone,tv_pinglunshu;
    private WebView tv_mark;
    private RatingBarView custom_ratingbar;
    private ImageView img_ad;
    private ListView lv_pinglun;

    private int pager;
    private final int p_size = 3;

    private List<StoreComment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_leyuan);
        context = this;
        setTitleName("乐园");

        this.findViewById(R.id.rl_top).setVisibility(View.GONE);
        this.findViewById(R.id.btn_back).setOnClickListener(this);

        data = (Storelist) this.getIntent().getSerializableExtra("data");

        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_juli = (TextView) this.findViewById(R.id.tv_juli);
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        tv_phone = (TextView) this.findViewById(R.id.tv_phone);
        tv_pinglunshu = (TextView) this.findViewById(R.id.tv_pinglunshu);
        tv_mark = (WebView) this.findViewById(R.id.tv_mark);
        lv_pinglun = (ListView) this.findViewById(R.id.lv_pinglun);

        img_ad = (ImageView) this.findViewById(R.id.img_ad);
        custom_ratingbar = (RatingBarView) this.findViewById(R.id.custom_ratingbar);

        tv_address.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
        this.findViewById(R.id.btn_gengduo).setOnClickListener(this);

        tv_name.setText(data.storeName);
        tv_juli.setText("距离"+data.distance);
        tv_address.setText(data.address);
        tv_phone.setText(data.tel);
        ImageLoader.getInstance().displayImage(data.acrossImage, img_ad, App.getInstance().getListViewDisplayImageOptions());
        custom_ratingbar.setStar(Integer.valueOf(data.rankLogistics==null?"0":data.rankLogistics), true);
        custom_ratingbar.setClickable(false);
//        tv_mark.setText(data.remark);
        Util.initViewWebData(tv_mark, data.remark+"");

        pager = 1;
        commentList = new ArrayList<StoreComment>();
        lv_pinglun.setAdapter(commentAdapter);
        getPinglun();
    }

    private BaseAdapter commentAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return commentList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_list_storecomment, parent, false);
            }

            StoreComment data = commentList.get(position);
            ((TextView)convertView.findViewById(R.id.tv_nickname)).setText(data.nickname);
            ((TextView)convertView.findViewById(R.id.tv_content)).setText(data.content);
            ((TextView)convertView.findViewById(R.id.tv_time)).setText(data.ctime);

            ImageLoader.getInstance().displayImage(data.avatar, (ImageView)convertView.findViewById(R.id.img_head), App.getInstance().getListViewDisplayImageOptions());

            return convertView;
        }
    };

    private void getPinglun()
    {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("storeId",data.storeId);
        map.put("currentPage",pager+"");
        map.put("pageSize",p_size+"");
        this.showProgressDialog();
        UserManager.getInstance().getStoreComment(context, map, new ServiceCallback<CommonResult<PageDataList<StoreComment>>>() {
            @Override
            public void done(int what, CommonResult<PageDataList<StoreComment>> obj) {
                hideProgressDialog();

                int total = Integer.valueOf(obj.data.total);
                tv_pinglunshu.setText("家长评论:"+total);
                if(obj.data.list != null)
                    commentList.addAll(obj.data.list);
                commentAdapter.notifyDataSetChanged();
                if(commentList.size()>=total)
                    findViewById(R.id.btn_gengduo).setVisibility(View.GONE);
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_gengduo:
                pager++;
                getPinglun();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(this,LocationMapAc.class);
                intent.putExtra("longitude", data.lon);
                intent.putExtra("latitude", data.lat);
                startActivity(intent);
                break;
            case R.id.tv_phone:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
