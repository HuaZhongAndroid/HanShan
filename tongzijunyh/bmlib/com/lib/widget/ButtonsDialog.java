package com.lib.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonsDialog extends Dialog{
	final static String TAG = "ButtonsDialog";
	private HashMap<String, Button> buttons = new HashMap<String, Button>();
	private List<String> buttonNames = new ArrayList<String>();
	private int gravity = Gravity.CENTER;
	public List<String> hiddenButtonNames = new ArrayList<String>();
	private boolean autoDismiss = false;
	private View.OnClickListener listener;
	
	/**
	 * 创建ButtonsDialog
	 * @param context
	 * @param buttonNames
	 * @param listeners
	 */
	public ButtonsDialog(Context context, String[] buttonNames, View.OnClickListener listener,
			int gravity) {
		//super(context, R.style.ButtonsDialog);
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.listener = listener;
		for(String name : buttonNames){
			this.buttonNames.add(name);	
		}
		this.gravity = gravity;
	}

	public ButtonsDialog(Context context, int theme){
		super(context, theme);
	}
	
	public ButtonsDialog setGravity(int gravity){
		this.gravity = gravity;
		return this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LinearLayout root = new LinearLayout(getContext());
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
				getContext().getResources().getDisplayMetrics().widthPixels
				-dp2px(30),ViewGroup.LayoutParams.WRAP_CONTENT);
		root.setOrientation(LinearLayout.VERTICAL);
		int pad = dp2px(6);
		root.setPadding(pad, pad, pad, pad);
		for(int i=0; i<buttonNames.size(); i++){
			Button button =createButton(buttonNames.get(i)); 
			button.setId(i);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(listener != null){
						listener.onClick(v);
					}
					if(autoDismiss){
						dismiss();
					}
				}
			});
			button.setVisibility(hiddenButtonNames.contains(buttonNames.get(i))?View.GONE:View.VISIBLE);
			buttons.put(buttonNames.get(i), button);
			root.addView(button, button.getLayoutParams());
		}
		this.setContentView(root, vlp);
		getWindow().setGravity(gravity);
	}
	
	public ButtonsDialog autoDismiss(){
		autoDismiss = true;
		return this;
	}
	
	@Override
	public void show() {
		super.show();
	}
	
	/**
	 *隐藏按钮 
	 * @param buttonId
	 */
	public void hideButton(String buttonName){
		hiddenButtonNames.add(buttonName);
		if(buttons.containsKey(buttonName)){
			buttons.get(buttonName).setVisibility(View.GONE);
		}
	}
	
	/**
	 * 显示按钮
	 * @param buttonId
	 */
	public void showButton(String buttonName){
		hiddenButtonNames.remove(buttonName);
		if(buttons.containsKey(buttonName)){
			buttons.get(buttonName).setVisibility(View.VISIBLE);
		}
	}
	
	private Button createButton(String name){
		Button button = new Button(getContext());
		button.setText(name);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		//lp.leftMargin = lp.rightMargin = UITools.dip2px(10);
		button.setLayoutParams(lp);
		return button;
	}
	
	public Button getButton(String buttonName){
		return buttons.get(buttonName);
	}
	
	public int dp2px(float spValue) {
		float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}