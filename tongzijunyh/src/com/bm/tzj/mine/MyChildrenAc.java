package com.bm.tzj.mine;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseActivity;
import com.bm.entity.Child;
import com.bm.entity.User;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

public class MyChildrenAc extends BaseActivity implements OnClickListener, OnItemClickListener {
	
	private ListView listView;
	
	private List<Child> dataList;
	
	private View addView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView(R.layout.ac_mychildren);
		setTitleName("我的孩子");
		
		listView = this.findListViewById(R.id.lv_children);
		
		addView = LayoutInflater.from(this).inflate(R.layout.item_list_mychild_bottom, listView, false);
		addView.setOnClickListener(this);
		listView.addFooterView(addView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		loadData();
	}



	private void loadData()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		if(null == App.getInstance().getUser()){
			map.put("userId", "0");
		}else{
			map.put("userId", App.getInstance().getUser().userid);
			
		}
		showProgressDialog();
		UserManager.getInstance().getChildrenlist(this, map, new ServiceCallback<CommonListResult<Child>>(){
			@Override
			public void done(int what, CommonListResult<Child> obj) {
				hideProgressDialog();
				dataList = obj.data;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void error(String msg) {
				hideProgressDialog();
				dialogToast(msg);
			}});
	}
	
	BaseAdapter adapter = new BaseAdapter()
	{

		@Override
		public int getCount() {
			if(dataList == null)
				return 0;
			if(dataList.size() >= 4)
				addView.setVisibility(View.GONE);
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null)
			{
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mychild, parent, false);
			}
			
			((TextView)convertView.findViewById(R.id.tv_name)).setText(dataList.get(position).realName);
			((TextView)convertView.findViewById(R.id.tv_age)).setText(dataList.get(position).age+"岁");
			if("1".equals(dataList.get(position).gender))
				((TextView)convertView.findViewById(R.id.tv_gender)).setText("男");
			else if("2".equals(dataList.get(position).gender))
				((TextView)convertView.findViewById(R.id.tv_gender)).setText("女");
			else
				((TextView)convertView.findViewById(R.id.tv_gender)).setText("");
			((TextView)convertView.findViewById(R.id.tv_birthday)).setText(dataList.get(position).birthday);
			
			ImageView iv_sixview_head = (ImageView)convertView.findViewById(R.id.iv_sixview_head);
			ImageLoader.getInstance().displayImage(dataList.get(position).avatar, iv_sixview_head, App.getInstance().getheadImage());

//			iv_sixview_head.setImageResource(R.drawable.add_child);
			
			return convertView;
		}
		
	};

	@Override
	public void onClick(View v) {
		if(v == addView)
		{
			//添加孩子
			this.startActivity(new Intent(this, AddChildAc.class));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user = App.getInstance().getUser();
		if(user != null)
		{
			Child child = dataList.get(position);
//			App.getInstance().setChild(child);
//			App.toast("选择孩子："+child.realName);
//			this.finish();
			
			Intent intent = new Intent(this, EditChildAc.class);
			intent.putExtra("child", child);
			this.startActivity(intent);
		}
	}
}
