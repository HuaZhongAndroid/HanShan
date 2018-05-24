package com.bm.base.adapter;

		import java.util.List;

		import android.content.Context;
		import android.content.Intent;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.widget.TextView;

		import com.bm.app.App;
		import com.bm.base.BaseAd;
		import com.bm.entity.Child;
		import com.bm.tzj.fm.GrowUpFragment;
		import com.bm.tzj.mine.AddChildAc;
		import com.bm.view.CircleImageView;
		import com.nostra13.universalimageloader.core.ImageLoader;
		import com.richer.tzj.R;

public class ChildAdapter extends BaseAd<Child> {

	public ChildAdapter(Context context, List<Child> childList) {
		Child child =new Child();
		childList.add(child);
		setActivity(context, childList);
	}
	@Override
	protected View setConvertView(View convertView, final int position) {
		Viewholder viewholder = null;
		if(convertView  ==null){
			viewholder = new Viewholder();
			convertView	= mInflater.inflate(R.layout.item_child, null);
			viewholder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			viewholder.img_head = (CircleImageView)convertView.findViewById(R.id.img_head);
			convertView.setTag(viewholder);
		}else{
			viewholder = (Viewholder)convertView.getTag();
		}
		Child child = mList.get(position);
		if (position == mList.size()-1) {
			viewholder.tv_name.setText("添加孩子");
			ImageLoader.getInstance().displayImage(child.avatar, viewholder.img_head, App.getInstance().getAddImage());
		}else{
			viewholder.tv_name.setText(child.realName);
			ImageLoader.getInstance().displayImage(child.avatar, viewholder.img_head, App.getInstance().getGrayHeadImage());
		}
//		viewholder.img_head.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (position == mList.size()-1) {
//					//跳转到添加孩子
//					context.startActivity(new Intent(context, AddChildAc.class));
//					popupWindow.dismiss();
//				}else{
//					//更换孩子
//					growUpFragment.changeChild(position);
//				}
//			}
//		});

		return convertView;
	}


	class Viewholder{
		TextView tv_name;
		CircleImageView img_head;
	}

}
