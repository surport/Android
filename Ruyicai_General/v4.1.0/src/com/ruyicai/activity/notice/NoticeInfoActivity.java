package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.notice.LotnoDetail.DLTDetailView;
import com.ruyicai.activity.notice.LotnoDetail.FC3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.LotnoDetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL3DetailView;
import com.ruyicai.activity.notice.LotnoDetail.PL5DetailView;
import com.ruyicai.activity.notice.LotnoDetail.QLCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.QXCDetailView;
import com.ruyicai.activity.notice.LotnoDetail.SsqDetailView;
import com.ruyicai.activity.notice.LotnoDetail.TwentyDetailView;
import com.ruyicai.activity.notice.LotnoDetail.ZCDetailView;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import com.third.tencent.TencentShareActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖号码页面
 */
public class NoticeInfoActivity extends Activity {
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	private Handler handler = new Handler();
	public ProgressDialog progress;
	String Lotno, LotLalel;
	int lotType = -1;
	ProgressBar progressbar;// 列表获取更多的progressbar
	LayoutInflater mInflater;
	private final int LISTSSQ = 0, LIST3D = 1, LISTQLC = 2, LISTPL3 = 3,
			LISTDLT = 4, LISTSSC = 5, LIST115 = 6, LISTSFC = 7, LISTRX9 = 8,
			LISTLCB = 9, LISTJQC = 10, LISTPL5 = 11, LISTQXC = 12,
			LISTYDJ = 13, LISTTWENTY = 14, LISTTEN = 15, ZC = 16, NMK3 = 19,CQ11X5=20;
	List<Map<String, Object>> adpterlist = new ArrayList<Map<String, Object>>();; // zlm
																					// 8.9
																					// 添加排列三、超级大乐透
	TextView noticePrizesTitle;
	BaseAdapter adapter;
	View addMoreView;
	Button reBtn;
	ListView listview;
	String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
			FINALDATE, MONEYSUM };
	int pageindex = 1;
	int totalItem = 24;
	private int lotnoZC = -1;
	String tencent_token;
	String tencent_access_token_secret;
	private OAuthV1 tenoAuth;
	RWSharedPreferences shellRW;
	private static int[] aRedColorResId = { R.drawable.red };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progress = UserCenterDialog.onCreateDialog(this);
		setContentView(R.layout.notice_prizes_single_specific_main);
		shellRW = new RWSharedPreferences(this, "addInfo");
		getInfo();
		noticeAllNet();
		MobclickAgent.onEvent(this, "kaijianghaoma"); // BY贺思明 最新开奖页点击“开奖号码”切换。
	}

	public void getInfo() {
		Intent intent = getIntent();
		if (intent != null) {
			lotnoZC = intent.getIntExtra("lotnoZC", -1);
		}
		if (lotnoZC == -1) {
			lotnoZC = NoticeActivityGroup.LOTNO;
		}
	}

	/**
	 * 联网获取数据
	 */
	public void noticeAllNet() {
		progress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				getLotno(lotnoZC);
				JSONObject prizemore = getJSONByLotno(Lotno, "50");
				if (prizemore != null) {
					try {
						final String msg = prizemore.getString("message");
						final String code = prizemore.getString("error_code");
						progress.dismiss();
						if (code.equals("0000")) {
							JsonToString(prizemore);
							handler.post(new Runnable() {
								@Override
								public void run() {
									initList();
								}
							});
						} else {
							handler.post(new Runnable() {
								@Override
								public void run() {
									initList();
									Toast.makeText(NoticeInfoActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
							});
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}
			}

		}).start();
	}

	private static JSONObject getJSONByLotno(String lotnoString,
			String maxresultString) {
		JSONObject jsonObjectByLotno = PrizeInfoInterface.getInstance()
				.getNoticePrizeInfo(lotnoString, "1", maxresultString);
		return jsonObjectByLotno;
	}

	/**
	 * 初始化列表
	 */
	private void initList() {
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);
		RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rl.setMargins(0, 0, 0,0);
		listview.setLayoutParams(rl);
		
		mInflater = LayoutInflater.from(this);
		addMoreView = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) addMoreView
				.findViewById(R.id.getmore_progressbar);
		listview.addFooterView(addMoreView);

		noticePrizesTitle.setTextSize(20);
		reBtn = (Button) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setBackgroundResource(R.drawable.returnselecter);
		adapter = getAdapter(Lotno);
		listview.setAdapter(adapter);
		addMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addMoreView.setEnabled(false);
				getMore();
			}
		});
	}

	public BaseAdapter getAdapter(String lotno) {
		if (lotno.equals(Constants.LOTNO_SSC)
				|| lotno.equals(Constants.LOTNO_11_5)
				|| lotno.equals(Constants.LOTNO_eleven)
				|| lotno.equals(Constants.LOTNO_GD115)
				|| lotno.equals(Constants.LOTNO_ten)
				|| lotno.equals(Constants.LOTNO_NMK3)
				|| lotno.equals(Constants.LOTNO_CQ_ELVEN_FIVE)) {
			// 如果是高频彩，返回高频率彩的适配器
			return new HightSubEfficientAdapter(this, str, adpterlist);
		} else {
			return new SubEfficientAdapter(this, str, adpterlist, true);
		}
	}

	/**
	 * 获取彩种类型
	 */
	private void getLotno(int listViewID) {
		switch (listViewID) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			Lotno = Constants.LOTNO_SSQ;
			lotType = LISTSSQ;// 双色球
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			Lotno = Constants.LOTNO_FC3D;
			lotType = LIST3D;// 福彩3D
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			Lotno = Constants.LOTNO_QLC;
			lotType = LISTQLC;// 七乐彩
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			Lotno = Constants.LOTNO_PL3;
			lotType = LISTPL3;// 排列3
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			Lotno = Constants.LOTNO_PL5;
			lotType = LISTPL5;// 排列五
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			Lotno = Constants.LOTNO_QXC;
			lotType = LISTQXC;// 七星彩
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			Lotno = Constants.LOTNO_DLT;
			lotType = LISTDLT;// 大乐透
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			Lotno = Constants.LOTNO_SSC;
			lotType = LISTSSC;// 时时彩
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			Lotno = Constants.LOTNO_11_5;
			lotType = LIST115;// 江西11-5
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			Lotno = Constants.LOTNO_eleven;
			lotType = LISTYDJ;// 11运夺金
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			Lotno = Constants.LOTNO_22_5;
			lotType = LISTTWENTY;// 22选5
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			Lotno = Constants.LOTNO_SFC;// 胜负彩
			lotType = ZC;
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			Lotno = Constants.LOTNO_RX9;// 任选9
			lotType = ZC;
			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			Lotno = Constants.LOTNO_LCB;// 六场半
			lotType = ZC;
			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			Lotno = Constants.LOTNO_JQC;// 进球彩
			lotType = ZC;
			break;
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			Lotno = Constants.LOTNO_GD115;// 广东11-5
			lotType = -1;
			break;
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			Lotno = Constants.LOTNO_ten;
			lotType = LISTTEN;// 22选5
			break;
		case NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW:
			Lotno = Constants.LOTNO_NMK3;
			lotType = NMK3;// 内蒙快三
			break;
		case NoticeActivityGroup.ID_SUB_CQ11X5_LISTVIEW:
			Lotno = Constants.LOTNO_CQ_ELVEN_FIVE;
			lotType = CQ11X5;// 内蒙快三
			break;
		}
	}

	/**
	 * 子列表适配器
	 */
	public class HightSubEfficientAdapter extends BaseAdapter {
		int count = 0;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Activity context;

		public HightSubEfficientAdapter(Activity context, String[] index,
				List<Map<String, Object>> list) {
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			this.mList = list;
			this.mIndex = index;

		}

		@Override
		public int getCount() {
			count = mList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public final int[] colors = new int[] { 0x3000000, 0x300010ff };

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			final String iIssueNo = (String) mList.get(position).get(mIndex[3]);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.win_lot_high_frequency_item, null);
				holder = new ViewHolder();
				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.imgView = (ImageView) convertView
						.findViewById(R.id.notice_prizes_single_specific_img);
				holder.imgView.setVisibility(ImageView.GONE);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (lotType == LISTSSC) {
				if (Lotno.equals(Constants.LOTNO_SSC)) {
					iNumbers = PublicMethod.formatSSCNum(iNumbers, 1);
				} else {
					iNumbers = PublicMethod.formatNum(iNumbers, 1);
				}
			} else if (lotType == LISTTEN) {
				/** add by pengcx 20130802 start */
				int width = PublicMethod.getDisplayWidth(context);
				float scale = 480.0f / width;
				float textSize = 11 * scale;
				holder.date.setTextSize(PublicMethod
						.getPxInt(textSize, context));
				iNumbers = PublicMethod.formatNum(iNumbers, 2);
				/** add by pengcx 20130802 end */
			}
			/** add by pengcx 20130808 start */
			else if (lotType == NMK3) {
				iNumbers = PublicMethod.formatNMK3Num(iNumbers, 2);
			}
			/** add by pengcx 20130808 end */
			else {
				iNumbers = PublicMethod.formatNum(iNumbers, 2);
			}
			holder.date.setText(iNumbers);
			holder.date.setTextColor(Color.RED);
			holder.issue.setText("第" + iIssueNo + "期");
			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView date;
			TextView issue;
			ImageView imgView;
		}
	}

	/**
	 * 子列表适配器
	 */
	public class SubEfficientAdapter extends BaseAdapter {
		int count = 0;
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Activity context;
		private boolean isImg = true;

		public SubEfficientAdapter(Activity context, String[] index,
				List<Map<String, Object>> list, boolean isImg) {
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			this.mList = list;
			this.mIndex = index;
			this.isImg = isImg;

		}

		@Override
		public int getCount() {
			count = mList.size();
			return count;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public final int[] colors = new int[] { 0x3000000, 0x300010ff };

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			final String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			final String iIssueNo = (String) mList.get(position).get(mIndex[3]);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.notice_prizes_single_specific_layout, null);
				holder = new ViewHolder();
				holder.numbers = (LinearLayout) convertView
						.findViewById(R.id.notice_pirzes_single_specific_ball_linearlayout);
				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.imgView = (ImageView) convertView
						.findViewById(R.id.notice_prizes_single_specific_img);
				if (isImg) {
					holder.imgView.setVisibility(ImageView.VISIBLE);
				} else {
					holder.imgView.setVisibility(ImageView.GONE);
				}

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (lotType == LIST3D) {
				String trycodestr = (String) mList.get(position).get("tryCode");
				holder.numbers.removeAllViews();
				holder.numbers.setGravity(Gravity.LEFT);
				holder.numbers.setPadding(50, 0, 0, 0);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					tempBallView = new OneBallView(context, 1);
					tempBallView.initBall(NoticeMainActivity.BALL_WIDTH,
							NoticeMainActivity.BALL_WIDTH, "" + iShowNumber,
							aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
				TextView trycode = new TextView(NoticeInfoActivity.this);
				trycode.setTextColor(Color.BLUE);
				trycode.setTextSize(17);
				trycode.setPadding(30, 10, 0, 0);
				String codeshow = "";
				if (!trycodestr.equals("")) {
					for (i1 = 0; i1 < 3; i1++) {
						codeshow += trycodestr
								.substring(i1 * 2 + 1, i1 * 2 + 2);
						if (i1 != 2) {
							codeshow += ",";
						}
					}
				}
				trycode.setText("试机号:" + codeshow);
				holder.numbers.addView(trycode);
			} else {
				PrizeBallLinearLayout linear = new PrizeBallLinearLayout(
						context);
				linear.Lotname = Lotno;
				linear.Batchcode = iNumbers;
				linear.linear = holder.numbers;
				linear.removeAllBalls();
				linear.initLinear();
			}
			holder.date.setText(iDate);
			holder.issue.setText("第" + iIssueNo + "期");
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isImg) {
						switch (lotType) {
						case LISTSSQ:
							SsqDetailView ssqDetailView = new SsqDetailView(
									context, Constants.LOTNO_SSQ, iIssueNo,
									progress, new Handler(), true);
							break;
						case LIST3D:
							FC3DetailView fc3DetailView = new FC3DetailView(
									context, Constants.LOTNO_FC3D, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTQLC:
							QLCDetailView qlcDetailView = new QLCDetailView(
									context, Constants.LOTNO_QLC, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTDLT:
							DLTDetailView dltDetailView = new DLTDetailView(
									context, Constants.LOTNO_DLT, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTPL3:
							PL3DetailView pl3DetailView = new PL3DetailView(
									context, Constants.LOTNO_PL3, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTPL5:
							PL5DetailView pl5DetailView = new PL5DetailView(
									context, Constants.LOTNO_PL5, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTQXC:
							QXCDetailView qxcDetailView = new QXCDetailView(
									context, Constants.LOTNO_QXC, iIssueNo,
									progress, new Handler(), true);
							break;
						case LISTTWENTY:
							TwentyDetailView twentview = new TwentyDetailView(
									context, Constants.LOTNO_22_5, iIssueNo,
									progress, new Handler(), true);
							break;
						case ZC:
							ZCDetailView zcview = new ZCDetailView(context,
									Lotno, iIssueNo, progress, new Handler(),
									true);
							break;
						}
					}

				}
			});
			return convertView;
		}

		class ViewHolder {
			LinearLayout numbers;
			TextView name;
			TextView date;
			TextView issue;
			ImageView imgView;
		}
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	private void netting() {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				totalItem = adapter.getCount();
				pageindex++;
				final JSONObject prizemore = PrizeInfoInterface.getInstance()
						.getNoticePrizeInfo(Lotno, pageindex + "", "50");
				if (prizemore != null) {
					try {
						final String msg = prizemore.getString("message");
						final String code = prizemore.getString("error_code");
						if (code.equals("0000")) {
							JsonToString(prizemore);
							tHandler.post(new Runnable() {
								@Override
								public void run() {
									progressbar
											.setVisibility(ProgressBar.INVISIBLE);
									addMoreView.setEnabled(true);
									adapter.notifyDataSetChanged();
								}
							});
						} else {
							tHandler.post(new Runnable() {
								@Override
								public void run() {
									progressbar
											.setVisibility(ProgressBar.INVISIBLE);
									addMoreView.setEnabled(true);
									Toast.makeText(NoticeInfoActivity.this,
											msg, Toast.LENGTH_SHORT).show();
								}
							});
						}
					} catch (JSONException e) {
						// TODO: handle exception
					}
				}
			}

		}).start();

	}

	private void JsonToString(JSONObject prizemore) throws JSONException {
		JSONArray prizeArray = prizemore.getJSONArray("result");
		for (int i = 0; i < prizeArray.length(); i++) {
			JSONObject prizeJson = (JSONObject) prizeArray.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, LotLalel);
			map.put(WINNINGNUM, prizeJson.getString("winCode"));
			map.put(DATE, "开奖日期： " + prizeJson.getString("openTime"));
			map.put(ISSUE, prizeJson.getString("batchCode"));
			if (lotType == LIST3D) {
				map.put("tryCode", prizeJson.getString("tryCode"));
			}
			adpterlist.add(map);
		}
	}

	private void getMore() {
		netting();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == OAuthV1AuthorizeWebView.RESULT_CODE) {
				// 从返回的Intent中获取验证码
				tenoAuth = (OAuthV1) data.getExtras().getSerializable("oauth");
				try {
					tenoAuth = OAuthV1Client.accessToken(tenoAuth);
					/*
					 * 注意：此时oauth中的Oauth_token和Oauth_token_secret将发生变化，用新获取到的
					 * 已授权的access_token和access_token_secret替换之前存储的未授权的request_token
					 * 和request_token_secret.
					 */
					tencent_token = tenoAuth.getOauthToken();
					tencent_access_token_secret = tenoAuth
							.getOauthTokenSecret();
					shellRW.putStringValue("tencent_token", tencent_token);
					shellRW.putStringValue("tencent_access_token_secret",
							tencent_access_token_secret);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(NoticeInfoActivity.this,
						TencentShareActivity.class);
				intent.putExtra("tencent", LotnoDetailView.shareString);
				intent.putExtra("oauth", tenoAuth);
				startActivity(intent);
			}
		}
	}
}
