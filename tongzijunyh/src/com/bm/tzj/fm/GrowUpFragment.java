package com.bm.tzj.fm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureFragmentActivity;
import com.bm.base.BaseFragmentActivity;
import com.bm.base.adapter.ChildAdapter;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.dialog.UtilDialog;
import com.bm.entity.Child;
import com.bm.entity.GrowUp;
import com.bm.entity.GrowUpImg;
import com.bm.entity.User;
import com.bm.tzj.activity.ImageViewActivity;
import com.bm.tzj.activity.MainAc;
import com.bm.tzj.mine.AddChildAc;
import com.bm.tzj.ts.SendGrowUpAc;
import com.bm.util.GlideUtils;
import com.bm.util.Util;
import com.bumptech.glide.Glide;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.log.Lg;
import com.lib.widget.FuGridView;
import com.lib.widget.HorizontalListView;
import com.lib.widget.RoundImageViewsix;
import com.lib.widget.refush.NsRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayout;
import com.lib.widget.refush.SwipyRefreshLayoutDirection;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

/**
 * 成长中心
 */
public class GrowUpFragment extends Fragment implements OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        SwipyRefreshLayout.OnRefreshListener,
        MainAc.OnTabActivityResultListener{

    private CollapsingToolbarLayout rl_bg;
    private ImageView img_addchild;
    private ImageView img_pubulish;
    private ThreeButtonDialog buttonDialog;
    private Context context;
    private TextView tv_name;
    public List<String> uploadListImg = new ArrayList<String>();
    private TextView tv_describe;
    private List<Child> dataList = new ArrayList<Child>();
    private HorizontalListView hlistView;
    private PopupWindow popupWindow;
    private ChildAdapter adapter;
    private User user;
    private ImageView img_bg;

    private ListView lv_main;

    private List<GrowUp> growUpList = new ArrayList<GrowUp>();

    private int page;
    private final int pageSize = 10;

    private View v_none;

    private Toolbar toolbar;
    private AppBarLayout app_bar;
    private TextView tv_title;

    private NestedScrollView nsv;

    private SwipyRefreshLayout swipyrefreshlayout;

    private boolean notFlush = false; //标识此次界面返回不刷新数据


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater
                .inflate(R.layout.fm_growup2, container, false);
        context = getActivity();
        MainAc.intance.setOnTabActivityResultListener(this);
        initView(messageLayout);
        return messageLayout;
    }

    private void initView(View view) {
        lv_main = (ListView) view.findViewById(R.id.lv_main);
        lv_main.setAdapter(mainAdapter);

        v_none = view.findViewById(R.id.v_none);

        nsv = (NestedScrollView) view.findViewById(R.id.nsv);

        swipyrefreshlayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        swipyrefreshlayout.setOnRefreshListener(this);
        swipyrefreshlayout.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
//		swipyrefreshlayout.setEnabled(false);
        swipyrefreshlayout.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//		toolbar.setTitle("成长中心");
        app_bar = (AppBarLayout) view.findViewById(R.id.app_bar);
        app_bar.getLayoutParams().height = App.getInstance().getScreenWidth() / 16 * 9;
        app_bar.addOnOffsetChangedListener(this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        user = App.getInstance().getUser();
        tv_describe = (TextView) view.findViewById(R.id.tv_describe);
        rl_bg = (CollapsingToolbarLayout) view.findViewById(R.id.rl_bg);
        img_bg = (ImageView) view.findViewById(R.id.img_bg);
        rl_bg.setOnClickListener(this);
        img_addchild = (ImageView) view.findViewById(R.id.img_addchild);
        img_addchild.setOnClickListener(this);
        img_pubulish = (ImageView) view.findViewById(R.id.img_pubulish);
        img_pubulish.setOnClickListener(this);
        if (user!=null&&user.coverImg != null && user.coverImg.length() > 0)
            ImageLoader.getInstance().displayImage(user.coverImg, img_bg, App.getInstance().getBgImage());
//		rl_bg.setBackground(bd);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        buttonDialog = new ThreeButtonDialog(context).setFirstButtonText("拍照")
                .setBtn1Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (MainAc.intance.isLogin())
                        ((BaseCaptureFragmentActivity) context).takePhoto();
                    }
                }).setThecondButtonText("从相册选择")
                .setBtn2Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (MainAc.intance.isLogin())
                        ((BaseCaptureFragmentActivity) context).pickPhoto();

                    }
                }).autoHide();

        Child child = App.getInstance().getChild();
        if (child != null) {
            ImageLoader.getInstance().displayImage(child.avatar, img_addchild, App.getInstance().getheadImage());
            tv_name.setText(child.realName);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (notFlush) {
            notFlush = false;
            return;
        }
        reflush();
    }

    private void reflush() {
        if (App.getInstance().getUser()==null)return;
        page = 1;
        growUpList.clear();
        mainAdapter.notifyDataSetChanged();
        loadMainData();
        loadChildData();
    }


    private BaseAdapter mainAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return growUpList.size();
        }

        @Override
        public Object getItem(int position) {
            return growUpList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_growup, parent, false);
            }
            TextView tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            View v_jiaolian = convertView.findViewById(R.id.v_jiaolian);
            TextView tv_jiaolianName = (TextView) convertView.findViewById(R.id.tv_jiaolianName);
            TextView tv_mendianName = (TextView) convertView.findViewById(R.id.tv_mendianName);
            ImageView img_tag = (ImageView) convertView.findViewById(R.id.img_tag);
            ImageView img_a = (ImageView) convertView.findViewById(R.id.img_a);
            TextView tv_pingjia_tip = (TextView) convertView.findViewById(R.id.tv_pingjia_tip);
            FuGridView fgv_a = (FuGridView) convertView.findViewById(R.id.fgv_a);
            View btn_menu = convertView.findViewById(R.id.btn_menu);

            final GrowUp data = growUpList.get(position);
            Date date = Util.StringToDate(data.ctime, "yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            sdf.format(date);
            tv_day.setText(Util.getStringDate(date, "dd"));
            tv_date.setText(Util.getStringDate(date, "yyyy-MM HH:mm"));
            tv_content.setText(data.content);
            if ("1".equals(data.recordFlag)) //家长发布
            {
                tv_day.setTextColor(0xff333333);
                convertView.findViewById(R.id.v_jiaolian).setVisibility(View.GONE);
                img_tag.setImageResource(R.drawable.jiaoya);
                btn_menu.setVisibility(View.VISIBLE);
                btn_menu.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainAc.intance.isLogin())
                        popMenu(v, data);
                    }
                });
                tv_pingjia_tip.setVisibility(View.INVISIBLE);
                v_jiaolian.setVisibility(View.GONE);
            } else {
                tv_day.setTextColor(0xffA59945);
                tv_jiaolianName.setText(data.coachName + "教练点评");
                tv_mendianName.setText(data.storeName);
                convertView.findViewById(R.id.v_jiaolian).setVisibility(View.VISIBLE);
                img_tag.setImageResource(R.drawable.koushao);
                btn_menu.setVisibility(View.GONE);
                v_jiaolian.setVisibility(View.VISIBLE);
                tv_pingjia_tip.setVisibility(View.VISIBLE);
            }

            if (position == 0) //第一行处理
            {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) convertView.findViewById(R.id.v_shuxian).getLayoutParams();
                RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) img_tag.getLayoutParams();
                lp.setMargins(lp.leftMargin, lp2.topMargin, lp.rightMargin, lp.bottomMargin);
            } else {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) convertView.findViewById(R.id.v_shuxian).getLayoutParams();
                lp.setMargins(lp.leftMargin, 0, lp.rightMargin, lp.bottomMargin);
            }

            //图片加载
            if (data.attachmentlist == null || data.attachmentlist.size() == 0) {
                img_a.setVisibility(View.GONE);
                fgv_a.setVisibility(View.GONE);
            } else if (data.attachmentlist.size() > 1) {
                img_a.setVisibility(View.GONE);
                fgv_a.setVisibility(View.VISIBLE);
                ImgAdapter apt = new ImgAdapter();
                apt.list = data.attachmentlist;
                fgv_a.setAdapter(apt);
            } else if (data.attachmentlist.size() == 1) {
                img_a.setVisibility(View.VISIBLE);
                fgv_a.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(data.attachmentlist.get(0).url, img_a, App.getInstance().getListViewDisplayImageOptions());
                img_a.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MainAc.intance.isLogin())return;
                        String[] paths = new String[data.attachmentlist.size()];
                        for (int i = 0; i < data.attachmentlist.size(); i++) {
                            paths[i] = data.attachmentlist.get(i).url;
                        }
                        Intent intent = new Intent(context,
                                ImageViewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("images", paths);
                        bundle.putInt("position", 0);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        notFlush = true;
                    }
                });
            }

            return convertView;
        }
    };

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            Log.d("fff", "到底了 " + verticalOffset);
            tv_title.setVisibility(View.GONE);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            Log.d("fff", "到顶了 " + verticalOffset);
            tv_title.setVisibility(View.VISIBLE);
        } else if (Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange()) {
            Log.d("fff", "在中间 " + verticalOffset);
            tv_title.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {

        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            loadMainData();
            swipyrefreshlayout.setRefreshing(false);
        }
    }




    class ImgAdapter extends BaseAdapter {
        private List<GrowUpImg> list = new ArrayList<GrowUpImg>();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_growup_pic, parent, false);
                int s = (int) ((context.getResources().getDisplayMetrics().widthPixels - Util.dpToPx(60, context.getResources())) / 3.3);
                convertView.getLayoutParams().width = s;
                convertView.getLayoutParams().height = s;
            }

            RoundImageViewsix iv_pic = (RoundImageViewsix) convertView.findViewById(R.id.iv_pic);
            //ImageLoader.getInstance().displayImage(list.get(position).url, iv_pic, App.getInstance().getListViewDisplayImageOptions());
            GlideUtils.loadImg(context, list.get(position).url, iv_pic, R.drawable.defult_shape);
            //Lg.e("debug==>pivUrl" + list.get(position).url);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MainAc.intance.isLogin())return;
                    String[] paths = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        paths[i] = list.get(i).url;
                    }
                    Intent intent = new Intent(context,
                            ImageViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", paths);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    notFlush = true;
                }
            });

            return convertView;
        }
    }

    private void loadMainData() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) return;
        map.put("userId", App.getInstance().getUser().userid);
        if (App.getInstance().getChild() == null)
            map.put("babyId", "-1");
        else
            map.put("babyId", App.getInstance().getChild().babyId);
        map.put("currentPage", page + "");
        map.put("pageSize", pageSize + "");

        ((BaseFragmentActivity) getActivity()).showProgressDialog();
        UserManager.getInstance().getGrowthRecord_list(getActivity(), map, new ServiceCallback<CommonListResult<GrowUp>>() {
            @Override
            public void done(int what, CommonListResult<GrowUp> obj) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                if (obj.data != null) {
                    if (obj.data.size() == 0) {
                        swipyrefreshlayout.setEnabled(false);
                    } else {
                        growUpList.addAll(obj.data);
                        mainAdapter.notifyDataSetChanged();
                        page++;
                        swipyrefreshlayout.setEnabled(true);
                    }
                }
                if (growUpList.size() > 0)
                    v_none.setVisibility(View.GONE);
                else
                    v_none.setVisibility(View.VISIBLE);
            }

            @Override
            public void error(String msg) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                ((BaseFragmentActivity) getActivity()).dialogToast(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bg:
                if (!MainAc.intance.isLogin())return;
                buttonDialog.show();
                break;
            case R.id.img_addchild:
                if (!MainAc.intance.isLogin())return;
                if (dataList != null && dataList.size() > 0) {
                    showPopupWindow(img_addchild);
                } else {
                    //跳转到添加孩子
                    Intent intent = new Intent(context, AddChildAc.class);
                    getActivity().startActivityFromFragment(GrowUpFragment.this,intent,100);
                }
                break;
            case R.id.img_pubulish:
                if (!MainAc.intance.isLogin())return;
                Intent intent = new Intent(context, SendGrowUpAc.class);
                context.startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void showPopupWindow(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        popupWindow.showAtLocation(view, Gravity.TOP, x - (view.getWidth() / 2), y);
    }

    /**
     * 设置背景图，并提交
     */
    @SuppressLint("NewApi")
    public void setbg(String imagePath) {
//		ImageLoader.getInstance().displayImage("file://"+imagePath,  new BgImageViewAware(rl_bg),null);
//		Drawable background=Drawable.createFromPath(imagePath);
//		rl_bg.setBackground(background);
        ImageLoader.getInstance().displayImage("file://" + imagePath, img_bg, App.getInstance().getBgImage());
        tv_describe.setVisibility(View.GONE);
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) return;
        map.put("userId", App.getInstance().getUser().userid);
        ((BaseFragmentActivity) getActivity()).showProgressDialog();
        UserManager.getInstance().API_UPLOAD_COVER(context, imagePath, map, new ServiceCallback<CommonResult<StringResult>>() {

            @Override
            public void error(String msg) {
                ((BaseFragmentActivity) getActivity()).dialogToast(msg);
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
            }

            @Override
            public void done(int what, CommonResult<StringResult> obj) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadChildData() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) return;
        if (null == App.getInstance().getUser()) {
            map.put("userId", "0");
        } else {
            map.put("userId", App.getInstance().getUser().userid);

        }
        ((BaseFragmentActivity) getActivity()).showProgressDialog();
        UserManager.getInstance().getChildrenlist(getActivity(), map, new ServiceCallback<CommonListResult<Child>>() {
            @Override
            public void done(int what, CommonListResult<Child> obj) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                dataList.clear();
                dataList.addAll(obj.data);
                makePopWindow();
            }

            @Override
            public void error(String msg) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                ((BaseFragmentActivity) getActivity()).dialogToast(msg);
            }
        });
    }

    protected void makePopWindow() {
        popupWindow = new PopupWindow(getActivity());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        View popLayout = LayoutInflater.from(getActivity()).inflate(com.richer.tzj.R.layout.layout_popupwindow_child, null);
        hlistView = (HorizontalListView) popLayout.findViewById(R.id.hlistview);
        adapter = new ChildAdapter(getActivity(), dataList);
        hlistView.setAdapter(adapter);
        popupWindow.setContentView(popLayout);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        popLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        hlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (!MainAc.intance.isLogin())return;
                if (position == dataList.size() - 1) {
                    //跳转到添加孩子
                    Intent intent = new Intent(context, AddChildAc.class);
                    getActivity().startActivityFromFragment(GrowUpFragment.this,intent,100);
                } else {
                    //更换孩子
                    changeChild(position);
                }
            }
        });
    }

    /**
     * 弹出操作菜单
     *
     * @param v
     */
    private void popMenu(View v, final GrowUp data) {
        final PopupWindow pop = new PopupWindow(getActivity());
        View popLayout = LayoutInflater.from(getActivity()).inflate(R.layout.chengzhang_menu, null);
        pop.setContentView(popLayout);
        pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        backgroundAlpha(0.5f);
        pop.showAsDropDown(v);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        //编辑按钮
        popLayout.findViewById(R.id.btn_edit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();

                Intent i = new Intent(context, SendGrowUpAc.class);
                i.putExtra("data", data);
                startActivity(i);
            }
        });
        //删除按钮
        popLayout.findViewById(R.id.btn_del).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();

                UtilDialog.dialogTwoBtnContentTtile(context, null, "取消", "删除", "确定要删除该条记录吗？", new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        if (msg.what == 14)
                            return;
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("pkid", data.pkid);
                        ((BaseFragmentActivity) getActivity()).showProgressDialog();
                        UserManager.getInstance().getGrowthRecord_delete(context, map, new ServiceCallback<StringResult>() {
                            @Override
                            public void done(int what, StringResult obj) {
                                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                                reflush();
                            }

                            @Override
                            public void error(String msg) {
                                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                            }
                        });
                    }
                }, 1);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    public void  setChildReflush(){
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) return;
        if (null == App.getInstance().getUser()) {
            map.put("userId", "0");
        } else {
            map.put("userId", App.getInstance().getUser().userid);

        }
        ((BaseFragmentActivity) getActivity()).showProgressDialog();
        UserManager.getInstance().getChildrenlist(getActivity(), map, new ServiceCallback<CommonListResult<Child>>() {
            @Override
            public void done(int what, CommonListResult<Child> obj) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                dataList.clear();
                dataList.addAll(obj.data);
                if (dataList.size()>=2)
                changeChild(dataList.size()-2);
            }

            @Override
            public void error(String msg) {
                ((BaseFragmentActivity) getActivity()).hideProgressDialog();
                ((BaseFragmentActivity) getActivity()).dialogToast(msg);
            }
        });

    }

    public void changeChild(int positon) {
        if (dataList != null && dataList.size() > 0) {
            Child child = dataList.get(positon);
            ImageLoader.getInstance().displayImage(child.avatar, img_addchild, App.getInstance().getheadImage());
            tv_name.setText(child.realName);
            App.getInstance().setChild(child);
            reflush();
        }
    }


    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setChildReflush();
                }
            },1000);
        }
    }
}
