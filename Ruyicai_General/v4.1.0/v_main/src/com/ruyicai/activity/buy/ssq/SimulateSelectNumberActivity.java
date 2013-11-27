package com.ruyicai.activity.buy.ssq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.miss.OrderDetails;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.ssq.SimulateSelectNumberView.Row;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.net.newtransaction.QueryLossValueInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class SimulateSelectNumberActivity extends Activity implements
		OnClickListener {
	private static final String TAG = "SimulateSelectNumberActivity";
	private static final String PROTOCOL_QUERYESUCCESS_FLAG = "0000";

	private static final int GET_PRIZEINFO_ERROR = 0;
	private static final int GET_LOSSVALUE_ERROR = 1;
	private static final int GET_PRIZEINFO_SUCCESS = 3;
	private static final int GET_LOSSVALUE_SUCCESS = 4;

	private static final int TOUZHU_BUTTON_FLAG = 5;

	private TextView selectedNumbersTextView;
	private Button betButton;

	private SimulateSelectNumberView simulateSelectNumberView;

	private List<PrizeInfo> prizeInfosList;
	private List<LossValue> lossValuesList;

	List<Integer> selectedRedBallList;
	List<Integer> selectedBlueBallList;

	// 获取历史开奖信息-第几页
	private int pageIndex = 1;
	// 获取历史开奖信息-每页显示的条数
	private int maxResult = 10;

	// 遗漏值查询参数-期数
	private int batchNum = 10;
	// 遗漏值查询参数-玩法
	private String sellWay = "F47104MV_X";
	// 遗漏值查询参数-彩种
	private String lotNo = Constants.LOTNO_SSQ;

	private RWSharedPreferences shellRW;

	private Message msg;
	private Bundle bundle;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			CharSequence msgString = (CharSequence) msg.getData().get("msg");

			switch (msg.arg1) {
			case GET_LOSSVALUE_ERROR:
				Toast.makeText(SimulateSelectNumberActivity.this,
						"遗漏值信息获取失败..." + msgString, Toast.LENGTH_LONG).show();
				break;
			case GET_LOSSVALUE_SUCCESS:
				simulateSelectNumberView.setLossValues(lossValuesList);
				break;

			}

			switch (msg.arg2) {
			case GET_PRIZEINFO_ERROR:
				Toast.makeText(SimulateSelectNumberActivity.this,
						"历史获奖信息获取失败..." + msgString, Toast.LENGTH_LONG).show();
				break;

			case GET_PRIZEINFO_SUCCESS:
				simulateSelectNumberView.setPrizeInfos(prizeInfosList);
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.simulate_selectnumber);

		initScreenShow();

		getDataFromInternet();
	}

	private void initScreenShow() {
		selectedNumbersTextView = (TextView) findViewById(R.id.simulate_textview_selectednumbers);
		selectedNumbersTextView
				.setMovementMethod(new ScrollingMovementMethod());

		betButton = (Button) findViewById(R.id.simulate_button_touzhu);
		betButton.setOnClickListener(this);

		simulateSelectNumberView = (SimulateSelectNumberView) findViewById(R.id.simulate_selectnumber_view);
		simulateSelectNumberView
				.setSelectedNumbersTextView(selectedNumbersTextView);
	}

	private void getDataFromInternet() {
		lossValuesList = new ArrayList<LossValue>();
		prizeInfosList = new ArrayList<PrizeInfo>();

		msg = new Message();
		bundle = new Bundle();

		new Thread(new Runnable() {
			public void run() {
				JSONObject lossValueJsonObject = getPrizeInfoFromInternet();
				analyzeJsonLossValue(lossValueJsonObject);

				JSONObject prizeInfoJsonObject = getLossValueFromIntent();
				analyzeJsonPrizeInfo(prizeInfoJsonObject);

				handler.sendMessage(msg);
			}
		}).start();
	}

	private JSONObject getLossValueFromIntent() {
		PrizeInfoInterface prizeInfoInterface = PrizeInfoInterface
				.getInstance();
		JSONObject prizeInfoJsonObject = prizeInfoInterface.getNoticePrizeInfo(
				Constants.LOTNO_SSQ, String.valueOf(pageIndex),
				String.valueOf(maxResult));

		return prizeInfoJsonObject;
	}

	private JSONObject getPrizeInfoFromInternet() {
		QueryLossValueInterface queryLossValueInterface = QueryLossValueInterface
				.getInstance();
		JSONObject lossValueJsonObject = queryLossValueInterface
				.lossValueQuery(sellWay, lotNo, String.valueOf(batchNum));

		return lossValueJsonObject;
	}

	private void analyzeJsonPrizeInfo(JSONObject prizeInfoJsonObject) {
		try {
			String messageString = prizeInfoJsonObject.getString("message");
			String errorString = prizeInfoJsonObject.getString("error_code");

			// 网络获取失败
			if (!errorString.equals(PROTOCOL_QUERYESUCCESS_FLAG)) {
				bundle.putString("msg", messageString);
				msg.setData(bundle);

				msg.arg2 = GET_PRIZEINFO_ERROR;
			}
			// 网络获取成功，解析网络
			else {
				JSONArray prizeInfoArray = prizeInfoJsonObject
						.getJSONArray("result");
				int length = prizeInfoArray.length();

				for (int i = 0; i < length; i++) {
					JSONObject jsonObject = (JSONObject) prizeInfoArray.get(i);

					String batchCode = jsonObject.getString("batchCode");
					String winCode = jsonObject.getString("winCode");
					String openTime = jsonObject.getString("openTime");

					PrizeInfo prizeInfo = new PrizeInfo(batchCode, winCode,
							openTime);
					prizeInfosList.add(prizeInfo);
				}

				msg.arg2 = GET_PRIZEINFO_SUCCESS;
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}
	}

	private void analyzeJsonLossValue(JSONObject lossValueJsonObject) {
		try {
			String messageString = lossValueJsonObject.getString("message");
			String errorString = lossValueJsonObject.getString("error_code");

			// 网络获取失败
			if (!errorString.equals(PROTOCOL_QUERYESUCCESS_FLAG)) {
				Message msg = new Message();

				Bundle bundle = new Bundle();
				bundle.putString("msg", messageString);
				msg.setData(bundle);

				msg.arg1 = GET_LOSSVALUE_ERROR;
			}
			// 网络获取成功,解析Json数据
			else {
				JSONArray lossValueArray = lossValueJsonObject
						.getJSONArray("result");
				int length = lossValueArray.length();

				for (int i = 0; i < length; i++) {
					JSONObject lossValueObject = (JSONObject) lossValueArray
							.get(i);

					int[] blues = new int[16];
					int[] reds = new int[33];
					String batchCode = null;

					JSONObject valueObject = lossValueObject
							.getJSONObject("value");

					JSONArray bluesArray = valueObject.getJSONArray("blue");
					int blueLength = bluesArray.length();
					blues = new int[blueLength];
					for (int l = 0; l < blueLength; l++) {
						blues[l] = bluesArray.getInt(l);
					}

					JSONArray redsArray = valueObject.getJSONArray("red");
					int redLength = redsArray.length();
					reds = new int[redLength];
					for (int k = 0; k < redLength; k++) {
						reds[k] = redsArray.getInt(k);
					}

					batchCode = lossValueObject.getString("batchCode");

					LossValue lossValue = new LossValue(reds, blues, batchCode);

					lossValuesList.add(lossValue);
				}

				msg.arg1 = GET_LOSSVALUE_SUCCESS;
			}

		} catch (JSONException e) {
			Log.i(TAG, e.toString());
		}
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();

		switch (Integer.valueOf(tag)) {
		case TOUZHU_BUTTON_FLAG:
			begainSimulateTouZhu();
			break;
		}
	}

	private void begainSimulateTouZhu() {
		if (!isLogin()) {
			Intent intent = new Intent(this, UserLogin.class);
			startActivityForResult(intent, 0);
		} else {
			selectedRedBallList = simulateSelectNumberView
					.getRedSelectedNumbers();
			selectedBlueBallList = simulateSelectNumberView
					.getSelectedBlueNumbers();

			int redSize = selectedRedBallList.size();
			int blueSize = selectedBlueBallList.size();

			if (isBetLegitimacy(redSize, blueSize)) {
				addViewAndTouZhu(redSize, blueSize);
			}
		}

	}

	private long caculateBetNums(int redSize, int blueSize) {
		long betNums = PublicMethod.zuhe(6, redSize)
				* PublicMethod.zuhe(1, blueSize);
		return betNums;
	}

	private boolean isBetLegitimacy(int redSize, int blueSize) {

		String promptString = null;
		boolean isTrue = true;

		if (redSize < 6 && blueSize < 1) {
			promptString = "请至少选择6个红球和1个蓝球";
			isTrue = false;
		} else if (redSize < 6) {
			promptString = "请选择至少6个红球";
			isTrue = false;
		} else if (blueSize < 1) {
			promptString = "请选择1个蓝球";
			isTrue = false;
		}

		if (!isTrue) {
			Toast.makeText(this, promptString, Toast.LENGTH_LONG).show();
		}

		return isTrue;
	}

	private void addViewAndTouZhu(int redSize, int blueSize) {
		long betNums = caculateBetNums(redSize, blueSize);

		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			BetAndGiftPojo betAndGiftPojo = new BetAndGiftPojo();

			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");
			String lotno = Constants.LOTNO_SSQ;

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");
			betAndGiftPojo.setBet_code("");
			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");

			AddViewMiss addViewMiss = new AddViewMiss(this);
			CodeInfoMiss codeInfo = addViewMiss.initCodeInfo(2, 1);
			codeInfo.setTouZhuCode(SsqZiZhiXuanCode.simulateZhuma(
					selectedRedBallList, selectedBlueBallList));
			codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
			codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
			codeInfo = setCodeInfoColor(codeInfo, selectedRedBallList,
					selectedBlueBallList);
			addViewMiss.addCodeInfo(codeInfo);

			ApplicationAddview app = (ApplicationAddview) getApplicationContext();
			app.setPojo(betAndGiftPojo);
			app.setAddviewmiss(addViewMiss);
			Intent intent = new Intent(SimulateSelectNumberActivity.this,
					OrderDetails.class);
			intent.putExtra("from", Constants.SEND_FROM_SIMULATE);
			startActivity(intent);
		}
	}

	private CodeInfoMiss setCodeInfoColor(CodeInfoMiss codeInfo, List<Integer> redList,
			List<Integer> blueList) {
		StringBuffer redString = new StringBuffer();
		StringBuffer blueString = new StringBuffer();

		int redSize = redList.size();
		int blueSize = blueList.size();

		for (int red = 0; red < redSize - 1; red++) {
			redString.append(integerToString(redList.get(red)) + ",");
		}

		redString.append(integerToString(redList.get(redSize - 1)));
		codeInfo.addAreaCode(redString.toString(), Color.RED);

		for (int blue = 0; blue < blueSize - 1; blue++) {
			blueString.append(integerToString(blueList.get(blue)) + ",");
		}

		blueString.append(integerToString(blueList.get(blueSize - 1)));
		codeInfo.addAreaCode(blueString.toString(), Color.BLUE);

		return codeInfo;
	}

	private String integerToString(Integer integer) {
		String result = null;
		if (integer < 10) {
			result = "0" + integer.toString();
		} else {
			result = integer.toString();
		}

		return result;
	}

	public void dialogExcessive(int maxNums) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SimulateSelectNumberActivity.this);
		builder.setTitle(SimulateSelectNumberActivity.this.getString(
				R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于" + maxNums + "注！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	private boolean isLogin() {
		boolean isLogin = true;

		shellRW = new RWSharedPreferences(this, "addInfo");
		String sessionidStr = shellRW.getStringValue("sessionid");
		if (sessionidStr == null || sessionidStr.equals("")) {
			isLogin = false;
		}

		return isLogin;
	}

	/**
	 * 历史开奖信息类
	 */
	class PrizeInfo {
		private String batchCode;

		private List<Integer> winRedCodes;
		private List<Integer> winBlueCodes;
		private static final int WINCODE_SIZE = 7;

		private String openTime;

		public String getBatchCode() {
			return batchCode;
		}

		public void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}

		public void setWinCodes(String winCode) {
			for (int i = 0; i < WINCODE_SIZE; i++) {
				String childCode = winCode.substring(i * 2, i * 2 + 2);
				if (i == WINCODE_SIZE - 1) {
					winBlueCodes.add(Integer.valueOf(childCode));
				} else {
					winRedCodes.add(Integer.valueOf(childCode));
				}

			}
		}

		public PrizeInfo(String batchCode, String winCode, String openTime) {
			super();
			this.batchCode = batchCode;
			this.openTime = openTime;

			winRedCodes = new ArrayList<Integer>();
			winBlueCodes = new ArrayList<Integer>();
			setWinCodes(winCode);
		}

		/**
		 * 获取指定列的开奖号码值：是开奖号码，返回该列值；不是开奖号码，返回-1
		 */
		public int getWinCodeByColum(int colum) {
			int value;

			// 如果是红球
			if (colum > 0 && colum <= Row.redColumNum) {
				value = colum;

				if (winRedCodes.contains(value)) {
					return value;
				}
			} else {
				value = colum - Row.redColumNum;
				if (winBlueCodes.contains(value)) {
					return value;
				}
			}

			return -1;
		}
	}

	/**
	 * 遗漏值类
	 */
	class LossValue {
		private int[] redBallLossValues;
		private int[] blueBallLossValues;

		private String batchCode;

		public int[] getRedBallLossValues() {
			return redBallLossValues;
		}

		public void setRedBallLossValues(int[] reds) {
			this.redBallLossValues = reds;
		}

		public int[] getBlueBallLossValues() {
			return blueBallLossValues;
		}

		public void setBlueBallLossValues(int[] blues) {
			this.blueBallLossValues = blues;
		}

		public LossValue(int[] reds, int[] blues, String batchCode) {
			super();
			this.redBallLossValues = reds;
			this.blueBallLossValues = blues;
			this.batchCode = batchCode;
		}

		/**
		 * 根据序列号获取红球遗漏值
		 */
		public int getRedLossNum(int index) {
			return redBallLossValues[index];
		}

		/**
		 * 根据序列号获取篮球遗漏值
		 */
		public int getBlueLossNum(int index) {
			return blueBallLossValues[index];
		}
	}
}
