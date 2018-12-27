package com.bm.tzj.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.ComContentData;
import com.bm.entity.ComRatinData;
import com.bm.entity.CommentList;
import com.bm.entity.Course;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.ObjResult;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

import java.util.HashMap;

/**
 * 发布的评价
 *
 * @author wanghy
 */
public class CheckCommentAc extends BaseActivity implements OnClickListener,
        OnRatingBarChangeListener {
    private Context context;
    private TextView tv_submit;
    private TextView tv_comment;
    private ImageView iv_status;
    private RatingBar rb_scoreCoach, rb_scoreVenue;
    private Course hotGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_checkcomment);
        context = this;
        setTitleName("查看评价");
        initView();
    }

    private void initView() {
        hotGoods = (Course) getIntent().getSerializableExtra("hotGoods");
        tv_comment = findTextViewById(R.id.tv_comment);
        rb_scoreCoach = findViewById(R.id.rb_scoreCoach);
        rb_scoreVenue = findViewById(R.id.rb_scoreCoach);
        rb_scoreCoach.setEnabled(false);
        rb_scoreVenue.setEnabled(false);
        tv_submit = findTextViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        getCommectStatus();
        getRatin();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:

                break;
            default:
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
        String strScoreCoach = rb_scoreCoach.getRating() + "";
        String strScoreVenue = rb_scoreVenue.getRating() + "";
    }

    /**
     * 获取星级
     */
    private void getRatin() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", App.getInstance().getUser().userid);//用户ID
        map.put("babyid", hotGoods.babyId);//
        map.put("toid", hotGoods.goodsId);//
        map.put("evaluateuserid", App.getInstance().getUser().userid);
        AsyncHttpHelp.httpGet(context, BaseApi.API_TZJGOODS_CHECK_COMMENT_RATIN, map, new ServiceCallback<CommonListResult<ComRatinData>>() {
            @Override
            public void done(int what, CommonListResult<ComRatinData> obj) {
                hideProgressDialog();
                if (obj.status == 1&&obj.data!=null) {
                    for (int i = 0; i <   obj.data.size(); i++) {
                        ComRatinData comRatinData =  obj.data.get(i);
                        if (comRatinData.evaluateType == 1) {
                            rb_scoreCoach.setRating(comRatinData.logistics);
                        } else if (comRatinData.evaluateType == 2) {
                            rb_scoreVenue.setRating(comRatinData.logistics);
                        } else if (comRatinData.evaluateType == 3) {
                        }
                    }
                }
            }

            @Override
            public void error(String msg) {
                dialogToast(msg);
                hideProgressDialog();
            }
        });
    }

    /**
     * 获取评价内容以及是否通过
     */
    private void getCommectStatus() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", App.getInstance().getUser().userid);
        map.put("babyid", hotGoods.babyId);
        map.put("goodsid", hotGoods.goodsId);
        map.put("coachid", hotGoods.coachId);
        AsyncHttpHelp.httpGet(context, BaseApi.API_TZJGOODS_CHECK_COMMENT_STATUS, map, new ServiceCallback<CommonResult<ComContentData>>() {
            @Override
            public void error(String msg) {
                dialogToast(msg);
                hideProgressDialog();
            }
            @Override
            public void done(int what, CommonResult<ComContentData> obj) {
                hideProgressDialog();
                if (obj.status == 1&&obj.data!=null) {
                    tv_comment.setText(obj.data.content);
                    if (obj.data.passStatus == 1) {
                        iv_status.setImageResource(R.drawable.tongguo);
                    } else {
                        iv_status.setImageResource(R.drawable.tongguo_un);
                    }
                }
            }
        });
    }


}
