package com.bm.tzj.fm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.im.ac.ChatActivity;
import com.bm.im.db.InviteMessgeDao;
import com.bm.im.fm.EaseConversationListFragment;
import com.bm.im.tool.Constant;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.util.NetUtils;
import com.richer.tzj.R;

/**
 * 
 * 吧啦
 * 
 * @author wangqiang
 * 
 */
@SuppressLint("NewApi")
public class BalaFm extends EaseConversationListFragment implements OnClickListener {
	private Context context;
	private TextView errorText;

	//	@Override
	//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//			Bundle savedInstanceState) {
	//		View messageLayout = inflater.inflate(R.layout.fm_msg, container,
	//				false);
	//		context = this.getActivity();
	//		initView(messageLayout);
	//		// App.toast("HealthRecordFm");
	//		return messageLayout;
	//	}
	//
	//	/**
	//	 * 初始化数据
	//	 * @param v
	//	 */
	//	private void initView(View v) {
	//		btn_bl=(Button) v.findViewById(R.id.btn_bl);
	//		btn_bl.setOnClickListener(this);
	//	}
	//
	//	@Override
	//	public void onClick(View v) {
	//		Intent intent = null;
	//		switch (v.getId()) {
	//		case R.id.btn_bl:
	//			intent =new Intent(context, FriendAc.class);
	//			startActivity(intent);
	//			break;
	//		default:
	//			break;
	//		}
	//	}
	@Override
	protected void initView() {
		super.initView();
		context = this.getActivity();
		View errorView = (LinearLayout) View.inflate(getActivity(),R.layout.fm_msg, null);
		errorItemContainer.addView(errorView);
		errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
	}
	@Override
	protected void onConnectionConnected() {
		etb.setRightText(" ");
		// 右边监听
//		etb.setRightLayoutClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(), FriendAc.class);
//				startActivity(intent);
//			}
//		});
		super.onConnectionConnected();
	}
	@Override
	protected void setUpView() {
		super.setUpView();
		//设置左右
		etb.setLeftText(" ");
		etb.setRightText(" ");
		//右边监听
//		etb.setRightLayoutClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent=new Intent(getActivity(), FriendAc.class);
//				startActivity(intent);
//			}
//		});
		// 注册上下文菜单
		registerForContextMenu(conversationListView);
		conversationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = conversationListView.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(EMChatManager.getInstance().getCurrentUser()))
					Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, 0).show();
				else {
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					if(conversation.isGroup()){
						if(conversation.getType() == EMConversationType.ChatRoom){
							// it's group chat
							intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
						}else{
							intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
						}

					}
					// it's single chat
					intent.putExtra(Constant.EXTRA_USER_ID, username);
					startActivity(intent);
				}
			}
		});
	}


	@Override
	protected void onConnectionDisconnected() {
		super.onConnectionDisconnected();
		if (NetUtils.hasNetwork(getActivity())){
			errorText.setText(R.string.can_not_connect_chat_server_connection);
		} else {
			errorText.setText(R.string.the_current_network);
		}

		etb.setRightText("");
		etb.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean handled = false;
		boolean deleteMessage = false;
		/*if (item.getItemId() == R.id.delete_message) {
		            deleteMessage = true;
		            handled = true;
		        } else*/ if (item.getItemId() == R.id.delete_conversation) {
		        	deleteMessage = true;
		        	handled = true;
		        }
		        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		        // 删除此会话
		        EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
		        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
		        refresh();

		        // 更新消息未读数
		        //		        ((MainAc) getActivity()).updateUnreadLabel();

		        return handled ? true : super.onContextItemSelected(item);
	}


	//		    private void sxxxx(){
	//		    	
	//		    	 List<EMConversation> conversationList = new ArrayList<EMConversation>();
	//		    	 
	//		    	EMConversation conversation = conversationListView.l
	//	            String username = conversation.getUserName();
	//		    	
	//		    	
	//		    }

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {

		default:
			break;
		}
	}
}
