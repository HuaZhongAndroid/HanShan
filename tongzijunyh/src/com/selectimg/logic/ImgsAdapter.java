
package com.selectimg.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.richer.tzj.R;

public class ImgsAdapter extends BaseAdapter {

    Context context;
    List<String> data;
    public Bitmap bitmaps[];
    Util util;
    OnItemClickClass onItemClickClass;
    private int index = -1;
    private ArrayList<String> list;
    List<View> holderlist;

    public ImgsAdapter(Context context, List<String> data,
            OnItemClickClass onItemClickClass, ArrayList<String> list) {
        this.context = context;
        this.data = data;
        this.onItemClickClass = onItemClickClass;
        bitmaps = new Bitmap[data.size()];
        util = new Util(context);
        this.list = list;
        holderlist = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public View getView(int pos, View v, ViewGroup vg) {
        Holder holder;
        if (pos != index && pos > index) {
            index = pos;
            v = LayoutInflater.from(context).inflate(R.layout.imgsitem, vg,
                    false);
            holder = new Holder();
            holder.imageView = (ImageView) v.findViewById(R.id.imageView1);
            holder.checkBox = (CheckBox) v.findViewById(R.id.checkBox1);
            v.setTag(holder);
            holderlist.add(v);
        } else {
            holder = (Holder) holderlist.get(pos).getTag();
            v = holderlist.get(pos);
        }
        if (bitmaps[pos] == null) {
            util.imgExcute(holder.imageView, new ImgClallBackLisner(pos),
                    data.get(pos));
        } else {
            holder.imageView.setImageBitmap(bitmaps[pos]);
        }
        v.setOnClickListener(new OnPhotoClick(pos, holder.checkBox));

        if (pos == this.getCount() - 1) // 最后一个加个底部padding为了适配界面
            v.setPadding(0, 0, 0, Util.dip2px(context, 50));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(data.get(pos))) {
                holder.checkBox.setChecked(true);
            }
            if (list.size() == 9) {

            }
        }
        return v;
    }

    class Holder {
        ImageView imageView;
        CheckBox checkBox;
    }

    public class ImgClallBackLisner implements ImgCallBack {
        int num;

        public ImgClallBackLisner(int num) {
            this.num = num;
        }

        @Override
        public void resultImgCall(ImageView imageView, Bitmap bitmap) {
            bitmaps[num] = bitmap;
            imageView.setImageBitmap(bitmap);
        }
    }

    public interface OnItemClickClass {
        public void OnItemClick(View v, int Position, CheckBox checkBox);
    }

    class OnPhotoClick implements OnClickListener {
        int position;
        CheckBox checkBox;

        public OnPhotoClick(int position, CheckBox checkBox) {
            this.position = position;
            this.checkBox = checkBox;
        }

        @Override
        public void onClick(View v) {
            if (data != null && onItemClickClass != null) {
                onItemClickClass.OnItemClick(v, position, checkBox);
            }
        }
    }

}