package com.bm.base.adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.BaseAd;
import com.bm.entity.Coach;
import com.bm.util.ProDialoging;
import com.bm.view.RatingBar;
import com.lib.http.ServiceCallback;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

public class CoachAdapter extends BaseAd<Coach>{
	private String	optType = "0";
	private ProDialoging progressDialog;

	public CoachAdapter(Context context,List<Coach> prolist){
		progressDialog = new ProDialoging(context);
		setActivity(context, prolist);
	}

	@Override
	protected View setConvertView(View convertView, final int position) {
		Viewholder viewholder = null;
		if(convertView  ==null){
			viewholder = new Viewholder();
			convertView	= mInflater.inflate(R.layout.item_list_coach, null);
			viewholder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			viewholder.tv_gender = (TextView)convertView.findViewById(R.id.tv_gender);
			viewholder.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
			viewholder.tv_bind = (TextView)convertView.findViewById(R.id.tv_bind);
			viewholder.room_ratingbar = (RatingBar) convertView.findViewById(R.id.room_ratingbar);
			
			convertView.setTag(viewholder);
		}else{
			viewholder = (Viewholder)convertView.getTag();
		}
		final Coach coach = mList.get(position);
		viewholder.tv_name.setText(coach.getCoachName()); 
		if ("2".equals(coach.getGender())) {
			viewholder.tv_gender.setText("女"); 
		}else{
			viewholder.tv_gender.setText("男"); 
		}
		viewholder.tv_age.setText(coach.getCoachAge()+"岁");
		if ("1".equals(coach.getIsBinding())) {//已经绑定
			viewholder.tv_bind.setBackgroundResource(R.drawable.btn_login_yellow);
			viewholder.tv_bind.setTextColor(Color.parseColor("#ffffff"));
			viewholder.tv_bind.setText("解除绑定");
		}else{
			viewholder.tv_bind.setBackgroundResource(R.drawable.btn_yellow_border);
			viewholder.tv_bind.setTextColor(Color.parseColor("#A59945"));
			viewholder.tv_bind.setText("立即绑定");
		}
		viewholder.tv_bind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("coachId", coach.getCoachId());
				if(null == App.getInstance().getUser()){
					map.put("userId", "0");
				}else{
					map.put("userId", App.getInstance().getUser().userid);
					
				}
				if ("1".equals(coach.getIsBinding())) {//已经绑定       解绑还是绑定   1 绑定  0解除
					optType = "0";
				}else{
					optType = "1";
				}
				map.put("optType", optType);
				showProgressDialog();
				UserManager.getInstance().getTzjcoachBindingcoach(context, map, new ServiceCallback<StringResult>() {
					
					@Override
					public void error(String msg) {
						hideProgressDialog();
						((BaseActivity) context).dialogToast(msg);
						
					}
					
					@Override
					public void done(int what, StringResult obj) {
						hideProgressDialog();
						Toast.makeText(context, "操作成功", 2000).show();
						mList.get(position).setIsBinding(optType);
						notifyDataSetChanged();
						
					}
				});
				
			}
		});
		viewholder.room_ratingbar.setRating(getNullIntData(coach.getCoachLogistics()));
		return convertView;
	}

	/**
	 * 判断值是否为空
	 * @param arg
	 * @return
	 */
	public static int getNullIntData(String arg) {
		if(TextUtils.isEmpty(arg)){
			return 0;
		}else {
			return Integer.parseInt(arg);
		}
	}
	class Viewholder{
		RatingBar room_ratingbar;
		TextView tv_name,tv_gender,tv_age,tv_bind;
	}
	public void showProgressDialog() {
		if(progressDialog!= null){
			if(((Activity) context).isFinishing()){
				 progressDialog.dismiss();
			}else{
				progressDialog.show();
			}
			
		}
	}
	public void hideProgressDialog() {
		progressDialog.hide();
	}
}
