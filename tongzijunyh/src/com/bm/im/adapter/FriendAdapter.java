package com.bm.im.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 新朋友适配器
 * @author shiyt
 *
 */
public class FriendAdapter  extends BaseAd<User>{
	private String pageType;
	public FriendAdapter(Context context,List<User> prolist,String pageType){
		setActivity(context, prolist);
		this.pageType=pageType;
	}
	
	private OnSeckillClick onSeckillClick;
	public void setOnSeckillClick(OnSeckillClick onSeckillClick){
		this.onSeckillClick = onSeckillClick;
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_friend, null);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.img_head = (ImageView)convertView.findViewById(R.id.img_head);
			itemView.tv_add=(TextView) convertView.findViewById(R.id.tv_add);
			
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		User entity= mList.get(position);
		itemView.tv_name.setText(entity.nickname);
		//从搜索进入
		if("SearchFriend".equals(pageType)){
			itemView.tv_add.setVisibility(View.GONE);
		}else{
			//从找朋友进入
			itemView.tv_add.setVisibility(View.VISIBLE);
			if(!TextUtils.isEmpty(entity.friStatus)){
				if(entity.friStatus.equals("0")){
					itemView.tv_add.setText("同意");
					itemView.tv_add.setBackgroundResource(R.drawable.btn_short);
				}else if (entity.friStatus.equals("3")){
					itemView.tv_add.setText("已申请");
					itemView.tv_add.setBackgroundResource(R.drawable.btn_short_gray);
				}else{
					itemView.tv_add.setText("已添加");
					itemView.tv_add.setBackgroundResource(R.drawable.btn_short_gray);
				}
			}else{
				itemView.tv_add.setText("已添加");
				itemView.tv_add.setBackgroundResource(R.drawable.btn_short_gray);
			}
			
		}
		
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.img_head,App.getInstance().getheadImage());
		
		//同意
		itemView.tv_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSeckillClick.onSeckillClick(position);
			}
		});
		return convertView;
	}
	
	public interface OnSeckillClick{
		void onSeckillClick(int position);
	}
	class ItemView{
		private TextView tv_name,tv_add;
		private ImageView img_head;
	
	}
}
