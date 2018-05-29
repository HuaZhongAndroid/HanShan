package com.bm.tzj.ts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.bm.api.UserManager;
import com.bm.app.App;
import com.bm.base.BaseCaptureActivity;
import com.bm.dialog.ThreeButtonDialog;
import com.bm.dialog.UtilDialog;
import com.bm.entity.GrowUp;
import com.bm.entity.GrowUpImg;
import com.bm.oss.app.Config;
import com.bm.oss.sample.customprovider.OSSCallback;
import com.bm.oss.sample.customprovider.OssService;
import com.lib.http.ServiceCallback;
import com.lib.http.result.CommonResult;
import com.lib.http.result.StringResult;
import com.lib.widget.FuGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.richer.tzj.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * 成长中心发布成长记录
 *
 * @author jiangsh
 */
public class SendGrowUpAc extends BaseCaptureActivity implements OnClickListener {
    //	private TextView tv_submit;
    private EditText et_content;
    private FuGridView fgv_addImage;
    private ThreeButtonDialog buttonDialog;
    public List<String> uploadListImg = new ArrayList<String>(); //要上传的图片
    //上传后的图片的名称

    private List<GrowUpImg> attachmentlist = new ArrayList<GrowUpImg>(); //之前已经上传的图片
    private LinearLayout ll_send_parent;
    private OSSCallback ossCallback = null;

    private GrowUp data;
    private String pkid = "";

    private boolean isSaveCaogao; //是否提示保存草稿


    private final int IMG_COUNT_MAX = 9;

    //OSS的上传下载
    private OssService ossService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.ac_send_grow_up);
        setTitleName("成长记录");
        init();
        isSaveCaogao = true;
        data = (GrowUp) this.getIntent().getSerializableExtra("data");
        if (data == null)
            loadCaogao();
        else {
            isSaveCaogao = false;
            pkid = data.pkid;
            setData();
        }
        ossCallback = new OSSCallback(this) {
            @Override
            protected void onCallback(Message inputMessage) {
                Log.e("debug_uplad_result", inputMessage.what + "");
                switch (inputMessage.what) {
                    case OSSCallback.UPLOAD_OK:
                        uploadImageIndex++;
                        uploadImg();
                        break;
                    case OSSCallback.DISPLAY_INFO:
                        Log.e("debug-->upload", inputMessage.obj.toString());
                        break;
                }
            }
        };
        ossService = initOSS(Config.endpoint, Config.bucketTest);

        setting(Config.bucketTest);
    }

    private void setting(String bucketTest) {
        ossService.setBucketName(Config.bucketTest);
        String newOssEndpoint = Config.endpoint;

        OSSCredentialProvider credentialProvider;
        //使用自己的获取STSToken的类
        String stsServer = Config.STSSERVER;
        credentialProvider = new OSSAuthCredentialsProvider(stsServer);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(getApplicationContext(), newOssEndpoint, credentialProvider, conf);
        ossService.initOss(oss);
        ossCallback.settingOK();
        Intent data = new Intent();
        data.putExtra("bucketName", bucketTest);
        data.putExtra("url", stsServer);
        data.putExtra("endpoint", newOssEndpoint);
        setResult(RESULT_OK, data);
    }

    private void setData() {
        pkid = data.pkid;
        if (data.attachmentlist != null) {
            attachmentlist = data.attachmentlist;
            adapter.notifyDataSetChanged();
        }
        if (data.content != null) {
            et_content.setText(data.content);
        }
    }

    //载入草稿
    private void loadCaogao() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (null == App.getInstance().getUser()) {
            map.put("userId", "0");
        } else {
            map.put("userId", App.getInstance().getUser().userid);
        }
        if (App.getInstance().getChild() == null)
            map.put("babyId", "-1");
        else
            map.put("babyId", App.getInstance().getChild().babyId);

        showProgressDialog();
        UserManager.getInstance().getGrowthRecord_getDraftInfo(this, map, new ServiceCallback<CommonResult<GrowUp>>() {

            @Override
            public void done(int what, CommonResult<GrowUp> obj) {
                hideProgressDialog();
                if (obj.data != null) {
                    data = obj.data;
                    setData();
                }
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
//				toast(msg);
//				finish();
            }
        });
    }


    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            int s = attachmentlist.size() + uploadListImg.size();
            if (s < IMG_COUNT_MAX)
                s += 1;
            return s;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d("fffffff", "getView " + position);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_send_pic_two, parent, false);
                int s = (int) (context.getResources().getDisplayMetrics().widthPixels / 3.3);
                convertView.getLayoutParams().width = s;
                convertView.getLayoutParams().height = s;
            }

            ImageView iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            View iv_delete = convertView.findViewById(R.id.iv_delete);
            convertView.setOnClickListener(null);

            if (position == attachmentlist.size() + uploadListImg.size()) {
                iv_delete.setVisibility(View.GONE);
//				iv_pic.setImageResource(R.drawable.pic_add);
                String imgstr = "drawable://" + R.drawable.pic_add;
                ImageLoader.getInstance().displayImage(imgstr, iv_pic, App.getInstance().getListViewDisplayImageOptions());
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonDialog.show();
                    }
                });
            } else if (position < attachmentlist.size()) {
                iv_delete.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(attachmentlist.get(position).url, iv_pic, App.getInstance().getListViewDisplayImageOptions());
                iv_delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delAttachment(attachmentlist.get(position));
                    }
                });
            } else {
                iv_delete.setVisibility(View.VISIBLE);
                final String src = uploadListImg.get(position - attachmentlist.size());
                ImageLoader.getInstance().displayImage("file://" + src, iv_pic, App.getInstance().getListViewDisplayImageOptions());
                iv_delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadListImg.remove(src);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }
    };

    //删除服务端的图片附件
    private void delAttachment(final GrowUpImg img) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pkid", img.pkid);
        showProgressDialog();
        UserManager.getInstance().sysAttachment_deleteById(this, map, new ServiceCallback<StringResult>() {
            @Override
            public void done(int what, StringResult obj) {
                hideProgressDialog();
                attachmentlist.remove(img);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(String msg) {
                hideProgressDialog();
                toast(msg);
            }
        });
    }

    @Override
    public void clickLeft() {
        backCheck();
    }

    @Override
    public void onBackPressed() {
        backCheck();
    }

    private void backCheck() {
        if (isSaveCaogao && (!TextUtils.isEmpty(et_content.getText().toString()) || uploadListImg.size() > 0)) {
            UtilDialog.dialogTwoBtnContentTtile(context, null, "退出", "保存", "是否保存为草稿？", new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 14) {
                        finish();
                        return;
                    }
                    addArticle("0");
                }
            }, 1);
        } else {
            finish();
        }
    }

    private void init() {
        ll_send_parent = findLinearLayoutById(R.id.ll_send_parent);
        fgv_addImage = (FuGridView) findViewById(R.id.fgv_addImage);
//		tv_submit = findTextViewById(R.id.tv_submit);
        et_content = findEditTextById(R.id.et_content);
        et_content.clearFocus();
//		tv_submit.setOnClickListener(this);
        ll_right.setVisibility(View.VISIBLE);
        tv_right_share_left.setText("发布");
        tv_right_share_left.setVisibility(View.VISIBLE);
        tv_right_share_left.setOnClickListener(this);
        buttonDialog = new ThreeButtonDialog(context).setFirstButtonText("拍照")
                .setBtn1Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        takePhoto();
                    }
                }).setThecondButtonText("从手机相册选择")
                .setBtn2Listener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
//						pickPhoto();
                        pickPhotoList(IMG_COUNT_MAX - uploadListImg.size() - attachmentlist.size());
                    }
                }).autoHide();

        fgv_addImage.setAdapter(adapter);

        ll_send_parent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (SendGrowUpAc.this.getCurrentFocus() != null) {
                        if (SendGrowUpAc.this.getCurrentFocus()
                                .getWindowToken() != null) {
                            // 调用系统自带的隐藏软键盘
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(
                                            SendGrowUpAc.this
                                                    .getCurrentFocus()
                                                    .getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }

                }
                return false;
            }
        });

        fgv_addImage.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (SendGrowUpAc.this.getCurrentFocus() != null) {
                        if (SendGrowUpAc.this.getCurrentFocus()
                                .getWindowToken() != null) {
                            // 调用系统自带的隐藏软键盘
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(
                                            SendGrowUpAc.this
                                                    .getCurrentFocus()
                                                    .getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }

                }
                return false;
            }
        });

    }


    @Override
    protected void onPhotoTaked(String photoPath) {
        uploadListImg.add(photoPath);
        adapter.notifyDataSetChanged();
    }

    /**
     * 照片多选以后回调
     *
     * @param photoPath
     */
    protected void onPhotoListTaked(List<String> photoPath) {
        uploadListImg.addAll(photoPath);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right_share_left:
                addArticle("1");
                break;
            default:
                break;
        }
    }

    /**
     * 发帖
     * saveType 0保存草稿，1正常发布
     */
    private void addArticle(final String saveType) {

//		tv_right_share_left.setClickable(false);
//		tv_right_share_left.setFocusable(false);
//		tv_right_share_left.setText("发布中...");

        if (et_content.getText().toString().trim().length() == 0 && attachmentlist.size() + uploadListImg.size() == 0) {
            toast("发布内容不能为空");
            return;
        }
        showProgressDialog();
        HashMap<String, String> map = new HashMap<String, String>();

        if (data != null)
            map.put("pkid", data.pkid);
        map.put("userId", App.getInstance().getUser().userid);
        if (App.getInstance().getChild() == null)
            map.put("babyId", "-1");
        else
            map.put("babyId", App.getInstance().getChild().babyId);
        map.put("content", et_content.getText().toString());
        map.put("saveType", saveType);
        UserManager.getInstance().sendGrowUpRecord(context, uploadListImg, map, new ServiceCallback<StringResult>() {
            @Override
            public void error(String msg) {
                hideProgressDialog();
                dialogToast(msg);
//				tv_right_share_left.setClickable(true);
//				tv_right_share_left.setFocusable(true);
//				tv_right_share_left.setText("发布");
            }

            @Override
            public void done(int what, StringResult obj) {
//                hideProgressDialog();
//                if ("0".equals(saveType)) {
//                    toast("草稿保存成功");
//                } else {
////                    pkid = obj.data;
////                    uploadImages();
//                }
                pkid = obj.data;
                uploadImages();
            }
        });
//        uploadImages();
    }


    public OssService initOSS(String endpoint, String bucket) {

//        移动端是不安全环境，不建议直接使用阿里云主账号ak，sk的方式。建议使用STS方式。具体参
//        https://help.aliyun.com/document_detail/31920.html
//        注意：SDK 提供的 PlainTextAKSKCredentialProvider 只建议在测试环境或者用户可以保证阿里云主账号AK，SK安全的前提下使用。具体使用如下
//        主账户使用方式
//        String AK = "******";
//        String SK = "******";
//        credentialProvider = new PlainTextAKSKCredentialProvider(AK,SK)
//        以下是使用STS Sever方式。
//        如果用STS鉴权模式，推荐使用OSSAuthCredentialProvider方式直接访问鉴权应用服务器，token过期后可以自动更新。
//        详见：https://help.aliyun.com/document_detail/31920.html
//        OSSClient的生命周期和应用程序的生命周期保持一致即可。在应用程序启动时创建一个ossClient，在应用程序结束时销毁即可。


        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(Config.STSSERVER);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);
        OSSLog.enableLog();
        return new OssService(oss, bucket, ossCallback);
    }

    //上传图片的索引
    private int uploadImageIndex = 0;
    private int uploadImageLen = 0;

    public void uploadImages() {
        Log.e("debug_upload_img", "开始上传图片");
        uploadImageIndex = 0;
        uploadImageLen = uploadListImg.size();
        uploadImg();
    }


    private void uploadImg() {
        if (uploadImageIndex < uploadImageLen) {
            Log.e("debug_upload_pro", uploadImageIndex + "/" + uploadImageLen);
            String uuid = UUID.randomUUID().toString();
            String imageName = uuid + ".jpg";
            String imgPath = String.format(Config.callbackAddress, imageName, pkid);
            //设置上传的callback地址，目前暂时只支持putObject的回调
            Log.e("debug_uploca_callback", imgPath);
            ossService.setCallbackAddress(imgPath);
            ossService.asyncPutImage(imageName, uploadListImg.get(uploadImageIndex));
        } else {
            hideProgressDialog();
            toast("操作成功");
            finish();
            Log.e("debug_upload_pro", "上传完成");
//            uploadAllDataToNet("1");
        }
    }

    private void dismissLoading() {
        hideProgressDialog();
    }

    private void showLoading() {
        showProgressDialog();
    }

}
