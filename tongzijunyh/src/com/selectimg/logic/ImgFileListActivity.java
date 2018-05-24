package com.selectimg.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.bm.base.BaseActivity;
import com.richer.tzj.R;


public class ImgFileListActivity extends BaseActivity implements OnItemClickListener{
	
	public static int imgCount = 9;  //最多选择几张

    ListView listView;
    Util util;
    ImgFileListAdapter listAdapter;
    List<FileTraversal> locallist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.imgfilelist);
		setTitleName("选择相册");
		
        listView=(ListView) findViewById(R.id.listView1);
        util=new Util(this);
        locallist=util.LocalImgFileList();
        List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
        Bitmap bitmap[] = null;
        if (locallist!=null && locallist.size()>0) {
            bitmap=new Bitmap[locallist.size()];
            for (int i = 0; i < locallist.size(); i++) {
                HashMap<String, String> map=new HashMap<String, String>();
                map.put("filecount", locallist.get(i).filecontent.size()+"张");
                map.put("imgpath", locallist.get(i).filecontent.get(0)==null?null:(locallist.get(i).filecontent.get(0)));
                map.put("filename", locallist.get(i).filename);
                listdata.add(map);
            }
            
            listAdapter=new ImgFileListAdapter(this, listdata);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(this);
        }
        else
        {
            Toast.makeText(this, "没有照片", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent=new Intent(this,ImgsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("data", locallist.get(arg2));
        intent.putExtras(bundle);
        if (this.getIntent().getStringArrayListExtra("list")==null) {
        	ArrayList<String> list = new ArrayList<String>();
        	 intent.putStringArrayListExtra("list1",list);     
		}else {
			  intent.putStringArrayListExtra("list1",this.getIntent().getStringArrayListExtra("list"));
		}
      
        startActivityForResult(intent,1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK || resultCode == 100)
        {
            this.setResult(resultCode, data);
            this.finish();
        }
    }
    
}