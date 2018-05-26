package com.bm.base.adapter;

import android.view.View;

import com.bm.base.BaseAd;

public abstract class XiaoxiListAdapter<TAG extends XiaoxiListAdapter.XiaoXitemViewTag, T> extends BaseAd<T> {

    @Override
    protected View setConvertView(View convertView, int position) {
        TAG tag;
        if (convertView == null) {
            convertView = mInflater.inflate(loadLayout(), null);
            tag = instanceTAG(convertView);
        } else {
            tag = (TAG) convertView.getTag();
        }
        handViewAndData(tag, mList.get(position));
        return convertView;
    }


    protected abstract TAG instanceTAG(View view);

    protected abstract void handViewAndData(TAG tag, T data);

    protected abstract int loadLayout();


    public static class XiaoXitemViewTag {
        public View iteView;

        public XiaoXitemViewTag(View iteView) {
            this.iteView = iteView;
            this.iteView.setTag(this);
        }
    }


}
