package com.bm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.richer.tzj.R;


/**
 * 
 * 
 * daialog  工具类
 * @author wangqiang
 *
 */
public class UtilDialog {
	
	/**
	 * 
	 * 请假提交反馈信息
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogPromtMessage(Context context,final Handler handler) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(true);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_btncontent, null);
		
		TextView tv_cancle = (TextView)view.findViewById(R.id.tv_cancle);
		TextView tv_submit = (TextView)view.findViewById(R.id.tv_submit);
		final EditText et_content=(EditText) view.findViewById(R.id.et_content);
		tv_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message=new Message();
				if(TextUtils.isEmpty(et_content.getText().toString())){
					message.what=0;
				}else{
					message.what=1;
					message.obj=et_content.getText().toString();
				}
				handler.dispatchMessage(message);
				dialog.cancel();
			}
		});
		tv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	/**
	 * 
	 * 输入支付密码
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogPay(Context context,final Handler handler) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(true);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_pay, null);
		
		TextView tv_cancle = (TextView)view.findViewById(R.id.tv_cancle);
		TextView tv_submit = (TextView)view.findViewById(R.id.tv_submit);
		final EditText et_content=(EditText) view.findViewById(R.id.et_content);
		tv_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message=new Message();
				message.what=1;
				message.obj=et_content;
				handler.dispatchMessage(message);
				dialog.cancel();
			}
		});
		tv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	/**
	 * 
	 * 两个按钮，一个提示文字
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogTwoBtnResultCode(Context context,String content,String btnLeftName,String btnRightName,final Handler handler,final int code) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_twobtncontent, null);
		
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		
		dialog_content.setText(content);
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = code;
				handler.sendMessage(msg);
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	
	/**
	 * 
	 * 新建群
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogTwoBtnGropu(Context context,final Handler handler) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_two_btncontent, null);
		
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
		TextView tv_create = (TextView) view.findViewById(R.id.tv_create);//创建
		final EditText et_groupname=(EditText) view.findViewById(R.id.et_groupname);
		
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		//确定事件
		tv_create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String groupName=et_groupname.getText().toString();
				Message msg = new Message();
				msg.obj=groupName;
				msg.what=1002;
				handler.sendMessage(msg);
				
				
//				if(groupName.length()==0){
//					GroupListAc.intance.dialogToast("请输入群名称");
//					return;
//				}
//				GroupListAc.intance.createGroup(et_groupname.getText().toString());
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	
	/**
	 * 
	 * 两个按钮，一个提示文字
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogTwoBtnRefused(final Context context,String btnLeftName,String btnRightName,final Handler handler,final int code) {
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_refused_content, null);
		
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		
		final EditText et_content=(EditText) view.findViewById(R.id.et_desc);
		
		
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			  
				Message msg = new Message();
				msg.what = code;
				msg.obj=et_content.getText();
				handler.sendMessage(msg);
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	
	/**
	 * 
	 * 两个按钮，一个提示文字   注销提示
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogTwoBtnContentTtile(Context context,String content,String btnLeftName,String btnRightName,String strTitle,final Handler handler,final int strCode) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_twobtncontent_title, null);
		
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);//确定
		if(content == null)
			dialog_content.setVisibility(View.GONE);
		else
			dialog_content.setText(content);
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		if(strTitle == null)
			tv_title.setVisibility(View.GONE);
		else
			tv_title.setText(strTitle);
		
		
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 14;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = strCode;
				handler.sendMessage(msg);
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}
	
	
	/**
	 * 
	 * 两个按钮，一个提示文字   解绑微信。喊山方UI新作品
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogZhouTwoBtnContentTtile(Context context,String content,String btnLeftName,String btnRightName,String strTitle,final Handler handler,final int strCode) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.zhou_dialog_twobtncontent_title, null);
		
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);//确定
		
		dialog_content.setText(content);
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		tv_title.setText(strTitle);
		
		
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 14;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = strCode;
				handler.sendMessage(msg);
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}






	/**
	 * 
	 * 版本更新dialog
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogVersion(boolean isHideCancel,Context context,String content,String btnLeftName,String btnRightName,final Handler handler) {
		
		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_twobtncontent_title, null);
		
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		View v_line = (View)view.findViewById(R.id.v_line);//确定
		dialog_content.setText(content);
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		
		if(isHideCancel){
			v_line.setVisibility(View.GONE);
			tv_cancel.setVisibility(View.GONE);
		}else{
			v_line.setVisibility(View.VISIBLE);
			tv_cancel.setVisibility(View.VISIBLE);
		}
		
		
		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 100;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = 101;
				handler.sendMessage(msg);
				dialog.cancel();				
			}
		});
		
		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}

	/**
	 *
	 * 一个标题 ，两个按钮，一个提示文字   解绑微信。喊山方UI新作品
	 * @param context
	 * @param content
	 * @param btnName
	 * @param handler
	 */
	public static void dialogPrompt(Context context,String content,String btnLeftName,
									String btnRightName,String strTitle,
									final Handler handler,final int strCode) {

		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_pay_yue, null);

		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_cancel = (TextView) view.findViewById(R.id.btn_Cancel);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);//确定

		dialog_content.setText(content);
		tv_cancel.setText(btnLeftName);
		tv_Determine.setText(btnRightName);
		tv_title.setText(strTitle);

		//取消事件
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what = 14;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = strCode;
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});

		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}

	/**
	 *
	 * 一个标题 ，一个按钮，一个提示文字
	 * @param context
	 * @param content
	 * @param handler
	 */
	public static void dialogPrompt(Context context,String content,
									String btnRightName,String strTitle,
									final Handler handler,final int strCode) {

		final Dialog dialog = new Dialog(context, R.style.MyDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.dialog_pay_success, null);
		TextView dialog_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_Determine = (TextView) view.findViewById(R.id.btn_Determine);//确定
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);//确定
		dialog_content.setText(content);
		tv_Determine.setText(btnRightName);
		tv_title.setText(strTitle);
		//确定事件
		tv_Determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				msg.what = strCode;
				if (handler!=null)
				handler.sendMessage(msg);
				dialog.cancel();
			}
		});

		dialog.setContentView(view);
		dialog.setTitle(null);
		dialog.show();
	}


}
