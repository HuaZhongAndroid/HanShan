package com.bm.tzj.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.bm.api.BaseApi;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.entity.Adverts;
import com.bm.entity.Province;
import com.bm.entity.UpgradeInfo;
import com.bm.tzj.city.City;
import com.bm.util.ApkUpdateUtil;
import com.bm.util.BDLocationHelper;
import com.bm.util.BDLocationHelper.LocationInfo;
import com.bm.util.BDLocationHelper.MyLocationListener;
import com.bm.util.GlobalPrams;
import com.bm.util.Util;
import com.lib.http.AsyncHttpHelp;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonListResult;
import com.lib.http.result.CommonResult;
import com.lib.tool.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.richer.tzj.R;

/**
 * 启动页
 *
 * @author wanghy
 */
public class StartAc extends Activity {

    /**
     * 该类是进入app的第一个界面，显示开启app的动画
     *
     * @author LiXuetao
     */
    private Context context;
    public static final int SKIP_GUIDE = 0x001;
    public static final int SKIP_MAIN = 0x002;
    SharedPreferences sharedPreferences;
    // 该线程用于延迟跳转activity
    Thread thread;
    // 判断是否第一次打开该应用
    Boolean b;

    private boolean isGoMain;


    private Handler mHandler = new Handler();


    //	 private AnimationDrawable animationDrawable;
//	 private ImageView animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_start);
        isGoMain = false;


        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        context = this;
        loacationInfo();
        /**
         * context.getgetSharedPreferences(String name, int mode)
         * 获取sharedPreference对象 name是sharedPreference生成的xml文件的名字
         */
        sharedPreferences = getSharedPreferences("test", Context.MODE_PRIVATE);


        //加载服务器地址
        final SharedPreferences sp = this.getSharedPreferences("ssspathss", MODE_PRIVATE);
        String bpath = sp.getString("path", null);
        if (bpath != null)
            BaseApi.API_URL_PRE = bpath;


        //jpush 初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getAds();
                    }
                });
            }
        }).start();

        getCitys();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkVersion();
                    }
                });
            }
        }).start();
    }

    /**
     * 缓存城市信息
     */
    private void getCitys() {
        long time = SharedPreferencesHelper.getLong("allcitys_time");
        if (System.currentTimeMillis() - time > 30 * 24 * 60 * 60 * 1000) {
            UserManager.getInstance().getTzjtrendAllSearchregion(context, new HashMap<String, String>(), new ServiceCallback<CommonListResult<Province>>() {
                @Override
                public void error(String msg) {
                }

                @Override
                public void done(int what, CommonListResult<Province> obj) {
                    if (obj.data != null) {
                        SharedPreferencesHelper.saveJSON("allcitys", obj);
                        SharedPreferencesHelper.saveLong("allcitys_time", System.currentTimeMillis());
                    }
                }
            });
        }

        //缓存热门城市
        UserManager.getInstance().getTzjtrendHotregion(context, new HashMap<String, String>(), new ServiceCallback<CommonListResult<City>>() {
            @Override
            public void error(String msg) {
            }

            @Override
            public void done(int what, CommonListResult<City> obj) {
                if (obj.data != null) {
                    SharedPreferencesHelper.saveJSON("hotcitys", obj);
                }
            }
        });
    }


    //检测新版本
    private void checkVersion() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", "20");
        map.put("appVersion", Util.getAppVersion(context));
        AsyncHttpHelp.httpGet(context, BaseApi.API_versionCheck, map, new ServiceCallback<CommonResult<UpgradeInfo>>() {
            @Override
            public void done(int what, CommonResult<UpgradeInfo> obj) {
                if (obj.data != null) {
                    try {
                        //弹框提示更新
                        showUpgradeDialog(obj.data, "20".equals(obj.data.popType));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//					if (2==tag) {
//						loadGg();
//					}
                }
            }

            @Override
            public void error(String msg) {
//				if (2==tag) {
//					loadGg();
//				}
            }
        });
    }

    private void showUpgradeDialog(final UpgradeInfo info, final boolean qiangzhi) {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_upgrade, null);
        final PopupWindow popupWindow = new PopupWindow(v,
                AbsoluteLayout.LayoutParams.MATCH_PARENT, AbsoluteLayout.LayoutParams.MATCH_PARENT, !qiangzhi);
        popupWindow.showAtLocation(this.findViewById(R.id.root_view), Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//				if (2==tag) {
//
//					loadGg();
//				}
            }
        });
        ((TextView) v.findViewById(R.id.tv_version)).setText("最新版本：" + info.version);
        ((TextView) v.findViewById(R.id.tv_verdes)).setText(info.content);
        v.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        v.findViewById(R.id.bt_upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApkUpdateUtil.downloadApk(findViewById(R.id.root_view), info.upgradeUrl);

                //				if(!qiangzhi)
                //				{
                popupWindow.dismiss();
                //				}
            }
        });

        if (qiangzhi) {
            v.findViewById(R.id.bt_cancel).setVisibility(View.GONE);
//			v.findViewById(R.id.shuxian).setVisibility(View.GONE);
        }
    }


    /**
     * 获取广告信息
     */
    private void getAds() {
        ServiceCallback<CommonListResult<Adverts>> callback = new ServiceCallback<CommonListResult<Adverts>>() {
            @Override
            public void done(int what, CommonListResult<Adverts> obj) {
                if (obj.data != null) {
                    SharedPreferencesHelper.saveJSON("sysAdvertsList", obj);

                    for (Adverts ad : obj.data) {
                        //图片的配置  打开磁盘缓存
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .build();
                        ImageLoader.getInstance().loadImage(ad.imageUrl, options, null);
                    }
                }
            }

            @Override
            public void error(String msg) {
            }
        };
        String strJson = SharedPreferencesHelper.getString("sysAdvertsList", null);
        if (strJson != null) {
            CommonListResult<Adverts> adList = AsyncHttpHelp.getGson().fromJson(strJson, callback.type);
            boolean bl = false;
            for (Adverts ad : adList.data) {
                if (ad.type.equals("10")) {
                    final View popupWindow = this.findViewById(R.id.lay_gg);
                    final ImageView img = (ImageView) popupWindow.findViewById(R.id.img_gg);
                    final TextView tv_tiaoguo = (TextView) popupWindow.findViewById(R.id.tv_tiaoguo);
                    tv_tiaoguo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//							popupWindow.setVisibility(View.GONE);
                            goMain();
                        }
                    });

                    final Adverts fad = ad;
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goMain();

                            Intent intent = new Intent(StartAc.this, AdvWebActivity.class);
//					        Uri content_url = Uri.parse(fad.linkUrl);
                            intent.putExtra(GlobalPrams.WebUrl, fad.linkUrl);
                            startActivity(intent);

//					        popupWindow.setVisibility(View.GONE);

                        }
                    });
                    //显示图片的配置
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .build();
                    ImageLoader.getInstance().loadImage(ad.imageUrl, options, new SimpleImageLoadingListener() {

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            super.onLoadingFailed(imageUri, view, failReason);
                            goMain();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            img.setImageBitmap(loadedImage);
                            popupWindow.setVisibility(View.VISIBLE);
                            tv_tiaoguo.setText(fad.timeout + " 跳过");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = Integer.parseInt(fad.timeout) - 1; i >= 0; i--) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        final int I = i;
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_tiaoguo.setText(I + " 跳过");
                                            }
                                        });
                                    }

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
//											popupWindow.setVisibility(View.GONE);
                                            goMain();
                                        }
                                    });
                                }
                            }).start();
                        }
                    });

                    bl = true;
                    break;
                }
            }

            if (!bl)
                goMain();
        } else {
            goMain();
        }

        HashMap<String, String> map = new HashMap<String, String>();
        AsyncHttpHelp.httpGet(context, BaseApi.API_sysAdvertsList, map, callback);
    }

    private void goMain() {
        if (isGoMain)
            return;
        isGoMain = true;
        b = sharedPreferences.getBoolean("isFirst", true);
        if (b) {
            Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst", false);
            editor.commit();
            Intent guideIntent = new Intent(StartAc.this, GuideAc.class);
            startActivity(guideIntent);
            finish();
        } else {
            if (App.getInstance().getUser() != null) {
                Intent intent = new Intent(context, MainAc.class);
                intent.putExtra("tag", 2);
                startActivity(intent);
            } else {
                Intent intent = new Intent(context, GuideAc.class);
                startActivity(intent);
            }
            finish();
        }
    }

    public void loacationInfo() {
        City city = App.getInstance().getCityCode();
        if (city==null)
        BDLocationHelper.locate(context, new MyLocationListener() {
            @Override
            public void success(BDLocation location) {
                LocationInfo info = BDLocationHelper.getCacheLocation();
                if (info != null) {
                    App.getInstance().saveCityCode(null);
                    City city = new City();
//								 city.lat = info.lat+"";
//								 city.lng = info.lng+"";
                    city.address = info.address;
                    city.regionName = info.city;
                    if (city.regionName == null) city.regionName = "";
                    Log.d("ffffffffffff", "replace shi");
                    city.regionName = city.regionName.replace("市", "");
                    App.getInstance().saveCityCode(city);
                }

            }
        });
//			 }
    }


    /**
     * 实现实位回调监听
     */
//		public class MyLocationListener implements BDLocationListener {
//
//			@Override
//			public void onReceiveLocation(BDLocation arg0) {
//				
//			}
//
//			@Override
//			public void onReceivePoi(BDLocation arg0) {
//			}
//		}


}
