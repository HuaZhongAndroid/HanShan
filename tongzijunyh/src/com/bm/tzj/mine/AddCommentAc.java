package com.bm.tzj.mine;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Course;
import com.bm.entity.HotGoods;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

/**
 * 发表评价
 * @author shiyt
 *
 */
public class AddCommentAc extends BaseActivity implements OnClickListener {
	private Context context;
	TextView tv_submit;
	private LinearLayout ll_jl,ll_cs;
	private EditText et_commentCamp,et_commentCoach;
	private TextView tv_name,tv_namepj;
	int campCount=-1,coachCount=-1;
	String strCommentCamp="",strCommentCoach="";
	Course hotGoods;
	private com.bm.view.RatingBar room_ratingbar,room_ratingbars;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_addcomment);
		context = this;
		setTitleName("发表评论");
		initView();
	}
	
	
	public void initView(){
//		room_ratingbar=(RatingBar) findViewById(R.id.room_ratingbar);
//		room_ratingbars=(RatingBar) findViewById(R.id.room_ratingbars);
		hotGoods = (Course) getIntent().getSerializableExtra("hotGoods");
		tv_name = findTextViewById(R.id.tv_name);
		tv_namepj=findTextViewById(R.id.tv_namepj);
		et_commentCamp = findEditTextById(R.id.et_commentCamp);//城市营地
		et_commentCoach = findEditTextById(R.id.et_commentCoach);//教练
		ll_jl = findLinearLayoutById(R.id.ll_jl);
		ll_cs = findLinearLayoutById(R.id.ll_cs);
		
		room_ratingbar=(com.bm.view.RatingBar) findViewById(R.id.room_ratingbar);
		room_ratingbars=(com.bm.view.RatingBar) findViewById(R.id.room_ratingbars);
		
		tv_submit=findTextViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
		ll_jl.setVisibility(View.GONE);
		ll_cs.setVisibility(View.GONE);
		
		
		room_ratingbar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				coachCount=(int) room_ratingbar.getRating();
				if (coachCount == 5) {
					ll_jl.setVisibility(View.GONE);
				}else{
					ll_jl.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		
		room_ratingbars.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				
				campCount=(int) room_ratingbars.getRating();
				if (campCount == 5) {
					ll_cs.setVisibility(View.GONE);
				}else{
					ll_cs.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});
		
		setDate();
	}
	
	public void setDate(){
		if (!TextUtils.isEmpty(hotGoods.goodsName)){
			tv_name.setText(hotGoods.goodsName);
			tv_name.setVisibility(View.VISIBLE);
		}else {
			tv_name.setVisibility(View.GONE);
		}


//		if(hotGoods.goodsType.equals("1")){
//			tv_namepj.setText("评价城市营地");
//			if(!TextUtils.isEmpty(hotGoods.goodsCategory)){
//				if(hotGoods.goodsCategory.equals("1")){
//					tv_name.setText("【体验课程】"+getNullData(hotGoods.goodsName));
//				}else if(hotGoods.goodsCategory.equals("2")){
//					tv_name.setText("【晋级课程】"+getNullData(hotGoods.goodsName));
//				}
//			}
//		}else if(hotGoods.goodsType.equals("2")){
//			tv_namepj.setText("评价暑期大露营");
//		}else{
//			tv_namepj.setText("评价周末户外");
//			if (!TextUtils.isEmpty(hotGoods.goodsCategory)) {
//				if (hotGoods.goodsCategory.equals("1")) {
//					tv_name.setText("【体验课程】" + getNullData(hotGoods.goodsName));
//				} else if (hotGoods.goodsCategory.equals("2")) {
//					tv_name.setText("【晋级课程】" + getNullData(hotGoods.goodsName));
//				}
//			}
//		}
	}
	
	/**
	 * 弹出框
	 */
	public void dialogContent(){
		float start=room_ratingbar.getRating();
		float starts=room_ratingbars.getRating();
		if(start<4 && starts<4){
			//弹出框
			UtilDialog.dialogTwoBtnRefused(context,"取消", "确认", handler, 2);
		}else{
			dialogToast("提交");
		}
	}
	
	/**
	 * 提交评价
	 */
	public void submitContent(){
		strCommentCamp=et_commentCamp.getText().toString().trim();//课程评价
		strCommentCoach=et_commentCoach.getText().toString().trim();//教练评价
		if(coachCount<=4){
			if(strCommentCoach.length()==0){
			dialogToast("五颗星以下的教练评价内容不能为空");
			return;
			}
		}
		
		if(campCount<=4){
			if(strCommentCamp.length()==0){
			dialogToast("五颗星以下的评价城市营地内容不能为空");
			return;
			}
		}
		
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);//用户ID
		map.put("baseId", hotGoods.goodsId);//课程ID
		map.put("goodsName", hotGoods.goodsName);//课程名称
		map.put("stoContent", strCommentCamp);
		map.put("coaContent", strCommentCoach);
		map.put("storeStar", campCount+"");
		map.put("coachStar", coachCount+"");
		map.put("coachId", hotGoods.coachId);
		map.put("goodsType", hotGoods.goodsType);//课程类型
		map.put("babyId", hotGoods.babyId);
		if(hotGoods.goodsType.equals("1")){//门店
			map.put("storeId", hotGoods.storeId+"");//门店ID
		}	
		UserManager.getInstance().getGoodsAddComment(context, map, new ServiceCallback<StringResult>() {

			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}

			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				setResult(200);
				finish();
			}
		});
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				//submitContent(msg.obj.toString());
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			submitContent();
			break;
		default:
			break;
		}
	}
}
