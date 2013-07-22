package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.account.AlipaySecureActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.home.MainGroup;
import com.ruyicai.activity.home.MainGroup.NoReadUpdateReceiver;
import com.ruyicai.activity.info.LotInfoActivity;
import com.ruyicai.activity.info.LotInfoConcreteActivity;
import com.ruyicai.activity.info.LotInfoDomain;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.activity.usercenter.UserScoreActivity.ScroeQueryAdapter;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.databases.OperatingDataBases;
import com.ruyicai.net.newtransaction.DeleteMessage;
import com.ruyicai.net.newtransaction.LetterQueryInterface;
import com.ruyicai.net.newtransaction.MsgUpdateReadState;
import com.ruyicai.net.newtransaction.QueryintegrationInterface;
import com.ruyicai.net.newtransaction.UpdateReadState;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 用户反馈
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings({ "unused", "unused", "unused" })
public class FeedbackListActivity extends Activity {

	Button back, feedback;
	ListView feedbackList;
	List<Map<String, Object>> feedList = new ArrayList<Map<String, Object>>();
	final String CREATETIME = "createTime";
	final String REPLY = "reply";
	final String CONTENT = "content";
	private int[] linearId = {R.id.system_info, R.id.latter, R.id.message };
	private String[] titles = {"系统信息", "站内信", "我的留言" };
	private LinearLayout message, latter, systemInfo;
	TabHost mTabHost;
	private LayoutInflater mInflater = null;
	View tabSpecLinearView;// 子列表的ListView
	RWSharedPreferences shellRW;
	String userno;
	ListView tabSpecListView;// 子列表的ListView
	String contentjson;
	String title;
	InfoAdapter adapter;
	ProgressBar progressbar;
	View view;
	int latterPages = 0;
	int latterIndex = 0;
	ProgressDialog dialog;
	int type = 0;
	public List<LotInfoDomain> latterlist = new ArrayList<LotInfoDomain>();// 如意活动
	int notReadLetterCount = 0;
	TextView lettercount;
	TextView feedbackcount;
	String msgReadStateId = "";// 我的消息所有未读id
	
	/**add by yejc 20130419 start*/
	private boolean isLatterEdit = false;  //标记站内信是否点击了编辑按钮
	private boolean isLatterSelectAll = false; //标记站内信是否点击了全选按钮
	private boolean isMessageSelectAll = false; //标记我的留言是否点击了全选按钮
	private boolean isMessageEdit = false; //标记我的留言是否点击了编辑按钮
	private LinearLayout submitLayout;
	private LinearLayout latterEditLayout;
	private LinearLayout messageEditLayout;
	private Button latterSelectAllBtn;
	private Button messageSelectAllBtn;
	private Button editBut;
	private String selectAllKey = "selectAllKey";
	private String infoType = "infoType";
	private final String LATTERCOMMAND = "letter";
	private final String MSGCOMMAND = "feedback";
	private final String BROADCAST_ACTION  = "BROADCAST_ACTION";
	FeedbackListAdapter listAdapter;
	/*存放选择的站内信*/
	private Map<Integer, Boolean> selectLatterMap = new HashMap<Integer, Boolean>();
	/*存放选择的我的留言*/
	private Map<Integer, Boolean> selectMessageMap = new HashMap<Integer, Boolean>();
	private List<Integer> selectedMsgList = new ArrayList<Integer>();
//	private List<Integer> selectedMsgList = new ArrayList<Integer>();
	ShowSelectTextBroadCast selectTextBroadCast = new ShowSelectTextBroadCast();
	/**add by yejc 20130419 end*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mymessage);
		/**add by yejc 20130422 start*/
		if(Constants.isDebug) {
			PublicMethod.outLog(getClass().getSimpleName(), "onCreate");
		}
		/**add by yejc 20130422 end*/
		initBtn();

		shellRW = new RWSharedPreferences(this, "addInfo");

		if (!shellRW.getStringValue("notReadLetterCount").equals("")) {
			notReadLetterCount = Integer.valueOf(shellRW
					.getStringValue("notReadLetterCount"));
		}

		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
		}
		mTabHost.setOnTabChangedListener(scroeTabChangedListener);
		userno = shellRW.getStringValue("userno");
//		getInfoNet(userno, latterIndex, false);
		initLinear(systemInfo, linearId[0], initSystemInfo());
	}

	protected void onRestart() {
		super.onRestart();
		if (notReadLetterCount > 0) {
			lettercount.setVisibility(View.VISIBLE);
			lettercount.setText(String.valueOf(notReadLetterCount));
		} else {
			lettercount.setVisibility(View.INVISIBLE);
		}

		feedbackcount.setVisibility(View.INVISIBLE);
	};

	/**
	 * TabHost切换监听器
	 */
	TabHost.OnTabChangeListener scroeTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			RWSharedPreferences pre = new RWSharedPreferences(
					FeedbackListActivity.this, "addInfo");
			String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
			if (sessionIdStr.equals("") || sessionIdStr == null) {
				Intent intentSession = new Intent(FeedbackListActivity.this,
						UserLogin.class);
				startActivity(intentSession);
			} else {
				if (tabId.equals(titles[0])) {
					type = 0;
					feedback.setText(R.string.usercenter_feedback_bet);
					initLinear(systemInfo, linearId[0], initSystemInfo());
					latterEditLayout.setVisibility(View.GONE);
					messageEditLayout.setVisibility(View.GONE);
					editBut.setVisibility(View.GONE);
					submitLayout.setVisibility(View.VISIBLE);
				} else if (tabId.equals(titles[1])) {
					// initLinear(scroedetail, linearId[0], view);
					editBut.setVisibility(View.VISIBLE);
					type = 1;
					feedback.setText(R.string.usercenter_submitfeedback);
					if (latterlist.size() > 0) {
						initLinear(latter, linearId[1], initlatterview(true));
						adapter.notifyDataSetChanged();
					} else {
						getInfoNet(userno, latterIndex, false);
						if (isLatterSelectAll) {
							isLatterSelectAll = false;
							latterSelectAllBtn.setText(R.string.my_message_edit_select_all);
						}
					}
					/**add by yejc 20130422 start*/
					if (isLatterEdit) {
						editBut.setText(R.string.my_message_edit_cancel);
						latterEditLayout.setVisibility(View.VISIBLE);
						submitLayout.setVisibility(View.GONE);
					} else {
						editBut.setText(R.string.my_message_edit_text);
						submitLayout.setVisibility(View.VISIBLE);
					}
					messageEditLayout.setVisibility(View.GONE);
					/**add by yejc 20130422 end*/
				} else if (tabId.equals(titles[2])) {
					editBut.setVisibility(View.VISIBLE);
					type = 2;
					feedback.setText(R.string.usercenter_submitfeedback);
					initLinear(message, linearId[2], initmessage());
					feedbackcount.setVisibility(View.GONE);
					if (!msgReadStateId.equals("")) {
						msgUpdateReadState(msgReadStateId);// 更新已读状态
					}
					
					/**add by yejc 20130422 start*/
					if (isMessageEdit) {
						editBut.setText(R.string.my_message_edit_cancel);
						messageEditLayout.setVisibility(View.VISIBLE);
						submitLayout.setVisibility(View.GONE);
					} else {
						editBut.setText(R.string.my_message_edit_text);
						submitLayout.setVisibility(View.VISIBLE);
					}
					latterEditLayout.setVisibility(View.GONE);
					/**add by yejc 20130422 end*/
				}
			}
			

		}
	};

	/**
	 * 初始化LinearLayout,为TabHost下的LinearLayout添加View
	 * 
	 * @param linear
	 *            要初始化的LinearLayout
	 * @param linearid
	 *            LinearLayout对应的Id
	 * @param view
	 *            要添加的View
	 */
	private void initLinear(LinearLayout linear, int linearid, View view) {
		linear = (LinearLayout) findViewById(linearid);
		linear.removeAllViews();
		linear.addView(view);
	}

	private View initlatterview(boolean flag) {
		tabSpecLinearView = (LinearLayout) mInflater.inflate(
				R.layout.usercenter_listview_layout, null);
		tabSpecListView = (ListView) tabSpecLinearView
				.findViewById(R.id.usercenter_listview_queryinfo);
		adapter = new InfoAdapter(this, latterlist);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		tabSpecListView.addFooterView(view);
		tabSpecListView.setAdapter(adapter);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setEnabled(false);
				addmore();
			}
		});
		return tabSpecLinearView;
	}

	public void addTab(int index) {
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab
				.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab
				.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);

		if (index == 1) {
			lettercount = (TextView) indicatorTab.findViewById(R.id.tv_quan);
			if (notReadLetterCount > 0) {
				lettercount.setVisibility(View.VISIBLE);
				lettercount.setText(String.valueOf(notReadLetterCount));
			} else {
				lettercount.setVisibility(View.INVISIBLE);
			}
		}

		if (index == 2) {
			feedbackcount = (TextView) indicatorTab.findViewById(R.id.tv_quan);
			feedbackcount.setVisibility(View.INVISIBLE);

		}
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}

	private View initmessage() {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tabSpecLinearView = (RelativeLayout) inflate.inflate(
				R.layout.usercenter_feedbacklist, null);
		feedbackList = (ListView) tabSpecLinearView
				.findViewById(R.id.usercenter_feedback_list);
		try {
			JSONArray feedBackArray = new JSONArray(this.getIntent()
					.getStringExtra("feedBackArray"));
			initListViewAfterNet(feedBackArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tabSpecLinearView;
	}

	private void initListViewAfterNet(JSONArray feedarray) {
		feedList.clear();
		for (int i = 0; i < feedarray.length(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			JSONObject item;
			try {
				item = feedarray.getJSONObject(i);
				String state = item.getString("readState");
				if (state.equals("0")) {
					String id = item.getString("id");
					msgReadStateId += id + ",";
				}
				map.put("createTime", item.getString(CREATETIME));
				map.put("reply", item.getString(REPLY));
				map.put("content", item.getString(CONTENT));
				map.put("id", item.getString("id"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			feedList.add(map);
		}
		if (!msgReadStateId.equals("")) {
			msgReadStateId = msgReadStateId.substring(0,
					msgReadStateId.length() - 1);
		}

		listAdapter = new FeedbackListAdapter(this,
				feedList);
		feedbackList.setAdapter(listAdapter);
	}

	private void initBtn() {
		feedback = (Button) findViewById(R.id.usercenter_feedback_submitbtn);
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(mTabHost.getCurrentTab() == 0) {
					Intent intent = new Intent(FeedbackListActivity.this,
							MainGroup.class);
					intent.putExtra(Constants.START_MAINGROUP_FROM_FEEDBACKLIST_KEY, 
							Constants.START_MAINGROUP_FROM_FEEDBACKLIST_VALUE);
					startActivity(intent);
					finish();
				} else {
					Intent intent1 = new Intent(FeedbackListActivity.this,
							FeedBack.class);
					startActivity(intent1);
				}
				
			}
		});
		
		/**add by yejc 20130419 start*/
		EditOnClickListener editListener = new EditOnClickListener();
		submitLayout = (LinearLayout)findViewById(R.id.usercenter_feedback_submitbtn_layout);
		latterEditLayout = (LinearLayout)findViewById(R.id.usercenter_feedback_latter_edit_layout);
		messageEditLayout = (LinearLayout)findViewById(R.id.usercenter_feedback_message_edit_layout);
		editBut = (Button)findViewById(R.id.my_message_edit_button);
		latterSelectAllBtn = (Button)findViewById(R.id.my_latter_select_all_button);
		messageSelectAllBtn = (Button)findViewById(R.id.my_message_select_all_button);
		Button readMsgBtn = (Button)findViewById(R.id.my_latter_read_msg_button);
		Button delLatterBtn = (Button)findViewById(R.id.my_latter_delete_msg_button);
		Button delMsgBtn = (Button)findViewById(R.id.my_message_delete_msg_button);
		
		editBut.setOnClickListener(editListener);
		latterSelectAllBtn.setOnClickListener(editListener);
		messageSelectAllBtn.setOnClickListener(editListener);
		readMsgBtn.setOnClickListener(editListener);
		delLatterBtn.setOnClickListener(editListener);
		delMsgBtn.setOnClickListener(editListener);
		/**add by yejc 20130419 end*/
	}
	
	/**add by yejc 20130419 start*/
	private class EditOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			switch (v.getId()) {
			case R.id.my_message_edit_button:  //编辑按钮
				if (type == 1) {
					if (getResources().getString(R.string.my_message_edit_text)
							.equals(button.getText().toString())) {
						isLatterEdit = true;
						button.setText(R.string.my_message_edit_cancel);
						submitLayout.setVisibility(View.GONE);
						latterEditLayout.setVisibility(View.VISIBLE);
					} else {
						isLatterEdit = false;
						button.setText(R.string.my_message_edit_text);
						submitLayout.setVisibility(View.VISIBLE);
						latterEditLayout.setVisibility(View.GONE);
						latterSelectAllBtn.setText(R.string.my_message_edit_select_all);
					}
					initLatterMapStatus(false);
					messageEditLayout.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
				} else if(type == 2){
					if (getResources().getString(R.string.my_message_edit_text)
							.equals(button.getText().toString())) {
						isMessageEdit = true;
						button.setText(R.string.my_message_edit_cancel);
						submitLayout.setVisibility(View.GONE);
						messageEditLayout.setVisibility(View.VISIBLE);
					} else {
						isMessageEdit = false;
						button.setText(R.string.my_message_edit_text);
						submitLayout.setVisibility(View.VISIBLE);
						messageEditLayout.setVisibility(View.GONE);
						messageSelectAllBtn.setText(R.string.my_message_edit_select_all);
					}
					initMessageMapStatus(false);
					latterEditLayout.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
				}
				
				break;

				//站内信全选按钮
			case R.id.my_latter_select_all_button:
				isLatterSelectAll = !isLatterSelectAll;
				if (isLatterSelectAll) {
					initLatterMapStatus(true);
					button.setText(R.string.my_message_edit_cancel_select_all);
				} else {
					initLatterMapStatus(false);
					button.setText(R.string.my_message_edit_select_all);
				}
				adapter.notifyDataSetChanged();

				break;

				//站内信标记为已读按钮
			case R.id.my_latter_read_msg_button:
				if(selectLatterMap.containsValue(true)) {
					updateReadState(getSelectedLatterIds());
				}
				break;

				//站内信删除按钮
			case R.id.my_latter_delete_msg_button:
				if (selectLatterMap.containsValue(true)) {
					deleteLatter();
				}
				break;
				
				//我的留言全选按钮
			case R.id.my_message_select_all_button:
				isMessageSelectAll = !isMessageSelectAll;
				if (isMessageSelectAll) {
					initMessageMapStatus(true);
					button.setText(R.string.my_message_edit_cancel_select_all);
				} else {
					initMessageMapStatus(false);
					button.setText(R.string.my_message_edit_select_all);
				}
				listAdapter.notifyDataSetChanged();
				break;
				//我的留言删除按钮
			case R.id.my_message_delete_msg_button:
				if (selectMessageMap.containsValue(true)) {
					deleteMsg();
				}
				break;	

			default:
				break;
			}
		}
	}

	/*初始化*/
	private void initLatterMapStatus(boolean flag) {
		int count = adapter.getCount();
		selectLatterMap.clear();
		for (int i = 0; i<count; i++) {
			selectLatterMap.put(i, flag);
		}
	}
	
	private void initMessageMapStatus(boolean flag) {
		int count = listAdapter.getCount();
		selectMessageMap.clear();
		for (int i = 0; i<count; i++) {
			selectMessageMap.put(i, flag);
		}
	}
	
//	private void initMapStatus(BaseAdapter adapter, Map<Integer, Boolean> map, boolean flag) {
//		int count = adapter.getCount();
//		map.clear();
//		for (int i = 0; i<count; i++) {
//			map.put(i, flag);
//		}
//	}
	
	/*获取选择站内信的所有ID*/
	private String getSelectedLatterIds() {
		String ids = "";
		selectedMsgList.clear();
		for (int i = 0; i < selectLatterMap.size(); i++) {
			if (selectLatterMap.get(i)) {
				ids += latterlist.get(i).getNewsId()+",";
				selectedMsgList.add(i);
			}
		}
		ids = ids.substring(0, ids.length() - 1);
		return ids;
	}
	/*获取选择我的留言的所有ID*/
	private String getSelectedMsgIds() {
		String ids = "";
		selectedMsgList.clear();
		for (int i = 0; i < selectMessageMap.size(); i++) {
			if (selectMessageMap.get(i)) {
				ids += feedList.get(i).get("id")+",";
				selectedMsgList.add(i);
			}
		}
		ids = ids.substring(0, ids.length() - 1);
		return ids;
	}
	/*删除站内信方法*/
	private void deleteLatter() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = DeleteMessage.deleteMsg(LATTERCOMMAND,
						ProtocolManager.REQUESTTYPE, getSelectedLatterIds());
				try {
					JSONObject obj = new JSONObject(str);
					String strarry = obj.getString("error_code");
					if ("0000".equals(strarry)) {
						handler.sendEmptyMessage(1);
					} else {
						Toast.makeText(FeedbackListActivity.this,
								R.string.feedback_network_connection_error,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/*删除我的留言方法*/
	private void deleteMsg() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = DeleteMessage.deleteMsg(MSGCOMMAND,
						ProtocolManager.TYPE, getSelectedMsgIds());
				try {
					JSONObject obj = new JSONObject(str);
					String strarry = obj.getString("error_code");
					if ("0000".equals(strarry)) {
						handler.sendEmptyMessage(2);
					} else {
						Toast.makeText(FeedbackListActivity.this,
								R.string.feedback_network_connection_error,
								Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public class ShowSelectTextBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getIntExtra(infoType, -1);
			if (type == 1) {
				if (intent.getBooleanExtra(selectAllKey, false)) {
					latterSelectAllBtn.setText(R.string.my_message_edit_select_all);
					isLatterSelectAll = false;
				} else {
					if(!selectLatterMap.containsValue(false)) {
						isLatterSelectAll = true;
						latterSelectAllBtn.setText(R.string.my_message_edit_cancel_select_all);
					}
				}
			} else if (type == 2){
				if (intent.getBooleanExtra(selectAllKey, false)) {
					messageSelectAllBtn.setText(R.string.my_message_edit_select_all);
					isMessageSelectAll = false;
				} else {
					if(!selectMessageMap.containsValue(false)) {
						isMessageSelectAll = true;
						messageSelectAllBtn.setText(R.string.my_message_edit_cancel_select_all);
					}
				}
			}
			
		}
		
	}
	/**add by yejc 20130419 end*/

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent("noreadupdate");
			switch (msg.what) {
			/**add by yejc 20130423 start*/
			case 0:
				boolean isReadMsg = false;
				isLatterEdit = false;
				isLatterSelectAll = false;
				latterSelectAllBtn.setText(R.string.my_message_edit_select_all);
				editBut.setText(R.string.my_message_edit_text);
				submitLayout.setVisibility(View.VISIBLE);
				latterEditLayout.setVisibility(View.GONE);
				initLatterMapStatus(false);
				for(int i=0; i<selectedMsgList.size();i++) {
					if ("0".equals(((LotInfoDomain)latterlist.get(selectedMsgList.get(i))).getReadState())) {
						((LotInfoDomain)latterlist.get(selectedMsgList.get(i))).setReadState("1");
						notReadLetterCount--;
						isReadMsg = true;
					}
				}
				adapter.notifyDataSetChanged();
				initLatterMapStatus(false);
				if (isReadMsg) {
					if (mTabHost.getCurrentTab() == 1) {
						if (notReadLetterCount >= 0) {
							shellRW.putStringValue("notReadLetterCount",
									String.valueOf(notReadLetterCount));
							lettercount.setVisibility(View.VISIBLE);
							lettercount.setText(String.valueOf(notReadLetterCount));
						} else {
							lettercount.setVisibility(View.GONE);
						}
					}
					mTabHost.invalidate();
					sendBroadcast(intent);
				}
				selectedMsgList.clear();
				break;
				
			case 1:
				boolean isDeleteReadMsg = false;
				Collections.sort(selectedMsgList);
				for(int i=selectedMsgList.size() -1; i>-1;i--) {
					if ("0".equals(((LotInfoDomain)latterlist.get(selectedMsgList.get(i))).getReadState())) {
						notReadLetterCount--;
						isDeleteReadMsg = true;
					}
					int index = (int)selectedMsgList.get(i);
					latterlist.remove(index);
				}
				adapter.notifyDataSetChanged();
				initLatterMapStatus(false);
				if (isDeleteReadMsg) {
					if (mTabHost.getCurrentTab() == 1) {
						if (notReadLetterCount >= 0) {
							shellRW.putStringValue("notReadLetterCount",
									String.valueOf(notReadLetterCount));
							lettercount.setVisibility(View.VISIBLE);
							lettercount.setText(String.valueOf(notReadLetterCount));
						} else {
							lettercount.setVisibility(View.GONE);
						}
					}
					mTabHost.invalidate();
					sendBroadcast(intent);
				}
				selectedMsgList.clear();
				break;	
				
			case 2:
				Collections.sort(selectedMsgList);
				for(int i=selectedMsgList.size()-1; i>-1;i--) {
					int index = (int)selectedMsgList.get(i);
					feedList.remove(index);
				}
				initMessageMapStatus(false);
				listAdapter.notifyDataSetChanged();
				break;

			default:
				break;
				/**add by yejc 20130423 end*/
			}
		}
	};

	/**
	 * 点击向后按钮调用的方法
	 */
	private void addmore() {
		
		latterIndex++;
		if (latterIndex > latterPages - 1) {
			latterIndex = latterPages - 1;
			progressbar.setVisibility(view.INVISIBLE);
			/**add by yejc 20130422 start*/
			Intent intent = new Intent(BROADCAST_ACTION);
			intent.putExtra(selectAllKey, false);
			intent.putExtra(infoType, type);
			sendBroadcast(intent);
			/**add by yejc 20130422 end*/
			// tabSpecListView.removeFooterView(view);
			Toast.makeText(FeedbackListActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		} else {
			getInfoNet(userno, latterIndex, true);
		}

	}

	private void getInfoNet(final String userno1, final int pageIndex, final boolean flag) {
		if (latterIndex == 0) {
			showDialog(0);
		} else if (progressbar != null) {
			progressbar.setVisibility(ProgressBar.VISIBLE);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = LetterQueryInterface.getInstance().letter(userno1,
						"10", String.valueOf(pageIndex)); // 1彩民趣闻
				String strarry = null;
				try {
					JSONObject obj = new JSONObject(str);
					strarry = obj.getString("result");
					latterPages = Integer.parseInt(obj.getString("totalPage"));
					JSONArray news = new JSONArray(strarry);
					for (int i = 0; i < news.length(); i++) {
						JSONObject contentnews = news.getJSONObject(i);
						LotInfoDomain domain = new LotInfoDomain();
						domain.setTitle(contentnews.getString("title"));
						domain.setContent(contentnews.getString("content"));
						domain.setReadState(contentnews.getString("readState"));
						domain.setNewsId(contentnews.getString("id"));
						latterlist.add(domain);
					}
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (dialog != null) {
								dismissDialog(0);
							}

							if (progressbar != null && view != null) {
								progressbar.setVisibility(ProgressBar.GONE);
								view.setEnabled(true);
							}
							if (latterIndex == 0) {
								initLinear(latter, linearId[1],
										initlatterview(false));
							} else {
								/**add by yejc 20130422 start*/
								if (flag) {
									Map<Integer, Boolean> tempMap = new HashMap<Integer, Boolean>();
									int count = adapter.getCount();
									tempMap.clear();
									for (int i = 0; i<count; i++) {
										tempMap.put(i, false);
									}
									tempMap.putAll(selectLatterMap);
									selectLatterMap = tempMap;
									adapter.notifyDataSetChanged();
									Intent intent = new Intent(BROADCAST_ACTION);
									intent.putExtra(selectAllKey, true);
									intent.putExtra(infoType, type);
									sendBroadcast(intent);
									/**add by yejc 20130422 end*/
								} else {
									adapter.notifyDataSetChanged();
								}
								
							}
						}
					});

				} catch (JSONException e) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (dialog != null) {
								dismissDialog(0);
							}
							if (progressbar != null && view != null) {
								progressbar.setVisibility(ProgressBar.GONE);
								view.setEnabled(true);
							}
						}
					});
					e.printStackTrace();
				}

			}
		}).start();
	}

	public class InfoAdapter extends BaseAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<LotInfoDomain> mList;

		public InfoAdapter(Context context, List<LotInfoDomain> informationdata) {
			mInflater = LayoutInflater.from(context);
			mList = informationdata;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final int index = position;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.informationitem, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.informationitemlable);
				holder.content = (TextView) convertView
						.findViewById(R.id.informationcontent);
				holder.layout = (RelativeLayout) convertView
						.findViewById(R.id.informationitemlayout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position % 2 == 0) {
				holder.layout
						.setBackgroundResource(R.drawable.zx_list_bg_white);
			} else {
				holder.layout.setBackgroundResource(R.drawable.zx_list_bg_gray);
			}
			/**add by yejc 20130419 start*/
			if (isLatterEdit) {
				if (mList.get(position).getReadState().equals("0")) {
					holder.content.setTextColor(Color.RED);
				} else {
					holder.content.setTextColor(Color.BLACK);
				}
				holder.content.setText(mList.get(position).getTitle());
				holder.content.setEnabled(false);
				holder.icon.setVisibility(View.VISIBLE);
				if (selectLatterMap.containsKey(position)) {
					if (selectLatterMap.get(position)) {
						holder.icon.setImageResource(R.drawable.check_button_b);
					} else {
						holder.icon.setImageResource(R.drawable.check_button);
					}
				} else {
					holder.icon.setImageResource(R.drawable.check_button);
				}
				holder.icon.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ImageView iv = (ImageView)v;
						if (selectLatterMap.containsKey(position)) {
							Intent intent = new Intent(BROADCAST_ACTION);
							intent.putExtra(infoType, type);
							if (selectLatterMap.get(position)) {
								selectLatterMap.put(position, false);
								intent.putExtra(selectAllKey, true);
								iv.setImageResource(R.drawable.check_button);
							} else {
								selectLatterMap.put(position, true);
								intent.putExtra(selectAllKey, false);
								iv.setImageResource(R.drawable.check_button_b);
							}
							sendBroadcast(intent);
						}
					}
				});
			} else {
				if (mList.get(position).getReadState().equals("0")) {
					holder.icon.setVisibility(View.VISIBLE);
					holder.icon.setImageResource(R.drawable.notread);
				} else {
					holder.icon.setVisibility(View.GONE);
				}
				holder.content.setText(Html.fromHtml((String) mList.get(position)
						.getTitle()));
				holder.content.setEnabled(true);
				holder.content.setTextColor(mList.get(position).getTextcolor());
				final ImageView iconread = holder.icon;
				holder.content.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						TextView textview = (TextView) v;
//						mList.get(index).setTextcolor(Color.RED);
						textview.setTextColor(mList.get(index).getTextcolor());
						contentjson = mList.get(index).getContent();
						title = mList.get(index).getTitle();
						if (mList.get(index).getReadState().equals("0")) {
							iconread.setVisibility(View.GONE);
							if (mTabHost.getCurrentTab() == 1) {
								if (notReadLetterCount > 0) {
									notReadLetterCount--;
									shellRW.putStringValue("notReadLetterCount",
											String.valueOf(notReadLetterCount));

								} else {
									lettercount.setVisibility(View.GONE);
								}
							}

							mTabHost.invalidate();
							Intent intent = new Intent("noreadupdate");
							sendBroadcast(intent);

							//改变已经阅读信息的状态
							mList.get(index).setReadState("1");
							
							updateReadState(mList.get(index).getNewsId());
						}
						turnContentActivity();
					}
				});
			}
			/**add by yejc 20130419 end*/
			return convertView;
		}

		class ViewHolder {
			RelativeLayout layout;
			ImageView icon;
			TextView content;
		}

	}

	private void turnContentActivity() {
		Intent intent = new Intent(FeedbackListActivity.this,
				MyLetterActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("content", contentjson);
		bundle.putString("title", title);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private void updateReadState(final String id) {
		
		// showDialog(0);
		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			@Override
			public void run() {
				String str = UpdateReadState.getInstance().updateReadState(id);
				if (isLatterEdit) {
					try {
						JSONObject obj = new JSONObject(str);
						String strarry = obj.getString("error_code");
						if ("0000".equals(strarry)) {
							handler.sendEmptyMessage(0);
						} else {
							Toast.makeText(FeedbackListActivity.this,
									R.string.feedback_network_connection_error,
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void msgUpdateReadState(final String id) {

		// showDialog(0);
		new Thread(new Runnable() {

			@SuppressWarnings("static-access")
			@Override
			public void run() {
				MsgUpdateReadState.getInstance().updateReadState(id);
			}
		}).start();
	}

	public class FeedbackListAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private List<Map<String, Object>> mlist;

		public FeedbackListAdapter(Context context,
				List<Map<String, Object>> list) {
			inflater = LayoutInflater.from(context);
			this.mlist = list;
		}

		@Override
		public int getCount() {
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			String createTime = (String) mlist.get(position).get(CREATETIME);
			String reply = (String) mlist.get(position).get(REPLY);
			String content = (String) mlist.get(position).get(CONTENT);
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.usercenter_feedback_listitem, null);
				holder = new ViewHolder();
				holder.time = (TextView) convertView
						.findViewById(R.id.feedback_time);
				holder.feedcontent = (TextView) convertView
						.findViewById(R.id.feededmessage);
				holder.reply = (TextView) convertView
						.findViewById(R.id.servicer_reply);
				/**add by yejc 20130422 start*/
				holder.icon = (ImageView)convertView
						.findViewById(R.id.informationitemlable);
				/**add by yejc 20130422 end*/
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String formatTime = "我于<font color=\"red\">" + createTime
					+ "</font>留言道：";
			holder.time.setText(Html.fromHtml(formatTime));
			holder.feedcontent.setText(formatContent(content));
			if (reply.equals("null") || reply.equals("")) {
				reply = "您的留言正在处理……";
			}
			holder.reply.setText(reply);
			holder.feedcontent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				
				}
			});
			
			/**add by yejc 20130422 start*/
			if (isMessageEdit) {
				holder.icon.setVisibility(View.VISIBLE);
				if (selectMessageMap.containsKey(position)) {
					if (selectMessageMap.get(position)) {
						holder.icon.setImageResource(R.drawable.check_button_b);
					} else {
						holder.icon.setImageResource(R.drawable.check_button);
					}
				} else {
					holder.icon.setImageResource(R.drawable.check_button);
				}
				holder.icon.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ImageView iv = (ImageView)v;
						if (selectMessageMap.containsKey(position)) {
							Intent intent = new Intent(BROADCAST_ACTION);
							intent.putExtra(infoType, type);
							if (selectMessageMap.get(position)) {
								selectMessageMap.put(position, false);
								intent.putExtra(selectAllKey, true);
								iv.setImageResource(R.drawable.check_button);
							} else {
								selectMessageMap.put(position, true);
								intent.putExtra(selectAllKey, false);
								iv.setImageResource(R.drawable.check_button_b);
							}
							sendBroadcast(intent);
						}
					}
				});
			} else {
				holder.icon.setVisibility(View.GONE);
			}
			
			/**add by yejc 20130422 end*/
			convertView.setTag(holder);
			return convertView;
		}

		class ViewHolder {
			TextView time;
			TextView feedcontent;
			TextView reply;
			ImageView icon;
		}
	}

	private String formatContent(String content) {
		String formatcontent = "";
		formatcontent = content.split("\\|")[0];
		return formatcontent;
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**add by yejc 20130419 start*/
		unregisterReceiver(selectTextBroadCast);
		 /**add by yejc 20130419 end*/
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		/**add by yejc 20130419 start*/
		IntentFilter filter = new IntentFilter(BROADCAST_ACTION);    
        registerReceiver(selectTextBroadCast, filter);
        /**add by yejc 20130419 end*/
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("网络连接中...");
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
	}
	
	private View initSystemInfo() {
		tabSpecLinearView = (LinearLayout) mInflater.inflate(
				R.layout.usercenter_listview_layout, null);
		tabSpecListView = (ListView) tabSpecLinearView
				.findViewById(R.id.usercenter_listview_queryinfo);
		OperatingDataBases operatingDb = new OperatingDataBases(this);
		List<String> infoList = operatingDb.getInfoList();
		if (infoList != null) {
			SystemInfoAdapter adapter = new SystemInfoAdapter(infoList);
			tabSpecListView.setAdapter(adapter);
		}
		return tabSpecLinearView;
	}
	
	private class SystemInfoAdapter extends BaseAdapter {

		List<String> mInfoList;
		
		public SystemInfoAdapter(List<String> infoList) {
			this.mInfoList = infoList;
		}

		@Override
		public int getCount() {
			return mInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.informationitem, null);
				holder = new ViewHolder();
				holder.content = (TextView) convertView
						.findViewById(R.id.informationcontent);
				holder.layout = (RelativeLayout) convertView
						.findViewById(R.id.informationitemlayout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.content.setText(mInfoList.get(position));
			if (position % 2 == 0) {
				holder.layout.setBackgroundResource(R.drawable.zx_list_bg_white);
			} else {
				holder.layout.setBackgroundResource(R.drawable.zx_list_bg_gray);
			}
			return convertView;
		}
		
		private class ViewHolder {
			RelativeLayout layout;
			TextView content;
		}
		
	}
}
