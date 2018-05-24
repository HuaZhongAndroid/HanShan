package com.bm.im.ac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bm.api.IMService;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.im.adapter.MyGroupAdapter;
import com.bm.im.api.ImApi;
import com.bm.im.tool.Constant;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.widget.FuListView;
import com.richer.tzj.R;

/**
 * 好友
 * 
 * @author shity
 * 
 */
public class FriendAc extends BaseActivity implements OnClickListener{
	private Context mContext;
	private FuListView lv_friend,lv_group;
	List<User> listFriend = new ArrayList<User>();
	List<User> listGroup = new ArrayList<User>();
	private MyGroupAdapter friendAdapter ;
	private MyGroupAdapter groupAdapter ;
	private LinearLayout ll_group,ll_friend,ll_lookfriend;
	private boolean flagGroup=false,flagFriend=false;
	private ImageView iv_wdq,iv_wdhy;
	private View v_groups,v_friend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_friend);
		setTitleName("玩伴儿");
		mContext = this;
		setIbRightImg(R.drawable.icon_search);
		init();
	}
	
	@Override
	public void ibClickRight() {
		// TODO Auto-generated method stub
		super.ibClickRight();
		Intent intent = new Intent(mContext, AddFriendAc.class);
		intent.putExtra("pageType", "SearchFriend");
		startActivity(intent);
	}
	
	/**
	 * 初始化控件
	 */
	public void init(){
		v_groups=findViewById(R.id.v_groups);
		v_friend=findViewById(R.id.v_friend);
		lv_friend=(FuListView) findViewById(R.id.lv_friend);
		lv_group=(FuListView) findViewById(R.id.lv_groups);
		
		ll_friend=findLinearLayoutById(R.id.ll_friend);
		ll_group=findLinearLayoutById(R.id.ll_group);
		ll_lookfriend=findLinearLayoutById(R.id.ll_lookfriend);
		
		iv_wdq=findImageViewById(R.id.iv_wdq);
		iv_wdhy=findImageViewById(R.id.iv_wdhy);
		
		ll_group.setOnClickListener(this);
		ll_friend.setOnClickListener(this);
		ll_lookfriend.setOnClickListener(this);
		
		friendAdapter= new MyGroupAdapter(mContext, listFriend,"friend");
		lv_friend.setAdapter(friendAdapter);
		
		groupAdapter= new MyGroupAdapter(mContext, listGroup,"group");
		lv_group.setAdapter(groupAdapter);
		
		lv_friend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(listFriend.get(position).friendUserId.equals(App.getInstance().getUser().userid)){
					dialogToast("对不起，您不能和自己聊天");
					return;
				}
				//同步用户信息
				ImApi.syncUserInfo(listFriend.get(position));
				Intent intent = new Intent(mContext, ChatActivity.class);
				intent.putExtra("userId", listFriend.get(position).friendUserId);
				intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
				startActivity(intent);
			}
		});
		
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// 进入群聊
				Intent intent = new Intent(FriendAc.this, ChatActivity.class);
				intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
				intent.putExtra("userId", listGroup.get(position).groupId);
				startActivityForResult(intent, 0);
			}
		});
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		getMyFriends();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_group:  //我的群
			System.out.println("sdfsdfsdf");
			if(!flagGroup){
				lv_group.setVisibility(View.GONE);
				v_groups.setVisibility(View.GONE);
				iv_wdq.setImageResource(R.drawable.public_right);
				flagGroup=true;
			}else{
				lv_group.setVisibility(View.VISIBLE);
				v_groups.setVisibility(View.VISIBLE);
				iv_wdq.setImageResource(R.drawable.public_down);
				flagGroup=false;
			}
			break;
		case R.id.ll_friend:  //我的好友
			if(!flagFriend){
				lv_friend.setVisibility(View.GONE);
				v_friend.setVisibility(View.GONE);
				iv_wdhy.setImageResource(R.drawable.public_right);
				flagFriend=true;
			}else{
				lv_friend.setVisibility(View.VISIBLE);
				v_friend.setVisibility(View.VISIBLE);
				iv_wdhy.setImageResource(R.drawable.public_down);
				flagFriend=false;
			}
			break;
		case R.id.ll_lookfriend:  //找朋友
			Intent intent =new Intent(mContext, AddFriendAc.class);
			intent.putExtra("pageType", "AddFriend");
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 我的好友
	 */
	 
	public void getMyFriends() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		
		showProgressDialog();
		
		IMService.getInstance().findFriendList(mContext, map, new ServiceCallback<CommonListResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<User> obj) {
				if(obj.data.size() > 0){
					listFriend.clear();
					listFriend.addAll(obj.data);
				}
				friendAdapter.notifyDataSetChanged();
				getMyGroups();
			}
		});
	}
	/**
	 * 我的群
	 */
	 
	public void getMyGroups() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		
		
		IMService.getInstance().findGroupList(mContext, map, new ServiceCallback<CommonListResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<User> obj) {
				hideProgressDialog();
				if(obj.data.size() > 0){
					listGroup.clear();
					listGroup.addAll(obj.data);
				}
				groupAdapter.notifyDataSetChanged();
			}
		});
	}
}
