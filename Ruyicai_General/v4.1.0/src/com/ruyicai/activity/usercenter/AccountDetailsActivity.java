package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.AccountDetailQueryInterface;
import com.ruyicai.net.newtransaction.pojo.AccountDetailQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 账户明细
 * 
 * @author miao
 */
public class AccountDetailsActivity extends Activity {
	private int pageallindex = 0, pagewithdrawindex = 0, pagepayindex = 0,
			pagesendprizeindex = 0, pagechargeindex = 0;
	private int[] linearId = { R.id.usercenterAccountdetailContent_all,
			R.id.usercenterAccountdetailContent_cz,
			R.id.usercenterAccountdetailContent_zf,
			R.id.usercenterAccountdetailContent_pj,
			R.id.usercenterAccountdetailContent_tx };
	private String[] titles = { "全部", "充值", "投注", "派奖", "提现" };
	private LinearLayout linearall, linearcharge, linearpay, linearsendprize,
			linearwithdraw;
	private LayoutInflater mInflater = null;
	private Button returnButton;
	private String jsonString;
	ProgressDialog dialog;
	private final String AMT = "amt", MEMO = "memo", PLATTIME = "platTime",
			TTRANSACTIONTYPE = "ttransactionType", TEXTCOLOR = "textcolor",
			BLSIGN = "blsign";
	private final int DIALOG1_KEY = 0;
	private String phonenum, sessionid, userno;
	List<AccountDetailQueryInfo> alldatalist = new ArrayList<AccountDetailQueryInfo>();
	List<AccountDetailQueryInfo> paydatalist = new ArrayList<AccountDetailQueryInfo>();
	List<AccountDetailQueryInfo> sendprizesdatalist = new ArrayList<AccountDetailQueryInfo>();
	List<AccountDetailQueryInfo> withdrawdatalist = new ArrayList<AccountDetailQueryInfo>();
	List<AccountDetailQueryInfo> chargedatalist = new ArrayList<AccountDetailQueryInfo>();
	String jsonobject;
	TabHost mTabHost;
	AccountQueryAdapter alladapter;
	AccountQueryAdapter payadadapter;
	AccountQueryAdapter sendprizesdatadadapter;
	AccountQueryAdapter withdrawdataadadapter;
	AccountQueryAdapter chargedataadadapter;
	private View view;
	private ProgressBar progressbar;

	private String withdrawAmt, chargeAmt, payAmt, prizeAmt;
	int allPages, withdrawPages, chargePages, payPages, sendPrizePages;
	boolean isAllFirst = true, isChargeFirst = true, isSendPrizeFirst = true,
			isWithdrawFirst = true, isPayFirst = true;
	View tabSpecLinearView;// 子列表的ListView
	ListView tabSpecListView;// 子列表的ListView
	Button more;// 获取更多
	int type = 0;
	/** 联网处理handler */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(AccountDetailsActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				allPages = encodejson((String) msg.obj, alldatalist);
				alladapter.notifyDataSetChanged();
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				chargePages = encodejson((String) msg.obj, chargedatalist);
				if (pagechargeindex == 0) {
					initLinear(
							linearcharge,
							linearId[1],
							initLinearView(pagechargeindex, chargePages,
									chargedatalist));
				} else {
					chargedataadadapter.notifyDataSetChanged();
				}
				break;
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				payPages = encodejson((String) msg.obj, paydatalist);
				if (pagepayindex == 0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					initLinear(linearcharge, linearId[2],
							initLinearView(pagepayindex, payPages, paydatalist));
				} else {
					payadadapter.notifyDataSetChanged();
				}
				break;
			case 4:
				if (dialog != null) {
					dialog.dismiss();
				}
				sendPrizePages = encodejson((String) msg.obj,
						sendprizesdatalist);
				if (pagesendprizeindex == 0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					initLinear(
							linearcharge,
							linearId[3],
							initLinearView(pagesendprizeindex, sendPrizePages,
									sendprizesdatalist));
				} else {
					sendprizesdatadadapter.notifyDataSetChanged();
				}
				break;
			case 5:
				if (dialog != null) {
					dialog.dismiss();
				}
				withdrawPages = encodejson((String) msg.obj, withdrawdatalist);
				if (pagewithdrawindex == 0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					initLinear(
							linearcharge,
							linearId[4],
							initLinearView(pagewithdrawindex, withdrawPages,
									withdrawdatalist));
				} else {
					if(withdrawdataadadapter != null){
						withdrawdataadadapter.notifyDataSetChanged();
					}
				}
				isWithdrawFirst = false;
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_accountdetail_layout);
		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
		}
		mTabHost.setOnTabChangedListener(accountTabChangedListener);
		returnButton = (Button) findViewById(R.id.usercenter_accountdetail_img_return);
		returnButton.setText(R.string.returnlastpage);
		initReturn();
		jsonobject = this.getIntent().getStringExtra("allaccountjson");
		allPages = encodejson(jsonobject, alldatalist);
		initLinear(linearall, linearId[0],
				initLinearView(pageallindex, allPages, alldatalist));
	}

	/**
	 * TabHost切换监听器
	 */
	TabHost.OnTabChangeListener accountTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			for (int i = 0; i < titles.length; i++) {
				if (tabId.equals(titles[0])) {
					type = 0;
					initLinear(linearall, linearId[0],
							initLinearView(pageallindex, allPages, alldatalist));
				} else if (tabId.equals(titles[1])) {
					type = 1;
					if (isChargeFirst) {
						getAccountDataNet(0, 1);
						isChargeFirst = false;
						break;
					} else {
						if (chargedatalist.size() != 0) {
							initLinear(
									linearcharge,
									linearId[1],
									initLinearView(pagechargeindex,
											chargePages, chargedatalist));
						}
					}
				} else if (tabId.equals(titles[2])) {
					type = 2;
					if (isPayFirst) {
						getAccountDataNet(0, 2);
						isPayFirst = false;
						break;
					} else {
						if (paydatalist.size() != 0) {
							initLinear(
									linearpay,
									linearId[2],
									initLinearView(pagepayindex, payPages,
											paydatalist));
						}
					}
				} else if (tabId.equals(titles[3])) {
					type = 3;
					if (isSendPrizeFirst) {
						isSendPrizeFirst = false;
						getAccountDataNet(0, 3);
						break;
					} else {
						if (sendprizesdatalist.size() != 0) {
							initLinear(
									linearsendprize,
									linearId[3],
									initLinearView(pagesendprizeindex,
											sendPrizePages, sendprizesdatalist));
						}
					}
				} else if (tabId.equals(titles[4])) {
					type = 4;
					if (isWithdrawFirst) {
						getAccountDataNet(0, 4);
						isWithdrawFirst = false;
						break;
					} else {
						if (withdrawdatalist.size() != 0) {
							initLinear(
									linearwithdraw,
									linearId[4],
									initLinearView(pagewithdrawindex,
											withdrawPages, withdrawdatalist));
						}
					}
				}
			}
		}
	};

	/**
	 * 从ShellRWSharesPreferences中获取phonenum 、sessionid 和userno
	 */
	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	/**
	 * 账户明细查询联网
	 * 
	 * @param pageindexgift
	 *            页数
	 * @param type
	 *            账户明细查询类型
	 */
	private void netting(final int pageindexgift) {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				initPojo();
				AccountDetailQueryPojo accountQueryPojo = new AccountDetailQueryPojo();
				accountQueryPojo.setUserno(userno);
				accountQueryPojo.setSessionid(sessionid);
				accountQueryPojo.setPhonenum(phonenum);
				accountQueryPojo.setPageindex(String.valueOf(pageindexgift));
				accountQueryPojo.setMaxresult("10");
				accountQueryPojo.setTransactiontype(String.valueOf(type));
				accountQueryPojo.setType("new");

				Message msg = new Message();
				jsonString = AccountDetailQueryInterface.getInstance()
						.accountDetailQuery(accountQueryPojo);
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(ProgressBar.INVISIBLE);
						view.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					if (errcode.equals("0000")) {
						setNewPage(pageindexgift);
						msg.what = type + 1;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void getAccountDataNet(final int pageindexgift, final int type) {
		showDialog(0);
		netting(pageindexgift);
	}

	/**
	 * 初始化用于布局Adapter的List
	 * 
	 * @param pageindex
	 * @param giftdatalist
	 * @return
	 */

	/**
	 * 解析联网获取的字符串
	 * 
	 * @param json
	 *            联网获取的字符串
	 * @param list
	 *            对应联网类型的list数组
	 */
	public int encodejson(String json, List<AccountDetailQueryInfo> list) {
		int typeAllPage = 0;
		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			typeAllPage = Integer.parseInt(winprizejsonobj
					.getString("totalPage"));
			setAmt(winprizejsonobj.getString("totalAmt"));
			String winprizejsonstring = winprizejsonobj.getString("result");
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					AccountDetailQueryInfo accountDetailInfo = new AccountDetailQueryInfo();
					accountDetailInfo.setAmt(winprizejson.getJSONObject(i)
							.getString(AMT));
					accountDetailInfo.setMemo(winprizejson.getJSONObject(i)
							.getString(MEMO));
					accountDetailInfo.setPlatTime(winprizejson.getJSONObject(i)
							.getString(PLATTIME));
					accountDetailInfo.setTtransactionType(winprizejson
							.getJSONObject(i).getString(TTRANSACTIONTYPE));
					accountDetailInfo.setBlsign(winprizejson.getJSONObject(i)
							.getString(BLSIGN));
					list.add(accountDetailInfo);
				} catch (Exception e) {
				}
			}
		} catch (JSONException e) {
			try {
				JSONObject winprizejson = new JSONObject(json);
			} catch (JSONException e1) {
			}
		}

		return typeAllPage;
	}

	private void setAmt(String amt) {
		amt = PublicMethod.toYuan(amt);
		switch (type) {
		case 1:
			chargeAmt = amt;
			break;
		case 2:
			payAmt = amt;
			break;
		case 3:
			prizeAmt = amt;
			break;
		case 4:
			withdrawAmt = amt;
			break;
		}
	}

	/**
	 * 为TabHost添加TabSpec
	 * 
	 * @param index
	 */
	public void addTab(int index) {
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab
				.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab
				.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}

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

	/**
	 * 初始化账户明细TabHost子布局
	 * 
	 * @param pageindex
	 *            子项对应的页数
	 * @param allpagenum
	 *            子项对应的总页数
	 * @param typelist
	 *            子项对应的List
	 * @param isfirst
	 *            确定子项是不是初次联网（TabHost切换的联网）
	 * @return 初始化之后的view
	 */
	private View initLinearView(final int pageindex, int allpage,
			final List typelist) {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tabSpecLinearView = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		tabSpecListView = (ListView) tabSpecLinearView
				.findViewById(R.id.usercenter_listview_queryinfo);
		initViewInfo(allpage, typelist, tabSpecLinearView, tabSpecListView);
		initListView(tabSpecListView);
		return tabSpecLinearView;
	}

	private void initViewInfo(int page, final List list, View view,
			ListView listView) {
		LinearLayout layout = (LinearLayout) view
				.findViewById(R.id.layout_account);
		TextView textName = (TextView) view.findViewById(R.id.layout_text_name);
		TextView textAmt = (TextView) view.findViewById(R.id.layout_text_amt);
		switch (type) {
		case 0:
			layout.setVisibility(View.GONE);
			allPages = page;
			alldatalist = list;
			break;
		case 1:
			layout.setVisibility(View.GONE);
			chargePages = page;
			chargedatalist = list;
			break;
		case 2:
			layout.setVisibility(View.GONE);
			payPages = page;
			paydatalist = list;
			break;
		case 3:
			listView.setPadding(0, 0, 0, PublicMethod.getPxInt(30, this));
			layout.setVisibility(View.VISIBLE);
			textName.setText(R.string.account_details_name_zhongjiang);
			textAmt.setText(prizeAmt + "元");
			sendPrizePages = page;
			sendprizesdatalist = list;
			break;
		case 4:
			listView.setPadding(0, 0, 0, PublicMethod.getPxInt(30, this));
			layout.setVisibility(View.VISIBLE);
			textName.setText(R.string.account_details_name_tixian);
			textAmt.setText(withdrawAmt + "元");
			withdrawPages = page;
			withdrawdatalist = list;
			break;
		}
	}

	/**
	 * 点击向后按钮调用的方法
	 */
	private void addmore() {
		int pageIndex = getNewPage();
		int allpagenum = getAllPage();
		List<AccountDetailQueryInfo> typelist = getList();
		pageIndex++;
		if (pageIndex > allpagenum - 1) {
			pageIndex = allpagenum - 1;
			setNewPage(pageIndex);
			progressbar.setVisibility(view.INVISIBLE);
			tabSpecListView.removeFooterView(view);
			Toast.makeText(AccountDetailsActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		} else {
			netting(pageIndex);
		}

	}

	private void setNewPage(int page) {
		switch (type) {
		case 0:
			pageallindex = page;
			break;
		case 1:
			pagechargeindex = page;
			break;
		case 2:
			pagepayindex = page;
			break;
		case 3:
			pagesendprizeindex = page;
			break;
		case 4:
			pagewithdrawindex = page;
			break;
		}
	}

	private int getNewPage() {
		int page = 0;
		switch (type) {
		case 0:
			page = pageallindex;
			break;
		case 1:
			page = pagechargeindex;
			break;
		case 2:
			page = pagepayindex;
			break;
		case 3:
			page = pagesendprizeindex;
			break;
		case 4:
			page = pagewithdrawindex;
			break;
		}
		return page;
	}

	private int getAllPage() {
		int page = 0;
		switch (type) {
		case 0:
			page = allPages;
			break;
		case 1:
			page = chargePages;
			break;
		case 2:
			page = payPages;
			break;
		case 3:
			page = sendPrizePages;
			break;
		case 4:
			page = withdrawPages;
			break;
		}
		return page;
	}

	private List<AccountDetailQueryInfo> getList() {
		List<AccountDetailQueryInfo> list = null;
		switch (type) {
		case 0:
			list = alldatalist;
			break;
		case 1:
			list = chargedatalist;
			break;
		case 2:
			list = paydatalist;
			break;
		case 3:
			list = sendprizesdatalist;
			break;
		case 4:
			list = withdrawdatalist;
			break;
		}
		return list;
	}

	// 根据type获得适配器
	private AccountQueryAdapter getadapter() {
		AccountQueryAdapter listadapter = null;
		switch (type) {
		case 0:
			alladapter = new AccountQueryAdapter(this, alldatalist);
			listadapter = alladapter;
			break;
		case 1:
			chargedataadadapter = new AccountQueryAdapter(this, chargedatalist);
			listadapter = chargedataadadapter;
			break;
		case 2:
			payadadapter = new AccountQueryAdapter(this, paydatalist);
			listadapter = payadadapter;
			break;
		case 3:
			sendprizesdatadadapter = new AccountQueryAdapter(this,
					sendprizesdatalist);
			listadapter = sendprizesdatadadapter;
			break;
		case 4:
			withdrawdataadadapter = new AccountQueryAdapter(this,
					withdrawdatalist);
			listadapter = withdrawdataadadapter;
			break;
		}
		return listadapter;
	}

	/**
	 * 初始化ListView列表
	 * 
	 * @param listview
	 * @param list
	 */
	private void initListView(ListView listview) {

		LayoutInflater mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		listview.addFooterView(view);
		// 数据源
		listview.setAdapter(getadapter());
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				view.setEnabled(false);
				addmore();

			}
		});

	}

	protected void initReturn() {
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 内部类pojo
	 */
	class AccountDetailQueryInfo {
		private String platTime = "";// 变动时间
		private String memo = "";// 账户科目明细描述
		private String amt = "";// 变动金额
		private String ttransactionType = "";// 账户类型
		private String blsign = "";// 进出帐标识 -1出账，1进账

		public String getBlsign() {
			return blsign;
		}

		public void setBlsign(String blsign) {
			this.blsign = blsign;
		}

		public String getTtransactionType() {
			return ttransactionType;
		}

		public void setTtransactionType(String ttransactionType) {
			this.ttransactionType = ttransactionType;
		}

		public String getPlatTime() {
			return platTime;
		}

		public void setPlatTime(String platTime) {
			this.platTime = platTime;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}
	}

	/**
	 * 账户中心查询的适配器
	 */
	public class AccountQueryAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<AccountDetailQueryInfo> mList;

		public AccountQueryAdapter(Context context,
				List<AccountDetailQueryInfo> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		public int getCount() {
			return mList.size();
		}

		public Object getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String accountMemo = (String) mList.get(position).getMemo();
			String accountPlattime = (String) mList.get(position).getPlatTime();
			String amt = (String) mList.get(position).getAmt();
			String amtType = (String) mList.get(position).getTtransactionType();
			String blsign = (String) mList.get(position).getBlsign();
			int amtColor = amountTextColor(blsign);
			String accountAmt = PublicMethod.formatMoney(amt);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.usercenter_accountdetails_listitem, null);
				holder = new ViewHolder();
				holder.memo = (TextView) convertView
						.findViewById(R.id.user_center_account_detail_trading_mode_id);
				holder.platTime = (TextView) convertView
						.findViewById(R.id.user_center_account_detail_trading_date_id);
				holder.amt = (TextView) convertView
						.findViewById(R.id.user_center_account_detail_yu_e_id);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.amt.setTextColor(amtColor);
			holder.memo.setText(accountMemo);
			holder.platTime.setText(accountPlattime);
			holder.amt.setText(amountText(blsign) + accountAmt);
			convertView.setTag(holder);
			return convertView;
		}

		class ViewHolder {
			TextView amt;// 账户变动金额
			TextView memo;// 账户变动标示
			TextView platTime;// 账户变动时间
		}
	}

	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		switch (id) {
		case DIALOG1_KEY: {
			dialog.setTitle(R.string.usercenter_netDialogTitle);
			dialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return dialog;
	}

	/**
	 * 根据用户账户的交易类型ttransactionType，来决定金额的颜色
	 * 
	 * @param tView
	 *            TextView
	 * @param type
	 *            ttransactionType
	 */
	private int amountTextColor(String type) {
		int Colortype = 1;
		int textColor = 0xff333333;
		try {
			Colortype = Integer.parseInt(type);
		} catch (Exception e) {
		}
		switch (Colortype) {
		case 1:
			textColor = 0xffcc0000;// 中奖为红色
			break;
		case -1:
			textColor = 0xff006600;// 提现为绿色
			return textColor;
		default:
			break;
		}
		return textColor;
	}

	/**
	 * 根据用户账户的交易类型ttransactionType，来决定金额的颜色
	 * 
	 * @param tView
	 *            TextView
	 * @param type
	 *            ttransactionType
	 */
	private String amountText(String type) {
		int Colortype = 1;
		String textColor = "";
		try {
			Colortype = Integer.parseInt(type);
		} catch (Exception e) {
		}
		switch (Colortype) {
		case 1:
			textColor = "+";// 红色为加号
			break;
		case -1:
			textColor = "-";// 绿色为减号
			break;
		default:
			break;
		}
		return textColor;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
