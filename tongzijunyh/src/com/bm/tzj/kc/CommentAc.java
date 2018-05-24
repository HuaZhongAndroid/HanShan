package com.bm.tzj.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.CommentAdapter;
import com.bm.entity.CoachInfo;
import com.bm.entity.Comment;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.tool.Pager;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 家长评价
 * @author shiyt
 *
 */
public class CommentAc extends BaseActivity implements OnRefreshListener{
	/** 分页器 */
	public Pager pager = new Pager(10);
	private SwipyRefreshLayout mSwipyRefreshLayout;
	
	private ListView lv_comment;
	private Context mContext;
	private List<Comment> listComment = new ArrayList<Comment>();  
	private CommentAdapter commentAdapter;
	private ImageView img_pic;
	private TextView tv_name,tv_age;
	private com.bm.view.RatingBar room_ratingbar;
	CoachInfo coachInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_comment);
		mContext = this;
		setTitleName("家长评价");
		init();
	}
	
	public void init(){
		
		coachInfo = (CoachInfo) getIntent().getSerializableExtra("coachInfo");
		mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
				R.color.color3, R.color.color4);
		
		tv_name = findTextViewById(R.id.tv_name);
		tv_age = findTextViewById(R.id.tv_age);
		img_pic=(ImageView) findViewById(R.id.img_pic);
		room_ratingbar=(com.bm.view.RatingBar) findViewById(R.id.room_ratingbar);
		
		lv_comment=(ListView) findViewById(R.id.lv_comment);
		commentAdapter=new CommentAdapter(mContext, listComment);
		lv_comment.setAdapter(commentAdapter);
		
		
		tv_name.setText(coachInfo.coachName);//教练名称
		tv_age.setText(coachInfo.coachAge ==""?"0岁":coachInfo.coachAge+"岁");
		ImageLoader.getInstance().displayImage(coachInfo.avatar, img_pic, App.getInstance().getHeadOptions());
			room_ratingbar.setRating(getNullIntData(coachInfo.coachLogistics)-1);
			getComment();
	}
	
	private void getComment(){
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("coachId", coachInfo.coachId);//教练ID
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		
		showProgressDialog();
		UserManager.getInstance().getTzjcoachCoachCommentlist(mContext, map, new ServiceCallback<CommonListResult<Comment>>() {
			
			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}
			
			@Override
			public void done(int what, CommonListResult<Comment> obj) {
				hideProgressDialog();
				
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),listComment.size()); 
					listComment.addAll(obj.data);
				}
				commentAdapter.notifyDataSetChanged();
			}
		});
		
	}
	
	
	/**
	 * 获取数据
	 */
	public void setData(){
		for (int i = 0; i < 5; i++) {
			listComment.add(new Comment());
		}
		commentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((CommentAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pager.setFirstPage();
							listComment.clear();
							getComment();
							// mSwipyRefreshLayout.setRefreshing(false);
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							
//							map.put("pageNum", pager.nextPage() + "");// 页码
//							pager.setCurrentPage(pager.nextPage(), list.size());
						}
					});
				}
			}, 2000);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((CommentAc) mContext).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getComment();
							
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);
							

						}
					});
				}
			}, 2000);
		}
	}
	
}
