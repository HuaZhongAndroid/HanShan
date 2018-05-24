package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Baby;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 课程旅伴适配器
 * @author shiyt
 *
 */
public class CourseCompanionAdapter  extends BaseAd<Baby>{
	public CourseCompanionAdapter(Context context,List<Baby> prolist){
		setActivity(context, prolist);
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
			convertView = mInflater.inflate(R.layout.item_list_coursecompanion, null);
			itemView.iv_sixview_head = (ImageView)convertView.findViewById(R.id.iv_sixview_head);
			itemView.tv_age=(TextView) convertView.findViewById(R.id.tv_age);
			
			itemView.tv_search=(TextView) convertView.findViewById(R.id.tv_search);
			itemView.tv_number=(TextView) convertView.findViewById(R.id.tv_number);
			itemView.tv_babyname=(TextView) convertView.findViewById(R.id.tv_babyname);
			itemView.tv_sex=(TextView) convertView.findViewById(R.id.tv_sex);
			itemView.tv_name=(TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		final Baby entity= mList.get(position);
		String strSex = "1".equals(entity.babyGender)?"男":"2".equals(entity.babyGender)?"女":"未知";
		itemView.tv_age.setText(strSex +"  | "+getNullData(entity.babyAge)+"岁");
		itemView.tv_number.setText("勋章数("+getNullData(entity.medalNum)+")");
		itemView.tv_babyname.setText(getNullData(entity.babyName));
		itemView.tv_sex.setText(strSex);
		itemView.tv_name.setText(entity.userName);
		ImageLoader.getInstance().displayImage(entity.avatar, itemView.iv_sixview_head,App.getInstance().getGrayHeadImage());
		
		if(null !=App.getInstance().getUser()){
			if(entity.userId.equals(App.getInstance().getUser().userid)){
				itemView.tv_search.setVisibility(View.GONE);
			}
		}else{
			itemView.tv_search.setVisibility(View.GONE);
		}
		
		
		//查看
		itemView.tv_search.setOnClickListener(new OnClickListener() {
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
		private ImageView iv_sixview_head;
		private TextView tv_search,tv_number,tv_age,tv_babyname,tv_sex,tv_name;
	}
}
