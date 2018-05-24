package com.bm.tzj.ts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.base.adapter.MyExploreReplayAdapter;
import com.bm.base.adapter.PicAdapter;
import com.bm.dialog.UtilDialog;
import com.bm.entity.CommentList;
import com.bm.entity.PlayMateList;
import com.bm.tzj.activity.ImageViewActivity;
import com.bm.util.Util;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.tool.Pager;
import com.lib.widget.FuGridView;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout.OnRefreshListener;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;


/**
 * 探索详情
 * @author wanghy
 *
 */
public class ExploreDetailAc extends BaseActivity implements OnClickListener,OnRefreshListener{
	private Context context;
	private TextView tv_exploreName,tv_exploreTime,tv_explore_content,tv_explore_chart,tv_explore_zan,tv_explore_count,tv_explore_zanName,tv_exploreDel;
	private ImageView tv_exploreImg;
	private FuGridView gv_explore_pic;
	private View v_count;
	private ListView lv_listReplay;
	private LinearLayout ll_cz,ll_jb,ll_zan;
	private MyExploreReplayAdapter replayAdapter;
	private PicAdapter picAdapter;
	private List<CommentList> list = new ArrayList<CommentList>();
	private ImageView img_zan;
	
	//判断是从我的探索进来还是探索进来
	String strType = "";
	//判断是从玩伴儿进来还是探索进来
	String strDisplay ="";
	String strIsZan="";//是否赞
	
	private SwipyRefreshLayout mSwipyRefreshLayout;
	/** 分页器 */
	public Pager pager = new Pager(10);
	
	private ListView lv_exploreDetail;
	
	private String[] picArr = new String[]{
			"http://www.totalfitness.com.cn/upfile/681x400/20120323104125_324.jpg",
			"http://d.hiphotos.baidu.com/zhidao/pic/item/d31b0ef41bd5ad6e1e7e401f83cb39dbb7fd3cbb.jpg",
			"http://elegantliving.ceconline.com/TEAMSITE/IMAGES/SITE/ES/20080203_EL_ES_02_02.jpg"};
	String [] paths;
	
	private PlayMateList playMateList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_exploredetail);
		context = this;
		isHaveData(false);
		initView();
	}
	
	private void initView(){
		strType = getIntent().getStringExtra("strType");
		strDisplay = getIntent().getStringExtra("type");
		
		mSwipyRefreshLayout = (SwipyRefreshLayout)findViewById(R.id.swipyrefreshlayout);
		mSwipyRefreshLayout.setOnRefreshListener(this);
		mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
		mSwipyRefreshLayout.setColorScheme(R.color.color1, R.color.color2,R.color.color3, R.color.color4);
		
		lv_exploreDetail = findListViewById(R.id.lv_exploreDetail);
		initHeadView();
		
		replayAdapter = new MyExploreReplayAdapter(context,list);
		lv_exploreDetail.setAdapter(replayAdapter);
		
		if(strType.equals("FindFm")){
			setTitleName("探索详情");
		}else{
			setTitleName("我的探索详情");
		}
		
		getTSDetial();
		getPLList();
		
	}
	/**
	 * 获取探索详情
	 */
	public void getTSDetial(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("userid", App.getInstance().getUser().userid);
		UserManager.getInstance().getProbeDetial(context, map, new ServiceCallback<CommonResult<PlayMateList>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<PlayMateList> obj) {
				hideProgressDialog();
				playMateList=obj.data;
				setData(playMateList);
			}
		});
	}
	/**
	 * 获取评论列表
	 */
	public void getPLList(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("pageNum", pager.nextPageToStr());//查询页数
		map.put("pageCount", "10");//每页展示条数
		UserManager.getInstance().getCommentList(context, map, new ServiceCallback<CommonListResult<CommentList>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<CommentList> obj) {
				hideProgressDialog();
				if(obj.data.size()>0){
					pager.setCurrentPage(pager.nextPage(),list.size()); 
					list.addAll(obj.data);
				}
				replayAdapter.notifyDataSetChanged();
			}
		});
	}
	/**
	 * 添加赞
	 */
	public void addZan(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("userId", App.getInstance().getUser().userid);
		UserManager.getInstance().getArticleAddLaud(context, map, new ServiceCallback<CommonResult<PlayMateList>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<PlayMateList> obj) {
				hideProgressDialog();
				getTSDetial();
			}
		});
	}
	
	/**
	 * 取消赞
	 */
	public void delZan(){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("userId", App.getInstance().getUser().userid);
		UserManager.getInstance().getArticleDelLaud(context, map, new ServiceCallback<CommonResult<PlayMateList>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<PlayMateList> obj) {
				hideProgressDialog();
				getTSDetial();
			}
		});
	}
	
	/**
	 * 添加举报
	 */
	public void addJB(String content){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("userId", App.getInstance().getUser().userid);
		map.put("content", content);
		UserManager.getInstance().getArticleAddReport(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				dialogToast("举报成功");
			}
		});
	}
	/**
	 * 添加回复
	 */
	public void addHF(String content){
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", getIntent().getStringExtra("id"));//帖子id
		map.put("userId", App.getInstance().getUser().userid);
		map.put("content", content);
		UserManager.getInstance().getArticleAddComment(context, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
				isHaveData(false);
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				 pager.setFirstPage();
				 list.clear();
				getTSDetial();
				getPLList();
			}
		});
	}
	private void setData(PlayMateList playMateList){
		strIsZan = playMateList.isArticleLaud;
		if(playMateList.isArticleLaud.equals("1")){
			img_zan.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.explore_zanname));
		}else{
			img_zan.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.explore_zan));
		}
		
		tv_exploreName.setText(getNullData(playMateList.nickname));//课程名称
		tv_exploreTime.setText(Util.toString(getNullData(playMateList.postDate), "yyyy-MM-dd HH:mm:ss",  "yyyy.MM.dd HH:mm"));//时间
		tv_explore_content.setText(getNullData(playMateList.articleContent));//评论内容
		tv_explore_zan.setText(getNullData("赞  ("+playMateList.praiseCount+")"));//点赞人数
		tv_explore_count.setText(getNullData("评论  ("+playMateList.replyCount+")"));//评论数
		ImageLoader.getInstance().displayImage(playMateList.avatar, tv_exploreImg,App.getInstance().getheadImage());
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < playMateList.praiseUserName.size(); i++) {
			if(playMateList.praiseUserName.size()-1==i){
				sb.append(playMateList.praiseUserName.get(i).nickName);
			}else{
				sb.append(playMateList.praiseUserName.get(i).nickName+",");
			}
		}
		if(playMateList.praiseUserName.size()>0){
			ll_zan.setVisibility(View.VISIBLE);
			tv_explore_zanName.setText(sb.toString());//点赞名单
			v_count.setVisibility(View.GONE);
		}else{
			ll_zan.setVisibility(View.GONE);
			v_count.setVisibility(View.VISIBLE);
		}

		paths=new String[playMateList.path.size()];
		for (int i = 0; i < playMateList.path.size(); i++) {
			paths[i]=playMateList.path.get(i).titleMultiUrl;
		}
		 
		picAdapter = new PicAdapter(context,paths);
		gv_explore_pic.setAdapter(picAdapter);
		
		gv_explore_pic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(context, ImageViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("images",paths);
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		

		isHaveData(true);
	}
	
//	private void setData() {
//		for (int i = 0; i < 5; i++) {
//			Model info = new Model();
//			info.name= "丽萨" + (i + 3) +":";
//			info.content="宝贝很棒哦"+(i+4);
//			list.add(info);
//		}
//		replayAdapter.notifyDataSetChanged();
//		tv_exploreName.setText(getNullData("嘻嘻"));//课程名称
//		tv_exploreTime.setText(getNullData("2015.8.12 15:00"));//时间
//		tv_explore_content.setText(getNullData("今天上课非常开心，学到了很多的知识宝宝很勇敢。谢谢教练"));//评论内容
//		tv_explore_chart.setText("(30)");
//		tv_explore_zan.setText(getNullData("赞  (15)"));//点赞人数
//		tv_explore_count.setText(getNullData("评价   (20)"));//评论数
//		tv_explore_zanName.setText(getNullData("安妮,爱探险的朵拉"));//点赞名单
//		ImageLoader.getInstance().displayImage("http://xbyx.cersp.com/xxzy/UploadFiles_7930/200808/20080810110053944.jpg", tv_exploreImg,App.getInstance().getHeadOptions());
//		
//	}
	
	/**
	 * 初始化HeadView信息
	 */
	public void initHeadView(){
		View headView = getLayoutInflater().inflate(R.layout.ac_head_explore, null);
		ll_cz=(LinearLayout) headView.findViewById(R.id.ll_cz);
		ll_jb=(LinearLayout) headView.findViewById(R.id.ll_jb);
		ll_zan=(LinearLayout) headView.findViewById(R.id.ll_zan);
		img_zan = (ImageView) headView.findViewById(R.id.img_zan);
		tv_exploreName = (TextView)headView.findViewById(R.id.tv_exploreName);
		tv_exploreImg = (ImageView)headView.findViewById(R.id.tv_exploreImg);
		tv_exploreTime = (TextView)headView.findViewById(R.id.tv_exploreTime);
		tv_explore_content = (TextView)headView.findViewById(R.id.tv_explore_content);
		gv_explore_pic= (FuGridView)headView.findViewById(R.id.gv_explore_pic);
		tv_explore_chart = (TextView)headView.findViewById(R.id.tv_explore_chart);
		tv_explore_zan = (TextView)headView.findViewById(R.id.tv_explore_zan);
		tv_explore_count = (TextView)headView.findViewById(R.id.tv_explore_count);
		tv_explore_zanName = (TextView)headView.findViewById(R.id.tv_explore_zanName);
		tv_exploreDel = (TextView) headView.findViewById(R.id.tv_exploreDel);
		tv_exploreDel.setVisibility(View.GONE);
		v_count = headView.findViewById(R.id.v_count);
		tv_explore_count.setOnClickListener(this);
		tv_explore_chart.setOnClickListener(this);
		tv_explore_zan.setOnClickListener(this);
		ll_jb.setOnClickListener(this);
//		if(strType.equals("FindFm")){
//			ll_cz.setVisibility(View.VISIBLE);
//		}else{
//			ll_cz.setVisibility(View.VISIBLE);
//		}
		
		if(strDisplay.equals("1")){
			ll_jb.setVisibility(View.GONE);
		}else{
			ll_cz.setVisibility(View.VISIBLE);
		}
		lv_exploreDetail.addHeaderView(headView);
		
//		headView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context,GrowthCenterAc.class);
//				intent.putExtra("pageType","HonoRollAc");
//				intent.putExtra("friendId",getNullData(entity.userid));
//				intent.putExtra("friendBabyId",getNullData(entity.babyId));
//				context.startActivity(intent);
//			}
//		});
	}

	private boolean flag=true;//true表示回复，false表示举报
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_explore_zan://赞
				if(strIsZan.equals("0")){//添加赞
					 addZan();
				}else{
					delZan();//取消赞
				}
				
			break;
			case R.id.tv_explore_count://恢复
				flag=true;
				UtilDialog.dialogPromtMessage(context,handler);
			break;
			case R.id.ll_jb://举报
				flag=false;
				UtilDialog.dialogPromtMessage(context,handler);
	//			UtilDialog.dialogTwoBtnContentTtile(context, "确定举报该条信息", "取消","确定","提示",handler,2);
				break;

		default:
			break;
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 回复每输入额内容
				dialogToast("请输入内容");
				break;
			case 1:// 确定事件
				if(flag){
					addHF(msg.obj+"");
				}else{
					addJB(msg.obj+"");
				}
				break;
			}
			;
		};
	};
	@Override
	public void onRefresh(SwipyRefreshLayoutDirection direction) {
		if (direction == SwipyRefreshLayoutDirection.TOP) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((ExploreDetailAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							 pager.setFirstPage();
							 list.clear();
							// mSwipyRefreshLayout.setRefreshing(false);
							 getPLList();;
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

							
						}
					});
				}
			}, 2000);

		} else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Hide the refresh after 2sec
					((ExploreDetailAc) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getPLList();;
							// 刷新设置
							mSwipyRefreshLayout.setRefreshing(false);

						}
					});
				}
			}, 2000);
		}
	}
}
