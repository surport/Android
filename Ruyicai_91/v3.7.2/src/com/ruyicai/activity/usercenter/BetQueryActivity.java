/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.usercenter.detail.Betdetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.BetDetailsInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 投注查询联网
 * 
 * @author Administrator
 * 
 */
public class BetQueryActivity extends Activity implements HandlerMsg {
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private LinearLayout usecenerLinear;
	private Button returnButton;
	private Button kindButton;
	private TextView titleTextView;
	private LinearLayout kind;// 按彩种查询
	private Spinner betkindspinner;
	private String lotno = "";
	private boolean isbetkindall = false;// 下拉框选择查询全部彩种，默认为fasle,点击一次变为true;
	private String[] allcountries = { "全部彩种", "双色球", "福彩3D", "江西11选5",
			"广东11选5", "快三", "大乐透", "时时彩", "七乐彩", "排列三", "竞彩足球", "排列五", "七星彩",
			"11运夺金", "竞彩篮球", "22选5", "足彩", "广东快乐十分" };
	private int allpages = 0, ssqpages = 0, dpages = 0, xuanpages = 0,
			dltpages = 0, sscpages = 0, qlcpages = 0, plspages = 0,
			jczqpages = 0, plwpages = 0, qxcpages = 0, djpages = 0,
			jclqpages = 0, twentypages = 0, sfcpages = 0, rxjpages = 0,
			lcbpages = 0, jqcpages = 0, gdpages = 0, tenpages = 0;
	private int allindex = 0, ssqindex = 0, dindex = 0, xuanindex = 0,
			dltindex = 0, sscindex = 0, qlcindex = 0, plsindex = 0,
			jczqindex = 0, plwindex = 0, qxcindex = 0, djindex = 0,
			jclqindex = 0, twentyindex = 0, sfcindex = 0, rxjindex = 0,
			lcbindex = 0, jqcindxe = 0, gdindex = 0, tenindex = 0;
	String jsonString;
	final String BATCHCODE = "batchCode", LOTMUTI = "lotMulti",
			ORDERTIME = "orderTime", PRIZEAMT = "prizeAmt",
			prizeState = "prizeState", WINCODE = "winCode",
			BETCODE = "betCode", LOTNO = "lotNo", AMOUNT = "amount",
			LOTNAME = "lotName", PLAY = "play", BET_CODE = "orderInfo";
	final String ISREPEATBUY = "isRepeatBuy";
	MyHandler touzhuhandler = new MyHandler(this);
	private final int DIALOG1_KEY = 0;
	ListView queryinfolist;
	private String phonenum, sessionid, userno;
	List<BetQueryInfo> betdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> ssqdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> ddatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> qlcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> xuandatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> dltdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> sscdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> gddatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> nmk3list = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> plsdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> plwdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> qxcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> djdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jclqdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> twentydatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> sfcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> rxjdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> lcbdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> jqcdatalist = new ArrayList<BetQueryInfo>();
	List<BetQueryInfo> tendatalist = new ArrayList<BetQueryInfo>();

	private int typekind = 0;
	Context context = this;
	ProgressDialog dialog;
	String jsonobject = null;
	BetAdapter adapter;
	View view;
	ProgressBar progressbar;
	boolean isfirst = false;

	private final int MAX_AMT = 10000000;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				encodejson((String) msg.obj);
				if (getNewPage() == 0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					selecttypelist(typekind);
				} else {
					adapter.notifyDataSetChanged();
				}

				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				selecttypelist(typekind);
				break;
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				detailsErrorCode(detailJson((BetQueryInfo) msg.obj));

				break;
			}
		}
	};

	private void setNewPage(int page) {
		switch (typekind) {
		case 0:
			allindex = page;
			break;
		case 1:
			ssqindex = page;
			break;
		case 2:
			dindex = page;
			break;
		case 3:
			xuanindex = page;
			break;
		case 4:
			gdindex = page;
			break;
		case 5:
			tenindex = page;
			break;
		case 6:
			dltindex = page;
			break;
		case 7:
			sscindex = page;
			break;
		case 8:
			qlcindex = page;
			break;
		case 9:
			plsindex = page;
			break;
		case 10:
			jczqindex = page;
			break;
		case 11:
			plwindex = page;
			break;
		case 12:
			qxcindex = page;
			break;
		case 13:
			djindex = page;
			break;
		case 14:
			jclqindex = page;
			break;
		case 15:
			twentyindex = page;
			break;
		case 16:
			sfcindex = page;
			break;
		case 17:
			rxjindex = page;
			break;
		case 18:
			lcbindex = page;
			break;
		case 19:
			jqcindxe = page;
			break;
		}
	}

	private int getNewPage() {
		int page = 0;
		switch (typekind) {
		case 0:
			page = allindex;
			break;
		case 1:
			page = ssqindex;
			break;
		case 2:
			page = dindex;
			break;
		case 3:
			page = xuanindex;
			break;
		case 4:
			page = gdindex;
			break;
		case 5:
			page = tenindex;
			break;
		case 6:
			page = dltindex;
			break;
		case 7:
			page = sscindex;
			break;
		case 8:
			page = qlcindex;
			break;
		case 9:
			page = plsindex;
			break;
		case 10:
			page = jczqindex;
			break;
		case 11:
			page = plwindex;
			break;
		case 12:
			page = qxcindex;
			break;
		case 13:
			page = djindex;
			break;
		case 14:
			page = jclqindex;
			break;
		case 15:
			page = twentyindex;
			break;
		case 16:
			page = sfcindex;
			break;
		case 17:
			page = rxjindex;
			break;
		case 18:
			page = lcbindex;
			break;
		case 19:
			page = jqcindxe;
			break;
		}
		return page;
	}

	private void setAllPage(int page) {

		switch (typekind) {
		case 0:
			allpages = page;
			break;
		case 1:
			ssqpages = page;
			break;
		case 2:
			dpages = page;
			break;
		case 3:
			xuanpages = page;
			break;
		case 4:
			gdpages = page;
			break;
		case 5:
			tenpages = page;
			break;
		case 6:
			dltpages = page;
			break;
		case 7:
			sscpages = page;
			break;
		case 8:
			qlcpages = page;
			break;
		case 9:
			plspages = page;
			break;
		case 10:
			jczqpages = page;
			break;
		case 11:
			plwpages = page;
			break;
		case 12:
			qxcpages = page;
			break;
		case 13:
			djpages = page;
			break;
		case 14:
			jclqpages = page;
			break;
		case 15:
			twentypages = page;
			break;
		case 16:
			sfcpages = page;
			break;
		case 17:
			rxjpages = page;
			break;
		case 18:
			lcbpages = page;
			break;
		case 19:
			jqcpages = page;
			break;
		}
	}

	private int getAllPage() {
		int page = 0;
		switch (typekind) {
		case 0:
			page = allpages;
			break;
		case 1:
			page = ssqpages;
			break;
		case 2:
			page = dpages;
			break;
		case 3:
			page = xuanpages;
			break;
		case 4:
			page = gdpages;
			break;
		case 5:
			page = tenpages;
			break;
		case 6:
			page = dltpages;
			break;
		case 7:
			page = sscpages;
			break;
		case 8:
			page = qlcpages;
			break;
		case 9:
			page = plspages;
			break;
		case 10:
			page = jczqpages;
			break;
		case 11:
			page = plwpages;
			break;
		case 12:
			page = qxcpages;
			break;
		case 13:
			page = djpages;
			break;
		case 14:
			page = jclqpages;
			break;
		case 15:
			page = twentypages;
			break;
		case 16:
			page = sfcpages;
			break;
		case 17:
			page = rxjpages;
			break;
		case 18:
			page = lcbpages;
			break;
		case 19:
			page = jqcpages;
			break;
		}
		return page;
	}

	public void selecttypelist(int type) {
		switch (type) {
		case 0:
			initListView(queryinfolist, betdatalist);
			break;
		case 1:
			initListView(queryinfolist, ssqdatalist);
			break;
		case 2:
			initListView(queryinfolist, ddatalist);
			break;
		case 3:
			initListView(queryinfolist, xuandatalist);
			break;
		case 4:
			initListView(queryinfolist, gddatalist);
			break;
		case 5:
			initListView(queryinfolist, nmk3list);
			break;
		case 6:
			initListView(queryinfolist, dltdatalist);
			break;
		case 7:
			initListView(queryinfolist, sscdatalist);
			break;
		case 8:
			initListView(queryinfolist, qlcdatalist);
			break;
		case 9:
			initListView(queryinfolist, plsdatalist);
			break;
		case 10:
			initListView(queryinfolist, jcdatalist);
			break;
		case 11:
			initListView(queryinfolist, plwdatalist);
			break;
		case 12:
			initListView(queryinfolist, qxcdatalist);
			break;
		case 13:
			initListView(queryinfolist, djdatalist);
			break;
		case 14:
			initListView(queryinfolist, jclqdatalist);
			break;
		case 15:
			initListView(queryinfolist, twentydatalist);
			break;
		case 16:
			initListView(queryinfolist, sfcdatalist);
			break;
		case 17:
			initListView(queryinfolist, rxjdatalist);
			break;
		case 18:
			initListView(queryinfolist, lcbdatalist);
			break;
		case 19:
			initListView(queryinfolist, jqcdatalist);
			break;
		case 20:
			initListView(queryinfolist, tendatalist);
			break;

		}

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayoutold);
		returnButton = (Button) findViewById(R.id.layout_usercenter_img_return);
		titleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		titleTextView.setText(R.string.usercenter_bettingDetails);
		returnButton.setText(R.string.returnlastpage);
		kind = (LinearLayout) findViewById(R.id.betkind);
		kind.setVisibility(View.VISIBLE);
		betkindspinner = (Spinner) findViewById(R.id.bet_kind_spinner);
		initLinear();
		initspinner();
		initReturn();
		getInfo();
		isfirst = true;
	}

	public void getInfo() {
		jsonobject = this.getIntent().getStringExtra("betjson");
		if (jsonobject == null || jsonobject.equals("")) {
			setSpinnerIndex();
		} else {
			encodejson(jsonobject);
		}
	}

	private void setSpinnerIndex() {
		Intent intent = getIntent();
		String type = intent.getStringExtra("lotno");
		if (type.equals(Constants.LOTNO_SSQ)) {
			betkindspinner.setSelection(1);
		} else if (type.equals(Constants.LOTNO_FC3D)) {
			betkindspinner.setSelection(2);
		} else if (type.equals(Constants.LOTNO_11_5)) {
			betkindspinner.setSelection(3);
		} else if (type.equals(Constants.LOTNO_GD_11_5)) {
			betkindspinner.setSelection(4);
		} else if (type.equals(Constants.LOTNO_NMK3)) {
			betkindspinner.setSelection(5);
		} else if (type.equals(Constants.LOTNO_DLT)) {
			betkindspinner.setSelection(6);
		} else if (type.equals(Constants.LOTNO_SSC)) {
			betkindspinner.setSelection(7);
		} else if (type.equals(Constants.LOTNO_QLC)) {
			betkindspinner.setSelection(8);
		} else if (type.equals(Constants.LOTNO_PL3)) {
			betkindspinner.setSelection(9);
		} else if (type.equals(Constants.LOTNO_JCZ)) {
			betkindspinner.setSelection(10);
		} else if (type.equals(Constants.LOTNO_PL5)) {
			betkindspinner.setSelection(11);
		} else if (type.equals(Constants.LOTNO_QXC)) {
			betkindspinner.setSelection(12);
		} else if (type.equals(Constants.LOTNO_eleven)) {
			betkindspinner.setSelection(13);
		} else if (type.equals(Constants.LOTNO_JCL)) {
			betkindspinner.setSelection(14);
		} else if (type.equals(Constants.LOTNO_22_5)) {
			betkindspinner.setSelection(15);
		} else if (type.equals(Constants.LOTNO_SFC)) {
			betkindspinner.setSelection(16);
		} else if (type.equals(Constants.LOTNO_RX9)) {
			betkindspinner.setSelection(17);
		} else if (type.equals(Constants.LOTNO_LCB)) {
			betkindspinner.setSelection(18);
		} else if (type.equals(Constants.LOTNO_JQC)) {
			betkindspinner.setSelection(19);
		} else if (type.equals(Constants.LOTNO_ten)) {
			betkindspinner.setSelection(20);
		}
	}

	// 按彩种查询投注记录
	private void initspinner() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		betkindspinner.setAdapter(adapter);
		betkindspinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = betkindspinner.getSelectedItemPosition();
				typekind = position;
				if (position == 0) {
					lotno = "";
					if (isbetkindall) {
						if (view != null) {
						}
						selecttypelist(position);
					}
				}
				if (position == 1) {
					isbetkindall = true;
					lotno = Constants.LOTNO_SSQ;
					if (ssqdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 2) {
					isbetkindall = true;
					lotno = Constants.LOTNO_FC3D;
					if (ddatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 3) {
					isbetkindall = true;
					lotno = Constants.LOTNO_11_5;
					if (xuandatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 4) {
					isbetkindall = true;
					lotno = Constants.LOTNO_GD115;
					if (gddatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 5) {
					isbetkindall = true;
					lotno = Constants.LOTNO_NMK3;
					if (tendatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 6) {
					isbetkindall = true;
					lotno = Constants.LOTNO_DLT;
					if (dltdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 7) {
					isbetkindall = true;
					lotno = Constants.LOTNO_SSC;
					if (sscdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 8) {
					isbetkindall = true;
					lotno = Constants.LOTNO_QLC;
					if (qlcdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 9) {
					isbetkindall = true;
					lotno = Constants.LOTNO_PL3;
					if (plsdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 10) {
					isbetkindall = true;
					lotno = "JC_Z";
					if (jcdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 11) {
					isbetkindall = true;
					lotno = Constants.LOTNO_PL5;
					if (plwdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 12) {
					isbetkindall = true;
					lotno = Constants.LOTNO_QXC;
					if (qxcdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 13) {
					isbetkindall = true;
					lotno = Constants.LOTNO_eleven;
					if (djdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 14) {
					isbetkindall = true;
					lotno = "JC_L";
					if (jclqdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 15) {
					isbetkindall = true;
					lotno = Constants.LOTNO_22_5;
					if (jclqdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 16) {
					isbetkindall = true;
					lotno = Constants.LOTNO_ZC;
					if (sfcdatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				} else if (position == 17) {
					isbetkindall = true;
					lotno = Constants.LOTNO_ten;
					if (tendatalist.size() > 0) {
						selecttypelist(position);
					} else {
						getWinDataNet(0);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

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

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	/**
	 * 获取投注查询联网
	 * 
	 * @param pageindex
	 */
	Handler thandler = new Handler();

	private void netting(final int pageindex) {
		initPojo();
		progressbar.setVisibility(ProgressBar.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BetAndWinAndTrackAndGiftQueryPojo betQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				betQueryPojo.setUserno(userno);
				betQueryPojo.setPageindex(String.valueOf(pageindex));
				betQueryPojo.setLotno(lotno);
				betQueryPojo.setMaxresult("10");
				betQueryPojo.setType("betList");

				Message msg = handler.obtainMessage();
				jsonString = BetQueryInterface.getInstance().betQuery(
						betQueryPojo);
				;
				thandler.post(new Runnable() {

					@Override
					public void run() {
						progressbar.setVisibility(view.INVISIBLE);
						view.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					if (errcode.equals("0000")) {
						msg.what = 1;
						msg.obj = jsonString;
						setNewPage(pageindex);
						handler.sendMessage(msg);

					}
					if (errcode.equals("0047")) {
						msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void getWinDataNet(final int pageindex) {
		showDialog(0);
		netting(pageindex);

	}

	private void initLinear() {
		usecenerLinear = (LinearLayout) findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());

	}

	private View initLinearView() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) viewlist
				.findViewById(R.id.usercenter_listview_queryinfo);
		LayoutInflater mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		queryinfolist.addFooterView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				view.setEnabled(false);
				addmore();

			}
		});
		initListView(queryinfolist, betdatalist);
		return viewlist;
	}

	private void addmore() {
		int pageIndex = getNewPage();
		int allpagenum = getAllPage();
		isfirst = false;
		pageIndex++;
		if (pageIndex < allpagenum) {
			netting(pageIndex);
		} else {
			progressbar.setVisibility(view.INVISIBLE);
			pageIndex = allpagenum - 1;
			view.setEnabled(true);
			Toast.makeText(BetQueryActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		}

	}

	private void initListView(ListView listview, List list) {
		adapter = new BetAdapter(context, list);
		// 数据源
		listview.setAdapter(adapter);

	}

	public void encodejson(String json) {

		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			int allPage = Integer.parseInt(winprizejsonobj
					.getString("totalPage"));
			String winprizejsonstring = winprizejsonobj.getString("result");
			setAllPage(allPage);
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					BetQueryInfo betQueryinfo = new BetQueryInfo();
					betQueryinfo.setBatchCode(winprizejson.getJSONObject(i)
							.getString(BATCHCODE));
					betQueryinfo.setOrdertime(winprizejson.getJSONObject(i)
							.getString(ORDERTIME));
					betQueryinfo.setLotNo(winprizejson.getJSONObject(i)
							.getString(LOTNO));
					betQueryinfo.setPrizeAmt(winprizejson.getJSONObject(i)
							.getString(PRIZEAMT));
					betQueryinfo.setLotName(winprizejson.getJSONObject(i)
							.getString(LOTNAME));
					betQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString(AMOUNT));
					betQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString(LOTMUTI));
					betQueryinfo.setBet_code(winprizejson.getJSONObject(i)
							.getString(BET_CODE));
					betQueryinfo.setPrizeState(winprizejson.getJSONObject(i)
							.getString(prizeState));
					betQueryinfo.setWin_code(winprizejson.getJSONObject(i)
							.getString(WINCODE));
					betQueryinfo.setRepeatBuy(winprizejson.getJSONObject(i)
							.getBoolean(ISREPEATBUY));
					betQueryinfo.setOrderId(winprizejson.getJSONObject(i)
							.getString("orderId"));
					betQueryinfo.setStateMemo(winprizejson.getJSONObject(i)
							.getString("stateMemo"));
					betQueryinfo.setBetNum(winprizejson.getJSONObject(i)
							.getString("betNum"));
					betQueryinfo.setOneAmount(winprizejson.getJSONObject(i)
							.getString("oneAmount"));
					if (typekind == 0) {
						betdatalist.add(betQueryinfo);
					} else if (typekind == 1) {
						ssqdatalist.add(betQueryinfo);
					} else if (typekind == 2) {
						ddatalist.add(betQueryinfo);
					} else if (typekind == 3) {
						xuandatalist.add(betQueryinfo);
					} else if (typekind == 4) {
						gddatalist.add(betQueryinfo);
					} else if (typekind == 5) {
						nmk3list.add(betQueryinfo);
					} else if (typekind == 6) {
						dltdatalist.add(betQueryinfo);
					} else if (typekind == 7) {
						sscdatalist.add(betQueryinfo);
					} else if (typekind == 8) {
						qlcdatalist.add(betQueryinfo);
					} else if (typekind == 9) {
						plsdatalist.add(betQueryinfo);
					} else if (typekind == 10) {
						jcdatalist.add(betQueryinfo);
					} else if (typekind == 11) {
						plwdatalist.add(betQueryinfo);
					} else if (typekind == 12) {
						qxcdatalist.add(betQueryinfo);
					} else if (typekind == 13) {
						djdatalist.add(betQueryinfo);
					} else if (typekind == 14) {
						jclqdatalist.add(betQueryinfo);
					} else if (typekind == 15) {
						twentydatalist.add(betQueryinfo);
					} else if (typekind == 16) {
						sfcdatalist.add(betQueryinfo);
					} else if (typekind == 17) {
						rxjdatalist.add(betQueryinfo);
					} else if (typekind == 18) {
						lcbdatalist.add(betQueryinfo);
					} else if (typekind == 19) {
						jqcdatalist.add(betQueryinfo);
					} else if (typekind == 20) {
						tendatalist.add(betQueryinfo);
					}

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

	/**
	 * 投注查询的适配器
	 */
	public class BetAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<BetQueryInfo> mList;

		public BetAdapter(Context context, List<BetQueryInfo> list) {
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
			int isJC = 0;
			// 如果是竞彩则彩种的期号是不能显示的，在这里要加上所有的竞彩彩种编号
			final BetQueryInfo info = mList.get(position);
			final boolean isRepeatBuy = (Boolean) mList.get(position)
					.isRepeatBuy();
			final String lotno = (String) mList.get(position).getLotNo();
			final String prizeqihao = (String) mList.get(position)
					.getBatchCode();
			final String amount = (String) mList.get(position).getAmount();
			final String fPayMoney = PublicMethod.formatMoney(amount);
			final String lotName = (String) mList.get(position).getLotName();
			final String prizemoney = (String) mList.get(position)
					.getPrizeAmt();
			final String formatPrizeName = PublicMethod.formatMoney(prizemoney);
			final String prize_State = (String) mList.get(position)
					.getPrizeState();
			if (lotno.equals("J00001") || lotno.equals("J00002")
					|| lotno.equals("J00003") || lotno.equals("J00004")
					|| lotno.equals(Constants.LOTNO_JCLQ)
					|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
					|| lotno.equals(Constants.LOTNO_JCLQ_RF)
					|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
					|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
					|| lotno.equals(Constants.LOTNO_JCLQ_HUN)) {
				isJC = 1;
			} else {
				isJC = 0;
			}
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.usercenter_listitem_winprize_query, null);
				holder = new ViewHolder();
				holder.lotteryname = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_lotteryname);
				holder.prizeqihao = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizeqihao);
				holder.paymoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_paymoney);
				holder.prizemoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizemoney);
				holder.buyagain = (Button) convertView
						.findViewById(R.id.usercenter_winprize_buyagain);
				holder.lookdetail = (Button) convertView
						.findViewById(R.id.usercenter_winprize_querydetail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.prizeqihao.setText("(期号:" + prizeqihao + ")");
			noBuyAgain(holder.buyagain, holder.prizeqihao, isRepeatBuy, isJC);
			String payString = getString(R.string.usercenter_winprize_payMoney);// 投注金额字

			String prizeString = getString(R.string.usercenter_prizeMoney);// 中奖金额字
			holder.lotteryname.setText(lotName);

			holder.paymoney.setText(payString + fPayMoney);
			if (prize_State.equals("0")) {
				holder.prizemoney.setTextColor(Color.GRAY);
				holder.prizemoney.setText("未开奖");
			} else if (prize_State.equals("3")) {
				holder.prizemoney.setTextColor(Color.GRAY);
				holder.prizemoney.setText("未中奖");
			} else {
				String prizeStr = prizeString + formatPrizeName;
				PublicMethod.setTextColor(holder.prizemoney, prizeStr, 5,
						prizeStr.length(), Color.RED);
			}
			holder.buyagain.setVisibility(View.GONE);

			holder.lookdetail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					betQueryDetails(info);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView lotteryname;
			TextView prizeqihao;
			TextView paymoney;
			TextView prizemoney;
			Button lookdetail;
			Button buyagain;
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

	private void noBuyAgain(Button a, TextView qihao, boolean isRepeatBuy,
			int isJC) {
		if (isRepeatBuy == false) {
			if (isJC == 1) {
				qihao.setVisibility(TextView.GONE);
			} else {
				qihao.setVisibility(TextView.VISIBLE);
			}
			a.setEnabled(false);
			a.setBackgroundResource(R.drawable.buyagainunenable);
		} else {
			qihao.setVisibility(TextView.VISIBLE);
			a.setBackgroundResource(R.drawable.usercenter_selector_buyagain);
			a.setEnabled(true);
		}
	}

	/**
	 * 投注详情
	 */
	public void betQueryDetails(final BetQueryInfo betQueryinfo) {
		showDialog(DIALOG1_KEY);
		Thread t = new Thread(new Runnable() {
			String str = "00";

			public void run() {
				str = BetDetailsInterface.getInstance().betDetails(
						betQueryinfo.getOrderId());
				try {
					JSONObject aa = new JSONObject(str);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					Message msg = handler.obtainMessage();
					if (errcode.equals("0000")) {
						betQueryinfo.setJson(str);
						msg.what = 3;
						msg.obj = betQueryinfo;
						handler.sendMessage(msg);

					}
					if (errcode.equals("0047")) {
						msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 解析详情json
	 * 
	 * @param betQueryinfo
	 * @return
	 */
	public BetQueryInfo detailJson(BetQueryInfo betQueryinfo) {

		try {
			JSONObject winprizejsonobj = new JSONObject(betQueryinfo.getJson());
			JSONObject winprizejsonstring = winprizejsonobj
					.getJSONObject("result");
			betQueryinfo.setBetCodeHtml(winprizejsonstring
					.getString("betCodeHtml"));
			betQueryinfo.setJson(winprizejsonstring.getString("betCodeJson"));

		} catch (JSONException e) {

		}
		return betQueryinfo;
	}

	/**
	 * 投注查询详情跳转
	 * 
	 * @param betQueryinfo
	 */
	public void detailsErrorCode(BetQueryInfo betQueryinfo) {
		Intent intent = new Intent(BetQueryActivity.this, Betdetail.class);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betQueryinfo);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		intent.putExtra("info", byteStream.toByteArray());
		BetQueryActivity.this.startActivity(intent);
	}

	public void errorCode_0000() {
	}

	public void errorCode_000000() {
	}

	public Context getContext() {
		return this;
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
