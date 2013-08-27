/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.GiftReciveCodeInterface;
import com.ruyicai.net.newtransaction.GiftReciveInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 增送查询
 * 
 * @author miao
 */
public class GiftQueryActivity extends Activity {
	private final static int GIFT = 0;
	private final static int GIFTED = 1;
	private int giftType = GIFT;
	private LayoutInflater mInflater = null;
	private LinearLayout usecenerLinear, usecenerLineargifted;
	private Button returnButton;
	private TextView titleTextView;
	String jsonString;
	final String AMOUNT = "amount", BETCODE = "betCode",
			BATCHCODE = "batchCode", LOTNO = "lotNo", ORDERTIME = "orderTime",
			LOTMULTI = "lotMulti", ISGIFT = "isgift";
	final String GIFTEDUSERNO = "toMobileId", GIFTUSERNO = "giftMobileId",
			USERNO = "userno", STATE = "reciveState", ID = "orderId";
	private final int DIALOG1_KEY = 0;
	private String phonenum, sessionid, userno;
	int[] linearId = { R.id.usercentergiftquery_gift,
			R.id.usercentergiftquery_gifted };
	List<GIftQueryInfo> giftdatalist = new ArrayList<GIftQueryInfo>();
	List<GIftQueryInfo> gifteddatalist = new ArrayList<GIftQueryInfo>();
	ProgressDialog dialog;
	String jsonobject;
	TabHost mTabHost;
	private String[] titles = { "赠送的彩票", "收到的彩票" };
	int allPage, allPagegifted;
	int pageindex = 1, pageindexgifted = 1;
	int giftmaxpage, giftedmaxpage;

	boolean isfirst = true, isGifedfirst = true;
	ListView queryinfolist;
	View view;
	GiftQueryAdapter giftadapter;
	GiftQueryAdapter giftedadapter;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(GiftQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (dialog != null) {
					dialog.dismiss();
				}
				isfirst = false;
				encodegiftedjson((String) msg.obj, giftdatalist, 0);
				giftadapter.notifyDataSetChanged();
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				if (view != null) {
				}
				isGifedfirst = false;
				encodegiftedjson((String) msg.obj, gifteddatalist, 1);
				if (pageindexgifted == 1) {
					initLinear(usecenerLineargifted,
							R.id.usercentergiftquery_gifted,
							initLinearViewgifted());
				} else {
					giftedadapter.notifyDataSetChanged();
				}
				break;
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				codeDialog((String) msg.obj);
				break;
			case 4:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(GiftQueryActivity.this, "领取彩票成功！",
						Toast.LENGTH_LONG).show();
				changeGifedList((String) msg.obj);
				if (gifteddatalist.size() != 0) {
					initLinear(usecenerLineargifted,
							R.id.usercentergiftquery_gifted,
							initLinearViewgifted());
				}
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_giftquery_layout);
		returnButton = (Button) findViewById(R.id.usercenter_giftquery_img_return);
		initReturn();
		jsonobject = this.getIntent().getStringExtra("giftjson");
		encodegiftedjson(jsonobject, giftdatalist, 0);
		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
		}
		mTabHost.setOnTabChangedListener(giftTabChangedListener);
		if (giftdatalist.size() != 0) {
			initLinear(usecenerLinear, R.id.usercentergiftquery_gift,
					initLinearView());
		}
	}

	/**
	 * Tab切换事件
	 */
	TabHost.OnTabChangeListener giftTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			for (int i = 0; i < titles.length; i++) {
				if (tabId.equals(titles[0])) {
					giftType = GIFT;
					if (giftdatalist.size() != 0) {
						initLinear(usecenerLinear,
								R.id.usercentergiftquery_gift, initLinearView());
					}
				} else {
					giftType = GIFTED;
					if (isGifedfirst) {
						getWinDataNet(1, 1);
						break;
					} else {
						if (gifteddatalist.size() != 0) {
							initLinear(usecenerLineargifted,
									R.id.usercentergiftquery_gifted,
									initLinearViewgifted());
						}
					}
				}
			}
		}
	};

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

	protected void initReturn() {
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	/**
	 * 赠彩查询联网
	 * 
	 * @param pageindexgift
	 *            页数
	 * @param type
	 *            类型 0代表赠送 1代表收到
	 */
	private void getWinDataNet(final int pageindexgift, final int type) {
		showDialog(0);
		if (progressbar != null) {
			progressbar.setVisibility(ProgressBar.VISIBLE);
		}
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			public void run() {
				initPojo();
				BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				winQueryPojo.setUserno(userno);
				winQueryPojo.setSessionid(sessionid);
				winQueryPojo.setPhonenum(phonenum);
				winQueryPojo.setPageindex(String.valueOf(pageindexgift));
				winQueryPojo.setMaxresult("10");
				switch (type) {
				case 0:
					winQueryPojo.setType("gift");
					break;
				case 1:
					winQueryPojo.setType("gifted");
					break;
				}
				Message msg = new Message();
				jsonString = GiftQueryInterface.getInstance().giftQuery(
						winQueryPojo);
				tHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (progressbar != null) {
							progressbar.setVisibility(ProgressBar.GONE);
						}
						if (view != null) {
							view.setEnabled(true);
						}
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					if (errcode.equals("0000")) {
						switch (type) {
						case 0:
							msg.what = 1;
							msg.obj = jsonString;
							handler.sendMessage(msg);
							break;
						case 1:
							msg.what = 2;
							msg.obj = jsonString;
							handler.sendMessage(msg);
							break;
						}
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

	private View initLinearViewgifted() {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) viewlist
				.findViewById(R.id.usercenter_listview_queryinfo);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		queryinfolist.addFooterView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.setEnabled(false);
				addMore();

			}
		});

		initListView();
		return viewlist;
	}

	private View initLinearView() {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) viewlist
				.findViewById(R.id.usercenter_listview_queryinfo);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		queryinfolist.addFooterView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				view.setEnabled(false);
				addMore();

			}
		});
		initListView();
		return viewlist;
	}

	ProgressBar progressbar;

	private void initListView() {

		queryinfolist.setAdapter(getadapter());

	}

	private GiftQueryAdapter getadapter() {
		GiftQueryAdapter listadapter = null;
		switch (giftType) {
		case 0:
			giftadapter = new GiftQueryAdapter(this, giftdatalist);
			listadapter = giftadapter;
			break;
		case 1:
			giftedadapter = new GiftQueryAdapter(this, gifteddatalist);
			listadapter = giftedadapter;
			break;
		}
		return listadapter;
	}

	/**
	 * 获取更多联网
	 * 
	 * @return
	 */
	public void addMore() {
		if (giftType == GIFT) {

			pageindex++;
			if (pageindex < allPage + 1) {

				getWinDataNet(pageindex, 0);

			} else {
				queryinfolist.removeFooterView(view);
				pageindex = allPage;
				Toast.makeText(GiftQueryActivity.this,
						R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT)
						.show();

			}
		} else {

			pageindexgifted++;
			if (pageindexgifted < allPagegifted + 1) {

				getWinDataNet(pageindexgifted, 1);

			} else {
				queryinfolist.removeFooterView(view);
				pageindexgifted = allPagegifted;
				Toast.makeText(GiftQueryActivity.this,
						R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	public AlertDialog lookDetailDialog(final GIftQueryInfo info) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 赠送查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.gift_detail, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		final String lotno = (String) info.getLotNo();
		final String lotteryqihao = (String) info.getBatchCode();
		// final String giftedUserNo = (String) info.getUserNo();
		final String orderTime = (String) info.getOrderTime();
		final String amount = (String) info.getAmount();
		final String FormatAmount = PublicMethod.toYuan(amount);
		final String betcode = (String) info.getBetCode();
		// final String state = (String) info.getReciveState();
		final String id = (String) info.getId();
		TextView lotkind = (TextView) view
				.findViewById(R.id.gift_detail_text_lotno);
		TextView batchcode = (TextView) view
				.findViewById(R.id.gift_detail_text_batchcode);
		TextView dingdanno = (TextView) view
				.findViewById(R.id.gift_detail_text_dingdan);
		TextView beishu = (TextView) view
				.findViewById(R.id.gift_detail_text_beishu);
		TextView zhushu = (TextView) view
				.findViewById(R.id.gift_detail_text_zhushu);
		TextView atm = (TextView) view.findViewById(R.id.gift_detail_text_atm);
		TextView statetext = (TextView) view
				.findViewById(R.id.gift_detail_text_state);
		TextView bettime = (TextView) view
				.findViewById(R.id.gift_detail_tex_gifttime);
		TextView content = (TextView) view
				.findViewById(R.id.gift_detail_text_content);
		TextView person = (TextView) view
				.findViewById(R.id.gift_detail_text_person);
		TextView kaijianghao = (TextView) view
				.findViewById(R.id.gift_detail_text_kaijianghao);
		lotkind.append(info.getLotname());
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)) {
			batchcode.setVisibility(View.GONE);
		} else {
			batchcode.append(lotteryqihao);
		}
		if (giftType == GIFT) {
			person.append(info.getToMobileId());
		} else if (giftType == GIFTED) {
			person.setText("赠送人：" + info.getGiftMobileId());
		}
		dingdanno.append(id);
		beishu.append(info.getLotMulti());
		zhushu.append(info.getBetNum());
		atm.append(FormatAmount);
		statetext.append(info.getStateMemo());
		bettime.append(orderTime);
		content.setText(Html.fromHtml("方案内容：<br>" + info.getBetCodeHtml()));
		if (info.getPrizeState().equals("0")) {
			kaijianghao.append("未开奖");
		} else {
			kaijianghao.append(info.getWinCode());
		}
		Button cancleLook = (Button) view
				.findViewById(R.id.gift_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		dialog.show();
		dialog.getWindow().setContentView(view);
		return dialog;
	}

	/**
	 * 初始化列表适配器的List数组
	 * 
	 * @param pageindex
	 * @param giftdatalist
	 * @param giftorgifted
	 *            赠送为0，收到为1
	 * @return
	 */

	public void encodegiftedjson(String json, List<GIftQueryInfo> list, int type) {
		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			if (type == 0) {
				allPage = Integer.parseInt(winprizejsonobj
						.getString("totalPage"));
			} else {
				allPagegifted = Integer.parseInt(winprizejsonobj
						.getString("totalPage"));
			}
			String winprizejsonstring = winprizejsonobj.getString("result");

			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					GIftQueryInfo winPrizeQueryinfo = new GIftQueryInfo();
					winPrizeQueryinfo.setBatchCode(winprizejson
							.getJSONObject(i).getString(BATCHCODE));
					winPrizeQueryinfo.setBetCode(winprizejson.getJSONObject(i)
							.getString(BETCODE));
					winPrizeQueryinfo.setOrderTime(winprizejson
							.getJSONObject(i).getString(ORDERTIME));
					winPrizeQueryinfo.setLotNo(winprizejson.getJSONObject(i)
							.getString(LOTNO));
					winPrizeQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString(AMOUNT));
					winPrizeQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString(LOTMULTI));
					winPrizeQueryinfo.setBetNum(winprizejson.getJSONObject(i)
							.getString("betNum"));
					winPrizeQueryinfo.setId(winprizejson.getJSONObject(i)
							.getString(ID));
					winPrizeQueryinfo.setLotname(winprizejson.getJSONObject(i)
							.getString("lotName"));
					winPrizeQueryinfo.setPrizeState(winprizejson.getJSONObject(
							i).getString("prizeState"));
					winPrizeQueryinfo.setWinCode(winprizejson.getJSONObject(i)
							.getString("winCode"));
					winPrizeQueryinfo.setBetCodeHtml(winprizejson
							.getJSONObject(i).getString("betCodeHtml"));
					if (type == 0) {
						winPrizeQueryinfo.setToMobileId(winprizejson
								.getJSONObject(i).getString("toMobileId"));
						winPrizeQueryinfo.setUserNo(winprizejson.getJSONObject(
								i).getString(GIFTEDUSERNO));
					} else {
						winPrizeQueryinfo.setGiftMobileId(winprizejson
								.getJSONObject(i).getString("giftMobileId"));
						winPrizeQueryinfo.setUserNo(winprizejson.getJSONObject(
								i).getString(GIFTUSERNO));
					}
					winPrizeQueryinfo.setReciveState(winprizejson
							.getJSONObject(i).getString(STATE));
					winPrizeQueryinfo.setStateMemo(winprizejson
							.getJSONObject(i).getString("stateMemo"));
					list.add(winPrizeQueryinfo);
				} catch (Exception e) {

				}
			}

		} catch (JSONException e) {
			try {
				JSONObject winprizejson = new JSONObject(json);
			} catch (JSONException e1) {
			}
		}
	}

	class GIftQueryInfo {
		private String winCode = "";

		public String getWinCode() {
			return winCode;
		}

		public void setWinCode(String winCode) {
			this.winCode = winCode;
		}

		private String giftMobileId = "";
		private String prizeState = "";

		public String getPrizeState() {
			return prizeState;
		}

		public void setPrizeState(String prizeState) {
			this.prizeState = prizeState;
		}

		public String getGiftMobileId() {
			return giftMobileId;
		}

		public void setGiftMobileId(String giftMobileId) {
			this.giftMobileId = giftMobileId;
		}

		private String stateMemo = "";

		public String getStateMemo() {
			return stateMemo;
		}

		public void setStateMemo(String stateMemo) {
			this.stateMemo = stateMemo;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		private String orderId = "";
		private String toMobileId = "";
		private String betNum = "";
		private String lotname = "";

		public String getLotname() {
			return lotname;
		}

		public void setLotname(String lotname) {
			this.lotname = lotname;
		}

		private String amount;

		public String getToMobileId() {
			return toMobileId;
		}

		public void setToMobileId(String toMobileId) {
			this.toMobileId = toMobileId;
		}

		public String getBetNum() {
			return betNum;
		}

		public void setBetNum(String betNum) {
			this.betNum = betNum;
		}

		private String orderTime;
		private String lotNo;
		private String lotMulti;
		private String userNo;
		private String batchCode;
		private String betCode;
		private String reciveState;
		private String id;
		private String betCodeHtml;

		public String getBetCodeHtml() {
			return betCodeHtml;
		}

		public void setBetCodeHtml(String betCodeHtml) {
			this.betCodeHtml = betCodeHtml;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getReciveState() {
			return reciveState;
		}

		public void setReciveState(String reciveState) {
			this.reciveState = reciveState;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getOrderTime() {
			return orderTime;
		}

		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
		}

		public String getLotMulti() {
			return lotMulti;
		}

		public void setLotMulti(String lotMulti) {
			this.lotMulti = lotMulti;
		}

		public String getUserNo() {
			return userNo;
		}

		public void setUserNo(String userNo) {
			this.userNo = userNo;
		}

		public String getBatchCode() {
			return batchCode;
		}

		public void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}

		public String getLotNo() {
			return lotNo;
		}

		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}

		public String getBetCode() {
			return betCode;
		}

		public void setBetCode(String betCode) {
			this.betCode = betCode;
		}
	}

	/**
	 * 中奖查询的适配器
	 */
	public class GiftQueryAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<GIftQueryInfo> mList;

		public GiftQueryAdapter(Context context, List<GIftQueryInfo> list) {
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
			final int isgift = giftType;
			final GIftQueryInfo info = mList.get(position);
			final String lotteryname = (String) mList.get(position).getLotNo();
			String lottname = PublicMethod.toLotno(lotteryname);
			final String lotteryqihao = (String) mList.get(position)
					.getBatchCode();
			final String giftedUserNo = (String) mList.get(position)
					.getUserNo();
			final String orderTime = (String) mList.get(position)
					.getOrderTime();
			final String amount = (String) mList.get(position).getAmount();
			final String FormatAmount = PublicMethod.toYuan(amount);
			final String betcode = (String) mList.get(position).getBetCode();
			final String state = (String) mList.get(position).getReciveState();
			final String id = (String) mList.get(position).getId();
			// if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.usercenter_giftquery_listitem, null);
			holder = new ViewHolder();
			holder.lotteryname = (TextView) convertView
					.findViewById(R.id.usercenter_giftquery_leftone);
			holder.paymoney = (TextView) convertView
					.findViewById(R.id.usercenter_giftquery_amount);
			holder.giftphoneNum = (TextView) convertView
					.findViewById(R.id.usercenter_giftquery_lefttwo);
			holder.giftDate = (TextView) convertView
					.findViewById(R.id.usercenter_giftquery_leftthree);
			holder.reciveText = (TextView) convertView
					.findViewById(R.id.usercenter_giftquery_leftone_recive);
			holder.lookdetail = (Button) convertView
					.findViewById(R.id.usercenter_giftquery_lookdetail);
			holder.reciveState = (Button) convertView
					.findViewById(R.id.usercenter_giftquery_recive_state);
			// } else {
			// holder = (ViewHolder) convertView.getTag();
			// }
			final String userType = getUserType(isgift);
			holder.giftphoneNum.setText(userType + giftedUserNo);
			int length = 6;
			if (lottname.length() > length) {
				String str1 = lottname.substring(0, length);
				String str2 = lottname.substring(length, lottname.length());
				lottname = str1 + "\n" + str2;
			}
			final String formatLottname = lottname;
			holder.lotteryname.setText(formatLottname);
			holder.paymoney.setText(getString(R.string.usercenter_giftAmount)
					+ FormatAmount + getString(R.string.unit));
			holder.giftDate.setText(getString(R.string.time) + orderTime);
			holder.lookdetail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					lookDetailDialog(info);
				}
			});
			if (isgift == 0) {// 0为赠送，1为收到
				holder.reciveState.setVisibility(Button.GONE);
				if (state.equals("0")) {// 0：未领取，1：已领取
					holder.reciveText.setText("(未领取)");
					holder.reciveText.setTextColor(Color.RED);
				} else if (state.equals("1")) {
					holder.reciveText.setText("(已领取)");
					holder.reciveText.setTextColor(Color.BLUE);
				}
			} else {
				holder.reciveText.setVisibility(TextView.GONE);
				if (state.equals("0")) {// 0：未领取，1：已领取
					holder.reciveState
							.setOnClickListener(new OnClickListener() {
								public void onClick(View v) {
									getCodeNet(id);
								}
							});
				} else if (state.equals("1")) {
					holder.reciveState
							.setBackgroundResource(R.drawable.user_gift_get_after);
				}
			}

			convertView.setTag(holder);
			return convertView;
		}

		class ViewHolder {
			TextView lotteryname;// 彩种名
			TextView giftphoneNum;// 被赠送人姓名
			TextView paymoney;// 付款金额
			TextView giftDate;// 赠送日期
			TextView reciveText;// 是否领取彩票
			Button lookdetail;// 查看详情按钮
			Button reciveState;// 是否领取彩票按钮
		}

		private String getUserType(int isgift) {
			String userType = "";
			switch (isgift) {
			case 1:
				return userType = getString(R.string.usercenter_giftPoneNum);
			default:
				return userType = getString(R.string.usercenter_giftedPoneNum);
			}
		}
	}

	/**
	 * 领取彩票获取验证码联网
	 */
	public void getCodeNet(final String id) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String returnStr = GiftReciveCodeInterface.getInstance()
						.giftCodeQuery(id, userno);
				dialog.dismiss();
				try {
					JSONObject json = new JSONObject(returnStr);
					String errcode = json.getString("error_code");
					String message = json.getString("message");
					Message msg = new Message();
					if (errcode.equals("0000")) {
						msg.what = 3;
						msg.obj = id;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 领取彩票码联网
	 */
	public void getReciveNet(final String id, final String code) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String returnStr = GiftReciveInterface.getInstance()
						.giftReciveQuery(id, code);
				dialog.dismiss();
				try {
					JSONObject json = new JSONObject(returnStr);
					String errcode = json.getString("error_code");
					String message = json.getString("message");
					Message msg = new Message();
					if (errcode.equals("0000")) {
						msg.what = 4;
						msg.obj = id;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 验证码对话框
	 * 
	 * @param id
	 */
	public void codeDialog(final String id) {
		final Dialog giftCode = new Dialog(this, R.style.dialog);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.usercenter_bindphone, null);
		TextView title = (TextView) v
				.findViewById(R.id.usercenter_bindphonetitle);
		TextView label = (TextView) v
				.findViewById(R.id.usercenter_bindphonelabel);
		final TextView context = (EditText) v
				.findViewById(R.id.usercenter_edittext_bindphoneContext);
		Button submit = (Button) v.findViewById(R.id.usercenter_bindphone_ok);
		Button cancle = (Button) v.findViewById(R.id.usercenter_bindphone_back);
		title.setText(getString(R.string.gift_dialog_title));
		label.setText(getString(R.string.usercenter_securitycode));
		label.setTextSize(20);
		submit.setText(getString(R.string.get_money_submit));
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				giftCode.cancel();
			}
		});
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String contextStr = context.getText().toString();
				giftCode.cancel();
				getReciveNet(id, contextStr);
			}
		});
		giftCode.setContentView(v);
		giftCode.show();
	}

	/**
	 * 领取彩票成功，根据彩票id改变彩票状态
	 */
	public void changeGifedList(String id) {
		for (int i = 0; i < gifteddatalist.size(); i++) {
			GIftQueryInfo winPrizeQueryinfo = gifteddatalist.get(i);
			if (winPrizeQueryinfo.getId().equals(id)) {
				winPrizeQueryinfo.setReciveState("1");
				return;
			}
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
