package com.bm.tzj.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.bm.base.BaseActivity;
import com.richer.tzj.R;

/**
 * 发布评价
 * 
 * @author wanghy
 * 
 */
public class MyCommentAc extends BaseActivity implements OnClickListener,
		OnRatingBarChangeListener {
	private Context context;
	private TextView tv_submit;
	private EditText et_commentVenue, et_commentCoach;
	private RatingBar rb_scoreCoach, rb_scoreVenue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mycomment);
		context = this;
		setTitleName("发表评价");
		initView();
	}

	private void initView() {
		et_commentVenue = findEditTextById(R.id.et_commentVenue);
		et_commentCoach = findEditTextById(R.id.et_commentCoach);
		rb_scoreCoach = (RatingBar) findViewById(R.id.rb_scoreCoach);
		rb_scoreVenue = (RatingBar) findViewById(R.id.rb_scoreCoach);
		rb_scoreCoach.setOnRatingBarChangeListener(this);
		rb_scoreVenue.setOnRatingBarChangeListener(this);
		tv_submit = findTextViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
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

}
