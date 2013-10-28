package com.ruyicai.activity.buy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

public class HighFrequencyNoticeHistroyActivity extends Activity {
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	public ProgressDialog progress;
	public float SCALE = 1;
	public int BALL_WIDTH = 46;
	String LotLalel = "22-5";
	LayoutInflater mInflater;
	Button flush, ok;
	TextView noticePrizesTitle;
	ListView listview;
	String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
			FINALDATE, MONEYSUM };
	String lotno = Constants.LOTNO_22_5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.noticehistroy);
		setScale();
		flush = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		listview = (ListView) findViewById(R.id.noticehistroylist);
		getlotno();
		netting();
		flush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				netting();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	public void getlotno() {
		Bundle bundle = getIntent().getExtras();
		lotno = bundle.getString("lotno");
	}

	/**
	 * 根据手机屏幕设置球的大小和球的缩放比例
	 */
	private void setScale() {
		int screenWith = PublicMethod.getDisplayWidth(this);
		if (screenWith <= 240) {
			BALL_WIDTH = 46 * 120 / 240;
			SCALE = (float) 140 / 240;
		} else if (screenWith > 240 && screenWith <= 320) {
			BALL_WIDTH = 46 * 160 / 240;
			SCALE = (float) 180 / 240;
		} else if (screenWith == 480) {
			BALL_WIDTH = 46;
			SCALE = 1;
		} else if (screenWith > 480) {
			BALL_WIDTH = 80;
			SCALE = (float) 1.5;
		}
	}

	private void netting() {
		final Handler tHandler = new Handler();
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String errorMessageString = "";
				String msg = "";
				String code = "";
				do {
					final JSONObject prizemore = PrizeInfoInterface
							.getInstance()
							.getNoticePrizeInfo(lotno, "1", "100");
					if (prizemore == null) {
						errorMessageString = "数据获取错误";
						break;
					}
					try {
						msg = prizemore.getString("message");
						code = prizemore.getString("error_code");
						if (code.equals("0000")) {
							final List<Map<String, Object>> list = JsonToString(prizemore);
							tHandler.post(new Runnable() {
								@Override
								public void run() {
									SubEfficientAdapter adapter = new SubEfficientAdapter(
											HighFrequencyNoticeHistroyActivity.this,
											str, list);
									listview.setAdapter(adapter);
								}
							});
							errorMessageString = "查询成功";
						} else {
							errorMessageString = msg.toString();
						}
					} catch (JSONException e) {
						errorMessageString = "未知错误";
						break;
					}
				} while (false);
				final String message = errorMessageString;
				tHandler.post(new Runnable() {
					public void run() {
						dialog.dismiss();
						Toast.makeText(HighFrequencyNoticeHistroyActivity.this,
								message, Toast.LENGTH_SHORT).show();
					}
				});
			}
		}).start();
	}

	public String getLotLalel() {
		if (lotno.equals(Constants.LOTNO_SSQ)) {
			LotLalel = "ssq";
		} else if (lotno.equals(Constants.LOTNO_FC3D)) {
			LotLalel = "fc3d";
		} else if (lotno.equals(Constants.LOTNO_11_5)) {
			LotLalel = "11-5";
		} else if (lotno.equals(Constants.LOTNO_22_5)) {
			LotLalel = "22-5";
		} else if (lotno.equals(Constants.LOTNO_DLT)) {
			LotLalel = "cjdlt";
		} else if (lotno.equals(Constants.LOTNO_eleven)) {
			LotLalel = "11-ydj";
		} else if (lotno.equals(Constants.LOTNO_PL3)) {
			LotLalel = "pl3";
		} else if (lotno.equals(Constants.LOTNO_PL5)) {
			LotLalel = "pl5";
		} else if (lotno.equals(Constants.LOTNO_QLC)) {
			LotLalel = "qlc";
		} else if (lotno.equals(Constants.LOTNO_QXC)) {
			LotLalel = "qxc";
		} else if (lotno.equals(Constants.LOTNO_JQC)) {
			LotLalel = "jqc";
		} else if (lotno.equals(Constants.LOTNO_RX9)) {
			LotLalel = "rxj";
		} else if (lotno.equals(Constants.LOTNO_LCB)) {
			LotLalel = "lcb";
		} else if (lotno.equals(Constants.LOTNO_SFC)) {
			LotLalel = "sfc";
		} else if (lotno.equals(Constants.LOTNO_SSC)) {
			LotLalel = "ssc";
		} else if (lotno.equals(Constants.LOTNO_SSQ)) {
			LotLalel = "ssq";
		}
		return LotLalel;

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

		public SubEfficientAdapter(Activity context, String[] index,
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

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (lotno.equals(Constants.LOTNO_SSC)) {
				iNumbers = PublicMethod.formatNum(iNumbers, 1);
			} else if (lotno.equals(Constants.LOTNO_ten)) {
				if (PublicMethod.getDisplayWidth(context) == 240) {
//					holder.date.setTextSize(PublicMethod.getPxInt(15, context));
					iNumbers = PublicMethod.formatNum(iNumbers, 2);
				} else {
//					holder.date.setTextSize(PublicMethod.getPxInt(9, context));
					iNumbers = PublicMethod.formatNum(iNumbers, 2);
				}
			} else {
				iNumbers = PublicMethod.formatNum(iNumbers, 2);
			}
			holder.imgView.setVisibility(ImageView.INVISIBLE);
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

	private List<Map<String, Object>> JsonToString(JSONObject prizemore)
			throws JSONException {
		List<Map<String, Object>> adpterlist = new ArrayList<Map<String, Object>>();
		JSONArray prizeArray = prizemore.getJSONArray("result");

		for (int i = 0; i < prizeArray.length(); i++) {
			JSONObject prizeJson = (JSONObject) prizeArray.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(LOTTERYTYPE, getLotLalel());
			map.put(WINNINGNUM, prizeJson.getString("winCode"));
			map.put(DATE, "开奖日期:" + prizeJson.getString("openTime"));
			map.put(ISSUE, prizeJson.getString("batchCode"));
			adpterlist.add(map);

		}
		return adpterlist;
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
