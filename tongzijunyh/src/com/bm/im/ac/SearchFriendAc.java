package com.bm.im.ac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.bm.api.IMService;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.im.adapter.SearchFriendAdapter;
import com.easemob.chat.EMContactManager;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.StringResult;
import com.richer.tzj.R;

/**
 * 查询好友
 * 
 * @author shiyt
 * 
 */
public class SearchFriendAc extends BaseActivity implements SearchFriendAdapter.OnSeckillClick {
	private Context mContext;
	private ListView lv_friend;
	private List<User> list = new ArrayList<User>();
	private SearchFriendAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_search_friend);
		setTitleName("找玩伴儿");
		mContext = this;
		init();
	}

	/**
	 * 初始化控件
	 */
	public void init() {
		lv_friend=findListViewById(R.id.lv_friend);
		adapter = new SearchFriendAdapter(mContext, list);
		lv_friend.setAdapter(adapter);
		adapter.setOnSeckillClick(this);
		getFriends();
	}

	/**
	 * 获取数据
	 */
	/**
	 * 找朋友
	 */
	 
	private void getFriends() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", getIntent().getStringExtra("keyWord"));
		map.put("userId", App.getInstance().getUser().userid);
		showProgressDialog();
		
		IMService.getInstance().getFriendsList(mContext, map, new ServiceCallback<CommonListResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonListResult<User> obj) {
				hideProgressDialog();
				if(obj.data.size() > 0){
					list.clear();
					list.addAll(obj.data);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	/**
	 * 加好友
	 */
	private void addFriend(final int position) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userid", App.getInstance().getUser().userid);
		map.put("friendId", list.get(position).userid);
		
		IMService.getInstance().addFriend(mContext, map, new ServiceCallback<StringResult>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, StringResult obj) {
				hideProgressDialog();
				dialogToast("您的请求已提交，等待对方验证");
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						Message message = new Message();
						handler.dispatchMessage(message);
					}
				}, 2000);
			}
		});
	}
	Handler handler=new Handler(){
		public void dispatchMessage(android.os.Message msg) {
//			Intent intent =new Intent(mContext, MainAc.class);
//			intent.putExtra("tag", "SearchFriendAc");
//			startActivity(intent);
			finish();
		};
	};
	
	@Override
	public void onSeckillClick(int position) {
		addContact(position);
	}
	/**
	 *  添加contact
	 * @param view
	 */
	public void addContact(final int position){
		showProgressDialog();
		new Thread(new Runnable() {
			public void run() {
				try {
					//demo写死了个reason，实际应该让用户手动填入
					String s = getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(list.get(position).nickname, s);
					runOnUiThread(new Runnable() {
						public void run() {
							addFriend(position);
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							hideProgressDialog();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}
}
