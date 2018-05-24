package com.bm.base.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.app.App;
import com.bm.base.BaseAd;
import com.bm.entity.Model;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 我的私人教练适配器
 * @author jiangsh
 *
 */
public class MyTeachersAdapter  extends BaseAd<Model>{
	public MyTeachersAdapter(Context context,List<Model> prolist){
		setActivity(context, prolist);
	}
	
	@Override
	protected View setConvertView(View convertView, final int position) {
		ItemView itemView = null;
		if(convertView  ==null){
			itemView = new ItemView();
			convertView = mInflater.inflate(R.layout.item_list_my_teacher, null);
			itemView.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			itemView.iv_pic = (ImageView)convertView.findViewById(R.id.iv_pic);
			itemView.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			itemView.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
			itemView.tv_bd = (TextView)convertView.findViewById(R.id.tv_bd);
			convertView.setTag(itemView);
		}else{
			itemView = (ItemView)convertView.getTag();
		}
		
		Model entity= mList.get(position);
		ImageLoader.getInstance().displayImage("http://xbyx.cersp.com/xxzy/UploadFiles_7930/200808/20080810110053944.jpg", itemView.iv_pic,App.getInstance().getHeadOptions());
//		itemView.tv_name.setText(getNullData(entity.name));//姓名
//		itemView.tv_content.setText(getNullData(entity.time));//内容
//		itemView.tv_age.setText(getNullData(entity.time)+"岁");//年龄
		
		
		return convertView;
	}
	class ItemView{
		private TextView tv_name,tv_content,tv_age,tv_bd;
		private ImageView iv_pic,ic_start;
	
	}
}
