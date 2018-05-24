package com.bm.im.ac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.api.IMService;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.User;
import com.bm.im.adapter.GroupInfoAdapter;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 群信息
 * @author shiyt
 *
 */
public class GroupInfoAc extends BaseActivity implements OnClickListener {
	private Context mContext;
	private GridView gv_person;
	private List<User> list = new ArrayList<User>();
	GroupInfoAdapter adapter ;
	private ImageView img_head;
	private TextView tv_name,tv_search,tv_newfriend;
	String groupName="",strSearch="";
	private EditText et_seachName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_group);
		setTitleName("群信息");
		mContext = this;
		init();
	}
	
	/**
	 * 初始化控件
	 */
	public void init(){
		et_seachName = findEditTextById(R.id.et_seachName);
		tv_search=findTextViewById(R.id.tv_search);
		tv_newfriend=findTextViewById(R.id.tv_newfriend);
		img_head=(ImageView) findViewById(R.id.img_head);
		gv_person= (GridView) findViewById(R.id.gv_person);
		adapter=new GroupInfoAdapter(mContext, list);
		gv_person.setAdapter(adapter);
		tv_name=findTextViewById(R.id.tv_name);
		gv_person.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//同步用户信息
				//ImApi.syncUserInfo(list.get(position));
//				Intent intent = new Intent(mContext, ChatActivity.class);
//				intent.putExtra("userId", list.get(position).userId);
//				intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
//				startActivity(intent);
			}
		});
		
		
		getGroupInfo();
		
		tv_search.setOnClickListener(this);
	}
	
	 

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search:
			strSearch = et_seachName.getText().toString().trim();
			getGroupInfo();
			break;

		default:
			break;
		}
		
	}
	/**
	 * 群信息
	 */
	private void getGroupInfo() {
		showProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("groupId", getIntent().getStringExtra("groupId"));
		if(strSearch.length()>0){
			map.put("phoneOrName", strSearch);
		}
		IMService.getInstance().findGroupInfo(mContext, map, new ServiceCallback<CommonResult<User>>() {
			
			@Override
			public void error(String msg) {
				dialogToast(msg);
				hideProgressDialog();
			}
			
			@Override
			public void done(int what, CommonResult<User> obj) {
				hideProgressDialog();
				if(null!=obj.data){
					setData(obj.data);
				}
			}
		});
	}
	/**
	 * 设置数据
	 */
	private void setData(User user){
		if(TextUtils.isEmpty(user.groupPic)){
			img_head.setImageResource(R.drawable.ease_groups_icon);
		}else{
			ImageLoader.getInstance().displayImage(user.groupPic, img_head,App.getInstance().getGroupHeadOptions());
		}
		
		tv_name.setText(user.groupName);
		
		list.clear();
		if(user.groupUserList.size()>0){
			list.addAll(user.groupUserList);
			tv_newfriend.setText("群成员（"+user.userCount+"）");
		}else{
			tv_newfriend.setText("群成员（0）");
		}
		adapter.notifyDataSetChanged();
	}
}
