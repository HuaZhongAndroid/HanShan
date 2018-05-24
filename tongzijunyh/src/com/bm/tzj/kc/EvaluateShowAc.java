package com.bm.tzj.kc;

import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.api.BaseApi;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.CoachComment;
import com.bm.entity.Course;
import com.bm.entity.EvaluateContent;
import com.bm.entity.HotGoods;
import com.bm.view.RatingBar;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.richer.tzj.R;

public class EvaluateShowAc extends BaseActivity{

	private Course hotGoods;
	
	private TextView tv_pingjia_r,tv_pingjia_t,tv_dianping,tv_goodsName;
	private ImageView img_tongguo;
	private RatingBar teacher_ratingbar,room_ratingbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_evaluate_show);
		setTitleName("查看评价");
		hotGoods = (Course) getIntent().getSerializableExtra("hotGoods");
		
		tv_pingjia_r = this.findTextViewById(R.id.tv_pingjia_r);
		tv_pingjia_t = this.findTextViewById(R.id.tv_pingjia_t);
		tv_dianping = this.findTextViewById(R.id.tv_dianping);
		tv_goodsName = this.findTextViewById(R.id.tv_goodsName);
		img_tongguo = this.findImageViewById(R.id.img_tongguo);
		teacher_ratingbar = (RatingBar)this.findViewById(R.id.teacher_ratingbar);
		room_ratingbar = (RatingBar)this.findViewById(R.id.room_ratingbar);
		
		tv_goodsName.setText(hotGoods.goodsName);
		
		loadDianping();
		loadEvaluate();
	}

	//加载教练点评
	private void loadDianping()
	{
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("coachid", hotGoods.coachId);
		map.put("goodsid", hotGoods.goodsId);
		map.put("babyid", hotGoods.babyId);
		AsyncHttpHelp.httpGet(this,BaseApi.API_coachCommentByBaby, map, new ServiceCallback<CommonResult<CoachComment>>(){
			@Override
			public void done(int what, CommonResult<CoachComment> obj) {
				if(obj.data != null)
				{
					tv_dianping.setText(obj.data.content);
					if("1".equals(obj.data.passStatus))
					{
						img_tongguo.setImageResource(R.drawable.tongguo);
					}
					else
					{
						img_tongguo.setImageResource(R.drawable.tongguo_un);
					}
				}
				else {
					img_tongguo.setVisibility(View.GONE);
					tv_dianping.setText("未评论");
				}
			}
			@Override
			public void error(String msg) {
			}});
	}
	
	//加载我的评价
	private void loadEvaluate()
	{
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("toid", hotGoods.goodsId);
		map.put("evaluateuserid", App.getInstance().getUser().userid);
		map.put("babyid", hotGoods.babyId);
		AsyncHttpHelp.httpGet(this,BaseApi.API_tzjcomment, map, new ServiceCallback<CommonListResult<EvaluateContent>>(){
			@Override
			public void done(int what, CommonListResult<EvaluateContent> obj) {
				if(obj.data != null && obj.data.size() > 0)
				{
					for(EvaluateContent e:obj.data)
					{
						if("1".equals(e.evaluateType)) //评价教练
						{
							teacher_ratingbar.setRating(Float.parseFloat(e.logistics));
							tv_pingjia_t.setText(e.evaluateContent);
						}
						else if("2".equals(e.evaluateType)) //评价门店
						{
							room_ratingbar.setRating(Float.parseFloat(e.logistics));
							tv_pingjia_r.setText(e.evaluateContent);
						}
					}
				}
			}
			@Override
			public void error(String msg) {
			}});
	}
}
